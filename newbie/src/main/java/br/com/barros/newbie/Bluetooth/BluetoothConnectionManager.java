package br.com.barros.newbie.Bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by thiago on 21/04/15.
 */
public class BluetoothConnectionManager extends Thread{

    private final BluetoothDevice device;
    private BluetoothSocket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private Handler handler;

    private static final String LOG_TAG = BluetoothConnectionManager.class.getSimpleName();
    private static BluetoothConnectionManager _instance;
    private static BluetoothStatus STATUS = BluetoothStatus.NONE;

    public BluetoothConnectionManager(BluetoothDevice device, BluetoothSocket socket, Handler handler){
        this.socket = socket;
        this.device = device;
        this.handler = handler;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try{
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        }catch (IOException e){}

        inputStream = tmpIn;
        outputStream = tmpOut;
        _instance = this;
    }

    public void run() {
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()
        setName(LOG_TAG);
        Log.i(LOG_TAG, "Communication with the device ["+device.getName() +"] is started");
        STATUS = BluetoothStatus.IN_COMMUNICATION;
        // Keep listening to the InputStream until an exception occurs
        while (!isInterrupted()) {
            try {
                // Read from the InputStream
                Log.i(LOG_TAG, "Waiting data...");
                bytes = inputStream.read(buffer);
                Log.i(LOG_TAG, "Data received ["+ new String(buffer, "ISO-8859-1") +"]");
                // Send the obtained bytes to the UI activity
                if (handler != null) {
                    handler.obtainMessage(BluetoothStatus.READ.getId(),
                                            bytes, -1, buffer).sendToTarget();
                }
            } catch (IOException e) {
                break;
            }
        }
    }

    public void write(byte[] bytes) {
        try {
            Log.d(LOG_TAG, "Writing data[" + new String(bytes, "ISO-8859-1") + "]");
            outputStream.write(bytes);
        } catch (IOException e) { }
    }

    public void cancel() {
        try {
            socket.close();
            STATUS = BluetoothStatus.NONE;
            Log.i(LOG_TAG, "Communication with the device ["+device.getName()+"] has been closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean inCommunication(){
        return STATUS == BluetoothStatus.IN_COMMUNICATION;
    }

    public BluetoothDevice getDevice(){
        return device;
    }

    public void setHandler(Handler handler){
        this.handler = handler;
    }

    public static BluetoothConnectionManager getInstance(){
        return _instance;
    }

}