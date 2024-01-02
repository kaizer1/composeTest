package cather.lfree.workdscather

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Window
import android.webkit.URLUtil
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import cather.lfree.workdscather.internet.AsynchronousGet
import cather.lfree.workdscather.internet.CallbackData

class MainActivity : ComponentActivity(), CallbackData {

     private var keyMain : String? = null
    private var domainMain : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


               val prefs = PreferenceManager.getDefaultSharedPreferences  ( applicationContext )
         val inet = prefs.getInt("okEnter", 0)

        if(inet == 1){
            startActivity(Intent(this, testTabCompose::class.java).apply {
        })}


        setContent {
            ComposeTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val state: MutableState<Int> = remember { mutableIntStateOf(0) }
                    val textFieldA : MutableState<String> = remember { mutableStateOf("") }
                    val textFieldB: MutableState<String>  =  remember { mutableStateOf("") }

                    Column {

                    HideControllBars(window)
                    val mContext = LocalContext.current
                    Greeting("CATCHER", 102, 42, Color.Black)
                    VersionText()
                    TextFieldLos(textFieldA, textFieldB)
                        Spacer(modifier = Modifier
                            .padding(top = 36.dp)
                         )

                    ButtonN(stringResource(id = R.string.next_string), lGreen) { //println(" press this ")

                         if(textFieldA.value.isNotEmpty() && URLUtil.isValidUrl(textFieldA.value)){
                            }else {
                                 println(" nnn  3 ")
                                  state.value = 3
                           }

                          if(!NetworkUtils.isNetworkConnected(mContext)){
                              println(" nnn  2 ")
                            state.value = 2
                        }

                        if(textFieldA.value.isEmpty() || textFieldB.value.isEmpty()){
                            println(" nnn  1 ")
                                state.value = 1
                        }

                        if(state.value == 0){

                            keyMain = textFieldA.value
                            domainMain = textFieldB.value


sendServerCheck(keyMain!!, domainMain!!)
                             // mContext.startActivity(Intent(mContext, ComposeLos2::class.java))
                        }
                    }

                        Spacer(modifier = Modifier
                            .padding(top = 26.dp))


                        when(state.value){
                            1 -> CallbackText(stringResource(id = R.string.not_two_full))
                            2 -> CallbackText(stringResource(id = R.string.need_internet))
                            3 -> CallbackText(stringResource(id = R.string.correct_data))
                            0 -> CallbackText("")
                        }

                        Text(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 70.dp)
                            .padding(start = 20.dp)
                            .clickable (enabled = true){
                                mContext.startActivity(Intent(mContext, QrCodeActivity::class.java))
                            }
                            ,
                            text = stringResource(id = R.string.name_qr_codeactive),
                            color = Color.Gray,
                            fontSize = 20.sp,
                            fontStyle = FontStyle.Italic,
                            textDecoration = TextDecoration.Underline)

                    }
                }
            }
        }
    }



    private fun sendServerCheck(a :String, b : String ){

               val sRequestToServer = AsynchronousGet(a, 1, null, b, this)
                            sRequestToServer.dataReturn = this
                            sRequestToServer.run()
    }

       override fun returnServerAnswer(urlPing: String, urlMess: String, numberOperations: Int) {

       println(" return urlPing ! $urlPing and utlMess = $urlMess  and operNumner = $numberOperations")

        if(numberOperations == 7 ) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            val edi = prefs.edit()
            edi.putString("ping", urlPing)
            edi.putString("mess", urlMess)
            edi.putString("domen", domainMain!!)
            edi.putString("api_k", keyMain!!)
            edi.apply()

            startActivity(Intent(this, ComposeLos2::class.java).apply {

            })

        }else {
             // alertDialog - enter data not valid
        }

    }


}


@Composable
fun VersionText(){
     Text(modifier = Modifier
         .padding(start = 22.dp)
         , text = stringResource(id = R.string.version_actualy), fontSize = 16.sp)
}

@Composable
fun HideControllBars(wind : Window?){

                val insetsController = WindowCompat.getInsetsController(wind!!, wind.decorView)

                    insetsController.apply {

      hide(WindowInsetsCompat.Type.statusBars())
      hide(WindowInsetsCompat.Type.navigationBars())
      systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
     }
}



@Composable
fun CallbackText(alarmIs : String){
    Text(modifier = Modifier
        .padding(start = 20.dp)
        .padding(end = 20.dp),
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Red,
        text = alarmIs)
}
@Composable
fun ButtonN(textN : String, col : Color, onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
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
fun Greeting(name: String, sizeInt : Int, fontSize : Int, color : Color) {
    Text(
        text = name,
        fontSize = fontSize.sp,
        fontWeight = FontWeight.Bold,
        color = color,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = sizeInt.dp, start = 20.dp, end = 20.dp)
    )
}




@Composable
fun TextFieldLos( textFieldA : MutableState<String> , textFieldB : MutableState<String> ) {
    Column (modifier = Modifier
        .fillMaxWidth()
        .padding(top = 86.dp)
           ){

        
        Text(modifier = Modifier
            .padding(start = 20.dp)
            , text = stringResource(id = R.string.enter_domain))

             Spacer(modifier = Modifier
            .padding(top = 4.dp))
        
        OutlinedTextField(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .border(2.dp, Color.Black)
            , value = textFieldA.value, onValueChange = {
            textFieldA.value = it
        })

        Spacer(modifier = Modifier
            .padding(top = 12.dp))

             Spacer(modifier = Modifier
            .padding(top = 4.dp))

            Text(modifier = Modifier
            .padding(start = 20.dp)
            , text = stringResource(id = R.string.enter_apikey))

        OutlinedTextField(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .border(2.dp, Color.Black),
                value = textFieldB.value, onValueChange = {
            textFieldB.value = it
        })
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeTestTheme {
        Greeting("Android", 102, 42, Color.Black)
    }
}