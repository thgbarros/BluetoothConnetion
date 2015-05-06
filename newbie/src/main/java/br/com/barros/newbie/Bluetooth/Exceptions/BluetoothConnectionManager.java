package br.com.barros.newbie.Bluetooth.Exceptions;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import br.com.barros.newbie.Bluetooth.BluetoothStatus;

/**
 * Created by thiago on 21/04/15.
 */
public class BluetoothConnectionManager extends Thread{

    private final BluetoothDevice device;
    private final BluetoothSocket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private Handler handler;

    private static final String LOG_TAG = BluetoothConnectionManager.class.getSimpleName();
    private static BluetoothConnectionManager _instance;
    public static BluetoothStatus STATUS = BluetoothStatus.NONE;

    private BluetoothConnectionManager(BluetoothDevice device, BluetoothSocket socket){
        this.socket = socket;
        this.device = device;
        this.handler = null;
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

        Log.i(LOG_TAG, "Communication with the device ["+device.getName() +"] is started");
        STATUS = BluetoothStatus.IN_COMMUNICATION;
        // Keep listening to the InputStream until an exception occurs
        while (!isInterrupted()) {
            try {
                // Read from the InputStream
                bytes = inputStream.read(buffer);
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
            outputStream.write(bytes);
        } catch (IOException e) { }
    }

    public void cancel() {
        try {
            socket.close();
            Log.i(LOG_TAG, "Communication with the device ["+device.getName()+"] has been closed");
        } catch (IOException e) { }
    }

    public BluetoothDevice getDevice(){
        return device;
    }

    public void setHandler(Handler handler){
        this.handler = handler;
    }

    public static BluetoothConnectionManager getInstance(BluetoothDevice device, BluetoothSocket socket){
        if (_instance == null)
            _instance = new BluetoothConnectionManager(device, socket);
        return _instance;
    }

    public static BluetoothConnectionManager getInstance(){
        return _instance;
    }

}
