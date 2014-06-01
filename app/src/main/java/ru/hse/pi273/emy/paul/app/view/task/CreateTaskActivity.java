package ru.hse.pi273.emy.paul.app.view.task;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.inject.Inject;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
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
    @InjectExtra("Action")
    String action;
    @InjectExtra(value = "Day", optional = true)
    int iniDay = -1;
    @InjectExtra(value = "Mode", optional = true)
    int mode = 2;
    @InjectExtra(value = "Hours", optional = true)
    int iniHours = -1;
    @InjectExtra(value = "Minutes", optional = true)
    int iniMinutes = -1;
    int Day, Hours, Minutes;

    void probe() {
        ProbeStatus result = engine.probe(Day, Hours, Minutes, "Edit".equals(action));
        boolean btn = false;
        boolean editing = "Edit".equals(action) && iniDay == Day && iniHours == Hours && iniMinutes == Minutes;
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
            case COLLISION:
                btn = editing;
                msg = editing ? R.string.limits_ok : R.string.collision;
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
            case LIM:
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
        addingButton.setOnClickListener(this);
        if (iniHours == -1) {
            iniHours = engine.getDate().getHours();
            iniMinutes = engine.getDate().getMinutes();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Day = iniDay;
        Hours = iniHours;
        Minutes = iniMinutes;
        timeChosen.setText("" + (Hours < 10 ? "0" + Hours : Hours) + ":" + (Minutes < 10 ? "0" + Minutes : Minutes));
        timeChosen.setOnClickListener(this);
        dayChosen.setText(taskStrings.getDays()[Day]);
        dayChosen.setOnClickListener(this);
        modeChosen.setText("" + taskStrings.getModeMessages()[mode]);
        modeChosen.setOnClickListener(this);
        probe();
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
            DayDialog.newInstance(Day).show(getSupportFragmentManager(), "Day Dialog");
        } else if (modeChosen.equals(view)) {
            ModeDialog.newInstance(mode != 2 ? mode : 0).show(getSupportFragmentManager(), "Mode Dialog");
        } else if (addingButton.equals(view)) {
            if ("Edit".equals(action)) {
                engine.remove(iniDay, iniHours, iniMinutes);
            }
            engine.add(new Task(Day, mode, Hours, Minutes));
            finish();
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i2) {
        Hours = i;
        Minutes = i2;
        timeChosen.setText("" + (Hours < 10 ? "0" + Hours : Hours) + ":" + (Minutes < 10 ? "0" + Minutes : Minutes));
        probe();
    }

    @Override
    public void onFragmentInteraction(DialogCallback callbackMode, int value) {
        switch (callbackMode) {
            case DAY:
                Day = value;
                dayChosen.setText(taskStrings.getDays()[Day]);
                break;
            case MODE:
                mode = value;
                modeChosen.setText(taskStrings.getModes()[mode]);
                break;
        }
        probe();
    }
}
