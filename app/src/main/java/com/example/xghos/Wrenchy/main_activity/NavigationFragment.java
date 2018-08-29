package com.example.xghos.Wrenchy.main_activity;

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
import com.example.xghos.Wrenchy.helpers_extras.CurrentUser;
import com.example.xghos.Wrenchy.helpers_extras.LockableViewPager;
import com.example.xghos.Wrenchy.interfaces.ToolbarInterface;


public class NavigationFragment extends Fragment {

    //Fragmentul in care se afiseaza Viewpagerul, cu cele 2 fragmente de profil si calendar(home)

    public LockableViewPager viewPager;

    HomeFragment mHomeFragment;
    ProfileFragment mProfileFragment;
    HistoryFragment mHistoryFragment;
    MenuItem mPrevMenuItem;
    ToolbarInterface toolbarInterface;

    BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_navigation, container, false);

        try{
            toolbarInterface = (ToolbarInterface)getContext();
            toolbarInterface.setToolbarTitle(R.string.title_home);
        }
        catch (ClassCastException e){
            throw new ClassCastException(getContext().toString() + " must implement ToolbarInterface");
        }


        bottomNavigationView = view.findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home:
                                viewPager.setCurrentItem(0);
                                toolbarInterface.setToolbarTitle(R.string.title_home);
                                break;
                            case R.id.history:
                                viewPager.setCurrentItem(1);
                                toolbarInterface.setToolbarTitle(R.string.title_history);
                                break;
                            case R.id.profile:
                                viewPager.setCurrentItem(2);
                                toolbarInterface.setToolbarTitle(CurrentUser.getUserName());
                                break;
                        }
                        return false;
                    }
                });

        viewPager = view.findViewById(R.id.content);
        viewPager.setSwipeable(false);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mPrevMenuItem != null) {
                    mPrevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                switch (position){
                    case(0):
                        toolbarInterface.setToolbarTitle(R.string.title_home);
                        break;
                    case(1):
                        toolbarInterface.setToolbarTitle(R.string.title_history);
                        break;
                    case(2):
                        toolbarInterface.setToolbarTitle(CurrentUser.getUserName());
                        break;
                }
                mPrevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(2);


//        Calendar startDate = Calendar.getInstance();
//
//        Calendar endDate = Calendar.getInstance();
//        endDate.add(Calendar.MONTH, 1);
//
//        mHorizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
//                .range(startDate, endDate)
//                .datesNumberOnScreen(5)
//                .build();
//
//        mHorizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
//            @Override
//            public void onDateSelected(Calendar date, int position) {
//
//            }
//        });
        setupViewPager(viewPager);
        return view;
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        mHomeFragment = new HomeFragment();
        mHistoryFragment = new HistoryFragment();
        mProfileFragment = new ProfileFragment();

        adapter.addFragment(mHomeFragment);
        adapter.addFragment(mHistoryFragment);
        adapter.addFragment(mProfileFragment);
        viewPager.setAdapter(adapter);
    }

}

