package com.example.xghos.Wrenchy;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



public class NavigationFragment extends Fragment {

    //Fragmentul in care se afiseaza Viewpagerul, cu cele 2 fragmente de profil si calendar(home)

    public ViewPager viewPager;

    HomeFragment mHomeFragment;
    ProfileFragment mProfileFragment;
    MenuItem mPrevMenuItem;

    BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("status", currentUser.getStatus());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_navigation, container, false);

        final Toolbar toolbar = ((MainActivity)getActivity()).toolbar.findViewById(R.id.toolbar);
        final TextView toolbarTitle = ((MainActivity)getActivity()).toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(R.string.title_home);

        bottomNavigationView = view.findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.home:
                                viewPager.setCurrentItem(0);
                                toolbarTitle.setText(R.string.title_home);
                                break;
                            case R.id.profile:
                                viewPager.setCurrentItem(1);
                                toolbarTitle.setText(R.string.title_profile);
                                break;
                            case R.id.history:
                                viewPager.setCurrentItem(2);
                                break;
                        }
                        return false;
                    }
                });

        viewPager = view.findViewById(R.id.content);
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
                        toolbarTitle.setText(R.string.title_home);
                        break;
                    case(1):
                        toolbarTitle.setText(R.string.title_profile);
                }
                mPrevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


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
        ((MainActivity)getActivity()).toolbar.setVisibility(View.VISIBLE);

        return view;
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        mHomeFragment = new HomeFragment();
        mProfileFragment = new ProfileFragment();

        adapter.addFragment(mHomeFragment);
        adapter.addFragment(mProfileFragment);
        viewPager.setAdapter(adapter);
    }

}

