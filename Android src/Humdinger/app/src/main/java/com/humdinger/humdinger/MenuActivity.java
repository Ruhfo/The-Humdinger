package com.humdinger.humdinger;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MenuActivity extends AppCompatActivity {
    private final String LOG_TAG = MenuActivity.class.getSimpleName();
    private String ipAddress;
    public static String selectedProfile = "SNES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //ToDO: Profile variable needs to be in persistent storage
        final TextView profileTextView = (TextView) findViewById(R.id.menu_textView_currentProfile);
        profileTextView.setText(selectedProfile);

        //SnesSetup(readSetupFile());

//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        if (!prefs.getBoolean("firstTime", false)) {
//            // <---- run your one time code here
//
//            // mark first time has runned.
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putBoolean("firstTime", true);
//            editor.apply();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView profileTextView = (TextView) findViewById(R.id.menu_textView_currentProfile);
        profileTextView.setText(selectedProfile);
    }

    public void toSettingsActivity(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void toProfilesActivity(View view) {
        startActivity(new Intent(this, ProfilesActivity.class));
    }

    public void toGameActivity(View view) {

        //Be sure to delete it if you use other methods to
        //Access the ControllerActivity
        startActivity(new Intent(this, ControllerActivity.class));

/*        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.setPackage("com.google.zxing.client.android");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        // Verify that the intent will resolve to an activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 0);
        }
        //If not then ask the user to input the ip manually
        else {
            popUpDialog();
        }*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If everything goes OK with reading the QR data
        if (resultCode == RESULT_OK) {
            ipAddress = data.getStringExtra("SCAN_RESULT"); // This will contain the scan result

            Intent intent = new Intent(this, ControllerActivity.class);
            intent.putExtra("key", ipAddress);
            startActivity(intent);
        }
    }

    private void popUpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Input server ip");
        // Set up the input
        final EditText input = new EditText(this);
        input.setHint("E.g. 192.168.1.254");
        // Specify the type of input expected - a string with numbers;
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ipAddress = input.getText().toString();
                //Check if the user actually entered something
                if (!ipAddress.isEmpty()) {
                    Intent intent = new Intent(MenuActivity.this, ControllerActivity.class);
                    intent.putExtra("key", ipAddress);
                    startActivity(intent);
                } else dialog.cancel();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public String readSetupFile() {
        String textLine;
        StringBuilder fileContent = new StringBuilder();
        InputStream inputStream = getResources().openRawResource(R.raw.setup);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            while ((textLine = bufferedReader.readLine()) != null) {
                fileContent.append(textLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileContent.toString();
    }

    public void SnesSetup(String JsonString) {
        //Get the size of the windows screen in pixels
        Point screenSize = new Point();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getSize(screenSize);
        int screenWidth = screenSize.x;
        int screenHeight = screenSize.y;

        //Read the initial x;y multipliers from JSON file and convert them into actual
        //x,y coordinates  using the current devices display size
        try {
            JSONArray buttons = new JSONArray(JsonString);
            for (int i = 0; i < buttons.length(); i++) {
                JSONObject currentButton = buttons.getJSONObject(i);
                JSONArray coordinates = currentButton.getJSONArray("coordinates");
                double x = (double) coordinates.get(0);
                double y = (double) coordinates.get(1);

                x *= screenWidth;
                y *= screenHeight;

                JSONArray newCoordinates = new JSONArray();
                newCoordinates.put(x);
                newCoordinates.put(y);

                currentButton.put("coordinates", newCoordinates);
            }
            writeToStorage(selectedProfile, buttons.toString());
        } catch (JSONException | IOException e) {
            //ToDo: Error handling
            e.printStackTrace();
        }
    }

    //Writes the new SNES.txt profile file into Android internal storage
    public void writeToStorage(String fileName, String content) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert fos != null;
            fos.close();
        }

    }
}
