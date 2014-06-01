package ru.hse.pi273.emy.paul.app.view.thermostat;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.inject.Inject;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import ru.hse.pi273.emy.paul.app.R;
import ru.hse.pi273.emy.paul.app.engine.Engine;
import ru.hse.pi273.emy.paul.app.representation.Task;
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
    int lastTemp = 0;
    boolean firstLook = true;
    private int tab;
    private int min = 50;
    private Task date;
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
        seekBar.setMax(250);
        seekBar.setOnSeekBarChangeListener(this);
        vacationButton.setOnClickListener(this);
        if (tab != 0) {
            right.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fromEngine();
        if (tab != 0) {
            left.setText(String.format(tempFormatter, 5, 30, 0.1));
        } else {
            engine.setObserver(this);
        }
    }

    @Override
    public void onPause() {
        firstLook = true;
        super.onPause();
        engine.delObserver();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (!firstLook) {
            megoTextView.setText(String.format(megoFormatter, (min + i) / 10, i % 10));
            if (tab == 0) {
                overrideMode = overrideMode == 0 ? 1 : overrideMode;
                engine.setOverriding(overrideMode);
            }
            lastTemp = i;
            engine.setTemperature(tab, min + i);

            Log.i("Home", "Changing!");
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        firstLook = false;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_vacation) {
            overrideMode = overrideMode == 1 || overrideMode == 0 ? 2 : 0;
            engine.setOverriding(overrideMode);
        }
    }

    public void update() {
        fromEngine();
    }

    private void fromEngine() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lastTemp = temperature = engine.getTemperature(tab);
                date = engine.getDate();
                int hour = date.getHours(), minute = date.getMinutes(), day = date.getDay();
                left.setText(String.format(modeFormatter, stringKeeper.getModes()[engine.getMode()]));
                right.setText(String.format(dateFormatter, stringKeeper.getDays()[day], hour < 10 ? "0" + hour : hour, minute < 10 ? "0" + minute : minute));
                megoTextView.setText(String.format(megoFormatter, temperature / 10, temperature % 10));
                seekBar.setProgress(temperature - min);
            }
        });
    }
}
