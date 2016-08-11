package com.yy2039.chigga;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class YYBackViewActivity extends Activity {
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        setContentView( getContentViewResID() );

        initView();
    }

    public int getContentViewResID() {
        return 0;
    }

    public String getContentViewTitle() {
        return "";
    }

    public void initView() {
        // button back
        LinearLayout ll_back_obj = (LinearLayout)findViewById( R.id.btn_back );
        ll_back_obj.setClickable( true );
        ll_back_obj.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // title
        TextView tv_obj = (TextView)findViewById( R.id.title );
        tv_obj.setText( getContentViewTitle() );
    }

    public boolean onKeyDown( int keyCode, KeyEvent event ) {
        if( keyCode == KeyEvent.KEYCODE_BACK ) {
        }

        return false;
    }
}

