package Common;

import java.io.File;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Common {
    public static final String iconFolder = "icons";
    public static int CountDay = 3;
    public static Boolean FullDesc = false;
    public static String XMLOut = "vsetv.xml";
    public static String Lang = "ru";
    public static Boolean IndexSort = true;
    
    public enum DBType {STRING, INTEGER, BOOL, DATETIME, TIME, OBJECT}

    public static CategoryProgrammeList CatList;

    public static String getIconsPatch() {
        return getCurrentPatch() + File.separator + iconFolder + File.separator;
    }

    public static String getCurrentPatch() {
        return Paths.get(".").toAbsolutePath().normalize().toString();
    }

    public static void loadConfigs() {
        CatList = new CategoryProgrammeList();
        CatList.loadFromDB();
    }

    public static void saveConfigs() {
        CatList.saveToDB();
    }

    public static String addZero(int Value) {
        if (Value < 10) {
            return "0" + Integer.toString(Value);
        } else {
            return Integer.toString(Value);
        }
    }

    public static String intToTime(int Value, String Sep, Boolean isZN, Boolean isSec) {
        String ZN = "";
        int Del = 60;
        if (isSec) {
            Del = Del * 60;
        }
        int crh = Value / Del;
        int crm;
        if (isSec) {
            crm = (Value - crh * Del) / 60;
        } else crm = Value - crh * Del;
        if (isZN) {
            if (Value >= 0) {
                ZN = "+";
            } else ZN = "-";
        }
        return ZN + addZero(crh) + Sep + addZero(crm);
    }

    public static String getMounth(Calendar cal, Boolean isShort) {
        String month = "";
        switch (cal.get(Calendar.MONTH)) {
            case Calendar.JANUARY:
                month = "янв";
                break;
            case Calendar.FEBRUARY:
                month = "фев";
                break;
            case Calendar.MARCH:
                month = "мар";
                break;
            case Calendar.APRIL:
                month = "апр";
                break;
            case Calendar.MAY:
                month = "май";
                break;
            case Calendar.JUNE:
                month = "июн";
                break;
            case Calendar.JULY:
                month = "июл";
                break;
            case Calendar.AUGUST:
                month = "авг";
                break;
            case Calendar.SEPTEMBER:
                month = "сен";
                break;
            case Calendar.OCTOBER:
                month = "окт";
                break;
            case Calendar.NOVEMBER:
                month = "ноя";
                break;
            case Calendar.DECEMBER:
                month = "дек";
                break;
        }
        return month;
    }

    public static String getWeek(Calendar cal, Boolean isShort) {
        String week = "";
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                week = "Пн";
                break;
            case Calendar.TUESDAY:
                week = "Вт";
                break;
            case Calendar.WEDNESDAY:
                week = "Ср";
                break;
            case Calendar.THURSDAY:
                week = "Чт";
                break;
            case Calendar.FRIDAY:
                week = "Пт";
                break;
            case Calendar.SATURDAY:
                week = "Сб";
                break;
            case Calendar.SUNDAY:
                week = "Вс";
                break;
        }
        return week;
    }

    public static String DTFormat(String Value, Boolean isOnlyTime) {
        String res = "";
        SimpleDateFormat ftdt = new SimpleDateFormat(Strings.DateFormatTime);
        Calendar dt = Calendar.getInstance();
        try {
            dt.setTime(ftdt.parse(Value));
            int day = dt.get(Calendar.DAY_OF_MONTH);
            String month = getMounth(dt, true);
            String week = getWeek(dt, true);
            int hour = dt.get(Calendar.HOUR_OF_DAY);
            int minute = dt.get(Calendar.MINUTE);
            if (isOnlyTime) {
                return addZero(hour) + ":" + addZero(minute);
            } else return addZero(day) + " " + month + " (" + week + ") " + addZero(hour) + ":" + addZero(minute);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static int isTimeLine(String sDate, String eDate) {
        int res = -1;
        SimpleDateFormat ftdt = new SimpleDateFormat(Strings.DateFormatTime);
        Date cdt = new Date();
        Date sdt;
        Date edt;
        try {
            sdt = ftdt.parse(sDate);
            edt = ftdt.parse(eDate);
            if (cdt.after(edt)) {
                return 0;
            } else if (cdt.before(edt) && cdt.after(sdt)) {
                return 1;
            } if (edt.after(cdt)) {
                return 2;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

}
