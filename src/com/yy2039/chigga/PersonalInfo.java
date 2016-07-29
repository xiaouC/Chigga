package com.yy2039.chigga;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.view.ViewGroup;
import android.util.Log;

class AttentionInfo {
    public String headIcon;
    public String name;
    public String signature;
    public String desc;
    public int attentionStatus;

    AttentionInfo(  String h, String n, String s, String d, int status ) {
        headIcon = h;
        name = n;
        signature = s;
        desc = d;
        attentionStatus = status;
    }
}

public class PersonalInfo {
    public String nick_name;
    public boolean is_female;
    public String signature;
    public int attention_count;
    public int fans_count;
    public int published_count;
    public List<AttentionInfo> attention_list;
    public List<AttentionInfo> interested_fans_list;
    public List<AttentionInfo> all_fans_list;

    // 0 : add,  1 : single,   2 : each other
    private static final int[] attention_status_res_id = { R.drawable.attention_each_other, R.drawable.attention_single, R.drawable.attention_add };

    PersonalInfo( String n, boolean f, String s, int attention, int fans, int published ) {
        nick_name = n;
        is_female = f;
        signature = s;
        attention_count = attention;
        fans_count = fans;
        published_count = published;

        attention_list = new ArrayList<AttentionInfo>();
        interested_fans_list = new ArrayList<AttentionInfo>();
        all_fans_list = new ArrayList<AttentionInfo>();
    }

    public void addAttentionInfo( String h, String n, String s, String d, int status ) {
        attention_list.add( new AttentionInfo( h, n, s, d, status ) );
    }

    public void addInterestedFansInfo( String h, String n, String s, String d, int status ) {
        interested_fans_list.add( new AttentionInfo( h, n, s, d, status ) );
    }

    public void addFansInfo( String h, String n, String s, String d, int status ) {
        all_fans_list.add( new AttentionInfo( h, n, s, d, status ) );
    }

    private YYShowDialog my_attention_dlg = null;
    public void showMyAttentionView( final Activity activity ) {
        my_attention_dlg = YYShowDialog.createListViewDialog( activity, "我的关注", new YYShowDialog.onDialogListener() {
            public void onInitContentView( View view ) {
                ListView lv_view = (ListView)view;

                YYListAdapter yy_list_adapter = new YYListAdapter( activity, R.layout.attention_item, getAttentionItemListData() );
                lv_view.setAdapter( yy_list_adapter );
            }
            public void onInitBottomView( View view ) {
            }
            public void onDismiss() { my_attention_dlg = null; }
        });
    }

    public List<Map<Integer,YYListAdapter.onYYListItemHandler>> getAttentionItemListData() {
        List<Map<Integer,YYListAdapter.onYYListItemHandler>> ret_list_data = new ArrayList<Map<Integer,YYListAdapter.onYYListItemHandler>>();

        for( int i=0; i < attention_list.size(); ++i ) {
            final AttentionInfo item = attention_list.get( i );

            Map<Integer,YYListAdapter.onYYListItemHandler> map = new HashMap<Integer,YYListAdapter.onYYListItemHandler>();
            map.put( R.id.item_image, new YYListAdapter.onYYListItemHandler() {
                public void item_handle( Object view_obj, View convertView ) {
                    ImageView iv_obj = (ImageView)view_obj;
                }
            });
            map.put( R.id.name, new YYListAdapter.onYYListItemHandler() {
                public void item_handle( Object view_obj, View convertView ) {
                    TextView tv_obj = (TextView)view_obj;
                    tv_obj.setText( item.name );
                }
            });
            map.put( R.id.signature, new YYListAdapter.onYYListItemHandler() {
                public void item_handle( Object view_obj, View convertView ) {
                    TextView tv_obj = (TextView)view_obj;
                    tv_obj.setText( item.signature );
                }
            });
            map.put( R.id.desc, new YYListAdapter.onYYListItemHandler() {
                public void item_handle( Object view_obj, View convertView ) {
                    TextView tv_obj = (TextView)view_obj;
                    tv_obj.setText( item.desc );
                }
            });
            map.put( R.id.attention_status, new YYListAdapter.onYYListItemHandler() {
                public void item_handle( Object view_obj, View convertView ) {
                    ImageView iv_obj = (ImageView)view_obj;
                    iv_obj.setImageResource( attention_status_res_id[item.attentionStatus] );
                }
            });

            ret_list_data.add( map );
        }

        // 
        return ret_list_data;
    }

    public void showMyFansView( final Activity activity ) {
        YYShowDialog.createScrollViewDialog( activity, "粉丝", new YYShowDialog.onDialogListener() {
            public void onInitContentView( View view ) {
                ScrollView scroll_view = (ScrollView)view;

                LinearLayout container_obj = new LinearLayout( activity );
                container_obj.setOrientation( LinearLayout.VERTICAL );
                LinearLayout.LayoutParams container_layout_params = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
                scroll_view.addView( container_obj, container_layout_params );

                // interested fans
                View interested_caption_view = LayoutInflater.from( activity ).inflate( R.layout.fans_interested_caption, null );
                container_obj.addView( interested_caption_view );

                for( int i=0; i < interested_fans_list.size(); ++i ) {
                    View item_view = createFansItemView( activity, interested_fans_list.get( i ) );
                    container_obj.addView( item_view );
                }

                // all fans
                View all_caption_view = LayoutInflater.from( activity ).inflate( R.layout.fans_all_caption, null );
                container_obj.addView( all_caption_view );

                for( int i=0; i < all_fans_list.size(); ++i ) {
                    View item_view = createFansItemView( activity, all_fans_list.get( i ) );
                    container_obj.addView( item_view );
                }
            }
            public void onInitBottomView( View view ) {
            }
            public void onDismiss() {
            }
        });
    }

    public View createFansItemView( Activity activity, AttentionInfo item ) {
        View item_view = LayoutInflater.from( activity ).inflate( R.layout.attention_item, null );

        ImageView iv_obj = (ImageView)item_view.findViewById( R.id.item_image );

        TextView tv_name_obj = (TextView)item_view.findViewById( R.id.name );
        tv_name_obj.setText( item.name );

        TextView tv_signature_obj = (TextView)item_view.findViewById( R.id.signature );
        tv_signature_obj.setText( item.signature );

        TextView tv_desc_obj = (TextView)item_view.findViewById( R.id.desc );
        tv_desc_obj.setText( item.desc );

        ImageView iv_status_obj = (ImageView)item_view.findViewById( R.id.attention_status );
        iv_status_obj.setImageResource( attention_status_res_id[item.attentionStatus] );

        ImageView iv_line_obj = (ImageView)item_view.findViewById( R.id.fans_line );
        iv_line_obj.setVisibility( View.VISIBLE );

        return item_view;
    }

    public void showMyPublishedView( final Activity activity ) {
    }
}
