package br.com.thgbarros.bluetoothconnetion.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.thgbarros.bluetoothconnetion.R;

/**
 * Created by thiago on 15/05/15.
 */
public class TabTroublePresentFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null)
            return null;

        return inflater.inflate(R.layout.tab_layout_trouble_present, container, false);
    }
}
