package com.humdinger.humdinger;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

public class ControllerView extends SurfaceView implements Runnable, View.OnTouchListener{

    private Thread t = null;
    private SurfaceHolder holder;
    private boolean isItOK = false;
    private ArrayList<Button> buttons = new ArrayList<Button>();
    private Canvas canvas;
    Paint paint = new Paint();

    public ControllerView(Context context){
        super(context);
        this.holder = getHolder();
        this.setOnTouchListener(this);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3f);
        //Get the size of the drawable field
        Point screenSize = new Point();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getSize(screenSize);
        int x = screenSize.x;
        int y = screenSize.y;
        //

        //Add button A
        buttons.add(new Button(0.85f*x,0.45f*y, BitmapFactory.decodeResource(getResources(), R.drawable.button_a),BitmapFactory.decodeResource(getResources(),R.drawable.button_a_pressed)));
        //Add button B
        buttons.add(new Button(0.775f*x,0.65f*y,BitmapFactory.decodeResource(getResources(),R.drawable.button_b),BitmapFactory.decodeResource(getResources(),R.drawable.button_b_pressed)));
        //Add button X
        buttons.add(new Button(0.775f*x,0.25f*y,BitmapFactory.decodeResource(getResources(),R.drawable.button_x),BitmapFactory.decodeResource(getResources(),R.drawable.button_x_pressed)));
        //Add button Y
        buttons.add(new Button(0.7f*x,0.45f*y,BitmapFactory.decodeResource(getResources(),R.drawable.button_y),BitmapFactory.decodeResource(getResources(),R.drawable.button_y_pressed)));

        //Add start button
        buttons.add(new Button(0.5f*x,0.4f*y,BitmapFactory.decodeResource(getResources(),R.drawable.button_start),BitmapFactory.decodeResource(getResources(),R.drawable.button_start_pressed)));
        //Add select button
        buttons.add(new Button(0.4f*x,0.4f*y,BitmapFactory.decodeResource(getResources(),R.drawable.button_select),BitmapFactory.decodeResource(getResources(),R.drawable.button_select_pressed)));

        //Add the Directional pad
        buttons.add(new Button(0f*x,0.35f*y,BitmapFactory.decodeResource(getResources(),R.drawable.button_dpad),BitmapFactory.decodeResource(getResources(),R.drawable.button_dpad_pressed)));
        //Add the left button
        buttons.add(new Button(0f*x,0f*y,BitmapFactory.decodeResource(getResources(),R.drawable.button_lb),BitmapFactory.decodeResource(getResources(),R.drawable.button_lb_pressed)));
        //Add the right button
        buttons.add(new Button(0.75f*x,0f*y,BitmapFactory.decodeResource(getResources(),R.drawable.button_rb),BitmapFactory.decodeResource(getResources(),R.drawable.button_rb_pressed)));
    }


    @Override
    public void run() {
        while(isItOK){

            //To save resources Let the thread sleep 80 milliseconds
            // because the app isn't animation intensive
            try {
                Thread.sleep(80);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //Perform canvas drawing if there's a valid canvas to draw on
            //if there isn't start the loop again
            if(!holder.getSurface().isValid()){
                continue;
            }

            //Returns a Canvas to draw on
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.BLACK);
            for(Button currentButton:buttons){
                canvas.drawBitmap(currentButton.currentlyDisplayed,currentButton.x,currentButton.y,null);
                canvas.drawRect(currentButton.buttonArea,paint);
            }
            //Unlock it and do the actual drawing
            holder.unlockCanvasAndPost(canvas);
        }

    }

    public void pause(){
        isItOK = false;
        while(true){
            try {
                t.join();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            break;
        }
        t = null;
    }

    public void resume(){
        isItOK = true;
        t = new Thread(this);
        t.start();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int pointerIndex = motionEvent.getActionIndex();
        int pointerID = motionEvent.getPointerId(pointerIndex);
        // Get the active pointer's current position
        float x = motionEvent.getX(pointerIndex);
        float y = motionEvent.getY(pointerIndex);
        Button selectedButton = null;

        for(Button button: buttons){
            if(button.isItTouched(x,y)){
                selectedButton = button;
            }
        }


        //get the action that was performed from the motionEvent
        //Using getActionMasked instead of getAction to support multiple touches
        switch (motionEvent.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:{
                if(selectedButton != null){
                    selectedButton.currentlyDisplayed = selectedButton.pressedBitmap;
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:{
                selectedButton.currentlyDisplayed = selectedButton.normalBitmap;
                break;
            }

            case MotionEvent.ACTION_MOVE:{
                break;
            }
        }
        return true;
    }
}

