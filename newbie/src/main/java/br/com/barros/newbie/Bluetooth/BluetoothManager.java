package br.com.barros.newbie.Bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import br.com.barros.newbie.Bluetooth.Exceptions.BluetoothConnectionManager;
import br.com.barros.newbie.Bluetooth.Exceptions.BluetoothException;

/**
 * Created by thiagobarros on 05/04/15.
 */
public class BluetoothManager{
    private static final UUID uuid = UUID.fromString("0BED0288-DFBF-4557-9699-0929DAA7C2EB");
    private static final String LOG_TAG = BluetoothManager.class.getSimpleName();

    private Handler handler;
    private Activity activity;
    private BluetoothAdapter defaultAdapter;
    private BluetoothReceiver defaultReceiver;
    private Set<BluetoothDevice> devicesFound;

    private BluetoothAccept acceptSocketThread = null;
    private BluetoothConnect bluetoothConnectThread = null;
    private BluetoothConnectionManager bluetoothConnectionManager;
    private BluetoothStatus bluetoothStatus = BluetoothStatus.NONE;

    private static BluetoothManager _instance;

    private BluetoothManager(Activity activity) throws BluetoothException {
        defaultAdapter = BluetoothAdapter.getDefaultAdapter();

        if (defaultAdapter == null)
            throw new BluetoothException("Não foi encontrado nenhum adaptador Bluetooth no dispositivo.");

        Log.d(LOG_TAG, "Adaptador bluetooth: " + defaultAdapter.getName() +
                " no endereço: " + defaultAdapter.getAddress());

        defaultReceiver = new BluetoothReceiver();
        devicesFound = new HashSet<>();

        this.activity = activity;
        _instance = this;
    }

    public void enableBluetooth() {
        if (!defaultAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableIntent, BluetoothStatus.ENABLED.getId());
        }
    }

    public void disableBluetooth() {
        defaultAdapter.disable();
    }

    public void destroy(){
        activity.unregisterReceiver(defaultReceiver);
    }

    public boolean isEnabledBluetooth(){
        return defaultAdapter.isEnabled();
    }

    public void startDiscovery(Handler handler) {
        devicesFound.clear();

        IntentFilter filterActionFound = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        IntentFilter filterDiscoveryFinished = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        boolean registerReceiverHasError = false;

        do {
            try {
                activity.registerReceiver(defaultReceiver, filterActionFound);
                activity.registerReceiver(defaultReceiver, filterDiscoveryFinished);
            } catch (Exception e) {
                registerReceiverHasError = true;
                Log.d(LOG_TAG, "RegisterReciver error, unregister now, retrying...");
                activity.unregisterReceiver(defaultReceiver);
            }
        }while (registerReceiverHasError);

        defaultReceiver.setHandler(handler);
        defaultReceiver.clearListDevicesFound();

        defaultAdapter.startDiscovery();
        bluetoothStatus = BluetoothStatus.DISCOVERY;

        Log.d(LOG_TAG, "Discovery start...");

        Message message = new Message();
        message.what = BluetoothStatus.DISCOVERY.getId();
        handler.sendMessage(message);
    }

    public void acceptConnection(Handler handler) {
        acceptSocketThread = null;
        acceptSocketThread = new BluetoothAccept(defaultAdapter, uuid, handler);
        acceptSocketThread.start();
    }

    public void connect(String address, Handler handler){
        BluetoothDevice device = defaultAdapter.getRemoteDevice(address);
        connect(device, handler);
    }

    public void connect(BluetoothDevice device, Handler handler) {
        bluetoothConnectThread = null;
        bluetoothConnectThread = new BluetoothConnect(defaultAdapter, device, uuid, handler);
        bluetoothConnectThread.start();
    }

    public void disconnect() {
        if (bluetoothConnectThread.isConnected()) {
            bluetoothConnectThread.cancel();
            bluetoothConnectThread = null;
        }

//        try {
//            _instance = null;
//            _instance = new BluetoothManager(activity);
//        } catch (BluetoothException e) {
//            e.printStackTrace();
//        }
    }

    public boolean isConnected(){
        return (bluetoothConnectThread != null && bluetoothConnectThread.isConnected());
    }

    public BluetoothDevice getDeviceConnected(){
        if (bluetoothConnectThread.isConnected())
            return bluetoothConnectThread.getDevice();

        //TODO - Deve retornar um adaptador default;
        return null;
    }

    public Set<BluetoothDevice> getDevicesPaired() {
        return defaultAdapter.getBondedDevices();
    }

    public Collection<BluetoothDevice> getDevicesFound() {
        if (devicesFound.isEmpty()) {
            devicesFound.addAll(defaultAdapter.getBondedDevices());
            devicesFound.addAll(defaultReceiver.getDevicesFound());
        }

        return Collections.unmodifiableCollection(devicesFound);
    }

    public void initCommunication(){
        if (bluetoothConnectionManager == null)
            bluetoothConnectionManager = new BluetoothConnectionManager(bluetoothConnectThread.getDevice(),
                                                                        bluetoothConnectThread.getSocket());

        if (!bluetoothConnectionManager.inCommunication())
            bluetoothConnectionManager.start();
    }

    public void stopCommunication(){
        if (bluetoothConnectionManager != null) {
            bluetoothConnectionManager.cancel();
            bluetoothConnectionManager = null;
        }
    }

    public boolean inCommunication(){
        return (bluetoothConnectionManager != null && bluetoothConnectionManager.inCommunication());
    }

    private void setActivity(Activity activity){
        this.activity = activity;
    }

    public static BluetoothManager getInstance(Activity activity) throws BluetoothException {
        if (_instance == null){
            _instance = new BluetoothManager(activity);
        }else
            _instance.setActivity(activity);

        return _instance;
    }

    public static  BluetoothManager getInstance(){
        return _instance;
    }

}
