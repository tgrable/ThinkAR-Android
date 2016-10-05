package com.Trekk.ThinkAR;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import com.unity3d.player.UnityPlayer;

import java.util.Map;

public class ARActivity extends AppCompatActivity {

    private static final String TAG = "TimGrable";
    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;

    private ActionBarDrawerToggle mDrawerToggle;

    //Declare a FrameLayout object
    FrameLayout fl_forUnity;
    private FrameLayout winnerFrame;

    private TextView mWinnerClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

        winnerFrame = (FrameLayout)findViewById(R.id.instant_winner_frame);

        mUnityPlayer = new UnityPlayerWrapper(this);

        //Inflate the frame layout from XML
        this.fl_forUnity = (FrameLayout)findViewById(R.id.fl_forUnity);

        //Add the mUnityPlayer view to the FrameLayout, and set it to fill all the area available
        this.fl_forUnity.addView(mUnityPlayer.getView(),
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        mUnityPlayer.requestFocus();

        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        getSupportActionBar().setTitle("");

        addDrawerItems();
        setupDrawer();

        addListenerOnButton();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
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
//        mUnityPlayer.configurationChanged(newConfig);
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
                    startActivity(new Intent(ARActivity.this, ARActivity.class));
                    mUnityPlayer.quit();
                    break;
                case 1:
                    startActivity(new Intent(ARActivity.this, ThinkActivity.class));
                    mUnityPlayer.quit();
                    break;
                case 2:
                    startActivity(new Intent(ARActivity.this, AboutActivity.class));
                    mUnityPlayer.quit();
                    break;
                case 3:
                    startActivity(new Intent(ARActivity.this, ContactActivity.class));
                    mUnityPlayer.quit();
                    break;
                case 4:
                    startActivity(new Intent(ARActivity.this, DownloadActivity.class));
                    mUnityPlayer.quit();
                    break;
                default:
                    startActivity(new Intent(ARActivity.this, MainActivity.class));
                    mUnityPlayer.quit();
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
        mWinnerClose = (TextView)findViewById(R.id.winner_close);
        mWinnerClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setVisibility needs to be run on the main thread
                ARActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {winnerFrame.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    // Quit Unity
    @Override protected void onDestroy ()
    {
        super.onDestroy();
        mUnityPlayer.quit();

    }

    // Pause Unity
    @Override protected void onPause()
    {
        super.onPause();
        mUnityPlayer.pause();
    }

    // Resume Unity
    @Override protected void onResume()
    {
        super.onResume();
        mUnityPlayer.resume();
    }

    // Notify Unity of the focus change.
    @Override public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    @Override public boolean onKeyUp(int keyCode, KeyEvent event)     { return mUnityPlayer.injectEvent(event); }
    @Override public boolean onKeyDown(int keyCode, KeyEvent event)   { return mUnityPlayer.injectEvent(event); }
    @Override public boolean onTouchEvent(MotionEvent event)          { return mUnityPlayer.injectEvent(event); }
    /*API12*/ public boolean onGenericMotionEvent(MotionEvent event)  { return mUnityPlayer.injectEvent(event); }


    /**
     *
     * These methods get called from the Unity player
     * each time an experience is loaded from the marker.
     *
     **/
    public void didReturnName(String markerName) {

        // Check SharedPreferences for markerName
        // If it does not exist add it to SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences(markerName, MODE_PRIVATE);
        if (!sharedPref.contains(markerName)) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(markerName, markerName);
            editor.commit();
        }
    }

    public void didReturnWinner(String winner) {

        // If winner equals true display the winner overlay message
        if (winner.equals("true")) {

            // setVisibility needs to be run on the main thread
            ARActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    winnerFrame.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
