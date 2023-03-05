package com.avicodes.halchalin.presentation.ui.auth

import androidx.lifecycle.ViewModel
import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.GetUserPhoneUseCase
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.UserUploadRemotelyUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseUserMetadata

class DetailsFragmentViewModel(
    private val auth: FirebaseAuth,
    private val userUploadRemotelyUseCase: UserUploadRemotelyUseCase,
    private val getUserPhoneUseCase: GetUserPhoneUseCase
): ViewModel() {

    fun uploadUser(
        name: String,
        loc: String
    ) {
        val user = User(
            name = name,
            mobile = getUserPhoneUseCase.execute(),
            location = loc,
            userId = auth.currentUser!!.uid
        )
        userUploadRemotelyUseCase.execute(user)
    }

}