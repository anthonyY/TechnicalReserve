package com.aiitec.widgets;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.aiitec.technicalreserve.R;


/**
 * 分享弹窗口类
 *
 * @author Anthony
 */
public class ShareDialog1 extends Dialog {


    // private LinearLayout ll_share;
    private Context context;
    private View view;
    /**
     * 需要多个动画，因为用一个的话，执行过的控件会再次执行，就会感觉到抖动
     */
    private Animation animationIn1, animationIn2, animationIn3, animationIn4, animationIn5;
    private Animation animationOut1, animationOut2, animationOut3, animationOut4, animationOut5;
    private static final int sleepTime = 100;
    private TextView tv_weixin, tv_circle, tv_qq, tv_share_qzone, tv_share_sina;

    public ShareDialog1(Context context) {
        this(context, R.layout.share1, R.style.LoadingDialog);
    }

    /**
     * @param context
     * @param layoutId
     * @param theme
     */
    public ShareDialog1(Context context, int layoutId, int theme) {
        super(context, theme);
        this.context = context;
        view = LayoutInflater.from(context).inflate(layoutId, null);
        // ll_share = (LinearLayout) view.findViewById(R.id.ll_share);

        tv_weixin = (TextView) view.findViewById(R.id.tv_share_weixin);
        tv_circle = (TextView) view.findViewById(R.id.tv_share_weixin_circle);
        tv_qq = (TextView) view.findViewById(R.id.tv_share_qq);
        tv_share_qzone = (TextView) view.findViewById(R.id.tv_share_qzone);
        tv_share_sina = (TextView) view.findViewById(R.id.tv_share_sina);

        setContentView(view);

        animationIn1 = AnimationUtils.loadAnimation(context,
                R.anim.push_bottom_in1);
        animationIn2 = AnimationUtils.loadAnimation(context,
                R.anim.push_bottom_in1);
        animationIn3 = AnimationUtils.loadAnimation(context,
                R.anim.push_bottom_in1);
        animationIn4 = AnimationUtils.loadAnimation(context,
                R.anim.push_bottom_in1);
        animationIn5 = AnimationUtils.loadAnimation(context,
                R.anim.push_bottom_in1);
        animationOut1 = AnimationUtils.loadAnimation(context,
                R.anim.push_bottom_out1);
        animationOut2 = AnimationUtils.loadAnimation(context,
                R.anim.push_bottom_out1);
        animationOut3 = AnimationUtils.loadAnimation(context,
                R.anim.push_bottom_out1);
        animationOut4 = AnimationUtils.loadAnimation(context,
                R.anim.push_bottom_out1);
        animationOut5 = AnimationUtils.loadAnimation(context,
                R.anim.push_bottom_out1);
        animationOut1.setFillAfter(true);
        animationOut2.setFillAfter(true);
        animationOut3.setFillAfter(true);
        animationOut4.setFillAfter(true);
        animationOut5.setFillAfter(true);

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }


    /**
     * 隐藏部分控件
     */
    public void goneView(int id) {
        if (view != null) {
            View goneView = view.findViewById(id);
            if (goneView != null) {
                goneView.setVisibility(View.GONE);
            }
        }
    }

    public void visibilityView(int id) {
        if (view != null) {
            View goneView = view.findViewById(id);
            if (goneView != null) {
                goneView.setVisibility(View.VISIBLE);
            }
        }
    }

    public View getView() {
        return view;
    }

    @Override
    public void dismiss() {

        if (isShowing()) {
            if (view != null) {
                view.clearAnimation();
            }
            if (view != null) {
                tv_weixin.startAnimation(animationOut1);
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            Thread.sleep(sleepTime);
                            animHandler.sendEmptyMessage(12);
                            Thread.sleep(sleepTime);
                            animHandler.sendEmptyMessage(13);
                            Thread.sleep(sleepTime);
                            animHandler.sendEmptyMessage(14);
                            Thread.sleep(sleepTime);
                            animHandler.sendEmptyMessage(15);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
            animHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            superDismiss();
                                        }
                                    },
                    sleepTime * 2 + animationOut1.getDuration());
            // 按标准来说是*2，不过从主观视觉觉得没有必要等动画完全执行完，所以*1
        }
    }

    Handler animHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    tv_circle.startAnimation(animationIn2);
                    break;
                case 2:
                    tv_qq.startAnimation(animationIn3);
                    break;
                case 3:
                    tv_share_qzone.startAnimation(animationIn4);
                    break;
                case 4:
                    tv_share_sina.startAnimation(animationIn5);
                    break;
                case 12:
                    tv_circle.startAnimation(animationOut2);
                    break;
                case 13:
                    tv_qq.startAnimation(animationOut3);
                    break;
                case 14:
                    tv_share_qzone.startAnimation(animationOut4);
                    break;
                case 15:
                    tv_share_sina.startAnimation(animationOut5);
                    break;
            }
            return false;
        }
    });

    /**
     * 用动画的形式把控件移到别的地方，要不然一开始就看见它了，然后才又出现动画，就不行了
     */
    private void animHideView() {
        TranslateAnimation defaultAnimation = new TranslateAnimation(0, 0, 400,
                400);
        defaultAnimation.setDuration(0);
        defaultAnimation.setFillAfter(true);
        tv_weixin.setAnimation(defaultAnimation);
        tv_circle.setAnimation(defaultAnimation);
        tv_qq.setAnimation(defaultAnimation);
    }

    @Override
    public void show() {
        if (!isShowing()) {
            super.show();
        }
        if (view != null) {

            animHideView();

            tv_weixin.startAnimation(animationIn1);
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(sleepTime);
                        animHandler.sendEmptyMessage(1);
                        Thread.sleep(sleepTime);
                        animHandler.sendEmptyMessage(2);
                        Thread.sleep(sleepTime);
                        animHandler.sendEmptyMessage(3);
                        Thread.sleep(sleepTime);
                        animHandler.sendEmptyMessage(4);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }

    public void superDismiss() {// 这是无奈之举，没有更好的办法了super.dismiss();不能放在内部类
        super.dismiss();
    }

    public void switchToActivity(Context context, Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        dismiss();
    }
}