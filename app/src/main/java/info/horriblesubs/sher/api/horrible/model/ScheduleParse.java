package info.horriblesubs.sher.api.horrible.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

abstract class ScheduleParse {

    Date parseDate(String date) {
        try {
            String format = "dd HH:mm Z";
            return new SimpleDateFormat(format, Locale.US).parse(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    String parseTimeView(String date) {
        try {
            return new SimpleDateFormat("HH:mm", Locale.US).format(parseDate(date));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}