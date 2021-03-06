package com.zqc.fold;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * @author zhy http://blog.csdn.net/lmj623565791/
 */
public class TouchFoldLayout extends FoldLayout {
    public static final int LEN = 40;
    public static final int BETWEEN = 5;
    private float[] factors = new float[LEN];
    private Handler mHandler;
    private int count = 0;
    public FoldFinishListener listener;

    public void setListener(FoldFinishListener listener) {
        this.listener = listener;
    }

    public TouchFoldLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        if (msg.arg1 < factors.length) {
                            setFactor(1 - factors[msg.arg1]);
                            Message message = mHandler.obtainMessage(1, count, 1);
                            mHandler.sendMessageDelayed(message, (long) (BETWEEN * (1- factors[msg.arg1])));
                            count++;
                        } else {
                            setVisibility(INVISIBLE);
                            if (listener != null) {
                                listener.onEnd();
                            }
                        }
                        break;
                }
            }
        };
        for (int i = 0; i < factors.length; i++) {
            factors[i] = ((float) i) / factors.length;
        }
        Log.e("factor", factors[10]+"");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.onStart();
                }
                Message msg = mHandler.obtainMessage(1, count, 1);
                mHandler.sendMessage(msg);
            }
        }, 100);
        return super.onTouchEvent(event);
    }

    public interface FoldFinishListener {
        public void onStart();
        public void onEnd();
    }
}
