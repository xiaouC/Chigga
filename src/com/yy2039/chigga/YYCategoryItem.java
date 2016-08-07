package com.yy2039.chigga;

import java.util.Map;
import java.util.HashMap;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.widget.ScrollView;
import android.widget.ImageButton;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class YYCategoryItem {
    public String item_id;
    public String item_title;

    public String item_image_url;

    public String item_user_id;
    public String item_user_email;
    public String item_user_name;

    public static int item_max_tags_count = 3;
    public String[] item_tags = { "", "", "" };

    public String item_read_count;
    public String item_comment_count;
    public String item_zan_count;

    public Map<Integer, YYListAdapter.onYYListItemHandler> lv_item_data;

    YYCategoryItem( JSONObject json_obj ) {
        try {
            item_id = json_obj.getString( "_id" );
            item_title = json_obj.getString( "title" );

            JSONObject json_picture = json_obj.getJSONObject( "thumbnail" );
            item_image_url = ChiggaCommon.SOURCE_URL + json_picture.getString( "src" );

            JSONObject json_user = json_obj.getJSONObject( "user" );
            item_user_id = json_user.getString( "_id" );
            item_user_email = json_user.getString( "email" );
            item_user_name = json_user.getString( "nickname" );

            JSONArray tags_array = json_obj.getJSONArray( "tags" );
            int max_tags_length = ( tags_array.length() > item_max_tags_count ? item_max_tags_count : tags_array.length() );
            for( int i=0; i < max_tags_length; ++i ) {
                item_tags[i] = tags_array.getString( i );
            }

            JSONObject json_reading = json_obj.getJSONObject( "reading" );
            item_read_count = json_reading.getString( "total" );

            item_comment_count = json_obj.getString( "comments" );
            item_zan_count = json_obj.getString( "likes" );
        } catch ( JSONException e ) {
            e.printStackTrace();
        }

        // 
        lv_item_data = new HashMap<Integer, YYListAdapter.onYYListItemHandler>();

        lv_item_data.put( R.id.item_image, new YYListAdapter.onYYListItemHandler() {
            public void item_handle( Object view_obj, View convertView ) {
                ImageView iv_obj = (ImageView)view_obj;
                Helper.setImageViewResource( iv_obj, item_image_url );
            }
        });
        lv_item_data.put( R.id.item_title, new YYListAdapter.onYYListItemHandler() {
            public void item_handle( Object view_obj, View convertView ) {
                TextView tv_obj = (TextView)view_obj;
                TextPaint tp = tv_obj.getPaint();
                tp.setFakeBoldText( true );
                tv_obj.setText( item_title );
            }
        });
        lv_item_data.put( R.id.item_tv_1, new YYListAdapter.onYYListItemHandler() {
            public void item_handle( Object view_obj, View convertView ) {
                TextView tv_obj = (TextView)view_obj;
                tv_obj.setText( item_tags[0] );

                if( item_tags[0] == null || item_tags[0].equals( "" ) ) {
                    tv_obj.setVisibility( View.INVISIBLE );
                } else {
                    tv_obj.setVisibility( View.VISIBLE );
                }
            }
        });
        lv_item_data.put( R.id.item_tv_2, new YYListAdapter.onYYListItemHandler() {
            public void item_handle( Object view_obj, View convertView ) {
                TextView tv_obj = (TextView)view_obj;
                tv_obj.setText( item_tags[1] );

                if( item_tags[1] == null || item_tags[1].equals( "" ) ) {
                    tv_obj.setVisibility( View.INVISIBLE );
                } else {
                    tv_obj.setVisibility( View.VISIBLE );
                }
            }
        });
        lv_item_data.put( R.id.item_tv_3, new YYListAdapter.onYYListItemHandler() {
            public void item_handle( Object view_obj, View convertView ) {
                TextView tv_obj = (TextView)view_obj;
                tv_obj.setText( item_tags[2] );

                if( item_tags[2] == null || item_tags[2].equals( "" ) ) {
                    tv_obj.setVisibility( View.INVISIBLE );
                } else {
                    tv_obj.setVisibility( View.VISIBLE );
                }
            }
        });
        lv_item_data.put( R.id.btn_read, new YYListAdapter.onYYListItemHandler() {
            public void item_handle( Object view_obj, View convertView ) {
                View ll_obj = (View)view_obj;
                ll_obj.setClickable( true );
                ll_obj.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }
        });
        lv_item_data.put( R.id.item_count_1, new YYListAdapter.onYYListItemHandler() {
            public void item_handle( Object view_obj, View convertView ) {
                TextView tv_obj = (TextView)view_obj;
                tv_obj.setText( item_read_count );
            }
        });
        lv_item_data.put( R.id.btn_comment, new YYListAdapter.onYYListItemHandler() {
            public void item_handle( Object view_obj, View convertView ) {
                View ll_obj = (View)view_obj;
                ll_obj.setClickable( true );
                ll_obj.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }
        });
        lv_item_data.put( R.id.item_count_2, new YYListAdapter.onYYListItemHandler() {
            public void item_handle( Object view_obj, View convertView ) {
                TextView tv_obj = (TextView)view_obj;
                tv_obj.setText( item_comment_count );
            }
        });
        lv_item_data.put( R.id.btn_zan, new YYListAdapter.onYYListItemHandler() {
            public void item_handle( Object view_obj, final View convertView ) {
                View ll_obj = (View)view_obj;
                ll_obj.setClickable( true );
                ll_obj.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //HomePageItem op_item = YYDataSource.hp_item_list.get( index );
                        //op_item.is_zan = !op_item.is_zan;

                        //ImageView iv_obj = (ImageView)convertView.findViewById( R.id.item_zan );
                        //iv_obj.setImageResource( op_item.is_zan ? R.drawable.zan_2 : R.drawable.zan_1 );
                    }
                });
            }
        });
        lv_item_data.put( R.id.item_zan, new YYListAdapter.onYYListItemHandler() {
            public void item_handle( Object view_obj, View convertView ) {
                ImageView iv_obj = (ImageView)view_obj;
                //iv_obj.setImageResource( item.is_zan ? R.drawable.zan_2 : R.drawable.zan_1 );
                iv_obj.setImageResource( R.drawable.zan_2 );
            }
        });
        lv_item_data.put( R.id.item_count_3, new YYListAdapter.onYYListItemHandler() {
            public void item_handle( Object view_obj, View convertView ) {
                TextView tv_obj = (TextView)view_obj;
                tv_obj.setText( item_zan_count );
            }
        });
    }

    public void showCategoryItemContent( final Activity activity ) {
        YYShowDialog.createScrollViewDialog( activity, activity.getString( R.string.item_name_1 ), new YYShowDialog.onDialogListener() {
            public void onInitContentView( View view ) {
                View content_view = LayoutInflater.from( activity ).inflate( R.layout.category_item_content_view, null );
                ((ScrollView)view).addView( content_view );

                // 
                WebView wv = (WebView)content_view.findViewById( R.id.web_view );
                WebSettings wSet = wv.getSettings();    
                wSet.setJavaScriptEnabled( true );
                wv.setWebViewClient( new WebViewClient() {
                    // 重写此方法表明点击网页里面的链接还是在当前的 webview 里跳转，不跳到浏览器那边
                    public boolean shouldOverrideUrlLoading( WebView view, String url ) {
                        view.loadUrl( url );

                        return true;
                    }
                });
                wv.loadUrl( "http://www.163.com" );

                // btn_attention
                final ImageButton attention_obj = (ImageButton)view.findViewById( R.id.btn_attention );
                attention_obj.setImageResource( R.drawable.attention_action_1 );
                attention_obj.setOnClickListener( new View.OnClickListener () {
                    public void onClick( View v ){
                    }
                });

                // head zan
                final ImageButton ib_head_zan_obj = (ImageButton)view.findViewById( R.id.btn_head_zan );
                ib_head_zan_obj.setImageResource( R.drawable.head_zan_1 );

                ImageButton ib_head_more_zan_obj = (ImageButton)view.findViewById( R.id.btn_head_more );
                ib_head_more_zan_obj.setOnClickListener( new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });

                for( int i=1; i <= 14; ++i ) {
                    ImageButton ib_head_obj = (ImageButton)view.findViewById( Helper.getResourceId( String.format( "btn_head_%d", i ) ) );
                    ib_head_obj.setImageResource( Helper.getDrawableResourceId( String.format( "zan_head_%d", i ) ) );
                    ib_head_obj.setOnClickListener( new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                }

                // comments
                LinearLayout comment_list_obj = (LinearLayout)view.findViewById( R.id.comment_list );
                //for( int i=0; i < comment_list.size(); ++i ) {
                //    initCommentView( activity, comment_list_obj, i );
                //}

                ib_head_zan_obj.setOnClickListener( new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });

            }
            public void onInitBottomView( View view ) {
                View bottom_view = LayoutInflater.from( activity ).inflate( R.layout.hp_bottom_view, null );
                ((LinearLayout)view).addView( bottom_view );

                // btn dian zan
                View dian_zan_obj = bottom_view.findViewById( R.id.btn_dian_zan );
                final ImageView iv_zan_obj = (ImageView)bottom_view.findViewById( R.id.dian_zan );
                iv_zan_obj.setImageResource( R.drawable.dian_zan_1 );

                dian_zan_obj.setOnClickListener( new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });

                // btn share
                View share_obj = bottom_view.findViewById( R.id.btn_share );
                share_obj.setOnClickListener( new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });

                // write comment
                View write_comment_obj = bottom_view.findViewById( R.id.write_comment );
                write_comment_obj.setOnClickListener( new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //showWriteCommentView( activity );
                    }
                });

            }
            public void onDismiss() {
            }
        });

        YYDataSource.getContents( item_id, new YYHttpRequest.onResponseListener() {
            public void onResponse( String rsp_data ) {
                Log.v( "Fly", "showDetail getContents onResponse : " + rsp_data );
            }
        });
    }
}
