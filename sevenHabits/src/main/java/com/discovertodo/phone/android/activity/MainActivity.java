package com.discovertodo.phone.android.activity;

import android.Manifest;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.adapter.NavDrawerListAdapter;
import com.discovertodo.phone.android.fragment.HomeFragment;
import com.discovertodo.phone.android.model.NavDrawerItem;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 199;
    private HomeFragment fragmentHome;
    private FrameLayout mainView;
    private DrawerLayout drawerLayout;
    private LinearLayout layoutMenu;
    private ListView listViewMenu;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] navMenuTitles;
    private ArrayList<NavDrawerItem> listDrawerItems;
    private NavDrawerListAdapter adapter;
    private int positionOld, test;
    public static Typeface msGothic;

    private boolean checkPermissionForTheFirstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        navMenuTitles = new String[]{
                getString(R.string.menu_dailyboosters),
                getString(R.string.menu_ebook),
                getString(R.string.menu_mind_card),
                getString(R.string.menu_honeweek),
                getString(R.string.menu_purpose_mean),
                getString(R.string.menu_content_video),
                getString(R.string.menu_nothing),
                getString(R.string.menu_settings),
                getString(R.string.menu_policy),
                getString(R.string.menu_termsuse),
                getString(R.string.menu_ebook_pdf)};

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        layoutMenu = (LinearLayout) findViewById(R.id.layout_slidermenu);
        listViewMenu = (ListView) findViewById(R.id.list_slidermenu);

        listDrawerItems = new ArrayList<NavDrawerItem>();
        listDrawerItems.add(new NavDrawerItem(navMenuTitles[0]));
        listDrawerItems.add(new NavDrawerItem(navMenuTitles[1]));
        listDrawerItems.add(new NavDrawerItem(navMenuTitles[2]));
        listDrawerItems.add(new NavDrawerItem(navMenuTitles[3]));
        listDrawerItems.add(new NavDrawerItem(navMenuTitles[4]));
        listDrawerItems.add(new NavDrawerItem(navMenuTitles[5]));
        listDrawerItems.add(new NavDrawerItem(navMenuTitles[6]));
        listDrawerItems.add(new NavDrawerItem(navMenuTitles[7]));
        listDrawerItems.add(new NavDrawerItem(navMenuTitles[8]));
        listDrawerItems.add(new NavDrawerItem(navMenuTitles[9]));
        listDrawerItems.add(new NavDrawerItem(navMenuTitles[10]));

        listViewMenu.setOnItemClickListener(new SlideMenuClickListener());

        adapter = new NavDrawerListAdapter(this, listDrawerItems);
        listViewMenu.setAdapter(adapter);
        mainView = (FrameLayout) findViewById(R.id.frame_container);

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                mainView.setTranslationX(slideOffset * drawerView.getWidth());
                drawerLayout.bringChildToFront(drawerView);
                drawerLayout.requestLayout();
            }
        };
        drawerLayout.setDrawerListener(mDrawerToggle);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        displayView(0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermissions(true);
            checkPermissionForTheFirstTime = true;
        }
    }

    public ArrayList<NavDrawerItem> getListDrawerItems() {
        return listDrawerItems;
    }

    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (!activity.checkSessionID(null)){
                switch (position){
                    case 0: test=0;
                        break;
                    case 1: test=4;
                        break;
                    case 2: test = 6;
                        break;
                    case 3: test=7;
                        break;
                    case 4: test =8;
                        break;
                    case 5: test =9;
                }
            }else{
                test = position;
            }
            if (test==6){
                return;
            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (test == 1) {
                        if (checkAndRequestPermissions(false) ) {
                            displayView(test);
                        }
                    } else {
                        displayView(test);
                    }
                } else {
                    displayView(test);
                }
            }
        }
    }

    private void displayView(int position) {
        boolean flagNewFragment = false;
        if (fragmentHome == null) {
            fragmentHome = HomeFragment.getInstance(drawerLayout,layoutMenu);
            flagNewFragment = true;
        }
        fragmentHome.setPosition(position);

        if (flagNewFragment) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragmentHome).commit();
        }
        positionOld = position;
        drawerLayout.closeDrawer(layoutMenu);
        listViewMenu.setItemChecked(positionOld, true);
        listViewMenu.setSelection(positionOld);
    }

    public void notifyDataSetChangedSlide() {
        adapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean checkAndRequestPermissions(boolean isRequestAll) {
        int permissionReadExternalStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWriteExternalStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int permissionReadCalendar = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR);

        int permissionWriteCalendar = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissionReadExternalStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (permissionWriteExternalStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (isRequestAll) {
            if (permissionReadCalendar != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_CALENDAR);
            }
            if (permissionWriteCalendar != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_CALENDAR);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (checkPermissionForTheFirstTime) {
                    checkPermissionForTheFirstTime = false;
                } else if (grantResults.length > 0){
                    boolean isPermissionGranted = true;
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            isPermissionGranted = false;
                        }
                    }
                    if (isPermissionGranted) {
                        // test = 1
                        displayView(test);
                    } else {
                        drawerLayout.closeDrawer(layoutMenu);
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }
}
