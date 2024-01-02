package cather.lfree.workdscather

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cather.lfree.workdscather.servers.ServesNotify
import cather.lfree.workdscather.servers.SmsProcessService
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar

class testTabCompose : ComponentActivity() {

        private lateinit var prefs : SharedPreferences
        private lateinit var mContext : Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
  setContent {
        val navController = rememberNavController()

            ComposeTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    mContext = LocalContext.current
                    HideControllBars(wind = window)
                    MainScreen(navController = navController)
                }
            }
     }

  }


@Composable
fun MainScreen(
    navController: NavHostController
) {
    Scaffold(
        bottomBar = {
            BottomAppBar(modifier = Modifier) {
                BottomNavigationBar(navController = navController)
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(
                PaddingValues(
                    0.dp,
                    0.dp,
                    0.dp,
                    innerPadding.calculateBottomPadding()
                )
            )
        ) {
            Navigations(navController = navController)
        }
    }
}


 @Composable
fun Navigations(navController: NavHostController) {
    NavHost(navController, startDestination = NavigationItem.Home.route) {
        composable(NavigationItem.Home.route) {
            HomeScreen()
        }
        composable(NavigationItem.Vector.route) {
            VectorScreen()
        }
        composable(NavigationItem.Settings.route) {
            SettingsScreen()
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Vector,
        NavigationItem.Settings,
    )
    var selectedItem by remember { mutableStateOf(0) }
    var currentRoute by remember { mutableStateOf(NavigationItem.Home.route) }

    items.forEachIndexed { index, navigationItem ->
        if (navigationItem.route == currentRoute) {
            selectedItem = index
        }
    }

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                alwaysShowLabel = true,
                icon = {  Icon( item.icon!!, contentDescription = item.title.toString()) },
                // icon = { painterResource(id = R.drawable.settinim)},
                //icon = {     (R.drawable.settinim, contentDescription = item.title )   }, //  (item.icon!!, contentDescription = item.title) },
                label = { Text(stringResource(id =  item.title)) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    currentRoute = item.route
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen() {



    val db = FirebaseFirestore.getInstance();
      prefs = PreferenceManager.getDefaultSharedPreferences( mContext)
     val androidUnique = prefs.getString("androidID", "")

     val onleinA  by remember { mutableStateOf(true) }
     var seeUpdateFire by remember { mutableStateOf(false) }

       Column {
           Text(
               modifier = Modifier
                   .padding(top = 30.dp)
                   .padding(start = 20.dp),
               text = stringResource(id = R.string.data_main_d),
               color = Color.Black,
               fontSize = 42.sp,
               fontWeight = FontWeight.Bold
           )

           FlowRow(  modifier = Modifier
               .fillMaxWidth()
               .padding(horizontal = 20.dp)
               .padding(top = 16.dp),
              //verticalAlignment = Alignment.CenterVertically
           ){

               if(onleinA) {
                    drawKF(Color.Green)
                    UpdateDataOnlineText(stringResource(id = R.string.online_data))
                    CurrentTime(1)
               }else{
                    drawKF(Color.Red)
                    UpdateDataOnlineText(stringResource(id = R.string.offline_data))
                    Text(modifier =  Modifier
                       .padding(start = 10.dp),
                       text = stringResource(id = R.string.succes_last))
               }

      } // end's ROW

               if(!onleinA){
                   CurrentTime(110)
               }

           // StartList
//           val dataList = listOf(
//               DataL("900","SMS","Вы получили перевод на такую-то сумму. Вы молодец и всё такое","2023:11:05 10:01:08"),
//               DataL("Почта банк","PUSH","С вас списали миллиарды. Всёпока петух","2023:11:05 10:1:08"),
//               DataL("Почта банк","PUSH","С вас списали миллиарды. Всёпока петух","2023:11:05 10:1:08"),
//               DataL("Почта банк","PUSH","С вас списали миллиарды. Всёпока петух","2023:11:05 10:1:08"),
//               DataL("Почта банк","PUSH","С вас списали миллиарды. Всёпока петух","2023:11:05 10:1:08")
//           )

           val arrayMu = remember {
               mutableListOf<DataL>()
           }



           db.collection(androidUnique!!)
            .get()
            .addOnSuccessListener { result ->

             for (document in result.documents) {
                 println(" my document sizes ")
                    arrayMu.add(DataL(document.get("name").toString(), document.get("type").toString(), document.get("message").toString(), document.get("time").toString()))
                }


                seeUpdateFire = true
          //customAdapterLo = CustomADap( arrayMu, inflater.context)
          //listV.adapter = customAdapterLo

            }
            .addOnFailureListener { exception ->
                println( "Error getting documents. $exception ")
            }


//           LazyColumn(modifier = Modifier
//               .padding(top = 12.dp)
//               .padding(horizontal = 10.dp)
//           ){
////               items(items = arrayMu) {item ->
////                   ViewItemL(item)
////               }
//               items (items = arrayMu, itemContent = { item ->
//                   ViewItemL(item)
//               })
//           }

           if(seeUpdateFire){
                        FirebaseUI(arrayMu)
           }else{
                  Text(" No see update database is empty. Sorry")
           }



       }
}


    @Composable
    fun FirebaseUI(arrayMu1 : MutableList<DataL>) {

         LazyColumn(modifier = Modifier
             .padding(top = 12.dp)
             .padding(horizontal = 10.dp)
           ){
               items(items = arrayMu1) {item ->
                   ViewItemL(item)
               }
           }
    }

@Composable
fun drawKF(col : Color){
               Canvas(modifier = Modifier.size(26.dp), onDraw = {
                drawCircle(color = col)
    })
}

@Composable
fun CurrentTime(startOnPd : Int){
      val formatter = SimpleDateFormat("dd.MM.yyyy hh:mm:ss")
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = System.currentTimeMillis()
                Text( modifier = Modifier
                    .padding(start = startOnPd.dp)
                    ,
                    text = " (${formatter.format(calendar.time)})")
}
    
    
@Composable
fun ViewItemL(dataMy : DataL){

      Column(modifier = Modifier
          .fillMaxWidth()
          .padding(top = 4.dp)
          .clip(RoundedCornerShape(14))
          .background(Color.Black)
          ,
    ) {
          Row(modifier = Modifier
              .padding(start = 16.dp)
              .padding(top = 10.dp))

          {

              Text(modifier = Modifier
                  .align(Alignment.Bottom),
                  text = dataMy.type,

                  color = Color.White,
                  fontSize = 20.sp,
                  fontWeight = FontWeight.Bold
                  )
              Spacer(modifier = Modifier.padding(start = 3.dp))

              Text(modifier = Modifier
                  .align(Alignment.Bottom),
                  text = "from:",
                  color = Color.LightGray
                  )

              Spacer(modifier = Modifier.padding(start = 3.dp))

               Text(modifier = Modifier
                   .align(Alignment.Bottom),
                   text = dataMy.title,
                  color = Color.White)


              Spacer(modifier = Modifier.padding(start = 14.dp))
              
              Text(modifier = Modifier
                  .align(Alignment.CenterVertically),
                  text = dataMy.time,
                  color = Color.White,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold
              )
          }

          Row (modifier = Modifier
              .padding(top = 6.dp)
              .padding(start = 16.dp)
          ){
             Text(text = "content:",
                 color = lGrey
             )
          }

            Row (modifier = Modifier
                .padding(top = 6.dp)
                .padding(start = 16.dp)
          ){
             Text(text = dataMy.messages,
                 color = Color.White)
          }


            Row (modifier = Modifier
                .padding(top = 9.dp)
                .padding(start = 16.dp)
                .padding(bottom = 12.dp)
          ){
             Text(text = "status:",
                 color = lGrey
             )

                Spacer(modifier = Modifier.padding(start = 3.dp))
                Text(text = stringResource(id = R.string.succes_deliver),
                 color = Color.Green)
          }

      }
}

@Composable
fun UpdateDataOnlineText(stdState : String ){


    Text(modifier = Modifier
                   .padding(start = 8.dp),
                   text = stdState)

}

@Composable
fun VectorScreen() {

      Column {
          Text(
              modifier = Modifier
                  .padding(top = 30.dp)
                  .padding(start = 20.dp),
              text = stringResource(id = R.string.vector_main_d),
              color = Color.Black,
              fontSize = 42.sp,
              fontWeight = FontWeight.Bold

          )

          Text(
              modifier = Modifier
                  .padding(top = 18.dp)
                  .padding(start = 20.dp),
              text = stringResource(id = R.string.vector_main_d_sbor),
              color = Color.Black,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold

          )



          Row(
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 20.dp)
                  .padding(top = 20.dp)
                  .border(2.dp, Color.Black)
                  .size(width = 0.dp, height = 56.dp),
              verticalAlignment = Alignment.CenterVertically
          ) {
              Text(
                  modifier = Modifier
                      .padding(start = 40.dp),
                  text = "PUSH",
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold
              )

              Spacer(
                  modifier = Modifier
                      .padding(start = 140.dp)
              )

              SwitchWithIconExample("push")

          }



           Row(
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 20.dp)
                  .padding(top = 14.dp)
                  .border(2.dp, Color.Black)
                  .size(width = 0.dp, height = 56.dp),
              verticalAlignment = Alignment.CenterVertically
          ) {
              Text(
                  modifier = Modifier
                      .padding(start = 40.dp)
                      .weight(2f, false)
                  ,
                  text = "SMS ",
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold
              )

              Spacer(
                  modifier = Modifier
                      .padding(start = 140.dp)
                      .weight(4f, false)
              )

              SwitchWithIconExample("sms")

          }
      }
}




@Composable
fun SwitchWithIconExample(names : String) {
    var checked by remember { mutableStateOf(true) }
    var appSelectSee by remember { mutableStateOf(false) }

    Switch(modifier = Modifier,
        checked = checked,

        onCheckedChange = {
            println(" change checked $names")
           if(setServices(names, it))
                appSelectSee = true

            checked = it
        },
        thumbContent = if (checked) {
            {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )


            }
        } else {
            null
        }
    )

     if(appSelectSee){

        Dialog(onDismissRequest = {
                         // onDismissRequest()
                     }) {
                         // Draw a rectangle shape with rounded corners inside the dialog
                         Card(
                             modifier = Modifier
                                 .fillMaxWidth()
                                 .height(375.dp)
                                 .padding(16.dp),
                             shape = RoundedCornerShape(16.dp),
                         ) {


                             Column (   modifier = Modifier
                                         .fillMaxSize()){


                                 Text(
                                     text = "This is a dialog with buttons and an image.",
                                     modifier = Modifier.padding(16.dp),
                                 )

                                 val texB = testB(window.context)
                                 val arGet = texB.packages
                                 val dataList = listOf("sdf", " sdf", " sldkfjs","sdfsdf","sdfsadfe3d")

                                 LazyColumn(
                                     verticalArrangement = Arrangement.Center,
                                     horizontalAlignment = Alignment.CenterHorizontally,
                                 ) {

                                       items(dataList) {item ->
                                        SimpleView(item)
                                    }
                                 }


                                 Row(
                                     modifier = Modifier
                                         .fillMaxWidth(),
                                     horizontalArrangement = Arrangement.Center,
                                 ) {
                                     TextButton(
                                         onClick = {
                                             // onDismissRequest()
                                         },
                                         modifier = Modifier.padding(8.dp),
                                     ) {
                                         Text("Dismiss")
                                     }
                                     TextButton(
                                         onClick = {
                                             // onConfirmation()
                                             appSelectSee = false
                                         },
                                         modifier = Modifier.padding(8.dp),
                                     ) {
                                         Text("Confirm")
                                     }
                                 }
                             }
                         }
                     }


     }

}


    private fun setServices(names :String, it : Boolean) : Boolean{



     if(names == "sms" && !it ){
         println(" stopServices SMS ")

             val smsServiceIntent = Intent(this, SmsProcessService::class.java)
              smsServiceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
              stopService(smsServiceIntent)


         return false
     }

    if(names == "sms"){
         println(" startServices SMS ")

        val smsServiceIntent = Intent(this, SmsProcessService::class.java)
              smsServiceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
              startService(smsServiceIntent)

        return false
    }

      if(names == "push" && !it ){
         println(" stopServices PUSH ")

             val requl = Intent(this, ServesNotify::class.java)
              requl.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
              stopService(requl)

          return false
     }

    if(names == "push"){
         println(" startServices PUSH")
     val requl = Intent(this, ServesNotify::class.java)
              requl.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
              startService(requl)

        return true
    }
    return false
}



@Composable
fun SimpleView(t : String){
    Text(text = t)
}
@Composable
fun SettingsScreen() {

       var showAlert by remember { mutableStateOf(false) }
       val mContext = window.context

          Column {
              Text(
                  modifier = Modifier
                      .padding(top = 30.dp)
                      .padding(start = 20.dp),
                  text = stringResource(id = R.string.sett_main_d),
                  color = Color.Black,
                  fontSize = 42.sp,
                  fontWeight = FontWeight.Bold

              )
              
              Text(
                  modifier = Modifier
                      .padding(top = 30.dp)
                      .padding(start = 20.dp),
                  text = stringResource(R.string.id_this),
                  color = Color.Black,
                  fontSize = 16.sp,
              )

              if(showAlert){
                 AlertDialog(
        title = {
            Text(text = "Ok")
        },
        text = {
            Text(text = stringResource(id = R.string.copy_succes))
        },
        onDismissRequest = {
        },
        confirmButton = {
            TextButton(
                onClick = {

                    showAlert = false
                }
            ) {
                Text("Ok")
            }
        }, )
    }

              Row (modifier = Modifier
                  .fillMaxWidth()
                  .padding(top = 8.dp)
                  .padding(horizontal = 20.dp)
                  .border(2.dp, Color.Gray)
                  .size(0.dp, 36.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {

                  Text(modifier = Modifier
                      .padding(start = 10.dp)
                      .weight(10f, false),
                      text = prefs.getString("androidID" , "").toString())

                  Image(modifier = Modifier
                      .weight(2f, false)
                      .clickable(true) {
                          println(" ok copy ")
                      }
                      ,

                 painter = painterResource(id = R.drawable.copy_ok), contentDescription = "image ds")
              }

               Text(modifier = Modifier
                   .padding(top = 12.dp)
                   .padding(start = 20.dp),
                  text = stringResource(id = R.string.app_id),
                  color = Color.Black,
                  fontSize = 16.sp, )

               Row (modifier = Modifier
                   .fillMaxWidth()
                   .padding(top = 8.dp)
                   .padding(horizontal = 20.dp)
                   .border(2.dp, Color.Gray)
                   .size(0.dp, 36.dp),
                   verticalAlignment = Alignment.CenterVertically ) {

                     Text(modifier = Modifier
                         .padding(start = 10.dp)
                         .weight(10f, false),
                      text = "1.0.1")

                  Image(modifier = Modifier
                      .weight(2f, false)
                      .clickable(true) {
                          println(" ok copy ")
                          copyToClipboard(mContext, "loskutnikov Power !")
                          showAlert = true },
                      painter = painterResource(id = R.drawable.copy_ok), contentDescription = "image ds")
               }

               Text(modifier = Modifier
                   .padding(top = 12.dp)
                   .padding(start = 20.dp),
                  text = stringResource(id = R.string.langu_ch),
                  color = Color.Black,
                  fontSize = 16.sp,
              )

              Demo_ExposedDropdownMenuBox()

              Spacer(modifier = Modifier.padding(top = 90.dp))

              // FourButton
              ButtonNGray(stringResource(id = R.string.test_send)){

              }
              ButtonNGray(stringResource(id = R.string.give_permis)){

                  //val checkingFunctions =
                    if(checkPerm()){
                         showAlert = true
                    }

              }
              ButtonNGray(stringResource(id = R.string.down_log)){

              }
              ButtonNGray(stringResource(id = R.string.check_upda)){

              }

          }
     }


    private fun checkPerm() : Boolean {
          val perm = PermissionOn(mContext, this)
        return if(perm.allPermissionsGranted()){
            true
        }else {
            perm.requestPermissiong()
            false
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Demo_ExposedDropdownMenuBox() {
    val context = LocalContext.current
    val coffeeDrinks = arrayOf("Русский", "English")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(coffeeDrinks[0]) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .padding(horizontal = 20.dp)
            .border(2.dp, Color.Gray)
            .size(0.dp, 36.dp),
                   // verticalAlignment = Alignment.CenterVertically

//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(32.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                coffeeDrinks.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            selectedText = item
                            expanded = false
                            Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun AlertDialogComfirmClipBoard(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
) {
    AlertDialog(
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Ok")
            }
        },
    )
}



    fun copyToClipboard(context: Context, text: String) {
    val clipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("password", text)
    clipboardManager.setPrimaryClip(clip)
  }


    @Composable
fun ButtonNGray(textN : String, onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(top = 6.dp)
            .fillMaxWidth()
            .size(width = 0.dp, height = 46.dp)
        ,
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(lGrey),
        onClick  = {onClick() }){
        Text(textN,
            fontSize = 14.sp,
            color = Color.White)
    }
}

}