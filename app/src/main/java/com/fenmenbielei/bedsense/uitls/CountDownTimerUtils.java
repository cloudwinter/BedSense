package com.fenmenbielei.bedsense.uitls;

import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;


import com.fenmenbielei.bedsense.MyApplication;
import com.wnhz.shidaodianqi.R;

public class CountDownTimerUtils extends CountDownTimer {
    private TextView mTextView;

    public CountDownTimerUtils(TextView textView, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mTextView = textView;
    }

    private boolean hasBg = false;

    public void setHasBg(boolean hasBg) {
        this.hasBg = hasBg;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mTextView.setClickable(false); //设置不可点击
        mTextView.setText(millisUntilFinished / 1000 + "s");  //设置倒计时时间
        Log.e("倒计时",millisUntilFinished / 1000 + "s");
        if (!hasBg)
            mTextView.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.set_right_text)); //设置按钮为灰
        SpannableString spannableString = new SpannableString(mTextView.getText().toString());
        ForegroundColorSpan span = new ForegroundColorSpan(MyApplication.getInstance().getResources().getColor(R.color.set_right_text));
        spannableString.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan span2 = new ForegroundColorSpan(MyApplication.getInstance().getResources().getColor(R.color.set_right_text));
        spannableString.setSpan(span2, 2, mTextView.getText().length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        mTextView.setText(spannableString);
    }

    @Override
    public void onFinish() {
        mTextView.setText("5s");
        mTextView.setClickable(false);//重新获得点击
        if (!hasBg)
            mTextView.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.set_right_text)); //设置按钮为灰
    }
}