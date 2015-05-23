package br.com.thgbarros.bluetoothconnetion.communication.command;

import android.os.Handler;
import android.os.Message;

import br.com.barros.newbie.Bluetooth.BluetoothManager;
import br.com.thgbarros.bluetoothconnetion.communication.Command;
import br.com.thgbarros.bluetoothconnetion.communication.model.Read;
import br.com.thgbarros.bluetoothconnetion.communication.model.Trouble;
import br.com.thgbarros.bluetoothconnetion.communication.protocol.ElmProtocol;

/**
 * Created by thiago on 23/05/15.
 */
public class ElmCommand implements Command {

    private ElmProtocol protocol;
    private Handler interfaceHandler;
    private int attempts = 5;
    private int currentAttempt;
    private BluetoothManager bluetoothManager;
    private Handler internalHandler; //Somente para controle do getDefaultHandler;

    public ElmCommand(Handler interfaceHandler, BluetoothManager bluetoothManager){
        this.interfaceHandler = interfaceHandler;
        this.bluetoothManager = bluetoothManager;
        protocol = new ElmProtocol(getDefaultHandler(), bluetoothManager);
    }

    @Override
    public void startCommunication() {
        bluetoothManager.initCommunication(getDefaultHandler());
        protocol.request((byte) 0x5A); //Z
    }

    @Override
    public void stopCommunication() {

    }

    @Override
    public boolean inCommunication() {
        return false;
    }

    @Override
    public void starRead() {

    }

    @Override
    public void startReadTrouble() {

    }

    @Override
    public Read getRead() {
        return null;
    }

    @Override
    public Trouble getTrouble() {
        return null;
    }

    public void setAttempts(int value){
        if (value < 0)
            new IllegalArgumentException("Attempts may not be lass than zero.");

        this.attempts = value;
    }

    public void parseResponse(byte[] response){
        byte[] dataParsed = protocol.parse(response);

        if (dataParsed.length > 0){
            Message message = new Message();
            message.what = SUCCESS_COMMAND;
            interfaceHandler.sendMessage(message);
            currentAttempt = 0;
            return;
        }

        if (currentAttempt < attempts) {
            protocol.retry();
            return;
        }

    }

    public Handler getDefaultHandler(){
        if (internalHandler == null) {
            internalHandler = new Handler() {
                public void handleMessage(Message msg) {
                    if (msg.what == protocol.COMMAND_RESPONSE) {
                        byte[] response = (byte[]) msg.obj;
                        parseResponse(response);
                    }
                }
            };
        }

        return internalHandler;
    }

}
