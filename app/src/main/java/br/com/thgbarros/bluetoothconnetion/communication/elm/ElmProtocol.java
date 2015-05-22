package br.com.thgbarros.bluetoothconnetion.communication.elm;

import android.os.Handler;
import android.os.Message;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import br.com.barros.newbie.Bluetooth.BluetoothManager;
import br.com.barros.newbie.Bluetooth.BluetoothStatus;
import br.com.thgbarros.bluetoothconnetion.communication.AbstractProtocol;
import br.com.thgbarros.bluetoothconnetion.communication.model.Read;
import br.com.thgbarros.bluetoothconnetion.communication.model.Trouble;

import static br.com.barros.newbie.Bluetooth.BluetoothStatus.READ;
import static br.com.barros.newbie.Bluetooth.BluetoothStatus.getValueOf;

/**
 * Created by thiago on 20/05/15.
 */
public class ElmProtocol extends AbstractProtocol {

    private final int ACTUAL_COMMAND = 0;

    private Handler interfaceHandler;
    private List<String> commandList = new ArrayList<>();

    private ElmProtocol(Handler interfaceHandler, Handler thisHandler, BluetoothManager bluetoothManager){
        super(thisHandler, bluetoothManager);
        this.interfaceHandler = interfaceHandler;
        commandList = new ArrayList<>();
    }

    @Override
    public void startCommunication() {
        super.startCommunication();
        commandList.add("ATZ\r"); //reseta ELM327
        commandList.add("ATL0\r");//Habilita linefeeds
        commandList.add("ATE1\r");//Habilita echo
        commandList.add("ATH1\r");//habilita header
        commandList.add("ATSP0\r");//Defini protocolo automático

        commandList.add("0100\r");//Defini protocolo automático
        commandList.add("ATDP\r");//Defini protocolo automático
        commandList.add("0902\r");//Defini protocolo automático
        commandList.add("0100\r");//Defini protocolo automático
        commandList.add("0120\r");//Defini protocolo automático
        commandList.add("020000\r");//Defini protocolo automático
        commandList.add("022000\r");//Defini protocolo automático

        setDataToSend(commandList.get(ACTUAL_COMMAND).getBytes());
    }

    @Override
    public void stopCommunication() {
        super.stopCommunication();
    }

    @Override
    public boolean inCommunication() {
        return false;
    }

    @Override
    public void startReading() {

    }

    @Override
    public void startReadingTroubles() {

    }

    @Override
    public Read getRead() {
        return null;
    }

    @Override
    public Trouble getTrouble() {
        return null;
    }


    private void processMessageReceive(Message message){
        byte[] allData = (byte[]) message.obj;
        try {
            String data = new String(allData, "ISO-8859-1");
            if (data.contains(commandList.get(ACTUAL_COMMAND))){

                //todo - Criar uma estrutura que possa processar todo o protocolo e
                //todo - verificar qual Menssagem deve ser enviada a UI (ChainOfResposability)
                Message inCommunication = new Message();
                inCommunication.what = BluetoothStatus.IN_COMMUNICATION.getId();
                receiveMessage(inCommunication);

                commandList.remove(ACTUAL_COMMAND);

                if (!commandList.isEmpty())
                    setDataToSend(commandList.get(ACTUAL_COMMAND).getBytes());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    protected void receiveMessage(Message msg){
        if (getValueOf(msg.what) == READ){
            processMessageReceive(msg);
            return;
        }

        interfaceHandler.obtainMessage(msg.what,
                msg.arg1, msg.arg2, msg.obj).sendToTarget();
    }

    public static AbstractProtocol getInstance(Handler handler, BluetoothManager bluetoothManager){
        if (_instance == null)
            _instance = new ElmProtocol(handler,
                    AbstractProtocol.getDefaultHandler(), bluetoothManager);

        return _instance;
    }
}
