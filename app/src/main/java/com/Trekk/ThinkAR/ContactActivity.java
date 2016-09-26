package com.Trekk.ThinkAR;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactActivity extends AppCompatActivity {

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;

    private ActionBarDrawerToggle mDrawerToggle;
    private TextView phoneLink;
    private TextView emailLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        getSupportActionBar().setTitle("");

        addDrawerItems();
        setupDrawer();
        addListenerOnButton();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

//        logActivityWithAnswers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void addDrawerItems() {
        Resources res = getResources();
        String[] osArray = res.getStringArray(R.array.nav_array);
        mAdapter = new ArrayAdapter<String>(this, R.layout.custom_listview, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    startActivity(new Intent(ContactActivity.this, ARActivity.class));
                    break;
                case 1:
                    startActivity(new Intent(ContactActivity.this, ThinkActivity.class));
                    break;
                case 2:
                    startActivity(new Intent(ContactActivity.this, AboutActivity.class));
                    break;
                case 3:
                    startActivity(new Intent(ContactActivity.this, ContactActivity.class));
                    break;
                case 4:
                    startActivity(new Intent(ContactActivity.this, DownloadActivity.class));
                    break;
                default:
                    startActivity(new Intent(ContactActivity.this, MainActivity.class));
                    break;
            }
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void addListenerOnButton() {
        phoneLink = (TextView)findViewById(R.id.contact_phone);
        phoneLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneLink.getText().toString().replace("-","")));
                startActivity(intent);
            }
        });

        emailLink = (TextView)findViewById(R.id.contact_email);
        emailLink.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent i = new Intent(Intent.ACTION_SEND);
                 i.setType("message/rfc822");
                 i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"tgrable@trekk.com"});
                 i.putExtra(Intent.EXTRA_SUBJECT, "thINK AR App");

                 startActivity(Intent.createChooser(i, "Send mail..."));
             }
        });
    }


//    private void logActivityWithAnswers() {
//        if (!Fabric.isInitialized()) {
//            // Set up Crashlytics, disabled for debug builds
//            Crashlytics crashlyticsKit = new Crashlytics.Builder()
//                    .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
//                    .build();
//
//            Fabric.with(this, crashlyticsKit);
//        }
//
//        Analytics.LogView(ContactActivity.class);
//    }
}
