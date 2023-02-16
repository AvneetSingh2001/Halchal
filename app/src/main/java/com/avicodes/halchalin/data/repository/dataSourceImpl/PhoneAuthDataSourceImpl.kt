package com.avicodes.halchalin.data.repository.dataSourceImpl

import android.content.ContentValues.TAG
import android.util.Log
import com.avicodes.halchalin.MainActivity
import com.avicodes.halchalin.data.repository.dataSource.PhoneAuthDataSource
import com.avicodes.halchalin.data.utils.Result
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.TimeUnit

class PhoneAuthDataSourceImpl(
    private val auth: FirebaseAuth,
    private val activity: MainActivity
): PhoneAuthDataSource {

    var verificationOtp: String = ""
    var resentToken: PhoneAuthProvider.ForceResendingToken? = null

    private var _signUpState : MutableStateFlow<Result<String>> = MutableStateFlow(Result.NotInitialized)
    override val signUpState: MutableStateFlow<Result<String>>
        get() = _signUpState

    override suspend fun authenticate(phone: String) {
        _signUpState.value = Result.Loading(activity.getString(com.avicodes.halchalin.R.string.code_will_be_send))
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

    override suspend fun onVerifyOtp(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationOtp,code)
        signInWithPhoneAuthCredential(credential)
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
            _signUpState.value = Result.Loading("Verification completed")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.w(TAG, "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                Log.w(TAG, "onVerificationFailed", e)
            } else if (e is FirebaseTooManyRequestsException) {
                Log.w(TAG, "onVerificationFailed", e)
            }

            _signUpState.value = Result.Error(java.lang.Exception(activity.getString(com.avicodes.halchalin.R.string.verification_failed)))

        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            Log.d(TAG, "onCodeSent:$verificationId")
            verificationOtp = verificationId
            resentToken = token
            _signUpState.value = Result.Loading(activity.getString(com.avicodes.halchalin.R.string.code_sent))
        }
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    _signUpState.value = Result.Success<String>(task.result.user!!.uid)
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        _signUpState.value = Result.Error(Exception(activity.getString(com.avicodes.halchalin.R.string.invalid_code)))
                        return@addOnCompleteListener
                    }
                    _signUpState.value = Result.Error(Exception(activity.getString(com.avicodes.halchalin.R.string.verification_failed)))


                }
            }
    }

}