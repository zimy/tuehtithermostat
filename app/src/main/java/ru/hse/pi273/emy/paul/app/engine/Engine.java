package ru.hse.pi273.emy.paul.app.engine;

import com.google.inject.ImplementedBy;

import java.util.Date;
import java.util.List;

import ru.hse.pi273.emy.paul.app.representation.Task;

/**
 * Higher School of Economics
 * Computer Science Faculty
 * Created by Dmitry 'Zimy' Yakovlev
 * on 26.05.14.
 */
@ImplementedBy(PersistentEngine.class)
public interface Engine {
    public ProbeStatus probe(int day, int hour, int minute);

    public void add(Task task);

    public List<Task> getTasks(int day);

    public int getTemperature(int tab);

    public void setTemperature(int tab, int temperature);

    public Date getDate();

    public int getMode();

    public void setPermanentOverriding(boolean override);
}
