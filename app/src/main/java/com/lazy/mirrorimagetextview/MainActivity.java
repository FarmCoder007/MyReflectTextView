package com.lazy.mirrorimagetextview;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ImageView picture_qian;
    TIView tiview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.tv_reflect);
        setGradientFont(textView, "#70C6FF", "#4570B5");

        picture_qian = findViewById(R.id.picture_qian);
        tiview = findViewById(R.id.tiview);

        setImage();
    }

    /**
     * 搞图片
     */
    private void setImage() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.listen_page_play_icon, options);
        int ratio = calculateInSampleSize(options, 1080, 1920);//计算压缩比
        options.inSampleSize = ratio;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.listen_page_play_icon, options);

        Log.d("main", "--------------------MainActivity:" + bitmap);
        // 选取主颜色渐变
        getMainColor(bitmap);
        //  设置图片投影
//        picture_qian.setImageBitmap(createReflectedImage(bitmap));
    }

    /**
     * 计算压缩比
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        if (width > reqWidth || height > reqHeight) {
            inSampleSize++;
            int widthRatio = reqWidth / width;
            int heightRatio = reqHeight / height;
            int ratio = Math.min(widthRatio, heightRatio);
            while (ratio > inSampleSize) {
                inSampleSize *= 2;
            }

        }
        return inSampleSize;
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


    /**
     * 设置图片倒影
     *
     * @param originalImage
     * @return
     */
    public Bitmap createReflectedImage(Bitmap originalImage) {

        final int reflectionGap = 0;

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
                height / 2, width, height / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
                (height + height / 2), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);

        canvas.drawBitmap(originalImage, 0, 0, null);

        Paint defaultPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);

        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0,
                originalImage.getHeight(), 0, bitmapWithReflection.getHeight()
                + reflectionGap, 0x70ffffff, 0x00ffffff,
                Shader.TileMode.MIRROR);
        paint.setShader(shader);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
                + reflectionGap, paint);

        return bitmapWithReflection;
    }

    /**
     * 吸取主色设置背景
     *
     * @param bitmap
     */
    private void getMainColor(Bitmap bitmap) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(@Nullable Palette palette) {

                Palette.Swatch swatch = palette.getVibrantSwatch();
//                Palette.Swatch swatch = palette.getDominantSwatch();
//                Palette.Swatch swatch = palette.getLightMutedSwatch();
//                Palette.Swatch swatch = palette.getLightVibrantSwatch();
//                Palette.Swatch swatch = palette.getDarkMutedSwatch();

                int mainColor = swatch.getRgb();
                int one = Color.argb(100, Color.red(mainColor), Color.green(mainColor), Color.blue(mainColor));
                int two = Color.argb(80, Color.red(mainColor), Color.green(mainColor), Color.blue(mainColor));
                int three = Color.argb(60, Color.red(mainColor), Color.green(mainColor), Color.blue(mainColor));
                int four = Color.argb(40, Color.red(mainColor), Color.green(mainColor), Color.blue(mainColor));
                int five = Color.argb(0, Color.red(mainColor), Color.green(mainColor), Color.blue(mainColor));
                int colors[] = {one, two, three, four, five};
                tiview.setMainColor(colors);
//                GradientDrawable bg = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
//                int sdk = android.os.Build.VERSION.SDK_INT;
//                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                    picture_qian.setBackgroundDrawable(bg);
//                } else {
//                    picture_qian.setBackground(bg);
//                }
            }
        });
    }

    /**
     * 高斯模糊
     *
     * @param bitmap
     * @param context
     * @return
     */
    public static Bitmap blurBitmap(Bitmap bitmap, Context context) {

        // Let's create an empty bitmap with the same size of the bitmap we want
        // to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        // Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(context);//RenderScript是Android在API 11之后增加的

        // Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        // Create the Allocations (in/out) with the Renderscript and the in/out
        // bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        // Set the radius of the blur
        blurScript.setRadius(25.f);

        // Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        // Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        // recycle the original bitmap
        bitmap.recycle();

        // After finishing everything, we destroy the Renderscript.
        rs.destroy();

        return outBitmap;

    }
}