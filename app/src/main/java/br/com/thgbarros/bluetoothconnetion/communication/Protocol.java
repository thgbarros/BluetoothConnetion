package br.com.thgbarros.bluetoothconnetion.communication;

import br.com.thgbarros.bluetoothconnetion.communication.model.Read;
import br.com.thgbarros.bluetoothconnetion.communication.model.Trouble;

/**
 * Essa classe define quais compotamentos os
 * protocolos implementados no sistema irão ter.
 *
 * Created by thiago on 20/05/15.
 */
public interface Protocol {

    public void startCommunication();
    public void stopCommunication();
    public boolean inCommunication();
    public void startReading();
    public void startReadingTroubles();

    public Read getRead();
    public Trouble getTrouble();

}
