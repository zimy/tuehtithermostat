package ru.hse.pi273.emy.paul.app.engine;

import com.google.inject.ImplementedBy;

import java.util.List;

import ru.hse.pi273.emy.paul.app.representation.Task;
import ru.hse.pi273.emy.paul.app.view.thermostat.ThermostatFragment;

/**
 * Higher School of Economics
 * Computer Science Faculty
 * Created by Dmitry 'Zimy' Yakovlev
 * on 26.05.14.
 */
@ImplementedBy(PersistentEngine.class)
public interface Engine {
    public ProbeStatus probe(int day, int hour, int minute, boolean equals);

    public void add(Task task);

    public List<Task> getTasks(int day);

    public int getTemperature(int tab);

    public void setTemperature(int tab, int temperature);

    public Task getDate();

    public int getMode();

    public void setOverriding(int overrideMode);

    public Task remove(int day, int hours, int minutes);

    public void pulse(int day, int hour, int minute);

    public void setObserver(ThermostatFragment obs);

    public void delObserver();
}
