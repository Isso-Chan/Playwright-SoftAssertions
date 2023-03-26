package com.utilities;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BrowserUtilities {

    /**
     * Performs a pause
     *
     * @param miliseconds
     */
    public static void waitFor(int miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    /**
     * this method returns date and time as string
     *
     * @return date
     */
    public static String getDateAndTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
    }

    /**
     * returns the date of yesterday
     *
     * @return
     */
    public static String yesterday() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        // Create a calendar object with today date. Calendar is in java.util pakage.
        Calendar calendar = Calendar.getInstance();

        // Move calendar to yesterday
        calendar.add(Calendar.DATE, -1);

        // Get current date of calendar which point to the yesterday now
        Date yesterday = calendar.getTime();

        return dateFormat.format(yesterday).toString();
    }

    /**
     * returns the date of today
     *
     * @return
     */
    public static String todayWithDotSeparators() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.uuuu");
        ZonedDateTime now = ZonedDateTime.now();
        return dtf.format(now);
    }

    /**
     * this method returns a string as a Date
     *
     * @param datePattern
     * @param dateAsString
     */
    public static Date convertStringToDate(String datePattern, String dateAsString) {
        //i.e pattern may be "yyyy-MM-dd HH:mm:ss.SSS"
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateAsString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    public static List<String> trimElementsOnList(List<String> list) {
        List<String> trimmedTexts = new ArrayList<>();
        for (String el : list) {
            trimmedTexts.add(el.trim());
        }
        return trimmedTexts;
    }
    public static List<Long> convertToLong(List<String> list) {
        List<Long> listLong = new ArrayList<>();
        for (String str : list) {
            listLong.add(Long.parseLong(str));
        }
        return listLong;
    }

    /**
     * This method removes null values from list of String
     *
     * @param list
     * @return
     */
    public static List<String> removeNulls(List<String> list) {
        list.removeAll(Collections.singletonList(null));
        return list;
    }

    /**
     * This method converts string into List according to rgx
     *
     * @param str
     * @param rgx
     * @return
     */
    public static List<String> convertStringToList(String str, String rgx) {
        String[] arr = str.split(rgx);
        List<String> list = new ArrayList<>();
        for (String s : arr) {
            list.add(s.trim());
        }
        removeNulls(list);
        return list;
    }

    public static String reverseString(String str) {
        StringBuilder sb = new StringBuilder(str);
        sb.reverse();
        return sb.toString();
    }

    public static String roundDecimalPart(String str) {
        double d = Double.parseDouble("0." + str);
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d).substring(2);
    }

    public static int getRandomNumberUsingNextInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public static void countDownTimer(String time) {
        String[] split = time.split(":");
        String strSecond = split[1];
        String strMinute = split[0];
        int minute = Integer.parseInt(time.split(":")[0]);
        int second = Integer.parseInt(time.split(":")[1]);
        if (strMinute.equals("00")) {
            minute--;
            second = 59;
        }
        System.out.println("Time remaining to go ahead after syncronisation!");

        do {
            do {
                if (minute < 10) {
                    strMinute = "0" + minute;
                } else {
                    strMinute = Integer.toString(minute);
                }
                if (second < 10) {
                    strSecond = "0" + second;
                } else {
                    strSecond = Integer.toString(second);
                }
                int itr = 0;
                while (itr < second) {
                    System.out.print("_");
                    itr++;
                }
                //print minute and second as time
                System.out.println(strMinute + ":" + strSecond);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                second--;
            } while (second >= 0);

            second = 59;
            minute--;
        } while (minute > -1);
        System.out.println("Time is over!!");
    }

    public static List<String> removeThisPartFromElements(List<String> list, String thisPart) {
        List<String> newList = new ArrayList<>();
        for (String el : list) {
            String s = el.split(thisPart)[1];
            newList.add(s);
        }
        return newList;
    }

    public static <T> List hasDuplicate(Iterable<T> all) {
        Set<T> set = new HashSet<T>();
        List<T> duplicates = new ArrayList<>();
        for (T s : all) {
            if (set.contains(s)) {
                duplicates.add(s);
            } else {
                set.add(s);
            }
        }
        return duplicates;
    }

}


