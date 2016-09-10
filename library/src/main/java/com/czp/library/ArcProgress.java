package com.czp.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ProgressBar;

/**
 * Created by caizepeng on 16/9/6.
 */
public class ArcProgress extends ProgressBar {
    public static final int STYLE_TICK = 1;
    public static final int STYLE_ARC = 0;
    private final int DEFAULT_LINEHEIGHT = dp2px(15);
    private final int DEFAULT_mTickWidth = dp2px(2);
    private final int DEFAULT_mRadius = dp2px(72);
    private final int DEFAULT_mUnmProgressColor = 0xffeaeaea;
    private final int DEFAULT_mProgressColor = Color.YELLOW;
    private final int DEFAULT_OFFSETDEGREE = 60;
    private final int DEFAULT_DENSITY = 4;
    private final int MIN_DENSITY = 2;
    private final int MAX_DENSITY = 8;
    private int mStylePogress = STYLE_TICK;
    private boolean mBgShow;
    private float mRadius;
    private int mArcbgColor;
    private int mBoardWidth;
    private int mDegree = DEFAULT_OFFSETDEGREE;
    private RectF mArcRectf;
    private Paint mLinePaint;
    private Paint mArcPaint;
    private int mUnmProgressColor;
    private int mProgressColor;
    private int mTickWidth;
    private int mTickDensity;
    private Bitmap  mCenterBitmap;
    private Canvas mCenterCanvas;
    private OnCenterDraw mOnCenter;
    public ArcProgress(Context context) {
        this(context, null);
    }
    public ArcProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public ArcProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray attributes = getContext().obtainStyledAttributes(
                attrs, R.styleable.ArcProgress);
        mBoardWidth = attributes.getDimensionPixelOffset(R.styleable.ArcProgress_borderWidth, DEFAULT_LINEHEIGHT);
        mUnmProgressColor = attributes.getColor(R.styleable.ArcProgress_unprogresColor, DEFAULT_mUnmProgressColor);
        mProgressColor = attributes.getColor(R.styleable.ArcProgress_progressColor, DEFAULT_mProgressColor);
        mTickWidth = attributes.getDimensionPixelOffset(R.styleable.ArcProgress_tickWidth,DEFAULT_mTickWidth);
        mTickDensity = attributes.getInt(R.styleable.ArcProgress_tickDensity,DEFAULT_DENSITY);
        mRadius = attributes.getDimensionPixelOffset(R.styleable.ArcProgress_radius,DEFAULT_mRadius);
        mArcbgColor = attributes.getColor(R.styleable.ArcProgress_arcbgColor,DEFAULT_mUnmProgressColor);
        mTickDensity = Math.max(Math.min(mTickDensity,MAX_DENSITY),MIN_DENSITY);
        mBgShow = attributes.getBoolean(R.styleable.ArcProgress_bgShow,false);
        mDegree = attributes.getInt(R.styleable.ArcProgress_degree,DEFAULT_OFFSETDEGREE);
        mStylePogress = attributes.getInt(R.styleable.ArcProgress_progressStyle,STYLE_TICK);
        boolean capRount = attributes.getBoolean(R.styleable.ArcProgress_arcCapRound,false);
        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setColor(mArcbgColor);
        if(capRount)
            mArcPaint.setStrokeCap(Paint.Cap.ROUND);
        mArcPaint.setStrokeWidth(mBoardWidth);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(mTickWidth);
    }
    public void setOnCenterDraw(OnCenterDraw mOnCenter) {
        this.mOnCenter = mOnCenter;
    }
    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if(widthMode!=MeasureSpec.EXACTLY){
            int widthSize = (int) (mRadius*2+mBoardWidth*2);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize,MeasureSpec.EXACTLY);
        }
        if(heightMode != MeasureSpec.EXACTLY){
            int heightSize = (int) (mRadius*2+mBoardWidth*2);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize,MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        float roate = getProgress() * 1.0f / getMax();
        float x = mArcRectf.right / 2 + mBoardWidth / 2;
        float y = mArcRectf.right / 2 + mBoardWidth / 2;
        if (mOnCenter != null) {
            if(mCenterCanvas == null){
                mCenterBitmap = Bitmap.createBitmap((int)mRadius*2,(int)mRadius*2, Bitmap.Config.ARGB_8888);
                mCenterCanvas = new Canvas(mCenterBitmap);
            }
            mCenterCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            mOnCenter.draw(mCenterCanvas, mArcRectf, x, y,mBoardWidth,getProgress());
            canvas.drawBitmap(mCenterBitmap, 0, 0, null);
        }
        int angle = mDegree/2;
        int count = (360 - mDegree )/mTickDensity;
        int target = (int) (roate * count);
        if(mStylePogress == STYLE_ARC){
            float targetmDegree = (360-mDegree)*roate;
            //绘制完成部分
            mArcPaint.setColor(mProgressColor);
            canvas.drawArc(mArcRectf,90+angle,targetmDegree,false,mArcPaint);
            //绘制未完成部分
            mArcPaint.setColor(mUnmProgressColor);
            canvas.drawArc(mArcRectf,90+angle+targetmDegree,360-mDegree-targetmDegree,false,mArcPaint);
        }else{
            if(mBgShow)
                canvas.drawArc(mArcRectf,90+angle,360-mDegree,false,mArcPaint);
            canvas.rotate(180+angle,x,y);
            for(int i = 0 ; i<count;i++){
                if(i<target){
                    mLinePaint.setColor(mProgressColor);
                }else{
                    mLinePaint.setColor(mUnmProgressColor);
                }
                canvas.drawLine(x,mBoardWidth+mBoardWidth/2,x,mBoardWidth-mBoardWidth/2,mLinePaint);
                canvas.rotate(mTickDensity,x,y);
            }
        }
        canvas.restore();
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mArcRectf = new RectF(mBoardWidth,
                mBoardWidth,
                mRadius*2 - mBoardWidth,
                mRadius*2 - mBoardWidth);
        Log.e("DEMO","right == "+mArcRectf.right+"   mRadius == "+mRadius*2);
    }
    /**
     * dp 2 px
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }
    public interface  OnCenterDraw {
        /**
         *
         * @param canvas
         * @param rectF  圆弧的Rect
         * @param x      圆弧的中心x
         * @param y      圆弧的中心y
         * @param storkeWidth   圆弧的边框宽度
         * @param progress      当前进度
         */
        public  void draw(Canvas canvas, RectF rectF, float x, float y,float storkeWidth,int progress);
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mCenterBitmap!=null){
            mCenterBitmap.recycle();
            mCenterBitmap = null;
        }

    }
}
