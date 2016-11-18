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
import ir.kcoder.KDeviceStats.KCellSignalStats;


public class CellularStatsActivity extends ActionBarActivity {
    private StatsListAdapter adapter;
    private ArrayList data;
    private ListView list;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cellular_stats);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.cell);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        adapter = new StatsListAdapter(this, data, this.getResources());
        data = new ArrayList<>();
        list = (ListView)findViewById(R.id.lstCellularStats);
        activity = this;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int iDbm = KCellSignalStats.getInstance(activity).getdBm();
                StatsListModel dbm;
                if(iDbm > -200) {
                    dbm = new StatsListModel(getString(R.string.signal_strength),iDbm + " dBm");
                }
                else {
                    dbm = new StatsListModel(getString(R.string.signal_strength)
                            ,getString(R.string.offline));
                }

                StatsListModel providerName = new StatsListModel(getString(R.string.provider_name),
                        KCellSignalStats.getInstance(activity).getOperatorName());

                StatsListModel lac;
                int iLac = KCellSignalStats.getInstance(activity).getLocationAreaCode();
                if(iLac < 0) {
                    lac = new StatsListModel(getString(R.string.location_area_code)
                            , getString(R.string.unknown));
                }
                else {
                    lac = new StatsListModel(getString(R.string.location_area_code), iLac + " ");
                }

                StatsListModel cid;
                int iCid = KCellSignalStats.getInstance(activity).getCellTowerId();
                if(iCid < 0) {
                    cid = new StatsListModel(getString(R.string.cell_id), getString(R.string.unknown));
                }
                else {
                    cid = new StatsListModel(getString(R.string.cell_id), iCid + " ");
                }

                data.clear();
                data.add(dbm);
                data.add(providerName);
                data.add(lac);
                data.add(cid);
                adapter = new StatsListAdapter(activity, data, activity.getResources());
                list.setAdapter(adapter);
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }
}
