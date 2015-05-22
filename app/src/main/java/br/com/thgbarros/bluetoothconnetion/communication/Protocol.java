package br.com.thgbarros.bluetoothconnetion.communication;

import br.com.thgbarros.bluetoothconnetion.communication.state.ProtocolState;

/**
 * Essa classe define quais compotamentos os
 * protocolos implementados no sistema irão ter.
 *
 * Created by thiago on 20/05/15.
 */
public interface Protocol {

    void retry(int value);
    byte[] txRx(byte[] data);

    ProtocolState getState();
}


