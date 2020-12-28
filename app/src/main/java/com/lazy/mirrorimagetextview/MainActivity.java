package com.lazy.mirrorimagetextview;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.tv_reflect);
        setGradientFont(textView, "#70C6FF", "#4570B5");
    }

    /**
     * TextView 字体渐变
     *
     * @param textView   文本框
     * @param startColor 起始颜色
     * @param endColor   终止颜色
     */
    public static void setGradientFont(TextView textView, String startColor, String endColor) {
        // Shader.TileMode.CLAMP：如果着色器超出原始边界范围，会复制边缘颜色
        // 上下渐变
        float width = textView.getPaint().measureText("推荐");
        float height = textView.getPaint().getTextSize();
        Log.d("MainActivity", "---------textview  height:" + height + "--width:" + width);
        // 上下渐变
        LinearGradient backGradient = new LinearGradient(0, 0, 0, height,
                new int[]{Color.parseColor(startColor),
                        Color.parseColor(endColor), Color.parseColor(startColor)}, null, Shader.TileMode.CLAMP);
        // 斜  45度渐变
        LinearGradient backGradient45 = new LinearGradient(0, 0, width, height,
                new int[]{Color.parseColor(startColor),
                        Color.parseColor(endColor), Color.parseColor(startColor)}, null, Shader.TileMode.CLAMP);
        // 左右渐变
        LinearGradient gradient = new LinearGradient(0, 0, width, 0,
                Color.parseColor(startColor), Color.parseColor(endColor),
                Shader.TileMode.CLAMP);

        textView.getPaint().setShader(backGradient);
        // 直接调用invalidate()方法，请求重新draw()，但只会绘制调用者本身
        textView.invalidate();
    }
}