package com.avicodes.halchalin.presentation.di

import com.avicodes.halchalin.presentation.ui.auth.MainActivityViewModelFactory
import com.avicodes.halchalin.presentation.ui.SplashViewModelFactory
import com.avicodes.halchalin.domain.repository.*
import com.avicodes.halchalin.presentation.ui.auth.details.DetailsFragmentViewModelFactory
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
        userRespository: UserRespository,
        cityRepository: CityRepository
    ): DetailsFragmentViewModelFactory {
        return DetailsFragmentViewModelFactory(
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
        userRespository: UserRespository,
        cityRepository: CityRepository,
        articleRepository: ArticleRepository
    ): HomeActivityViewModelFactory {
        return HomeActivityViewModelFactory(
            auth = auth,
            remoteNewsRepository = remoteNewsRepository,
            localNewsRepository = localNewsRepository,
            adsRepository = adsRepository,
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

    @Singleton
    @Provides
    fun provideSplashViewModelFactory(
        userRespository: UserRespository
    ) : SplashViewModelFactory {
        return SplashViewModelFactory(
            userRespository = userRespository
        )
    }
}