package com.Trekk.ThinkAR;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ThinkActivity extends AppCompatActivity {
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private View vendorChart;
    private MyView myView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_think);

        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        getSupportActionBar().setTitle("");

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        buildTableRowsFromJson();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_rules);
        item.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_rules) {
            startActivity(new Intent(ThinkActivity.this, RulesActivity.class));
            return true;
        }

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
                        startActivity(new Intent(ThinkActivity.this, ARActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(ThinkActivity.this, ThinkActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(ThinkActivity.this, AboutActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(ThinkActivity.this, ContactActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(ThinkActivity.this, DownloadActivity.class));
                        break;
                    default:
                        startActivity(new Intent(ThinkActivity.this, MainActivity.class));
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private int convertDegreesForPieChart(int numberCompleted, int total) {
        float num = (float)numberCompleted / (float)total;
        float degrees = num * 360;

        return Math.round(degrees);
    }

    private void buildTableRowsFromJson() {
        try {
            int visited = 0;

            JsonHelper json = new JsonHelper();
            JSONObject obj = new JSONObject(json.loadDataFromAsset(ThinkActivity.this, "test.json"));

            TableLayout tl = (TableLayout) findViewById(R.id.vendor_table);
            JSONArray mArry = obj.getJSONArray("vendors");

            for (int i = 0; i < mArry.length(); i++) {
                JSONObject vendors = mArry.getJSONObject(i);

                String name = vendors.getString("name");
                String booth = vendors.getString("booth");

                TableRow tr = new TableRow(this);
                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                SharedPreferences sharedPref = getSharedPreferences("vendor_id", MODE_PRIVATE);

                if (sharedPref.contains(name)) {
                    ImageView checkIcon = new ImageView(this);
                    checkIcon.setImageResource(R.drawable.check_mark);
                    checkIcon.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    checkIcon.setPadding(10, 15, 10, 15);
                    tr.addView(checkIcon);

                    visited = visited + 1;
                }
                else {
                    TextView vendorsString = new TextView(this);
                    vendorsString.setText("");
                    vendorsString.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    tr.addView(vendorsString);
                }

                TextView vendorsString = new TextView(this);
                vendorsString.setText(name);
                vendorsString.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                if (Build.VERSION.SDK_INT < 23) {
                    vendorsString.setTextAppearance(getApplicationContext(), R.style.TextViewCustomFont_table);
                } else{
                    vendorsString.setTextAppearance(R.style.TextViewCustomFont_table);
                }
                vendorsString.setPadding(10, 15, 10, 15);
                tr.addView(vendorsString);

                TextView boothString = new TextView(this);
                boothString.setText(booth);
                boothString.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                if (Build.VERSION.SDK_INT < 23) {
                    boothString.setTextAppearance(getApplicationContext(), R.style.TextViewCustomFont_table);
                } else{
                    boothString.setTextAppearance(R.style.TextViewCustomFont_table);
                }
                boothString.setPadding(10, 15, 10, 15);
                boothString.setGravity(Gravity.CENTER_HORIZONTAL);
                tr.addView(boothString);

                tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            }

            setVisited(visited, mArry.length());

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setVisited(int numberCompleted, int totalVendors) {
        myView = (MyView)findViewById(R.id.vendor_chart);
        myView.setCircleAngle(convertDegreesForPieChart(numberCompleted, totalVendors));
        myView.setCircleText(numberCompleted + " / " + totalVendors);
    }
}
