package com.avicodes.halchalin.data.repository.auth

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import com.avicodes.halchalin.data.utils.Result
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.TimeUnit

class PhoneAuthDataSourceImpl(
    private val auth: FirebaseAuth,
) : PhoneAuthDataSource {

    var verificationOtp: String = ""

    var resentToken: PhoneAuthProvider.ForceResendingToken? = null

    private var _phoneState: MutableStateFlow<Result<String>> =
        MutableStateFlow(Result.NotInitialized)
    override val phoneState: MutableStateFlow<Result<String>>
        get() = _phoneState


    private var _codeState: MutableStateFlow<Result<String>> =
        MutableStateFlow(Result.NotInitialized)
    override val codeState: MutableStateFlow<Result<String>>
        get() = _codeState

    override suspend fun authenticate(phone: String, activity: Activity) {
        _phoneState.value =
            Result.Loading(activity.getString(com.avicodes.halchalin.R.string.code_will_be_send))
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)       // Phone number to verify
            .setActivity(activity)
            .setCallbacks(callbacks)
            .setTimeout(120L, TimeUnit.SECONDS)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
        callbacks.onCodeSent(verificationId, token)
    }

    override suspend fun onVerifyOtp(code: String, activity: Activity) {
        val credential = PhoneAuthProvider.getCredential(verificationOtp, code)
        signInWithPhoneAuthCredential(credential, activity = activity)
    }

    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
        callbacks.onVerificationCompleted(credential)
    }

    override fun onVerificationFailed(exception: Exception) {
        callbacks.onVerificationFailed(exception as FirebaseException)
    }

    override fun getUserPhone(): String {
        return auth.currentUser?.phoneNumber.toString()
    }


    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted:$credential")
            _phoneState.value = Result.Loading("Verified")
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.w(TAG, "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                Log.w(TAG, "onVerificationFailed", e)
            } else if (e is FirebaseTooManyRequestsException) {
                Log.w(TAG, "onVerificationFailed", e)
            }

            _phoneState.value = Result.Error(e)
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            verificationOtp = verificationId
            resentToken = token
            _phoneState.value = Result.Success("Code Sent")
        }
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential, activity: Activity) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    task.result.user?.let { user ->
                        _codeState.value = Result.Success(user.uid)
                    }

                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        _codeState.value =
                            Result.Error(Exception(activity.getString(com.avicodes.halchalin.R.string.invalid_code)))
                        return@addOnCompleteListener
                    }
                    _codeState.value =
                        Result.Error(Exception(activity.getString(com.avicodes.halchalin.R.string.verification_failed)))
                }
            }
    }

}