package ir.kcoder.cooldevicestats;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import ir.kcoder.KDeviceStats.KWiFiStats;


public class WifiStatsActivity extends ActionBarActivity {
    private ListView list;
    private StatsListAdapter adapter;
    private ArrayList<StatsListModel> data = new ArrayList<>();
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_stats);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.wifi);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        list = (ListView)findViewById(R.id.lstWifiStats);
        activity = this;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                data.clear();
                KWiFiStats.getInstance(activity).refreshStats();
                if(KWiFiStats.getInstance(activity).getConnectionState()) {
                    StatsListModel name = new StatsListModel(getString(R.string.network_name),
                            KWiFiStats.getInstance(activity).getSSID());
                    String _strength = KWiFiStats.getInstance(activity).getRssi() + " dBm " +
                            " [" + KWiFiStats.getInstance(activity).getSignalStrength(5) + " / 5]";
                    StatsListModel strength = new StatsListModel(getString(R.string.signal_strength)
                            , _strength);
                    StatsListModel linkSpeed = new StatsListModel(getString(R.string.link_speed)
                            , KWiFiStats.getInstance(activity).getLinkSpeed());
                    StatsListModel apMac = new StatsListModel(getString(R.string.ap_mac_address)
                            , KWiFiStats.getInstance(activity).getAPMacAddress());
                    StatsListModel mac = new StatsListModel(getString(R.string.mac_address)
                            , KWiFiStats.getInstance(activity).getMacAddress());
                    StatsListModel ip = new StatsListModel(getString(R.string.ip_address)
                            , KWiFiStats.getInstance(activity).getIpAddress());
                    data.add(new StatsListModel(getString(R.string.status), getString(R.string.connected)));
                    data.add(name);
                    data.add(strength);
                    data.add(linkSpeed);
                    data.add(apMac);
                    data.add(mac);
                    data.add(ip);
                }
                else {
                    data.add(new StatsListModel(getString(R.string.status), getString(R.string.disconnected)));
                }

                adapter = new StatsListAdapter(activity, data, activity.getResources());
                list.setAdapter(adapter);
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }
}
