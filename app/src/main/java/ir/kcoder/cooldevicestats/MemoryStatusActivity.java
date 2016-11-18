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
import ir.kcoder.KDeviceStats.KRAMStats;
import ir.kcoder.KDeviceStats.KStorageStats;


public class MemoryStatusActivity extends ActionBarActivity {
    private StatsListAdapter adapter;
    private ArrayList<StatsListModel> data = new ArrayList<>();
    private ListView list;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_status);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.memory);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        adapter = new StatsListAdapter(this, data, this.getResources());
        data = new ArrayList<>();
        list = (ListView)findViewById(R.id.lstMemoryStatus);
        activity = this;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                KStorageStats.getInstance().refreshStats();

                StatsListModel internal = new StatsListModel(getString(R.string.internal_storage),
                        KStorageStats.getInstance().getUsedInternal() + "MB / " +
                        KStorageStats.getInstance().getTotalInternal() + "MB ( " +
                        KStorageStats.getInstance().getInternalPercentage() + "%" + " )");
                StatsListModel external;
                if(KStorageStats.getInstance().getTotalExternal() > 0) {
                    external = new StatsListModel(getString(R.string.external_storage),
                            KStorageStats.getInstance().getUsedExternal() + "MB / " +
                                    KStorageStats.getInstance().getTotalExternal() + "MB ( " +
                                    KStorageStats.getInstance().getExternalPercentage() + "%" + " )");
                }
                else {
                    external = new StatsListModel(getString(R.string.external_storage),
                            getString(R.string.no_external_storage));
                }

                int usedRam = KRAMStats.getInstance().getTotal() - KRAMStats.getInstance().getFree();
                usedRam /= 1024;
                int totalRam = KRAMStats.getInstance().getTotal() / 1024;
                int activeRam = KRAMStats.getInstance().getActive() / 1024;

                StatsListModel memoryUsed = new StatsListModel(getString(R.string.memory_used),
                        usedRam + "MB / " + totalRam + "MB" +
                        " ( " + KRAMStats.getInstance().getPercentUsed() + "% )");
                StatsListModel memoryActive = new StatsListModel(getString(R.string.memory_active),
                        activeRam + "MB / " + totalRam + "MB" +
                                " ( " + KRAMStats.getInstance().getPercentActive() + "% )");


                data.clear();
                data.add(internal);
                data.add(external);
                data.add(memoryUsed);
                data.add(memoryActive);
                adapter = new StatsListAdapter(activity, data, activity.getResources());
                list.setAdapter(adapter);
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }
}
