package ru.hse.pi273.emy.paul.app.view.week;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.hse.pi273.emy.paul.app.R;
import ru.hse.pi273.emy.paul.app.representation.Task;

/**
 * Higher School of Economics
 * Computer Science Faculty
 * Created by Dmitry 'Zimy' Yakovlev
 * on 26.05.14.
 */
public class WeekListAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater inflater;
    List<Task> objects;

    public WeekListAdapter(Context ctx, List<Task> objects) {
        this.ctx = ctx;
        this.objects = objects;
        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int i) {
        return objects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1 = view;
        if (view1 == null) {
            view1 = inflater.inflate(R.layout.tasklist_adapter, viewGroup, false);
        }
        Task c = objects.get(i);
        if (view1 != null) {
            ((TextView) view1.findViewById(R.id.adapter_text)).setText("" + c.getDay() + " " + c.getHours() + ":" + c.getMinutes() + " " + c.getMode());
        }
        return view1;
    }
}
