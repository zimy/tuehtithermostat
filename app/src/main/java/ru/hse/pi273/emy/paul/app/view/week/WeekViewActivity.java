package ru.hse.pi273.emy.paul.app.view.week;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.google.inject.Inject;

import java.util.Calendar;
import java.util.Date;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import ru.hse.pi273.emy.paul.app.R;
import ru.hse.pi273.emy.paul.app.representation.TaskStringKeeper;
import ru.hse.pi273.emy.paul.app.view.task.CreateTaskActivity;

@ContentView(R.layout.activity_week_view)
public class WeekViewActivity extends RoboActionBarActivity implements ActionBar.OnNavigationListener, ActionMode.Callback {
    @Inject
    TaskStringKeeper stringKeeper;
    FragmentTransaction fTrans;
    ActionMode actionMode;
    int Day = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, stringKeeper.getDays());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bar.setListNavigationCallbacks(adapter, this);
        bar.setHomeButtonEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        getSupportActionBar().setSelectedNavigationItem(calendar.get(Calendar.DAY_OF_WEEK) - 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.week_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        if (id == R.id.action_add) {
            startActivity(new Intent(this, CreateTaskActivity.class).putExtra("Day", Day).putExtra("Action", "New"));
            return true;
        }
        if (id == R.id.action_delete) {
            actionMode = startSupportActionMode(this);
            ((WeekDayFragment) getSupportFragmentManager().findFragmentById(R.id.frCont)).multiChose(true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(int i, long l) {
        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.frCont, WeekDayFragment.newInstance(i));
        fTrans.commit();
        Day = i;
        return false;
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        actionMode.getMenuInflater().inflate(R.menu.week_context, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        ((WeekDayFragment) getSupportFragmentManager().findFragmentById(R.id.frCont)).deleteSelected();
        actionMode.finish();
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        ((WeekDayFragment) getSupportFragmentManager().findFragmentById(R.id.frCont)).multiChose(false);
    }
}
