package com.humdinger.humdinger;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.humdinger.humdinger.MenuActivity.selectedProfile;

public class ProfilesActivity extends AppCompatActivity {
    private static final String LOG_TAG = ProfilesActivity.class.getSimpleName();
    ArrayAdapter profilesAdapter;
    private String newProfileName;
    List<String> profiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);

        final TextView profileTextView = (TextView) findViewById(R.id.profiles_textView_currentProfile);
        profileTextView.setText(selectedProfile);
        final ListView listView = (ListView) findViewById(R.id.list);

        //Select profile
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView clickedItem = (TextView) view;
                String clickedProfileName = (String) clickedItem.getText();
                profileTextView.setText(clickedProfileName);
                MenuActivity.selectedProfile = clickedProfileName;
            }
        });

        //Add profile
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpDialog();
            }
        });

        //Edit or delete profile contextMenu
        registerForContextMenu(listView);

        //ToDo: The file "instant-run" needs to be removed from the list - created by the IDE
        String[] filesInStorage = fileList();
        profiles = new ArrayList<String>(Arrays.asList(filesInStorage));


        profilesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, profiles);
        listView.setAdapter(profilesAdapter);
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
                Toast.makeText(ProfilesActivity.this,"You clicked on edit ",Toast.LENGTH_SHORT).show();
                return true;
            //ToDo: What if I delete current profile?
            case R.id.title_delete:
                //If the user wants to delete a profile other than the one currently selected
                if(!MenuActivity.selectedProfile.equals(profiles.get(info.position))){
                    Toast.makeText(ProfilesActivity.this,"Removed: "+profiles.get(info.position),Toast.LENGTH_SHORT).show();
                    profiles.remove(info.position);
                    profilesAdapter.notifyDataSetChanged();
                    return true;
                }
                //If the user wants to delete a profile that is currently selected as the main profile
                else{
                    Toast.makeText(ProfilesActivity.this,"Can't remove profile "+profiles.get(info.position)+" because it's currently in use!",Toast.LENGTH_LONG).show();
                    return false;
                }
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void popUpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter custom profile name");
        // Set up the input
        final EditText input = new EditText(this);
        input.setHint("Profile name");
        // Specify the type of input expected - a string with numbers;
        input.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(input);
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newProfileName = input.getText().toString();
                Log.v(LOG_TAG,newProfileName);
                //Check if the user actually entered something
                if (!newProfileName.isEmpty()) {
                    profiles.add(newProfileName);
                    profilesAdapter.notifyDataSetChanged();
                    writeToStroage(newProfileName);
                    //Do something
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

    private void writeToStroage(String fileName){
        FileOutputStream outputStream;
        String dummyData = "[]";
        try {
            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(dummyData.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
