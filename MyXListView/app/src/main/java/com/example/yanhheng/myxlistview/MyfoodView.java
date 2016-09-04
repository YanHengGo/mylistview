package com.example.yanhheng.myxlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by yanheng on 2016/09/04.
 * genmanabu@gmail.com
 */
public class MyfoodView extends LinearLayout{

    private View footerView;
    private View progressbar;
    private TextView hint_textview;
    private View content;

    class STATE{
        public static final int NORMAL=1;
        public static final int READY=2;
        public static final int LOADING=3;
    }
    private int mState= STATE.NORMAL;

    public MyfoodView(Context context) {
        this(context,null);
    }

    public MyfoodView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyfoodView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        footerView = View.inflate(getContext(), R.layout.xlistview_footer, null);
        content = footerView.findViewById(R.id.xlistview_footer_content);
        hint_textview = (TextView) footerView.findViewById(R.id.xlistview_footer_hint_textview);
        progressbar =  footerView.findViewById(R.id.xlistview_footer_progressbar);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.addView(footerView,layoutParams);
    }
    public void setState(int state){
        if(state==this.mState){
            return;
        }
        switch(state){
            case STATE.NORMAL:
                hint_textview.setVisibility(VISIBLE);
                hint_textview.setText(R.string.xlistview_footer_hint_normal);
                progressbar.setVisibility(INVISIBLE);
                break;
            case STATE.READY:
                hint_textview.setVisibility(VISIBLE);
                hint_textview.setText(R.string.xlistview_footer_hint_ready);
                progressbar.setVisibility(INVISIBLE);
                break;
            case STATE.LOADING:
                hint_textview.setVisibility(INVISIBLE);
                progressbar.setVisibility(VISIBLE);
                break;
            default:
                break;
        }
        this.mState=state;
    }
    public void setMarginBottom(int height){
        if(height<0){
            return;
        }
        LayoutParams param = (LayoutParams)content.getLayoutParams();
        param.bottomMargin=height;
        content.setLayoutParams(param);
    }
    public int getMarginBottom(){
        LayoutParams param = (LayoutParams)content.getLayoutParams();
        return param.bottomMargin;
    }
}
