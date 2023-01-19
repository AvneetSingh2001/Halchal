package com.avicodes.halchalin.presentation.di

import com.avicodes.halchalin.domain.usecase.authenticationUseCase.authenticateUseCase
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.onVerifyOtpUseCase
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.signUpStateUseCase
import com.avicodes.halchalin.presentation.ui.AuthFragmentViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class FactoryModule {

    @Singleton
    @Provides
    fun provideAuthFragmentViewModelFactory(
        authenticateUseCase: authenticateUseCase,
        onVerifyOtpUseCase: onVerifyOtpUseCase,
        signUpStateUseCase: signUpStateUseCase
    ): AuthFragmentViewModelFactory {
        return AuthFragmentViewModelFactory(authenticateUseCase, onVerifyOtpUseCase, signUpStateUseCase)
    }

}