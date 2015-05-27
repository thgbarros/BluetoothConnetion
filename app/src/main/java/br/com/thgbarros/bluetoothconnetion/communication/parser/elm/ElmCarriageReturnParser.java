package br.com.thgbarros.bluetoothconnetion.communication.parser.elm;

import java.util.Arrays;

/**
 * Created by thiagobarros on 26/05/15.
 */
public class ElmCarriageReturnParser extends ElmParser {

    private final byte carriageReturn = 0x0D;
    private byte[] value;

    @Override
    public void clear() {
        super.clear();
        value = new byte[];
    }

    @Override
    public byte[] getData() {
        byte[] result = new byte[0];
        if (dataValid)
            copy(result, super.getData(), value);

        return result;
    }

    @Override
    public byte[] parse(byte[] request, byte[] response) {
        byte[] result = new byte[0];

        if (dataValid)
            return getData();

        int i = 0;
        while(response[i] == carriageReturn){

        }


        dataValid = (response[0] == echo[0]) && (response[1]==echo[1]);
        value = new byte[]{response[0], response[1]};

        if (!dataValid)
            return getData();

        return super.parse(request, response);
    }


}
