package com.yy2039.chigga;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.Display;
import android.graphics.Rect;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;
import android.util.Log;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Gravity;
import android.widget.Button;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.graphics.Color;
import android.widget.ScrollView;
import android.webkit.WebView;

class RecommentInfo {
    RecommentInfo( String n1, String n2, String c, String t ) {
        name_1 = n1;
        name_2 = n2;
        content = c;
        time = t;
    }
    public String name_1;
    public String name_2;
    public String content;
    public String time;
}

class CommentInfo {
    CommentInfo( String h, String n, String t, String c ) {
        headURL = h;
        name = n;
        time = t;
        content = c;

        recomment_list = new ArrayList<RecommentInfo>();
    }

    public String headURL;
    public String name;
    public String time;
    public String content;
    public List<RecommentInfo> recomment_list;
}

// 
public class HomePageItem {
    public String id;
    public String imageURL;         // 
    public String title;
    public String[] tags = { "", "", "" };
    public String read_count;
    public String comment_count;
    public String zan_count;
    public String user_id;
    public String user_email;
    public String user_nick_name;
    public boolean is_zan;
    public boolean is_attention;
    public String page_content;
    public List<CommentInfo> comment_list;

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public HomePageItem() {
        comment_list = new ArrayList<CommentInfo>();
    }

    public int addComment( String head, String n, String t, String c ) {
        comment_list.add( new CommentInfo( head, n, t, c ) );

        return comment_list.size() - 1;
    }

    public void addRecomment( int index, String n1, String n2, String c, String t ) {
        comment_list.get( index ).recomment_list.add( new RecommentInfo( n1, n2, c, t ) );
    }

    private YYShowDialog hp_detail_dlg = null;
    public void showDetail( final Activity activity ) {
        hp_detail_dlg = YYShowDialog.createScrollViewDialog( activity, activity.getString( R.string.item_name_1 ), new YYShowDialog.onDialogListener() {
            public void onInitContentView( View view ) {
                View content_view = LayoutInflater.from( activity ).inflate( R.layout.hp_content_view, null );
                ((ScrollView)view).addView( content_view );

                WebView wv = (WebView)content_view.findViewById( R.id.web_view );
                wv.loadUrl( "http://www.android100.org/html/201311/19/4804.html" );
            }
            public void onInitBottomView( View view ) {
                View bottom_view = LayoutInflater.from( activity ).inflate( R.layout.hp_bottom_view, null );
                ((LinearLayout)view).addView( bottom_view );
            }
            public void onDismiss() { hp_detail_dlg = null; }
        });
        View view = hp_detail_dlg.yy_view;

        // btn_attention
        final ImageButton attention_obj = (ImageButton)view.findViewById( R.id.btn_attention );
        attention_obj.setImageResource( is_attention ? R.drawable.attention_action_2 : R.drawable.attention_action_1 );
        attention_obj.setOnClickListener( new View.OnClickListener () {
            public void onClick( View v ){
                is_attention = !is_attention;

                attention_obj.setImageResource( is_attention ? R.drawable.attention_action_2 : R.drawable.attention_action_1 );
            }
        });

        //// read count
        //TextView tv_read_count_obj = (TextView)view.findViewById( R.id.read_count );
        //tv_read_count_obj.setText( "" + read_count );

        //// content
        //TextView tv_content_obj = (TextView)view.findViewById( R.id.content );
        //tv_content_obj.setText( page_content );

        // head zan
        final ImageButton ib_head_zan_obj = (ImageButton)view.findViewById( R.id.btn_head_zan );
        ib_head_zan_obj.setImageResource( is_zan ? R.drawable.head_zan_2 : R.drawable.head_zan_1 );

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
        for( int i=0; i < comment_list.size(); ++i ) {
            initCommentView( activity, comment_list_obj, i );
        }

        // btn dian zan
        LinearLayout dian_zan_obj = (LinearLayout)view.findViewById( R.id.btn_dian_zan );
        final ImageView iv_zan_obj = (ImageView)view.findViewById( R.id.dian_zan );
        iv_zan_obj.setImageResource( is_zan ? R.drawable.dian_zan_2 : R.drawable.dian_zan_1 );

        dian_zan_obj.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                is_zan = !is_zan;

                iv_zan_obj.setImageResource( is_zan ? R.drawable.dian_zan_2 : R.drawable.dian_zan_1 );
                ib_head_zan_obj.setImageResource( is_zan ? R.drawable.head_zan_2 : R.drawable.head_zan_1 );
            }
        });

        ib_head_zan_obj.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                is_zan = !is_zan;

                iv_zan_obj.setImageResource( is_zan ? R.drawable.dian_zan_2 : R.drawable.dian_zan_1 );
                ib_head_zan_obj.setImageResource( is_zan ? R.drawable.head_zan_2 : R.drawable.head_zan_1 );
            }
        });


        // btn share
        LinearLayout share_obj = (LinearLayout)view.findViewById( R.id.btn_share );
        share_obj.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        // write comment
        LinearLayout write_comment_obj = (LinearLayout)view.findViewById( R.id.write_comment );
        write_comment_obj.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                showWriteCommentView( activity );
            }
        });
    }

    public void initCommentView( final Activity activity, LinearLayout comment_list_obj, final int comment_index ) {
        CommentInfo comment_info = comment_list.get( comment_index );

        LinearLayout comment_obj = new LinearLayout( activity );
        comment_obj.setOrientation( LinearLayout.VERTICAL );
        LinearLayout.LayoutParams comment_layout_params = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        comment_list_obj.addView( comment_obj, comment_layout_params );

        // line ImageView
        ImageView iv_line_obj = new ImageView( activity );
        iv_line_obj.setImageResource( R.drawable.line_comment );

        LinearLayout.LayoutParams iv_line_layout_params = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        iv_line_layout_params.topMargin = 10;
        iv_line_layout_params.bottomMargin = 10;
        comment_obj.addView( iv_line_obj, iv_line_layout_params );

        // LinearLayout [left : head Imageview, right LinearLayout ]
        LinearLayout container_obj = new LinearLayout( activity );
        container_obj.setOrientation( LinearLayout.HORIZONTAL );

        LinearLayout.LayoutParams container_layout_params = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        comment_obj.addView( container_obj, container_layout_params );

        // head ImageView
        ImageView iv_head_obj = new ImageView( activity );
        iv_head_obj.setImageResource( R.drawable.user_head_1 );
        //iv_head_obj.setGravity( Gravity.TOP );

        LinearLayout.LayoutParams iv_head_layout_params = new LinearLayout.LayoutParams( Helper.dip2px( activity, 30.0f ), Helper.dip2px( activity, 30.0f ) );
        iv_head_layout_params.topMargin = Helper.dip2px( activity, 5 );
        iv_head_layout_params.bottomMargin = Helper.dip2px( activity, 10 );
        iv_head_layout_params.leftMargin = Helper.dip2px( activity, 5 );
        iv_head_layout_params.rightMargin = Helper.dip2px( activity, 10 );
        container_obj.addView( iv_head_obj, iv_head_layout_params );

        // LinearLayout [LinearLayout : name & data, TextView : content, recomment]
        LinearLayout content_obj = new LinearLayout( activity );
        content_obj.setOrientation( LinearLayout.VERTICAL );

        LinearLayout.LayoutParams content_layout_params = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        container_obj.addView( content_obj, content_layout_params );

        // LinearLayout name & time
        LinearLayout name_time_obj = new LinearLayout( activity );
        name_time_obj.setOrientation( LinearLayout.HORIZONTAL );

        LinearLayout.LayoutParams name_time_layout_params = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        name_time_layout_params.gravity = Gravity.CENTER;
        content_obj.addView( name_time_obj, name_time_layout_params );

        // name TextView
        TextView tv_name_obj = new TextView( activity );
        tv_name_obj.setGravity( Gravity.LEFT );
        tv_name_obj.setText( comment_info.name );
        name_time_obj.addView( tv_name_obj, new LinearLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );

        // time TextView
        TextView tv_time_obj = new TextView( activity );
        tv_time_obj.setGravity( Gravity.RIGHT );
        tv_time_obj.setText( comment_info.time );

        LinearLayout.LayoutParams time_layout_params = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        time_layout_params.rightMargin = Helper.dip2px( activity, 10 );
        name_time_obj.addView( tv_time_obj, time_layout_params );

        // content TextView
        TextView tv_content_obj = new TextView( activity );
        tv_content_obj.setGravity( Gravity.LEFT );
        tv_content_obj.setText( comment_info.content );

        LinearLayout.LayoutParams iv_content_layout_params = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        iv_content_layout_params.topMargin = Helper.dip2px( activity, 10 );
        iv_content_layout_params.bottomMargin = Helper.dip2px( activity, 10 );
        content_obj.addView( tv_content_obj, iv_content_layout_params );

        // LinearLayout recomment list
        LinearLayout recomment_obj = new LinearLayout( activity );
        recomment_obj.setOrientation( LinearLayout.VERTICAL );

        content_obj.addView( recomment_obj, new LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );

        for( int i=0; i < comment_info.recomment_list.size(); ++i ) {
            initRecommentView( activity, recomment_obj, comment_index, i );
        }
    }

    public void initRecommentView( Activity activity, LinearLayout recomment_obj, final int comment_index, final int recomment_index ) {
        CommentInfo comment_info = comment_list.get( comment_index );
        RecommentInfo recomment_info = comment_info.recomment_list.get( recomment_index );

        Button recomment_content_obj = new Button( activity );
        recomment_content_obj.setGravity( Gravity.LEFT );
        recomment_content_obj.getBackground().setAlpha( 0 );

        String text_m = String.format( " 回复@%s  ", recomment_info.name_2 );
        String text = recomment_info.name_1 + text_m + recomment_info.content;

        SpannableString msp = new SpannableString( text );
        msp.setSpan( new ForegroundColorSpan( Color.GRAY ), 0, recomment_info.name_1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );
        msp.setSpan( new ForegroundColorSpan( Color.BLACK ), recomment_info.name_1.length(), text_m.length() + recomment_info.name_1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );
        msp.setSpan( new ForegroundColorSpan( Color.GRAY ), text_m.length() + recomment_info.name_1.length(), text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );

        recomment_content_obj.setTextSize( Helper.dip2px( activity, 8 ) );
        recomment_content_obj.setText( msp );

        recomment_obj.addView( recomment_content_obj, new LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );

        // time TextView
        TextView tv_time_obj = new TextView( activity );
        tv_time_obj.setTextSize( Helper.dip2px( activity, 6 ) );
        tv_time_obj.setText( recomment_info.time );

        LinearLayout.LayoutParams time_layout_params = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        time_layout_params.leftMargin = Helper.dip2px( activity, 20 );
        time_layout_params.topMargin = Helper.dip2px( activity, -10 );
        time_layout_params.bottomMargin = Helper.dip2px( activity, 10 );
        recomment_obj.addView( tv_time_obj, time_layout_params );
    }

    public void showWriteCommentView( final Activity activity ) {
        YYShowDialog.createScrollViewDialog( activity, activity.getString( R.string.item_name_1 ), new YYShowDialog.onDialogListener() {
            public void onInitContentView( View view ) {
                View content_view = LayoutInflater.from( activity ).inflate( R.layout.write_comment_view, null );
                ((ScrollView)view).addView( content_view );
            }
            public void onInitBottomView( View view ) {
            }
            public void onDismiss() {
            }
        });
    }
}

