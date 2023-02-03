package com.avicodes.halchalin.presentation.ui.home

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase


class HomeActivityViewModel(
    val auth: FirebaseAuth
): ViewModel() {

    fun logout() {
        auth.signOut()
    }
}