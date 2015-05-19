package br.com.thgbarros.bluetoothconnetion.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by thiagobarros on 18/05/15.
 */
public class ViewPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public ViewPageAdapter(FragmentManager fragmentManager, List<Fragment> fragments){
        super(fragmentManager);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}
