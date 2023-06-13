package com.sierraobryan.myapplication

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sierraobryan.myapplication.ui.theme.MyApplicationTheme

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
                    Content()
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

    // Note: just a list of Strings for now
    var labels by remember {
        mutableStateOf(emptyList<String>())
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
                        label = label,
                        confidence = 0f,
                        index = 0
                    )
                }
            }
            ButtonRow(
                processButtonEnabled = bitmap != null,
                onProcessClicked = { /* TODO: button functionality */ },
                onSelectClicked = { /* TODO: button functionality */ }
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
            modifier = Modifier.fillMaxSize().wrapContentSize()
        )
    } else {
        Text(
            text = "Select a Photo to get started!",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxSize().wrapContentSize()
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
        modifier = Modifier.fillMaxWidth().padding(8.dp),
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
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
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

