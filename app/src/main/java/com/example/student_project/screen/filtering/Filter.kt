package com.example.student_project.screen.filtering

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.student_project.R
import com.example.student_project.screen.Screens

@Composable
fun FilterScreen(navController: NavController){
    Scaffold(
topBar = {
    ScaffoldFilterScreenTopBar(navController = navController)
}
    ) {innerPadding->
        Column (Modifier.padding(innerPadding)){

        }

    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldFilterScreenTopBar(navController: NavController) {
    TopAppBar(
        title = {
            Column(
                modifier = Modifier

                    .padding(20.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { navController.navigate(Screens.HomeScreen.route) }) {
                        Image(painter = painterResource(id = R.drawable.move_back), contentDescription =null )
                    }
                        Text(
                            text = "Filters",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight(400)
                        )
                        
                    }
                }
            
        }
    )

}
