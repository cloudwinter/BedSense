package com.fenmenbielei.bedsense.fragment;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
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
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.fenmenbielei.bedsense.uitls.ScreenUtils;
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
public class WeitiaoW1Fragment extends WeitiaoBaseFragment implements View.OnTouchListener {

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
    @BindView(R.id.view_tunbutiaozheng)
    AnjianWeitiaoView tunbutiaozhengView;
    @BindView(R.id.view_tuobutiaozheng)
    AnjianWeitiaoView tuobutiaozhengView;
    @BindView(R.id.view_tuibutiaozheng)
    AnjianWeitiaoView tuibutiaozhengView;

    // 循环
    @BindView(R.id.layout_xunhuan)
    LinearLayout xunhuanLayout;
    @BindView(R.id.view_quanshengxunhuan)
    AnjianChangTuoYuanView quanshengxunhuanView;
    @BindView(R.id.view_tunbuxunhuan)
    AnjianChangTuoYuanView tunbuxunhuanView;
    @BindView(R.id.view_toubuxunhuan)
    AnjianChangTuoYuanView toubuxunhuanView;
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
        View view = inflater.inflate(R.layout.fragment_weitiao_w1, container, false);
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
        tunbuxunhuanView.setOnTouchListener(this);
        toubuxunhuanView.setOnTouchListener(this);
        tuibuxunhuanView.setOnTouchListener(this);

        beibutiaozhengView.setChildTouchListener(new ChildTouchListener() {
            @Override
            public void onTopTouch(MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    setTitle(R.string.beibutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 03 97 01");
                    topIconImgView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_top_img_placehoder));
                    Glide.with(getContext()).load(R.drawable.beibutzf).diskCacheStrategy(DiskCacheStrategy.ALL).into(topIconImgView);

                } else if (isUPorCancel(event.getAction())) {
                    topIconImgView.setImageDrawable(null);
                    Glide.with(getContext()).clear(topIconImgView);
                    setTopIconAndTitle(R.drawable.ic_beibu, R.string.beibutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 00 D7 00");
                }
            }

            @Override
            public void onBottomTouch(MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    setTitle(R.string.beibutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 04 D6 C3");
                    topIconImgView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_top_img_placehoder));
                    Glide.with(getContext()).load(R.drawable.beibutz)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(topIconImgView);
                } else if (isUPorCancel(event.getAction())) {
                    topIconImgView.setImageDrawable(null);
                    Glide.with(getContext()).clear(topIconImgView);
                    setTopIconAndTitle(R.drawable.ic_beibu, R.string.beibutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 00 D7 00");
//                    stopAnimation();
                }
            }
        });

        tunbutiaozhengView.setChildTouchListener(new ChildTouchListener() {
            @Override
            public void onTopTouch(MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    setTitle(R.string.tunbutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 0D 16 C5");
                    topIconImgView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_top_img_placehoder));
                    Glide.with(getContext()).load(R.drawable.tunbutzf)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(topIconImgView);
                } else if (isUPorCancel(event.getAction())) {
                    topIconImgView.setImageDrawable(null);
                    Glide.with(getContext()).clear(topIconImgView);
                    setTopIconAndTitle(R.drawable.ic_tunbu, R.string.tunbutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 00 D7 00");
//                    stopAnimation();
                }
            }

            @Override
            public void onBottomTouch(MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    setTitle(R.string.tunbutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 0E 56 C4");
                    setTopIconAndTitle(R.drawable.ic_tunbu, R.string.tunbutiaozheng);
                    topIconImgView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_top_img_placehoder));
                    Glide.with(getContext()).load(R.drawable.tunbutz)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).into(topIconImgView);
                } else if (isUPorCancel(event.getAction())) {
                    topIconImgView.setImageDrawable(null);
                    Glide.with(getContext()).clear(topIconImgView);
                    setTopIconAndTitle(R.drawable.ic_tunbu, R.string.tunbutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 00 D7 00");
//                    stopAnimation();
                }
            }
        });

        tuobutiaozhengView.setChildTouchListener(new ChildTouchListener() {
            @Override
            public void onTopTouch(MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    setTitle(R.string.toubutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 01 16 C0");
                    topIconImgView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_top_img_placehoder));
                    Glide.with(getContext()).load(R.drawable.toubutzf).diskCacheStrategy(DiskCacheStrategy.ALL).into(topIconImgView);;
                } else if (isUPorCancel(event.getAction())) {
                    topIconImgView.setImageDrawable(null);
                    Glide.with(getContext()).clear(topIconImgView);
                    setTopIconAndTitle(R.drawable.ic_toubu, R.string.toubutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 00 D7 00");
//                    stopAnimation();
                }
            }

            @Override
            public void onBottomTouch(MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    setTitle(R.string.toubutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 02 56 C1");
                    topIconImgView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_top_img_placehoder));
                    Glide.with(getContext()).load(R.drawable.toubutz).diskCacheStrategy(DiskCacheStrategy.ALL).into(topIconImgView);
                } else if (isUPorCancel(event.getAction())) {
                    topIconImgView.setImageDrawable(null);
                    Glide.with(getContext()).clear(topIconImgView);
                    setTopIconAndTitle(R.drawable.ic_toubu, R.string.toubutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 00 D7 00");
//                    stopAnimation();
                }
            }
        });

        tuibutiaozhengView.setChildTouchListener(new ChildTouchListener() {
            @Override
            public void onTopTouch(MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    setTitle(R.string.tuibutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 06 57 02");
//                    startAnimation(R.drawable.weitiao_tuibu_top_animation);
                    topIconImgView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_top_img_placehoder));
                    Glide.with(getContext()).load(R.drawable.jiaobutzf).diskCacheStrategy(DiskCacheStrategy.ALL).into(topIconImgView);
                } else if (isUPorCancel(event.getAction())) {
                    topIconImgView.setImageDrawable(null);
                    Glide.with(getContext()).clear(topIconImgView);
                    setTopIconAndTitle(R.drawable.ic_jiaobu, R.string.tuibutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 00 D7 00");
//                    stopAnimation();
                }
            }

            @Override
            public void onBottomTouch(MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    setTitle(R.string.tuibutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 07 96 C2");
                    topIconImgView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_top_img_placehoder));
                    Glide.with(getContext()).load(R.drawable.jiaobutz).diskCacheStrategy(DiskCacheStrategy.ALL).into(topIconImgView);;
                } else if (isUPorCancel(event.getAction())) {
                    topIconImgView.setImageDrawable(null);
                    Glide.with(getContext()).clear(topIconImgView);
                    setTopIconAndTitle(R.drawable.ic_jiaobu, R.string.tuibutiaozheng);
                    sendBlueCmd("FF FF FF FF 05 00 00 00 00 D7 00");
                    //stopAnimation();
                }
            }
        });

    }


    @Override
    public void onTongbukzEvent(boolean show, boolean open) {
        tongbukzView.setVisibility(show ? View.VISIBLE : View.GONE);
        tongbukzView.setSelected(open);
        tongbukzView.setTitle(open ? getString(R.string.tongbukz_on) : getString(R.string.tongbukz_off));
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
            case R.id.view_tunbuxunhuan:
                setTopIconAndTitle(R.drawable.ic_tunbu_light, R.string.tunbuxunhuan);
                if (MotionEvent.ACTION_DOWN == action) {
                    sendBlueCmd("FF FF FF FF 05 00 05 00 E6 46 8B");
                }
                break;
            case R.id.view_toubuxunhuan:
                setTopIconAndTitle(R.drawable.ic_toubu, R.string.toubuxunhuan);
                if (MotionEvent.ACTION_DOWN == action) {
                    sendBlueCmd("FF FF FF FF 05 00 05 00 E3 86 88");
                }
                break;
            case R.id.view_tuibuxunhuan:
                setTopIconAndTitle(R.drawable.ic_jiaobu, R.string.tuibuxunhuan);
                if (MotionEvent.ACTION_DOWN == action) {
                    sendBlueCmd("FF FF FF FF 05 00 05 00 E5 06 8A");
                }
                break;
        }
        return true;
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
