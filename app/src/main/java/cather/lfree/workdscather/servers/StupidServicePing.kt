package cather.lfree.workdscather.servers


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service;
import android.app.job.JobInfo.PRIORITY_MIN
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.preference.PreferenceManager
import android.provider.Settings
import androidx.core.app.NotificationCompat
import cather.lfree.workdscather.R
import cather.lfree.workdscather.internet.AsynchronousGet
import org.json.JSONObject
import java.util.Timer
import java.util.TimerTask



class StupidServicePing : Service() {

    var AsyncG : AsynchronousGet? = null
    // private lateinit var broadRec : BroadcastLReceiver
   // var broadRec = BroadCastEasy()

   companion object
   {
       private  final const val ID_SERVICE = 101
       private val timer = Timer()
   }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


  // LocalBroadcastManager.getInstance(this).registerReceiver(broadRec, IntentFilter("my.event.calling.circle") )

        val uniqueID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID)
        val prefs = PreferenceManager.getDefaultSharedPreferences  ( applicationContext )

        val json1  = JSONObject()
        json1.put("id", uniqueID)
        json1.put("version", "1.0.1")

        val manufacturer = Build.MANUFACTURER;
        val model = Build.MODEL;

        val json2  = JSONObject()
        json2.put("ip", "0.0.0.0")
        json2.put("model", "$manufacturer $model")

        val jsonM1 = JSONObject()
        jsonM1.put("application", json1)
        jsonM1.put("device", json2)

        AsyncG = AsynchronousGet(prefs.getString("api_k","")!!,2, jsonM1, prefs.getString("domen", "")!!, this)

        timer.scheduleAtFixedRate( object: TimerTask() {
            override fun run() {
                   AsyncG!!.run()
            }
        }, 0, 5000)



       // AsyncG!!.run()

        return START_STICKY
    }


    override fun onCreate() {
        super.onCreate()

        val notificationManager =  getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = createNotificationChannel(notificationManager)
        val notificationBuilder =  NotificationCompat.Builder(this, channelId)
        val notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(PRIORITY_MIN)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .build();

        startForeground(ID_SERVICE, notification)

    }

    private fun createNotificationChannel(notificationManager : NotificationManager ) : String{
        val channelId = "my_service_channelid";
        val channelName = "My Foreground Service";
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        // omitted the LED color
        channel.setImportance(NotificationManager.IMPORTANCE_NONE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        notificationManager.createNotificationChannel(channel);
        return channelId;
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
      //LocalBroadcastManager.getInstance(this).unregisterReceiver(broadRec)
        //stopForeground(true)
    }

}