package com.humdinger.humdinger.ControllerView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Vibrator;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;


import java.util.ArrayList;

public class ControllerView extends SurfaceView implements Runnable, View.OnTouchListener {

    private Thread t = null;
    private SurfaceHolder holder;
    private boolean isItOK = false;

    private ArrayList<Button> buttons = new ArrayList<>();
    Paint paint = new Paint();
    private Vibrator vibrator;
    boolean vibrationState = false;
    int vibrationLength = 100, backgroundColor = Color.BLACK;
    private final String LOG_TAG = ControllerView.class.getSimpleName();
    private SparseArray<PointF> mActivePointers = new SparseArray<>();

    public ControllerView(Context context) {
        super(context);
        this.holder = getHolder();
        this.setOnTouchListener(this);
        this.vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3f);

        //Get the size of the drawable field
        Point screenSize = new Point();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getSize(screenSize);
        int x = screenSize.x;
        int y = screenSize.y;

        //Add button A
        buttons.add(new CircleButton(0.85f * x, 0.5f * y, 100f,'A',Color.GREEN,"A"));
        //Add button B
        buttons.add(new CircleButton(0.775f * x, 0.7f * y,100f,'B',Color.RED,"B"));
        //Add button X
        buttons.add(new CircleButton(0.775f * x, 0.3f * y,100f, 'X',Color.BLUE,"X"));
        //Add button Y
        buttons.add(new CircleButton(0.7f * x, 0.5f * y,100f, 'Y',Color.YELLOW,"Y"));

        //Add start button
        buttons.add(new RectButton(0.5f * x, 0.4f * y,100f,200f,'t',Color.CYAN,"Start"));
        //Add select button
        buttons.add(new RectButton(0.4f * x, 0.4f * y,100f, 200f, 'y',Color.CYAN,"Select"));

        //Add the Directional pad
        new DirectionalPad(0.05f*x, 0.25f * y,510,'w', 'd', 's', 'a',Color.MAGENTA, buttons);
        //Add the left button
        buttons.add(new RectButton(0f * x, 0f * y, 450,200,'q',Color.GREEN,"Left"));
        //Add the right button
        buttons.add(new RectButton(0.75f * x, 0f * y,450,200,'e',Color.GREEN,"Right"));
    }


    @Override
    public void run() {
        while (isItOK) {
            //To save resources Let the thread sleep 100 milliseconds
            // because the app isn't animation intensive
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //Perform canvas drawing if there's a valid canvas to draw on
            //if there isn't start the loop again
            if (!holder.getSurface().isValid()) {
                continue;
            }

            //Returns a Canvas to draw on
            Canvas canvas = holder.lockCanvas();
            canvas.drawColor(backgroundColor);
            for (Button currentButton : buttons) {
                currentButton.update();
                currentButton.drawButton(canvas);
                currentButton.drawRectangle(canvas, paint);

            }
            //Unlock it and do the actual drawing
            holder.unlockCanvasAndPost(canvas);
        }

    }

    public void pause() {
        isItOK = false;
        while (true) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            break;
        }
        t = null;
    }

    public void resume() {
        isItOK = true;
        t = new Thread(this);
        t.start();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        // get pointer index from the event object
        int pointerIndex = motionEvent.getActionIndex();

        // get pointer ID
        int pointerId = motionEvent.getPointerId(pointerIndex);

        // get masked (not specific to a pointer) action
        int maskedAction = motionEvent.getActionMasked();

        // Get the active pointer's current position
        float x = motionEvent.getX(pointerIndex);
        float y = motionEvent.getY(pointerIndex);
        Button selectedButton = null;

        for (Button button : buttons) {
            if (button.isItTouched(x, y)) {
                selectedButton = button;
            }
        }

        //ToDO: The code doesn't work properly if you slide off your finger
        //Get the action that was performed from the motionEvent
        //Using getActionMasked to support multiple touches
        switch (maskedAction) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                // We have a new pointer. Lets add it to the list of pointers
                PointF f = new PointF();
                f.x = motionEvent.getX(pointerIndex);
                f.y = motionEvent.getY(pointerIndex);
                mActivePointers.put(pointerId, f);

                if (selectedButton != null) {
                    selectedButton.buttonPressed = true;
                    if(this.vibrationState){
                        vibrator.vibrate(vibrationLength);
                    }
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
//                if(selectedButton != null) {
//                    // a pointer was moved so go through the array to find it by Index to get ID
//                    for (int size = motionEvent.getPointerCount(), i = 0; i < size; i++) {
//                        //By its ID we get the Coordinates from the array
//                        PointF point = mActivePointers.get(motionEvent.getPointerId(i));
//                        if (point != null) {
//                            point.x = motionEvent.getX(i);
//                            point.y = motionEvent.getY(i);
//                        }
//                    }
//                }
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP: {
                if (selectedButton != null) {
                    //If the finger is taken off without being slid off the button
                    selectedButton.buttonPressed = false;
                }
                mActivePointers.remove(pointerId);
                break;
            }
        }
        return true;
    }
}

