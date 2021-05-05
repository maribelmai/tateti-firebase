package ar.com.develup.tateti

import android.app.Application
import com.google.firebase.messaging.FirebaseMessaging

class TaTeTiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseMessaging.getInstance().subscribeToTopic("general")
    }
}