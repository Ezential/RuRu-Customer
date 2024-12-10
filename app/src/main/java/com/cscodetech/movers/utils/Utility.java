package com.cscodetech.movers.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility  {

    private static final Utility instance = new Utility();

    public int paymentsucsses = 0;
    public String tragectionID = "0";
    public String paymentMID = "0";

    public int isvarification = -1;

    public boolean removepost = false;
    public double tWallet = 0;
    public Double payAmount;

    private Utility() {


    }


    public static Utility getInstance() {
        return instance;
    }




    public String parseDateToddMMyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MMM-yy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


}
