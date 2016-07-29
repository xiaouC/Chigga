package com.yy2039.chigga;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.content.DialogInterface;
import android.widget.LinearLayout;
import android.view.LayoutInflater;

public class YYShowDialog {
    public interface onDialogListener {
        void onInitContentView( View view );
        void onInitBottomView( View view );
        void onDismiss();
    }

    public static YYShowDialog createListViewDialog( Activity activity, String title, onDialogListener dialog_listener ) {
        return new YYShowDialog( activity, true, title, dialog_listener );
    }

    public static YYShowDialog createScrollViewDialog( Activity activity, String title, onDialogListener dialog_listener ) {
        return new YYShowDialog( activity, false, title, dialog_listener );
    }

    public Dialog yy_dlg = null;
    public View yy_view = null;
    public onDialogListener yy_dialog_listener = null;

    YYShowDialog( Activity activity, boolean is_list_view, String title, onDialogListener dialog_listener ) {
        yy_dialog_listener = dialog_listener;

        yy_dlg = new Dialog( activity, R.style.fill_activity_view );

        // content view
        yy_view = View.inflate( activity, is_list_view ? R.layout.yy_show_dialog_list_view : R.layout.yy_show_dialog_view, null );
        yy_dlg.setContentView( yy_view );

        TextView tv_title = (TextView)yy_view.findViewById( R.id.title );
        tv_title.setText( title );

        yy_dialog_listener.onInitContentView( yy_view.findViewById( R.id.content_container ) );
        yy_dialog_listener.onInitBottomView( yy_view.findViewById( R.id.bottom_container ) );

        // fill activity
        android.view.WindowManager.LayoutParams p = yy_dlg.getWindow().getAttributes();

        // activity width & height
        Rect outRect = new Rect();  
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame( outRect );
        p.height = (int) (outRect.height());
        p.width = (int) (outRect.width());

        // // screen width & height
        // WindowManager m = activity.getWindowManager();  
        // Display d = m.getDefaultDisplay();
        // p.height = (int) (d.getHeight());
        // p.width = (int) (d.getWidth());

        yy_dlg.getWindow().setAttributes(p);

        // 
        Helper.startDialogShowAnimation( yy_view, null );

        // key back
        yy_dlg.setOnKeyListener( new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey( DialogInterface dialog, int keyCode, KeyEvent event ) {
                if( keyCode == KeyEvent.KEYCODE_BACK ) {
                    if( yy_dlg != null ) {
                        final Dialog tmp_ad = yy_dlg;
                        yy_dlg = null;

                        Helper.startDialogDismissAnimation( yy_view, new Helper.onAnimationEndListener() {
                            public void onAnimationEnd() {
                                tmp_ad.dismiss();

                                if( yy_dialog_listener != null ) {
                                    yy_dialog_listener.onDismiss();
                                    yy_dialog_listener = null;
                                }
                            }
                        });
                    }

                    return true;
                }

                return false;
            }
        });

        // button back
        LinearLayout ll_back_obj = (LinearLayout)yy_view.findViewById( R.id.btn_back );
        ll_back_obj.setClickable( true );
        ll_back_obj.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                if( yy_dlg != null ) {
                    final Dialog tmp_ad = yy_dlg;
                    yy_dlg = null;

                    Helper.startDialogDismissAnimation( yy_view, new Helper.onAnimationEndListener() {
                        public void onAnimationEnd() {
                            tmp_ad.dismiss();

                            if( yy_dialog_listener != null ) {
                                yy_dialog_listener.onDismiss();
                                yy_dialog_listener = null;
                            }
                        }
                    });
                }
            }
        });

        yy_dlg.show();
    }

}
