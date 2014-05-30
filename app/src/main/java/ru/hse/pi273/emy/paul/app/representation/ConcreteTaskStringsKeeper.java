package ru.hse.pi273.emy.paul.app.representation;

import com.google.inject.Singleton;

import roboguice.inject.InjectResource;
import ru.hse.pi273.emy.paul.app.R;

/**
 * Higher School of Economics
 * Computer Science Faculty
 * Created by Dmitry 'Zimy' Yakovlev
 * on 29.05.14.
 */
@Singleton
public class ConcreteTaskStringsKeeper implements TaskStringKeeper {
    @InjectResource(R.string.day_monday)
    private String Monday;
    @InjectResource(R.string.day_tuesday)
    private String Tuesday;
    @InjectResource(R.string.day_wednesday)
    private String Wednesday;
    @InjectResource(R.string.day_thursday)
    private String Thursday;
    @InjectResource(R.string.day_friday)
    private String Friday;
    @InjectResource(R.string.day_saturday)
    private String Saturday;
    @InjectResource(R.string.day_sunday)
    private String Sunday;

    @InjectResource(R.string.mode_day)
    private String Day;
    @InjectResource(R.string.mode_night)
    private String Night;
    @InjectResource(R.string.mode_notSet)
    private String NotSet;

    @Override
    public String[] getDays() {
        return new String[]{Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday};
    }

    @Override
    public String[] getModes() {
        return new String[]{Day, Night};
    }

    @Override
    public String[] getModeMessages() {
        return new String[]{Day, Night, NotSet};
    }
}
