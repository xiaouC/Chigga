package com.yy2039.chigga;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class YYPersonal {
    public static final int ATTENTION_STATUS_NULL       = 1;        // 相互之间都没有关注
    public static final int ATTENTION_STATUS_SINGLE     = 2;        // 你关注了对方，对方没有关注你
    public static final int ATTENTION_STATUS_EACH_OTHER = 3;        // 互相都有关注对方

    class UserSummary {
        public String   user_id;
        public String   head_icon_url;
        public String   nick_name;
        public String   signature;
        public String   desc;
        public int      attention_status;

        UserSummary( String id, String h, String n, String s, String d, int status ) {
            user_id = id;
            head_icon_url = h;
            nick_name = n;
            signature = s;
            desc = d;
            attention_status = status;
        }
    }

    // 
    public interface onAttributeListener {
        void onChanged( Attribute attr );
    }
    class Attribute {
        Attribute( String name, String value ) {
            attr_name = name;
            attr_value = value;

            attr_listeners = new HashMap<String, onAttributeListener>();
        }

        public String       attr_name;
        public String       attr_value;

        public Map<String, onAttributeListener> attr_listeners;     // first : tag
    }

    // 
    public boolean              is_login = false;

    public Map<String, Attribute> personal_attributes;

    public List<UserSummary>    attention_list;
    public List<UserSummary>    interested_fans_list;
    public List<UserSummary>    all_fans_list;

    YYPersonal() {
        is_login = false;

        // 
        personal_attributes = new HashMap<String, Attribute>();

        personal_attributes.put( "NickName",         new Attribute( "NickName", "" ) );
        personal_attributes.put( "Sex",              new Attribute( "Sex", "Male" ) );        // value : [ Male, Female ]
        personal_attributes.put( "Signature",        new Attribute( "Signature", "" ) );
        personal_attributes.put( "AttentionCounter", new Attribute( "AttentionCounter", "0" ) );
        personal_attributes.put( "FansCounter",      new Attribute( "FansCounter", "0" ) );
        personal_attributes.put( "PublishedCounter", new Attribute( "PublishedCounter", "0" ) );

        attention_list = new ArrayList<UserSummary>();
        interested_fans_list = new ArrayList<UserSummary>();
        all_fans_list = new ArrayList<UserSummary>();
    }

    public void refresh() {
        setAttribute( "NickName", "Charlie-The-Creator" );
        setAttribute( "Sex", "Male" );
        setAttribute( "Signature", "CTC-造物主，这是无法阻止的Charlie,你知道的..." );
        setAttribute( "AttentionCounter", "39" );
        setAttribute( "FansCounter", "3724" );
        setAttribute( "PublishedCounter", "68" );

        for( int i=0; i < 25; ++i ) {
            attention_list.add( new UserSummary( "", "", "奇婴扮嘻哈", "一个特立独行的奇怪婴儿，不喜勿扰。", "@小泽玛利亚 等28万人关注了", 1 ) );
        }

        for( int i=0; i < 3; ++i ) {
            interested_fans_list.add( new UserSummary( "", "", "奇婴扮嘻哈", "一个特立独行的奇怪婴儿，不喜勿扰。", "@小泽玛利亚 等28万人关注了", 1 ) );
        }

        for( int i=0; i < 25; ++i ) {
            all_fans_list.add( new UserSummary( "", "", "奇婴扮嘻哈", "一个特立独行的奇怪婴儿，不喜勿扰。", "@小泽玛利亚 等28万人关注了", 1 ) );
        }

        is_login = true;
    }

    public void attentionSomeone( String user_id, boolean is_attention, String head_icon_url, String nick_name, String signature, String desc, boolean attention_flag ) {
        if( is_login ) {
            if( is_attention ) {
                for( int i=0; i < attention_list.size(); ++i ) {
                    UserSummary us = attention_list.get( i );
                    if( us.user_id == user_id ) {
                        return;
                    }
                }

                int status = ( attention_flag ? ATTENTION_STATUS_EACH_OTHER : ATTENTION_STATUS_SINGLE );
                attention_list.add( new UserSummary( user_id, head_icon_url, nick_name, signature, desc, status ) );
            } else {
                for( int i=0; i < attention_list.size(); ++i ) {
                    UserSummary us = attention_list.get( i );
                    if( us.user_id == user_id ) {
                        attention_list.remove( i );

                        return;
                    }
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void listenerAttribute( String attr_name, String tag, onAttributeListener attr_listener ) {
        Attribute attr = personal_attributes.get( attr_name );
        if( attr != null ) {
            attr.attr_listeners.put( tag, attr_listener );
        }
    }

    public void unlistenerAttribute( String attr_name, String tag ) {
        Attribute attr = personal_attributes.get( attr_name );
        if( attr != null ) {
            attr.attr_listeners.remove( tag );
        }
    }

    // 
    public void setAttribute( String attr_name, String value ) {
        if( !is_login ) {
            return;
        }

        Attribute attr = personal_attributes.get( attr_name );
        if( attr != null ) {
            attr.attr_value = value;

            for( Map.Entry<String, onAttributeListener> item_entry : attr.attr_listeners.entrySet() ) {
                onAttributeListener attr_listener = item_entry.getValue();
                attr_listener.onChanged( attr );
            }
        }
    }

    public String getAttribute( String attr_name ) {
        Attribute attr = personal_attributes.get( attr_name );
        if( attr != null ) {
            return attr.attr_value;
        }

        return "";
    }
}
