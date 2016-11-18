package ir.kcoder.cooldevicestats.Theme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Calendar;

import ir.kcoder.cooldevicestats.CDSPrefsManager;
import ir.kcoder.cooldevicestats.R;

/**
 * Created by mnvoh on 3/24/15.
 *
 */
public class ThemeProvider {
    private ThemeConfigParser config;
    private SpriteLoader sprites;
    private final int DISABLED_ALPHA = 90;
    private boolean supportedDevice = true;
    private Context context;
    private String lastKnownTheme = "";

    public ThemeProvider(Context c) {
        context = c.getApplicationContext();

        init();
    }

    public int getWidgetWidth() {
        return (int)context.getResources().getDimension(R.dimen.widget_width);
    }

    public int getWidgetHeight() {
        return (int)context.getResources().getDimension(R.dimen.widget_height);
    }

    /**
     * Checks the final bitmap for each cell before returning it. In case we are on an
     * unsupported device, a completely transparent bitmap will be returned.
     * @param input         The input bitmap which is about to be placed on the app widget.
     * @param printNotice   If the device is not supported, print "Unsupported Device" on the
     *                      transparent bitmap.
     * @return              The resulting bitmap.
     */
    public Bitmap resizeBitmap(Bitmap input, boolean printNotice) {
        if(supportedDevice) {
            return input;
        }

        Bitmap retval = Bitmap.createBitmap(input.getWidth(), input.getHeight(), Bitmap.Config.ARGB_8888);
        retval.eraseColor(0x33cccccc);
        if(printNotice) {
            Canvas canvas = new Canvas(retval);
            Paint p = new Paint();
            p.setAntiAlias(true);
            p.setTextSize(16.0f);
            p.setColor(0xff111111);
            p.setShadowLayer(3.0f, 0.0f, 0.0f, 0xffffffff);
            String text = context.getResources().getString(R.string.unsupported_device);
            float textWidth = p.measureText(text);
            canvas.drawText(text, input.getWidth() / 2 - textWidth / 2,
                    input.getHeight() / 2 - p.getTextSize() / 2, p);
        }
        return retval;
    }


    public Bitmap getClock() {
        checkThemeChanged();
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        Bitmap hour10 = sprites.getDigit(hour / 10);
        Bitmap hour1 = sprites.getDigit(hour % 10);
        Bitmap minute10 = sprites.getDigit(minute / 10);
        Bitmap minute1 = sprites.getDigit(minute % 10);
        Bitmap separator = sprites.getSeparator();

        int width = hour10.getWidth() + hour1.getWidth() + separator.getWidth() +
                minute10.getWidth() + minute1.getWidth();
        Bitmap clock = sprites.getClockBg();
        Canvas canvas = new Canvas(clock);
        int startX = clock.getWidth() / 2 - width / 2;
        Paint p = new Paint();
        canvas.drawBitmap(hour10, startX, 0, p);
        canvas.drawBitmap(hour1, startX + hour10.getWidth(), 0, p);
        canvas.drawBitmap(separator, startX + hour10.getWidth() + hour1.getWidth(), 0, p);
        canvas.drawBitmap(minute10, startX + hour10.getWidth() + hour1.getWidth() + separator.getWidth(), 0, p);
        canvas.drawBitmap(minute1, startX + hour10.getWidth() + hour1.getWidth() + separator.getWidth()
                + minute10.getWidth(), 0, p);
        hour10.recycle();
        hour1.recycle();
        separator.recycle();
        minute10.recycle();
        minute1.recycle();

        return resizeBitmap(clock, true);
    }

    /**
     * Returns the corresponding bitmap to the temperature provided
     * @param temperature the temperature in tenth of celsius
     * @return The bitmap
     */
    public Bitmap getThermometer(int temperature) {
        checkThemeChanged();
        /**
         * In order to get the fill, we have multiple condition:
         *      1. temperature > MAX_TEMP in which the fill is red and always fills the thermometer
         * completely.
         *      2. temperature > NORMAL_TEMP_UPPER and temperature < MAX_TEMP in which the fill is red and fills a
         * portion of the thermometer.
         *      3. temperature > NORMAL_TEMP_LOWER and temperature < NORMAL_TEMP_UPPER in which the fill is blue and fills a
         * portion of the thermometer.
         *      4. temerature > 0 and temperature < NORMAL_TEMP_LOWER in which the fill is red and fills a portion
         * of the thermometer between the zero level and upwards.
         *      5. temperature > MIN_TEMP and temperature < 0 in which the fill is red and fills a
         * portion of the thermometer between the base and the zero level.
         *      6. temperature < MIN_TEMP in which the fill is red and just above the base.
         */
        final int MAX_TEMP = 75;
        final int NORMAL_TEMP_UPPER = 45;
        final int NORMAL_TEMP_LOWER = 5;
        final int MIN_TEMP = -15;

        temperature = temperature / 10;
        int eraseHeight = 0;
        Bitmap bmp = null;

        // CONDITION #1
        if(temperature >= MAX_TEMP) {
            bmp = sprites.getThermometerCritical();
            eraseHeight = 0;
        }
        // CONDITION #2
        else if(temperature >= NORMAL_TEMP_UPPER && temperature < MAX_TEMP) {
            bmp = sprites.getThermometerCritical();
            int availableSpace = (int)Math.round(bmp.getHeight() *
                    config.getDoubleValue(ThemeConfigParser.KEY_THERMOMETER_ZERO_POSITION));
            eraseHeight = Math.round((MAX_TEMP - temperature) / (float)MAX_TEMP * availableSpace);
        }
        // CONDITION #3
        else if(temperature < NORMAL_TEMP_UPPER && temperature >= NORMAL_TEMP_LOWER) {
            bmp = sprites.getThermometerNormal();
            int availableSpace = (int)Math.round(bmp.getHeight() *
                    config.getDoubleValue(ThemeConfigParser.KEY_THERMOMETER_ZERO_POSITION));
            eraseHeight = Math.round((MAX_TEMP - temperature) / (float)MAX_TEMP * availableSpace);
        }
        // CONDITION #4
        else if(temperature < NORMAL_TEMP_LOWER && temperature >= 0) {
            bmp = sprites.getThermometerCritical();
            int availableSpace = (int)Math.round(bmp.getHeight() *
                    config.getDoubleValue(ThemeConfigParser.KEY_THERMOMETER_ZERO_POSITION));
            eraseHeight = Math.round((MAX_TEMP - temperature) / (float)MAX_TEMP * availableSpace);
        }
        // CONDITION #5
        else if(temperature >= MIN_TEMP && temperature < 0) {
            bmp = sprites.getThermometerCritical();
            int scale = MAX_TEMP - MIN_TEMP;
            int emptySpace = Math.abs(temperature);
            eraseHeight = (int)Math.round(bmp.getHeight() * ((emptySpace / (float)scale) +
                    config.getDoubleValue(ThemeConfigParser.KEY_THERMOMETER_ZERO_POSITION)));
        }
        // CONDITION #6
        else if(temperature < MIN_TEMP) {
            bmp = sprites.getThermometerCritical();
            eraseHeight = (int)Math.round(bmp.getHeight() *
                    config.getDoubleValue(ThemeConfigParser.KEY_THERMOMETER_BASE_POSITION));
        }

        Canvas canvas = new Canvas(bmp);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0x00000000);
        p.setStyle(Paint.Style.FILL);
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawRect(0, 0, bmp.getWidth(), eraseHeight, p);

        p.reset();
        canvas.drawBitmap(sprites.getThermometerBorder(), 0, 0, p);

        return resizeBitmap(bmp, false);
    }

    /**
     * returns the wifi sprite
     * @param signalStrength    The signal strength from -1 to 4, -1 being disabled.
     * @return                  Returns the bitmap corresponding to wifi status
     */
    public Bitmap getWifi(int signalStrength) {
        checkThemeChanged();
        switch(signalStrength) {
            case -1:
                Bitmap wifi = sprites.getWifi0();
                Bitmap disBitmap = Bitmap.createBitmap(wifi.getWidth(), wifi.getHeight(),
                        Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(disBitmap);
                Paint p = new Paint();
                p.setAlpha(DISABLED_ALPHA);
                canvas.drawBitmap(wifi, 0, 0, p);
                wifi.recycle();
                return resizeBitmap(disBitmap, false);
            case 0:
                return resizeBitmap(sprites.getWifi0(), false);
            case 1:
                return resizeBitmap(sprites.getWifi1(), false);
            case 2:
                return resizeBitmap(sprites.getWifi2(), false);
            case 3:
                return resizeBitmap(sprites.getWifi3(), false);
            case 4:
                return resizeBitmap(sprites.getWifi4(), false);
            default:
                return resizeBitmap(sprites.getWifi0(), false);
        }
    }

    /**
     * returns the corresponding bitmap to the bluetooth status
     * @param status    True: on, false: off
     * @return          Bitmap
     */
    public Bitmap getBluetooth(boolean status) {
        checkThemeChanged();
        Bitmap bmp;
        if(status) {
            bmp = sprites.getBluetoothOn();
        }
        else {
            bmp = sprites.getBluetoothOff();
        }

        return resizeBitmap(bmp, false);
    }

    public Bitmap getBattery(int percent) {
        checkThemeChanged();
        Bitmap bmp;
        if(percent > 80) {
            bmp = sprites.getBattery5();
        }
        else if(percent > 60) {
            bmp = sprites.getBattery4();
        }
        else if(percent > 40) {
            bmp = sprites.getBattery3();
        }
        else if(percent > 20) {
            bmp = sprites.getBattery2();
        }
        else {
            bmp = sprites.getBattery1();
        }
        Canvas canvas = new Canvas(bmp);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawBitmap(sprites.getBatteryBorder(), 0, 0, p);
        return resizeBitmap(bmp, false);
    }

    public Bitmap getSignal(int strength) {
        checkThemeChanged();
        Bitmap bmp;

        if(strength <= 1) {
            bmp = sprites.getSignal1();
        }
        else if(strength > 1 && strength <= 2) {
            bmp = sprites.getSignal2();
        }
        else if(strength > 2 && strength <= 3) {
            bmp = sprites.getSignal3();
        }
        else if(strength > 3 && strength <= 4) {
            bmp = sprites.getSignal4();
        }
        else {
            bmp = sprites.getSignal5();
        }

        return resizeBitmap(bmp, false);
    }

    public Bitmap getInternalStorage(int percentFull) {
        checkThemeChanged();
        Bitmap bmp = sprites.getInternalStorageBg();
        Bitmap fg = sprites.getProgressFg();
        int progType = config.getIntValue(ThemeConfigParser.KEY_PROGRESS_TYPE);
        if(progType == ThemeConfigParser.PROGTYPE_PIE) {
            Canvas c = new Canvas(fg);
            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
            p.setColor(0xffffffff);
            p.setStrokeWidth(3);
            p.setStyle(Paint.Style.FILL);
            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

            if(percentFull <= 0) {
                c.drawRect(0, 0, fg.getWidth(), fg.getHeight(), p);
            }
            else if(percentFull < 100) {
                Path path = createPiePath(fg.getWidth(), fg.getHeight(), percentFull);
                c.drawPath(path, p);
            }
            Canvas canvas = new Canvas(bmp);
            p = new Paint(Paint.ANTI_ALIAS_FLAG);
            canvas.drawBitmap(fg, 0, 0, p);
            fg.recycle();
            return resizeBitmap(bmp, false);
        }
        else if(progType == ThemeConfigParser.PROGTYPE_GAUGE) {
            int gaugeRangeDegree = config.getIntValue(ThemeConfigParser.KEY_GAUGE_RANGE_DEGREE);
            float degreesToRotate = gaugeRangeDegree * (percentFull / 100.0f);
            Matrix matrix = new Matrix();
            matrix.postRotate(degreesToRotate, fg.getWidth() / 2, fg.getWidth() / 2);
            Canvas canvas = new Canvas(bmp);
            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
            canvas.drawBitmap(fg, matrix, p);
            fg.recycle();
            return resizeBitmap(bmp, false);
        }
        return Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
    }

    /**
     *
     * @param percentFull The full percentage or -1 if no extSD is present.
     * @return The corresponding bitmap
     */
    public Bitmap getExternalStorage(int percentFull) {
        checkThemeChanged();
        Bitmap bmp = sprites.getExternalStorageBg();
        Bitmap fg = sprites.getProgressFg();

        if(percentFull == -1) {
            Bitmap retval = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas disCanvas = new Canvas(retval);
            Paint p = new Paint();
            p.setAlpha(DISABLED_ALPHA);
            disCanvas.drawBitmap(bmp, 0, 0, p);
            bmp.recycle();
            fg.recycle();
            return resizeBitmap(retval, false);
        }

        int progType = config.getIntValue(ThemeConfigParser.KEY_PROGRESS_TYPE);
        if(progType == ThemeConfigParser.PROGTYPE_PIE) {
            Canvas c = new Canvas(fg);
            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
            p.setColor(0xffffffff);
            p.setStrokeWidth(3);
            p.setStyle(Paint.Style.FILL);
            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

            if(percentFull <= 0) {
                c.drawRect(0, 0, fg.getWidth(), fg.getHeight(), p);
            }
            else if(percentFull < 100) {
                Path path = createPiePath(fg.getWidth(), fg.getHeight(), percentFull);
                c.drawPath(path, p);
            }
            Canvas canvas = new Canvas(bmp);
            p = new Paint(Paint.ANTI_ALIAS_FLAG);
            canvas.drawBitmap(fg, 0, 0, p);
            fg.recycle();
            return resizeBitmap(bmp, false);
        }
        else if(progType == ThemeConfigParser.PROGTYPE_GAUGE) {
            int gaugeRangeDegree = config.getIntValue(ThemeConfigParser.KEY_GAUGE_RANGE_DEGREE);
            float degreesToRotate = gaugeRangeDegree * (percentFull / 100.0f);
            Matrix matrix = new Matrix();
            matrix.postRotate(degreesToRotate, fg.getWidth() / 2, fg.getWidth() / 2);
            Canvas canvas = new Canvas(bmp);
            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
            canvas.drawBitmap(fg, matrix, p);
            fg.recycle();
            return resizeBitmap(bmp, false);
        }
        return Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
    }

    public Bitmap getMemory(int percentFull) {
        checkThemeChanged();
        Bitmap bmp = sprites.getMemoryBg();
        Bitmap fg = sprites.getProgressFg();


        int progType = config.getIntValue(ThemeConfigParser.KEY_PROGRESS_TYPE);
        if(progType == ThemeConfigParser.PROGTYPE_PIE) {
            Canvas c = new Canvas(fg);
            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
            p.setColor(0xffffffff);
            p.setStrokeWidth(3);
            p.setStyle(Paint.Style.FILL);
            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

            if(percentFull <= 0) {
                c.drawRect(0, 0, fg.getWidth(), fg.getHeight(), p);
            }
            else if(percentFull < 100) {
                Path path = createPiePath(fg.getWidth(), fg.getHeight(), percentFull);
                c.drawPath(path, p);
            }
            Canvas canvas = new Canvas(bmp);
            p = new Paint(Paint.ANTI_ALIAS_FLAG);
            canvas.drawBitmap(fg, 0, 0, p);
            fg.recycle();
            return resizeBitmap(bmp, false);
        }
        else if(progType == ThemeConfigParser.PROGTYPE_GAUGE) {
            int gaugeRangeDegree = config.getIntValue(ThemeConfigParser.KEY_GAUGE_RANGE_DEGREE);
            float degreesToRotate = gaugeRangeDegree * (percentFull / 100.0f);
            Matrix matrix = new Matrix();
            matrix.postRotate(degreesToRotate, fg.getWidth() / 2, fg.getWidth() / 2);
            Canvas canvas = new Canvas(bmp);
            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
            canvas.drawBitmap(fg, matrix, p);
            fg.recycle();
            return resizeBitmap(bmp, false);
        }
        return Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
    }

    public Bitmap getCpu(int percentFull) {
        checkThemeChanged();
        Bitmap bmp = sprites.getCpuBg();
        Bitmap fg = sprites.getProgressFg();

        int progType = config.getIntValue(ThemeConfigParser.KEY_PROGRESS_TYPE);
        if(progType == ThemeConfigParser.PROGTYPE_PIE) {
            Canvas c = new Canvas(fg);
            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
            p.setColor(0xffffffff);
            p.setStrokeWidth(3);
            p.setStyle(Paint.Style.FILL);
            p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

            if(percentFull <= 0) {
                c.drawRect(0, 0, fg.getWidth(), fg.getHeight(), p);
            }
            else if(percentFull < 100) {
                Path path = createPiePath(fg.getWidth(), fg.getHeight(), percentFull);
                c.drawPath(path, p);
            }
            Canvas canvas = new Canvas(bmp);
            p = new Paint(Paint.ANTI_ALIAS_FLAG);
            canvas.drawBitmap(fg, 0, 0, p);
            fg.recycle();
            return resizeBitmap(bmp, false);
        }
        else if(progType == ThemeConfigParser.PROGTYPE_GAUGE) {
            int gaugeRangeDegree = config.getIntValue(ThemeConfigParser.KEY_GAUGE_RANGE_DEGREE);
            Log.e("Gauge_RAnge_degrees", "GRD: " + gaugeRangeDegree);
            float degreesToRotate = gaugeRangeDegree * (percentFull / 100.0f);
            Matrix matrix = new Matrix();
            matrix.postRotate(degreesToRotate, fg.getWidth() / 2, fg.getWidth() / 2);
            Canvas canvas = new Canvas(bmp);
            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
            canvas.drawBitmap(fg, matrix, p);
            fg.recycle();
            return resizeBitmap(bmp, false);
        }
        return Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
    }

    private void init() {
        CDSPrefsManager prefs = new CDSPrefsManager(context);

        String activeThemeName = prefs.getActiveTheme();
        String activeThemePath = context.getExternalFilesDir(null).getParent() + "/themes/" +
                activeThemeName + "/";
        int dpi = context.getResources().getDisplayMetrics().densityDpi;
        String spritePath;
        String confPath;

        switch(dpi) {
            case DisplayMetrics.DENSITY_MEDIUM:
                spritePath = activeThemePath + produceFilenameFromThemeName(activeThemeName, false)
                        + "mdpi" + ".png";
                confPath = activeThemePath + produceFilenameFromThemeName(activeThemeName, false)
                        + "mdpi" + ".conf";
                break;
            case DisplayMetrics.DENSITY_HIGH:
                spritePath = activeThemePath + produceFilenameFromThemeName(activeThemeName, false)
                        + "hdpi" + ".png";
                confPath = activeThemePath + produceFilenameFromThemeName(activeThemeName, false)
                        + "hdpi" + ".conf";
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                spritePath = activeThemePath + produceFilenameFromThemeName(activeThemeName, false)
                        + "xhdpi" + ".png";
                confPath = activeThemePath + produceFilenameFromThemeName(activeThemeName, false)
                        + "xhdpi" + ".conf";
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                spritePath = activeThemePath + produceFilenameFromThemeName(activeThemeName, false)
                        + "xxhdpi" + ".png";
                confPath = activeThemePath + produceFilenameFromThemeName(activeThemeName, false)
                        + "xxhdpi" + ".conf";
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                spritePath = activeThemePath + produceFilenameFromThemeName(activeThemeName, false)
                        + "xxxhdpi" + ".png";
                confPath = activeThemePath + produceFilenameFromThemeName(activeThemeName, false)
                        + "xxxhdpi" + ".conf";
                break;
            case DisplayMetrics.DENSITY_TV:
                spritePath = activeThemePath + produceFilenameFromThemeName(activeThemeName, false)
                        + "tvdpi" + ".png";
                confPath = activeThemePath + produceFilenameFromThemeName(activeThemeName, false)
                        + "tvdpi" + ".conf";
                break;
            default:
                spritePath = activeThemePath + produceFilenameFromThemeName(activeThemeName, false)
                        + "xxxhdpi" + ".png";
                confPath = activeThemePath + produceFilenameFromThemeName(activeThemeName, false)
                        + "xxxhdpi" + ".conf";
                supportedDevice = false;
                break;
        }

        config = ThemeConfigParser.getInstance(confPath);
        sprites = SpriteLoader.getInstance(spritePath, config);
    }

    public Bitmap getRefreshButton() {
        return sprites.getRefreshButton();
    }

    public Bitmap getLaunchButton() {
        return sprites.getLaunchButton();
    }

    private void reinit() {
        CDSPrefsManager prefs = new CDSPrefsManager(context);

        String activeThemeName = prefs.getActiveTheme();
        String activeThemePath = context.getExternalFilesDir(null).getParent() + "/themes/" +
                activeThemeName + "/";
        int dpi = context.getResources().getDisplayMetrics().densityDpi;
        String spritePath;
        String confPath;

        switch(dpi) {
            case DisplayMetrics.DENSITY_MEDIUM:
                spritePath = activeThemePath + produceFilenameFromThemeName(activeThemeName, false)
                        + "mdpi" + ".png";
                confPath = activeThemePath + produceFilenameFromThemeName(activeThemeName, false)
                        + "mdpi" + ".conf";
                break;
            case DisplayMetrics.DENSITY_HIGH:
                spritePath = activeThemePath + produceFilenameFromThemeName(activeThemeName, false)
                        + "hdpi" + ".png";
                confPath = activeThemePath + produceFilenameFromThemeName(activeThemeName, false)
                        + "hdpi" + ".conf";
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                spritePath = activeThemePath + produceFilenameFromThemeName(activeThemeName, false)
                        + "xhdpi" + ".png";
                confPath = activeThemePath + produceFilenameFromThemeName(activeThemeName, false)
                        + "xhdpi" + ".conf";
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                spritePath = activeThemePath + produceFilenameFromThemeName(activeThemeName, false)
                        + "xxhdpi" + ".png";
                confPath = activeThemePath + produceFilenameFromThemeName(activeThemeName, false)
                        + "xxhdpi" + ".conf";
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                spritePath = activeThemePath + produceFilenameFromThemeName(activeThemeName, false)
                        + "xxxhdpi" + ".png";
                confPath = activeThemePath + produceFilenameFromThemeName(activeThemeName, false)
                        + "xxxhdpi" + ".conf";
                break;
            case DisplayMetrics.DENSITY_TV:
                spritePath = activeThemePath + produceFilenameFromThemeName(activeThemeName, false)
                        + "tvdpi" + ".png";
                confPath = activeThemePath + produceFilenameFromThemeName(activeThemeName, false)
                        + "tvdpi" + ".conf";
                break;
            default:
                spritePath = activeThemePath + produceFilenameFromThemeName(activeThemeName, false)
                        + "xxxhdpi" + ".png";
                confPath = activeThemePath + produceFilenameFromThemeName(activeThemeName, false)
                        + "xxxhdpi" + ".conf";
                supportedDevice = false;
                break;
        }

        config = ThemeConfigParser.getInstance(confPath, true);
        sprites = SpriteLoader.getInstance(spritePath, config, true);
    }

    private void checkThemeChanged() {
        CDSPrefsManager prefs = new CDSPrefsManager(context);
        String currentTheme = prefs.getActiveTheme();
        if(!currentTheme.equals(lastKnownTheme)) {
            reinit();
        }
        lastKnownTheme = currentTheme;
    }

    private Path createPiePath(int width, int height, int percent) {
        final double DEG45 = Math.PI / 4;
        final double DEG135 = Math.PI / 4 * 3;
        final double DEG225 = Math.PI * (5 / 4.0f);
        final double DEG315 = Math.PI * (7 / 4.0f);

        double angle = (percent / 100.0f) * (Math.PI * 2);
        double cartesianAngle = CDSMath.percentToAngle(percent);
        int x = (int)(width * CDSMath.x(cartesianAngle));
        int y = (int)(height * CDSMath.y(cartesianAngle));

        Path path = new Path();
        if(angle > 0 && angle < DEG45) {
            path.moveTo(width / 2, 0);
            path.lineTo(width / 2, height / 2);
            path.lineTo(x, y);
            path.lineTo(width, 0);
            path.lineTo(width, height);
            path.lineTo(0, height);
            path.lineTo(0, 0);
            path.lineTo(width / 2, 0);
        }
        else if(angle >= DEG45 && angle < DEG135) {
            path.moveTo(width / 2, 0);
            path.lineTo(width / 2, height / 2);
            path.lineTo(x, y);
            path.lineTo(width, height);
            path.lineTo(0, height);
            path.lineTo(0, 0);
            path.lineTo(width / 2, 0);
        }
        else if(angle >= DEG135 && angle < DEG225) {
            path.moveTo(width / 2, 0);
            path.lineTo(width / 2, height / 2);
            path.lineTo(x, y);
            path.lineTo(0, height);
            path.lineTo(0, 0);
            path.lineTo(width / 2, 0);
        }
        else if(angle >= DEG225 && angle < DEG315) {
            path.moveTo(width / 2, 0);
            path.lineTo(width / 2, height / 2);
            path.lineTo(x, y);
            path.lineTo(0, 0);
            path.lineTo(width / 2, 0);
        }
        else {
            path.moveTo(width / 2, 0);
            path.lineTo(width / 2, height / 2);
            path.lineTo(x, y);
            path.lineTo(width / 2, 0);
        }
        return path;
    }

    private static String produceFilenameFromThemeName(String themeName, boolean omitLastDash) {
        String regex = "([A-Z][a-z]+)";
        String replacement = "$1-";
        String replaced = themeName.replaceAll(regex, replacement).toLowerCase();
        String retval;
        if(omitLastDash) {
            retval = replaced.substring(0, replaced.length() - 1);
        }
        else {
            retval = replaced;
            if(!retval.contains("-")) {
                retval += "-";
            }
        }
        return retval;
    }

    public static class CDSMath {


        /**
         * returns a value from 0 to 1 based on the given angle using cotangent
         * @param angle The angle in radians
         * @return      The value from 0 to 1 which is the the x position of the angle
         *              on the unit circle, calculated from the bottom of the circle.
         */
        public static double x(double angle) {
            final double DEG45 = Math.PI / 4;
            final double DEG90 = Math.PI / 2;
            final double DEG135 = Math.PI / 4 * 3;
            final double DEG225 = Math.PI * (5 / 4.0f);
            final double DEG270 = Math.PI * (3 / 2.0f);
            final double DEG315 = Math.PI * (7 / 4.0f);

            if(angle >= DEG315 || angle <= DEG45) {
                return 1.0;
            }
            else if(angle > DEG45 && angle <= DEG90) {
                return ctan(angle) / 2.0f + 0.5;
            }
            else if(angle > DEG90 && angle <= DEG135) {
                return (1 - Math.abs(ctan(angle))) / 2.0f;
            }
            else if(angle > DEG135 && angle <= DEG225) {
                return 0.0;
            }
            else if(angle > DEG225 && angle <= DEG270) {
                return (1 - Math.abs(ctan(angle))) / 2.0f;
            }
            else if(angle > DEG270 && angle <= DEG315) {
                return Math.abs(ctan(angle)) / 2.0f + 0.5;
            }
            return 0;
        }



        /**
         * returns a value from 0 to 1 based on the given angle using tangent
         * @param angle The angle in radians
         * @return      The value from 0 to 1 which is the the y position of the angle
         *              on the unit circle, calculated from the bottom of the circle.
         */
        public static double y(double angle) {
            final double DEG45 = Math.PI / 4;
            final double DEG135 = Math.PI / 4 * 3;
            final double DEG180 = Math.PI;
            final double DEG225 = Math.PI * (5 / 4.0f);
            final double DEG315 = Math.PI * (7 / 4.0f);
            final double DEG360 = Math.PI * 2;

            if(angle >= 0 && angle <= DEG45) {
                return (1 - Math.tan(angle)) / 2.0f;
            }
            else if(angle > DEG45 && angle < DEG135) {
                return 0.0;
            }
            else if(angle >= DEG135 && angle < DEG180) {
                return (1 - Math.abs(Math.tan(angle))) / 2.0f;
            }
            else if(angle >= DEG180 && angle < DEG225) {
                return Math.tan(angle) / 2.0f + 0.5;
            }
            else if(angle >= DEG225 && angle < DEG315) {
                return 1.0;
            }
            else if(angle >= DEG315 && angle < DEG360) {
                return Math.abs(Math.tan(angle)) / 2.0f + 0.5;
            }
            return 0;
        }


        public static double ctan(double angle) {
            return 1 / Math.tan(angle);
        }

        public static double percentToAngle(int percent) {
            if(percent > 0 && percent <= 25) {
                return ((25 - percent) / 25.0f) * (Math.PI / 2);
            }
            else if(percent > 25 && percent <= 50) {
                return ((50 - percent) / 25.0f) * (Math.PI / 2) + (Math.PI * (3 / 2.0f));
            }
            else if(percent > 50 && percent <= 75) {
                return ((75 - percent) / 25.0f) * (Math.PI / 2) + Math.PI;
            }
            else if(percent > 75 && percent <= 100) {
                return ((100 - percent) / 25.0f) * (Math.PI / 2) + (Math.PI / 2);
            }
            return 0;
        }
    }
}
