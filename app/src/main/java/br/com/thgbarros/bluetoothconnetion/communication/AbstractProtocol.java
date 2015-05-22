package br.com.thgbarros.bluetoothconnetion.communication;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import br.com.barros.newbie.Bluetooth.BluetoothManager;
import br.com.thgbarros.bluetoothconnetion.communication.elm.ElmProtocol;

/**
 * Created by thiago on 20/05/15.
 */
public abstract class AbstractProtocol extends Thread  implements Protocol {

    private static final String LOG_TAG = AbstractProtocol.class.getSimpleName();
    private Handler handler;
    private byte[] dataToSend;
    private BluetoothManager bluetoothManager;
    protected static AbstractProtocol _instance;

    //Thread para enviar e aguardar a resposta do serviço solicitado.
    //Thread de recebimento precisa saber por quanto tempo ela vai ficar esperando.
    private ProtocolSending sending;
    private ProtocolReceiver receiver;

    public AbstractProtocol(Handler handler, BluetoothManager bluetoothManager){
        this.handler = handler;
        this.bluetoothManager = bluetoothManager;
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
            if (bluetoothManager.inCommunication() && (dataToSend != null && dataToSend.length > 0)) {
                bluetoothManager.send(dataToSend);
                dataToSend = null;
            }
        }
    }

    public synchronized byte[] txRx(byte[] dataToSend){

        this.dataToSend = dataToSend;
        return null;
    }

    protected static Handler getDefaultHandler(){
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                _instance.receiveMessage(msg);
            }
        };
        return handler;
    }

    public void startCommunication() {
        bluetoothManager.initCommunication(getHandler());
    }
    public void stopCommunication() {
        bluetoothManager.stopCommunication();
    }

    protected abstract void receiveMessage(Message message);


}
