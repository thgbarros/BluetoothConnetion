package br.com.thgbarros.bluetoothconnetion.communication.elm;

import android.os.Handler;
import android.os.Message;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import br.com.barros.newbie.Bluetooth.BluetoothManager;
import br.com.barros.newbie.Bluetooth.BluetoothStatus;
import br.com.thgbarros.bluetoothconnetion.communication.AbstractProtocol;
import br.com.thgbarros.bluetoothconnetion.communication.Command;
import br.com.thgbarros.bluetoothconnetion.communication.Protocol;
import br.com.thgbarros.bluetoothconnetion.communication.model.Read;
import br.com.thgbarros.bluetoothconnetion.communication.model.Trouble;

import static br.com.barros.newbie.Bluetooth.BluetoothStatus.READ;
import static br.com.barros.newbie.Bluetooth.BluetoothStatus.getValueOf;

/**
 * Created by thiago on 20/05/15.
 */
public class ElmProtocol extends AbstractProtocol implements Command {
    private final int ACTUAL_COMMAND = 0;

    private final byte[] header = {0x41, 0x54};
    private byte[] data;
    private Protocol protocol;
    private int retry = 5;


    private Handler interfaceHandler;
    private List<String> commandList = new ArrayList<>();
    private ElmCommand command;

    private ElmProtocol(Handler interfaceHandler, Handler thisHandler, BluetoothManager bluetoothManager){
        super(thisHandler, bluetoothManager);
        this.interfaceHandler = interfaceHandler;
        commandList = new ArrayList<>();
    }

    @Override
    public void startCommunication() {
        super.startCommunication();
        command.add(new byte[]{0x41, 0x54});
        command.execute();

        commandList.add("AT@1\r"); //reseta ELM327
//        commandList.add("ATL0\r");//Habilita linefeeds
//        commandList.add("ATE1\r");//Habilita echo
//        commandList.add("ATH1\r");//habilita header
//        commandList.add("ATSP0\r");//Defini protocolo automático
//
//        commandList.add("0100\r");//Defini protocolo automático
//        commandList.add("ATDP\r");//Defini protocolo automático
//        commandList.add("0902\r");//Defini protocolo automático
//        commandList.add("0100\r");//Defini protocolo automático
//        commandList.add("0120\r");//Defini protocolo automático
//        commandList.add("020000\r");//Defini protocolo automático
//        commandList.add("022000\r");//Defini protocolo automático

        setDataToSend(commandList.get(ACTUAL_COMMAND).getBytes());
    }


    @Override
    public void execute() {
        byte[] flushed = copy(header, data);

        protocol.txRx(flushed);
    }

    @Override
    public Command retry(int value) {
        this.retry = value;
        return this;
    }

    @Override
    public void add(byte[] data) {
        this.data = copy(this.data, data);
    }

    private byte[] copy(byte[] dataA, byte[] dataB){
        byte[] dataCopy = new byte[dataA.length + dataB.length];
        System.arraycopy(dataA, 0, dataCopy, 0, dataA.length);
        System.arraycopy(dataB, 0, dataCopy, dataA.length, dataB.length);
        return dataCopy;
    }

    public static AbstractProtocol getInstance(Handler handler, BluetoothManager bluetoothManager){
        if (_instance == null)
            _instance = new ElmProtocol(handler,
                    AbstractProtocol.getDefaultHandler(), bluetoothManager);

        return _instance;
    }
}
