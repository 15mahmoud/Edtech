package com.example.student_project.ui.screen.widgets

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.student_project.data.model.Student
import com.example.student_project.data.model.User

@Composable
fun ImagePicker(
    student: User, onImageSelected: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var currentImageUrl by remember { mutableStateOf(student.image) }
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }
    var showPermissionDialog by remember { mutableStateOf(false) }
    var permissionMessage by remember { mutableStateOf("") }

    //contract is the output and input
    //here we define it as takepic->input will be uri and output will be boolean
    //if it is true it will call onresult and it take image uri and put it in current after switch to string
    val cameraLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture(),
            onResult = {
                if (it) {
                    tempImageUri?.let { uri ->
                        currentImageUrl = uri.toString()
                        onImageSelected(uri)
                    }
                }
            })

    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                uri?.let {
                    currentImageUrl = it.toString()
                    onImageSelected(it)
                }
            })

    val cameraPermissions =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted) {
                    openCamera(context, cameraLauncher) { uri ->
                        tempImageUri = uri
                    }
                } else {
                    permissionMessage = "Camera permission is required to take a picture"
                    showPermissionDialog = true
                }
            })

    val galleryPermission =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted) {
                    openGallery(context, galleryLauncher)
                } else {
                    permissionMessage = "Storage permission is required to select photos"
                    showPermissionDialog = true
                }

            })

    AsyncImage(
        model =
        ImageRequest.Builder(context)
            .data(currentImageUrl)
            .crossfade(true)
            .transformations(CircleCropTransformation())
            .build(),
        contentDescription = "profile pic",
        modifier = Modifier.fillMaxSize(),
    )
    IconButton(
        onClick = {
            showImagePickerOption(context,
                onCameraSelected = {
                    checkCameraPermission(context, onPermissionGranted = {
                        openCamera(context, cameraLauncher) { uri ->
                            tempImageUri = uri
                        }
                    }, launcher = cameraPermissions)
                }, onGallerySelected = {
                    checkGalleryPermission(context, onPermissionGranted = {
                        openGallery(context, galleryLauncher)
                    }, launcher = galleryPermission)
                })
        },
//        modifier = Modifier.align(Alignment.BottomEnd),
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "change pic",
            tint = Color.White,
            modifier = Modifier.background(Color.Black),
        )
    }

}

fun checkCameraPermission(
    context: Context,
    onPermissionGranted: () -> Unit,
    launcher: ManagedActivityResultLauncher<String, Boolean>
) {
    when {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED -> {
            onPermissionGranted()
        }

        (context as? android.app.Activity)?.let { activity ->
            ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.CAMERA
            )
        } == true -> {
            launcher.launch(Manifest.permission.CAMERA)
        }

        else -> {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }
}

fun checkGalleryPermission(
    context: Context,
    onPermissionGranted: () -> Unit,
    launcher: ManagedActivityResultLauncher<String, Boolean>
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        // For Android 13+, we don't need storage permission for gallery
        onPermissionGranted()
    }else{
        when{
        ContextCompat.checkSelfPermission(context,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ->{
            onPermissionGranted()
        }
            (context as? Activity)?.let{activity->
                ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.READ_EXTERNAL_STORAGE)
            } == true ->{
                launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            else -> {
                launcher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }
}

fun showImagePickerOption(
    context: Context,
    onCameraSelected: () -> Unit,
    onGallerySelected: () -> Unit
) {
    val option = arrayOf("camera", "gallery", "cancel")
    AlertDialog.Builder(context).apply {
        setTitle("Choose your profile Pic")
        setItems(option) { dialog, which ->
            when (which) {
                1 -> {
                    dialog.dismiss()
                    onCameraSelected()
                }

                2 -> {
                    dialog.dismiss()
                    onGallerySelected()
                }

                3 -> {
                    dialog.dismiss()
                }
            }

        }
    }

}

fun openGallery(context: Context, galleryLauncher: ManagedActivityResultLauncher<String, Uri?>) {
    galleryLauncher.launch("image/*")
}

fun openCamera(
    context: Context,
    cameraLauncher: androidx.activity.result.ActivityResultLauncher<Uri>,
    onUriCreated: (Uri) -> Unit
) {
    val values = ContentValues().apply {
        put(MediaStore.Images.Media.TITLE, "New Picture")
        put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
    }

    val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

    uri?.let {
        onUriCreated(it)
        cameraLauncher.launch(it)
    }
}
