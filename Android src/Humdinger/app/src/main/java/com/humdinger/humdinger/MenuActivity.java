package com.humdinger.humdinger;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

public class MenuActivity extends AppCompatActivity {
    private final String LOG_TAG = MenuActivity.class.getSimpleName();
    String ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void toSettingsActivity(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void toProfilesActivity(View view) {
        startActivity(new Intent(this, ProfilesActivity.class));
    }

    public void toGameActivity(View view) {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.setPackage("com.google.zxing.client.android");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        // Verify that the intent will resolve to an activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 0);
        }
        //If not then ask the user to input the ip manually
        else {
            popUpDialog();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If everything goes OK with reading the QR data
        if (resultCode == RESULT_OK) {
            ipAddress = data.getStringExtra("SCAN_RESULT"); // This will contain scan result
            startActivity(new Intent(this, ControllerActivity.class));
        }
    }

    private void popUpDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Input server ip");
        // Set up the input
        final EditText input = new EditText(this);
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
                    startActivity(new Intent(getApplicationContext(), ControllerActivity.class));
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
}
