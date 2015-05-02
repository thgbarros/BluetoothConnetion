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
    private static final UUID uuid = UUID.fromString("0bed0288-dfbf-4557-9699-0929daa7c2eb");
    private static final String LOG_TAG = BluetoothManager.class.getSimpleName();

    private Handler handler;
    private Set<BluetoothDevice> devicesFound;

    private BluetoothAdapter defaultAdapter;
    private BluetoothReceiver defaultReceiver;

    private Activity activity;

    private static final int TEMPO_DE_DESCOBERTA = 30;

    private BluetoothStatus bluetoothStatus = BluetoothStatus.NONE;

    private BluetoothAccept acceptSocketThread = null;
    private BluetoothConnect bluetoothConnectThread = null;

    private BluetoothConnectionManager bluetoothConnectionManager;

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
    }

    /**
     * Habilita o bluetooth do dispositivo se o
     * mesmo não estiver habilitado.
     */
    public void enableBluetooth() {
        if (!defaultAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableIntent, BluetoothStatus.ENABLED.getId());
        }
    }

    public void disableBluetooth() {
        try {
            activity.unregisterReceiver(defaultReceiver);
        }catch(IllegalArgumentException e){
            Log.d(LOG_TAG, e.getMessage());
        }
        defaultAdapter.disable();
    }

    public boolean isEnabledBluetooth(){
        return defaultAdapter.isEnabled();
    }

    public void startDiscovery(Handler handler) {
        devicesFound.clear();

        IntentFilter filterActionFound = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        IntentFilter filterDiscoveryFinished = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        activity.registerReceiver(defaultReceiver, filterActionFound);
        activity.registerReceiver(defaultReceiver, filterDiscoveryFinished);

        defaultReceiver.setHandler(handler);
        defaultReceiver.clearListDevicesFound();

        //Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        //discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, TEMPO_DE_DESCOBERTA);
        //activity.startActivity(discoverableIntent);

        defaultAdapter.startDiscovery();
        bluetoothStatus = BluetoothStatus.DISCOVERY;

        Log.d(LOG_TAG, "Discovery start...");

        Message message = new Message();
        message.what = BluetoothStatus.DISCOVERY.getId();
        handler.sendMessage(message);
    }

    public void acceptConnection() {
        acceptSocketThread = new BluetoothAccept(defaultAdapter, uuid, null);
        acceptSocketThread.start();
    }

    public void connect(BluetoothDevice device, Handler handler) {
        bluetoothConnectThread = new BluetoothConnect(defaultAdapter, device, uuid, handler);
        bluetoothConnectThread.start();
    }

    public Set<BluetoothDevice> getDevicesPaired() {
        return defaultAdapter.getBondedDevices();
    }

    public Collection<BluetoothDevice> getDevicesFound() {
        Collection<BluetoothDevice> allDevices = new HashSet<>();
        allDevices.addAll(defaultAdapter.getBondedDevices());
        allDevices.addAll(defaultReceiver.getDevicesFound());

        return Collections.unmodifiableCollection(allDevices);
    }

    public BluetoothConnectionManager getBluetoothConnectionManager(Handler handler){
        if (bluetoothConnectThread.getSocket() != null) {
            bluetoothConnectionManager = new BluetoothConnectionManager(
                    bluetoothConnectThread.getDevice(), bluetoothConnectThread.getSocket(), handler);
        }

        return bluetoothConnectionManager;
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

//    public Handler getDefaultHandler(){
//        if (handler == null) {
//            handler = new Handler() {
//                @Override
//                public void handleMessage(Message msg) {
//                    updateThis(msg);
//                }
//            };
//        }
//
//        return handler;
//    }
//
//    private void updateThis(Message message){
//
//    }
}
