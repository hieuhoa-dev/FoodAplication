package com.example.foodapp.Helper;

import android.content.Context;

import com.example.foodapp.Model.searchRecommend;

import java.util.ArrayList;

public class ManagementSearchRecent {
    private Context context;
    private SearchRecent searchRecent;

    public ManagementSearchRecent(Context context) {
        this.context = context;
        this.searchRecent = new SearchRecent(context);
    }

    public ArrayList<searchRecommend> getListRecent() {
        return searchRecent.getListObject("SearchRecent");
    }

    public void insertSearchRecent(searchRecommend item) {
        ArrayList<searchRecommend> listpop = getListRecent();
        boolean existAlready = false;
        int n = 0;
        for (int i = 0; i < listpop.size(); i++) {
            if (listpop.get(i).getTitle().equals(item.getTitle())) {
                existAlready = true;
                n = i;
                break;
            }
        }
        if (existAlready) {
            return;
        } else {
            listpop.add( 0,item);
        }
        searchRecent.putListObject("SearchRecent", listpop);
    }

    public void deleteSearchRecent(ArrayList<searchRecommend> listItem, int position) {
        listItem.remove(position);
    }
}
