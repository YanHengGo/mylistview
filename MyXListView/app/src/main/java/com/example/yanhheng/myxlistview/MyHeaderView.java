package com.example.yanhheng.myxlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by yanheng on 2016/09/03.
 */
public class MyHeaderView extends LinearLayout{

    private View headerView;
    private ImageView header_arrow;
    private ProgressBar header_progressbar;
    private TextView header_time;
    private TextView header_hint_textview;
    private RotateAnimation animation_up;
    private final long DURATION_TIME=100L;
    private RotateAnimation animation_down;

    class STATE{
        public final static int NORMAL=1;       //
        public final static int READY=2;
        public final static int REFRESHING=3;
    }
    private int state=STATE.NORMAL;

    public MyHeaderView(Context context) {
        this(context,null);
    }

    public MyHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /*初期化
     */
    private void initView() {
//        headerViw
        headerView = View.inflate(getContext(), R.layout.xlistview_header,null);
        //init param
        LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT,0);
        this.addView(headerView,param);
        header_arrow =  (ImageView) headerView.findViewById(R.id.xlistview_header_arrow);
        header_progressbar =   (ProgressBar)headerView.findViewById(R.id.xlistview_header_progressbar);
        header_hint_textview = (TextView) headerView.findViewById(R.id.xlistview_header_hint_textview);
        header_time = (TextView) headerView.findViewById(R.id.xlistview_header_time);
        //init animation
        animation_up =  new RotateAnimation(0,-180, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation_up.setDuration(DURATION_TIME);
        animation_up.setFillAfter(true);
        animation_down =  new RotateAnimation(-180,0, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation_down.setDuration(DURATION_TIME);
        animation_down.setFillAfter(true);
    }

    public void setState(int state){
        if(this.state==state){
            return;
        }
        switch(state){
            case STATE.NORMAL:
                header_progressbar.setVisibility(View.INVISIBLE);
                header_arrow.setVisibility(View.VISIBLE);
                if(this.state==STATE.REFRESHING){
                    header_arrow.startAnimation(animation_down);
                }else if(this.state==STATE.READY){
                    header_arrow.startAnimation(animation_down);
                }
                header_hint_textview.setText(R.string.xlistview_footer_hint_normal);
                break;
            case STATE.READY:
                header_progressbar.setVisibility(View.INVISIBLE);
                header_arrow.setVisibility(View.VISIBLE);
                header_arrow.startAnimation(animation_up);
                header_hint_textview.setText(R.string.xlistview_footer_hint_ready);
                break;
            case STATE.REFRESHING:
                header_progressbar.setVisibility(View.VISIBLE);
                header_arrow.setVisibility(View.INVISIBLE);
                header_arrow.clearAnimation();
                header_hint_textview.setText(R.string.xlistview_header_hint_loading);
                break;
            default:
        }
        this.state=state;
    }
    public void setVisibleHeight(int height){
        LayoutParams layoutParam = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        headerView.setLayoutParams(layoutParam);
    }
    public int getVisibleHeight(){
        return headerView.getHeight();
    }
 }
