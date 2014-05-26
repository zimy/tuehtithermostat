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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import ru.hse.pi273.emy.paul.app.R;

public class CreateTaskActivity extends Activity {

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
                Log.d("Thermostat", "pos = " + day);
                TextView dayChosen = (TextView) findViewById(R.id.day_chosen);
                dayChosen.setText(days[day]);
            } else
                // выводим в лог позицию нажатого элемента
                Log.d("Thermostat", "which = " + which);
        }
    };

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
        return super.onCreateDialog(id);
    }
}
