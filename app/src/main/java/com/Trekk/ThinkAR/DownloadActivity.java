package com.Trekk.ThinkAR;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.print.PrintHelper;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DownloadActivity extends AppCompatActivity {

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;

    private ActionBarDrawerToggle mDrawerToggle;

    private ImageButton markerOne;
    private ImageButton markerTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

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
                        startActivity(new Intent(DownloadActivity.this, ARActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(DownloadActivity.this, ThinkActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(DownloadActivity.this, AboutActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(DownloadActivity.this, ContactActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(DownloadActivity.this, DownloadActivity.class));
                        break;
                    default:
                        startActivity(new Intent(DownloadActivity.this, MainActivity.class));
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
//        Analytics.LogView(DownloadActivity.class);
//    }

    private void addListenerOnButton() {

        markerOne = (ImageButton)findViewById(R.id.think_postcard_1);
        markerOne.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                sendToPrintHelper(view);
            }
        });

        markerTwo = (ImageButton)findViewById(R.id.think_postcard_2);
        markerTwo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                sendToPrintHelper(view);
            }
        });

        markerTwo = (ImageButton)findViewById(R.id.think_postcard_3);
        markerTwo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                sendToPrintHelper(view);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void sendToPrintHelper(View view) {
        // Create the imageview
        ImageView imgView = (ImageView)view;

        // Get the print manager.
        PrintHelper photoPrinter = new PrintHelper(DownloadActivity.this);

        // Set the desired scale mode.
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);

        // Get the bitmap for the ImageView's drawable.
        Bitmap bitmap = ((BitmapDrawable) imgView.getDrawable()).getBitmap();

        // Print the bitmap.
        photoPrinter.printBitmap("marker", bitmap);
    }
}
