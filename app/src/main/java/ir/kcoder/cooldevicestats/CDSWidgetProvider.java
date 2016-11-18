package ir.kcoder.cooldevicestats;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import ir.kcoder.KDeviceStats.KBatteryStats;
import ir.kcoder.KDeviceStats.KCPUStats;
import ir.kcoder.KDeviceStats.KCellSignalStats;
import ir.kcoder.KDeviceStats.KRAMStats;
import ir.kcoder.KDeviceStats.KStorageStats;
import ir.kcoder.KDeviceStats.KWiFiStats;
import ir.kcoder.cooldevicestats.Theme.ThemeArchive;
import ir.kcoder.cooldevicestats.Theme.ThemeProvider;

/**
 * Implementation of App Widget functionality.
 */
public class CDSWidgetProvider extends AppWidgetProvider {
    public static final String ACTION_AUTO_UPDATE = "ir.kcoder.action.AUTO_UPDATE";
    public static final String ACTION_MANUAL_UPDATE = "ir.kcoder.action.MANUAL_UPDATE";
    private static ThemeProvider themeProvider;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        Log.i("CDSWidgetProvider", "CDSWidgetProvider->onUpdate");

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.cdswidget);

            setEventHandlers(views, context);

            // Tell the AppWidgetManager to perform an update on the current app widget

            updateImageViews(views, context);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onEnabled(Context context) {
        Log.i("CDSWidgetProvider", "CDSWidgetProvider->onEnabled");
        // start alarm
        AppWidgetAlarm appWidgetAlarm = new AppWidgetAlarm(context.getApplicationContext(), this);
        appWidgetAlarm.startAlarm();
        ThemeArchive themeArchive = new ThemeArchive(context);
        themeArchive.unpackDefaultThemes();
    }

    @Override
    public void onDisabled(Context context) {
        Log.i("CDSWidgetProvider", "CDSWidgetProvider->onDisabled");
        // Enter relevant functionality for when the last widget is disabled
        AppWidgetAlarm appWidgetAlarm = new AppWidgetAlarm(context.getApplicationContext(), this);
        appWidgetAlarm.stopAlarm();
        KBatteryStats.getInstance(context).dispose();
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);

        if(intent.getAction().equals(ACTION_AUTO_UPDATE) ||
            intent.getAction().equals(ACTION_MANUAL_UPDATE))
        {
            CDSPrefsManager prefsManager = new CDSPrefsManager(context);
            KBatteryStats.getInstance(context).refreshStats();
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.cdswidget);
            remoteViews.setInt(R.id.cdsWidget, "setBackgroundColor", prefsManager.getWidgetBg());
            updateImageViews(remoteViews, context);
            ComponentName componentName = new ComponentName(context, CDSWidgetProvider.class);
            appWidgetManager.updateAppWidget(componentName, remoteViews);
        }

        //if (intent.getAction().equals(ACTION_AUTO_UPDATE)) {
        //    CDSPrefsManager prefsManager = new CDSPrefsManager(context);
        //    KBatteryStats.getInstance(context).refreshStats();
        //    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        //    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.cdswidget);
        //    remoteViews.setInt(R.id.cdsWidget, "setBackgroundColor", prefsManager.getWidgetBg());
        //    updateImageViews(remoteViews, context);
        //    ComponentName componentName = new ComponentName(context, CDSWidgetProvider.class);
        //    appWidgetManager.updateAppWidget(componentName, remoteViews);
        //}
        //else if (intent.getAction().equals(ACTION_MANUAL_UPDATE)) {
        //    CDSPrefsManager prefsManager = new CDSPrefsManager(context);
        //    KBatteryStats.getInstance(context).refreshStats();
        //    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        //    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.cdswidget);
        //    remoteViews.setInt(R.id.cdsWidget, "setBackgroundColor", prefsManager.getWidgetBg());
        //    updateImageViews(remoteViews, context);
        //    ComponentName componentName = new ComponentName(context, CDSWidgetProvider.class);
        //    appWidgetManager.updateAppWidget(componentName, remoteViews);
        //}
        //else if(intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
        //    CDSPrefsManager prefsManager = new CDSPrefsManager(context);
        //    KBatteryStats.getInstance(context).refreshStats();
        //    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        //    RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.cdswidget);
        //    remoteViews.setInt(R.id.cdsWidget, "setBackgroundColor", prefsManager.getWidgetBg());
        //    updateImageViews(remoteViews, context);
        //    ComponentName componentName = new ComponentName(context, CDSWidgetProvider.class);
        //    appWidgetManager.updateAppWidget(componentName, remoteViews);
        //}
    }

    private void updateImageViews(RemoteViews remoteViews, Context context) {
        if(themeProvider == null) {
            themeProvider = new ThemeProvider(context);
        }

        /** ---------------------------------------------------- **/
        Bitmap clock = themeProvider.getClock();
        remoteViews.setImageViewBitmap(R.id.imvClock, clock);

        /** ---------------------------------------------------- **/
        KWiFiStats wiFiStats = KWiFiStats.getInstance(context);
        boolean wifiState = (wiFiStats != null) ? wiFiStats.getConnectionState() : false;
        int wifiStrength = -1;
        if(wifiState) {
            wifiStrength = KWiFiStats.getInstance(context).getSignalStrength(4);
        }
        Bitmap wifi = themeProvider.getWifi(wifiStrength);
        remoteViews.setImageViewBitmap(R.id.imvWifi, wifi);

        /** ---------------------------------------------------- **/
        KBatteryStats batteryStats = KBatteryStats.getInstance(context);
        int temperature = (batteryStats != null) ? batteryStats.getTemperature() : 0;
        Bitmap thermo = themeProvider.getThermometer(temperature);
        remoteViews.setImageViewBitmap(R.id.imvThermo, thermo);


        /** ---------------------------------------------------- **/
        int batteryPct = (batteryStats != null) ? batteryStats.getBatteryPct() : 0;
        Bitmap battery = themeProvider.getBattery(batteryPct);
        remoteViews.setImageViewBitmap(R.id.imvBattery, battery);


        /** ---------------------------------------------------- **/
        KCellSignalStats signalStats = KCellSignalStats.getInstance(context);
        int signalStrength = (signalStats != null) ? signalStats.getBars(5) : 0;
        Bitmap signal = themeProvider.getSignal(signalStrength);
        remoteViews.setImageViewBitmap(R.id.imvSignal, signal);

        /** ---------------------------------------------------- **/
        KStorageStats storageStats = KStorageStats.getInstance();
        int internalPercent = (storageStats != null) ? storageStats.getInternalPercentage() : 0;
        Bitmap internal = themeProvider.getInternalStorage(internalPercent);
        remoteViews.setImageViewBitmap(R.id.imvInternal, internal);


        /** ---------------------------------------------------- **/
        int externalPercent = (storageStats != null) ? storageStats.getExternalPercentage() : 0;
        Bitmap external = themeProvider.getExternalStorage(externalPercent);
        remoteViews.setImageViewBitmap(R.id.imvExternal, external);


        /** ---------------------------------------------------- **/
        KRAMStats kramStats = KRAMStats.getInstance();
        int memoryPercent = (kramStats != null) ? kramStats.getPercentUsed() : 0;
        Bitmap memory = themeProvider.getMemory(memoryPercent);
        remoteViews.setImageViewBitmap(R.id.imvMemory, memory);


        /** ---------------------------------------------------- **/
        KCPUStats cpuStats = KCPUStats.getInstance();
        int cpuPercent;
        if(cpuStats != null) {
            cpuStats.refreshStats();
            cpuPercent = cpuStats.calculateCpuUsage(0);
        }
        else {
            cpuPercent = 0;
        }
        Bitmap cpu = themeProvider.getCpu(cpuPercent);
        remoteViews.setImageViewBitmap(R.id.imvCpu, cpu);

        /** ---------------------------------------------------- **/
        remoteViews.setImageViewBitmap(R.id.imvRefreshWidget, themeProvider.getRefreshButton());
        remoteViews.setImageViewBitmap(R.id.imvLaunchApp, themeProvider.getLaunchButton());
    }

    private void setEventHandlers(RemoteViews views, Context context) {
        CDSPrefsManager prefsManager = new CDSPrefsManager(context);

        Intent updateIntent = new Intent(context, CDSWidgetProvider.class);
        updateIntent.setAction(ACTION_MANUAL_UPDATE);
        PendingIntent update_pintent = PendingIntent.getBroadcast(context, 0, updateIntent, 0);

        Intent batteryIntent = new Intent(context, BatteryStatsActivity.class);
        PendingIntent batteryPIntent = PendingIntent.getActivity(context, 0, batteryIntent, 0);

        Intent wifiIntent = new Intent(context, WifiStatsActivity.class);
        PendingIntent wifiPIntent = PendingIntent.getActivity(context, 0, wifiIntent, 0);

        Intent signalIntent = new Intent(context, CellularStatsActivity.class);
        PendingIntent signalPIntent = PendingIntent.getActivity(context, 0, signalIntent, 0);

        Intent storageIntent = new Intent(context, MemoryStatusActivity.class);
        PendingIntent storagePIntent = PendingIntent.getActivity(context, 0, storageIntent, 0);

        Intent cpuIntent = new Intent(context, CpuStatsActivity.class);
        PendingIntent cpuPIntent = PendingIntent.getActivity(context, 0, cpuIntent, 0);

        Intent mainAppIntent = new Intent(context, MainActivity.class);
        PendingIntent mainAppPIntent = PendingIntent.getActivity(context, 0, mainAppIntent, 0);

        views.setInt(R.id.cdsWidget, "setBackgroundColor", prefsManager.getWidgetBg());
        views.setOnClickPendingIntent(R.id.imvBattery, batteryPIntent);
        views.setOnClickPendingIntent(R.id.imvThermo, batteryPIntent);
        views.setOnClickPendingIntent(R.id.imvWifi, wifiPIntent);
        views.setOnClickPendingIntent(R.id.imvSignal, signalPIntent);
        views.setOnClickPendingIntent(R.id.imvInternal, storagePIntent);
        views.setOnClickPendingIntent(R.id.imvExternal, storagePIntent);
        views.setOnClickPendingIntent(R.id.imvMemory, storagePIntent);
        views.setOnClickPendingIntent(R.id.imvCpu, cpuPIntent);
        views.setOnClickPendingIntent(R.id.imvRefreshWidget, update_pintent);
        views.setOnClickPendingIntent(R.id.imvLaunchApp, mainAppPIntent);

        // Create an intent for launching the default clock
        PackageManager packageManager = context.getPackageManager();
        Intent alarmClockIntent = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER);

        // Verify clock implementation
        String clockImpls[][] = {
                {"HTC Alarm Clock", "com.htc.android.worldclock", "com.htc.android.worldclock.WorldClockTabControl" },
                {"Standar Alarm Clock", "com.android.deskclock", "com.android.deskclock.AlarmClock"},
                {"Froyo Nexus Alarm Clock", "com.google.android.deskclock", "com.android.deskclock.DeskClock"},
                {"Moto Blur Alarm Clock", "com.motorola.blur.alarmclock",  "com.motorola.blur.alarmclock.AlarmClock"},
                {"Samsung Galaxy Clock", "com.sec.android.app.clockpackage","com.sec.android.app.clockpackage.ClockPackage"} ,
                {"Sony Ericsson Xperia Z", "com.sonyericsson.organizer", "com.sonyericsson.organizer.Organizer_WorldClock" }

        };

        boolean foundClockImpl = false;

        for(int j=0; j < clockImpls.length; j++) {
            String vendor = clockImpls[j][0];
            String packageName = clockImpls[j][1];
            String className = clockImpls[j][2];
            try {
                ComponentName cn = new ComponentName(packageName, className);
                ActivityInfo aInfo = packageManager.getActivityInfo(cn, PackageManager.GET_META_DATA);
                alarmClockIntent.setComponent(cn);
                foundClockImpl = true;
                break;
            } catch (Exception e) {

            }
        }

        if (foundClockImpl) {
            PendingIntent clockPendingIntent = PendingIntent.getActivity(context, 0, alarmClockIntent, 0);
            views.setOnClickPendingIntent(R.id.imvClock, clockPendingIntent);
        }
    }
}


