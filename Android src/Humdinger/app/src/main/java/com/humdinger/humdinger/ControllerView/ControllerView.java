package com.humdinger.humdinger.ControllerView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Vibrator;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.humdinger.humdinger.Networker.Key;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import static com.humdinger.humdinger.MenuActivity.selectedProfile;

public class ControllerView extends SurfaceView implements Runnable, View.OnTouchListener {

    public static ArrayBlockingQueue<Key> gamePad;

    private final String LOG_TAG = ControllerView.class.getSimpleName();
    Paint paint = new Paint();
    boolean vibrationState = false;
    int vibrationLength = 100, backgroundColor = Color.BLACK;
    private Thread t = null;
    private SurfaceHolder holder;
    private boolean isItOK = false;
    private ArrayList<Button> buttons = new ArrayList<>();
    private Vibrator vibrator;
    private SparseArray<PointF> mActivePointers = new SparseArray<>();

    public ControllerView(Context context) {
        super(context);
        this.holder = getHolder();
        this.setOnTouchListener(this);
        this.vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3f);

        buttonSetup();

        //Create new SocketClient and start networking thread
        //gamePad = new ArrayBlockingQueue<>(buttons.size());
        //SocketClient sockClient = new SocketClient("192.168.1.142", 21000, gamePad);
        //pass IP and port to client socket
        //new Thread(sockClient).start(); // Start new client socket
    }

    private void buttonSetup() {
        String JsonString = readFromFile(selectedProfile);
        try {
            JSONArray buttonsInJSON = new JSONArray(JsonString);

            for (int i = 0; i < buttonsInJSON.length(); i++) {
                JSONObject currentButton = buttonsInJSON.getJSONObject(i);
                createButton(currentButton);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void createButton(JSONObject buttonInfo) {
        try {
            String buttonType = (String) buttonInfo.get("name");
            //Every button has coordinates
            JSONArray coordinates = buttonInfo.getJSONArray("coordinates");

            if (buttonType.equals("CircleButton")) {
                buttons.add(new CircleButton((float) coordinates.getDouble(0), (float) coordinates.getDouble(1), (float) buttonInfo.getDouble("radius"),
                        (char) buttonInfo.getInt("message"), buttonInfo.getInt("color"), buttonInfo.getString("text")));
            } else if (buttonType.equals("RectButton")) {
                buttons.add(new RectButton((float) coordinates.getDouble(0), (float) coordinates.getDouble(1), (float) buttonInfo.getDouble("width"),
                        (float) buttonInfo.getDouble("height"), (char) buttonInfo.getInt("message"), buttonInfo.getInt("color"), buttonInfo.getString("text")));
            } else if (buttonType.equals("DirectionalPad")) {
                new DirectionalPad((float) coordinates.getDouble(0), (float) coordinates.getDouble(1), (float) buttonInfo.getDouble("width"), (char) buttonInfo.getInt("messageUp"),
                        (char) buttonInfo.getInt("messageRight"), (char) buttonInfo.getInt("messageDown"), (char) buttonInfo.getInt("messageLeft"), buttonInfo.getInt("color"), buttons);
            } else Log.e(LOG_TAG, "Didn't find a button called " + buttonType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                    if (this.vibrationState) {
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

    private String readFromFile(String fileName) {
        String content = "";

        try {
            InputStream inputStream = getContext().openFileInput(fileName);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                content = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(LOG_TAG, "Can not read file: " + e.toString());
        }

        return content;
    }
}

