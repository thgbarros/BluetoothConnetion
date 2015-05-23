package br.com.thgbarros.bluetoothconnetion.communication;

import br.com.thgbarros.bluetoothconnetion.communication.model.Read;
import br.com.thgbarros.bluetoothconnetion.communication.model.Trouble;

/**
 * Created by thiago on 22/05/15.
 */
public interface Command {

    static final int SUCCESS_COMMAND = 0x200001;
    static final int FAILURE_COMMAND = 0x200002;

    void startCommunication();
    void stopCommunication();
    boolean inCommunication();

    void starRead();
    void startReadTrouble();

    Read getRead();
    Trouble getTrouble();

}
