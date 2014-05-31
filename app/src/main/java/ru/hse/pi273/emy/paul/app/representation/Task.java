package ru.hse.pi273.emy.paul.app.representation;

/**
 * Higher School of Economics
 * Computer Science Faculty
 * Created by Dmitry 'Zimy' Yakovlev
 * on 28.05.14.
 */
public class Task implements Comparable<Task> {
    int day, mode, hours, minutes;

    public Task(int day, int mode, int hours, int minutes) {
        this.day = day;

        this.mode = mode;
        this.hours = hours;
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getMode() {
        return mode;
    }

    public int getDay() {

        return day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        return day == task.day && hours == task.hours && minutes == task.minutes;

    }

    @Override
    public int hashCode() {
        int result = day;
        result = 31 * result + hours;
        result = 31 * result + minutes;
        return result;
    }

    @Override
    public int compareTo(Task task) {
        return ((Integer) day).compareTo(task.day) == 0 ?
                ((((Integer) hours).compareTo(task.hours) == 0 ?
                        (((Integer) minutes).compareTo(task.minutes)) :
                        (((Integer) hours).compareTo(task.hours)))) :
                ((Integer) day).compareTo(task.day);
    }
}
