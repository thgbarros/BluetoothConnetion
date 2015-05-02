package br.com.barros.newbie.Bluetooth.Exceptions;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;

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

    private static BluetoothConnectionManager _instance;

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

        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                // Read from the InputStream
                bytes = inputStream.read(buffer);
                // Send the obtained bytes to the UI activity
                handler.obtainMessage(BluetoothStatus.READ.getId(), bytes, -1, buffer)
                        .sendToTarget();

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
        } catch (IOException e) { }
    }

    public BluetoothDevice getDevice(){
        return device;
    }

    private void setHandler(Handler handler){
        this.handler = handler;
    }

    public static BluetoothConnectionManager getInstance(Handler handler){
        if (_instance != null)
            _instance.setHandler(handler);
        return _instance;
    }

    public static BluetoothConnectionManager getInstance(){
        return _instance;
    }

}
