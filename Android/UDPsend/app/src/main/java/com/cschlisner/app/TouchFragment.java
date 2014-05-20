package com.cschlisner.app;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by Cole on 3/13/14.
 */

public class TouchFragment extends Fragment {
    public TouchFragment(){
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_touchpad, container, false);
        if (v != null) {
            v.setOnTouchListener(new View.OnTouchListener() {
                int initX, initY, deltaX, deltaY, clicks;
                double dist;
                long dTime, lastUTime, lastDTime;
                @Override
                public boolean onTouch(View view, MotionEvent e) {
                    int eX = (int) e.getX(), eY = (int) e.getY();
                    int sens = Integer.valueOf(GlobalContext.mouseSensitivity);
                    try {
                        switch (e.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                if (lastDTime==0)
                                    lastDTime = e.getEventTime();
                                initX = eX;
                                initY = eY;
                                dTime = e.getDownTime();
                                if ((e.getEventTime()-lastDTime)>500) {
                                    Networking brkdwn = new Networking();
                                    brkdwn.execute("LEFTCLICKU", GlobalContext.hostIP);
                                    clicks=0;
                                }
                                lastDTime = e.getEventTime();

                                break;
                            case MotionEvent.ACTION_UP:
                                //System.out.println("click time: "+(e.getEventTime()-dTime)+" ms with "+fingersBitch+" pointers");
                                if ((e.getEventTime()-dTime) < 100 && dist<3){
                                    if (clicks==0)
                                        clicks=1;
                                    else if ((e.getEventTime()-lastUTime)<300&&clicks==1)
                                        clicks = 2;
                                }
                                else {
                                    if (clicks==2) {
                                        Networking bleh = new Networking();
                                        bleh.execute("LEFTCLICKU", GlobalContext.hostIP);
                                    }
                                    clicks = 0;
                                }
                                if (clicks>0) {
                                    Networking dwn = new Networking(), up = new Networking();
                                    dwn.execute("LEFTCLICKD", GlobalContext.hostIP);
                                    if (clicks==1)
                                        up.execute("LEFTCLICKU", GlobalContext.hostIP);
                                }
                                System.out.println((e.getEventTime()-lastUTime));
                                lastUTime = e.getEventTime();
                                System.out.println(clicks);
                                break;
                        }
                        deltaX = eX - initX;
                        deltaY = eY - initY;
                        dist = Math.hypot((double)deltaX, (double)deltaY);
                        if (dist <= 10)
                            sens = 1;
                        if (Math.abs(deltaX) > 0 && Math.abs(deltaY) > 0) {
                            Networking n = new Networking();
                            n.execute(String.format("MOUSE: %d %d", (deltaX * sens), (deltaY * sens)), GlobalContext.hostIP);
                        }
                        initX = eX;
                        initY = eY;
                    } catch(Exception exc){
                        exc.printStackTrace();
                    }
                    return true;
                }
            });
        }
        return v;
    }

}
