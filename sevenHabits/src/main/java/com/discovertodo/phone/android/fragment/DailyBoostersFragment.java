package com.discovertodo.phone.android.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.adapter.DailyBoostersAllAdapter;
import com.discovertodo.phone.android.model.DailyBoostersAllItem;
import com.discovertodo.phone.android.util.DailyBoostersUtil;
import com.discovertodo.phone.android.util.FavoriteUtil;

@SuppressLint("ValidFragment")
public class DailyBoostersFragment extends BaseFragment {

    private HomeFragment parentFragment;
    private JSONArray arrBootster;
    private View btnShowAll, btnMenu,layoutHomeTop, layoutAll, searchBooster, btnShare,
            btnCloseAll, tabBooster, tabFavorite, layoutTopSearch,layoutTopSelect,btnMenuHome,
            btnCancelSearch, btnClearText;
    private ListView listViewAllBooster;
    private TextView txtTime, txtContent,empty;
    private ImageView imgFavorite;
    private LinearLayout linearSeach;
    private Button btnSeach;
    private EditText inputText;
    public DrawerLayout drawerLayout;
    public LinearLayout layoutMenu;
    private DailyBoostersAllAdapter adapterItem, adapterFavorite;
    private ArrayList<DailyBoostersAllItem> listItem;
    private ArrayList<DailyBoostersAllItem> listItemFavorite;
    private FavoriteUtil favoriteUtil;
    private long timeFlagShowAll;
    private boolean flag = true;

//    @SuppressLint("ValidFragment")
//    public DailyBoostersFragment(HomeFragment fragment) {
//        parentFragment = fragment;
//        drawerLayout = parentFragment.drawerLayout;
//        layoutMenu = parentFragment.layoutMenu;
//    }

    public static DailyBoostersFragment getInstance(HomeFragment homeFragment){
        DailyBoostersFragment dailyBoostersFragment = new DailyBoostersFragment();
        dailyBoostersFragment.parentFragment = homeFragment;
        dailyBoostersFragment.drawerLayout = dailyBoostersFragment.parentFragment.drawerLayout;
        dailyBoostersFragment.layoutMenu = dailyBoostersFragment.parentFragment.layoutMenu;
        return dailyBoostersFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            arrBootster = DailyBoostersUtil.loadArray(activity);
            if (arrBootster == null || arrBootster.length() == 0) {
                return super.onCreateView(inflater, container, savedInstanceState);
            }

            view = inflater.inflate(R.layout.dailyboosters_layout, container, false);
            setupLayout();
            setupValue();
            setupPopupAll();
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        layoutTopSelect.setVisibility(View.VISIBLE);
        tabBooster.setSelected(flag);
        tabFavorite.setSelected(!flag);
        layoutHomeTop.setVisibility(View.VISIBLE);
        if (layoutAll.getVisibility() == View.GONE) {
            parentFragment.setTitle(getString(R.string.menu_dailyboosters));
            btnShare.setVisibility(View.VISIBLE);
        }
        if (layoutTopSearch.getVisibility() == View.VISIBLE) {
            layoutTopSelect.setVisibility(View.GONE);
            btnMenu.setEnabled(false);
        }
        if (layoutTopSelect.getVisibility()==View.VISIBLE){
            searchBooster.setVisibility(View.VISIBLE);
        }
		setupValue();
    }

    @Override
    public void onPause() {
        super.onPause();
        layoutTopSelect.setVisibility(View.GONE);
        layoutAll.setVisibility(View.GONE);
        btnCloseAll.setVisibility(View.GONE);
        layoutTopSearch.setVisibility(View.GONE);
        layoutHomeTop.setVisibility(View.VISIBLE);
        btnShare.setVisibility(View.GONE);
    }

    private void setupValue() {

        timeFlagShowAll = System.currentTimeMillis();
        Time time = new Time();
        time.setToNow();
        final DailyBoostersAllItem item2 = new DailyBoostersAllItem();
        item2.setTime(String.format("%04d", time.year) + "年" + String.format("%02d", time.month + 1)
                + "月" + String.format("%02d", time.monthDay) + "日");
        txtTime.setText(item2.getTime());
        int days = DailyBoostersUtil.getDaysBetweenCustom(activity, time.year, time.month, time.monthDay);
        int count = days % arrBootster.length();
        item2.setId(count);
        try {
            item2.setDesc(arrBootster.getString(count));
            txtContent.setText(item2.getDesc());
        } catch (JSONException ignored) {
        }
        setImgFavotireState(imgFavorite, item2, activity);
        listItemFavorite = favoriteUtil.getFavorite(activity);
        if (listItemFavorite == null || listItemFavorite.size() == 0) {
            imgFavorite.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imgFavorite.getTag() == "uncheck") {
                        imgFavorite.setImageResource(R.drawable.icon_bookmark);
                        favoriteUtil.addFavorite(activity, item2);
                        imgFavorite.setTag("check");
                    } else {
                        imgFavorite.setImageResource(R.drawable.icon_bookmark2);
                        favoriteUtil.removeFavorite(activity, item2);
                        imgFavorite.setTag("uncheck");
                    }
                }
            });
        } else {
            imgFavorite.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imgFavorite.getTag() == "check") {
                        imgFavorite.setImageResource(R.drawable.icon_bookmark2);
                        favoriteUtil.removeFavorite(activity, item2);
                        imgFavorite.setTag("uncheck");
                    } else if (imgFavorite.getTag() == "uncheck" && !favoriteUtil.checkFavoriteItem(item2, activity)) {
                        imgFavorite.setImageResource(R.drawable.icon_bookmark);
                        favoriteUtil.addFavorite(activity, item2);
                        imgFavorite.setTag("check");
                    }
                }
            });
        }
    }

    private void setupLayout() {
        empty = (TextView)view.findViewById(R.id.txt_no_result);
        btnMenu = parentFragment.btnMenu;
        btnMenuHome = view.findViewById(R.id.home_btn_menu);
        imgFavorite = (ImageView) view.findViewById(R.id.btn_favorite);
        searchBooster = view.findViewById(R.id.search_booster);
        linearSeach = (LinearLayout) view.findViewById(R.id.linearSeach);
        btnSeach = (Button) view.findViewById(R.id.btnSearch);
        btnShowAll = view.findViewById(R.id.dailybootsters_btn_show_all);
        layoutAll = view.findViewById(R.id.dailybootsters_all_layout);
        listViewAllBooster = (ListView) view.findViewById(R.id.dailybootsters_all_listview);
        listViewAllBooster.setEmptyView(empty);
        txtTime = (TextView) view.findViewById(R.id.dailybootsters_time_text);
        txtContent = (TextView) view.findViewById(R.id.dailybootsters_content_text);
        favoriteUtil = new FavoriteUtil();
        btnShare = parentFragment.btnDailyBoostersShare;
        btnCloseAll = view.findViewById(R.id.dailyboosters_btn_close_all);
        tabBooster = view.findViewById(R.id.btnBooster);
        tabFavorite = view.findViewById(R.id.btnFavorite);
        layoutHomeTop = parentFragment.layoutHomeTop;

        layoutTopSearch = view.findViewById(R.id.home_layout_top_search);
        inputText = (EditText)view.findViewById(R.id.search);
        btnCancelSearch = view.findViewById(R.id.btn_cancel_search);
        btnClearText = view.findViewById(R.id.btnClearText);
        layoutTopSelect = view.findViewById(R.id.home_layout_top_select);

        btnShowAll.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                showDailyBoostersAll(true);
            }
        });
        btnShare.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
                    intent.setType("message/rfc822");
                    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.menu_dailyboosters));
                    intent.putExtra(android.content.Intent.EXTRA_TEXT, "Discover-to-Doアプリから共有：\n\n" +
                            txtContent.getText().toString());
                    Intent mailer = Intent.createChooser(intent, "Choose an Email client :");
                    startActivity(mailer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        btnCloseAll.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                showDailyBoostersAll(false);
                setupValue();
            }
        });
        linearSeach.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutTopSelect.setVisibility(View.GONE);
                layoutTopSearch.setVisibility(View.VISIBLE);
                btnMenu.setEnabled(false);
                searchBooster.setVisibility(View.GONE);

            }
        });
        btnSeach.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutTopSelect.setVisibility(View.GONE);
                layoutTopSearch.setVisibility(View.VISIBLE);
                btnMenu.setEnabled(false);
                searchBooster.setVisibility(View.GONE);
            }
        });
        btnCancelSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard();
                searchBooster.setVisibility(View.VISIBLE);
                layoutTopSelect.setVisibility(View.VISIBLE);
                layoutTopSearch.setVisibility(View.GONE);
                btnMenu.setEnabled(true);
                inputText.setText(null);
                if (listItemFavorite==null||listItemFavorite.size()==0) empty.setTextColor(Color.parseColor("#FFFBC7"));
            }
        });
        btnMenuHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(layoutMenu);
            }
        });
        btnClearText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                inputText.setText(null);
            }
        });
    }

    private void setupPopupAll() {
        final Time time = new Time();
        time.setToNow();
        time.set(0, 0, 0, time.monthDay, time.month, time.year);
        listItem = new ArrayList<DailyBoostersAllItem>();
        adapterItem = new DailyBoostersAllAdapter(activity, listItem);

        tabBooster.setSelected(flag);
        listViewAllBooster.setAdapter(adapterItem);
        tabBooster.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                tabBooster.setSelected(true);
                tabFavorite.setSelected(false);
                listViewAllBooster.setAdapter(adapterItem);

                if (listItemFavorite==null && listItem!=null){
                    empty.setTextColor(Color.BLACK);
                }
            }
        });

        tabFavorite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = false;
                tabFavorite.setSelected(true);
                tabBooster.setSelected(flag);
                listItemFavorite = getAndSortListFavorite();
                if (listItemFavorite != null) {
                    new FavoriteTask().execute(listItemFavorite);
                }else{
                    listViewAllBooster.setAdapter(null);
                }
                if (listItemFavorite==null ||listItemFavorite.size()==0) empty.setTextColor(Color.parseColor("#FFFBC7"));
            }
        });


        listViewAllBooster.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
                hideKeyBoard();
                inputText.setText(null);
                final DailyBoostersAllItem itemSelected = (DailyBoostersAllItem) adapterView.getItemAtPosition(position);
                showDailyBoostersAll(false);
                txtTime.setText(itemSelected.getTime());
                txtContent.setText(itemSelected.getDesc());
                setImgFavotireState(imgFavorite, itemSelected, activity);
                imgFavorite.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!favoriteUtil.checkFavoriteItem(itemSelected, activity)) {
                            favoriteUtil.addFavorite(activity, itemSelected);
                            imgFavorite.setImageResource(R.drawable.icon_bookmark);
                        } else {
                            favoriteUtil.removeFavorite(activity, itemSelected);
                            imgFavorite.setImageResource(R.drawable.icon_bookmark2);
                        }
                    }
                });

            }
        });

        new Thread() {
            public void run() {
                try {
                    final int days = DailyBoostersUtil.getDaysBetweenNow(activity);
                    Log.i("DailyFragment", "Test1");
                    final DailyBoostersAllItem item = new DailyBoostersAllItem();
                    item.setId(days);
                    item.setDesc(arrBootster.getString(days % arrBootster.length()));
                    item.setTime(String.format("%04d", time.year) + "年" + String.format("%02d", time.month + 1)
                            + "月" + String.format("%02d", time.monthDay) + "日");
                    listItem.add(item);

                    for (int i = days - 1; i >= 0; i--) {
                        time.set(time.toMillis(true) - 86400000);
                        DailyBoostersAllItem item1 = new DailyBoostersAllItem();
                        item1.setId(i);
                        item1.setDesc(arrBootster.getString(i % arrBootster.length()));
                        item1.setTime(String.format("%04d", time.year) + "年" + String.format("%02d", time.month + 1)
                                + "月" + String.format("%02d", time.monthDay) + "日");
                        listItem.add(item1);
                        if (listItem.size() == 23) {
                            break;
                        }
                    }
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Log.i("DailyFragment", "test2");
                            adapterItem.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    Log.e("TAGLOG", e.toString());
                    e.printStackTrace();
                }
            }
        }.start();

        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapterItem.getFilter().filter(s);
                if (adapterFavorite != null) {
                    adapterFavorite.getFilter().filter(s);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void showDailyBoostersAll(boolean isShow) {
        if ((System.currentTimeMillis() - timeFlagShowAll) > 800) {
            timeFlagShowAll = System.currentTimeMillis();
            if (isShow) {
                layoutHomeTop.setVisibility(View.GONE);
                layoutAll.setVisibility(View.VISIBLE);
                btnShare.setVisibility(View.GONE);
                btnCloseAll.setVisibility(View.VISIBLE);
                Animation animFadein = AnimationUtils.loadAnimation(activity, R.anim.activity_fade_in);
                layoutAll.startAnimation(animFadein);
                layoutTopSearch.setVisibility(View.GONE);
                layoutTopSelect.setVisibility(View.VISIBLE);
                searchBooster.setVisibility(View.VISIBLE);

            } else {
                layoutHomeTop.setVisibility(View.VISIBLE);
                btnMenu.setEnabled(true);
                parentFragment.setTitle(getString(R.string.menu_dailyboosters));
                layoutAll.setVisibility(View.GONE);
                btnShare.setVisibility(View.VISIBLE);
                btnCloseAll.setVisibility(View.GONE);
                Animation animFadein = AnimationUtils.loadAnimation(activity, R.anim.activity_fade_out);
                layoutAll.startAnimation(animFadein);
            }

        }
        if (tabBooster.isSelected()){
            listViewAllBooster.setAdapter(adapterItem);
        }else if (tabFavorite.isSelected()){
            listItemFavorite = getAndSortListFavorite();
            if (listItemFavorite==null){
                listViewAllBooster.setAdapter(null);
            }else {
                new FavoriteTask().execute(listItemFavorite);
            }
            if (listItemFavorite==null ||listItemFavorite.size()==0) empty.setTextColor(Color.parseColor("#FFFBC7"));
        }
    }

    public void setImgFavotireState(ImageView img, DailyBoostersAllItem item, Context context) {
        if (favoriteUtil.checkFavoriteItem(item, context)) {
            img.setImageResource(R.drawable.icon_bookmark);
            img.setTag("check");
        } else {
            img.setImageResource(R.drawable.icon_bookmark2);
            img.setTag("uncheck");
        }
    }

    class FavoriteTask extends AsyncTask<ArrayList, ArrayList, ArrayList> {
        @Override
        protected ArrayList doInBackground(ArrayList... params) {
            ArrayList<DailyBoostersAllItem> list = params[0];
            ArrayList<DailyBoostersAllItem> listTemp = new ArrayList<DailyBoostersAllItem>();
            for (int i = 0; i < list.size(); i++) {
                listTemp.add(list.get(i));
                if (listTemp.size() == 23) {
                    break;
                }
            }
            publishProgress(listTemp);
            return listTemp;
        }

        @Override
        protected void onProgressUpdate(final ArrayList... values) {
            super.onProgressUpdate(values);
            final ArrayList<DailyBoostersAllItem> items = values[0];
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final DailyBoostersAllAdapter adapter = new DailyBoostersAllAdapter(activity, items);
                    listViewAllBooster.setAdapter(adapter);
                    inputText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            adapter.getFilter().filter(s);
                            if (items!=null) {
                                empty.setTextColor(Color.BLACK);
                            }else{
                                empty.setTextColor(Color.YELLOW);
                            }

                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            });
        }
    }

    class StringDateComparator implements Comparator<DailyBoostersAllItem> {
        int a;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");

        @Override
        public int compare(DailyBoostersAllItem lhs, DailyBoostersAllItem rhs) {
            try {
                a = dateFormat.parse(lhs.getTime()).compareTo(dateFormat.parse(rhs.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return a;
        }
    }

    public ArrayList<DailyBoostersAllItem> getAndSortListFavorite() {
        ArrayList<DailyBoostersAllItem> list = favoriteUtil.getFavorite(activity);
        if (list!=null) {
            Collections.sort(list, new StringDateComparator());
            Collections.reverse(list);
            return list;
        }else{
            return null;
        }

    }
}
