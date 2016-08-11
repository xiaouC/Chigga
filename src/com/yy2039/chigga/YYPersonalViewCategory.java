package com.yy2039.chigga;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.ImageView;

public class YYPersonalViewCategory extends YYCategory {
    private View category_personal_view;
    YYPersonalViewCategory( Activity activity, String id, String name ) {
        super( activity, id, name );
    }

    public void clearup( LinearLayout main_content_view ) {
        super.clearup( main_content_view );

        category_personal_view = null;
    }

    public View createCategoryView() {
        category_personal_view = LayoutInflater.from( category_activity ).inflate( R.layout.personal_category_view, null );

        // my attention
        View my_attention_obj = category_personal_view.findViewById( R.id.btn_my_attention );
        my_attention_obj.setClickable( true );
        my_attention_obj.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) { YYDataSource.persion_info.showMyAttentionView( category_activity ); }
        });

        // my fans
        View my_fans_obj = category_personal_view.findViewById( R.id.btn_my_fans );
        my_fans_obj.setClickable( true );
        my_fans_obj.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) { YYDataSource.persion_info.showMyFansView( category_activity ); }
        });

        // my published
        View my_published_obj = category_personal_view.findViewById( R.id.btn_my_published );
        my_published_obj.setClickable( true );
        my_published_obj.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) { YYDataSource.persion_info.showMyPublishedView( category_activity ); }
        });

        Button settings_obj = (Button)category_personal_view.findViewById( R.id.settings );
        settings_obj.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass( category_activity, YYPersonalInformationViewActivity.class );
                category_activity.startActivity( intent );
            }
        });

        return category_personal_view;
    }

    public void Refresh() {
    }

    public void updateContent() {
        // nick name
        TextView tv_nick_name_obj = (TextView)category_personal_view.findViewById( R.id.nick_name );
        tv_nick_name_obj.setText( YYDataSource.persion_info.nick_name );

        // female or male
        ImageView iv_gender_obj = (ImageView)category_personal_view.findViewById( R.id.gender );
        iv_gender_obj.setImageResource( YYDataSource.persion_info.is_female ? R.drawable.female : R.drawable.male );

        // signature
        TextView tv_signature_obj = (TextView)category_personal_view.findViewById( R.id.signature );
        tv_signature_obj.setText( YYDataSource.persion_info.signature );

        // my attentions
        TextView tv_my_attention_obj = (TextView)category_personal_view.findViewById( R.id.my_attention_count );
        tv_my_attention_obj.setText( "" + YYDataSource.persion_info.attention_count );

        // my fans
        TextView tv_my_fans_obj = (TextView)category_personal_view.findViewById( R.id.my_fans_count );
        tv_my_fans_obj.setText( "" + YYDataSource.persion_info.fans_count );

        // my published
        TextView tv_my_published_obj = (TextView)category_personal_view.findViewById( R.id.my_published_count );
        tv_my_published_obj.setText( "" + YYDataSource.persion_info.published_count );
    }
}
