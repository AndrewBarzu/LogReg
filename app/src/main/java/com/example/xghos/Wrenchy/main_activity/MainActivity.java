package com.example.xghos.Wrenchy.main_activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.xghos.Wrenchy.R;
import com.example.xghos.Wrenchy.helpers_extras.CurrentUser;
import com.example.xghos.Wrenchy.interfaces.ToolbarInterface;
import com.example.xghos.Wrenchy.interfaces.WindowInterface;

public class MainActivity extends AppCompatActivity implements ToolbarInterface, WindowInterface {

    public Toolbar toolbar;
    private ActionBar actionbar;
    private Window window;
    private MenuItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#6E74A9"));

        if (getApplicationContext().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE).getString("status", "1").equals("2")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ChangePW()).commit();

        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new NavigationFragment()).commit();
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_24dp);
            actionbar.setTitle("");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof OfferFragment || fragment instanceof CreateOfferFragment)
            super.onBackPressed();
        else
            switch (item.getItemId()) {
                case R.id.action_add:
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new CreateOfferFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
            }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        item = menu.getItem(0);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof NavigationFragment) {
            NavigationFragment navigationFragment = (NavigationFragment) fragment;
            if (navigationFragment.viewPager.getCurrentItem() != 0) {
                navigationFragment.viewPager.setCurrentItem(0);
                navigationFragment.mPrevMenuItem.setChecked(false);
                navigationFragment.bottomNavigationView.getMenu().getItem(0).setChecked(true);
                toolbar.setVisibility(View.VISIBLE);
            }
            else super.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CurrentUser.setTabindex(0);
    }

    @Override
    public void showToolbar() {
        toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideToolbar() {
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void setToolbarTitle(int s) {
        TextView textView = toolbar.findViewById(R.id.toolbar_title);
        textView.setText(s);
    }

    @Override
    public void setToolbarTitle(String s) {
        TextView textView = toolbar.findViewById(R.id.toolbar_title);
        textView.setText(s);
    }

    @Override
    public void showBackButton() {
        actionbar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void hideBackButton() {
        actionbar.setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void setStatusBarColor(int color) {
        window.setStatusBarColor(color);
    }

    @Override
    public void cancel() {
        item.setTitle("Cancel");

        item.setIcon(null);
    }

    @Override
    public void add() {
        item.setTitle(null);
        item.setIcon(getResources().getDrawable(R.drawable.add_24p));
    }

    @Override
    public void remove_add() {
        item.setVisible(false);
    }

    @Override
    public void show_add() {
        item.setVisible(true);
    }
}
