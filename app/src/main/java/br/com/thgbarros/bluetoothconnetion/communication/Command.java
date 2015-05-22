package br.com.thgbarros.bluetoothconnetion.communication;

/**
 * Created by thiago on 22/05/15.
 */
public interface Command {

    Command retry(int value);
    void execute();
    void add(byte[] data);

}
