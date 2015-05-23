package br.com.thgbarros.bluetoothconnetion.communication.parser.elm;

import java.util.Arrays;

import br.com.thgbarros.bluetoothconnetion.communication.ProtocolParser;

/**
 * Created by thiago on 23/05/15.
 */
public class ElmEchoParser extends ElmParser {
    private byte[] echo;

    @Override
    public void clear() {
        super.clear();
        this.echo = new byte[0];
    }

    @Override
    public byte[] getData() {
        byte[] result = new byte[0];
        if (dataValid)
            copy(result, echo, getData());

        return result;
    }

    @Override
    public byte[] parse(byte[] request, byte[] response) {
        byte[] result = new byte[0];
        if (dataValid)
            return super.parse(request, response);

        for (int i=0; i<request.length; i++){
            dataValid = response[i]==response[i];
            if (!dataValid)
                return result;
        }

        result = new byte[response.length - request.length];


        return super.parse(request, response);
    }
}
