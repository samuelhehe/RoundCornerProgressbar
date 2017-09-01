package cn.samuelnotes.roundcornerprogressbar.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import cn.samuelnotes.roundcornerprogressbar.R;


/**
 * author：panda on 17/7/24 13:00
 */
public class ProgressView extends View {

    private Context mContext;
    private int defaultWH = 500;
    private int mViewWidth;
    private int mViewHeight;

    private int mTextColor;
    private int mBgColor;
    private int mProColor;
    private float mTextrSize;
    private TextPaint mTextPaint;
    private Paint bgPaint;
    private Paint proPaint;

    private Path mPath;
    private int duration = 1000;

    /**
     * 进度值
     **/
    private int progress = 0;
    private int progressAni = 0;

    private float mRadius = 0;
    private int paddingtop;
    private int paddingleft;
    private int paddingbottom;
    private int paddingright;
    private boolean showtxt;


    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttrs(attrs);
        initPaint();
    }

    private void initPaint() {
        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(mBgColor);

        float corner = dp2px(mRadius);
        /// 圆角半径太大
        if (mViewHeight <=corner * 2) {
            corner = (mViewHeight - paddingbottom - paddingtop) / 2;
        }

        CornerPathEffect corEffect = new CornerPathEffect(corner);
        bgPaint.setPathEffect(corEffect);

        proPaint = new TextPaint();
        proPaint.setAntiAlias(true);
        proPaint.setColor(mProColor);
        proPaint.setPathEffect(corEffect);

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextrSize);
        mTextPaint.setColor(mTextColor);
        mPath = new Path();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.ProgressView);
        mTextColor = typedArray.getColor(R.styleable.ProgressView_pro_text_color, Color.WHITE);
        mTextrSize = typedArray.getDimension(R.styleable.ProgressView_pro_text_size, dp2px(14));
        mBgColor = typedArray.getColor(R.styleable.ProgressView_pro_bg_color, Color.BLACK);
        mProColor = typedArray.getColor(R.styleable.ProgressView_pro_pro_color, Color.GRAY);
        progress = typedArray.getInteger(R.styleable.ProgressView_pro_progress, 0);
        mRadius = typedArray.getDimensionPixelSize(R.styleable.ProgressView_pro_radius, 0);
        showtxt = typedArray.getBoolean(R.styleable.ProgressView_pro_showtxt, true);
        progressAni = progress;
        typedArray.recycle();
    }


    /**
     * 渲染的数据
     *
     * @param progress
     */
    public void setProgress(int progress) {
        setProgress(progress, true);
    }

    public void setProgress(int progress, boolean anime) {
        this.progress = progress;
        invalidate();
        if (anime) {
            animeValue(duration);
        } else {
            progressAni = progress;
        }
    }

    public void setShowtxt(boolean showtxt) {
        this.showtxt = showtxt;
        setProgress(progress, false);
    }


    private ValueAnimator anime;

    private void animeValue(int duration) {
        anime = ValueAnimator.ofFloat(0, 1f);
        if (!anime.isStarted()) {
            anime.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float percent = (float) animation.getAnimatedValue();
                    progressAni = (int) (progress * percent);
                    if (progressAni < progress) {
                        invalidate();
                    } else {
                        anime.cancel();
                    }
                }
            });
            anime.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressAni = progress;
                    invalidate();
                }
            });
            anime.setDuration(duration).start();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
//// draw bg
        mPath.moveTo(0, 0);
        mPath.lineTo(mViewWidth, 0);
        mPath.lineTo(mViewWidth, mViewHeight);
        mPath.lineTo(0, mViewHeight);
        mPath.close();
        canvas.drawPath(mPath, bgPaint);

///  draw progress
        int right = (mViewWidth - paddingright - paddingleft) * progressAni / 100;

        mPath.reset();
        mPath.moveTo(0 + paddingleft, 0 + paddingtop);
        mPath.lineTo(right + paddingleft, 0 + paddingtop);
        mPath.lineTo(right + paddingleft, mViewHeight - paddingbottom);
        mPath.lineTo(0 + paddingleft, mViewHeight - paddingbottom);
        mPath.close();
        canvas.drawPath(mPath, proPaint);

        if (showtxt) {
            String aniText = progressAni + "%";
            float textWidth = mTextPaint.measureText(aniText);
            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            float textHeight = fontMetrics.descent - fontMetrics.ascent;
            int widthRel = (mViewWidth - paddingleft - paddingright) * progress / 100;

            if ((mViewHeight - paddingtop - paddingbottom) >= textHeight) {
                float texty = (mViewHeight - paddingtop - paddingbottom) / 2 + textHeight / 2;

                if (widthRel < textWidth * 2 + dp2px(mRadius)) {
                    canvas.drawText(aniText, right + dp2px(mRadius), texty, mTextPaint);
                } else {
                    canvas.drawText(aniText, right - dp2px(mRadius) - textWidth, texty, mTextPaint);
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = measureWidth(widthMeasureSpec);
        mViewHeight = measureHeight(heightMeasureSpec);
        paddingtop = getPaddingTop();
        paddingleft = getPaddingLeft();
        paddingbottom = getPaddingBottom();
        paddingright = getPaddingRight();

        initPaint();

        setMeasuredDimension(mViewWidth, mViewHeight);

    }


    private int measureWidth(int measureSpec) {
        int preferred = defaultWH;
        return getMeasurement(measureSpec, preferred);
    }

    private int measureHeight(int measureSpec) {
        int preferred = mViewWidth;
        return getMeasurement(measureSpec, preferred);
    }

    private int getMeasurement(int measureSpec, int preferred) {
        int specSize = View.MeasureSpec.getSize(measureSpec);
        int measurement;

        switch (View.MeasureSpec.getMode(measureSpec)) {
            case View.MeasureSpec.EXACTLY://相当于我们设置为match_parent或者为一个具体的值
                measurement = specSize;
                break;
            case View.MeasureSpec.AT_MOST://相当于我们设置为wrap_content
                measurement = Math.min(preferred, specSize);
                break;
            default:
                measurement = preferred;
                break;
        }
        return measurement;
    }


    private float dp2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return dpValue * scale + 0.5f;
    }


}
