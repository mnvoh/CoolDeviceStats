package ir.kcoder.cooldevicestats;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import java.io.File;

/**
 * Created by mnvoh on 3/22/15.
 *
 */
public class CDSPrefsManager {
    public static final String PREFERENCES_NAME = "ir.kcoder.cooldevicestats";

    public static final String PREF_DEFAULT_CALENDAR = "default_calendar";
    public static final int CALENDAR_GREGORIAN = 0;
    public static final int CALENDAR_SHAMSI = 1;

    public static final String PREF_DEFAULT_UPDATE_INTERVAL = "default_update_interval";
    public static final int DEFAULT_UPDATE_INTERVAL = 60 * 30; //30 minutes

    public static final String PREF_ACTIVE_THEME = "active_theme";
    public static final String DEFAULT_ACTIVE_THEME = "Flat";

    public static final String PREF_WIDGET_BG = "widget_background_color";
    public static final int DEFAULT_WIDGET_BG = 0x00ffffff;

    private SharedPreferences prefs;
    private Context context;
    public CDSPrefsManager(Context context) {
        this.context = context;
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context); //context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void setDefaultCalendar(int value) {
        SharedPreferences.Editor editor = prefs.edit();
        if(value != CALENDAR_GREGORIAN && value != CALENDAR_SHAMSI) {
            value = CALENDAR_SHAMSI;
        }
        editor.putInt(PREF_DEFAULT_CALENDAR, value);
        editor.commit();
    }
    public int getDefaultCalendar() {
        return prefs.getInt(PREF_DEFAULT_CALENDAR, CALENDAR_SHAMSI);
    }

    /**
     * Returns the minimum value for update interval in seconds. For <br>
     * API >= LOLLIPOP(And5.1) it returns 60, since the alarm manager ignores <br>
     * smaller values and rounds it up to that amount. For other API levels <br>
     * it returns 10 seconds;
     * @return Minimum amount for update interval in seconds.
     */
    public int getMinUpdateInterval() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return 60;
        }
        return 60;
    }
    public void setUpdateInterval(int seconds) {
        SharedPreferences.Editor editor = prefs.edit();
        int min = getMinUpdateInterval();
        if(seconds < min) {
            seconds = min;
        }
        editor.putInt(PREF_DEFAULT_UPDATE_INTERVAL, seconds);
        editor.commit();
    }
    public int getUpdateInterval() {
        return prefs.getInt(PREF_DEFAULT_UPDATE_INTERVAL, DEFAULT_UPDATE_INTERVAL);
    }

    public void setActiveTheme(String themeName) {
        File themeDir = new File(context.getExternalFilesDir(null).getParent() + "/themes/" +
                themeName);
        if(!themeDir.exists()) {
            themeName = DEFAULT_ACTIVE_THEME;
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREF_ACTIVE_THEME, themeName);
        editor.commit();
    }
    public String getActiveTheme() {
        return prefs.getString(PREF_ACTIVE_THEME, DEFAULT_ACTIVE_THEME);
    }

    public void setWidgetBg(int color) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(PREF_WIDGET_BG, color);
        editor.commit();
    }
    public int getWidgetBg() {
        return prefs.getInt(PREF_WIDGET_BG, DEFAULT_WIDGET_BG);
    }
}