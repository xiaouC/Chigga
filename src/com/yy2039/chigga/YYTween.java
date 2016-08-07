package com.yy2039.chigga;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import android.text.format.Time;

public class YYTween {
    public static YYTween yy_tween_instance = null;
    public static YYTween getInstance() {
        if( yy_tween_instance == null ) {
            yy_tween_instance = new YYTween();
        }
        return yy_tween_instance;
    }

    public interface onTweenListener {
        void onUpdate( float time );
        void onEnd();
    }

    class TweenObj {
        long start_time;
        long duration;
        int loop_count;

        onTweenListener tween_listener;

        TweenObj( long st, long d, int lc, onTweenListener tl ) {
            start_time = st;
            duration = d;
            loop_count = lc;
            tween_listener = tl;
        }
    }

    private List<TweenObj> tween_objs = new ArrayList<TweenObj>();

    private int yy_tween_schedule_handler = -1;
    private YYSchedule.onScheduleAction yy_schedule_action = new YYSchedule.onScheduleAction() {
        public void doSomething() {
            Time now = new Time();
            now.setToNow();

            long now_time = now.toMillis( true );

            Iterator<TweenObj> iter = tween_objs.iterator();
            while( iter.hasNext() ) {
                TweenObj t_obj = iter.next();

                boolean rm_flag = false;

                double dt = (double)( now_time - t_obj.start_time ) / (double)t_obj.duration;
                if( dt > 1 ) {
                    dt = dt - 1;

                    if( t_obj.loop_count > 0 ) {
                        --t_obj.loop_count;
                    } else if( t_obj.loop_count == 0 ) {
                        dt = 1;

                        rm_flag = true;

                        iter.remove();
                    }
                }

                // update
                t_obj.tween_listener.onUpdate( (float)dt );

                if( rm_flag ) {
                    t_obj.tween_listener.onEnd();
                }
            }

            if( tween_objs.size() == 0 ) {
                YYSchedule.getInstance().cancelSchedule( yy_tween_schedule_handler );

                yy_tween_schedule_handler = -1;
            }
        }
    };

    YYTween() {
    }

    public void start( long duration, int loop_count, onTweenListener t_listener ) {
        Time now = new Time();
        now.setToNow();

        long now_time = now.toMillis( true );

        tween_objs.add( new TweenObj( now_time, duration, loop_count, t_listener ) );

        if( yy_tween_schedule_handler == -1 ) {
            yy_tween_schedule_handler = YYSchedule.getInstance().scheduleCircle( 3, yy_schedule_action );
        }
    }
}
