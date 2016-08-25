package com.humdinger.humdinger;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

        ListView listView = (ListView) findViewById(R.id.list);

        //Select profile
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ProfilesActivity.this, "This selects the new profile nr "+i,Toast.LENGTH_SHORT).show();
            }
        });

        //Add profile
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProfilesActivity.this, "This adds a new profile", Toast.LENGTH_SHORT).show();
            }
        });

        //Edit or delete profile
        registerForContextMenu(listView);

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
        listView.setAdapter(profilesAdapter);

//        Scanner scanner = new Scanner(getResources().openRawResource(R.raw.snes));
//
//        while (scanner.hasNext()) {
//            String word = scanner.next();
//            Log.v(LOG_TAG, word);
//        }
//        scanner.close();

    }

    @Override
    //Called when a Long-Click happens on a listView item
    //then creates a context Menu using a custom menu layout
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profiles, menu);
    }

    //Defines the behaviour of the click depending on what
    //menu item the user clicked on
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.title_edit:
                Toast.makeText(ProfilesActivity.this,"You click on edit ",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.title_delete:
                Toast.makeText(ProfilesActivity.this,"Removed: "+profiles.get(info.position),Toast.LENGTH_SHORT).show();
                profiles.remove(info.position);
                profilesAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void send(String option){
        Log.e(LOG_TAG,"Vajutasid "+option);
    }
}
