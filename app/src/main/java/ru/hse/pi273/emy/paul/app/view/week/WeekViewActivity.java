package ru.hse.pi273.emy.paul.app.view.week;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.inject.Inject;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.InjectView;
import ru.hse.pi273.emy.paul.app.R;
import ru.hse.pi273.emy.paul.app.engine.Engine;
import ru.hse.pi273.emy.paul.app.engine.PersistentEngine;
import ru.hse.pi273.emy.paul.app.view.task.CreateTaskActivity;

public class WeekViewActivity extends RoboActionBarActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    @Inject
    Engine engine = new PersistentEngine();
    @InjectView(R.id.listView)
    ListView listView;
    @InjectView(R.id.weekViewButton)
    Button button;
    WeekListAdapter week;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);
        week = new WeekListAdapter(this, engine.getTasks());
        listView.setAdapter(week);
        week.notifyDataSetChanged();
        listView.setOnItemClickListener(this);
        button.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        week.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.week_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(this, CreateTaskActivity.class));
    }
}
