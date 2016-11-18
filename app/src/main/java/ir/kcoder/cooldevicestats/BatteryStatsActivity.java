package ir.kcoder.cooldevicestats;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import ir.kcoder.KDeviceStats.KBatteryStats;


public class BatteryStatsActivity extends ActionBarActivity {
    private StatsListAdapter adapter;
    private ArrayList<StatsListModel> data = new ArrayList<>();
    private ListView list;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_stats);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.battery);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        adapter = new StatsListAdapter(this, data, this.getResources());
        data = new ArrayList<>();
        list = (ListView)findViewById(R.id.lstBatteryStats);
        activity = this;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                KBatteryStats.getInstance(activity).refreshStats();
                StatsListModel status = new StatsListModel(getString(R.string.charging_status),
                        KBatteryStats.getInstance(activity).getChargingStatus());
                StatsListModel remaining = new StatsListModel(getString(R.string.remaining_battery),
                        KBatteryStats.getInstance(activity).getBatteryPct() + "%");
                StatsListModel temperature = new StatsListModel(getString(R.string.temperature),
                        KBatteryStats.getInstance(activity).getTemperatureCelsius() + "(" +
                        KBatteryStats.getInstance(activity).getTemperatureFahrenheit() + ")");
                StatsListModel voltage = new StatsListModel(getString(R.string.voltage),
                        KBatteryStats.getInstance(activity).getVoltage() + " " + getString(R.string.millivolt));

                data.clear();
                data.add(status);
                data.add(remaining);
                data.add(temperature);
                data.add(voltage);
                adapter = new StatsListAdapter(activity, data, activity.getResources());
                list.setAdapter(adapter);
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KBatteryStats.getInstance(activity).dispose();
    }
}
