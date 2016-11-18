package ir.kcoder.cooldevicestats;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by mnvoh on 3/24/15.
 *
 */
public class AppWidgetAlarm
{
    private final int ALARM_ID = 558801;
    private int updateInterval = CDSPrefsManager.DEFAULT_UPDATE_INTERVAL;

    private Context mContext;
    private BroadcastReceiver receiver;

    public AppWidgetAlarm(Context context, BroadcastReceiver receiver) {
        mContext = context;
        updateInterval = new CDSPrefsManager(context).getUpdateInterval();
        this.receiver = receiver;
    }


    public void startAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, updateInterval);

        Intent alarmIntent = new Intent(CDSWidgetProvider.ACTION_AUTO_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, ALARM_ID, alarmIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
        // RTC does not wake the device up
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis()
                , updateInterval * 1000, pendingIntent);
    }


    public void stopAlarm() {
        Intent alarmIntent = new Intent(CDSWidgetProvider.ACTION_AUTO_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, ALARM_ID, alarmIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
