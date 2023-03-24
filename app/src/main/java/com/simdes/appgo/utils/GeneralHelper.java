package com.simdes.appgo.utils;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class GeneralHelper {

    public static String formatDate(String time) {
        String isoDate = time;
        SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        isoDateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // set timezone ke UTC
        Date date = null; // parse tanggal ISO 8601 menjadi objek Date
        try {
            date = isoDateFormat.parse(isoDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat newDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedDate = newDateFormat.format(date);
        return formattedDate;
    }

    public static void ToastNew(Context c, String message){
        Toast.makeText(c, "" + message, Toast.LENGTH_SHORT).show();
    }

    public void intentAct(Context c, Class cs){
        Intent u = new Intent(c, cs);
        c.startActivity(u);
    }

    public void intentActs(Context c, Class cs, String param){
        Intent u = new Intent(c, cs);
        u.putExtra("dataPut", param);
        c.startActivity(u);
    }

    //sjfkjfkjh

    public static String formatBalance(String bal){

        if(bal == null){
            bal = "0";
        }

        String conBal = "";
        String barArr[] = bal.split("");
        String balDot = "";

        int inde = 0;
        Log.d("brbr", barArr.length + "");

        for (int u = barArr.length-1; u >= 0; u--){
            inde++;
            if(inde%3 == 0){
                if(u != 0) {
                    if(inde == barArr.length-1){
                        balDot = barArr[u] + balDot;
                    }else {
                        balDot = "." + barArr[u] + balDot;
                    }
                }else{
                    balDot = barArr[u] + balDot;
                }
            }else {
                balDot = barArr[u] + balDot;
            }
        }

        Log.d("br_nom", bal);
        Log.d("br_nom_com", balDot);

//        if(configSystem.format.equals("1")){
//            conBal = configSystem.currency + " " + bal;
//        }else if(configSystem.format.equals("2")){
//            conBal = configSystem.currency + " " + balDot;
//        }else if(configSystem.format.equals("3")){
//            conBal = bal + " " + configSystem.code;
//        }else if(configSystem.format.equals("4")){
//            conBal = balDot + " " + configSystem.code;
//        }else {
//            conBal = "-";
//        }

        conBal = "Rp " + bal + ",-";

        return conBal;
    }

//    public void backPage(Activity act, View v){
//        ImageView iv = v.findViewById(R.id.btnBack);
//        iv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                act.finish();
//            }
//        });
//    }
}
