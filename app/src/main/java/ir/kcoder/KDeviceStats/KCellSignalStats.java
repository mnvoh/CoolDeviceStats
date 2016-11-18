package ir.kcoder.KDeviceStats;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import ir.kcoder.cooldevicestats.CDSWidgetProvider;
import ir.kcoder.cooldevicestats.R;

/**
 * Created by mnvoh on 4/3/15.
 *
 */
public class KCellSignalStats {
    public static KCellSignalStats signalStats;
    private TelephonyManager telephonyManager;
    private myPhoneStateListener pslistener;
    private static Context context;
    private static int dBm = 0;
    private static String operatorName;

    public static KCellSignalStats getInstance(Context c) {
        if(context == null) {
            context = c;
        }
        if(signalStats == null) {
            signalStats = new KCellSignalStats(context);
        }
        return signalStats;
    }

    public KCellSignalStats(Context c) {
        context = c;
        try {
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            tm.listen(new myPhoneStateListener(), PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getdBm() {
        if(isAirplaneModeOn()) {
            return -200;
        }
        return dBm;
    }

    public int getBars(int maxBarsCount) {
        int bestDBM = -87;
        int worstDBM = -112;
        int scale = Math.abs(Math.abs(worstDBM) - Math.abs(bestDBM));
        int value = Math.abs(worstDBM) - Math.abs(dBm);
        float bars = (value / (float)scale) * maxBarsCount;
        if(bars > maxBarsCount) {
            return maxBarsCount;
        }
        else if(bars < 0) {
            return 0;
        }
        return Math.round(bars);
    }

    public String getOperatorName() {
        try {
            if (isAirplaneModeOn()) {
                return context.getString(R.string.unknown);
            }
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getSimOperatorName();
        }
        catch(Exception ex) {
            return context.getString(R.string.unknown);
        }
    }

    public int getLocationAreaCode() {
        try {
            if (isAirplaneModeOn()) {
                return -1;
            }
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            GsmCellLocation loc = (GsmCellLocation) tm.getCellLocation();
            return loc.getLac();
        }
        catch(Exception ex) {
            return -1;
        }
    }

    public int getCellTowerId() {
        try {
            if(isAirplaneModeOn()) {
                return -1;
            }
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            GsmCellLocation loc = (GsmCellLocation)tm.getCellLocation();
            return loc.getCid();
        }
        catch(Exception ex) {
            return -1;
        }
    }


    public class myPhoneStateListener extends PhoneStateListener {
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            int s = signalStrength.getGsmSignalStrength();
            dBm = (2 * s) - 113;
            Intent autoUpdateWidget = new Intent(CDSWidgetProvider.ACTION_AUTO_UPDATE);
            context.sendBroadcast(autoUpdateWidget);
        }
    }

    /**
     * Gets the state of Airplane Mode.
     *
     * @return true if enabled.
     */
    @SuppressWarnings("deprecation")
    public static boolean isAirplaneModeOn() {
        if(context == null) { return true; }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        } else {
            return Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }
}
