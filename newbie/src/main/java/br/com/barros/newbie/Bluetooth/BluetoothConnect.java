package br.com.barros.newbie.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by thiagobarros on 06/04/15.
 */
public class BluetoothConnect extends Thread {
    private BluetoothSocket socket;
    private final BluetoothAdapter adapter;
    private final BluetoothDevice device;
    private final Handler handler;
    private BluetoothStatus bluetoothStatus = BluetoothStatus.NOT_CONNECTED;
    private final UUID uuid;

    private static final String LOG_TAG = BluetoothConnect.class.getSimpleName();

    public BluetoothConnect(BluetoothAdapter adapter, BluetoothDevice device, UUID uuid, Handler handler) {
        this.device = device;
        this.adapter = adapter;
        this.handler = handler;
        this.uuid = uuid;
        BluetoothSocket tmp = null;
        try {
            Log.d(LOG_TAG, "CreateRfcommSocketToServiceRecord on UUID["+ uuid.toString() + "]");
            tmp = this.device.createRfcommSocketToServiceRecord(this.uuid);
        } catch (IOException e) {
            Log.d(LOG_TAG, "CreateRfcommSocketToServiceRecord ERROR["+ e.getMessage() + "]");
        }

        this.socket = tmp;
    }

    public void run() {
        Log.d(LOG_TAG, "Trying socket connect...");
        adapter.cancelDiscovery();
        try {
            socket.connect();
            Log.d(LOG_TAG, "Socket connected");
            bluetoothStatus = BluetoothStatus.CONNECTED;
        } catch (IOException connectionE) {
            try {
                bluetoothStatus = BluetoothStatus.NOT_CONNECTED;
                Log.d(LOG_TAG, "Socket not connected [" + connectionE.getMessage() + "]");
                socket.close();
            } catch (IOException closeE) {
                Log.d(LOG_TAG, "Socket not closed [" + closeE.getMessage() + "]");
            }
        }

        Message message = new Message();
        message.what = bluetoothStatus.getId();
        handler.sendMessage(message);
    }

    public void cancel() {
        try {
            socket.close();
            bluetoothStatus = BluetoothStatus.NOT_CONNECTED;
        } catch (IOException e) {
        }
    }

    public BluetoothSocket getSocket() {
        return socket;
    }

    public BluetoothDevice getDevice(){ return device; }

}
