package com.yy2039.chigga;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
import android.widget.LinearLayout;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class YYListViewCategory extends YYCategory {
    public int category_page_index = 0;
    public int category_page_size = 10;
    public List<YYCategoryItem> category_items;

    public YYListView category_lv_obj;
    public YYListAdapter yy_list_adapter;
    public List<Map<Integer,YYListAdapter.onYYListItemHandler>> category_lv_item_data;

    YYListViewCategory( Activity activity, String id, String name ) {
        super( activity, id, name );

        category_items = new ArrayList<YYCategoryItem>();
        category_lv_item_data = new ArrayList<Map<Integer, YYListAdapter.onYYListItemHandler>>();
    }

    public void clearup( LinearLayout main_content_view ) {
        super.clearup( main_content_view );

        category_lv_obj = null;
    }

    public View createCategoryView() {
        category_lv_obj = new YYListView( category_activity );

        yy_list_adapter = new YYListAdapter( category_activity, R.layout.info_item, category_lv_item_data );
        category_lv_obj.setAdapter( yy_list_adapter );

        category_lv_obj.setYYListViewListener( new YYListView.onYYListViewListener() {
            public void onRefresh( final YYListView.onYYListViewActionEnd yy_action_end ) {
                refresh( new YYCategory.onRefreshListener() {
                    public void onRefreshEnd( boolean bSucceed ) {
                        yy_action_end.onActionEnd( bSucceed );
                    }
                });
            }
            public void onLoad( final YYListView.onYYListViewActionEnd yy_action_end ) {
                loadMore( new YYCategory.onRefreshListener() {
                    public void onRefreshEnd( boolean bSucceed ) {
                        yy_action_end.onActionEnd( bSucceed );
                    }
                });
            }
        });

        category_lv_obj.setOnItemClickListener( new OnItemClickListener() {
            @Override   
            public void onItemClick( AdapterView<?> arg0, View arg1, int arg2, long arg3 ) {
                onClickItem( arg2 - 1 );
            }
        });

        return category_lv_obj;
    }

    public void refresh( onRefreshListener refresh_listener ) {
        Log.v( "Fly", "refresh 1111111111111111111111111111111111111111111111" );
        category_page_index = 0;
        loadMore( refresh_listener );
    }

    public void loadMore( final onRefreshListener refresh_listener ) {
        Log.v( "Fly", "load more 1111111111111111111111111111111111111111111111" );
        YYDataSource.getContentList( category_id, false, category_page_size, ++category_page_index,new YYHttpRequest.onResponseListener() {
            public void onResponse( String rsp_data ) {
                Log.v( "Fly", "rsp_data : " + rsp_data );
                boolean bSucceed = false;
                if( rsp_data != null ) {
                    try {
                        JSONArray json_array = new JSONObject( rsp_data ).getJSONArray( "contents" );
                        for( int i = 0; i < json_array.length(); ++i ) {
                            YYCategoryItem category_item = new YYCategoryItem( (JSONObject)json_array.get( i ) );
                            category_items.add( category_item );
                            category_lv_item_data.add( category_item.lv_item_data );
                        }

                        bSucceed = true;
                    } catch ( JSONException e ) {
                        e.printStackTrace();
                    }

                    updateContent();
                }

                // 
                if( refresh_listener != null ) {
                    refresh_listener.onRefreshEnd( bSucceed );
                }
            }
        });
    }

    public void updateContent() {
        if( yy_list_adapter != null ) {
            YYListAdapter.updateListViewTask task = new YYListAdapter.updateListViewTask();
            task.yy_adapter = yy_list_adapter;
            task.yy_adapter.list_data = category_lv_item_data;
            task.execute();
        }
    }

    public void onClickItem( int index ) {
        YYCategoryItem category_item = category_items.get( index );
        if( category_item != null ) {
            category_item.showCategoryItemContent( category_activity );
        }
    }
}
