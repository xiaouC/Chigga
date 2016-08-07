package com.yy2039.chigga;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ImageView;
import android.text.Html.ImageGetter;
import android.graphics.drawable.Drawable;
import android.text.Html;
import java.lang.reflect.Field; 
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.util.Log;
import android.text.TextPaint;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Dialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.graphics.Bitmap;
import android.widget.ImageView.ScaleType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WelcomeActivity extends Activity
{
    WelcomeActivity activity;

    ListView lv;
    LinearLayout item_3_list;
    View personal_view = null;

    LinearLayout item_1;
    LinearLayout item_2;
    LinearLayout item_3;

    private int cur_sel = -1;

    protected Integer yy_scrolled_x = null;
    protected Integer yy_scrolled_y = null;

    YYDataSource yy_data_source = null;
    ImageDownLoader yy_image_downloader = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);

        yy_data_source = new YYDataSource();
        yy_image_downloader = new ImageDownLoader( this );

        activity = this;

        lv = (ListView)findViewById( R.id.item_list );
        item_3_list = (LinearLayout)findViewById( R.id.item_3_list );

        item_1 = (LinearLayout)findViewById( R.id.item_btn_1 );
        item_1.setClickable( true );
        item_1.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) { clickItem_1(); }
        });

        item_2 = (LinearLayout)findViewById( R.id.item_btn_2 );
        item_2.setClickable( true );
        item_2.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) { clickItem_2(); }
        });

        item_3 = (LinearLayout)findViewById( R.id.item_btn_3 );
        item_3.setClickable( true );
        item_3.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) { clickItem_3(); }
        });

        // 
        lv.setOnScrollListener( new OnScrollListener() {
            /**
             * 滚动状态改变时调用
             */
            @Override
            public void onScrollStateChanged( AbsListView view, int scrollState ) {
                // 不滚动时保存当前滚动到的位置
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                    yy_scrolled_x = lv.getFirstVisiblePosition();
                    yy_scrolled_y = lv.getFirstVisiblePosition();
                }
            }

            /**
             * 滚动时调用
             */
            @Override
            public void onScroll( AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount ) {
            }
        });

        YYListAdapter yy_list_adapter = new YYListAdapter( this, R.layout.info_item, getItemListData() );
        lv.setAdapter( yy_list_adapter );

        lv.setOnItemClickListener( new OnItemClickListener() {
            @Override   
            public void onItemClick( AdapterView<?> arg0, View arg1, int arg2, long arg3 ) {
                HomePageItem item = YYDataSource.hp_item_list.get( arg2 );
                item.showDetail( activity );
            }
        });

        clickItem_1();

        if( yy_scrolled_x != null && yy_scrolled_y != null ) {
            //lv.scrollTo( yy_scrolled_x, yy_scrolled_y );
            lv.setSelection( yy_scrolled_x );
        }
    }

    public List<Map<Integer,YYListAdapter.onYYListItemHandler>> getItemListData() {
        List<Map<Integer,YYListAdapter.onYYListItemHandler>> ret_list_data = new ArrayList<Map<Integer,YYListAdapter.onYYListItemHandler>>();


        for( int i=0; i < YYDataSource.hp_item_list.size(); ++i ) {
            ret_list_data.add( initItemData( i ) );
        }

        // 
        return ret_list_data;
    }

    public Map<Integer,YYListAdapter.onYYListItemHandler> initItemData( final int index ) {
        Map<Integer,YYListAdapter.onYYListItemHandler> map = new HashMap<Integer,YYListAdapter.onYYListItemHandler>();

        final HomePageItem item = YYDataSource.hp_item_list.get( index );

        map.put( R.id.item_image, new YYListAdapter.onYYListItemHandler() {
            public void item_handle( Object view_obj, View convertView ) {
                ImageView iv_obj = (ImageView)view_obj;
                //setImageViewResource( iv_obj, item.imageURL );
            }
        });
        map.put( R.id.item_title, new YYListAdapter.onYYListItemHandler() {
            public void item_handle( Object view_obj, View convertView ) {
                TextView tv_obj = (TextView)view_obj;
                TextPaint tp = tv_obj.getPaint();
                tp.setFakeBoldText( true );
                tv_obj.setText( item.title );
            }
        });
        map.put( R.id.item_tv_1, new YYListAdapter.onYYListItemHandler() {
            public void item_handle( Object view_obj, View convertView ) {
                TextView tv_obj = (TextView)view_obj;
                tv_obj.setText( item.tags[0] );

                if( item.tags[0] == null || item.tags[0].equals( "" ) ) {
                    tv_obj.setVisibility( View.INVISIBLE );
                } else {
                    tv_obj.setVisibility( View.VISIBLE );
                }
            }
        });
        map.put( R.id.item_tv_2, new YYListAdapter.onYYListItemHandler() {
            public void item_handle( Object view_obj, View convertView ) {
                TextView tv_obj = (TextView)view_obj;
                tv_obj.setText( item.tags[1] );

                if( item.tags[1] == null || item.tags[1].equals( "" ) ) {
                    tv_obj.setVisibility( View.INVISIBLE );
                } else {
                    tv_obj.setVisibility( View.VISIBLE );
                }
            }
        });
        map.put( R.id.item_tv_3, new YYListAdapter.onYYListItemHandler() {
            public void item_handle( Object view_obj, View convertView ) {
                TextView tv_obj = (TextView)view_obj;
                tv_obj.setText( item.tags[2] );

                if( item.tags[2] == null || item.tags[2].equals( "" ) ) {
                    tv_obj.setVisibility( View.INVISIBLE );
                } else {
                    tv_obj.setVisibility( View.VISIBLE );
                }
            }
        });
        map.put( R.id.btn_read, new YYListAdapter.onYYListItemHandler() {
            public void item_handle( Object view_obj, View convertView ) {
                LinearLayout ll_obj = (LinearLayout)view_obj;
                ll_obj.setClickable( true );
                ll_obj.setOnClickListener( new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }
        });
        map.put( R.id.item_count_1, new YYListAdapter.onYYListItemHandler() {
            public void item_handle( Object view_obj, View convertView ) {
                TextView tv_obj = (TextView)view_obj;
                tv_obj.setText( item.read_count );
            }
        });
        map.put( R.id.btn_comment, new YYListAdapter.onYYListItemHandler() {
            public void item_handle( Object view_obj, View convertView ) {
                LinearLayout ll_obj = (LinearLayout)view_obj;
                ll_obj.setClickable( true );
                ll_obj.setOnClickListener( new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }
        });
        map.put( R.id.item_count_2, new YYListAdapter.onYYListItemHandler() {
            public void item_handle( Object view_obj, View convertView ) {
                TextView tv_obj = (TextView)view_obj;
                tv_obj.setText( item.comment_count );
            }
        });
        map.put( R.id.btn_zan, new YYListAdapter.onYYListItemHandler() {
            public void item_handle( Object view_obj, final View convertView ) {
                LinearLayout ll_obj = (LinearLayout)view_obj;
                ll_obj.setClickable( true );
                ll_obj.setOnClickListener( new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HomePageItem op_item = YYDataSource.hp_item_list.get( index );
                        op_item.is_zan = !op_item.is_zan;

                        ImageView iv_obj = (ImageView)convertView.findViewById( R.id.item_zan );
                        iv_obj.setImageResource( op_item.is_zan ? R.drawable.zan_2 : R.drawable.zan_1 );
                    }
                });
            }
        });
        map.put( R.id.item_zan, new YYListAdapter.onYYListItemHandler() {
            public void item_handle( Object view_obj, View convertView ) {
                ImageView iv_obj = (ImageView)view_obj;
                iv_obj.setImageResource( item.is_zan ? R.drawable.zan_2 : R.drawable.zan_1 );
            }
        });
        map.put( R.id.item_count_3, new YYListAdapter.onYYListItemHandler() {
            public void item_handle( Object view_obj, View convertView ) {
                TextView tv_obj = (TextView)view_obj;
                tv_obj.setText( item.zan_count );
            }
        });

        return map;
    }

    public void clickItem_1() {
        if( cur_sel != 1 ) {
            item_1.setBackgroundResource( R.drawable.tab_selected );
            item_2.setBackgroundResource( R.drawable.tab_normal );
            item_3.setBackgroundResource( R.drawable.tab_normal );

            cur_sel = 1;

            lv.setVisibility( View.VISIBLE );
            item_3_list.setVisibility( View.GONE );

            YYDataSource.getContentList( "", false, 5, ++YYDataSource.hp_curr_index,new YYHttpRequest.onResponseListener() {
                public void onResponse( String rsp_data ) {
                    Log.v( "Fly", "onResponse : " + rsp_data );
                    try {
                        JSONArray json_array = new JSONObject( rsp_data ).getJSONArray( "contents" );
                        for( int i = 0; i < json_array.length(); ++i ) {
                            JSONObject json_obj = (JSONObject)json_array.get( i );

                            HomePageItem hp_item = new HomePageItem();
                            hp_item.id = json_obj.getString( "_id" );

                            JSONObject json_picture = json_obj.getJSONObject( "thumbnail" );
                            hp_item.imageURL = "http://23.105.198.234" + json_picture.getString( "src" );

                            hp_item.title = json_obj.getString( "title" );

                            JSONObject json_user = json_obj.getJSONObject( "user" );
                            hp_item.user_id = json_user.getString( "_id" );
                            hp_item.user_email = json_user.getString( "email" );
                            hp_item.user_nick_name = json_user.getString( "nickname" );

                            JSONArray tags_array = json_obj.getJSONArray( "tags" );
                            int max_tags_length = ( tags_array.length() > 3 ? 3 : tags_array.length() );
                            for( int j=0; j < max_tags_length; ++j ) {
                                hp_item.tags[j] = tags_array.getString( j );
                            }

                            JSONObject json_reading = json_obj.getJSONObject( "reading" );
                            hp_item.read_count = json_reading.getString( "total" );

                            hp_item.comment_count = json_obj.getString( "comments" );
                            hp_item.zan_count = json_obj.getString( "likes" );

                            YYDataSource.hp_item_list.add( hp_item );
                        }
                    } catch ( JSONException e ) {
                        e.printStackTrace();
                    }

                    YYListAdapter.updateListViewTask task = new YYListAdapter.updateListViewTask();
                    task.yy_adapter = (YYListAdapter)lv.getAdapter();
                    task.yy_adapter.list_data = getItemListData();
                    task.execute();
                }
            });
        }
    }

    public void clickItem_2() {
        if( cur_sel != 2 ) {
            item_2.setBackgroundResource( R.drawable.tab_selected );
            item_1.setBackgroundResource( R.drawable.tab_normal );
            item_3.setBackgroundResource( R.drawable.tab_normal );

            cur_sel = 2;

            lv.setVisibility( View.GONE );
            item_3_list.setVisibility( View.GONE );
        }
    }

    public void clickItem_3() {
        if( cur_sel != 3 ) {
            item_3.setBackgroundResource( R.drawable.tab_selected );
            item_1.setBackgroundResource( R.drawable.tab_normal );
            item_2.setBackgroundResource( R.drawable.tab_normal );

            cur_sel = 3;

            lv.setVisibility( View.GONE );
            item_3_list.setVisibility( View.VISIBLE );

            // 
            if( personal_view == null ) {
                personal_view = LayoutInflater.from( this ).inflate( R.layout.personal_info, null );
                item_3_list.addView( personal_view );

                // my attention
                LinearLayout ll_my_attention_obj = (LinearLayout)findViewById( R.id.btn_my_attention );
                ll_my_attention_obj.setClickable( true );
                ll_my_attention_obj.setOnClickListener( new OnClickListener() {
                    @Override
                    public void onClick(View v) { YYDataSource.persion_info.showMyAttentionView( activity ); }
                });

                // my fans
                LinearLayout ll_my_fans_obj = (LinearLayout)findViewById( R.id.btn_my_fans );
                ll_my_fans_obj.setClickable( true );
                ll_my_fans_obj.setOnClickListener( new OnClickListener() {
                    @Override
                    public void onClick(View v) { YYDataSource.persion_info.showMyFansView( activity ); }
                });

                // my published
                LinearLayout ll_my_published_obj = (LinearLayout)findViewById( R.id.btn_my_published );
                ll_my_published_obj.setClickable( true );
                ll_my_published_obj.setOnClickListener( new OnClickListener() {
                    @Override
                    public void onClick(View v) { YYDataSource.persion_info.showMyPublishedView( activity ); }
                });

            }

            // nick name
            TextView tv_nick_name_obj = (TextView)personal_view.findViewById( R.id.nick_name );
            tv_nick_name_obj.setText( YYDataSource.persion_info.nick_name );

            // female or male
            ImageView iv_gender_obj = (ImageView)personal_view.findViewById( R.id.gender );
            iv_gender_obj.setImageResource( YYDataSource.persion_info.is_female ? R.drawable.female : R.drawable.male );

            // signature
            TextView tv_signature_obj = (TextView)personal_view.findViewById( R.id.signature );
            tv_signature_obj.setText( YYDataSource.persion_info.signature );

            // my attentions
            TextView tv_my_attention_obj = (TextView)personal_view.findViewById( R.id.my_attention_count );
            tv_my_attention_obj.setText( "" + YYDataSource.persion_info.attention_count );

            // my fans
            TextView tv_my_fans_obj = (TextView)personal_view.findViewById( R.id.my_fans_count );
            tv_my_fans_obj.setText( "" + YYDataSource.persion_info.fans_count );

            // my published
            TextView tv_my_published_obj = (TextView)personal_view.findViewById( R.id.my_published_count );
            tv_my_published_obj.setText( "" + YYDataSource.persion_info.published_count );
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public int getResourceId( String name ) {
        try {
            // 根据资源的ID的变量名获得Field的对象,使用反射机制来实现的  
            Field field = R.drawable.class.getField( name );
            // 取得并返回资源的id的字段(静态变量)的值，使用反射机制  
            return Integer.parseInt( field.get( null ).toString() );
        } catch (Exception e) {
            // TODO: handle exception
        }
        return 0;
    }

    public CharSequence transferText( String text ) {
        CharSequence charSequence = Html.fromHtml( text, new ImageGetter() {
            @Override
            public Drawable getDrawable( String source ) {
                Drawable drawable = getResources().getDrawable( getResourceId( source ) );
                drawable.setBounds( 0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight() );
                return drawable;
            }  
        }, null );
        
        return charSequence;
    }
}
