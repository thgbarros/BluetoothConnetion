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
import br.com.thgbarros.bluetoothconnetion.view.adapter.TroubleListViewAdapter;
import br.com.thgbarros.bluetoothconnetion.view.adapter.rowitem.TroubleRowItem;

/**
 * Created by thiago on 15/05/15.
 */
public class TroublePastTabFragment extends Fragment {
    private List<TroubleRowItem> rowItemList;
    private ListView troubleListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        troubleListView = (ListView) inflater.inflate(R.layout.tab_layout_trouble_past, container, false);

        rowItemList = new ArrayList<>();
        rowItemList.add(new TroubleRowItem("P0107", "Mau funcionamento da massa ou volume do cicuito..."));

        TroubleListViewAdapter adapter = new TroubleListViewAdapter(getActivity(), rowItemList);

        troubleListView.setAdapter(adapter);
        return troubleListView;
    }

    @Override
    public String toString() {
        return TroublePastTabFragment.class.getSimpleName();
    }
}
