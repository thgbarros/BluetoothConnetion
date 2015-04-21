package br.com.barros.newbie.Bluetooth;

/**
 * Created by thiago on 21/04/15.
 */
public class BluetoothDevice {

    private android.bluetooth.BluetoothDevice device;

    public BluetoothDevice(android.bluetooth.BluetoothDevice device){
        this.device = device;
    }

    public android.bluetooth.BluetoothDevice getDevice(){
        return device;
    }

    @Override
    public String toString() {
        return device.getName() + "\n" + device.getAddress();
    }
}
