package br.com.barros.newbie.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by thiago on 06/04/15.
 */
public class BluetoothAccept extends Thread {
    private final BluetoothServerSocket severSocket;
    private final UUID uuid;
    private final Handler handler;

    private BluetoothSocket socket;

    private static final String TAG_NAME = BluetoothManager.class.getName();

   public BluetoothAccept(BluetoothAdapter adapter, UUID uuid, Handler handler) {
       this.uuid = uuid;
       this.handler = handler;
       BluetoothServerSocket tmp = null;
       try {
           tmp = adapter.listenUsingInsecureRfcommWithServiceRecord(TAG_NAME, uuid);
       } catch (IOException e) { }

       this.severSocket = tmp;
   }

   public void run() {
       BluetoothSocket socket = null;
       while (true) {
           try {
               socket = severSocket.accept();
           } catch (IOException e) {
               break;
           }
           if (socket != null) {
               Message message = new Message();
               message.what = BluetoothStatus.ACCEPT.getId();
               handler.sendMessage(message);
               cancel();
               break;
           }
       }
   }

   public void cancel() {
       try {
           severSocket.close();
       } catch (IOException e) {
       }
   }

    public BluetoothSocket getSocket(){
        return socket;
    }
}
