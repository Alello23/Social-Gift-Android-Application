package com.example.practica2.ClassObjects;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.widget.ImageView;

import com.squareup.picasso.Transformation;
public class RoundedCornerTransformation implements Transformation {
    private final float radius;
    private final ImageView imageView;

    public RoundedCornerTransformation(float radius, ImageView imageView) {
        this.radius = radius;
        this.imageView = imageView;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        if (imageView.getWidth() <= 0 || imageView.getHeight() <= 0) {
            // Devuelve la imagen de origen sin transformar si las dimensiones son inválidas
            return source;
        }

        int targetWidth = imageView.getWidth();
        int targetHeight = imageView.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(targetWidth, targetHeight, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawRoundRect(new RectF(0, 0, targetWidth, targetHeight), radius, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, null, new RectF(0, 0, targetWidth, targetHeight), paint);

        source.recycle();

        return bitmap;
    }

    @Override
    public String key() {
        return "rounded_corner";
    }
}

