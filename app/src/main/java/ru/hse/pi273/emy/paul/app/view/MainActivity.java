package ru.hse.pi273.emy.paul.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import ru.hse.pi273.emy.paul.app.R;
import ru.hse.pi273.emy.paul.app.view.week.WeekViewActivity;

@ContentView(R.layout.activity_main)
public class MainActivity extends RoboActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();
        bar.setHomeButtonEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_week) {
            startActivity(new Intent(this, WeekViewActivity.class));
            return true;
        }
        if (id == android.R.id.home) {
            //here will switch to main tab
            Log.d("STUB", "Stub");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
