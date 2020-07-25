package com.example.eatitadmin.Common;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;

import com.example.eatitadmin.Model.Request;
import com.example.eatitadmin.Model.Users;
import com.example.eatitadmin.Notifications.APIService;
import com.example.eatitadmin.Notifications.RetrofitClient;

import java.util.Calendar;
import java.util.Locale;

public class Common {

    public static Users currentUser;
    public static Request currentRequest;

    public static final String BASE_URL="https://fcm.googleapis.com/";

    public static APIService getFCMService()
    {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);

    }


    public static final String UPDATE="Update";
    public static final String DELETE="Delete";


    public static String convertCodeToStatus(String code){

        if (code.equals("0")){

            return "Placed";

        }else if(code.equals("1")){

            return "On My Way";


        }else{

            return "Shipped";
        }

    }
    public static String getDate(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        StringBuilder date = new StringBuilder(DateFormat.format("dd-MM-yyyy HH:mm", calendar).toString());
        return date.toString();


    }
    public static boolean isConnectedToInternet(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {

            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {

                for (int i = 0; i < info.length; i++) {

                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }

            }
        }
        return false;

    }
}
