package com.example.student_project.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.student_project.ui.screen.home.content.BottomNavBar
import com.example.student_project.util.Constant

@Composable
fun MyCoursesScreen(navController: NavController) {
    val selectedItemIndex by rememberSaveable { mutableStateOf(1) }
    Scaffold(
        Modifier
            .fillMaxSize()
            .background(Color.White),
        topBar = {
            Row(
                modifier = Modifier.background(Color.White).padding(Constant.paddingComponentFromScreen),
                horizontalArrangement = Arrangement.spacedBy(Constant.paddingComponentFromScreen)
            ) {
                Button(onClick = { /*TODO*/ }) {

                }
                Button(onClick = { /*TODO*/ }) {

                }
            }
        },
        bottomBar = { BottomNavBar(selectedItemIndex, navController) },
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {}
    }
}
