package br.com.thgbarros.bluetoothconnetion.communication.parser.elm;

/**
 * Created by thiago on 23/05/15.
 */
public class ElmEchoParser extends ElmParser {
    private byte[] echo;

    @Override
    public void clear() {
        super.clear();
        echo = new byte[0];
    }

    @Override
    public byte[] getData() {
        byte[] result = new byte[0];
        if (dataValid)
            copy(result, echo, super.getData());

        return result;
    }

    @Override
    public byte[] parse(byte[] request, byte[] response) {
        byte[] echo = new byte[0];

        if (dataValid)
            return getData();

        int i = 0;
        for (byte requestData: request){
            dataValid = response[i++] == requestData;

            if (!dataValid)
                return getData();

            echo[i] = requestData;
        }

        this.echo = echo;
        byte[] responseWithOutEcho = copy(response, 0, i);
        byte[] requestWithOutEcho = copy(request, 0, i);

        return super.parse(requestWithOutEcho, responseWithOutEcho);
    }
}
