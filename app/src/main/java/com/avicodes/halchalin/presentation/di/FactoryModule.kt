package com.avicodes.halchalin.presentation.di

import com.avicodes.halchalin.MainActivityViewModelFactory
import com.avicodes.halchalin.domain.repository.*
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.*
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
        remoteNewsRepository: RemoteNewsRepository,
        localNewsRepository: LocalNewsRepository,
        updateUserPicUseCase: updateUserPicUseCase,
        userRespository: UserRespository,
        cityRepository: CityRepository,
        articleRepository: ArticleRepository
    ): HomeActivityViewModelFactory {
        return HomeActivityViewModelFactory(
            auth = auth,
            remoteNewsRepository = remoteNewsRepository,
            localNewsRepository = localNewsRepository,
            adsRepository = adsRepository,
            updateUserPicUseCase = updateUserPicUseCase,
            userRespository = userRespository,
            cityRepository = cityRepository,
            articleRepository = articleRepository
        )
    }

    @Singleton
    @Provides
    fun provideMainActivityViewModelFactory(
        phoneAuthRepository: PhoneAuthRepository,
        userRespository: UserRespository
    ) : MainActivityViewModelFactory {
        return MainActivityViewModelFactory(
            phoneAuthRepository = phoneAuthRepository,
            userRespository = userRespository
        )
    }
}