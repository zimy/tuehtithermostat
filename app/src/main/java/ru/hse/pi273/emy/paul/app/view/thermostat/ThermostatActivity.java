package ru.hse.pi273.emy.paul.app.view.thermostat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.inject.Inject;

import roboguice.activity.RoboActionBarActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import ru.hse.pi273.emy.paul.app.R;
import ru.hse.pi273.emy.paul.app.representation.TaskStringKeeper;
import ru.hse.pi273.emy.paul.app.view.week.WeekViewActivity;

@ContentView(R.layout.activity_main)
public class ThermostatActivity extends RoboActionBarActivity implements ActionBar.TabListener {
    @InjectResource(R.string.screen_home)
    String home;
    @Inject
    TaskStringKeeper stringKeeper;
    ThermostatFragment[] fragments = new ThermostatFragment[]{
            ThermostatFragment.newInstance(0),
            ThermostatFragment.newInstance(1),
            ThermostatFragment.newInstance(2)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setHomeButtonEnabled(true);
        addTab(bar, home);
        addTab(bar, stringKeeper.getModes()[0]);
        addTab(bar, stringKeeper.getModes()[1]);
    }

    void addTab(ActionBar bar, String title) {
        ActionBar.Tab tab = bar.newTab();
        tab.setText(title);
        tab.setTabListener(this);
        bar.addTab(tab);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_week) {
            startActivity(new Intent(this, WeekViewActivity.class));
            return true;
        }
        if (id == android.R.id.home) {
            getSupportActionBar().setSelectedNavigationItem(0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        fragmentTransaction.replace(R.id.main_fragment, fragments[tab.getPosition()]);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
}
