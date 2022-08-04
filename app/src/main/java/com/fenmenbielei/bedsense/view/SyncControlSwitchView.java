package com.fenmenbielei.bedsense.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.wnhz.shidaodianqi.R;

import androidx.annotation.Nullable;


/**
 * 长椭圆的item
 * Created by xiayundong on 2022/1/3.
 */
public class SyncControlSwitchView extends LinearLayout {

    private Context mContext;

    private TextView mTitleView;
    private ImageView mSwitchImageView;

    private boolean mSelected;

    public SyncControlSwitchView(Context context) {
        super(context, null);
    }

    public SyncControlSwitchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        String title = "";
        if (attrs != null) {
            TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.SyncControlSwitchView);
            title = typedArray.getString(R.styleable.SyncControlSwitchView_title);
        }
        setOrientation(HORIZONTAL);
        inflate(mContext, R.layout.view_sync_control_switch,this);
        mTitleView = findViewById(R.id.tv_title);
        mSwitchImageView = findViewById(R.id.ic_switch);
        mTitleView.setText(title);
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
        if (selected) {
            mSwitchImageView.setImageResource(R.mipmap.dian_normal);
        } else {
            mSwitchImageView.setImageResource(R.mipmap.dian_selected);
        }
    }

    public boolean getSelected() {
        return mSelected;
    }


    public void setTitle(String title) {
        mTitleView.setText(title);
    }
}
