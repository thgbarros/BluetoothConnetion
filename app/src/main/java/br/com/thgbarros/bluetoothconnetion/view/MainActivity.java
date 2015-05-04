package br.com.thgbarros.bluetoothconnetion.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import br.com.barros.newbie.Bluetooth.BluetoothManager;
import br.com.barros.newbie.Bluetooth.Exceptions.BluetoothConnectionManager;
import br.com.barros.newbie.Bluetooth.Exceptions.BluetoothException;

/**
 * Created by thiago on 02/05/15.
 */
public class MainActivity extends ActionBarActivity {
    public static final String PREFERENCES = "ScanCar Preferences";

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent start = new Intent(this, SettingsActivity.class);
        startActivity(start);
    }

    @Override
    protected void onDestroy() {
        BluetoothManager.getInstance().disableBluetooth();
        super.onDestroy();

    }
}
