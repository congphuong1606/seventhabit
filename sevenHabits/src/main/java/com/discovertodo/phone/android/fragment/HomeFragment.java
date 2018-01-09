package com.discovertodo.phone.android.fragment;

import java.util.ArrayList;

import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.global.Constant;
import com.discovertodo.phone.android.model.NavDrawerItem;
import com.discovertodo.phone.android.util.StoreData;

public class HomeFragment extends BaseFragment {

    protected BaseFragment fragmentDailyBoorters, fragmentContentVideo,
            fragmentEbook, fragmentMindCard, fragmentPurpose, fragmentSettings,
            fragmentPolicy, fragmentTermsUse, fragmentHoneWeek,
            fragmentCurrent,fragmentEbookPdf,fragmentPdf;
    public View btnDailyBoostersShare, layoutHomeTop, layoutEBookBarTop, btnHoneWeekEdit,
            layoutTopMindCard, btnBackCard, btnFlipCard, layoutTopHomeFinder, btnHomeFinder,layoutnull,
            btnShareFinder, btnSaveFinder;
    public DrawerLayout drawerLayout;
    public LinearLayout layoutMenu;
    private ArrayList<NavDrawerItem> listDrawerItems;
    public View btnMenu;
    private TextView txtTitle;
    private int position, positionOld = -1;
    public StoreData storeData;
    public RelativeLayout hometop;

//    public HomeFragment(DrawerLayout drawerLayout, LinearLayout layoutMenu) {
//        this.drawerLayout = drawerLayout;
//        this.layoutMenu = layoutMenu;
//    }

    public  static HomeFragment getInstance(DrawerLayout drawerLayout, LinearLayout layoutMenu){
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.drawerLayout = drawerLayout;
        homeFragment.layoutMenu = layoutMenu;
        return homeFragment;
    }

    public void setPosition(int position) {
        this.position = position;
        if (view != null) {
            setTitle(listDrawerItems.get(position).getTitle());
            switch (position) {
                case 0:
                    if (fragmentDailyBoorters == null) {
//                        fragmentDailyBoorters = new DailyBoostersFragment(this);
                        fragmentDailyBoorters = DailyBoostersFragment.getInstance(this);
                    }
                    fragmentCurrent = fragmentDailyBoorters;
                    break;
                case 1:
                    if (fragmentEbook == null) {
                        fragmentEbook = EbookFragment.getInstance(this);
                    }
                    fragmentCurrent = fragmentEbook;

                    try {
                        ((EbookFragment) fragmentEbook).setUpdateAdapter();
                    } catch (Exception e) {
                    }
                    break;
                case 2:
                    if (fragmentMindCard == null) {
//                        fragmentMindCard = new MindCardFragment(this);
                        fragmentMindCard = MindCardFragment.getInstance(this);
                    }
                    fragmentCurrent = fragmentMindCard;
                    break;

                case 3:
                    if (fragmentHoneWeek == null) {
//                        fragmentHoneWeek = new HoneWeekFragment(this);
                        fragmentHoneWeek = HoneWeekFragment.getInstance(this);
                    }
                    fragmentCurrent = fragmentHoneWeek;

                    break;
                case 4:
                    setTitle(null);
                    storeData = new StoreData(activity);
                    boolean check = storeData.getBooleanValue("check");
                    boolean clear = storeData.getBooleanValue("clear");
                    Log.i("CHECK", check + "");

                    if (!check && !clear) {
//                        fragmentPurpose = new FinderPurposeFragment(this);
                        fragmentPurpose = FinderPurposeFragment.getInstance(this);
                    } else if (check && clear) {
//                        fragmentPurpose = new FinderPurposeInputFragment(this);
                        fragmentPurpose = FinderPurposeInputFragment.getInstance(this);
                    } else if (check) {
//                        fragmentPurpose = new FinderPurposeResult(this);
                        fragmentPurpose = FinderPurposeResult.getInstance(this);
                    } else {
                        fragmentPurpose = FinderPurposeFragment.getInstance(this);
                    }
                    fragmentCurrent = fragmentPurpose;
                    break;
                case 5:
                    if (fragmentContentVideo == null) {
//                        fragmentContentVideo = new ContentVideoListFragment();
                        fragmentContentVideo = ContentVideoListFragment.getInstance();
                    }
                    fragmentCurrent = fragmentContentVideo;
                    break;
                case 7:
                    if (fragmentSettings == null) {
                        fragmentSettings = new SettingsFragment();
                    }
                    fragmentCurrent = fragmentSettings;
                    break;

                case 8:
                    if (fragmentTermsUse == null) {
                        fragmentTermsUse = new TermsUseFragment();
                    }
                    fragmentCurrent = fragmentTermsUse;
                    break;
                case 9:
                    if (fragmentPolicy == null) {
                        fragmentPolicy = new PolicyFragment();
                    }

                    fragmentCurrent = fragmentPolicy;
                    break;
                case 10:
                    if (fragmentPdf == null) {
                        fragmentPdf = PdfFragment.getInstance(this);
                    }
                    fragmentCurrent = fragmentPdf;
                    txtTitle.setText("hihihi");
                    break;
            }
            if (this.position != positionOld) {
                positionOld = position;
                replaceFragmentChild(fragmentCurrent);
            }
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listDrawerItems = activity.getListDrawerItems();
        if (view == null) {
            view = inflater.inflate(R.layout.home_layout, container, false);
            setupLayoutTopDailyBoosters();
            setupLayoutTopEbook();
            setupLayoutTopMindCard();
            setupLayoutHomeFinder();
            setupLayoutTopHoneWeek();
            btnMenu = view.findViewById(R.id.home_btn_menu);
            txtTitle = (TextView) view.findViewById(R.id.home_text_title);
            btnMenu.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    drawerLayout.openDrawer(layoutMenu);
                }
            });
            setPosition(0);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void replaceFragmentChild(BaseFragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.home_content_fragment, fragment).commit();
    }

    public void setTitle(String title) {
        txtTitle.setText(title);
    }

    private void setupLayoutTopDailyBoosters() {
        layoutHomeTop = view.findViewById(R.id.home_top);
        btnDailyBoostersShare = view.findViewById(R.id.dailyboosters_btn_share);
    }

    private void setupLayoutTopEbook() {
        layoutnull=view.findViewById(R.id.home_layout_top_ebook_null);
        layoutEBookBarTop = view.findViewById(R.id.home_layout_top_ebook);
        View btnMenuEbook = view.findViewById(R.id.home_btn_menu_ebook);
        btnMenuEbook.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                drawerLayout.openDrawer(layoutMenu);
            }
        });
    }

    private void setupLayoutTopHoneWeek() {
        btnHoneWeekEdit = view.findViewById(R.id.honeweek_btn_edit);
    }

    private void setupLayoutTopMindCard() {
        layoutTopMindCard = view.findViewById(R.id.home_layout_top_mindcard);
        btnBackCard = view.findViewById(R.id.btn_back_mincard);
        btnFlipCard = view.findViewById(R.id.btn_flip_card);
        btnBackCard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(layoutMenu);
            }
        });
    }

    private void setupLayoutHomeFinder() {
        layoutTopHomeFinder = view.findViewById(R.id.home_layout_top_finder);
        btnHomeFinder = view.findViewById(R.id.btn_menu_home_finder);
        btnShareFinder = view.findViewById(R.id.btn_share_finder);
        btnSaveFinder = view.findViewById(R.id.btn_save_finder);
        btnHomeFinder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(layoutMenu);
            }
        });

    }

}
