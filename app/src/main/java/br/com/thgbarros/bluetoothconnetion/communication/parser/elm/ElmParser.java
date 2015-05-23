package br.com.thgbarros.bluetoothconnetion.communication.parser.elm;

import br.com.thgbarros.bluetoothconnetion.communication.ProtocolParser;

/**
 * Created by thiago on 23/05/15.
 */
public class ElmParser implements ProtocolParser {
    private ProtocolParser nextParser;
    protected boolean dataValid;

    @Override
    public void next(ProtocolParser parser) {
        if (nextParser == null)
            nextParser = parser;
        else
            nextParser.next(parser);
    }

    @Override
    public byte[] parse(byte[] request, byte[] response) {
        if (dataValid && nextParser != null)
            return nextParser.parse(request, response);

        return response;
    }

    @Override
    public void clear() {
        if (nextParser != null)
            nextParser.clear();
    }

    @Override
    public boolean isDataValid() {
        if (nextParser != null)
            return dataValid && nextParser.isDataValid();

        return dataValid;
    }

    @Override
    public byte[] getData() {
        if (nextParser != null)
            return nextParser.getData();

        return new byte[0];
    }

    public byte[] copy(byte[] dest, byte[] ... src){
        for (byte[] dataSrc: src)
            System.arraycopy(dataSrc, 0, dest, dest.length, dataSrc.length);

        return dest;
    }
}
