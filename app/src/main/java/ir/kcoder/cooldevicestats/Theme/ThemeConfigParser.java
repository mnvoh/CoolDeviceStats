package ir.kcoder.cooldevicestats.Theme;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by mnvoh on 4/23/15.
 * 
 */
public class ThemeConfigParser {
    public static final String KEY_DIGIT_WIDTH = "digit-width";
    public static final String KEY_DIGIT_HEIGHT = "digit-height";
    public static final String KEY_DIGIT_0_X = "digit-0-x";
    public static final String KEY_DIGIT_0_Y = "digit-0-y";
    public static final String KEY_DIGIT_1_X = "digit-1-x";
    public static final String KEY_DIGIT_1_Y = "digit-1-y";
    public static final String KEY_DIGIT_2_X = "digit-2-x";
    public static final String KEY_DIGIT_2_Y = "digit-2-y";
    public static final String KEY_DIGIT_3_X = "digit-3-x";
    public static final String KEY_DIGIT_3_Y = "digit-3-y";
    public static final String KEY_DIGIT_4_X = "digit-4-x";
    public static final String KEY_DIGIT_4_Y = "digit-4-y";
    public static final String KEY_DIGIT_5_X = "digit-5-x";
    public static final String KEY_DIGIT_5_Y = "digit-5-y";
    public static final String KEY_DIGIT_6_X = "digit-6-x";
    public static final String KEY_DIGIT_6_Y = "digit-6-y";
    public static final String KEY_DIGIT_7_X = "digit-7-x";
    public static final String KEY_DIGIT_7_Y = "digit-7-y";
    public static final String KEY_DIGIT_8_X = "digit-8-x";
    public static final String KEY_DIGIT_8_Y = "digit-8-y";
    public static final String KEY_DIGIT_9_X = "digit-9-x";
    public static final String KEY_DIGIT_9_Y = "digit-9-y";
    public static final String KEY_CLOCK_SEP_X = "clock-sep-x";
    public static final String KEY_CLOCK_SEP_Y = "clock-sep-y";
    public static final String KEY_CLOCK_SEP_WIDTH = "clock-sep-width";
    public static final String KEY_CLOCK_SEP_HEIGHT = "clock-sep-height";
    public static final String KEY_CLOCK_BG_X = "clock-bg-x";
    public static final String KEY_CLOCK_BG_Y = "clock-bg-y";
    public static final String KEY_CLOCK_BG_WIDTH = "clock-bg-width";
    public static final String KEY_CLOCK_BG_HEIGHT = "clock-bg-height";
    public static final String KEY_WIFI_WIDTH = "wifi-width";
    public static final String KEY_WIFI_HEIGHT = "wifi-height";
    public static final String KEY_WIFI_4_X = "wifi-4-x";
    public static final String KEY_WIFI_4_Y = "wifi-4-y";
    public static final String KEY_WIFI_3_X = "wifi-3-x";
    public static final String KEY_WIFI_3_Y = "wifi-3-y";
    public static final String KEY_WIFI_2_X = "wifi-2-x";
    public static final String KEY_WIFI_2_Y = "wifi-2-y";
    public static final String KEY_WIFI_1_X = "wifi-1-x";
    public static final String KEY_WIFI_1_Y = "wifi-1-y";
    public static final String KEY_WIFI_0_X = "wifi-0-x";
    public static final String KEY_WIFI_0_Y = "wifi-0-y";
    public static final String KEY_SIGNAL_WIDTH = "signal-width";
    public static final String KEY_SIGNAL_HEIGHT = "signal-height";
    public static final String KEY_SIGNAL_4_X = "signal-4-x";
    public static final String KEY_SIGNAL_4_Y = "signal-4-y";
    public static final String KEY_SIGNAL_3_X = "signal-3-x";
    public static final String KEY_SIGNAL_3_Y = "signal-3-y";
    public static final String KEY_SIGNAL_2_X = "signal-2-x";
    public static final String KEY_SIGNAL_2_Y = "signal-2-y";
    public static final String KEY_SIGNAL_1_X = "signal-1-x";
    public static final String KEY_SIGNAL_1_Y = "signal-1-y";
    public static final String KEY_SIGNAL_0_X = "signal-0-x";
    public static final String KEY_SIGNAL_0_Y = "signal-0-y";
    public static final String KEY_PROGRESS_WIDTH = "progress-width";
    public static final String KEY_PROGRESS_HEIGHT = "progress-height";
    public static final String KEY_INTERNAL_STORAGE_X = "internal-storage-x";
    public static final String KEY_INTERNAL_STORAGE_Y = "internal-storage-y";
    public static final String KEY_EXTERNAL_STORAGE_X = "external-storage-x";
    public static final String KEY_EXTERNAL_STORAGE_Y = "external-storage-y";
    public static final String KEY_MEMORY_X = "memory-x";
    public static final String KEY_MEMORY_Y = "memory-y";
    public static final String KEY_CPU_X = "cpu-x";
    public static final String KEY_CPU_Y = "cpu-y";
    public static final String KEY_PROGRESS_TYPE = "progress-type";
    public static final String KEY_GAUGE_RANGE_DEGREE = "gauge-range-degree";
    public static final String KEY_PROGRESS_FILL_X = "progress-fill-x";
    public static final String KEY_PROGRESS_FILL_Y = "progress-fill-y";
    public static final String KEY_BLUETOOTH_WIDTH = "bluetooth-width";
    public static final String KEY_BLUETOOTH_HEIGHT = "bluetooth-height";
    public static final String KEY_BLUETOOTH_ON_X = "bluetooth-on-x";
    public static final String KEY_BLUETOOTH_ON_Y = "bluetooth-on-y";
    public static final String KEY_BLUETOOTH_OFF_X = "bluetooth-off-x";
    public static final String KEY_BLUETOOTH_OFF_Y = "bluetooth-off-y";
    public static final String KEY_REFRESH_BUTTON_WIDTH = "refresh-button-width";
    public static final String KEY_REFRESH_BUTTON_HEIGHT = "refresh-button-height";
    public static final String KEY_REFRESH_BUTTON_X = "refresh-button-x";
    public static final String KEY_REFRESH_BUTTON_Y = "refresh-button-y";
    public static final String KEY_LAUNCH_BUTTON_WIDTH = "launch-button-width";
    public static final String KEY_LAUNCH_BUTTON_HEIGHT = "launch-button-height";
    public static final String KEY_LAUNCH_BUTTON_X = "launch-button-x";
    public static final String KEY_LAUNCH_BUTTON_Y = "launch-button-y";
    public static final String KEY_THERMOMETER_WIDTH = "thermometer-width";
    public static final String KEY_THERMOMETER_HEIGHT = "thermometer-height";
    public static final String KEY_THERMOMETER_BORDER_X = "thermometer-border-x";
    public static final String KEY_THERMOMETER_BORDER_Y = "thermometer-border-y";
    public static final String KEY_THERMOMETER_NORMAL_FILL_X = "thermometer-normal-fill-x";
    public static final String KEY_THERMOMETER_NORMAL_FILL_Y = "thermometer-normal-fill-y";
    public static final String KEY_THERMOMETER_CRITICAL_FILL_X = "thermometer-critical-fill-x";
    public static final String KEY_THERMOMETER_CRITICAL_FILL_Y = "thermometer-critical-fill-y";
    public static final String KEY_THERMOMETER_ZERO_POSITION = "thermometer-zero-position";
    public static final String KEY_THERMOMETER_BASE_POSITION = "thermometer-base-position";
    public static final String KEY_BATTERY_WIDTH = "battery-width";
    public static final String KEY_BATTERY_HEIGHT = "battery-height";
    public static final String KEY_BATTERY_BORDER_X = "battery-border-x";
    public static final String KEY_BATTERY_BORDER_Y = "battery-border-y";
    public static final String KEY_BATTERY_5_X = "battery-5-x";
    public static final String KEY_BATTERY_5_Y = "battery-5-y";
    public static final String KEY_BATTERY_4_X = "battery-4-x";
    public static final String KEY_BATTERY_4_Y = "battery-4-y";
    public static final String KEY_BATTERY_3_X = "battery-3-x";
    public static final String KEY_BATTERY_3_Y = "battery-3-y";
    public static final String KEY_BATTERY_2_X = "battery-2-x";
    public static final String KEY_BATTERY_2_Y = "battery-2-y";
    public static final String KEY_BATTERY_1_X = "battery-1-x";
    public static final String KEY_BATTERY_1_Y = "battery-1-y";

    public static final int PROGTYPE_PIE = 0;
    public static final int PROGTYPE_GAUGE = 1;


    private String configPath;
    private HashMap<String, Double> conf;

    private static ThemeConfigParser instance;



    public static ThemeConfigParser getInstance(String configPath) {
        if(instance == null) {
            instance = new ThemeConfigParser(configPath);
        }
        return instance;
    }

    public static ThemeConfigParser getInstance(String configPath, boolean forceNewInstance) {
        if(instance == null || forceNewInstance) {
            instance = new ThemeConfigParser(configPath);
        }
        return instance;
    }

    private ThemeConfigParser(String configPath) {
        this.configPath = configPath;
        parseThemeConf(false);
    }

    public double getDoubleValue(String key) {
        if(conf.containsKey(key)) {
            return conf.get(key).doubleValue();
        }
        return 0;
    }

    public int getIntValue(String key) {
        if(conf.containsKey(key)) {
            return conf.get(key).intValue();
        }
        return 0;
    }

    private void parseThemeConf(boolean themeStructureRebuilt) {
        conf = new HashMap<>();
        try {
            FileInputStream is = new FileInputStream(configPath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while((line = reader.readLine()) != null) {
                line = line.replaceAll(" ", "");
                line = line.replaceAll("\t", "");
                if(line.length() <= 0) { continue; }
                if(line.charAt(0) == '#') { continue; }
                if(line.charAt(0) == '[') { continue; }
                String[] params = line.split(":");
                conf.put(params[0], Double.parseDouble(params[1]));
            }
            reader.close();
            is.close();
        }
        catch(FileNotFoundException fnfex) {
            fnfex.printStackTrace();
        }
        catch(IOException ioex) {
            ioex.printStackTrace();
        }
        catch(NumberFormatException nfex) {
            nfex.printStackTrace();
        }
    }
}