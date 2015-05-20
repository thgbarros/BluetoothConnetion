package br.com.thgbarros.bluetoothconnetion.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.List;

import br.com.thgbarros.bluetoothconnetion.R;
import br.com.thgbarros.bluetoothconnetion.view.adapter.ViewPageAdapter;

/**
 * Created by thiagobarros on 18/05/15.
 */
public class TroubleFragment extends Fragment implements TabHost.OnTabChangeListener,
                                                            ViewPager.OnPageChangeListener {
    public static final String ACTIVITY_TITLE_KEY = "activity_title_key";

    private TabHost tabHost;
    private ViewPager viewPager;
    private ViewPageAdapter viewPageAdapter;
    private static final String LOG_TAG = TroubleFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.label_title_trouble);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trouble, container, false);

        initializeTabHost(view);
        initializeViewPage(view);
        String tab = "";
        if (savedInstanceState != null)
            tab = savedInstanceState.getString("tab");
        onTabChanged(tab);
        return view;
    }

    private void initializeTabHost(View view){
        tabHost = (TabHost) view.findViewById(android.R.id.tabhost);
        tabHost.setup();
        addTab(tabHost, tabHost.newTabSpec("Presente").setIndicator("Presente"));
        addTab(tabHost, tabHost.newTabSpec("Passado").setIndicator("Passado"));
        tabHost.setOnTabChangedListener(this);
    }

    private void initializeViewPage(View view){
        viewPageAdapter = new ViewPageAdapter(getActivity().getSupportFragmentManager(), getFragmentList());
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(viewPageAdapter);
        viewPager.setCurrentItem(tabHost.getCurrentTab());
        viewPager.setOnPageChangeListener(this);
    }

    private void addTab(TabHost tabHost, TabHost.TabSpec tabSpec) {
        tabSpec.setContent(new TabFactory(getActivity()));
        tabHost.addTab(tabSpec);
    }

    private List<Fragment> getFragmentList() {
        List<Fragment> fragments = new ArrayList<>();

        fragments.add(Fragment.instantiate(getActivity(), TroublePresentTabFragment.class.getName()));
        fragments.add(Fragment.instantiate(getActivity(), TroublePastTabFragment.class.getName()));

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment instanceof TroublePresentTabFragment || fragment instanceof TroublePastTabFragment) {
                Log.d(LOG_TAG, "Detach [" + fragment + "]");
                fragmentManager.beginTransaction().detach(fragment).commit();
            }
        }
        return fragments;
    }


    // Um simples factory que retorna View para o TabHost
    class TabFactory implements TabHost.TabContentFactory {
        private final Context mContext;
        public TabFactory(Context context) {
            mContext = context;
        }

        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {}

    @Override
    public void onPageSelected(int i) {
        tabHost.setCurrentTab(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {}

    @Override
    public void onTabChanged(String tabId) {
        int pos = tabHost.getCurrentTab();
        viewPager.setCurrentItem(pos);
    }

}
