package ru.hse.pi273.emy.paul.app.view.week;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.inject.Inject;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import ru.hse.pi273.emy.paul.app.R;
import ru.hse.pi273.emy.paul.app.engine.Engine;

public class WeekDayFragment extends RoboFragment implements AdapterView.OnItemClickListener {

    @Inject
    Engine engine;
    @InjectView(R.id.listView)
    ListView listView;
    WeekListAdapter week;
    private int Day;

    public WeekDayFragment() {
    }

    public static WeekDayFragment newInstance(int DayParam) {
        WeekDayFragment fragment = new WeekDayFragment();
        Bundle args = new Bundle();
        args.putInt("Day", DayParam);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Day = getArguments().getInt("Day");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_week_day, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        week = new WeekListAdapter(getActivity(), engine.getTasks(Day));
        listView.setAdapter(week);
        week.notifyDataSetChanged();
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        week.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
