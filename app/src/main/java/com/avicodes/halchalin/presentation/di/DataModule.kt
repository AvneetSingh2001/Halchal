package com.avicodes.halchalin.presentation.di

import com.avicodes.halchalin.data.API.NewsApiService
import com.avicodes.halchalin.data.prefs.UserPrefs
import com.avicodes.halchalin.data.repository.ads.AdsDataSouceImpl
import com.avicodes.halchalin.data.repository.ads.AdsDataSource
import com.avicodes.halchalin.data.repository.article.ArticleDataSource
import com.avicodes.halchalin.data.repository.article.ArticleDataSourceImpl
import com.avicodes.halchalin.data.repository.auth.PhoneAuthDataSource
import com.avicodes.halchalin.data.repository.auth.PhoneAuthDataSourceImpl
import com.avicodes.halchalin.data.repository.news.local.RemoteLocalNewsDataSource
import com.avicodes.halchalin.data.repository.news.local.RemoteLocalNewsDataSourceImpl
import com.avicodes.halchalin.data.repository.news.remote.RemoteNewsDataSource
import com.avicodes.halchalin.data.repository.news.remote.RemoteNewsDataSourceImpl
import com.avicodes.halchalin.data.repository.settings.city.CityDataSource
import com.avicodes.halchalin.data.repository.settings.city.CityDataSourceImpl
import com.avicodes.halchalin.data.repository.settings.user.UserDataSource
import com.avicodes.halchalin.data.repository.settings.user.UserDataSourceImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DataModule {

    @Provides
    @Singleton
    fun providePhoneAuthDataSource(
        auth: FirebaseAuth,
    ): PhoneAuthDataSource {
        return PhoneAuthDataSourceImpl(auth)
    }

    @Provides
    @Singleton
    fun provideArticleDataSource(
        userPrefs: UserPrefs,
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
    ): ArticleDataSource {
        return ArticleDataSourceImpl(
            userPrefs = userPrefs,
            firestore = firestore,
            storage = storage
        )
    }

    @Provides
    @Singleton
    fun provideUserDataSource(
        auth: FirebaseAuth,
        firestoreDb: FirebaseFirestore,
        storage: FirebaseStorage,
        userPrefs: UserPrefs
    ): UserDataSource {
        return UserDataSourceImpl(
            auth, firestoreDb, storage, userPrefs
        )
    }

    @Provides
    @Singleton
    fun provideNewsDataSource(
        firestore: FirebaseFirestore,
        newsApiService: NewsApiService
    ): RemoteNewsDataSource {
        return RemoteNewsDataSourceImpl(
            newsApiService,
            firestore
        )
    }

    @Provides
    @Singleton
    fun provideRemoteLocalNewsDataSource(
        auth: FirebaseAuth,
        firestoreDb: FirebaseFirestore,
        storage: FirebaseStorage
    ): RemoteLocalNewsDataSource {
        return RemoteLocalNewsDataSourceImpl(
            auth, firestoreDb, storage
        )
    }


    @Provides
    @Singleton
    fun provideAdsDataSource(firestoreDb: FirebaseFirestore, storage: FirebaseStorage): AdsDataSource {
        return AdsDataSouceImpl(
            firestoreDb,
            storage
        )
    }

    @Provides
    @Singleton
    fun provideCityDataSource(firestoreDb: FirebaseFirestore): CityDataSource {
        return CityDataSourceImpl(
            firestoreDb
        )
    }

}