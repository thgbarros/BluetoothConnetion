package br.com.thgbarros.bluetoothconnetion.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.List;
import java.util.Set;

/**
 * Created by thiagobarros on 18/05/15.
 */
public class ViewPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private static final String LOG_TAG = ViewPageAdapter.class.getSimpleName();

    public ViewPageAdapter(FragmentManager fragmentManager, List<Fragment> fragments){
        super(fragmentManager);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int i) {
        Log.d(LOG_TAG, "Attach [" + fragments.get(i) + "]");
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public String toString() {
        return ViewPageAdapter.class.getSimpleName();
    }
}
