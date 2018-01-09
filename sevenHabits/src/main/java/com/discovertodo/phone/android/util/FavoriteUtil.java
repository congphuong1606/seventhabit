package com.discovertodo.phone.android.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.discovertodo.phone.android.global.Constant;
import com.discovertodo.phone.android.model.DailyBoostersAllItem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ngoc on 7/26/2015.
 */
public class FavoriteUtil {
    public FavoriteUtil(){
        super();
    }
    public void saveFavorite(Context context,List<DailyBoostersAllItem> listDailyBooster){
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(Constant.SHARED_PREFERENCE_FAVORITE, Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonFavorite = gson.toJson(listDailyBooster);
        editor.putString(Constant.SHARED_PREFERENCE_FAVORITE_DAILYBOOSTER, jsonFavorite);
        editor.commit();
    }
    public void addFavorite(Context context,DailyBoostersAllItem item){
        List<DailyBoostersAllItem> listFavorites = getFavorite(context);
        if (listFavorites==null){
            listFavorites = new ArrayList<DailyBoostersAllItem>();
        }
        listFavorites.add(item);
        saveFavorite(context, listFavorites);
    }
    public void removeFavorite(Context context,DailyBoostersAllItem item){
        ArrayList<DailyBoostersAllItem> listFavorite = getFavorite(context);
        if (listFavorite!=null){
            listFavorite.remove(item);
            saveFavorite(context, listFavorite);
        }
    }

    public ArrayList<DailyBoostersAllItem> getFavorite(Context context){
        SharedPreferences settings;
        List<DailyBoostersAllItem> listFavorites;
        List<DailyBoostersAllItem> newList = new ArrayList<DailyBoostersAllItem>();
        settings = context.getSharedPreferences(Constant.SHARED_PREFERENCE_FAVORITE,Context.MODE_PRIVATE);
        if (settings.contains(Constant.SHARED_PREFERENCE_FAVORITE_DAILYBOOSTER)){
            String jsonFavorite = settings.getString(Constant.SHARED_PREFERENCE_FAVORITE_DAILYBOOSTER,null);
            Gson gson = new Gson();
            DailyBoostersAllItem[] favoriteItems = gson.fromJson(jsonFavorite,DailyBoostersAllItem[].class);
            listFavorites = Arrays.asList(favoriteItems);
            listFavorites = new ArrayList<DailyBoostersAllItem>(listFavorites);
        }else{
            return null;
        }
        return (ArrayList<DailyBoostersAllItem>)listFavorites;
    }

    public boolean checkFavoriteItem(DailyBoostersAllItem checkItem,Context context){
        boolean check = false;
        List<DailyBoostersAllItem> listFavorite = getFavorite(context);
        if (listFavorite!=null){
            for (DailyBoostersAllItem item : listFavorite){
                if (item.equals(checkItem)){
                    check = true;
                    break;
                }
            }
        }
        return check;
    }

}
