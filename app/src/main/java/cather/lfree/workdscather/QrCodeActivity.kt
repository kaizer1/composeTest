package cather.lfree.workdscather

import android.app.LocaleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import android.Manifest
import android.preference.PreferenceManager
import cather.lfree.workdscather.internet.AsynchronousGet
import cather.lfree.workdscather.internet.CallbackData
import org.json.JSONObject

class QrCodeActivity : ComponentActivity() , CallbackData {

  private var urlText by mutableStateOf("")

    var tokenIS : String? = null
    var domainIS : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        //urlText.state
         val prefs = PreferenceManager.getDefaultSharedPreferences  ( applicationContext )
         val inet = prefs.getInt("okEnter", 0)

        if(inet == 1){
            startActivity(Intent(this, testTabCompose::class.java).apply {
        })}

        setContent {
            Surface (modifier = Modifier
                .fillMaxSize(),
                color = MaterialTheme.colorScheme.background
              ) {

                 val mContext = LocalContext.current
                 val contex = LocalContext.current
                 var statusText by remember { mutableStateOf("") }

                PermissionRequestDialog(
                 permission = Manifest.permission.CAMERA,
                 onResult = { isGranted ->
                 statusText = if (isGranted) {
                "Scan QR code now!"
            } else {
                "No camera permission!"
            }
        },
    )


                Column {
                    HideControllBars(window)


                    val configuration = LocalConfiguration.current
                    val screenHeight = configuration.screenHeightDp
                    val screenWidth = configuration.screenWidthDp

                    Greeting("CATCHER", 52, 42, Color.Black)
                    VersionText()

                    Spacer(modifier = Modifier.padding(top = 30.dp))

                    Text(
                        modifier = Modifier.padding(start = 20.dp),
                        text = stringResource(id = R.string.nav_qrscrev),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    callChange(urlText)


                       CameraPreview (screenWidth, screenHeight /3 , urlText, urlCallback= {
                                urlText = it
                       })

                    
                 Text(modifier = Modifier
                     .padding(top = 14.dp)
                     .padding(horizontal = 20.dp)
                     , text = stringResource(id = R.string.enter_handley),
                     fontWeight = FontWeight.Bold,
                     fontSize = 16.sp
                     )

                    Spacer(modifier = Modifier.padding(top = 10.dp))

                    ButtonN(stringResource(id = R.string.enter_handley_data), lGrey) {
                          mContext.startActivity(Intent(mContext, MainActivity::class.java))
                    }


                    Row (modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 18.dp)
                        .padding(start = 20.dp)) {
                            Text(modifier = Modifier
                            .clickable(enabled = true) {
                                 ChangeLanguage(contex)
                            }
                            ,
                            text = stringResource(id = R.string.change_one),
                            color = Color.Gray,
                            fontSize = 16.sp,
                            fontStyle = FontStyle.Italic )

                        Spacer(modifier = Modifier.padding(start = 4.dp))
                        
                            Text(modifier = Modifier
                            .clickable(enabled = true) {

                                    ChangeLanguage(contex)

                            }
                            ,
                            text = stringResource(id = R.string.change_two),
                            color = Color.Gray,
                            fontSize = 16.sp,
                            fontStyle = FontStyle.Italic,
                            textDecoration = TextDecoration.Underline)
                    }

               }
            }
        }
    }

      override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("URL_KEY", urlText)
          println(" my URL 1 = $urlText")
        super.onSaveInstanceState(outState) // need to be called last
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val restoreUrlText = savedInstanceState.getString("URL_KEY")

        if(restoreUrlText != null) {
            urlText = restoreUrlText
            println("urlText 2 = $urlText")

        }
    }


    private fun callChange(urlText : String){
         println(" change text is this $urlText")
        if(urlText.length <= 1){

        }else {
            // parse JSON
             val jS = JSONObject(urlText)
            if(jS.getString("token").isNotEmpty()){
                 tokenIS = jS.getString("token")
                 domainIS = jS.getString("domain")
                println(" my token is = $tokenIS and domain = $domainIS")

                val asd = AsynchronousGet(tokenIS!!, 1, null, domainIS!!, this)
                asd.dataReturn = this
                asd.run()

            }
        }
     }


    private fun ChangeLanguage(cont : Context){

        val current = resources.configuration.locales.get(0)
        println(" my current is == $current") // ru_RU or en
            //println(" my current is22 == ${current.language}") // ru_RU or en
        println("dsf = ${current.displayLanguage} ")

        //if(current == "")

         val lan = if (current.displayLanguage == "русский"){
            "en"
         } else {
            "ru"
          }

        println(" my lan is == $lan")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        cont.getSystemService(LocaleManager::class.java).applicationLocales =
            LocaleList.forLanguageTags(lan) // was en
    } else {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(lan)
        )
      }
    }

    override fun returnServerAnswer(urlPing: String, urlMess: String, numberOperations: Int) {

       println(" return urlPing ! $urlPing and utlMess = $urlMess  and operNumner = $numberOperations")

        if(numberOperations ==7 ) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            val edi = prefs.edit()
            edi.putString("ping", urlPing)
            edi.putString("mess", urlMess)
            edi.putString("domen", domainIS!!)
            edi.putString("api_k", tokenIS!!)
            //edi.putInt("okEnter", 1)
            edi.apply()

            startActivity(Intent(this, ComposeLos2::class.java).apply {

            })

        }else {
             // alertDialog - enter data not valid
        }

    }
}