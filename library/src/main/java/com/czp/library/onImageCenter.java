package com.czp.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

/**
 * Created by caizepeng on 16/9/8.
 */
public class onImageCenter implements ArcProgress.OnCenterDraw {
    private Context context;
    private int rid;
    private Bitmap mBmp,mTarget;
    public onImageCenter(Context context, int rid) {
        this.context = context;
        this.rid = rid;
        mBmp = BitmapFactory.decodeResource(context.getResources(), rid);
    }

    @Override
    public void draw(Canvas canvas, RectF rectF, float x, float y, float storkeWidth, int progress) {

        if(mTarget == null){
            mTarget = Bitmap.createScaledBitmap(mBmp, (int) (rectF.right - storkeWidth * 2), (int) (rectF.right - storkeWidth * 2), false);
        }

        Bitmap target = Bitmap.createBitmap(mTarget, 0, 0, mTarget.getWidth(), mTarget.getHeight());
        float sx = x - target.getWidth() / 2;
        float sy = y - target.getHeight() / 2;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawCircle(x, y, (rectF.right - storkeWidth * 2) / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(target, sx, sy, paint);
    }

}
