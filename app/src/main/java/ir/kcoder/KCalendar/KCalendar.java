package ir.kcoder.KCalendar;

import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import ir.kcoder.cooldevicestats.CDSPrefsManager;
import ir.kcoder.cooldevicestats.R;

/**
 * Created by mnvoh on 3/22/15.
 */
public class KCalendar {
    private Context context;
    private int year;
    private int month;
    private int date;
    private String shortDate;
    private String longDate;

    public KCalendar(Context context) {
        this.context = context;
        CDSPrefsManager prefsManager = new CDSPrefsManager(context);
        int defCal = prefsManager.getDefaultCalendar();
        Calendar cal = new GregorianCalendar();
        if(defCal == CDSPrefsManager.CALENDAR_GREGORIAN) {
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            date = cal.get(Calendar.DATE);
            SimpleDateFormat shortfmt = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat longfmt = new SimpleDateFormat("EEEE, MMMM d, yyyy");
            shortDate = shortfmt.format(cal.getTime());
            longDate = longfmt.format(cal.getTime());
        }
        else {
            int[] sdate = KGregorianToShamsi.ConvertGregorianToShamsi(
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)
            );
            date = sdate[0];
            month = sdate[1];
            year = sdate[2];
            shortDate = year + "/" + ((month > 9) ? month : "0" + month) + "/" +
                    ((date > 9) ? date : "0" + date);
            longDate = getShamsiDay() + "، " + date + " " + getShamsiMonthName(month) + " " + year;
        }
    }

    public int getYear() {
        return year;
    }
    public int getMonth() {
        return month;
    }
    public int getDate() {
        return date;
    }

    public String getShortDate() {
        return shortDate;
    }
    public String getLongDate() {
        return longDate;
    }

    private String getShamsiMonthName(int month) {
        switch(month) {
            case 1:
                return "فروردین";
            case 2:
                return "اردیبهشت";
            case 3:
                return "خرداد";
            case 4:
                return "تیر";
            case 5:
                return "مرداد";
            case 6:
                return "شهریور";
            case 7:
                return "مهر";
            case 8:
                return "آبان";
            case 9:
                return "آذر";
            case 10:
                return "دی";
            case 11:
                return "بهمن";
            case 12:
                return "اسفند";
        }
        return "";
    }

    private String getShamsiDay() {
        Calendar cal = new GregorianCalendar();
        switch(cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SATURDAY:
                return "شنبه";
            case Calendar.SUNDAY:
                return "یکشنبه";
            case Calendar.MONDAY:
                return "دوشنبه";
            case Calendar.TUESDAY:
                return "سه شنبه";
            case Calendar.WEDNESDAY:
                return "چهارشنبه";
            case Calendar.THURSDAY:
                return "پنجشنبه";
            case Calendar.FRIDAY:
                return "جمعه";
        }
        return "";
    }

}
