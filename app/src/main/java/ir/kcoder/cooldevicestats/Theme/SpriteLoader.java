package ir.kcoder.cooldevicestats.Theme;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Created by mnvoh on 4/23/15.
 *
 */
public class SpriteLoader {

    /**
     * [0] : thermometer border <br>
     * [1] : thermometer critical fill <br>
     * [2] : thermometer normal fill <br>
     */
    private Bitmap[] thermometer = new Bitmap[3];

    /**
     * [0]: battery border <br>
     * [1]: battery 5 (full)
     * [2]: battery 4
     * ...
     * [5]: battery 1 (critically low)
     */
    private Bitmap[] battery = new Bitmap[6];

    /**
     * [4]: Signal 4 ( Full )
     * [3] Signal 3
     * [2]: Signal 2
     * [1]: Signal 1
     * [0]: Signal 0 ( Empty )
     */
    private Bitmap[] signal = new Bitmap[5];

    /**
     * [0]: on
     * [1]: off
     */
    private Bitmap[] bluetooth = new Bitmap[2];

    /**
     * [0]: internal storage bg
     * [1]: external storage bg
     * [2]: memory bg
     * [3]: cpu bg
     * [4]: fill
     */
    private Bitmap[] progressbars = new Bitmap[5];

    /**
     * [0]: wifi 4 ( full )
     * [1]: wifi 3
     * [2]: wifi 2
     * [3]: wifi 1
     * [4]: wifi 0 ( empty )
     */
    private Bitmap[] wifi = new Bitmap[5];

    /**
     * [0]: digit 0
     * [1]: digit 1
     * ...
     * [9]: digit 9
     * [10]: separator
     * [11]: bg
     */
    private Bitmap[] clock = new Bitmap[12];

    private Bitmap refreshButton, launchButton;

    private static SpriteLoader instance;

    public static SpriteLoader getInstance(String spritePath, ThemeConfigParser conf) {
        if(instance == null) {
            instance = new SpriteLoader(spritePath, conf);
        }
        return instance;
    }

    public static SpriteLoader getInstance(String spritePath, ThemeConfigParser conf
            , boolean forceNewInstance) {
        if(instance == null || forceNewInstance) {
            instance = new SpriteLoader(spritePath, conf);
        }
        return instance;
    }

    private SpriteLoader(String spritePath, ThemeConfigParser conf) {
        Bitmap sprite = BitmapFactory.decodeFile(spritePath);

        thermometer[0] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_THERMOMETER_BORDER_X),
                conf.getIntValue(ThemeConfigParser.KEY_THERMOMETER_BORDER_Y),
                conf.getIntValue(ThemeConfigParser.KEY_THERMOMETER_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_THERMOMETER_HEIGHT)
        );
        thermometer[1] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_THERMOMETER_CRITICAL_FILL_X),
                conf.getIntValue(ThemeConfigParser.KEY_THERMOMETER_CRITICAL_FILL_Y),
                conf.getIntValue(ThemeConfigParser.KEY_THERMOMETER_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_THERMOMETER_HEIGHT)
        );
        thermometer[2] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_THERMOMETER_NORMAL_FILL_X),
                conf.getIntValue(ThemeConfigParser.KEY_THERMOMETER_NORMAL_FILL_Y),
                conf.getIntValue(ThemeConfigParser.KEY_THERMOMETER_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_THERMOMETER_HEIGHT)
        );

        /** ####################################################################### */

        battery[0] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_BATTERY_BORDER_X),
                conf.getIntValue(ThemeConfigParser.KEY_BATTERY_BORDER_Y),
                conf.getIntValue(ThemeConfigParser.KEY_BATTERY_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_BATTERY_HEIGHT)
        );
        battery[1] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_BATTERY_5_X),
                conf.getIntValue(ThemeConfigParser.KEY_BATTERY_5_Y),
                conf.getIntValue(ThemeConfigParser.KEY_BATTERY_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_BATTERY_HEIGHT)
        );
        battery[2] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_BATTERY_4_X),
                conf.getIntValue(ThemeConfigParser.KEY_BATTERY_4_Y),
                conf.getIntValue(ThemeConfigParser.KEY_BATTERY_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_BATTERY_HEIGHT)
        );
        battery[3] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_BATTERY_3_X),
                conf.getIntValue(ThemeConfigParser.KEY_BATTERY_3_Y),
                conf.getIntValue(ThemeConfigParser.KEY_BATTERY_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_BATTERY_HEIGHT)
        );
        battery[4] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_BATTERY_2_X),
                conf.getIntValue(ThemeConfigParser.KEY_BATTERY_2_Y),
                conf.getIntValue(ThemeConfigParser.KEY_BATTERY_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_BATTERY_HEIGHT)
        );
        battery[5] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_BATTERY_1_X),
                conf.getIntValue(ThemeConfigParser.KEY_BATTERY_1_Y),
                conf.getIntValue(ThemeConfigParser.KEY_BATTERY_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_BATTERY_HEIGHT)
        );

        /** ####################################################################### */

        signal[4] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_SIGNAL_4_X),
                conf.getIntValue(ThemeConfigParser.KEY_SIGNAL_4_Y),
                conf.getIntValue(ThemeConfigParser.KEY_SIGNAL_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_SIGNAL_HEIGHT)
        );
        signal[3] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_SIGNAL_3_X),
                conf.getIntValue(ThemeConfigParser.KEY_SIGNAL_3_Y),
                conf.getIntValue(ThemeConfigParser.KEY_SIGNAL_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_SIGNAL_HEIGHT)
        );
        signal[2] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_SIGNAL_2_X),
                conf.getIntValue(ThemeConfigParser.KEY_SIGNAL_2_Y),
                conf.getIntValue(ThemeConfigParser.KEY_SIGNAL_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_SIGNAL_HEIGHT)
        );
        signal[1] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_SIGNAL_1_X),
                conf.getIntValue(ThemeConfigParser.KEY_SIGNAL_1_Y),
                conf.getIntValue(ThemeConfigParser.KEY_SIGNAL_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_SIGNAL_HEIGHT)
        );
        signal[0] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_SIGNAL_0_X),
                conf.getIntValue(ThemeConfigParser.KEY_SIGNAL_0_Y),
                conf.getIntValue(ThemeConfigParser.KEY_SIGNAL_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_SIGNAL_HEIGHT)
        );

        /** ####################################################################### */

        bluetooth[0] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_BLUETOOTH_ON_X),
                conf.getIntValue(ThemeConfigParser.KEY_BLUETOOTH_ON_Y),
                conf.getIntValue(ThemeConfigParser.KEY_BLUETOOTH_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_BLUETOOTH_HEIGHT)
        );
        bluetooth[1] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_BLUETOOTH_OFF_X),
                conf.getIntValue(ThemeConfigParser.KEY_BLUETOOTH_OFF_Y),
                conf.getIntValue(ThemeConfigParser.KEY_BLUETOOTH_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_BLUETOOTH_HEIGHT)
        );

        /** ####################################################################### */

        progressbars[0] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_INTERNAL_STORAGE_X),
                conf.getIntValue(ThemeConfigParser.KEY_INTERNAL_STORAGE_Y),
                conf.getIntValue(ThemeConfigParser.KEY_PROGRESS_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_PROGRESS_HEIGHT)
        );
        progressbars[1] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_EXTERNAL_STORAGE_X),
                conf.getIntValue(ThemeConfigParser.KEY_EXTERNAL_STORAGE_Y),
                conf.getIntValue(ThemeConfigParser.KEY_PROGRESS_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_PROGRESS_HEIGHT)
        );
        progressbars[2] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_MEMORY_X),
                conf.getIntValue(ThemeConfigParser.KEY_MEMORY_Y),
                conf.getIntValue(ThemeConfigParser.KEY_PROGRESS_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_PROGRESS_HEIGHT)
        );
        progressbars[3] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_CPU_X),
                conf.getIntValue(ThemeConfigParser.KEY_CPU_Y),
                conf.getIntValue(ThemeConfigParser.KEY_PROGRESS_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_PROGRESS_HEIGHT)
        );
        progressbars[4] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_PROGRESS_FILL_X),
                conf.getIntValue(ThemeConfigParser.KEY_PROGRESS_FILL_Y),
                conf.getIntValue(ThemeConfigParser.KEY_PROGRESS_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_PROGRESS_HEIGHT)
        );

        /** ####################################################################### */

        wifi[4] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_WIFI_4_X),
                conf.getIntValue(ThemeConfigParser.KEY_WIFI_4_Y),
                conf.getIntValue(ThemeConfigParser.KEY_WIFI_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_WIFI_HEIGHT)
        );
        wifi[3] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_WIFI_3_X),
                conf.getIntValue(ThemeConfigParser.KEY_WIFI_3_Y),
                conf.getIntValue(ThemeConfigParser.KEY_WIFI_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_WIFI_HEIGHT)
        );
        wifi[2] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_WIFI_2_X),
                conf.getIntValue(ThemeConfigParser.KEY_WIFI_2_Y),
                conf.getIntValue(ThemeConfigParser.KEY_WIFI_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_WIFI_HEIGHT)
        );
        wifi[1] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_WIFI_1_X),
                conf.getIntValue(ThemeConfigParser.KEY_WIFI_1_Y),
                conf.getIntValue(ThemeConfigParser.KEY_WIFI_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_WIFI_HEIGHT)
        );
        wifi[0] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_WIFI_0_X),
                conf.getIntValue(ThemeConfigParser.KEY_WIFI_0_Y),
                conf.getIntValue(ThemeConfigParser.KEY_WIFI_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_WIFI_HEIGHT)
        );


        /** ####################################################################### */

        clock[0] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_0_X),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_0_Y),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_HEIGHT)
        );
        clock[1] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_1_X),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_1_Y),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_HEIGHT)
        );
        clock[2] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_2_X),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_2_Y),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_HEIGHT)
        );
        clock[3] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_3_X),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_3_Y),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_HEIGHT)
        );
        clock[4] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_4_X),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_4_Y),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_HEIGHT)
        );
        clock[5] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_5_X),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_5_Y),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_HEIGHT)
        );
        clock[6] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_6_X),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_6_Y),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_HEIGHT)
        );
        clock[7] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_7_X),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_7_Y),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_HEIGHT)
        );
        clock[8] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_8_X),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_8_Y),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_HEIGHT)
        );
        clock[9] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_9_X),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_9_Y),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_DIGIT_HEIGHT)
        );
        clock[10] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_CLOCK_SEP_X),
                conf.getIntValue(ThemeConfigParser.KEY_CLOCK_SEP_Y),
                conf.getIntValue(ThemeConfigParser.KEY_CLOCK_SEP_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_CLOCK_SEP_HEIGHT)
        );
        clock[11] = Bitmap.createBitmap(
                sprite,
                conf.getIntValue(ThemeConfigParser.KEY_CLOCK_BG_X),
                conf.getIntValue(ThemeConfigParser.KEY_CLOCK_BG_Y),
                conf.getIntValue(ThemeConfigParser.KEY_CLOCK_BG_WIDTH),
                conf.getIntValue(ThemeConfigParser.KEY_CLOCK_BG_HEIGHT)
        );

        try {

            refreshButton = Bitmap.createBitmap(
                    sprite,
                    conf.getIntValue(ThemeConfigParser.KEY_REFRESH_BUTTON_X),
                    conf.getIntValue(ThemeConfigParser.KEY_REFRESH_BUTTON_Y),
                    conf.getIntValue(ThemeConfigParser.KEY_REFRESH_BUTTON_WIDTH),
                    conf.getIntValue(ThemeConfigParser.KEY_REFRESH_BUTTON_HEIGHT)
            );

            launchButton = Bitmap.createBitmap(
                    sprite,
                    conf.getIntValue(ThemeConfigParser.KEY_LAUNCH_BUTTON_X),
                    conf.getIntValue(ThemeConfigParser.KEY_LAUNCH_BUTTON_Y),
                    conf.getIntValue(ThemeConfigParser.KEY_LAUNCH_BUTTON_WIDTH),
                    conf.getIntValue(ThemeConfigParser.KEY_LAUNCH_BUTTON_HEIGHT)
            );
        }
        catch(Exception ex) {
            String message = "RX: " + conf.getIntValue(ThemeConfigParser.KEY_REFRESH_BUTTON_X)
                            + "RY: " + conf.getIntValue(ThemeConfigParser.KEY_REFRESH_BUTTON_Y)
                            + "RW: " + conf.getIntValue(ThemeConfigParser.KEY_REFRESH_BUTTON_WIDTH)
                            + "RH: " + conf.getIntValue(ThemeConfigParser.KEY_REFRESH_BUTTON_HEIGHT);
            Log.e("LOADER", message);
        }
    }

    public Bitmap getThermometerBorder() {
        return Bitmap.createBitmap(thermometer[0]);
    }
    public Bitmap getThermometerCritical() {
        return Bitmap.createBitmap(thermometer[1]);
    }
    public Bitmap getThermometerNormal() {
        return Bitmap.createBitmap(thermometer[2]);
    }

    /**********************************************************************************/

    public Bitmap getBatteryBorder() {
        return Bitmap.createBitmap(battery[0]);
    }
    public Bitmap getBattery5() {
        return Bitmap.createBitmap(battery[1]);
    }
    public Bitmap getBattery4() {
        return Bitmap.createBitmap(battery[2]);
    }
    public Bitmap getBattery3() {
        return Bitmap.createBitmap(battery[3]);
    }
    public Bitmap getBattery2() {
        return Bitmap.createBitmap(battery[4]);
    }
    public Bitmap getBattery1() {
        return Bitmap.createBitmap(battery[5]);
    }

    /**********************************************************************************/

    public Bitmap getSignal5() {
        return Bitmap.createBitmap(signal[4]);
    }
    public Bitmap getSignal4() {
        return Bitmap.createBitmap(signal[3]);
    }
    public Bitmap getSignal3() {
        return Bitmap.createBitmap(signal[2]);
    }
    public Bitmap getSignal2() {
        return Bitmap.createBitmap(signal[1]);
    }
    public Bitmap getSignal1() {
        return Bitmap.createBitmap(signal[0]);
    }

    /**********************************************************************************/

    public Bitmap getBluetoothOn() {
        return Bitmap.createBitmap(bluetooth[0]);
    }
    public Bitmap getBluetoothOff() {
        return Bitmap.createBitmap(bluetooth[1]);
    }

    /**********************************************************************************/

    public Bitmap getInternalStorageBg() {
        return Bitmap.createBitmap(progressbars[0]);
    }
    public Bitmap getExternalStorageBg() {
        return Bitmap.createBitmap(progressbars[1]);
    }
    public Bitmap getMemoryBg() {
        return Bitmap.createBitmap(progressbars[2]);
    }
    public Bitmap getCpuBg() {
        return Bitmap.createBitmap(progressbars[3]);
    }
    public Bitmap getProgressFg() {
        return Bitmap.createBitmap(progressbars[4]);
    }

    /**********************************************************************************/

    public Bitmap getWifi4() {
        return Bitmap.createBitmap(wifi[4]);
    }
    public Bitmap getWifi3() {
        return Bitmap.createBitmap(wifi[3]);
    }
    public Bitmap getWifi2() {
        return Bitmap.createBitmap(wifi[2]);
    }
    public Bitmap getWifi1() {
        return Bitmap.createBitmap(wifi[1]);
    }
    public Bitmap getWifi0() {
        return Bitmap.createBitmap(wifi[0]);
    }

    /**********************************************************************************/

    public Bitmap getDigit0() {
        return Bitmap.createBitmap(clock[0]);
    }
    public Bitmap getDigit1() {
        return Bitmap.createBitmap(clock[1]);
    }
    public Bitmap getDigit2() {
        return Bitmap.createBitmap(clock[2]);
    }
    public Bitmap getDigit3() {
        return Bitmap.createBitmap(clock[3]);
    }
    public Bitmap getDigit4() {
        return Bitmap.createBitmap(clock[4]);
    }
    public Bitmap getDigit5() {
        return Bitmap.createBitmap(clock[5]);
    }
    public Bitmap getDigit6() {
        return Bitmap.createBitmap(clock[6]);
    }
    public Bitmap getDigit7() {
        return Bitmap.createBitmap(clock[7]);
    }
    public Bitmap getDigit8() {
        return Bitmap.createBitmap(clock[8]);
    }
    public Bitmap getDigit9() {
        return Bitmap.createBitmap(clock[9]);
    }
    public Bitmap getSeparator() {
        return Bitmap.createBitmap(clock[10]);
    }
    public Bitmap getClockBg() {
        return Bitmap.createBitmap(clock[11]);
    }
    public Bitmap getDigit(int digit) {
        switch(digit) {
            case 0:
                return getDigit0();
            case 1:
                return getDigit1();
            case 2:
                return getDigit2();
            case 3:
                return getDigit3();
            case 4:
                return getDigit4();
            case 5:
                return getDigit5();
            case 6:
                return getDigit6();
            case 7:
                return getDigit7();
            case 8:
                return getDigit8();
            case 9:
                return getDigit9();
            default:
                return null;
        }
    }

    public Bitmap getRefreshButton() {
        return refreshButton;
    }

    public Bitmap getLaunchButton() {
        return launchButton;
    }
}
