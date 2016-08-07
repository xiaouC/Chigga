package com.yy2039.chigga;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.Display;
import android.graphics.Rect;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.util.Log;
import java.lang.reflect.Field; 
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class Helper {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */
    public static int dip2px( Context context, float dpValue ) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)( dpValue * scale + 0.5f );
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */
    public static int px2dip( Context context, float pxValue ) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)( pxValue / scale + 0.5f );
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static int getResourceId( String name ) {
        try {
            // 根据资源的ID的变量名获得Field的对象,使用反射机制来实现的  
            Field field = R.id.class.getField( name );
            // 取得并返回资源的id的字段(静态变量)的值，使用反射机制  
            return Integer.parseInt( field.get( null ).toString() );
        } catch (Exception e) {
            // TODO: handle exception
        }
        return 0;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static int getDrawableResourceId( String name ) {
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

    public interface onAnimationEndListener {
        void onAnimationEnd();
    }

    public static void startDialogShowAnimation( View view, onAnimationEndListener anim_end_listener ) {
        TranslateAnimation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, 
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        translate.setDuration( 200 );
        translate.setFillAfter( true );

        startDialogAnimation( view, translate, anim_end_listener );
    }

    public static void startDialogDismissAnimation( View view, onAnimationEndListener anim_end_listener ) {
        TranslateAnimation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, 
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        translate.setDuration( 200 );
        translate.setFillAfter( true );

        startDialogAnimation( view, translate, anim_end_listener );
    }

    public static void startDialogAnimation( View view, Animation anim, final onAnimationEndListener anim_end_listener ) {
        view.clearAnimation();

        if( anim_end_listener != null ) {
            anim.setAnimationListener( new Animation.AnimationListener() {
                public void onAnimationStart( Animation animation ) { }
                public void onAnimationRepeat( Animation animation ) { }
                public void onAnimationEnd( Animation animation ) {
                    anim_end_listener.onAnimationEnd();
                }
            });
        }

        view.startAnimation( anim );
    }

    public static void setImageViewResource( final ImageView iv, String URL ) {
        iv.setScaleType( ScaleType.FIT_XY );
        Bitmap bitmap = ImageDownLoader.getInstance().downloadImage( URL, new ImageDownLoader.onImageLoaderListener() {
            public void onImageLoader( Bitmap bitmap, String url ) {
                if( bitmap != null ) {
                    iv.setImageBitmap( bitmap );
                }
            }
        });

        if( bitmap != null ) {
            iv.setImageBitmap( bitmap );
        }
    }
}
