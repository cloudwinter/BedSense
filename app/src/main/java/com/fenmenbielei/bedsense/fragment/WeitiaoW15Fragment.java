package com.fenmenbielei.bedsense.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fenmenbielei.bedsense.view.AnjianChangTuoYuanView;
import com.fenmenbielei.bedsense.view.AnjianWeitiaoView;
import com.fenmenbielei.bedsense.view.ChildTouchListener;
import com.fenmenbielei.bedsense.view.ProlateItemSwitchView;
import com.wnhz.shidaodianqi.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 微调
 */
public class WeitiaoW15Fragment extends WeitiaoBaseFragment implements View.OnTouchListener {

    public static final String TAG = "WeitiaoFragment";

    @BindView(R.id.item_tongbukz)
    ProlateItemSwitchView tongbukzView;

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
    @BindView(R.id.view_yaobutiaozheng)
    AnjianWeitiaoView yaobutiaozhengView;
    @BindView(R.id.view_zhengtishengjiang)
    AnjianWeitiaoView zhengtishengjiangView;
    @BindView(R.id.view_tuibutiaozheng)
    AnjianWeitiaoView tuibutiaozhengView;



    private long eventDownTime = 0L;

    AnimationDrawable animationDrawable = null;

    // 1:调整、2:循环
    private int currentPage = 1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_weitiao_w15, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        tongbukzView.setVisibility(View.GONE);
        tongbukzView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tongbukzView.getSelected()) {
                    sendBlueCmd("FF FF FF FF 01 00 09 0B 00 11 04");
                } else {
                    sendBlueCmd("FF FF FF FF 01 00 09 0B 01 12 04");
                }
            }
        });

        beibutiaozhengView.setChildTouchListener(new ChildTouchListener() {
            @Override
            public void onTopTouch(MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    setTitle(R.string.beibutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 03 97 01");
                    startAnimation(R.drawable.beibutzf);
                } else if (isUPorCancel(event.getAction())) {
                    stopAnimation();
                    setTopIconAndTitle(R.drawable.beibutz, R.string.beibutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 00 D7 00");
                }
            }

            @Override
            public void onBottomTouch(MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    setTitle(R.string.beibutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 04 D6 C3");
                    startAnimation(R.drawable.beibutz);
                } else if (isUPorCancel(event.getAction())) {
                    stopAnimation();
                    setTopIconAndTitle(R.drawable.beibutz, R.string.beibutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 00 D7 00");
//                    stopAnimation();
                }
            }
        });

        yaobutiaozhengView.setChildTouchListener(new ChildTouchListener() {
            @Override
            public void onTopTouch(MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    setTitle(R.string.yaobutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 0D 16 C5");
                    startAnimation(R.drawable.tunbutz);
                } else if (isUPorCancel(event.getAction())) {
                    stopAnimation();
                    setTopIconAndTitle(R.drawable.tunbutzf, R.string.yaobutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 00 D7 00");
                }
            }

            @Override
            public void onBottomTouch(MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    setTitle(R.string.yaobutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 0E 56 C4");
                    startAnimation(R.drawable.tunbutzf);
                } else if (isUPorCancel(event.getAction())) {
                    stopAnimation();
                    setTopIconAndTitle(R.drawable.tunbutzf, R.string.yaobutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 00 D7 00");
                }
            }
        });

        zhengtishengjiangView.setChildTouchListener(new ChildTouchListener() {
            @Override
            public void onTopTouch(MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    setTitle(R.string.zhengtishengjiang);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 01 16 C0");
                    setTopIconAndTitle(R.drawable.tuiup, R.string.zhengtishengjiang);
                } else if (isUPorCancel(event.getAction())) {
//                    stopAnimation();
                    setTopIconAndTitle(R.drawable.tuiup, R.string.zhengtishengjiang);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 00 D7 00");
                }
            }

            @Override
            public void onBottomTouch(MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    setTitle(R.string.zhengtishengjiang);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 02 56 C1\n");
                    setTopIconAndTitle(R.drawable.tuiup, R.string.zhengtishengjiang);
                } else if (isUPorCancel(event.getAction())) {
//                    stopAnimation();
                    setTopIconAndTitle(R.drawable.tuiup, R.string.zhengtishengjiang);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 00 D7 00");
                }
            }
        });

        tuibutiaozhengView.setChildTouchListener(new ChildTouchListener() {
            @Override
            public void onTopTouch(MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    setTitle(R.string.tuibutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 06 57 02");
                    startAnimation(R.drawable.jiaobutz);
                } else if (isUPorCancel(event.getAction())) {
                    stopAnimation();
                    setTopIconAndTitle(R.drawable.jiaobutzf, R.string.tuibutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 00 D7 00");
                }
            }

            @Override
            public void onBottomTouch(MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    setTitle(R.string.tuibutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 07 96 C2");
                    startAnimation(R.drawable.jiaobutzf);
                } else if (isUPorCancel(event.getAction())) {
                    stopAnimation();
                    setTopIconAndTitle(R.drawable.jiaobutzf, R.string.tuibutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 00 D7 00");

                }
            }
        });

    }

    private void startAnimation(int gifResId) {
        topIconImgView.setBackground(null);
//        topIconImgView.setBackgroundResource(R.drawable.bg_top_img_placehoder);
        Glide.with(getContext()).load(gifResId).diskCacheStrategy(DiskCacheStrategy.ALL).into(topIconImgView);
    }

    private void stopAnimation() {
        topIconImgView.setImageDrawable(null);
        Glide.with(getContext()).clear(topIconImgView);
    }

    @Override
    public void onTongbukzEvent(boolean show, boolean open) {
        tongbukzView.setVisibility(show ? View.VISIBLE : View.GONE);
        tongbukzView.setSelected(open);
        tongbukzView.setTitle(open ? getString(R.string.tongbukz_on) : getString(R.string.tongbukz_off));
    }

    /**
     * 设置顶部icon和title
     *
     * @param iconResId
     * @param titleResId
     */
    private void setTopIconAndTitle(int iconResId, int titleResId) {
        topIconImgView.setImageDrawable(ContextCompat.getDrawable(getContext(), iconResId));
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


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
