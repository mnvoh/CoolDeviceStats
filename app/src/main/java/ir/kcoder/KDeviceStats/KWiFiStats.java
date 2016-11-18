package ir.kcoder.KDeviceStats;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by mnvoh on 3/19/15.
 * MAC: 88:32:9b:4d:df:39, Supplicant state: COMPLETED, RSSI: -200, Link speed: -1, Net ID: -1, Metered hint: false, mFrequency: -1
 * MAC: 88:32:9b:4d:df:39, Supplicant state: COMPLETED, RSSI: -54, Link speed: 72, Net ID: 1, Metered hint: false, mFrequency: 2417
 */
public class KWiFiStats {
    /** The acquired WifiManager instance; */
    private WifiManager wifiManager;

    /** Wifi connection state. true: connected, false disconnected. */
    private boolean connectionState = false;

    /** network ssid */
    private WifiInfo wifiInfo;

    /** The starting point of wifi channels in MHz . */
    private final double WIFI_CHANNELS_SPECTRUM_START = 2409.5;

    private static Context context;
    private static KWiFiStats wiFiStats;

    public static KWiFiStats getInstance(Context c) {
        if(context == null) {
            context = c;
        }
        if(wiFiStats == null) {
            wiFiStats = new KWiFiStats();
        }
        return wiFiStats;
    }

    protected KWiFiStats() {
        wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        refreshStats();
    }

    /**
     * Refreshes the stats of the connected wifi network  and sets <br>
     * connection state to true or sets the connection state to false <br>
     * if there aren't any active connection.
     */
    public void refreshStats() {
        wifiInfo = wifiManager.getConnectionInfo();
        if(wifiInfo.getNetworkId() > 0) {
            // We have an active wifi connection
            connectionState = true;
        }
        else {
            // no active wifi connection at the moment
            connectionState = false;
        }
    }

    /**
     * The wifi connection state.
     * @return true if there's an active wifi connection. false otherwise.
     */
    public boolean getConnectionState() {
        return connectionState;
    }

    /**
     * Gets the SSID (visible name when searching) of the access point <br>
     * which the device is connected to.
     * @return The SSID of the associated access point.
     */
    public String getSSID() {
        if(wifiInfo != null && connectionState) {
            return wifiInfo.getSSID();
        }
        return null;
    }

    /**
     * Returns the signal strength of the wifi connection.
     * @return The signal strength in dBm ( <0 ).
     */
    public int getRssi() {
        if(wifiInfo != null && connectionState) {
            return wifiInfo.getRssi();
        }
        return -1000;
    }

    /**
     * Returns the signal strength of the wifi connection from a scale of <br>
     * 1 to 5.
     * @return WiFi signal strength from a scale of 1 to 5.
     */
    public int getSignalStrength() {
        return getSignalStrength(5);
    }

    /**
     * Returns the signal strength of the wifi connectinon from a scale of 1 to <b><i>scale</i></b>.
     * @param scale The scale of the connection strength.
     * @return WiFi signal strength from a scale of 1 to <b><i>scale</i></b> or -1 if <br>
     *     there isn't any active connection.
     */
    public int getSignalStrength(int scale) {
        if(wifiInfo == null || connectionState == false) {
            return -1;
        }
        int rssi = wifiInfo.getRssi();
        if(rssi >= -60)
            return scale;
        else if(rssi <= -90)
            return 0;

        return Math.round(((90 - Math.abs(rssi)) / 30.0f) * (float)scale);
    }

    /**
     * Gets the frequency of the access point.
     * @require API Level >= 21
     * @return The wifi frequency in {@link WifiInfo#FREQUENCY_UNITS} unit <br>
     *     or null if the api doesn't support {@link android.net.wifi.WifiInfo#getFrequency()} <br>
     *     or there is no active connection.
     */
    public String getFrequency() {
        try {
            return wifiInfo.getFrequency() + " " + WifiInfo.FREQUENCY_UNITS;
        } catch(NoSuchMethodError ex) {
            return null;
        }
    }

    /**
     * Gets the channel of the WiFi access point.
     * @return a channel number between 1 and 14 or 0 if the api doesn't <br>
     * support {@link android.net.wifi.WifiInfo#getFrequency()} or there is no active connection.
     */
    public int getChannel() {
        try {
            double frequency = (double)wifiInfo.getFrequency();
            if(frequency > 2474.5)
                return 14;
            return (int)((frequency - WIFI_CHANNELS_SPECTRUM_START) / 5) + 1;
        } catch(NoSuchMethodError ex) {
            return 0;
        }
    }

    /**
     * Gets the link speed of the active wifi connection in <br>
     *     {@link android.net.wifi.WifiInfo#LINK_SPEED_UNITS}
     * @return The link speed or null if there is no active connection.
     */
    public String getLinkSpeed() {
        if(wifiInfo == null || connectionState == false) { return null; }
        return wifiInfo.getLinkSpeed() + " " + WifiInfo.LINK_SPEED_UNITS;
    }

    /**
     * Gets the MAC address of the access point otherwise known as the BSSID.
     * @return The MAC address of the access point or null if there is no <br>
     *     active connection.
     */
    public String getAPMacAddress() {
        if(wifiInfo == null || connectionState == false) { return null; }
        return wifiInfo.getBSSID().toUpperCase();
    }

    /**
     * Gets the device's MAC Address
     * @return the MAC address of the device or null if unavailable.
     */
    public String getMacAddress() {
        if(wifiInfo == null)
            return null;
        String mac = wifiInfo.getMacAddress();
        return (mac != null) ? mac.toUpperCase() : null;
    }

    /**
     * Gets the IP address of the device
     * @return The ip address of the device or null if there is no active connection.
     */
    public String getIpAddress() {
        if(wifiInfo == null || connectionState == false) { return null; }
        int ip = wifiInfo.getIpAddress();
        return Integer.toString((ip) & 0xff) + "." +
                Integer.toString((ip >>> 8) & 0xff) + "." +
                Integer.toString((ip >>> 16) & 0xff) + "." +
                Integer.toString((ip >>> 24) & 0xff);
    }
}
