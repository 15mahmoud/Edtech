package com.example.student_project.ui.screen.ai

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.student_project.ui.theme.buttonColor

@Composable
fun RowButtonToTransformFromAiToHistoryForGithubAi(
    ai: String,
    history: String,
    onAiClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    aiButtonSelected: Boolean,
    historySelected: Boolean
) {


    Row(modifier = Modifier.fillMaxWidth()) {
        Button(modifier = Modifier.weight(.5f),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (aiButtonSelected) buttonColor else Color.White
            ),
            onClick = {
                onAiClick()
            }) {
            Text(text = ai, color = if (aiButtonSelected) Color.White else buttonColor)
        }
        Button(modifier = Modifier.weight(.5f), colors = ButtonDefaults.buttonColors(
            containerColor = if (historySelected) buttonColor else Color.White
        ), onClick = {
            onHistoryClick()
        }) {
            Text(text = history, color = if (historySelected) Color.White else buttonColor)
        }
    }

}