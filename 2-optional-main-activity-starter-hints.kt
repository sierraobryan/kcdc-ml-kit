/* package com.sierraobryan.myapplication TODO your package name */ 

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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

    // Note: just use a list of Strings for now
    var labels by remember {
        mutableStateOf(emptyList<String>())
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Call the functions below here
    }
}

@Composable
private fun ImageContent(
    bitmap: Bitmap?
) {
    if (bitmap != null) {
        // display an [Image] Composeable
    } else {
        // display some text when there is no image selected
    }
}

@Composable
private fun LabelRow(
    label: String,
    confidence: Float,
    index: Int,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            /**
             *  Display the index and label
             *  Use `MaterialTheme.typography. ...` to change the sizes
             */

        }
        /**
         *  Display the confidence
         *  Format the confidence as a percentage; 0.985 -> 98.5%
         */
    }
}

@Composable
private fun ButtonRow(
    processButtonEnabled: Boolean,
    onProcessClicked: () -> Unit,
    onSelectClicked: () -> Unit,
) {
    /**
     * Use a row and two buttons
     * Pass `horizontalArrangement = Arrangement.SpaceEvenly` into the Row
     */
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

