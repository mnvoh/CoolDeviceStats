package ir.kcoder.KDeviceStats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import ir.kcoder.cooldevicestats.R;

/**
 * Created by mnvoh on 3/21/15.
 *
 */
public class KBatteryStats {
    private static boolean isCharging;
    private static boolean isFull;
    private static boolean usbCharge;
    private static boolean acCharge;
    private static int batteryPct;
    private static int temperature;
    private static int voltage;

    private static PowerConnectionReceiver pcr;
    private static Context c = null;
    private static KBatteryStats batteryStatus;

    public static KBatteryStats getInstance(Context context) {
        if(c == null) {
            c = context.getApplicationContext();
        }
        if(batteryStatus == null) {
            batteryStatus = new KBatteryStats();
        }
        return batteryStatus;
    }

    public static class PowerConnectionReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;
            isFull = status == BatteryManager.BATTERY_STATUS_FULL;

            int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
            acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            batteryPct = (int) ((level / (float)scale) * 100);

            temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
            voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
        }
    }

    protected KBatteryStats() {
        if(pcr != null || c == null) {
            return;
        }
        pcr = new PowerConnectionReceiver();
        c.registerReceiver(pcr, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    public void dispose() {
        // unregister the receiver to prevent leaking
        try { if(pcr != null) c.unregisterReceiver(pcr); } catch(Exception e) {}
    }

    public void refreshStats() {
        if(c == null) {
            return;
        }
        if(pcr != null) {
            try {
                c.unregisterReceiver(pcr);
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
        pcr = new PowerConnectionReceiver();
        c.registerReceiver(pcr, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    /** Returns true if the device is in charging state */
    public boolean isCharging() {
        return isCharging;
    }

    /** returns true if the device is being charged by a USB port */
    public boolean isUsbCharge() {
        return usbCharge;
    }

    /** returns true if the battery is fully charged */
    public boolean isFull() {
        return isFull;
    }

    public String getChargingStatus() {
        if(isCharging()) {
            if(isUsbCharge()) {
                return c.getString(R.string.usb_charging);
            }
            else {
                return c.getString(R.string.ac_charging);
            }
        }
        else {
            return c.getString(R.string.discharging);
        }
    }

    /** returns the battery percentage */
    public int getBatteryPct() {
        return batteryPct;
    }

    /** returns true if the device is being charged by an AC adapter */
    public boolean isAcCharge() {
        return acCharge;
    }

    /** returns the temperature of the battery in tenths of a celsius degree */
    public int getTemperature() {
        return temperature;
    }

    /** returns the temperature in fahrenheit followed by a  &#778; and an F */
    public String getTemperatureFahrenheit() {
        return (int)(temperature / 10 * (9/5.0f) + 32) + " \u030AF";
    }

    /** returns the temperature in celsius followed by a  &#778; and a C */
    public String getTemperatureCelsius() {
        return temperature / 10 + " \u030AC";
    }

    /** returns the voltage produced by the battery in mV */
    public int getVoltage() {
        return voltage;
    }
}
