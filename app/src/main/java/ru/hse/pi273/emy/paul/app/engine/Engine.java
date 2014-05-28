package ru.hse.pi273.emy.paul.app.engine;

import ru.hse.pi273.emy.paul.app.representation.Task;

/**
 * Higher School of Economics
 * Computer Science Faculty
 * Created by Dmitry 'Zimy' Yakovlev
 * on 26.05.14.
 */
public interface Engine {
    public ProbeStatus probe(int day);

    void add(Task task);
}
