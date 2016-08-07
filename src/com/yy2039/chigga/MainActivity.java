package com.yy2039.chigga;

import java.util.List;
import java.util.ArrayList;

import android.os.Bundle;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.util.Log;
import android.graphics.Rect;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
    public List<YYCategory> all_category_list;

    public boolean content_layout_flag = false;
    public LinearLayout main_content_view = null;
    public int content_height = 0;
    public int content_width = 0;

    public interface onLoadingListener {
        boolean onCheckLoadingEnd();
        void onLoadingEnd();
    }
    public Dialog welcome_dlg = null;
    public int welcome_schedule_handler = -1;

    public View.OnClickListener first_bottom_view_click_listener = null;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main_view_new );

        ImageDownLoader.init( this );

        // 
        main_content_view = (LinearLayout)findViewById( R.id.main_content );
        main_content_view.getViewTreeObserver().addOnGlobalLayoutListener( new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                main_content_view.getViewTreeObserver().removeGlobalOnLayoutListener( this );
                content_height = main_content_view.getHeight();
                content_width = main_content_view.getWidth();
                Log.v( "Fly", "main_content_view height : " + main_content_view.getHeight() );
                Log.v( "Fly", "main_content_view width : " + main_content_view.getWidth() );

                content_layout_flag = true;
            }
        });

        // 
        loadCategoryList();

        // 
        initBottomView();

        // welcome page
        showWelcomePage( new onLoadingListener() {
            public boolean onCheckLoadingEnd() {
                if( !content_layout_flag ) {
                    return false;
                }

                return true;
            }
            public void onLoadingEnd() {
                first_bottom_view_click_listener.onClick( null );
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void loadCategoryList() {
        all_category_list = new ArrayList<YYCategory>();

        all_category_list.add( new YYListViewCategory( this, "", "" ) );
        all_category_list.add( new YYListViewCategory( this, "", "" ) );
        all_category_list.add( new YYPersonalViewCategory( this, "", "" ) );
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private int last_selected_bottom_index = -1;
    private static final int[] bottom_res_ids = { R.id.item_btn_1, R.id.item_btn_2, R.id.item_btn_3 };
    public void initBottomView() {
        for( int i=0; i < bottom_res_ids.length; ++i ) {
            final int index = i;

            View view_obj = findViewById( bottom_res_ids[index] );
            view_obj.setClickable( true );

            View.OnClickListener click_listener = new View.OnClickListener() {
                @Override
                public void onClick( View v ) {
                    if( last_selected_bottom_index == index ) {
                        return;
                    }

                    last_selected_bottom_index = index;

                    for( int k=0; k < bottom_res_ids.length; ++k ) {
                        findViewById( bottom_res_ids[k] ).setBackgroundResource( index == k ? R.drawable.tab_selected : R.drawable.tab_normal );
                    }

                    // 
                    showCategory( index );
                }
            };

            view_obj.setOnClickListener( click_listener );

            if( i == 0 ) {
                first_bottom_view_click_listener = click_listener;
            }
        }
    }

    public void showWelcomePage( final onLoadingListener loading_listener ) {
        welcome_dlg = new Dialog( this, R.style.fill_activity_view );

        // content view
        View welcome_view = View.inflate( this, R.layout.welcome_view, null );
        welcome_dlg.setContentView( welcome_view );

        // fill activity
        android.view.WindowManager.LayoutParams p = welcome_dlg.getWindow().getAttributes();

        // activity width & height
        Rect outRect = new Rect();  
        getWindow().getDecorView().getWindowVisibleDisplayFrame( outRect );
        p.height = (int) (outRect.height());
        p.width = (int) (outRect.width());

        // // screen width & height
        // WindowManager m = activity.getWindowManager();  
        // Display d = m.getDefaultDisplay();
        // p.height = (int) (d.getHeight());
        // p.width = (int) (d.getWidth());

        welcome_dlg.getWindow().setAttributes(p);

        // key back
        welcome_dlg.setOnKeyListener( new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey( DialogInterface dialog, int keyCode, KeyEvent event ) {
                if( keyCode == KeyEvent.KEYCODE_BACK ) {
                    return true;
                }

                return false;
            }
        });

        welcome_dlg.show();

        //
        welcome_schedule_handler = YYSchedule.getInstance().scheduleCircle( 100, new YYSchedule.onScheduleAction() {
            public void doSomething() {
                if( loading_listener.onCheckLoadingEnd() ) {
                    Log.v( "Fly", "onCheckLoadingEnd 11111111111111111111111111111111111111" );
                    YYSchedule.getInstance().cancelSchedule( welcome_schedule_handler );

                    if( welcome_dlg != null ) {
                        welcome_dlg.dismiss();
                        welcome_dlg = null;
                    }

                    loading_listener.onLoadingEnd();
                }
            }
        });
    }

    private YYCategory cur_sel_category = null;
    public void showCategory( int index ) {
        if( cur_sel_category != null ) {
            cur_sel_category.clearup( main_content_view );
            cur_sel_category = null;
        }

        // 
        cur_sel_category = all_category_list.get( index );
        if( cur_sel_category != null ) {
            cur_sel_category.setView( main_content_view, content_width, content_height );
        }
    }
}
