package br.com.thgbarros.bluetoothconnetion.communication.parser.elm;

import java.util.Arrays;

import br.com.thgbarros.bluetoothconnetion.communication.ProtocolParser;

/**
 * Created by thiago on 23/05/15.
 */
public class ElmEchoParser extends ElmParser {
    private final byte[] echo = {0x41, 0x54}; //AT
    private byte[] value; ;

    @Override
    public void clear() {
        super.clear();
        this.value = new byte[0];
    }

    @Override
    public byte[] getData() {
        byte[] result = new byte[0];
        if (dataValid)
            copy(result, value, super.getData());

        return result;
    }

    @Override
    public byte[] parse(byte[] request, byte[] response) {
        byte[] result = new byte[0];

        if (dataValid)
            return getData();

        dataValid = (response[0] == echo[0]) && (response[1]==echo[1]);
        value = new byte[]{response[0], response[1]};

        if (!dataValid)
            return getData();

        return super.parse(request, response);
    }
}
