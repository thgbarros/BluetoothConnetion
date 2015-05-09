package br.com.thgbarros.bluetoothconnetion.view;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.com.barros.newbie.Bluetooth.BluetoothManager;
import br.com.thgbarros.bluetoothconnetion.R;

/**
 * Created by thiago on 02/05/15.
 */
public class SettingsActivity extends ActionBarActivity {

    private static final int REQUEST_CONNECT_DEVICE = 1;
    public static final String PREFERENCES_BLUETOOTH_NAME = "Bluetooth_name";
    public static final String PREFERENCES_BLUETOOTH_ADDRESS = "Bluetooth_address";

    private static final String LOG_TAG = SettingsActivity.class.getSimpleName();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
//            case R.id.menu_search_device:
//                Intent intent = new Intent(this, ListBluetoothDeviceActivity.class);
//                startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
//                break;
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CONNECT_DEVICE:
                if (resultCode == RESULT_OK){
                    Log.d(LOG_TAG, "Getting device connected...");
                    BluetoothDevice deviceConnected = BluetoothManager.getInstance().getDeviceConnected();

                    TextView textViewDeviceConnected = (TextView) findViewById(R.id.textViewDeviceConnected);
                    textViewDeviceConnected.setText(deviceConnected.getName() +
                            "\n" + deviceConnected.getAddress());

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(PREFERENCES_BLUETOOTH_NAME, deviceConnected.getName());
                    editor.putString(PREFERENCES_BLUETOOTH_ADDRESS, deviceConnected.getAddress());
                    editor.commit();
                    Log.d(LOG_TAG, "Saved preferably device.");
                }
        }
    }

    private View.OnClickListener listenerForgetDevice = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView deviceInfo = (TextView) findViewById(R.id.textViewDeviceConnected);
//            deviceInfo.setText(getString(R.string.label_device_not_setting));
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(PREFERENCES_BLUETOOTH_NAME);
            editor.remove(PREFERENCES_BLUETOOTH_ADDRESS);
            editor.commit();
        }
    };

}
