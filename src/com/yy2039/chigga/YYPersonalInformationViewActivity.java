package com.yy2039.chigga;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.LinearLayout;

public class YYPersonalInformationViewActivity extends YYBackViewActivity {
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public int getContentViewResID() {
        return R.layout.personal_information_view;
    }

    @Override
    public String getContentViewTitle() {
        return getString( R.string.personal_information_view_title );
    }

    @Override
    public void initView() {
        super.initView();

        // head icon
        ImageButton ib_obj = (ImageButton)findViewById( R.id.head_icon );
        ib_obj.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        // nick name
        EditText dt_obj = (EditText)findViewById( R.id.nick_name );

        final TextView tv_sex_male_obj = (TextView)findViewById( R.id.sex_male_text );
        final TextView tv_sex_female_obj = (TextView)findViewById( R.id.sex_female_text );
        final TextView tv_sex_others_obj = (TextView)findViewById( R.id.sex_others_text );

        // sex male
        LinearLayout ll_sex_male_obj = (LinearLayout)findViewById( R.id.sex_male );
        ll_sex_male_obj.setClickable( true );
        ll_sex_male_obj.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_sex_male_obj.setBackgroundResource( R.drawable.personal_information_view_sex_selected_bg );
                tv_sex_female_obj.setBackgroundResource( R.drawable.personal_information_view_sex_normal_bg );
                tv_sex_others_obj.setBackgroundResource( R.drawable.personal_information_view_sex_normal_bg );
            }
        });

        // sex female
        LinearLayout ll_sex_female_obj = (LinearLayout)findViewById( R.id.sex_female );
        ll_sex_female_obj.setClickable( true );
        ll_sex_female_obj.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_sex_male_obj.setBackgroundResource( R.drawable.personal_information_view_sex_normal_bg );
                tv_sex_female_obj.setBackgroundResource( R.drawable.personal_information_view_sex_selected_bg );
                tv_sex_others_obj.setBackgroundResource( R.drawable.personal_information_view_sex_normal_bg );
            }
        });

        // sex others
        LinearLayout ll_sex_others_obj = (LinearLayout)findViewById( R.id.sex_others );
        ll_sex_others_obj.setClickable( true );
        ll_sex_others_obj.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_sex_male_obj.setBackgroundResource( R.drawable.personal_information_view_sex_normal_bg );
                tv_sex_female_obj.setBackgroundResource( R.drawable.personal_information_view_sex_normal_bg );
                tv_sex_others_obj.setBackgroundResource( R.drawable.personal_information_view_sex_selected_bg );
            }
        });

        // confirm button
        ImageButton ib_confirm_obj = (ImageButton)findViewById( R.id.btn_confirm );
        ib_confirm_obj.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}
