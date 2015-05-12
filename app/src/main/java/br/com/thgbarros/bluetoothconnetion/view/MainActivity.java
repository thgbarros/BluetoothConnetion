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
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
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

import static br.com.barros.newbie.Bluetooth.BluetoothStatus.getValueOf;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private ProgressDialog dialog;
    private Menu menu;
    private Handler handler;
    private String deviceName;
    private String deviceAddress;
    private BluetoothManager bluetoothManager;

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

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
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
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

    private void bluetoothConnect(){
        dialog = ProgressDialog.show(this, getString(R.string.string_connecting_device),
                getString(R.string.string_please_wait), true, true);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        deviceAddress = sharedPreferences.getString(SettingsFragment.PREFERENCES_BLUETOOTH_ADDRESS, "");
        deviceName = sharedPreferences.getString(SettingsFragment.PREFERENCES_BLUETOOTH_NAME, "");

        bluetoothManager.connect(deviceAddress, getDefaultHandler());
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
                .setTitle("ATENÇÂO")
                .setMessage("Dispositivo bluetooth não conectado.\nDeseja conectar?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bluetoothConnect();
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
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

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
        }
    }

}
