package com.lazy.mirrorimagetextview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;


/**
 * author : xu
 * date : 2021/1/18 16:32
 * description :
 */
public class ColorImaeView extends AppCompatImageView {
    int mainColor;

    Paint paint;

    public ColorImaeView(Context context) {
        super(context);
        paint = new Paint();
    }

    public ColorImaeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    public ColorImaeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
    }

    public void setColor(int mainColor) {
        this.mainColor = mainColor;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        LinearGradient shader = new LinearGradient(0,
                0, 0, getHeight(), new int[]{0xFF + mainColor, 0xE6 + mainColor, 0x80 + mainColor
                , 0x40 + mainColor, mainColor}, null,
                Shader.TileMode.MIRROR);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRect(0, 0,400,300, paint);
    }
}
