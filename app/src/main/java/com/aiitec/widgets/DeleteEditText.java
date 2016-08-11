package com.aiitec.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.aiitec.openapi.utils.AiiUtil;

/**
 * @author Anthony
 * @createTime 2016-06-20
 * 带删除按钮的输入框，点击X就删除文字，并提供一个删除监听接口
 *
 */
public class DeleteEditText extends EditText {

    private int padding, drawablePadding;
    private Bitmap deleteImg ;
    private int deleteX, deleteY, deleteW, deleteH;

    public DeleteEditText(Context context){
        super(context);
        init();
    }
    public DeleteEditText(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }
    public DeleteEditText(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        padding = AiiUtil.dip2px(getContext(), 4);
        drawablePadding = AiiUtil.dip2px(getContext(), 8);
        int backgroundRec = getResources().getIdentifier("common_ic_delete", "drawable", getContext().getPackageName());
        if (backgroundRec > 0) {
            Drawable drawable = getResources().getDrawable(backgroundRec);
            deleteImg = ((BitmapDrawable) drawable).getBitmap();
        }
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public void setDrawablePadding(int drawablePadding) {
        this.drawablePadding = drawablePadding;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!TextUtils.isEmpty(getText().toString().trim()) && deleteImg != null){
            int x = getWidth() - deleteImg.getWidth()- drawablePadding-padding;
            int y = (getHeight() - deleteImg.getHeight())>>1;
            //删除按钮区域比，按钮大4dp
            deleteX = x-padding;
            deleteW = x + deleteImg.getWidth() + padding;
            deleteY = y - padding;
            deleteH = y + deleteImg.getHeight() + padding;
            Paint paint = new Paint();
            canvas.drawBitmap(deleteImg, x, y, paint);

        }
    }

    private long downTime ;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                long upTime = System.currentTimeMillis();
                if(upTime-downTime < 500 ){
                    //短按才算，长按不算
                    if(event.getX() > deleteX && event.getX()<deleteW && event.getY() > deleteY && event.getY()<deleteH){
                        //点击删除按钮区域
                        setText("");
                        if(onDeleteListener != null){
                            onDeleteListener.onDelete();
                        }
                    }

                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }


    private OnDeleteListener onDeleteListener;

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    /**
     * 删除按钮监听接口
     */
    public interface OnDeleteListener{
        void onDelete();
    };

}
