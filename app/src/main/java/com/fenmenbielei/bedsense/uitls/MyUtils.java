package com.fenmenbielei.bedsense.uitls;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.wnhz.shidaodianqi.R;


public class MyUtils {

    public static Dialog callUpdialDialog(Context context, String phoneNumber, View.OnClickListener clickListener){
        final Dialog dialog = new Dialog(context, R.style.MyDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.call_up_dialog);
        dialog.findViewById(R.id.call_up).setOnClickListener(clickListener);
        dialog.findViewById(R.id.call_off).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView telephone_number = (TextView) dialog.findViewById(R.id.telephone_number);
        telephone_number.setText(phoneNumber);
        return  dialog;
    }




}
