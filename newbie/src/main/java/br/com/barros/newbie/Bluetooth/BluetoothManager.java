package br.com.barros.newbie.Bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.thiago.newbie.R;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import br.com.barros.newbie.Bluetooth.Exceptions.BluetoothConnectionManager;
import br.com.barros.newbie.Bluetooth.Exceptions.BluetoothException;

/**
 * Created by thiagobarros on 05/04/15.
 */
public class BluetoothManager {
    private static final UUID uuid = UUID.fromString("0bed0288-dfbf-4557-9699-0929daa7c2eb");
    private static final String LOG_TAG = BluetoothManager.class.getSimpleName();

    private Handler handler;
    private Set<BluetoothDevice> devicesFound;

    private BluetoothAdapter defaultAdapter;
    private BluetoothReceiver defaultReceiver;

    private Activity activity;

    private static final int TEMPO_DE_DESCOBERTA = 30;
    private static final int VISIVEL = 1;

    private BluetoothStatus bluetoothStatus = BluetoothStatus.NONE;

    private BluetoothAccept acceptSocketThread = null;
    private BluetoothConnect bluetoothConnectThread = null;

    private BluetoothConnectionManager bluetoothConnectionManager;

    public BluetoothManager(Activity activity) throws BluetoothException {
        defaultAdapter = BluetoothAdapter.getDefaultAdapter();

        if (defaultAdapter == null)
            throw new BluetoothException(activity.getString(R.string.adapter_bluetooth_not_found));

        Log.d(LOG_TAG, "Adaptador bluetooth: " + defaultAdapter.getName() +
                " no endereço: " + defaultAdapter.getAddress());

        defaultReceiver = new BluetoothReceiver();
        devicesFound = new HashSet<>();

        this.activity = activity;
    }

    public void enableBluetooth() {
        if (!defaultAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableIntent, BluetoothStatus.ENABLED.getId());
        }

        IntentFilter filterActionFound = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        IntentFilter filterDiscoveryFinished = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        activity.registerReceiver(defaultReceiver, filterActionFound);
        activity.registerReceiver(defaultReceiver, filterDiscoveryFinished);
    }

    public void disableBluetooth() {
        activity.unregisterReceiver(defaultReceiver);
        defaultAdapter.disable();
    }

    public void startDiscovery(Handler handler) {
        devicesFound.clear();
        defaultReceiver.setHandler(handler);
        defaultReceiver.clearListDevicesFound();

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, TEMPO_DE_DESCOBERTA);
        activity.startActivity(discoverableIntent);

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
        return allDevices;
    }

    public BluetoothConnectionManager getBluetoothConnectionManager(Handler handler){
        if (bluetoothConnectThread.getSocket() != null) {
            bluetoothConnectionManager = new BluetoothConnectionManager(
                    bluetoothConnectThread.getDevice(), bluetoothConnectThread.getSocket(), handler);
        }

        return bluetoothConnectionManager;
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
