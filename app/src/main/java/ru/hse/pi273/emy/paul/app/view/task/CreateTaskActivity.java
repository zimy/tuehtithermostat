package ru.hse.pi273.emy.paul.app.view.task;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import roboguice.inject.InjectView;
import ru.hse.pi273.emy.paul.app.R;
import ru.hse.pi273.emy.paul.app.engine.Engine;
import ru.hse.pi273.emy.paul.app.engine.ProbeStatus;
import ru.hse.pi273.emy.paul.app.representation.ConcreteTaskStringsKeeper;
import ru.hse.pi273.emy.paul.app.representation.Task;
import ru.hse.pi273.emy.paul.app.representation.TaskStringKeeper;

public class CreateTaskActivity extends RoboActionBarActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {
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
    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            if (which == Dialog.BUTTON_POSITIVE) {
                day = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                dayChosen.setText(taskStrings.getDays()[day]);
                probe();
            }
        }
    };
    int mode;
    DialogInterface.OnClickListener myAnotherClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            if (which == Dialog.BUTTON_POSITIVE) {
                mode = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                modeChosen.setText(taskStrings.getModes()[mode]);
                probe();
            }
        }
    };
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
        setContentView(R.layout.activity_create_task);

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
        timeChosen.setText("" + Hours + ":" + Minutes); //TODO this stub produces trash like that 1:0
        timeChosen.setOnClickListener(this);
        dayChosen.setText(taskStrings.getDays()[day]);
        dayChosen.setOnClickListener(this);
        modeChosen.setText("" + taskStrings.getModeMessages()[mode]);
        modeChosen.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_task, menu);
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
    protected Dialog onCreateDialog(int id) {
        if (id == 1) {
            return new TimePickerDialog(this, this, Hours, Minutes, true);
        }
        if (id == 2) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle(R.string.day_dialog_title);
            adb.setSingleChoiceItems(taskStrings.getDays(), day, myClickListener);

            adb.setPositiveButton(R.string.button_chose, myClickListener);
            return adb.create();
        }
        if (id == 3) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle(R.string.mode_dialog_title);
            adb.setSingleChoiceItems(taskStrings.getModes(), day, myAnotherClickListener);

            adb.setPositiveButton(R.string.button_chose, myAnotherClickListener);
            return adb.create();
        }
        return super.onCreateDialog(id);
    }

    @Override
    public void onClick(View view) {
        if (timeChosen.equals(view)) {
            showDialog(1);
        } else if (dayChosen.equals(view)) {
            showDialog(2);
        } else if (modeChosen.equals(view)) {
            showDialog(3);
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i2) {
        Hours = i;
        Minutes = i2;
        timeChosen.setText("" + Hours + ":" + Minutes);
    }
}
