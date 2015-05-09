package br.com.thgbarros.bluetoothconnetion.view;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

import br.com.barros.newbie.Bluetooth.BluetoothManager;
import br.com.barros.newbie.Bluetooth.Exceptions.BluetoothException;
import br.com.thgbarros.bluetoothconnetion.R;


/**
 * Created by thiago on 08/05/15.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 100;

    private BluetoothManager manager;
    BluetoothAdapter btAdapter;
    Set<BluetoothDevice> pairedDevices;
    Set<BluetoothDevice> discoveredDevices;
    ListPreference btDevicesList;
    ArrayList<CharSequence> entries;
    ArrayList<CharSequence> entryValues;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        try {
            manager = BluetoothManager.getInstance(getActivity());
            findPreference("pref_bt_enabled").
                    setEnabled(manager.isEnabledBluetooth());

        } catch (BluetoothException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onPause();
//        getActivity().unregisterReceiver(btDiscoveryReceiver);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("pref_bt_enabled")) {
            if (sharedPreferences.getBoolean(key, false)) {
                Intent intent = new Intent(getActivity(), ListBluetoothDeviceActivity.class);
                startActivityForResult(intent,  REQUEST_CONNECT_DEVICE);
                //enableBluetooth();
                //findPairedDevices();
                //discoverDevices();
                //btDevicesList.setEnabled(true);
            } else {
                //btAdapter.disable();
                //btDevicesList.setEnabled(false);
            }
        }
    }

    public BluetoothAdapter getBtAdapter() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            return null;
        } else {
            return mBluetoothAdapter;
        }
    }

    public void enableBluetooth() {
        if (!btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    public void findPairedDevices() {
        pairedDevices = btAdapter.getBondedDevices();
        entries = new ArrayList<CharSequence>();
        entryValues = new ArrayList<CharSequence>();
        for (BluetoothDevice d : pairedDevices) {
            entries.add("paired: " + d.getName());
            entryValues.add(d.getAddress());
        }
        btDevicesList.setEntries(listToArray(entries));
        btDevicesList.setEntryValues(listToArray(entryValues));
    }

    public void discoverDevices() {
        btAdapter.startDiscovery();
    }

    public CharSequence[] listToArray(ArrayList<CharSequence> list) {
        CharSequence[] sequence = new CharSequence[list.size()];
        for (int i = 0; i < list.size(); i++) {
            sequence[i] = list.get(i);
        }
        return sequence;
    }

    private final BroadcastReceiver btDiscoveryReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                discoveredDevices.add(device);

                entries.add("discovered: " + device.getName());
                btDevicesList.setEntries(listToArray(entries));

                entryValues.add(device.getAddress());
                btDevicesList.setEntryValues(listToArray(entryValues));

            }
        }
    };
}