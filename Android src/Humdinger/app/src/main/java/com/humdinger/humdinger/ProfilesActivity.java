package com.humdinger.humdinger;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Scanner;

public class ProfilesActivity extends AppCompatActivity {
    private static final String LOG_TAG = ProfilesActivity.class.getSimpleName();
    ArrayAdapter profilesAdapter;
    final ArrayList<String> profiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfilesActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        profiles.add("SNES");
        profiles.add("Test1");
        profiles.add("Test2");
        profiles.add("Test3");
        profiles.add("Test4");
        profiles.add("Test5");
        profiles.add("Test6");
        profiles.add("Test7");
        profiles.add("Test8");
        profiles.add("Test9");

        profilesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, profiles);
        ListView rootView = (ListView) findViewById(R.id.list);
        rootView.setAdapter(profilesAdapter);

        Scanner scanner = new Scanner(getResources().openRawResource(R.raw.snes));

        while (scanner.hasNext()) {
            String word = scanner.next();
            Log.v(LOG_TAG, word);
        }
        scanner.close();

    }
}
