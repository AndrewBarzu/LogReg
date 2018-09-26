package com.appist.xghos.Wrenchy.main_activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appist.xghos.Wrenchy.R;
import com.appist.xghos.Wrenchy.adapters.ViewPagerAdapter;
import com.appist.xghos.Wrenchy.helpers_extras.LockableViewPager;
import com.appist.xghos.Wrenchy.helpers_extras.NoReviewsFragment;

public class ReviewsFragment extends Fragment {

    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        if(rootView == null)
            rootView = inflater.inflate(R.layout.fragment_reviews, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        if(savedInstanceState == null) {
            LockableViewPager reviewsContainer = v.findViewById(R.id.reviewContainer);
            setupViewPager(reviewsContainer);
            reviewsContainer.setSwipeable(false);

            TabLayout tabLayout = v.findViewById(R.id.reviewTabs);
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(reviewsContainer));
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new NoReviewsFragment());
        adapter.addFragment(new NoReviewsFragment());
        viewPager.setAdapter(adapter);
    }
}
