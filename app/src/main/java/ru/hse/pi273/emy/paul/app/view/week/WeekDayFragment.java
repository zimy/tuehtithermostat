package ru.hse.pi273.emy.paul.app.view.week;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import ru.hse.pi273.emy.paul.app.R;
import ru.hse.pi273.emy.paul.app.engine.Engine;
import ru.hse.pi273.emy.paul.app.representation.Task;
import ru.hse.pi273.emy.paul.app.view.task.CreateTaskActivity;

public class WeekDayFragment extends RoboFragment implements AdapterView.OnItemClickListener, View.OnLongClickListener, AdapterView.OnItemSelectedListener {

    @Inject
    Engine engine;
    @InjectView(R.id.listView)
    ListView listView;
    WeekListAdapter week;
    boolean deleting = false;
    List<Integer> selected = new ArrayList<>(10);
    private int Day;

    public WeekDayFragment() {
        for (int i = 0; i < 10; i++) {
            selected.add(0);
        }
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
        listView.setOnLongClickListener(this);
        listView.setOnItemSelectedListener(this);
    }

    @Override
    public void onResume() {
        week.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (!deleting) {
            Task task = week.objects.get(i);
            startActivity(new Intent(getActivity(), CreateTaskActivity.class)
                    .putExtra("Action", "Edit")
                    .putExtra("Day", task.getDay())
                    .putExtra("Hours", task.getHours())
                    .putExtra("Minutes", task.getMinutes())
                    .putExtra("Mode", task.getMode()));
        } else {
            boolean itemChecked = listView.isItemChecked(i);
            view.setBackground(getResources().getDrawable(itemChecked ? R.drawable.color_blue : R.drawable.color_white));
        }
    }

    public void multiChose(boolean multiple) {
        deleting = multiple;
        if (multiple) {
            listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        } else {
            listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
            for (int i = 0; i < week.objects.size(); i++) {
                listView.setItemChecked(i, false);
            }
        }
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    public void deleteSelected() {
        if (deleting) {
            SparseBooleanArray sbArray = listView.getCheckedItemPositions();
            assert sbArray != null;
            for (int i = 0; i < sbArray.size(); i++) {
                {
                    int key = sbArray.keyAt(i);
                    if (sbArray.get(key)) {
                        Task task = week.objects.get(i);
                        engine.remove(task.getDay(), task.getHours(), task.getMinutes());
                    }
                }
            }
        }
        week.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
