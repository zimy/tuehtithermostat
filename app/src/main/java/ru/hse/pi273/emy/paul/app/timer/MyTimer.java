package ru.hse.pi273.emy.paul.app.timer;

import android.content.Intent;
import android.os.IBinder;

import com.google.inject.Inject;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import roboguice.service.RoboService;
import ru.hse.pi273.emy.paul.app.engine.Engine;

public class MyTimer extends RoboService {
    @Inject
    Engine engine;
    AtomicBoolean active = new AtomicBoolean();
    Calendar calendar = Calendar.getInstance();
    int day, hour, minute;

    public MyTimer() {
        calendar.setTime(new Date());
        active.set(true);
        day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(minute);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (active.get()) {
                    engine.pulse(day, hour, minute);
                    if (minute + 1 == 60) {
                        minute = 0;
                        if (hour + 1 == 24) {
                            hour = 0;
                            if (day + 1 == 7) {
                                day = 0;
                            } else {
                                day++;
                            }
                        } else {
                            hour++;
                        }

                    } else {
                        minute++;
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        active.set(false);
                        stopSelf();
                    }
                }
                stopSelf();
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
