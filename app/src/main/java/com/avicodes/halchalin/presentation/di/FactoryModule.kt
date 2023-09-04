package com.avicodes.halchalin.presentation.di

import com.avicodes.halchalin.presentation.ui.SplashViewModelFactory
import com.avicodes.halchalin.domain.repository.*
import com.avicodes.halchalin.presentation.ui.auth.details.DetailsActivityViewModelFactory
import com.avicodes.halchalin.presentation.ui.auth.providers.google.GoogleAuthViewModelFactory
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
    fun provideHomeActivityViewModelFactory(
        auth: FirebaseAuth,
        adsRepository: AdsRepository,
        remoteNewsRepository: RemoteNewsRepository,
        localNewsRepository: LocalNewsRepository,
        userRespository: UserRespository,
        cityRepository: CityRepository,
        articleRepository: ArticleRepository,
        adminRepository: AdminRepository
    ): HomeActivityViewModelFactory {
        return HomeActivityViewModelFactory(
            auth = auth,
            remoteNewsRepository = remoteNewsRepository,
            localNewsRepository = localNewsRepository,
            adsRepository = adsRepository,
            userRespository = userRespository,
            cityRepository = cityRepository,
            articleRepository = articleRepository,
            adminRepository = adminRepository
        )
    }

    @Singleton
    @Provides
    fun provideDetailsActivityViewModelFactory(
        cityRepository: CityRepository,
        userRespository: UserRespository
    ) : DetailsActivityViewModelFactory {
        return DetailsActivityViewModelFactory(
            userRespository = userRespository,
            cityRepository = cityRepository
        )
    }

    @Singleton
    @Provides
    fun provideGoogleAuthActivityViewModelFactory(
        userRespository: UserRespository
    ) : GoogleAuthViewModelFactory {
        return GoogleAuthViewModelFactory(
            userRespository = userRespository,
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