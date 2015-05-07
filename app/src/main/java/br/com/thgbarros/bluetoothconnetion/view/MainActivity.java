package br.com.thgbarros.bluetoothconnetion.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import br.com.barros.newbie.Bluetooth.BluetoothManager;
import br.com.barros.newbie.Bluetooth.Exceptions.BluetoothConnectionManager;
import br.com.barros.newbie.Bluetooth.Exceptions.BluetoothException;
import br.com.thgbarros.bluetoothconnetion.R;

/**
 * Created by thiago on 02/05/15.
 */
public class MainActivity extends ActionBarActivity {
    public static final String PREFERENCES = "ScanCar Preferences";

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_setting:
                startActivity(new Intent(this, SettingsActivity.class));

        }

        return super.onOptionsItemSelected(item);
    }
}
