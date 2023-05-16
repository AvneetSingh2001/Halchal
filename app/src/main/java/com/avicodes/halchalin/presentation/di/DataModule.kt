package com.avicodes.halchalin.presentation.di

import android.net.Uri
import com.avicodes.halchalin.MainActivity
import com.avicodes.halchalin.data.API.NewsApiService
import com.avicodes.halchalin.data.prefs.UserPrefs
import com.avicodes.halchalin.data.repository.ads.featured.AdsDataSouceImpl
import com.avicodes.halchalin.data.repository.ads.featured.AdsDataSource
import com.avicodes.halchalin.data.repository.article.ArticleDataSource
import com.avicodes.halchalin.data.repository.article.ArticleDataSourceImpl
import com.avicodes.halchalin.data.repository.auth.PhoneAuthDataSource
import com.avicodes.halchalin.data.repository.auth.PhoneAuthDataSourceImpl
import com.avicodes.halchalin.data.repository.news.local.dataSource.CacheLocalNewsDataSource
import com.avicodes.halchalin.data.repository.news.local.dataSource.RemoteLocalNewsDataSource
import com.avicodes.halchalin.data.repository.news.local.dataSourceImpl.CacheLocalNewsDataSourceImpl
import com.avicodes.halchalin.data.repository.news.local.dataSourceImpl.RemoteLocalNewsDataSourceImpl
import com.avicodes.halchalin.data.repository.news.remote.dataSource.RemoteNewsDataSource
import com.avicodes.halchalin.data.repository.news.remote.dataSourceImpl.RemoteNewsDataSourceImpl
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
        activity: MainActivity
    ): PhoneAuthDataSource {
        return PhoneAuthDataSourceImpl(auth, activity)
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
        userPrefs: UserPrefs
    ): RemoteLocalNewsDataSource {
        return RemoteLocalNewsDataSourceImpl(
            auth, firestoreDb, userPrefs
        )
    }

    @Provides
    @Singleton
    fun provideCacheLocalNewsDataSource(): CacheLocalNewsDataSource {
        return CacheLocalNewsDataSourceImpl()
    }

    @Provides
    @Singleton
    fun provideAdsDataSource(firestoreDb: FirebaseFirestore): AdsDataSource {
        return AdsDataSouceImpl(
            firestoreDb
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