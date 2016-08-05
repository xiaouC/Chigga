package com.yy2039.chigga;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import android.util.Log;


public class YYDataSource {
    public static PersonalInfo persion_info = new PersonalInfo( "Charlie-The-Creator", false, "CTC-造物主，这是无法阻止的Charlie,你知道的...", 39, 3724, 68 );

    public static int hp_curr_index = 0;
    public static List<HomePageItem> hp_item_list = new ArrayList<HomePageItem>();

    public YYDataSource() {
        /*
        getHomePageItemList();

        for( int i=0; i < 25; ++i ) {
            persion_info.addAttentionInfo( "", "奇婴扮嘻哈", "一个特立独行的奇怪婴儿，不喜勿扰。", "@小泽玛利亚 等28万人关注了", 1 );
        }

        for( int i=0; i < 3; ++i ) {
            persion_info.addInterestedFansInfo( "", "奇婴扮嘻哈", "一个特立独行的奇怪婴儿，不喜勿扰。", "@小泽玛利亚 等28万人关注了", 1 );
        }

        for( int i=0; i < 25; ++i ) {
            persion_info.addFansInfo( "", "奇婴扮嘻哈", "一个特立独行的奇怪婴儿，不喜勿扰。", "@小泽玛利亚 等28万人关注了", 1 );
        }

        String URL = "http://23.105.198.234/api/categories";
        YYHttpRequest.sendGetRequest( URL, null, new YYHttpRequest.onResponseListener() {
            public void onResponse( String data ) {
                Log.v( "Fly", "onResponse : " + data );
                try {
                    JSONObject msg_info = new JSONObject( data );
                } catch ( JSONException e ) {
                    e.printStackTrace();
                }
            }
        });
        */
    }

    /*
    public static void getHomePageItemList() {
        for( int i=0; i < 5; ++i ) {
            HomePageItem hp_item = new HomePageItem(
                        "http://img0.imgtn.bdimg.com/it/u=3176925970,1218337176&fm=21&gp=0.jpg",
                        "☆RDS速报☆一只开着X6豪车、戴金项链的熊猫正在占领街头！！！",
                        "#街头",
                        "#涂鸦",
                        "#SWAG",
                        8642,
                        36,
                        698,
                        false,
                        false,
                        "        王胤祺《LOVE IT》MV由全新实力班底合力打造，由王胤祺工作室CMC音乐娱乐公司统筹，知名影视工作室Funny Studio导演制作，专业造型发型总设计师AFLOAT的Henry担纲美术造型创作，深圳著名舞团MY STAR负责舞蹈创作。另外MV还邀请到一众好友客串出演，力求画面表现出最好的感觉。"
                        );

            for( int c=0; c < 3; ++c ) {
                String head = "";
                String name = "Motion_M";
                String time = "6月30日 17:07";
                String content = "略屌哦，哪里可以下载到这首歌和MV？";
                int comment_index = hp_item.addComment( head, name, time, content );

                for( int r=0; r < 3; ++r ) {
                    String re_name_1 = "DJ Jennies";
                    String re_name_2 = "Motion_M";
                    String re_content = "是的，混音的唱段混进去EDM的Trap Music，商业化流行。";
                    String re_time = "6月30日 17:20";
                    hp_item.addRecomment( comment_index, re_name_1, re_name_2, re_content, re_time );
                }
            }

            hp_item_list.add( hp_item );
        }
    }
    */

    public static void getContentList( String id, boolean is_delete, int page_size, int current_page, YYHttpRequest.onResponseListener rsp_listener ) {
        String URL = "http://23.105.198.234/api/contents";

        Map<String, String> data = new HashMap<String, String>();
        //data.put( "_id", id );
        //data.put( "deleted", is_delete ? "true" : "false" );
        //data.put( "pageSize", String.format( "%d", page_size ) );
        //data.put( "currentPage", String.format( "%d", current_page ) );

        YYHttpRequest.sendGetRequest( URL, data, rsp_listener );
    }
}
