package br.com.thgbarros.bluetoothconnetion.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import br.com.barros.newbie.Bluetooth.BluetoothManager;
import br.com.barros.newbie.Bluetooth.Exceptions.BluetoothException;
import br.com.thgbarros.bluetoothconnetion.R;
import br.com.thgbarros.bluetoothconnetion.view.android.SupportFragmentTabListener;

import static br.com.barros.newbie.Bluetooth.BluetoothStatus.CONNECTED;
import static br.com.barros.newbie.Bluetooth.BluetoothStatus.getValueOf;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int ITEM_SELECTED_READS = 1;
    private static final int ITEM_SELECTED_TROUBLE = 2;

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private ProgressDialog dialog;
    private Menu menu;
    private Handler handler;
    private String deviceName;
    private BluetoothManager bluetoothManager;
    private Fragment fragmentActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
                                                        .findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


        try {
           bluetoothManager = BluetoothManager.getInstance(this);
        } catch (BluetoothException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        uninstallTabs();
        switch (position){
            case ITEM_SELECTED_READS:
                fragmentActual = new ReadsFragment();
                break;
            case ITEM_SELECTED_TROUBLE:
                setupTabs();
                return;
            default:
                fragmentActual = new PlaceholderFragment();
        }

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragmentActual)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global, menu);
        this.menu = menu;

        if (bluetoothManager.isConnected()){
            MenuItem menuItem = menu.findItem(R.id.menu_connect_device);
            menuItem.setIcon(R.drawable.ic_connector_connected);
        }

        if (bluetoothManager.inCommunication()){
            MenuItem menuItem = menu.findItem(R.id.menu_init_comunication);
            menuItem.setIcon(R.drawable.ic_action_pause);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mNavigationDrawerFragment.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()){
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.menu_connect_device:
                if (!bluetoothManager.isConnected())
                    bluetoothConnect();
                else
                    bluetoothDisconnect();

                break;
            case R.id.menu_init_comunication:
                if (!bluetoothManager.inCommunication())
                    initCommunication();
                else
                    stopCommunication();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case SettingsFragment.REQUEST_CONNECT_DEVICE:
                switch (resultCode) {
                    case RESULT_OK:
                        deviceName = bluetoothManager.getDeviceConnected().getName();

                        Message message = new Message();
                        message.what = CONNECTED.getId();
                        updateUI(message);
                        break;
                    case RESULT_CANCELED:
                        Toast.makeText(this, getString(R.string.string_bluetooth_connection_failed),
                                Toast.LENGTH_LONG)
                                .show();
                }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void bluetoothConnect(){
        dialog = ProgressDialog.show(this, getString(R.string.string_connecting_device),
                getString(R.string.string_please_wait), true, true);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean connectByDeviceDefault = sharedPreferences.getBoolean(
                                getString(R.string.pref_default_device_connect_key), false);

        String deviceAddress = "";

        if (connectByDeviceDefault) {
            deviceAddress = sharedPreferences.getString(SettingsFragment.PREFERENCES_BLUETOOTH_ADDRESS, "");
            deviceName = sharedPreferences.getString(SettingsFragment.PREFERENCES_BLUETOOTH_NAME, "");
            if (!deviceAddress.isEmpty()) {
                bluetoothManager.connect(deviceAddress, getDefaultHandler());
                return;
            }
        }

        if (!connectByDeviceDefault || deviceAddress.isEmpty()) {
            Intent intent = new Intent(this, ListBluetoothDeviceActivity.class);
            startActivityForResult(intent, SettingsFragment.REQUEST_CONNECT_DEVICE);
            dialog.dismiss();
        }
    }

    private void bluetoothDisconnect(){
        MenuItem menuItem = menu.findItem(R.id.menu_connect_device);
        menuItem.setIcon(R.drawable.ic_connector);

        stopCommunication();
        bluetoothManager.disconnect();
        mNavigationDrawerFragment.updateDeviceStatus();
    }

    private void initCommunication(){
        if (!bluetoothManager.isConnected()){
            new AlertDialog.Builder(this)
                .setTitle(getString(R.string.string_alert_warning))
                .setMessage(getString(R.string.string_alert_device_not_connected_retry))
                .setPositiveButton(getString(R.string.string_alert_button_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bluetoothConnect();
                    }
                })
                .setNegativeButton(getString(R.string.string_alert_button_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
            return;
        }

        MenuItem menuItem = menu.findItem(R.id.menu_init_comunication);
        menuItem.setIcon(R.drawable.ic_action_pause);
        bluetoothManager.initCommunication();
    }

    private void stopCommunication(){
        MenuItem menuItem = menu.findItem(R.id.menu_init_comunication);
        menuItem.setIcon(R.drawable.ic_action_play);
        bluetoothManager.stopCommunication();
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

    private void updateUI(Message msg){
        MenuItem menuItemConnector = menu.findItem(R.id.menu_connect_device);
        MenuItem menuItemInitCommunication = menu.findItem(R.id.menu_init_comunication);

        switch (getValueOf(msg.what)){
            case CONNECTED:
                dialog.dismiss();
                mNavigationDrawerFragment.updateDeviceStatus();
                menuItemConnector.setIcon(R.drawable.ic_connector_connected);
                menuItemInitCommunication.setEnabled(true);
                Toast.makeText(this, String.format(
                                getString(R.string.string_device_connected), deviceName),
                        Toast.LENGTH_LONG).show();
                break;
            case NOT_CONNECTED:
                dialog.dismiss();
                menuItemConnector.setIcon(R.drawable.ic_connector);
                Toast.makeText(this, String.format(
                                getString(R.string.string_device_not_connected), deviceName),
                        Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void setupTabs(){
        getSupportFragmentManager().beginTransaction()
                   .remove(fragmentActual).commit();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        ActionBar.Tab troublePresent = actionBar.newTab()
                    .setText("PRESENTE")
                    .setTabListener(new SupportFragmentTabListener<TabTroublePresentFragment>(R.id.container, this,
                                                        "Present", TabTroublePresentFragment.class));

        ActionBar.Tab troublePast = actionBar.newTab()
                .setText("PASSADO")
                .setTabListener(new SupportFragmentTabListener<TabTroublePastFragment>(R.id.container, this,
                        "Present", TabTroublePastFragment.class));
        actionBar.addTab(troublePresent);
        actionBar.addTab(troublePast);

    }

    private void uninstallTabs(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.removeAllTabs();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            getActivity().setTitle(R.string.app_name);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
        }
    }

}
