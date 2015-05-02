package br.com.thgbarros.bluetoothconnetion.view;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import br.com.barros.newbie.Bluetooth.BluetoothManager;
import br.com.barros.newbie.Bluetooth.BluetoothStatus;
import br.com.barros.newbie.Bluetooth.Exceptions.BluetoothConnectionManager;
import br.com.barros.newbie.Bluetooth.Exceptions.BluetoothException;
import br.com.thgbarros.bluetoothconnetion.R;

import static br.com.barros.newbie.Bluetooth.BluetoothStatus.*;

/**
 * created by thiago barros
 */
public class MainActivity extends ActionBarActivity implements OnItemClickListener {
    private BluetoothManager bluetoothManager;
    private BluetoothConnectionManager bluetoothConnectionManager;
    private Handler handler;
    private ProgressDialog dialog = null;

    private static final String TAG_LOG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            bluetoothManager = new BluetoothManager(this);
            bluetoothManager.enableBluetooth();

        } catch (BluetoothException be) {
            Log.d(TAG_LOG, be.getMessage());
            be.printStackTrace();
            finish();
        }

        handler = getDefaultHandler();

        ListView listDevicePaired = (ListView) findViewById(R.id.listViewDevicesPaired);
        listDevicePaired.setOnItemClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (BluetoothStatus.getValueOf(requestCode) == BluetoothStatus.ENABLED){
            loadListView(bluetoothManager.getDevicesFound());
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();
        bluetoothManager.disableBluetooth();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuUpdateDevice:
                bluetoothManager.startDiscovery(getDefaultHandler());
                break;
            case R.id.menuTurnOn:
                bluetoothManager.enableBluetooth();
                break;
            case R.id.menuTurnOff:
                bluetoothManager.disableBluetooth();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        List <BluetoothDevice> devicesFound = Collections.emptyList();
        devicesFound.addAll(bluetoothManager.getDevicesFound());

        BluetoothDevice device = devicesFound.get(position);

        //BluetoothDevice device = (BluetoothDevice) parent.getItemAtPosition(position);

        bluetoothManager.connect(device, getDefaultHandler());
    }

    public Handler getDefaultHandler(){
        if (handler == null) {
            handler = new Handler() {
                public void handleMessage(Message msg) {
                    updateUI(msg);
                }
            };
        }
        return handler;
    }

    private void loadListView(Collection<BluetoothDevice> devices) {
        ListView listDevicePaired = (ListView) findViewById(R.id.listViewDevicesPaired);
        ArrayAdapter<BluetoothDevice> adapter = new ArrayAdapter<BluetoothDevice>(this,
                android.R.layout.simple_list_item_1, new ArrayList<BluetoothDevice>(devices));

        listDevicePaired.setAdapter(adapter);

        if (devices.size() == 0)
            Toast.makeText(this, getString(R.string.string_device_not_found), Toast.LENGTH_LONG).show();

    }

    private void updateUI(Message msg){
        switch (getValueOf(msg.what)){
            case DISCOVERY:
                dialog = ProgressDialog.show(this, getString(R.string.string_search_devices),
                        getString(R.string.string_please_wait), true, true);
                break;
            case DISCOVERY_FINISH:
                dialog.dismiss();
                loadListView(bluetoothManager.getDevicesFound());
                break;
            case CONNECTED:
                bluetoothConnectionManager = bluetoothManager.getBluetoothConnectionManager(getDefaultHandler());
                Toast.makeText(this, "Conectado ao " +
                        bluetoothConnectionManager.getDevice().getName(), Toast.LENGTH_LONG).show();
                break;
            default:
                return;
        }
    }
}
