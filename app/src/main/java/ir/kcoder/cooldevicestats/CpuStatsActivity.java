package ir.kcoder.cooldevicestats;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import ir.kcoder.KDeviceStats.KBatteryStats;
import ir.kcoder.KDeviceStats.KCPUStats;
import ir.kcoder.KDeviceStats.KProcessStats;
import ir.kcoder.KDeviceStats.KRAMStats;
import ir.kcoder.KDeviceStats.KStorageStats;


public class CpuStatsActivity extends ActionBarActivity {
    private StatsListAdapter adapter;
    private ListView list;
    private ArrayList<StatsListModel> data = new ArrayList<>();
    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu_stats);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.cpu);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        list = (ListView)findViewById(R.id.lstCpuStats);
        activity = this;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                KCPUStats.getInstance().refreshStats();
                data.clear();

                data.add(new StatsListModel(getString(R.string.active_cores),
                        KCPUStats.getInstance().getCoreCount() + " "));

                for(int i = 0; i <= KCPUStats.getInstance().getCoreCount(); i++) {
                    if(i == 0) {
                        data.add(
                                new StatsListModel(getString(R.string.total_cpu),
                                    KCPUStats.getInstance().calculateCpuUsage(i) + "%")
                        );
                    }
                    else {
                        String title = getString(R.string.core_activity);
                        title = title.replace("#", Integer.toString(i));
                        data.add(
                                new StatsListModel(title, KCPUStats.getInstance().calculateCpuUsage(i) + "%")
                        );
                    }
                }

                adapter = new StatsListAdapter(activity, data, activity.getResources());
                list.setAdapter(adapter);

                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    public void launchProcesses(View v) {
        Intent intent = new Intent(CpuStatsActivity.this, ProcessesActivity.class);
        this.startActivity(intent);
    }
}
