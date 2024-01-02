package cather.lfree.workdscather

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat



// urlCallback: (String) -> Unit,
@Composable
fun CameraPreview(sW: Int,sH: Int, urlTextM : String,  urlCallback: (String) -> Unit) {

    val lifeCycleOwner = LocalLifecycleOwner.current

     println( " sw = $sW and shh = $sH" )
    AndroidView( modifier =  Modifier
                    .padding(all = 30.dp)
                    .size(sW.dp),
        factory = { context ->
            val previewView = PreviewView(context)
            val preview = Preview.Builder().build()
            val cameraSelector = CameraSelector.Builder().build()

            preview.setSurfaceProvider(previewView.surfaceProvider)

            val imageAnalysis = ImageAnalysis.Builder().build()

            imageAnalysis.setAnalyzer(
                ContextCompat.getMainExecutor(context),
                QRCodeAnalyzer { url ->
                    urlCallback(url)
                }
            )

            println("urlTextMMM = ${urlTextM}")

            ProcessCameraProvider.getInstance(context).get().bindToLifecycle(
                lifeCycleOwner,
                cameraSelector,
                preview,
                imageAnalysis
            )

            previewView
        }
    )
}