package br.com.thgbarros.bluetoothconnetion.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.thgbarros.bluetoothconnetion.R;
import br.com.thgbarros.bluetoothconnetion.view.adapter.CustomReadsListViewAdapter;
import br.com.thgbarros.bluetoothconnetion.view.adapter.rowitem.ReadsRowItem;

/**
 * Created by thiago on 13/05/15.
 */
public class ReadsFragment extends Fragment {
    private List<ReadsRowItem> rowItemList;
    private ListView readListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.label_title_reads);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        readListView = (ListView)  inflater.inflate(R.layout.fragment_reads, container, false);

        rowItemList = new ArrayList<>();
        rowItemList.add(new ReadsRowItem("Tensão da bateria", "13,9", "V"));
        rowItemList.add(new ReadsRowItem("Rotação do motor", "920", "RPM"));
        rowItemList.add(new ReadsRowItem("Sensor MAP", "360", "BAR"));

        CustomReadsListViewAdapter adapter = new CustomReadsListViewAdapter(getActivity(),
                                                            R.layout.read_list_item, rowItemList);

        readListView.setAdapter(adapter);
        return readListView;
    }

}
