package uk.org.baverstock.bince;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class BincePong extends View implements Runnable {
    private Paint paint;
    private String TAG = BincePong.class.getCanonicalName();
    private float lastx;
    private float angle;;
    private int rad;
    private DashPathEffect dashes;
    private float[] bat = new float[4];
    private float[] norm = new float[] {0,0,1,0};
    private float[] ball = new float[4];
    private float[] ballv = new float[] {30,30};
    private Paint faint;

    public BincePong(Context context) {
        super(context);
        init();
    }

    public BincePong(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BincePong(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setARGB(255, 192, 192, 32);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.STROKE);

        faint = new Paint();
        faint.setARGB(255, 92, 192, 132);
        faint.setAntiAlias(true);
        faint.setStrokeWidth(2);
        faint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastx = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                angle += event.getX() - lastx;
                lastx = event.getX();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawARGB(125, 0, 0, 188);
        canvas.save();
        int size = 30;
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        rad = Math.min(width, height) * 9 / 20;
        canvas.translate(width/2, height/2);
        dashes = new DashPathEffect(new float[]{10, 20}, 0);
        paint.setPathEffect(dashes);
        canvas.drawCircle(0,0, rad, paint);
        paint.setPathEffect(null);
        bat[0] = (float) (rad * Math.sin((angle - size)/40));
        bat[1] = (float) (rad * Math.cos((angle - size)/40));
        bat[2] = (float) (rad * Math.sin((angle + size)/40));
        bat[3] = (float) (rad * Math.cos((angle + size)/40));
        canvas.drawRect(-20, -20, 20, 20, paint);
        canvas.drawLine(bat[0], bat[1], bat[2], bat[3], paint);
        canvas.drawCircle(ball[0], ball[1], 3, paint);

        update(canvas);

        canvas.restore();

        postDelayed(this, 16);
    }

    private void update(Canvas canvas) {

        Matrix toNorm = new Matrix();
        Matrix fromNorm = new Matrix();

        ball[2] = ball[0] + ballv[0];
        ball[3] = ball[1] + ballv[1];

        toNorm.setPolyToPoly(bat, 0, norm, 0, 2);
        fromNorm.setPolyToPoly(norm, 0, bat, 0, 2);

        toNorm.mapPoints(ball);

        ball[0] = ball[2];
        ball[1] = ball[3];

        if (bounced()) {
            ball[1] = -ball[1];
            toNorm.mapVectors(ballv);
            ballv[1] = -ballv[1];
            fromNorm.mapVectors(ballv);
        }

        fromNorm.mapPoints(ball);

        if (ball[0] * ball[0] + ball[1] * ball[1] > rad * rad) {
            ball[0] = 0;
            ball[1] = 0;
        }
    }

    private boolean bounced() {
        if (Math.signum(ball[1]) != Math.signum(ball[3])) {
            float x0 = ball[0] + (ball[0] - ball[2]) / (ball[1] - ball[3]);
            if (0 <= x0 && x0 <= 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {
        invalidate();
    }
}
