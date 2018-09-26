package com.appist.xghos.Wrenchy.main_activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appist.xghos.Wrenchy.helpers_extras.CurrentUser;
import com.appist.xghos.Wrenchy.helpers_extras.LockableViewPager;
import com.appist.xghos.Wrenchy.R;
import com.appist.xghos.Wrenchy.adapters.ViewPagerAdapter;
import com.appist.xghos.Wrenchy.interfaces.ToolbarInterface;

public class HistoryFragment extends Fragment {

    private TakenOffersFragment takenOffersFragment;
    private PostedOffersFragment postedOffersFragment;
    private TabLayout tabLayout;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        if(rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_history, container, false);

            takenOffersFragment = new TakenOffersFragment();
            postedOffersFragment = new PostedOffersFragment();

            LockableViewPager mViewPager = rootView.findViewById(R.id.historyContainer);
            mViewPager.setSwipeable(false);
            setupViewPager(mViewPager);
            mViewPager.setOffscreenPageLimit(1);

            tabLayout = rootView.findViewById(R.id.tabs);

            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        }
        return rootView;
    }

    private void setupViewPager(ViewPager viewPager) {
        if(viewPager.getAdapter() == null) {
            ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

            adapter.addFragment(takenOffersFragment);
            adapter.addFragment(postedOffersFragment);
            viewPager.setAdapter(adapter);
        }
    }
}
