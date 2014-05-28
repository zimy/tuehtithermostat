package ru.hse.pi273.emy.paul.app.engine;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import ru.hse.pi273.emy.paul.app.representation.Task;

/**
 * Higher School of Economics
 * Computer Science Faculty
 * Created by Dmitry 'Zimy' Yakovlev
 * on 27.05.14.
 */
public class PersistentEngine implements Engine {
    private static PersistentEngine instance;
    private List<Task> tasks = new CopyOnWriteArrayList<>();

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
        int totalday = 0, totalnight = 0, currentday = 0, currentnight = 0;
        for (Task task : tasks) {
            if ((task.getMode() == 0)) {
                totalday++;
                if (task.getDay() == day) {
                    currentday++;
                }
            } else {
                totalnight++;

                if (task.getDay() == day) {
                    currentnight++;
                }
            }
        }
        if (totalday + totalnight == 70) {
            return ProbeStatus.LIM;
        }
        if (currentday + currentnight == 10) {
            return ProbeStatus.LIM_TODAY;
        }
        if (totalday == 35) {
            return ProbeStatus.LIM_D;
        }
        if (totalnight == 35) {
            return ProbeStatus.LIM_N;
        }
        if (currentday == 5) {
            return ProbeStatus.LIM_D_TODAY;
        }
        if (currentnight == 5) {
            return ProbeStatus.LIM_N_TODAY;
        }
        return ProbeStatus.OK;
    }

    @Override
    public void add(Task task) {
        tasks.add(task);
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
