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
public class WeitiaoW3Fragment extends WeitiaoBaseFragment implements View.OnTouchListener {

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
    @BindView(R.id.view_beituitiaozheng)
    AnjianWeitiaoView beituitiaozhengView;
    @BindView(R.id.view_yaobutiaozheng)
    AnjianWeitiaoView yaobutiaozhengView;
    @BindView(R.id.view_tuibutiaozheng)
    AnjianWeitiaoView tuibutiaozhengView;

    // 循环
    @BindView(R.id.layout_xunhuan)
    LinearLayout xunhuanLayout;
    @BindView(R.id.view_quanshengxunhuan)
    AnjianChangTuoYuanView quanshengxunhuanView;
    @BindView(R.id.view_beituixunhuan)
    AnjianChangTuoYuanView beituixunhuanView;
    @BindView(R.id.view_yaobuxunhuan)
    AnjianChangTuoYuanView yaobuxunhuanView;
    @BindView(R.id.view_tuibuxunhuan)
    AnjianChangTuoYuanView tuibuxunhuanView;


    private long eventDownTime = 0L;

    AnimationDrawable animationDrawable = null;


    // 1:调整、2:循环
    private int currentPage = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_weitiao_w3, container, false);
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

        headLayout.setOnTouchListener(this);
        quanshengxunhuanView.setOnTouchListener(this);
        beituixunhuanView.setOnTouchListener(this);
        yaobuxunhuanView.setOnTouchListener(this);
        tuibuxunhuanView.setOnTouchListener(this);



        beibutiaozhengView.setChildTouchListener(new ChildTouchListener() {
            @Override
            public void onTopTouch(MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    setTitle(R.string.beibutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 03 97 01");
                    setTopIconAndTitle(R.drawable.ic_beibu, R.string.beibutiaozheng);
                } else if (MotionEvent.ACTION_UP == event.getAction()) {
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
                } else if (MotionEvent.ACTION_UP == event.getAction()) {
                    setTopIconAndTitle(R.drawable.ic_beibu, R.string.beibutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 00 D7 00");
                    stopAnimation();
                }
            }
        });

        beituitiaozhengView.setChildTouchListener(new ChildTouchListener() {
            @Override
            public void onTopTouch(MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    setTitle(R.string.beituitiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 05 00 2B 87 1E");
                    setTopIconAndTitle(R.drawable.ic_yijiankandianshi, R.string.beituitiaozheng);
                } else if (MotionEvent.ACTION_UP == event.getAction()) {
                    setTopIconAndTitle(R.drawable.ic_yijiankandianshi, R.string.beituitiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 00 D7 00");
                    stopAnimation();
                }
            }

            @Override
            public void onBottomTouch(MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    setTitle(R.string.beituitiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 05 00 2C C6 DC");
                    setTopIconAndTitle(R.drawable.ic_yijiankandianshi, R.string.beituitiaozheng);
                } else if (isUPorCancel(event.getAction())) {
                    setTopIconAndTitle(R.drawable.ic_yijiankandianshi, R.string.beituitiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 00 D7 00");
                    stopAnimation();
                }
            }
        });

        yaobutiaozhengView.setChildTouchListener(new ChildTouchListener() {
            @Override
            public void onTopTouch(MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    setTitle(R.string.yaobutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 0D 16 C5");
                    setTopIconAndTitle(R.drawable.ic_tunbu, R.string.yaobutiaozheng);
                } else if (isUPorCancel(event.getAction())) {
                    setTopIconAndTitle(R.drawable.ic_tunbu, R.string.yaobutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 00 D7 00");
                    stopAnimation();
                }
            }

            @Override
            public void onBottomTouch(MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    setTitle(R.string.yaobutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 0E 56 C4");
                    setTopIconAndTitle(R.drawable.ic_tunbu, R.string.yaobutiaozheng);
                } else if (isUPorCancel(event.getAction())) {
                    setTopIconAndTitle(R.drawable.ic_tunbu, R.string.yaobutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 00 D7 00");
                    stopAnimation();
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
                    stopAnimation();
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
                    stopAnimation();
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


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (v.getId()) {
            case R.id.layout_head:
                if (MotionEvent.ACTION_DOWN == action) {
                    eventDownTime = System.currentTimeMillis();
                    timeHandler.sendEmptyMessageDelayed(currentPage, DEFAULT_INTERVAL);
                } else if (MotionEvent.ACTION_UP == action) {
                    timeHandler.removeMessages(TIAOZHENG_WHAT);
                }
                break;
            case R.id.view_quanshengxunhuan:
                setTopIconAndTitle(R.drawable.ic_beibu, R.string.quanshenxunhuan);
                if (MotionEvent.ACTION_DOWN == action) {
                    sendBlueCmd("FF FF FF FF 05 00 05 00 E4 C7 4A");
                }
                break;
            case R.id.view_beituixunhuan:
                setTopIconAndTitle(R.drawable.ic_yijiankandianshi, R.string.beituixunhuan);
                if (MotionEvent.ACTION_DOWN == action) {
                    sendBlueCmd("FF FF FF FF 05 00 05 00 E8 C7 4F");
                }
                break;
            case R.id.view_yaobuxunhuan:
                setTopIconAndTitle(R.drawable.ic_tunbu_light, R.string.tunbuxunhuan);
                if (MotionEvent.ACTION_DOWN == action) {
                    sendBlueCmd("FF FF FF FF 05 00 05 00 E6 46 8B");
                }
                break;
            case R.id.view_tuibuxunhuan:
                setTopIconAndTitle(R.drawable.ic_jiaobu, R.string.tuibuxunhuan);
                if (MotionEvent.ACTION_DOWN == action) {
                    sendBlueCmd("FF FF FF FF 05 00 05 00 E5 06 8A");
                }
                break;
        }
        return false;
    }

    private static final int TIAOZHENG_WHAT = 1;
    private static final int XUNHUAN_WHAT = 2;

    private Handler timeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TIAOZHENG_WHAT:
                    tiaozhengLongClick();
                    currentPage = XUNHUAN_WHAT;
                    break;
                case XUNHUAN_WHAT:
                    xunhuanLongClick();
                    currentPage = TIAOZHENG_WHAT;
                    break;
                default:
                    break;
            }
        }
    };


    private void tiaozhengLongClick() {
        tiaozhengLayout.setVisibility(View.GONE);
        xunhuanLayout.setVisibility(View.VISIBLE);
    }

    private void xunhuanLongClick() {
        tiaozhengLayout.setVisibility(View.VISIBLE);
        xunhuanLayout.setVisibility(View.GONE);
    }
}