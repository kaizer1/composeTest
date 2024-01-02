package cather.lfree.workdscather

import android.app.Application
import com.google.firebase.FirebaseApp

class Applica : Application() {

     override fun onCreate() {
        super.onCreate()

      FirebaseApp.initializeApp(applicationContext)
    }
}