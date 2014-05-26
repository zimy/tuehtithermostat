package ru.hse.pi273.emy.paul.app.view.task;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import ru.hse.pi273.emy.paul.app.R;
import ru.hse.pi273.emy.paul.app.representation.Day;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
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
            day = calendar.get(Calendar.DAY_OF_WEEK);
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
        dayChosen.setText(Day.values()[day].toString());
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
        return super.onCreateDialog(id);
    }
}
