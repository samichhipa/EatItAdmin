package com.example.eatitadmin.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(

            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA76q2V14:APA91bE12bHa3uIlO1fMt6mlTL7fB0t0Rf_GDfmCNwzvg67a0kqfNKmSxqO7gJZ_iPzMKlg_yXdCO7oiv4fLbZZn5-zFFTlijwKdhi6X3ikp5sqhDUBAF88_WnLcSOyzyD1qEInftAoZ"

            }

    )


    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
