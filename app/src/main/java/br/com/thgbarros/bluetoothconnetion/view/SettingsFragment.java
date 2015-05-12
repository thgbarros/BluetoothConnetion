package br.com.thgbarros.bluetoothconnetion.view;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import br.com.barros.newbie.Bluetooth.BluetoothManager;
import br.com.barros.newbie.Bluetooth.Exceptions.BluetoothException;
import br.com.thgbarros.bluetoothconnetion.BuildConfig;
import br.com.thgbarros.bluetoothconnetion.R;


/**
 * Created by thiago on 08/05/15.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener,
            Preference.OnPreferenceClickListener {

    private static final int REQUEST_CONNECT_DEVICE = 1;
    public static final String PREFERENCES_BLUETOOTH_NAME = "Bluetooth_name";
    public static final String PREFERENCES_BLUETOOTH_ADDRESS = "Bluetooth_address";

    private static final String LOG_TAG = SettingsActivity.class.getSimpleName();
    private static final String PREFERENCES = "MackScanPreference";

    private BluetoothManager manager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        try {
            manager = BluetoothManager.getInstance(getActivity());
            loadPreference(getPreferenceManager().getSharedPreferences());
        } catch (BluetoothException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().findPreference(getActivity()
                .getString(R.string.pref_default_device_key))
                .setOnPreferenceClickListener(this);

        getPreferenceScreen().findPreference(getActivity()
                .getString(R.string.pref_software_version_key))
                .setOnPreferenceClickListener(this);

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onPause();
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        EditTextPreference editTextPreference = (EditTextPreference) preference;
        editTextPreference.getDialog().dismiss();

        if (preference.getKey().equals(getString(R.string.pref_default_device_key))) {
            Intent intent = new Intent(getActivity(), ListBluetoothDeviceActivity.class);
            startActivityForResult(intent, REQUEST_CONNECT_DEVICE);
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (resultCode == getActivity().RESULT_OK) {
                    Log.d(LOG_TAG, "Getting device connected...");
                    BluetoothDevice deviceConnected = BluetoothManager.getInstance().getDeviceConnected();

                    EditTextPreference editTextPreference = (EditTextPreference) getPreferenceScreen().
                            findPreference(getActivity().getString(R.string.pref_default_device_key));

                    editTextPreference.setSummary(deviceConnected.getName() +
                            " (" + deviceConnected.getAddress() + ")");

                    SharedPreferences.Editor editor = getPreferenceManager().getSharedPreferences().edit();
                    editor.putString(PREFERENCES_BLUETOOTH_NAME, deviceConnected.getName());
                    editor.putString(PREFERENCES_BLUETOOTH_ADDRESS, deviceConnected.getAddress());
                    editor.commit();

                    Log.d(LOG_TAG, "Saved preferably device.");
                }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_obd_protocol_list_key))) {
            ListPreference listPreference = (ListPreference) getPreferenceScreen().findPreference(key);
            listPreference.setSummary(sharedPreferences.getString(key, ""));
        }
    }

    private void loadPreference(SharedPreferences preferences) {
        if (preferences.contains(PREFERENCES_BLUETOOTH_NAME)) {
            EditTextPreference editTextDevicePreference = (EditTextPreference) getPreferenceScreen().
                    findPreference(getActivity().getString(R.string.pref_default_device_key));

            String deviceSelected = preferences.getString(PREFERENCES_BLUETOOTH_NAME, "");
            deviceSelected += " (" + preferences.getString(PREFERENCES_BLUETOOTH_ADDRESS, "") + ")";

            editTextDevicePreference.setSummary(deviceSelected);

            EditTextPreference editTextVersionPreference= (EditTextPreference) getPreferenceScreen().
                    findPreference(getActivity().getString(R.string.pref_software_version_key));

            editTextVersionPreference.setSummary(BuildConfig.VERSION_NAME);
        }
    }
}