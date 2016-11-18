package ir.kcoder.cooldevicestats;

import android.app.Activity;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ir.kcoder.KCalendar.KCalendar;
import ir.kcoder.KDeviceStats.KBatteryStats;
import ir.kcoder.KDeviceStats.KCPUStats;
import ir.kcoder.KDeviceStats.KProcessStats;
import ir.kcoder.KDeviceStats.KRAMStats;
import ir.kcoder.KDeviceStats.KStorageStats;
import ir.kcoder.KDeviceStats.KWiFiStats;


public class WidgetSettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment()).commit();
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
