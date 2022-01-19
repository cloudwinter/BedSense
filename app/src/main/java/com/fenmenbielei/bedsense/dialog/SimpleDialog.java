package com.fenmenbielei.bedsense.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wnhz.shidaodianqi.R;

/**
 *  Created by wanghongchuang
 *  on 2016/8/25.
 *  email:844285775@qq.com
 */
public class SimpleDialog extends Dialog implements View.OnClickListener {

    private OnButtonClick mButtonClick;

    private int negTextColorid;
    private int posTextColorid;

    View line1;
    TextView titleTv;
    TextView rightTv;
    TextView contentTv;
    TextView leftTv;
    boolean mIsClickDismiss = true;

    public interface OnButtonClick {
        void onNegBtnClick();
        void onPosBtnClick();
    }

    public SimpleDialog(Context context, String content) {
        super(context, R.style.simpleDialog);
        init(context, content, null, null, null, null);
    }

    public SimpleDialog(Context context, String content, OnButtonClick buttonClick) {
        super(context, R.style.simpleDialog);
        init(context, content, null, context.getString(R.string.cancel), context.getString(R.string.confirm), buttonClick);
    }

    public SimpleDialog(Context context, String content, String confirm, OnButtonClick buttonClick) {
        super(context, R.style.simpleDialog);
        init(context, content, null, null, confirm, buttonClick);
    }

    public SimpleDialog(Context context, String content, String cancel, String confirm, OnButtonClick buttonClick) {
        super(context, R.style.simpleDialog);
        init(context, content, null, cancel, confirm, buttonClick);
    }

    public SimpleDialog(Context context, OnButtonClick buttonClick, String title, String content, String confirm, String cancel) {
        super(context, R.style.simpleDialog);
        init(context, content, title, cancel, confirm, buttonClick);
    }

    public void init(Context context, String content, String title, String cancel, String confirm, OnButtonClick buttonClick) {
        this.mButtonClick = buttonClick;

        View v = LayoutInflater.from(context).inflate(R.layout.dialog_simple_alert, null);
        titleTv = (TextView) v.findViewById(R.id.title);
        contentTv = (TextView) v.findViewById(R.id.content);
        rightTv = (TextView) v.findViewById(R.id.confirm);
        leftTv = (TextView) v.findViewById(R.id.cancel);
        line1 = v.findViewById(R.id.line1);
        if (title != null && content != null) {
            titleTv.setText(title);
            contentTv.setText(content);
            contentTv.setGravity(Gravity.LEFT);
            titleTv.setVisibility(View.VISIBLE);
            line1.setVisibility(View.VISIBLE);
        } else if (content != null) {
        contentTv.setText(content);
    }

        if (confirm != null) {
            rightTv.setText(confirm);
        }

        if (posTextColorid != 0) {
            rightTv.setTextColor(posTextColorid);
        }

        if (cancel != null) {
            leftTv.setText(cancel);
        } else {
            leftTv.setVisibility(View.GONE);
        }

        if (negTextColorid != 0) {
            leftTv.setTextColor(negTextColorid);
        }

        rightTv.setOnClickListener(this);
        leftTv.setOnClickListener(this);
        setContentView(v);
    }

    public void setContent(String content) {
        contentTv.setText(content);
    }

    public void setPosBtnText(String text) {
        rightTv.setText(text);
    }

    public void setPosBtnTextColor(int id) {
        posTextColorid = id;
    }

    public void setNegBtnText(String text) {
        leftTv.setText(text);
    }

    public void setNegBtnTextColor(int id) {
        negTextColorid = id;
    }

    public void setIsDismissClick(boolean isClickDismiss) {
        mIsClickDismiss = isClickDismiss;
    }

    @Override
    public void onClick(View v) {
        if (v == leftTv) {
            if (mButtonClick != null) {
                mButtonClick.onNegBtnClick();
            }
        } else if (v == rightTv) {
            if (mButtonClick != null) {
                mButtonClick.onPosBtnClick();
            }
        }
        if (mIsClickDismiss) {
            dismiss();
        }
    }
}
