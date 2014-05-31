package ru.hse.pi273.emy.paul.app.view.week;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.inject.Inject;

import java.util.Calendar;
import java.util.Date;

import roboguice.activity.RoboActionBarActivity;
import ru.hse.pi273.emy.paul.app.R;
import ru.hse.pi273.emy.paul.app.representation.TaskStringKeeper;
import ru.hse.pi273.emy.paul.app.view.task.CreateTaskActivity;

public class WeekViewActivity extends RoboActionBarActivity implements AdapterView.OnItemClickListener, ActionBar.OnNavigationListener {
    @Inject
    TaskStringKeeper stringKeeper;
    FragmentTransaction fTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, stringKeeper.getDays());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bar.setListNavigationCallbacks(adapter, this);

    }

    @Override
    protected void onResume() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        getSupportActionBar().setSelectedNavigationItem(calendar.get(Calendar.DAY_OF_WEEK) - 1);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.week_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            startActivity(new Intent(this, CreateTaskActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public boolean onNavigationItemSelected(int i, long l) {
        Log.d("WeekViewActivity", "" + i + " " + l);
        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.frCont, WeekDayFragment.newInstance(i));
        fTrans.commit();
        return false;
    }
}
