package com.example.student_project.ui.screen.details.course

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun CourseLessonScreen(navController: NavController, encodedUrl: String?) {
val context = LocalContext.current
    val videoUrl = URLDecoder.decode(encodedUrl.toString(), StandardCharsets.UTF_8.toString())

    val exoPlayer = ExoPlayer.Builder(context).build()
    val mediaSource = remember(videoUrl){
        MediaItem.fromUri(videoUrl)
    }
    LaunchedEffect(mediaSource) {
        exoPlayer.setMediaItem(mediaSource)
        exoPlayer.prepare()
    }
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
    AndroidView(factory ={ctx->
        PlayerView(ctx).apply {
            player = exoPlayer
        }
    }, modifier = Modifier.fillMaxSize()
    )

}
