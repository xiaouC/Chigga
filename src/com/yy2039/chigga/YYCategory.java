package com.yy2039.chigga;

import android.app.Activity;
import java.util.Set;
import java.util.HashSet;

import android.view.View;
import android.widget.LinearLayout;
import android.util.Log;

public class YYCategory {
    public Activity category_activity;
    public String category_id;          // 
    public String category_name;

    public View category_view = null;

    public Set<Integer> category_async_task_ids;

    public boolean category_auto_refresh;
    public interface onRefreshListener {
        void onRefreshEnd( boolean bSucceed );
    }

    YYCategory( Activity activity, String id, String name ) {
        category_activity = activity;
        category_id = id;
        category_name = name;

        category_async_task_ids = new HashSet<Integer>();

        category_auto_refresh = true;
    }

    public void setView( LinearLayout main_content_view, int content_width, int content_height ) {
        category_view = createCategoryView();
        main_content_view.addView( category_view, content_width, content_height );

        updateContent();

        if( category_auto_refresh ) {
            category_auto_refresh = false;

            refresh( null );
        }
    }

    public void clearup( LinearLayout main_content_view ) {
        main_content_view.removeView( category_view );
        category_view = null;
    }

    public View createCategoryView() {
        return null;
    }

    public void refresh( onRefreshListener refresh_listener ) {
        updateContent();

        if( refresh_listener != null ) {
            refresh_listener.onRefreshEnd( true );
        }
    }

    public void updateContent() {
    }
}
