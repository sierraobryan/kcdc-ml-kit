/* package com.sierraobryan.myapplication TODO your package name */ 

import android.Manifest
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.sierraobryan.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

@androidx.camera.core.ExperimentalGetImage
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CameraContent()
                }
            }
        }
    }
}

@androidx.camera.core.ExperimentalGetImage
@Composable
private fun CameraContent() {

    var labels by remember {
        mutableStateOf(emptyList<ImageLabel>())
    }

    /** Request permissions prior to launching exercise.**/
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        if (result.all { it.value }) {
            Log.d("MainActivity", "All required permissions granted")
        }
    }

    LaunchedEffect(Unit) {
        launch {
            permissionLauncher.launch(
                arrayOf(Manifest.permission.CAMERA)
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        CameraPreview(
            onImageAnalyzerOutputs = {labels = it}
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.colorScheme.background.copy(alpha = .8f))
        ) {
            LazyColumn(
                modifier = Modifier.weight(0.5f, false)
            ) {
                items(labels) { label ->
                    LabelRow(
                        label = label.text,
                        confidence = label.confidence,
                        index = label.index,
                    )
                }
            }
        }
    }
}

@Composable
private fun Content() {
    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var labels by remember {
        mutableStateOf(emptyList<ImageLabel>())
    }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images
                    .Media.getBitmap(context.contentResolver, uri)

            } else {
                val source = ImageDecoder
                    .createSource(context.contentResolver,uri)
                ImageDecoder.decodeBitmap(source)
            }
        }
        labels = emptyList()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ImageContent(bitmap = bitmap)
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.colorScheme.background.copy(alpha = .8f))
        ) {
            LazyColumn(
                modifier = Modifier.weight(0.5f, false)
            ) {
                items(labels) { label ->
                    LabelRow(
                        label = label.text,
                        confidence = label.confidence,
                        index = label.index,
                    )
                }
            }
            ButtonRow(
                processButtonEnabled = bitmap != null,
                onProcessClicked = {
                    process(bitmap, customLabeler) { labels = it }
                },
                onSelectClicked = { launcher.launch("image/*") }
            )
        }
    }
}

@Composable
private fun ImageContent(
    bitmap: Bitmap?
) {
    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Selected Photo",
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize()
        )
    } else {
        Text(
            text = "Select a Photo to get started!",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize()
        )
    }
}

@Composable
private fun LabelRow(
    label: String,
    confidence: Float,
    index: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = index.toString(),
                style = MaterialTheme.typography.labelSmall
            )
            Text(text = label)
        }
        Text(
            text = "%.2f%%".format(confidence * 100),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun ButtonRow(
    processButtonEnabled: Boolean,
    onProcessClicked: () -> Unit,
    onSelectClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = onSelectClicked,
        ) {
            Text("Select Image")
        }
        Button(
            onClick = onProcessClicked,
            enabled = processButtonEnabled,
        ) {
            Text(text = "Process Image")
        }
    }
}

@androidx.camera.core.ExperimentalGetImage
@Composable
fun CameraPreview(
    onImageAnalyzerOutputs: (List<ImageLabel>) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val executor = ContextCompat.getMainExecutor(ctx)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = androidx.camera.core.Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()

                val analyzer = ImageAnalyzer(customLabeler) { outputs ->
                    onImageAnalyzerOutputs(outputs)
                }

                val imageAnalyzer = ImageAnalysis.Builder()
                    .setTargetResolution(Size(224, 224))
                    .setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .apply { setAnalyzer(executor, analyzer) }


                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalyzer
                )
            }, executor)
            previewView
        },
        modifier = Modifier.fillMaxSize(),
    )
}

@androidx.camera.core.ExperimentalGetImage
class ImageAnalyzer(
    private val imageLabeler: ImageLabeler,
    private val onImageAnalyzerOutputs: (List<ImageLabel>) -> Unit,
) : ImageAnalysis.Analyzer {
    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.image?.let {
            val image = InputImage.fromMediaImage(it, imageProxy.imageInfo.rotationDegrees)

            imageLabeler.process(image)
                .addOnSuccessListener { labels ->
                    onImageAnalyzerOutputs(labels)
                    imageProxy.close()
                }
        }
    }
}

private val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

val customModel = LocalModel.Builder()
    .setAssetFilePath("custom_model.tflite")
    .build()
val customImageLabelerOptions = CustomImageLabelerOptions.Builder(customModel)
    .build()
val customLabeler = ImageLabeling.getClient(customImageLabelerOptions)

private fun process(
    bitmap: Bitmap?,
    imageLabeler: ImageLabeler,
    rotation: Int = 0,
    onComplete: (List<ImageLabel>) -> Unit
) {
    if (bitmap == null) return onComplete(emptyList())

    imageLabeler.process(bitmap, rotation)
        .addOnSuccessListener { onComplete(it) }
        .addOnFailureListener { Log.e("Failure", "${it.message}") }
}

@Preview(name = "Label Row Preview")
@Composable
private fun LabelRowPreview() {
    MyApplicationTheme {
        LabelRow(
            label = "Test label",
            confidence = 0.95678f,
            index = 100
        )
    }
}

@Preview(name = "Button Row Preview")
@Composable
private fun ButtonRowPreview() {
    MyApplicationTheme {
        ButtonRow(
            processButtonEnabled = true,
            onProcessClicked = {},
            onSelectClicked = {}
        )
    }
}
