package ru.hse.pi273.emy.paul.app.view.task;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import ru.hse.pi273.emy.paul.app.R;
import ru.hse.pi273.emy.paul.app.engine.Engine;
import ru.hse.pi273.emy.paul.app.engine.PersistentEngine;
import ru.hse.pi273.emy.paul.app.engine.ProbeStatus;

public class CreateTaskActivity extends Activity {
    Engine engine = PersistentEngine.getInstance();
    int Hours, Minutes;
    TimePickerDialog.OnTimeSetListener myCallBack = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Hours = hourOfDay;
            Minutes = minute;
            TextView timechosen = (TextView) findViewById(R.id.time_chosen);
            timechosen.setText("" + Hours + ":" + Minutes);
        }
    };

    int day;
    int mode;
    String days[];
    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            ListView lv = ((AlertDialog) dialog).getListView();
            if (which == Dialog.BUTTON_POSITIVE) {
                // выводим в лог позицию выбранного элемента
                day = lv.getCheckedItemPosition();
                TextView dayChosen = (TextView) findViewById(R.id.day_chosen);
                dayChosen.setText(days[day]);
                probe();
            }
        }
    };
    String modes[];
    DialogInterface.OnClickListener myAnotherClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            ListView lv = ((AlertDialog) dialog).getListView();
            if (which == Dialog.BUTTON_POSITIVE) {
                // выводим в лог позицию выбранного элемента
                mode = lv.getCheckedItemPosition();
                TextView dayChosen = (TextView) findViewById(R.id.mode_chosen);
                dayChosen.setText(modes[mode]);
                probe();
            }
        }
    };

    void probe() {
        TextView tw = (TextView) findViewById(R.id.task_adding_status_message);
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
        Button b = (Button) findViewById(R.id.add_new_task_button);
        b.setEnabled(btn);
        tw.setText(getResources().getString(msg));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        days = new String[]{
                getResources().getString(R.string.day_sunday),
                getResources().getString(R.string.day_monday),
                getResources().getString(R.string.day_tuesday),
                getResources().getString(R.string.day_wednesday),
                getResources().getString(R.string.day_thursday),
                getResources().getString(R.string.day_friday),
                getResources().getString(R.string.day_saturday),
        };
        modes = new String[]{
                getResources().getString(R.string.mode_day),
                getResources().getString(R.string.mode_night),
        };
        Button b = (Button) findViewById(R.id.add_new_task_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            switch (day) {
                case Calendar.MONDAY:
                    Log.d("Calendar", "MO");
                    break;
                case Calendar.TUESDAY:
                    Log.d("Calendar", "TU");
                    break;
                case Calendar.WEDNESDAY:
                    Log.d("Calendar", "WE");
                    break;
                case Calendar.THURSDAY:
                    Log.d("Calendar", "TH");
                    break;
                case Calendar.FRIDAY:
                    Log.d("Calendar", "FR");
                    break;
                case Calendar.SATURDAY:
                    Log.d("Calendar", "SA");
                    break;
                case Calendar.SUNDAY:
                    Log.d("Calendar", "SU");
                    break;
            }
            mode = 2;
        }

        TextView timechosen = (TextView) findViewById(R.id.time_chosen);
        timechosen.setText("" + Hours + ":" + Minutes);
        timechosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(1);
            }
        });
        TextView dayChosen = (TextView) findViewById(R.id.day_chosen);
        dayChosen.setText(days[day]);
        dayChosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(2);
            }
        });
        TextView modeChosen = (TextView) findViewById(R.id.mode_chosen);
        modeChosen.setText("" + (mode == 2 ? ("Tap here to select mode") : (mode == 0 ? "Day" : "Night")));
        modeChosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(3);
            }
        });
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
            TimePickerDialog tpd = new TimePickerDialog(this, myCallBack, Hours, Minutes, true);
            return tpd;
        }
        if (id == 2) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle(R.string.day_dialog_title);
            adb.setSingleChoiceItems(days, day, myClickListener);

            adb.setPositiveButton(R.string.button_chose, myClickListener);
            return adb.create();
        }
        if (id == 3) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle(R.string.mode_dialog_title);
            adb.setSingleChoiceItems(modes, day, myAnotherClickListener);

            adb.setPositiveButton(R.string.button_chose, myAnotherClickListener);
            return adb.create();
        }
        return super.onCreateDialog(id);
    }
}
