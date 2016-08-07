package com.yy2039.chigga;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.text.format.Time;
import android.widget.ImageView;
import android.graphics.drawable.AnimationDrawable;
import android.view.ViewGroup;
import android.util.Log;

public class YYListView extends ListView implements OnScrollListener {
    private View yy_header_view = null;
    private AnimationDrawable yy_header_anim = null;
    private int yy_header_padding_top;
    private int yy_header_content_height;

    private View yy_footer_view = null;
    private AnimationDrawable yy_footer_anim = null;

    private static final int yy_action_none         = 0;
    private static final int yy_action_loading      = 1;
    private static final int yy_action_refresh      = 2;
    private static final int yy_action_refreshing   = 3;

    private int yy_current_action = yy_action_none;

    private static final int yy_action_min_distance = 20;
    private int yy_action_start_y;

    private int yy_first_visible_item;
    private boolean yy_is_bottom;
    private Integer yy_last_first_visible_postion = null;

    // 
    public interface onYYListViewActionEnd {
        void onActionEnd( boolean bSucceed );
    }
    public interface onYYListViewListener {
        void onRefresh( onYYListViewActionEnd yy_action_end );
        void onLoad( onYYListViewActionEnd yy_action_end );
    }
    private onYYListViewListener yy_list_view_listener;

    private Time yy_last_refresh_time;
    private onYYListViewActionEnd yy_list_view_refresh_end;

    private onYYListViewActionEnd yy_list_view_load_end;
    
    public YYListView( Context context ) {
        super( context );

        initView( context );
    }
    
    public YYListView( Context context, AttributeSet attrs ) {
        super( context, attrs );

        initView( context );
    }
    
    public YYListView( Context context, AttributeSet attrs, int defStyle ) {
        super( context, attrs, defStyle );

        initView( context );
    }
    
    public void setYYListViewListener( onYYListViewListener yy_listener ) {
        yy_list_view_listener = yy_listener;
    }

    private void initView( Context context ) {
        yy_last_refresh_time = new Time();
        yy_last_refresh_time.setToNow();

        // 
        yy_header_view = LayoutInflater.from( context ).inflate( R.layout.yy_list_view_header, null );

        ImageView iv_header = (ImageView)yy_header_view.findViewById( R.id.refresh_anim );
        yy_header_anim = (AnimationDrawable)iv_header.getBackground();

        yy_header_padding_top = yy_header_view.getPaddingTop();

		measureView( yy_header_view );
		yy_header_content_height = yy_header_view.getMeasuredHeight();

        updateHeaderPaddingTop( -yy_header_content_height );

        addHeaderView( yy_header_view );

        // 
        yy_footer_view = LayoutInflater.from( context ).inflate( R.layout.yy_list_view_footer, null );
        yy_footer_view.setVisibility( View.INVISIBLE );
        addFooterView( yy_footer_view );

        // 
        yy_list_view_refresh_end = new onYYListViewActionEnd() {
            public void onActionEnd( boolean bSucceed ) {
                yy_current_action = yy_action_none;

                final int from = 0;
                final int to = -yy_header_content_height;
                YYTween.getInstance().start( 500, 0, new YYTween.onTweenListener() {
                    public void onUpdate( float time ) {
                        updateHeaderPaddingTop( (int)( time * ( to - from ) + from ) );
                    }
                    public void onEnd() {
                        updateHeaderPaddingTop( to );
                    }
                });

                if( bSucceed ) {
                    yy_last_refresh_time.setToNow();

                    TextView tv = (TextView)yy_header_view.findViewById( R.id.refresh_tips );
                    tv.setText( "刚刚" );
                }
            }
        };

        yy_list_view_load_end = new onYYListViewActionEnd() {
            public void onActionEnd( boolean bSucceed ) {
                yy_current_action = yy_action_none;

                TextView tv = (TextView)yy_footer_view.findViewById( R.id.loading_tips );
                tv.setText( "上拉加载更多" );
            }
        };

        // 
        setOnScrollListener( this );
    }

    @Override
    public void onScroll( AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount ) {
    	yy_first_visible_item = firstVisibleItem;

        yy_is_bottom = ( visibleItemCount + firstVisibleItem == totalItemCount );
    }

    @Override
    public void onScrollStateChanged( AbsListView view, int scrollState ) {
        if( scrollState == OnScrollListener.SCROLL_STATE_IDLE ) {
            yy_last_first_visible_postion = getFirstVisiblePosition();
        }

        if( yy_list_view_listener == null || yy_current_action != yy_action_none ) {
            return;
        }

        if( scrollState == OnScrollListener.SCROLL_STATE_IDLE && yy_is_bottom ) {
            yy_current_action = yy_action_loading;

            yy_footer_view.setVisibility( View.VISIBLE );

            TextView tv = (TextView)yy_footer_view.findViewById( R.id.loading_tips );
            tv.setText( "加载中..." );

            yy_list_view_listener.onLoad( yy_list_view_load_end );
        }
    }

    @Override
    public boolean onTouchEvent( MotionEvent ev ) {
        switch( ev.getAction() ) {
            case MotionEvent.ACTION_DOWN:
                if( yy_first_visible_item == 0 ) {
                    yy_current_action = yy_action_refresh;
                    yy_action_start_y = (int)ev.getY();

                    yy_header_anim.start();

                    TextView tv = (TextView)yy_header_view.findViewById( R.id.refresh_tips );
                    tv.setText( getRefreshTimeInterval() );
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if( yy_current_action == yy_action_refresh ) {
                    if( (int)ev.getY() - yy_action_start_y >= yy_action_min_distance ) {
                        if( yy_list_view_listener != null ) {
                            yy_current_action = yy_action_refreshing;

                            yy_list_view_listener.onRefresh( yy_list_view_refresh_end );
                        } else {
                            yy_current_action = yy_action_none;
                        }
                    } else {
                        yy_current_action = yy_action_none;
                    }
                }

                int yy_scroll_back_height = (int)ev.getY() - yy_action_start_y;
                if( yy_scroll_back_height > 0 ) {
                    Log.v( "Fly", "yy_scroll_back_height : " + yy_scroll_back_height );
                    final int from = yy_scroll_back_height - yy_header_content_height;
                    Log.v( "Fly", "from : " + from );
                    final int to = ( yy_current_action == yy_action_refreshing ? 0 : -yy_header_content_height );
                    Log.v( "Fly", "to : " + to );
                    YYTween.getInstance().start( 500, 0, new YYTween.onTweenListener() {
                        public void onUpdate( float time ) {
                            updateHeaderPaddingTop( (int)( time * ( to - from ) + from ) );
                        }
                        public void onEnd() {
                            updateHeaderPaddingTop( to );
                        }
                    });
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if( yy_current_action == yy_action_refresh ) {
                    int padding_top = (int)ev.getY() - yy_action_start_y - yy_header_content_height;
                    updateHeaderPaddingTop( padding_top );
                }
                break;
        }

        return super.onTouchEvent( ev );
    }

    private void updateHeaderPaddingTop( int padding_top ) {
        yy_header_view.setPadding( yy_header_view.getPaddingLeft(), padding_top, yy_header_view.getPaddingRight(), yy_header_view.getPaddingBottom() );
        yy_header_view.invalidate();
    }

    private String getRefreshTimeInterval() {
        Time now = new Time();
        now.setToNow();

        long diff = now.toMillis( true ) - yy_last_refresh_time.toMillis( true );

        int days = (int)(diff / (1000 * 60 * 60 * 24));
        if( days > 0 ) {
            return String.format( "%d 天前", days );
        }

        int hours = (int)(diff / ( 1000 * 60 * 60 ));
        if( hours > 0 ) {
            return String.format( "%d 小时前", hours );
        }

        int minutes = (int)(diff / ( 1000 * 60 ));
        if( minutes > 0 ) {
            return String.format( "%d 分钟前", minutes );
        }

        return "刚刚";
    }

	// 用来计算header大小的。比较隐晦。因为header的初始高度就是0,貌似可以不用。
	private void measureView( View child ) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if( p == null ) {
			p = new ViewGroup.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec( 0, 0 + 0, p.width );
		int childHeightSpec;
		if( p.height > 0 ) {
			childHeightSpec = MeasureSpec.makeMeasureSpec( p.height, MeasureSpec.EXACTLY );
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec( 0, MeasureSpec.UNSPECIFIED );
		}

		child.measure( childWidthSpec, childHeightSpec );
	}

}
