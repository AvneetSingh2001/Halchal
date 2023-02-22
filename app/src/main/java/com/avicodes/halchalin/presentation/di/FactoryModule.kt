package com.avicodes.halchalin.presentation.di

import com.avicodes.halchalin.domain.repository.AdsRepository
import com.avicodes.halchalin.domain.repository.NewsRepository
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.*
import com.avicodes.halchalin.presentation.ui.auth.AuthFragmentViewModelFactory
import com.avicodes.halchalin.presentation.ui.auth.DetailsFragmentViewModelFactory
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
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
        signUpStateUseCase: signUpStateUseCase,
        getUserUseCase: GetUserUseCase
    ): AuthFragmentViewModelFactory {
        return AuthFragmentViewModelFactory(authenticateUseCase, onVerifyOtpUseCase, signUpStateUseCase, getUserUseCase)
    }

    @Singleton
    @Provides
    fun provideDetailsFragmentViewModelFactory(
        userUploadRemotelyUseCase: UserUploadRemotelyUseCase,
        getUserPhoneUseCase: GetUserPhoneUseCase
    ): DetailsFragmentViewModelFactory {
        return DetailsFragmentViewModelFactory(
            userUploadRemotelyUseCase = userUploadRemotelyUseCase,
            getUserPhoneUseCase = getUserPhoneUseCase
        )
    }

    @Singleton
    @Provides
    fun provideHomeActivityViewModelFactory(
        auth: FirebaseAuth,
        newsRepository: NewsRepository,
        adsRepository: AdsRepository
    ): HomeActivityViewModelFactory {
        return HomeActivityViewModelFactory(
            auth = auth,
            newsRepository = newsRepository,
            adsRepository = adsRepository
        )
    }
}