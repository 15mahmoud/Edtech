package com.example.student_project.ui.screen.uiconstant

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.student_project.R

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
