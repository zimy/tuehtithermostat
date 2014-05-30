package ru.hse.pi273.emy.paul.app.representation;

import com.google.inject.ImplementedBy;

/**
 * Higher School of Economics
 * Computer Science Faculty
 * Created by Dmitry 'Zimy' Yakovlev
 * on 29.05.14.
 */
@ImplementedBy(ConcreteTaskStringsKeeper.class)
public interface TaskStringKeeper {
    public String[] getDays();

    public String[] getModes();

    public String[] getModeMessages();
}
