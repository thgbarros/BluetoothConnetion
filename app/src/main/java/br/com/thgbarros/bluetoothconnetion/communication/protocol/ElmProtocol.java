package br.com.thgbarros.bluetoothconnetion.communication.protocol;

import android.os.Handler;

import br.com.barros.newbie.Bluetooth.BluetoothManager;

/**
 * Created by thiago on 20/05/15.
 */
public class ElmProtocol extends AbstractProtocol {
    private final byte[] atCommand = {0x41, 0x54}; //AT

    public ElmProtocol(Handler responseHandler, BluetoothManager bluetoothManager){
        super(responseHandler, bluetoothManager);
    }

    @Override
    public void retry(){
        byte[] retry = {0x3C, 0x43, 0x52, 0x3E}; //<CR>
        request(retry);
    }

    @Override
    public synchronized void request(byte... dataToSend) {
        if (dataToSend.length == 0)
           throw new IllegalArgumentException("ELM Command length invalid.");

        byte[] data = new byte[(atCommand.length + dataToSend.length) + 1];
        data[0] = atCommand[0];
        data[1] = atCommand[1];
        int i = 2;

        for (byte dataSender: dataToSend)
            data[i++] = dataSender;

        data[i] = 0x0D; //\r - carriage return character

        super.request(data);
    }

    @Override
    public byte[] parse(byte[] response) {
        byte[] request = getActualRequest();

        return new byte[0];
    }


}
