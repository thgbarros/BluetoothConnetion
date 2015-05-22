package br.com.thgbarros.bluetoothconnetion.communication.elm;

import br.com.thgbarros.bluetoothconnetion.communication.Command;
import br.com.thgbarros.bluetoothconnetion.communication.Protocol;

/**
 * Created by thiago on 22/05/15.
 */
public class ElmCommand implements Command {

    private final byte[] header = {0x41, 0x54};
    private byte[] data;
    private Protocol protocol;
    private int retry = 5;


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

}
