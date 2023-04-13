package com.avicodes.halchalin.presentation.di

import com.avicodes.halchalin.MainActivityViewModelFactory
import com.avicodes.halchalin.domain.repository.*
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.*
import com.avicodes.halchalin.presentation.ui.auth.AuthFragmentViewModelFactory
import com.avicodes.halchalin.presentation.ui.auth.DetailsFragmentViewModelFactory
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModelFactory
import com.google.firebase.auth.FirebaseAuth
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
        getUserUseCase: GetUserUseCase,
        userRespository: UserRespository
    ): AuthFragmentViewModelFactory {
        return AuthFragmentViewModelFactory(
            authenticateUseCase,
            onVerifyOtpUseCase,
            signUpStateUseCase,
            getUserUseCase,
            userRespository
        )
    }

    @Singleton
    @Provides
    fun provideDetailsFragmentViewModelFactory(
        auth: FirebaseAuth,
        userUploadRemotelyUseCase: UserUploadRemotelyUseCase,
        userRespository: UserRespository,
        cityRepository: CityRepository
    ): DetailsFragmentViewModelFactory {
        return DetailsFragmentViewModelFactory(
            auth = auth,
            userUploadRemotelyUseCase = userUploadRemotelyUseCase,
            userRespository = userRespository,
            cityRepository = cityRepository
            )

    }

    @Singleton
    @Provides
    fun provideHomeActivityViewModelFactory(
        auth: FirebaseAuth,
        adsRepository: AdsRepository,
        categoryNewsRepository: CategoryNewsRepository,
        internationalNewsRepository: InternationalNewsRepository,
        nationalNewsRepository: NationalNewsRepository,
        localNewsRepository: LocalNewsRepository,
        getUserByIdUseCase: GetUserByIdUseCase,
        updateUserPicUseCase: updateUserPicUseCase,
        userRespository: UserRespository,
        cityRepository: CityRepository
    ): HomeActivityViewModelFactory {
        return HomeActivityViewModelFactory(
            auth = auth,
            categoryNewsRepository = categoryNewsRepository,
            internationalNewsRepository = internationalNewsRepository,
            nationalNewsRepository = nationalNewsRepository,
            localNewsRepository = localNewsRepository,
            adsRepository = adsRepository,
            getUserByIdUseCase = getUserByIdUseCase,
            updateUserPicUseCase = updateUserPicUseCase,
            userRespository = userRespository,
            cityRepository = cityRepository
        )
    }

    @Singleton
    @Provides
    fun provideMainActivityViewModelFactory(
        cityRepository: CityRepository
    ) : MainActivityViewModelFactory {
        return MainActivityViewModelFactory(
            cityRepository = cityRepository
        )
    }
}