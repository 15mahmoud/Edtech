package com.example.student_project.ui.screen.log.forgetpassword

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun OtpScreen(navController: NavController) {

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        val viewModel = viewModel<OtpViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val focusRequester = remember { (1..4).map { FocusRequester() } }
        val focusManger = LocalFocusManager.current
        val keyboardManger = LocalSoftwareKeyboardController.current

        LaunchedEffect(state.focusedIndex) {
            state.focusedIndex?.let { index -> focusRequester.getOrNull(index)?.requestFocus() }
        }

        LaunchedEffect(state.code, keyboardManger) {
            val allNumberEntered = state.code.none { it == null }
            if (allNumberEntered) {
                focusRequester.forEach { it.freeFocus() }
                focusManger.clearFocus()
                keyboardManger?.hide()
            }
        }
        OtpCodeScreen(
            state = state,
            focusRequester = focusRequester,
            onAction = { action ->
                when (action) {
                    is OtpAction.OnEnterNumber -> {
                        if (action.number != null) {
                            focusRequester[action.index].freeFocus()
                        }
                    }

                    else -> Unit
                }
                viewModel.onAction(action)
            },
            modifier = Modifier.padding(innerPadding).consumeWindowInsets(innerPadding),
            navController = navController,
        )
    }
}
