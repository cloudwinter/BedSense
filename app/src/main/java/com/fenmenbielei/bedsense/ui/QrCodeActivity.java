//package com.fenmenbielei.bedsense.ui;
//
//import android.os.Build;
//import android.os.Bundle;
//import android.view.WindowManager;
//import android.widget.Toast;
//
//import com.fenmenbielei.bedsense.base.BaseActivity;
//import com.fenmenbielei.bedsense.impl.ActionBarClickListener;
//import com.fenmenbielei.bedsense.javabean.ShareInfo;
//import com.fenmenbielei.bedsense.view.TranslucentActionBar;
//import com.wnhz.shidaodianqi.R;
//
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
//public class QrCodeActivity extends BaseActivity implements ActionBarClickListener {
//
//    @BindView(R.id.actionbar)
//    TranslucentActionBar actionbar;
//    //private ShareDialog shareDialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_qr_code);
//
//        ButterKnife.bind(this);
//        actionbar.setData("我的二维码", R.mipmap.ic_default_return, null, R.drawable.ic_erweima_share, null, this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            actionbar.setStatusBarHeight(getStatusBarHeight());
//        }
//    }
//
//    @Override
//    public void onLeftClick() {
//        finish();
//    }
//
//    @Override
//    public void onRightClick() {
//        //分享
//
//        ShareInfo shareInfo = new ShareInfo();
//        shareInfo.title = "全美思";
//        shareInfo.text = "专业按摩养生";
//        shareInfo.targetUrl = "http://www.baidu.com";
//
////        shareDialog = new ShareDialog(QrCodeActivity.this, shareInfo, null, new UMShareListener() {
////            @Override
////            public void onResult(SHARE_MEDIA platform) {
////                Toast.makeText(QrCodeActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
////            }
////
////            @Override
////            public void onError(SHARE_MEDIA platform, Throwable t) {
////                Toast.makeText(QrCodeActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
////            }
////
////            @Override
////            public void onCancel(SHARE_MEDIA platform) {
////                Toast.makeText(QrCodeActivity.this, "分享取消", Toast.LENGTH_SHORT).show();
////            }
////        });
////        shareDialog.show();
//    }
//}
