package br.com.barros.newbie.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by thiagobarros on 06/04/15.
 */
public class BluetoothConnect extends Thread {
    private final BluetoothSocket socket;
    private final BluetoothAdapter adapter;
    private final BluetoothDevice device;
    private final Handler handler;
    private BluetoothStatus bluetoothStatus = BluetoothStatus.NOT_CONNECTED;

    public BluetoothConnect(BluetoothAdapter adapter, BluetoothDevice device, UUID uuid, Handler handler) {
        this.device = device;
        this.adapter = adapter;
        this.handler = handler;
        BluetoothSocket tmp = null;
        try {
            tmp = device.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
        }

        this.socket = tmp;
    }

    public void run() {
        adapter.cancelDiscovery();
        try {
            socket.connect();
            bluetoothStatus = BluetoothStatus.CONNECTED;
        } catch (IOException connectionE) {
            try {
                socket.close();
            } catch (IOException closeE) {
            }
        }

        Message message = new Message();
        message.what = BluetoothStatus.CONNECTED.getId();
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
