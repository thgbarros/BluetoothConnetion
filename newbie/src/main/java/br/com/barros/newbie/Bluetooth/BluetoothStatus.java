package br.com.barros.newbie.Bluetooth;

/**
 * Created by thiagobarros on 06/04/15.
 */
public enum BluetoothStatus {
    NONE(0),
    DISCOVERY(1),
    DISCOVERY_FINISH(2),
    ACCEPT(3),
    CONNECTING(4),
    NOT_CONNECTED(5),
    CONNECTED(6),
    ENABLED(7),
    READ(8),
    IN_COMMUNICATION(9);


    private int id;

    BluetoothStatus(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public static BluetoothStatus getValueOf(int i){
        return BluetoothStatus.values()[i];
    }

}
