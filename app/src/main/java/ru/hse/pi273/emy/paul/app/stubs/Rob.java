package ru.hse.pi273.emy.paul.app.stubs;

import com.google.inject.AbstractModule;

import ru.hse.pi273.emy.paul.app.engine.Engine;
import ru.hse.pi273.emy.paul.app.engine.PersistentEngine;
import ru.hse.pi273.emy.paul.app.representation.ConcreteTaskStringsKeeper;
import ru.hse.pi273.emy.paul.app.representation.TaskStringKeeper;

/**
 * Higher School of Economics
 * Computer Science Faculty
 * Created by Dmitry 'Zimy' Yakovlev
 * on 29.05.14.
 */
public class Rob extends AbstractModule {
    @Override
    protected void configure() {
        bind(Engine.class).to(PersistentEngine.class);
        bind(TaskStringKeeper.class).to(ConcreteTaskStringsKeeper.class);
    }
}
