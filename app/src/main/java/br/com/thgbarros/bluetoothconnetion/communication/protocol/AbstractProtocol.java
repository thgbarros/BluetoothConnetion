package br.com.thgbarros.bluetoothconnetion.communication.protocol;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.barros.newbie.Bluetooth.BluetoothManager;
import br.com.thgbarros.bluetoothconnetion.communication.Protocol;

/**
 * Created by thiago on 20/05/15.
 */
public abstract class AbstractProtocol extends Thread implements Protocol {

    private static final String LOG_TAG = AbstractProtocol.class.getSimpleName();
    private Handler handler;
    private List<byte[]> requestList;
    private BluetoothManager bluetoothManager;

    protected boolean waitResponse = false;

    public AbstractProtocol(Handler handler, BluetoothManager bluetoothManager){
        this.handler = handler;
        this.bluetoothManager = bluetoothManager;
        this.requestList = new ArrayList<>();
        start();
    }

    public void setHandler(Handler handler){
        this.handler = handler;
    }

    public Handler getHandler(){
        return handler;
    }

    @Override
    public void run() {
        Log.d(LOG_TAG, "Init thread to send data.");
        while (!interrupted()){
            if (bluetoothManager.inCommunication() && (requestList != null && requestList.size() > 0 && !waitResponse)) {
                bluetoothManager.send(requestList.get(0)); //Envia sempre a primeira posição da lista
                waitResponse = true;
            }
        }
    }

    @Override
    public synchronized void request(byte ... dataToSend){
        requestList.add(dataToSend);
        Log.d(LOG_TAG, "Data request" + Arrays.toString(dataToSend));
    }

    protected synchronized void nextRequest(){
        waitResponse = false;
    }

    protected byte[] getActualRequest(){
        return requestList.get(0);
    }

}
