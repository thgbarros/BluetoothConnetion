package br.com.thgbarros.bluetoothconnetion.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import br.com.barros.newbie.Bluetooth.Exceptions.BluetoothConnectionManager;
import br.com.thgbarros.bluetoothconnetion.R;

/**
 * Created by thiago on 02/05/15.
 */
public class SettingsActivity extends ActionBarActivity {

    private static final int REQUEST_CONNECT_DEVICE = 1;
    public static final String PREFERENCES_BLUETOOTH_NAME = "Bluetooth_name";
    public static final String PREFERENCES_BLUETOOTH_ADDRESS = "Bluetooth_address";

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_search_device:
                Intent intent = new Intent(this, ListBluetoothDeviceActivity.class);
                startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
                break;
            case R.id.menu_reset_device:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK){
                    BluetoothConnectionManager connectionManager = BluetoothConnectionManager.getInstance();
                    TextView deviceConnected = (TextView) findViewById(R.id.textViewDeviceConnected);
                    deviceConnected.setText(connectionManager.getDevice().getName());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(PREFERENCES_BLUETOOTH_NAME, connectionManager.getDevice().getName());
                    editor.putString(PREFERENCES_BLUETOOTH_ADDRESS, connectionManager.getDevice().getAddress());
                    editor.commit();
                }
        }
    }
}
