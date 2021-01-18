package com.lazy.mirrorimagetextview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;

/**
 * author : xu
 * date : 2021/1/18 18:21
 * description :  梯形渐变view
 */
public class TIView extends androidx.appcompat.widget.AppCompatTextView {

    Paint paint;
    private int[] colorArray;
    private int bottomPadding = 20;

    public void setMainColor(int[] colorArray) {
        this.colorArray = colorArray;
        invalidate();
    }

    public TIView(Context context) {
        super(context);
        paint = new Paint();
    }

    public TIView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    public TIView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // LinearGradient 第一个参数第二个参数为 起始位置x,y  三四参数为终点位置x,y。
        // 如果x不变则为y轴渐变， y不变则为x轴渐变
        // 第五个参数为颜色渐变，此处为红色渐变为绿色
        // 第七个参数为渐变次数，可repeat
        Shader mShader = new LinearGradient(0, 0, 0, getHeight(), colorArray, null, Shader.TileMode.CLAMP);
        paint.setShader(mShader);

        Path path5 = new Path();
        path5.moveTo(0, 0);
        path5.lineTo(getWidth(), 0);
        path5.lineTo(getWidth() - bottomPadding, getHeight());
        path5.lineTo(bottomPadding, getHeight());
        path5.close();
        canvas.drawPath(path5, paint);
    }
}