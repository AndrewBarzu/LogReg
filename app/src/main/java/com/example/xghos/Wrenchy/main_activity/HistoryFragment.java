package com.example.xghos.Wrenchy.main_activity;

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

import com.example.xghos.Wrenchy.helpers_extras.CurrentUser;
import com.example.xghos.Wrenchy.helpers_extras.LockableViewPager;
import com.example.xghos.Wrenchy.R;
import com.example.xghos.Wrenchy.adapters.ViewPagerAdapter;

public class HistoryFragment extends Fragment {

    private TakenOffersFragment takenOffersFragment;
    private PostedOffersFragment postedOffersFragment;
    private LockableViewPager mViewPager;
    private SharedPreferences sharedPrefs;
    private TabLayout tabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        sharedPrefs = getContext().getApplicationContext().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);

        takenOffersFragment = new TakenOffersFragment();
        postedOffersFragment = new PostedOffersFragment();

        mViewPager = rootView.findViewById(R.id.historyContainer);
        mViewPager.setSwipeable(false);
        setupViewPager(mViewPager);

        tabLayout = rootView.findViewById(R.id.tabs);

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        return rootView;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(takenOffersFragment);
        adapter.addFragment(postedOffersFragment);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        TabLayout.Tab selectedTab = tabLayout.getTabAt(CurrentUser.getTabindex());
        selectedTab.select();
    }

    @Override
    public void onPause() {
        super.onPause();
        CurrentUser.setTabindex(tabLayout.getSelectedTabPosition());
    }
}
