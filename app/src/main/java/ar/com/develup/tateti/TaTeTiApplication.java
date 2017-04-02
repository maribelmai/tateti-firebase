package ar.com.develup.tateti;

import android.app.Application;

import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by facundomr on 4/2/17.
 */

public class TaTeTiApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseMessaging.getInstance().subscribeToTopic("general");
    }
}