package br.com.thgbarros.bluetoothconnetion.view;

import android.app.ActionBar;
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
public class ListBluetoothDeviceActivity extends ActionBarActivity implements OnItemClickListener {
    private BluetoothManager bluetoothManager;
    private Handler handler;
    private ProgressDialog dialog = null;
    private BluetoothDevice deviceSelected;

    private static final String LOG_TAG = ListBluetoothDeviceActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bluetooth_device);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            bluetoothManager = BluetoothManager.getInstance(this);
            if (!bluetoothManager.isEnabledBluetooth())
                bluetoothManager.enableBluetooth();
            else
                bluetoothManager.startDiscovery(getDefaultHandler());

        } catch (BluetoothException be) {
            Log.d(LOG_TAG, be.getMessage());
            be.printStackTrace();
            finish();
        }

        handler = getDefaultHandler();

        ListView listDevicePaired = (ListView) findViewById(R.id.listViewDevices);
        listDevicePaired.setOnItemClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (BluetoothStatus.getValueOf(requestCode) == BluetoothStatus.ENABLED){
            bluetoothManager.startDiscovery(getDefaultHandler());
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        bluetoothManager.destroy();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_bluetooth_device, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.menuUpdateDevice:
                if (bluetoothManager.isEnabledBluetooth()){
                    bluetoothManager.startDiscovery(getDefaultHandler());
                    break;
                }
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
        List<BluetoothDevice> devicesFound = new ArrayList<>(bluetoothManager.getDevicesFound());

        deviceSelected = devicesFound.get(position);

        dialog = ProgressDialog.show(this, getString(R.string.string_connecting_device),
                getString(R.string.string_please_wait), true, true);

        bluetoothManager.connect(deviceSelected, getDefaultHandler());
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
        ListView listDevicePaired = (ListView) findViewById(R.id.listViewDevices);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_1);

        for (BluetoothDevice device: devices)
            adapter.add(device.getName() + "\n" + device.getAddress());

        if (devices.size() == 0)
            adapter.add(getString(R.string.string_device_not_found));

        listDevicePaired.setAdapter(adapter);
    }

    private void updateUI(Message msg){
        switch (getValueOf(msg.what)){
            case DISCOVERY:
                dialog = ProgressDialog.show(this, getString(R.string.string_search_devices),
                        getString(R.string.string_please_wait), true, true);
                break;
            case DISCOVERY_FINISH:
                dialog.dismiss();
                Log.d(LOG_TAG, "Discovery finish");
                loadListView(bluetoothManager.getDevicesFound());
                break;
            case CONNECTED:
                dialog.dismiss();
                Log.d(LOG_TAG, "Bluetooth Connected");
                setResult(RESULT_OK);
                finish();
                break;
            case NOT_CONNECTED:
                dialog.dismiss();
                Toast.makeText(this, String.format(
                        getString(R.string.string_device_not_connected), deviceSelected.getName()),
                            Toast.LENGTH_LONG).show();
                break;
            default:
                return;
        }
    }
}
