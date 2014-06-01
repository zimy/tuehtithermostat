package ru.hse.pi273.emy.paul.app.engine;

import android.util.Log;

import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ru.hse.pi273.emy.paul.app.representation.Task;

/**
 * Higher School of Economics
 * Computer Science Faculty
 * Created by Dmitry 'Zimy' Yakovlev
 * on 27.05.14.
 */
@Singleton
public class PersistentEngine implements Engine {
    private final List<Task> tasks = new ArrayList<>();
    private final List<List<Task>> tasksByDay = new ArrayList<>();
    String sup = "1";
    String sDown = "10";
    int up, down;
    int[] temperatures = new int[]{200, 200, 200};
    int mode = 0;
    boolean overriding = false;

    PersistentEngine() {
        for (int i = 0; i < 7; i++) {
            tasksByDay.add(new ArrayList<Task>());
        }
        up = Integer.parseInt(sup);
        down = Integer.parseInt(sDown);
        Log.d("Engine", "Created");
    }

    @Override
    public ProbeStatus probe(int day) {
        ProbeStatus status;
        int totalDay = 0, totalNight = 0, currentDay = 0, currentNight = 0;
        for (Task task : tasks) {
            if ((task.getMode() == 0)) {
                totalDay++;
                if (task.getDay() == day) {
                    currentDay++;
                }
            } else {
                totalNight++;
                if (task.getDay() == day) {
                    currentNight++;
                }
            }
        }
        if (totalDay + totalNight == 70) {
            status = ProbeStatus.LIM;
        } else if (currentDay + currentNight == 10) {
            status = ProbeStatus.LIM_TODAY;
        } else if (totalDay == 35) {
            status = ProbeStatus.LIM_D;
        } else if (totalNight == 35) {
            status = ProbeStatus.LIM_N;
        } else if (currentDay == 5) {
            status = ProbeStatus.LIM_D_TODAY;
        } else if (currentNight == 5) {
            status = ProbeStatus.LIM_N_TODAY;
        } else {
            status = ProbeStatus.OK;
        }
        Log.d("Engine", "Probed " + day + ", " + status);
        return status;
    }

    @Override
    public void add(Task task) {
        tasks.add(task);
        tasksByDay.get(task.getDay()).add(task);
        Collections.sort(tasksByDay.get(task.getDay()));
        Log.d("Engine", "Added to " + task.getDay());
    }

    @Override
    public List<Task> getTasks(int day) {
        Log.d("Engine", "Requested " + day);
        return tasksByDay.get(day);
    }

    @Override
    public int getTemperature(int tab) {
        return temperatures[tab];
    }

    @Override
    public void setTemperature(int tab, int temperature) {
        temperatures[tab] = temperature;
        Log.d("Engine", "" + tab + ":" + temperature);
    }

    @Override
    public Date getDate() {
        return new Date();
    }

    @Override
    public int getMode() {
        return mode;
    }

    @Override
    public void setPermanentOverriding(boolean override) {
        overriding = override;
        Log.d("Engine", "Override: " + override);
    }
}
