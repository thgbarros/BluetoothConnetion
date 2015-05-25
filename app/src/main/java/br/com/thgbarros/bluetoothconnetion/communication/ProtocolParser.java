package br.com.thgbarros.bluetoothconnetion.communication;

/**
 *
 * Define os métodos básicos para fazer a análise dos dados que são recebidos.
 *
 * Created by thiago on 22/05/15.
 */
public interface ProtocolParser {

    void next(ProtocolParser parser);
    byte[] parse(byte[] request, byte[] response); //Retorna os dados analisados
    void clear();

    boolean isDataValid();
    byte[] getData();

}
