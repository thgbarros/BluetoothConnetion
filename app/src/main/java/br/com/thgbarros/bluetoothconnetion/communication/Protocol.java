package br.com.thgbarros.bluetoothconnetion.communication;

/**
 * Interface que define a forma como serão tratados os dados enviados e recebido do veículo
 * através de um protocolo de comunicação suportado. Cada protocolo deverá implementar suas
 * rotinas de verificação e validação dos dados.
 *
 * Created by thiago on 20/05/15.
 */
public interface Protocol {

    static final int COMMAND_RESPONSE = 0x100001;

    void retry();
    void request(byte ... data);
    byte[] parse(byte ... data);

}


