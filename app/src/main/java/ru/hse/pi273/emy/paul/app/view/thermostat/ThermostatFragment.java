package ru.hse.pi273.emy.paul.app.view.thermostat;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.inject.Inject;

import java.util.Calendar;
import java.util.Date;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import ru.hse.pi273.emy.paul.app.R;
import ru.hse.pi273.emy.paul.app.engine.Engine;
import ru.hse.pi273.emy.paul.app.representation.TaskStringKeeper;

public class ThermostatFragment extends RoboFragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    @Inject
    Engine engine;
    @Inject
    TaskStringKeeper stringKeeper;
    @InjectView(R.id.thermostat_left)
    TextView left;
    @InjectView(R.id.thermostat_right)
    TextView right;
    @InjectView(R.id.mego_text)
    TextView megoTextView;
    @InjectView(R.id.seekBar)
    SeekBar seekBar;
    @InjectView(R.id.override_buttons)
    LinearLayout linearLayout;
    @InjectView(R.id.button_override)
    ToggleButton overrideButton;
    @InjectView(R.id.button_vacation)
    ToggleButton vacationButton;
    @InjectResource(R.string.formatter_range)
    String tempFormatter;
    @InjectResource(R.string.formatter_mego)
    String megoFormatter;
    @InjectResource(R.string.formatter_mode)
    String modeFormatter;
    @InjectResource(R.string.formatter_date)
    String dateFormatter;
    int temperature;
    private int tab;
    private int min;
    private Date date;
    private int overrideMode = 0;

    public ThermostatFragment() {
    }

    public static ThermostatFragment newInstance(int tab) {
        ThermostatFragment fragment = new ThermostatFragment();
        Bundle args = new Bundle();
        args.putInt("Type", tab);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tab = getArguments().getInt("Type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_thermostat, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        temperature = engine.getTemperature(tab);
        min = 50;
        megoTextView.setText(String.format(megoFormatter, 20, 0));
        seekBar.setMax(250);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setProgress(150);
        date = engine.getDate();
        right.setText(date.toString());

        if (tab != 0) {
            right.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
            left.setText(String.format(tempFormatter, 5, 30, 0.1));
        } else {
            left.setText(String.format(modeFormatter, stringKeeper.getModes()[engine.getMode()]));
            seekBar.setEnabled(false);
            vacationButton.setEnabled(false);
            overrideButton.setOnClickListener(this);
            vacationButton.setOnClickListener(this);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            right.setText(String.format(dateFormatter, stringKeeper.getDays()[calendar.get(Calendar.DAY_OF_WEEK) - 1], calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        megoTextView.setText(String.format(megoFormatter, (min + i) / 10, i % 10));
        engine.setTemperature(tab, min + i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_override) {
            overrideMode = overrideMode == 0 ? 1 : 0;
            vacationButton.setEnabled(overrideMode == 1);
            seekBar.setEnabled(overrideMode == 1);
            engine.setPermanentOverriding(false);
            overrideButton.setChecked(false);
        } else if (view.getId() == R.id.button_vacation) {
            overrideMode = overrideMode == 1 ? 2 : 1;
            engine.setPermanentOverriding(overrideMode == 2);
        }
    }
}
