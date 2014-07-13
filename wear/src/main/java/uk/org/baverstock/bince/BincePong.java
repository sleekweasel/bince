package uk.org.baverstock.bince;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class BincePong extends View {
    private Path path;
    private Paint paint;
    private Paint yellow;
    private String TAG = BincePong.class.getCanonicalName();

    public BincePong(Context context) {
        super(context);
    }

    public BincePong(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BincePong(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.d(TAG, "Me=" + event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path = new Path();
                path.moveTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(event.getX(), event.getY());
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
        }
        if (paint == null) {
            paint = new Paint();
            paint.setARGB(255, 192, 192, 32);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(4);
            paint.setStyle(Paint.Style.STROKE);
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawARGB(25, 0, 0, 88);
        if (yellow == null) {
            yellow = new Paint();
            yellow.setARGB(255, 243, 200, 89);
        }
        if (path != null) {
            canvas.drawPath(path, paint);
        }
        canvas.drawRect(-40, -40, 40, 40, yellow);
    }
}
