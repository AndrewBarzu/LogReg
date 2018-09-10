package com.example.xghos.Wrenchy.main_activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.xghos.Wrenchy.R;
import com.example.xghos.Wrenchy.adapters.ViewPagerAdapter;
import com.example.xghos.Wrenchy.helpers_extras.LockableViewPager;
import com.example.xghos.Wrenchy.interfaces.ToolbarInterface;
import com.example.xghos.Wrenchy.interfaces.WindowInterface;


public class NavigationFragment extends Fragment {

    //Fragmentul in care se afiseaza Viewpagerul, cu cele 2 fragmente de profil si calendar(home)

    public LockableViewPager viewPager;
    public MenuItem mPrevMenuItem;
    private ToolbarInterface toolbarInterface;
    private WindowInterface windowInterface;
    private View rootView;

    public BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_navigation, container, false);

            try {
                toolbarInterface = (ToolbarInterface) getContext();
                toolbarInterface.showToolbar();
                windowInterface = (WindowInterface) getContext();
            } catch (ClassCastException e) {
                throw new ClassCastException(getContext().toString() + " must implement ToolbarInterface");
            }


            bottomNavigationView = rootView.findViewById(R.id.navigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            if (mPrevMenuItem != null) {
                                mPrevMenuItem.setChecked(false);
                            } else {
                                bottomNavigationView.getMenu().getItem(0).setChecked(false);
                            }
                            item.setChecked(true);
                            mPrevMenuItem = item;
                            switch (item.getItemId()) {
                                case R.id.home:
                                    viewPager.setCurrentItem(0);
                                    toolbarInterface.showToolbar();
                                    toolbarInterface.setToolbarTitle(R.string.title_home);
                                    windowInterface.setStatusBarColor(Color.parseColor("#6E74A9"));
                                    break;
                                case R.id.history:
                                    viewPager.setCurrentItem(1);
                                    toolbarInterface.showToolbar();
                                    toolbarInterface.setToolbarTitle(R.string.title_history);
                                    windowInterface.setStatusBarColor(Color.parseColor("#6E74A9"));
                                    break;
                                case R.id.profile:
                                    viewPager.setCurrentItem(2);
                                    toolbarInterface.hideToolbar();
                                    windowInterface.setStatusBarColor(Color.parseColor("#4A4070"));
                                    break;
                            }
                            return false;
                        }
                    });

            viewPager = rootView.findViewById(R.id.content);
            viewPager.setOffscreenPageLimit(2);
            if (viewPager.getChildCount() == 0)
                setupViewPager(viewPager);
            viewPager.setSwipeable(false);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        switch (viewPager.getCurrentItem()){
            case 0:
                toolbarInterface.setToolbarTitle(R.string.title_home);
                break;
            case 1:
                toolbarInterface.setToolbarTitle(R.string.title_history);
                break;
            case 2:
                toolbarInterface.hideToolbar();
                break;
            default :
                break;
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        if (viewPager.getAdapter() == null) {
            ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

            adapter.addFragment(new HomeFragment());
            adapter.addFragment(new HistoryFragment());
            adapter.addFragment(new ProfileFragment());
            viewPager.setAdapter(adapter);
        }
    }

}

