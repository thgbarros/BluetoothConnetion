package br.com.barros.newbie.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by thiagobarros on 06/04/15.
 */
public class BluetoothReceiver extends BroadcastReceiver {
    private Set<br.com.barros.newbie.Bluetooth.BluetoothDevice> devices;
    private Handler handler;

    private static final String LOG_TAG = BluetoothReceiver.class.getSimpleName();

    public BluetoothReceiver() {
        devices = new HashSet<>();
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
            br.com.barros.newbie.Bluetooth.BluetoothDevice device =
                    new br.com.barros.newbie.Bluetooth.BluetoothDevice((BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE));
            devices.add(device);
            Log.d(LOG_TAG, "Device encontrado  [" + device + "]");
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction())) {
            Log.d(LOG_TAG, "Discovery terminado, foram encontrados: [" + devices.size() + " device(s)]");
            Message message = new Message();
            message.what = BluetoothStatus.DISCOVERY_FINISH.getId();
            handler.sendMessage(message);
        }
    }

    public void clearListDevicesFound() {
        devices.clear();
    }

    public Collection<br.com.barros.newbie.Bluetooth.BluetoothDevice> getDevicesFound() {
        return new HashSet<>(devices);
    }
}
