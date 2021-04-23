package com.example.AmateurShipper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ShowableListMenu;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import io.grpc.Context;

public class MainActivity extends AppCompatActivity implements AHBottomNavigation.OnTabSelectedListener{

    private AHBottomNavigation bottomNavigationView;
    private Fragment fragment = null;
    public static int gallarey_count_number = 0;
    private int mCountOrder;
    HomeFragment homeFragment = new HomeFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = (AHBottomNavigation)findViewById(R.id.chipNavigation);
        loadFragment(new HomeFragment());



        //BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.cart);
        //badgeDrawable.setNumber(3333);
        // Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Bai dang", R.drawable.ic_home, R.color.whiteTextColor);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Trang thai", R.drawable.ic_status, R.color.whiteTextColor);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Tuyen duong", R.drawable.ic_map, R.color.whiteTextColor);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("Ca nhan", R.drawable.ic_profile, R.color.whiteTextColor);

        bottomNavigationView.addItem(item1);
        bottomNavigationView.addItem(item2);
        bottomNavigationView.addItem(item3);
        bottomNavigationView.addItem(item4);
        bottomNavigationView.setCurrentItem(0);
        bottomNavigationView.setOnTabSelectedListener(this);

        bottomNavigationView.setAccentColor(Color.parseColor("#FF3C4673"));
        bottomNavigationView.setInactiveColor(Color.parseColor("#FF3C4673"));
    }
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    public int getmCountOrder() {
        return mCountOrder;
    }

    public void setCountOrder(int countOrer) {
        mCountOrder = countOrer;
        AHNotification notification = new AHNotification.Builder()
                .setText(String.valueOf(mCountOrder))
                .build();
        bottomNavigationView.setNotification(notification, 1);
    }

    public void disableNotification(){
        AHNotification notification = new AHNotification.Builder()
                .setText(null)
                .build();
        bottomNavigationView.setNotification(notification, 1);
    }
    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {
        if (position == 0) {
            HomeFragment homeFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
            return true;
        } else if (position == 1) {
            CartFragment cartFragment = new CartFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, cartFragment).commit();
            return true;
        } else if (position == 2) {
            MapFragment mapFragment = new MapFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, mapFragment).commit();
            return true;
        } else if (position == 3) {
            CaNhanFragment caNhanFragment = new CaNhanFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, caNhanFragment).commit();

            return true;
        }
        return false;
    }

}