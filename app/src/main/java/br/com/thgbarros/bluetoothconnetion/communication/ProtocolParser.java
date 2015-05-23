package br.com.thgbarros.bluetoothconnetion.communication;

/**
 * Created by thiago on 22/05/15.
 */
public interface ProtocolParser {

    void next(ProtocolParser parser);
    byte[] parse(byte[] request, byte[] response);
    void clear();

    boolean isDataValid();
    byte[] getData();

}
