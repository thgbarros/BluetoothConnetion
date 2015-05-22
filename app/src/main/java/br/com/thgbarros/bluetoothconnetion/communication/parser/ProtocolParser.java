package br.com.thgbarros.bluetoothconnetion.communication.parser;

/**
 * Created by thiago on 22/05/15.
 */
public interface ProtocolParser {

    void next(ProtocolParser parser);
    byte[] parse(byte[] data, byte[] dataParsed);
    void clear();

    boolean isDataValid();
    byte[] getData();

}
