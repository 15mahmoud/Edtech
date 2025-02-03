package com.example.student_project.ui.screen.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.student_project.R
import com.example.student_project.ui.theme.buttonColor
import com.example.student_project.ui.theme.darkerGrayColor

// we will change this to lambda fun that will take string and will return -> string
// then take the returning string and put it in a a list
@Composable
fun BickerButton(timeSlot: String?, onClick: (String?) -> Unit) {
    var selected by remember { mutableStateOf(false) }
    val color = if (selected) buttonColor else Color.White
    Button(
        onClick = {
            selected = !selected
            onClick(timeSlot)
        },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        modifier = Modifier.padding(5.dp),
    ) {
        Text(
            text = timeSlot.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = darkerGrayColor,
            fontSize = 12.sp,
        )
    }
}

// we need to add these
// after modify it
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldFilterScreenTopBar(navController: NavController, text: String) {
    TopAppBar(
        title = {
            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                        contentDescription = null,
                    )
                }
                Text(
                    text = text,
                    modifier = Modifier.padding(top = 12.dp),
                    style = MaterialTheme.typography.headlineLarge,
                    fontSize = 24.sp,
                    fontWeight = FontWeight(700),
                )
            }
        }
    )
}

@Composable
fun CourseFilterScreenText(text: String) {
    Text(
        text = text,
        Modifier.padding(10.dp),
        fontSize = 14.sp,
        style = MaterialTheme.typography.headlineLarge,
    )
}

@Composable
fun PopBackStackEntry(navController: NavController) {
    IconButton(
        onClick = { navController.popBackStack() },
        modifier = Modifier.padding(top = 20.dp),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
            contentDescription = null,
        )
    }
}
