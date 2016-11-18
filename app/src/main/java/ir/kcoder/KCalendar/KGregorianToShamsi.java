package ir.kcoder.KCalendar;

/**
 * Created by mnvoh on 3/22/15.
 */
public class KGregorianToShamsi {

    /**
     * Takes gregorian date and returns a Shamsi date
     * @param year  The gregorian year
     * @param month The gregorian month
     * @param day   The gregorian date
     * @return Returns an array in which: <br>
     *     arr[0] = samsi date <br>
     *     arr[1] = shamsi month <br>
     *     arr[2] = shamsi year
     */
    public static int[] ConvertGregorianToShamsi(int year,int month, int day)
    {
        int syear, smonth, sday;
        month++;
        int[] day_of_year = new int[12]; 	//day_of_leapYear, i.e. days past in the year in a leap year
        //to calculate days past in a non-leap year decrement it if
        //month >= 2

        day_of_year[0] = 0;
        day_of_year[1] = 31;
        day_of_year[2] = 60;
        day_of_year[3] = 91;
        day_of_year[4] = 121;
        day_of_year[5] = 152;
        day_of_year[6] = 182;
        day_of_year[7] = 213;
        day_of_year[8] = 244;
        day_of_year[9] = 274;
        day_of_year[10] = 305;
        day_of_year[11] = 335;

        if((year/4)*4 == year)               //if it's leap year
        {    //so this starts from 11/10 and ends at 11/10 Shamsi
            if((month < 3) || (month == 3 && day <= 19))
            {
                int g_day_of_year;                              //day of year in gregorian date
                int s_day_of_year;                              //day of yer in shamsi date
                syear = year - 622;
                g_day_of_year = day_of_year[month-1] + day;
                //since this gregorian year starts from 11/10 Shamsi, so
                //till 11/10(Shamsi) the s_day_of_year would be 286
                s_day_of_year = g_day_of_year + 286;
                //Now all we have to do is to calculate the shamsi date from
                //the s_day_of_year
                int[] temp = ShamsiDateFromDays(s_day_of_year);
                sday = temp[0];
                smonth = temp[1];
            }
            else
            {
                //In this case we subtract 79 from g_day_of_year and there we
                //we have s_day_of_year
                int g_day_of_year;
                int s_day_of_year;
                syear = year - 621;
                g_day_of_year = day_of_year[month-1] + day;
                s_day_of_year = g_day_of_year - 79;
                int[] temp = ShamsiDateFromDays(s_day_of_year);
                sday = temp[0];
                smonth = temp[1];
            }
        }
        else if(((year-1)/4)*4 == (year-1))  //if it's one year after a leap year
        {    //this year is from 12/10 to 10/10 shamsi
            if((month < 3) || (month == 3 && day <= 20))
            {
                int g_day_of_year;
                int s_day_of_year;
                syear = year - 622;
                g_day_of_year = day_of_year[month-1] + day;
                if(month>=3)
                    g_day_of_year--;
                //since this gregorian year starts from 12/10 Shamsi, so
                //till 12/10(Shamsi) the s_day_of_year would be 287
                s_day_of_year = g_day_of_year + 287;
                //Now all we have to do is to calculate the shamsi date from
                //the s_day_of_year
                int[] temp = ShamsiDateFromDays(s_day_of_year);
                sday = temp[0];
                smonth = temp[1];
            }
            else
            {
                //In this case we subtract 79 from g_day_of_year and there we
                //we have s_day_of_year
                int g_day_of_year;
                int s_day_of_year;
                syear = year - 621;
                g_day_of_year = day_of_year[month-1] + day;
                if(month>=3) g_day_of_year--;
                s_day_of_year = g_day_of_year - 79;
                int[] temp = ShamsiDateFromDays(s_day_of_year);
                sday = temp[0];
                smonth = temp[1];
            }
        }
        else                                 //if it's none of the above cases
        {
            if((month < 3) || (month == 3 && day <= 20))
            {
                int g_day_of_year;
                int s_day_of_year;
                syear = year - 622;
                g_day_of_year = day_of_year[month-1] + day;
                if(month>=3) g_day_of_year--;
                //since this gregorian year starts from 11/10 Shamsi, so
                //till 11/10(Shamsi) the s_day_of_year would be 286
                s_day_of_year = g_day_of_year + 286;
                //Now all we have to do is to calculate the shamsi date from
                //the s_day_of_year
                int[] temp = ShamsiDateFromDays(s_day_of_year);
                sday = temp[0];
                smonth = temp[1];
            }
            else
            {
                //In this case we subtract 79 from g_day_of_year and there we
                //we have s_day_of_year
                int g_day_of_year;
                int s_day_of_year;
                syear = year - 621;
                g_day_of_year = day_of_year[month-1] + day;
                if(month>=3) g_day_of_year--;
                s_day_of_year = g_day_of_year - 79;
                int[] temp = ShamsiDateFromDays(s_day_of_year);
                sday = temp[0];
                smonth = temp[1];
            }
        }

        int[] retval = {sday, smonth, syear};
        return retval;
    }

    private static int[] ShamsiDateFromDays(int daysPast)
    {
        int month, day;
        int[] res = new int[2];

        if(daysPast <= 186)
        {
            month = (daysPast-1)/31;
            day = daysPast - (month*31);
            month++;
        }
        else if(daysPast > 186 && daysPast <=336)
        {
            month = 6 + (daysPast - 186 - 1)/30;
            day = daysPast - (186 + (month - 6)*30);
            month++;
        }
        else if(daysPast > 336 && daysPast <= 366)
        {
            month = 12;
            day = daysPast - (6*31 + 5*30);
        }
        else
        {
            month=-1;
            day=-1;
        }

        res[0] = day;
        res[1] = month;

        return res;
    }
}