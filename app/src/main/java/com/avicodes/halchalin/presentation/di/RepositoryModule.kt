package com.avicodes.halchalin.presentation.di

import com.avicodes.halchalin.data.repository.*
import com.avicodes.halchalin.data.repository.admin.AdminRepositoryImpl
import com.avicodes.halchalin.data.repository.ads.AdsDataSource
import com.avicodes.halchalin.data.repository.ads.AdsRepositoryImpl
import com.avicodes.halchalin.data.repository.article.ArticleDataSource
import com.avicodes.halchalin.data.repository.article.ArticleRepositoryImpl
import com.avicodes.halchalin.data.repository.auth.PhoneAuthDataSource
import com.avicodes.halchalin.data.repository.auth.PhoneAuthRepositoryImpl
import com.avicodes.halchalin.data.repository.news.local.LocalNewsRepositoryImpl
import com.avicodes.halchalin.data.repository.news.local.RemoteLocalNewsDataSource
import com.avicodes.halchalin.data.repository.news.remote.RemoteNewsRepositoryImpl
import com.avicodes.halchalin.data.repository.news.remote.RemoteNewsDataSource
import com.avicodes.halchalin.data.repository.settings.city.CityDataSource
import com.avicodes.halchalin.data.repository.settings.city.CityRespositoryImpl
import com.avicodes.halchalin.data.repository.settings.user.UserDataSource
import com.avicodes.halchalin.data.repository.settings.user.UserRepositoryImpl
import com.avicodes.halchalin.domain.repository.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun providePhoneAuthRepository(phoneAuthDataSource: PhoneAuthDataSource): PhoneAuthRepository {
        return PhoneAuthRepositoryImpl(phoneAuthDataSource)
    }

    @Provides
    @Singleton
    fun provideUserRepository(userDataSource: UserDataSource): UserRespository {
        return UserRepositoryImpl(userDataSource)
    }


    @Provides
    @Singleton
    fun provideLocalNewsRepository(
        remoteLocalNewsDataSource: RemoteLocalNewsDataSource,
    ): LocalNewsRepository {
        return LocalNewsRepositoryImpl(
            remoteLocalNewsDataSource = remoteLocalNewsDataSource,
        )
    }

    @Provides
    @Singleton
    fun provideRemoteNewsRepository(remoteNewsDataSource: RemoteNewsDataSource): RemoteNewsRepository {
        return RemoteNewsRepositoryImpl(
            remoteNewsDataSource = remoteNewsDataSource
        )
    }

    @Provides
    @Singleton
    fun provideArticleRepository(articleDataSource: ArticleDataSource): ArticleRepository {
        return ArticleRepositoryImpl(
            articleDataSource = articleDataSource
        )
    }

    @Provides
    @Singleton
    fun provideAdsRepository(adsDataSource: AdsDataSource): AdsRepository {
        return AdsRepositoryImpl(adsDataSource)
    }

    @Provides
    @Singleton
    fun provideCityRepository(cityDataSource: CityDataSource): CityRepository {
        return CityRespositoryImpl(cityDataSource)
    }

    @Provides
    @Singleton
    fun provideAdminRepository(
        firestore: FirebaseFirestore,
        auth: FirebaseAuth,
        storage: FirebaseStorage,
    ): AdminRepository {
        return AdminRepositoryImpl(
            fireStore = firestore,
            auth = auth,
            storage = storage
        )
    }
}