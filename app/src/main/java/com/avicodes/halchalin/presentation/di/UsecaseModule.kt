package com.avicodes.halchalin.presentation.di

import com.avicodes.halchalin.domain.repository.PhoneAuthRepository
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.authenticateUseCase
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.onVerifyOtpUseCase
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.signUpStateUseCase
import com.avicodes.halchalin.domain.usecase.getUserPhoneUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class UsecaseModule {

    @Provides
    @Singleton
    fun provideAuthenticateUsecase(phoneAuthRepository: PhoneAuthRepository): authenticateUseCase = authenticateUseCase(phoneAuthRepository)


    @Provides
    @Singleton
    fun provideOnVerifyOtpUsecase(phoneAuthRepository: PhoneAuthRepository): onVerifyOtpUseCase = onVerifyOtpUseCase(phoneAuthRepository)

    @Provides
    @Singleton
    fun provideSignupStateUsecase(phoneAuthRepository: PhoneAuthRepository): signUpStateUseCase = signUpStateUseCase(phoneAuthRepository)


    @Provides
    @Singleton
    fun provideGetUserPhoneUsecase(phoneAuthRepository: PhoneAuthRepository): getUserPhoneUseCase = getUserPhoneUseCase(phoneAuthRepository)

}