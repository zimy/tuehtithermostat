package ru.hse.pi273.emy.paul.app.view.task;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.inject.Inject;

import java.util.Calendar;
import java.util.Date;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import ru.hse.pi273.emy.paul.app.R;
import ru.hse.pi273.emy.paul.app.engine.Engine;
import ru.hse.pi273.emy.paul.app.engine.ProbeStatus;
import ru.hse.pi273.emy.paul.app.representation.ConcreteTaskStringsKeeper;
import ru.hse.pi273.emy.paul.app.representation.Task;
import ru.hse.pi273.emy.paul.app.representation.TaskStringKeeper;

@ContentView(R.layout.activity_create_task)
public class CreateTaskActivity extends RoboActionBarActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, OnFragmentInteractionListener {
    @Inject
    Engine engine;
    @Inject
    TaskStringKeeper taskStrings = new ConcreteTaskStringsKeeper();
    @InjectView(R.id.time_chosen)
    TextView timeChosen;
    @InjectView(R.id.day_chosen)
    TextView dayChosen;
    @InjectView(R.id.mode_chosen)
    TextView modeChosen;
    @InjectView(R.id.task_adding_status_message)
    TextView helperText;
    @InjectView(R.id.add_new_task_button)
    Button addingButton;
    int day;
    int mode;
    int Hours, Minutes;

    void probe() {
        ProbeStatus result = engine.probe(day);
        boolean btn = false;
        int msg = R.string.inner_error;
        switch (result) {
            case OK:
                switch (mode) {
                    case 0:
                    case 1:
                        btn = true;
                        msg = R.string.limits_ok;
                        break;
                    case 2:
                        btn = false;
                        msg = R.string.mode_not_selected_error;
                        break;
                }
                break;
            case LIM_D_TODAY:
                switch (mode) {
                    case 0:
                        btn = false;
                        msg = R.string.lim_d_today;
                        break;
                    case 1:
                        btn = true;
                        msg = R.string.limits_ok;
                        break;
                    case 2:
                        btn = false;
                        msg = R.string.mode_not_selected_error;
                        break;
                }
                break;
            case LIM_N_TODAY:
                switch (mode) {
                    case 0:
                        btn = true;
                        msg = R.string.limits_ok;
                        break;
                    case 1:
                        btn = false;
                        msg = R.string.lim_n_today;
                        break;
                    case 2:
                        btn = false;
                        msg = R.string.mode_not_selected_error;
                        break;
                }
                break;
            case LIM_TODAY:
                btn = false;
                msg = R.string.lim_today;
                break;
            case LIM_D:
                switch (mode) {
                    case 0:
                    case 2:
                        btn = false;
                        msg = R.string.lim_d;
                        break;
                    case 1:
                        btn = true;
                        msg = R.string.limits_ok;
                        break;
                }
                break;
            case LIM_N:
                switch (mode) {
                    case 0:
                        btn = true;
                        msg = R.string.limits_ok;
                        break;
                    case 1:
                    case 2:
                        btn = false;
                        msg = R.string.lim_n;
                        break;
                }
                break;
            default:
                btn = false;
                msg = R.string.lim;
        }
        if (msg == R.string.inner_error) {
            throw new AssertionError("Error at resolving current state. See CreateTaskActivity.probe() source code.");
        }
        addingButton.setEnabled(btn);
        helperText.setText(getResources().getString(msg));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();
        bar.setHomeButtonEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        addingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                engine.add(new Task(day, mode, Hours, Minutes));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if ("EDIT".equals(intent.getStringExtra("Action"))) {
            Hours = Integer.parseInt(intent.getStringExtra("Hours"));
            Minutes = Integer.parseInt(intent.getStringExtra("Minutes"));
            day = Integer.parseInt(intent.getStringExtra("Day"));
            mode = Integer.parseInt(intent.getStringExtra("Mode"));
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            Hours = calendar.get(Calendar.HOUR_OF_DAY);

            Minutes = calendar.get(Calendar.MINUTE);
            day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            mode = 2;
        }
        Log.d("CreateTask", "Today is " + day + " day of week, " + Hours + ":" + Minutes);
        timeChosen.setText("" + (Hours < 10 ? "0" + Hours : Hours) + ":" + (Minutes < 10 ? "0" + Minutes : Minutes));
        timeChosen.setOnClickListener(this);
        dayChosen.setText(taskStrings.getDays()[day]);
        dayChosen.setOnClickListener(this);
        modeChosen.setText("" + taskStrings.getModeMessages()[mode]);
        modeChosen.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (timeChosen.equals(view)) {
            (new TimePickerDialog(this, this, Hours, Minutes, true)).show();
        } else if (dayChosen.equals(view)) {
            DayDialog.newInstance(day).show(getSupportFragmentManager(), "Day Dialog");
        } else if (modeChosen.equals(view)) {
            ModeDialog.newInstance(mode).show(getSupportFragmentManager(), "Mode Dialog");
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i2) {
        Hours = i;
        Minutes = i2;
        timeChosen.setText("" + (Hours < 10 ? "0" + Hours : Hours) + ":" + (Minutes < 10 ? "0" + Minutes : Minutes));
    }

    @Override
    public void onFragmentInteraction(DialogCallback callbackMode, int value) {
        switch (callbackMode) {
            case DAY:
                day = value;
                dayChosen.setText(taskStrings.getDays()[day]);
                break;
            case MODE:
                mode = value;
                modeChosen.setText(taskStrings.getModes()[mode]);
                break;
        }
        probe();
    }
}
