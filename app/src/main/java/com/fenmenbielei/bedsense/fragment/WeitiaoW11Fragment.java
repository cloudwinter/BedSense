package com.fenmenbielei.bedsense.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.fenmenbielei.bedsense.view.AnjianWeitiaoView;
import com.fenmenbielei.bedsense.view.ChildTouchListener;
import com.wnhz.shidaodianqi.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 微调
 */
public class WeitiaoW11Fragment extends WeitiaoBaseFragment  {

    public static final String TAG = "WeitiaoFragment";


    @BindView(R.id.img_anjian_top_icon)
    ImageView topIconImgView;
    @BindView(R.id.text_anjian_top_title)
    TextView topTitleTextView;
    @BindView(R.id.layout_head)
    FrameLayout headLayout;

    // 调整
    @BindView(R.id.layout_tiaozheng)
    LinearLayout tiaozhengLayout;
    @BindView(R.id.view_beibutiaozheng)
    AnjianWeitiaoView beibutiaozhengView;
    @BindView(R.id.view_tuibutiaozheng)
    AnjianWeitiaoView tuibutiaozhengView;
    @BindView(R.id.view_zhengtishengjiang)
    AnjianWeitiaoView zhengtishengjiangView;


    private long eventDownTime = 0L;

    AnimationDrawable animationDrawable = null;

    // 1:调整、2:循环
    private int currentPage = 1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_weitiao_w11, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {

        beibutiaozhengView.setChildTouchListener(new ChildTouchListener() {
            @Override
            public void onTopTouch(MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    setTitle(R.string.beibutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 03 97 01");
                    setTopIconAndTitle(R.drawable.ic_beibu, R.string.beibutiaozheng);
                } else if (isUPorCancel(event.getAction())) {
                    setTopIconAndTitle(R.drawable.ic_beibu, R.string.beibutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 00 D7 00");
                    stopAnimation();
                }
            }

            @Override
            public void onBottomTouch(MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    setTitle(R.string.beibutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 04 D6 C3");
                    setTopIconAndTitle(R.drawable.ic_beibu, R.string.beibutiaozheng);
                } else if (isUPorCancel(event.getAction())) {
                    setTopIconAndTitle(R.drawable.ic_beibu, R.string.beibutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 00 D7 00");
                    stopAnimation();
                }
            }
        });


        zhengtishengjiangView.setChildTouchListener(new ChildTouchListener() {
            @Override
            public void onTopTouch(MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    setTitle(R.string.zhengtishengjiang);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 0D 16 C5");
                    setTopIconAndTitle(R.drawable.ic_yijianfuyuan, R.string.zhengtishengjiang);
                } else if (isUPorCancel(event.getAction())) {
                    setTopIconAndTitle(R.drawable.ic_yijianfuyuan, R.string.zhengtishengjiang);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 00 D7 00");
//                    stopAnimation();
                }
            }

            @Override
            public void onBottomTouch(MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    setTitle(R.string.zhengtishengjiang);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 0E 56 C4");
                    setTopIconAndTitle(R.drawable.ic_yijianfuyuan, R.string.zhengtishengjiang);
                } else if (isUPorCancel(event.getAction())) {
                    setTopIconAndTitle(R.drawable.ic_yijianfuyuan, R.string.zhengtishengjiang);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 00 D7 00");
                    //stopAnimation();
                }
            }
        });

        tuibutiaozhengView.setChildTouchListener(new ChildTouchListener() {
            @Override
            public void onTopTouch(MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    setTitle(R.string.tuibutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 06 57 02");
                    setTopIconAndTitle(R.drawable.ic_jiaobu, R.string.tuibutiaozheng);
                } else if (isUPorCancel(event.getAction())) {
                    setTopIconAndTitle(R.drawable.ic_jiaobu, R.string.tuibutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 00 D7 00");
                    //stopAnimation();
                }
            }

            @Override
            public void onBottomTouch(MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    setTitle(R.string.tuibutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 07 96 C2");
                    setTopIconAndTitle(R.drawable.ic_jiaobu, R.string.tuibutiaozheng);
                } else if (isUPorCancel(event.getAction())) {
                    setTopIconAndTitle(R.drawable.ic_jiaobu, R.string.tuibutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 00 D7 00");
                    //stopAnimation();
                }
            }
        });

    }

    private void startAnimation(int animationId) {
        topIconImgView.setBackground(ContextCompat.getDrawable(getContext(), animationId));
        animationDrawable = (AnimationDrawable) topIconImgView.getBackground();
        animationDrawable.start();
    }

    private void stopAnimation() {
        if (animationDrawable != null) {
            animationDrawable.stop();
            animationDrawable = null;
        }
    }

    /**
     * 设置顶部icon和title
     *
     * @param iconResId
     * @param titleResId
     */
    private void setTopIconAndTitle(int iconResId, int titleResId) {
        topIconImgView.setBackground(ContextCompat.getDrawable(getContext(), iconResId));
        topTitleTextView.setText(getString(titleResId));
    }


    /**
     * 设置顶部的title
     *
     * @param titleResId
     */
    private void setTitle(int titleResId) {
        topTitleTextView.setText(getString(titleResId));
    }


//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        int action = event.getAction();
//        switch (v.getId()) {
//            case R.id.layout_head:
//                if (MotionEvent.ACTION_DOWN == action) {
//                    eventDownTime = System.currentTimeMillis();
//                    timeHandler.sendEmptyMessageDelayed(currentPage, DEFAULT_INTERVAL);
//                } else if (MotionEvent.ACTION_UP == action) {
//                    timeHandler.removeMessages(TIAOZHENG_WHAT);
//                }
//                break;
//        }
//        return true;
//    }




}