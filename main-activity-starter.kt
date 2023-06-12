package com.sierraobryan.myapplication

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    // TODO figure out state and build the UI!  
}

@Composable
private fun Image(
    bitmap: Bitmap?
) {
    
}

@Composable
private fun LabelRow(
    label: String, 
    confidence: Double, 
    index: Int, 
) {
    
}

@Composable
private fun ButtonRow(
    // TODO figure out arguments! 
) {

}

@Preview(name = "Label Row Preview")
@Composable
private fun LabelRowPreview() {
    MyApplicationTheme {
        LabelRow(
            label = "Test label", 
            confidence = 0.95678, 
            index = 100
        )
    }
}

@Preview(name = "Button Row Preview")
@Composable
private fun ButtonRowPreview() {
    MyApplicationTheme {
        ButtonRow()
    }
}
