package ru.hse.pi273.emy.paul.app.engine;

/**
 * Higher School of Economics
 * Computer Science Faculty
 * Created by Dmitry 'Zimy' Yakovlev
 * on 27.05.14.
 */
public class PersistentEngine implements Engine {
    private static PersistentEngine instance;

    private PersistentEngine() {
    }

    public static PersistentEngine getInstance() {
        if (null == instance) {
            synchronized (PersistentEngine.class) {
                if (null == instance) {
                    instance = new PersistentEngine();
                }
            }
        }
        return instance;
    }

    @Override
    public ProbeStatus probe(int day) {
        return ProbeStatus.OK;
    }
}
