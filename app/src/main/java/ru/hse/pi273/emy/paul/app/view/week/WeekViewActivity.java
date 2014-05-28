package ru.hse.pi273.emy.paul.app.view.week;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import ru.hse.pi273.emy.paul.app.R;
import ru.hse.pi273.emy.paul.app.engine.PersistentEngine;
import ru.hse.pi273.emy.paul.app.representation.Task;
import ru.hse.pi273.emy.paul.app.view.task.CreateTaskActivity;

public class WeekViewActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    List<Task> content = PersistentEngine.getInstance().getTasks();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);
        ListView lv = (ListView) findViewById(R.id.listView);
        WeekListAdapter week = new WeekListAdapter(this, content);
        lv.setAdapter(week);
        lv.setOnItemClickListener(this);
        Button b = (Button) findViewById(R.id.weekViewButton);
        b.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

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
