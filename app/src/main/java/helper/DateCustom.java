package helper;

import java.text.SimpleDateFormat;

public class DateCustom {

    public static String todayDate (){
        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY");
        String dateString = simpleDateFormat.format(date);

        return dateString;
    }

}
