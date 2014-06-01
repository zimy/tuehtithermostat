package ru.hse.pi273.emy.paul.app.engine;

import android.util.Log;

import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import ru.hse.pi273.emy.paul.app.representation.Task;
import ru.hse.pi273.emy.paul.app.view.thermostat.ThermostatFragment;

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
    ThermostatFragment observer;
    String sup = "1";
    String sDown = "10";
    int up, down;
    int[] temperatures = new int[]{200, 200, 200};
    int scheduleTemp = 200;
    int mode = 0;
    int overriding = 0;
    int day, hour, minute;

    PersistentEngine() {
        for (int i = 0; i < 7; i++) {
            tasksByDay.add(new ArrayList<Task>());
        }
        up = Integer.parseInt(sup);
        down = Integer.parseInt(sDown);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        Log.i("Engine", "Created");
    }

    @Override
    public ProbeStatus probe(int day, int hour, int minute, boolean equals) {
        Task newTask = null;
        if (equals) {
            newTask = remove(day, hour, minute);
        }
        ProbeStatus status;
        int totalDay = 0, totalNight = 0, currentDay = 0, currentNight = 0;
        boolean collision = false;
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
            collision = collision | task.equals(new Task(day, 0, hour, minute));
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
        } else if (collision) {
            status = ProbeStatus.COLLISION;
        } else {
            status = ProbeStatus.OK;
        }
        Log.i("Engine", "Probed " + day + ", " + status);
        if (equals) {
            add(newTask);
        }
        return status;
    }

    @Override
    public void add(Task task) {
        tasks.add(task);
        tasksByDay.get(task.getDay()).add(task);
        Collections.sort(tasksByDay.get(task.getDay()));
        Log.i("Engine", "Added to " + task.getDay());
    }

    @Override
    public List<Task> getTasks(int day) {
        Log.i("Engine", "Requested " + day);
        return tasksByDay.get(day);
    }

    @Override
    public int getTemperature(int tab) {
        return temperatures[tab];
    }

    @Override
    public void setTemperature(int tab, int temperature) {
        temperatures[tab] = temperature;
        Log.i("Engine", "" + tab + ":" + temperature);
    }

    @Override
    public Task getDate() {
        return new Task(day, 2, hour, minute);
    }

    @Override
    public int getMode() {
        return mode;
    }

    @Override
    public void setOverriding(int overrideMode) {
        overriding = overrideMode;
        Log.i("Engine", "Override: " + overrideMode);
    }

    @Override
    public Task remove(int day, int hours, int minutes) {
        Task newTask = new Task(day, 0, hours, minutes);
        for (int i = 0; i < tasks.size(); i++) {
            if (newTask.equals(tasks.get(i))) {
                newTask = tasks.get(i);
                tasks.remove(i);
                break;
            }
        }
        for (int i = 0; i < tasksByDay.get(day).size(); i++) {
            if (newTask.equals(tasksByDay.get(day).get(i))) {
                tasksByDay.get(day).remove(i);
                break;
            }
        }
        return newTask;
    }

    @Override
    public void pulse(int pday, int phour, int pminute) {
        Log.i("Engine", "Pulse " + pday + " " + phour + " " + pminute);
        hour = phour;
        day = pday;
        minute = pminute;
        operate();
        if (observer != null) {
            observer.update();
        }
    }

    private void operate() {
        mode = (mode + 1) * 11;
        if (hour == 0 && minute == 0) {
            mode = 1;
        }
        List<Task> lllst = tasksByDay.get(day);
        for (Task task : lllst) {
            if (task.getHours() == hour & task.getMinutes() == minute) {
                mode = task.getMode();
            }
        }
        if (mode < 10) {
            if (overriding == 1) {
                overriding = 0;
            }
            scheduleTemp = temperatures[1 + mode];
            if (overriding == 0) {
                temperatures[0] = scheduleTemp;
            }
        } else {
            if (mode == 22) {
                mode = 1;
            } else if (mode == 11) {
                mode = 0;
            }
        }

    }

    @Override
    public void setObserver(ThermostatFragment obs) {
        observer = obs;
    }

    @Override
    public void delObserver() {
        observer = null;
    }
}
