package com.example.yanhheng.myxlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Scroller;

/**
 * Created by yanheng on 2016/09/04.
 * genmanabu@gmail.com
 */
public class MyXListView extends ListView implements AbsListView.OnScrollListener{

    private MyHeaderView myHeaderView;
    private View header_content;
    private int header_height;
    private float lastY=-1f;
    private int firstVisibleItem;
    private int totalItemCount;
    private MyfoodView myfoodView;
    private int FOOTVIEW_MERGE_BOTTOM=100;
    private Scroller scroller;

    class SCROLLER_STATE{
        public static final int IDEL = 0;
        public static final int REFRESHING = 1;
        public static final int LOADING =2;
    }
    private int scrollerState=SCROLLER_STATE.IDEL;
    public MyXListView(Context context) {
        this(context,null);
    }

    public MyXListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyXListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        scroller = new Scroller(getContext());
        this.setOnScrollListener(this);
    }

    private void initView() {
        myHeaderView = new MyHeaderView(getContext());
        this.addHeaderView(myHeaderView);
        header_content = myHeaderView.findViewById(R.id.xlistview_header_content);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                header_height = header_content.getHeight();
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        //footer view
        myfoodView = new MyfoodView(getContext());
        this.addFooterView(myfoodView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch(ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(lastY==-1f){
                    lastY = ev.getRawY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float gapY = ev.getRawY()-lastY;
                boolean canUpdateHeader=(firstVisibleItem==0&&(gapY>0||myHeaderView.getVisibleHeight()>0));
                boolean canUpdateFooter=(getLastVisiblePosition()==(this.totalItemCount-1)&&(gapY<0||myfoodView.getMarginBottom()>0));
                float radio = 1.8f;
                if(canUpdateHeader){
                    updateHeaderHeight(gapY/radio);
                }else if(canUpdateFooter){
                    updateFooterView(gapY/radio);
                }
                lastY=ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                lastY=-1;
                if(firstVisibleItem==0){
                    if(myHeaderView.getVisibleHeight()>header_height){
                        myHeaderView.setState(MyHeaderView.STATE.REFRESHING);
                        if(listviewlistener!=null)listviewlistener.setRefreshing();
                    }
                    resetHeaderHeight();
                }else if(getLastVisiblePosition() == totalItemCount-1){
                    if(myfoodView.getMarginBottom()>FOOTVIEW_MERGE_BOTTOM){
                        myfoodView.setState(MyfoodView.STATE.LOADING);
                        myfoodView.setMarginBottom(FOOTVIEW_MERGE_BOTTOM);
                        if(listviewlistener!=null)listviewlistener.setLoading();
                    }else{
                        resetFooterViewMarginBottom();
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void resetFooterViewMarginBottom() {
        myfoodView.setMarginBottom(0);
        scrollerState=SCROLLER_STATE.LOADING;
        scroller.startScroll(0,myfoodView.getMarginBottom(),0,0-myfoodView.getMarginBottom(),400);
        postInvalidate();
    }

    private void updateFooterView(float f) {
        myfoodView.setMarginBottom((int) (myfoodView.getMarginBottom()-f));
        if(myfoodView.getMarginBottom()>FOOTVIEW_MERGE_BOTTOM){
            myfoodView.setState(MyfoodView.STATE.READY);
        }else{
            myfoodView.setState(MyfoodView.STATE.NORMAL);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(scrollerState==SCROLLER_STATE.REFRESHING&&scroller.computeScrollOffset()){
            myHeaderView.setVisibleHeight(scroller.getCurrY());
        }else if(scrollerState==SCROLLER_STATE.LOADING&&scroller.computeScrollOffset()){
            myfoodView.setMarginBottom(scroller.getCurrY());
        }
    }

    private void resetHeaderHeight() {
        if(firstVisibleItem!=0)return;
        if(myHeaderView.getVisibleHeight()<=0)return;

        int finalHeight=0;
        if(myHeaderView.getVisibleHeight()>header_height){
//            myHeaderView.setVisibleHeight(header_height);
            finalHeight=header_height;

        }else{
            finalHeight=0;
//            myHeaderView.setVisibleHeight(0);
        }
        scrollerState=SCROLLER_STATE.REFRESHING;
        scroller.startScroll(0,myHeaderView.getVisibleHeight(),
                0,finalHeight-myHeaderView.getVisibleHeight(),400);
        postInvalidate();
    }

    private void updateHeaderHeight(float gapY) {
        if(firstVisibleItem==0){
            myHeaderView.setVisibleHeight((int) (myHeaderView.getVisibleHeight()+gapY));
            if(myHeaderView.getVisibleHeight()>header_height){
                myHeaderView.setState(MyHeaderView.STATE.READY);
            }else{
                myHeaderView.setState(MyHeaderView.STATE.NORMAL);
            }
            setSelection(0);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem=firstVisibleItem;
        this.totalItemCount=totalItemCount;
    }
    private ListViewListener listviewlistener;
    public void setListener(ListViewListener l){
        listviewlistener=l;
    }

    public interface ListViewListener {
        public void setRefreshing();
        public void setLoading();
    }
    public void setRefreshingFinished(){
        resetHeaderHeight();
    }
    public void setLoadingFinished(){
        myfoodView.setState(MyfoodView.STATE.NORMAL);
        myfoodView.setMarginBottom(0);
    }
}
