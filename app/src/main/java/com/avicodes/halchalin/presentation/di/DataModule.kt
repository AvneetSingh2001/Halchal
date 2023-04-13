package com.avicodes.halchalin.presentation.di

import com.avicodes.halchalin.MainActivity
import com.avicodes.halchalin.data.API.NewsApiService
import com.avicodes.halchalin.data.prefs.UserPrefs
import com.avicodes.halchalin.data.repository.ads.featured.AdsDataSouceImpl
import com.avicodes.halchalin.data.repository.ads.featured.AdsDataSource
import com.avicodes.halchalin.data.repository.auth.PhoneAuthDataSource
import com.avicodes.halchalin.data.repository.auth.PhoneAuthDataSourceImpl
import com.avicodes.halchalin.data.repository.news.category.CategoryNewsRepositoryImpl
import com.avicodes.halchalin.data.repository.news.category.dataSource.RemoteCategoryNewsDataSource
import com.avicodes.halchalin.data.repository.news.category.dataSourceImpl.RemoteCategoryNewsDataSourceImpl
import com.avicodes.halchalin.data.repository.news.international.dataSource.CacheInternationalNewsDataSource
import com.avicodes.halchalin.data.repository.news.international.dataSource.RemoteInternationalNewsDataSource
import com.avicodes.halchalin.data.repository.news.international.dataSourceImpl.CacheInternationalNewsDataSourceImpl
import com.avicodes.halchalin.data.repository.news.international.dataSourceImpl.RemoteInternationalNewsDataSourceImpl
import com.avicodes.halchalin.data.repository.news.local.dataSource.CacheLocalNewsDataSource
import com.avicodes.halchalin.data.repository.news.local.dataSource.RemoteLocalNewsDataSource
import com.avicodes.halchalin.data.repository.news.local.dataSourceImpl.CacheLocalNewsDataSourceImpl
import com.avicodes.halchalin.data.repository.news.local.dataSourceImpl.RemoteLocalNewsDataSourceImpl
import com.avicodes.halchalin.data.repository.news.national.dataSource.CacheNationalNewsDataSource
import com.avicodes.halchalin.data.repository.news.national.dataSource.RemoteNationalNewsDataSource
import com.avicodes.halchalin.data.repository.news.national.dataSourceImpl.CacheNationalNewsDataSourceImpl
import com.avicodes.halchalin.data.repository.news.national.dataSourceImpl.RemoteNationalNewsDataSourceImpl
import com.avicodes.halchalin.data.repository.settings.city.CityDataSource
import com.avicodes.halchalin.data.repository.settings.city.CityDataSourceImpl
import com.avicodes.halchalin.data.repository.settings.user.UserDataSource
import com.avicodes.halchalin.data.repository.settings.user.UserDataSourceImpl
import com.avicodes.halchalin.domain.repository.CategoryNewsRepository
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
    fun providePhoneAuthDataSource(auth: FirebaseAuth, activity: MainActivity) : PhoneAuthDataSource {
        return PhoneAuthDataSourceImpl(auth, activity)
    }

    @Provides
    @Singleton
    fun provideUserDataSource(auth:FirebaseAuth, firestoreDb: FirebaseFirestore, storage: FirebaseStorage, userPrefs: UserPrefs) : UserDataSource {
        return UserDataSourceImpl(
            auth, firestoreDb, storage, userPrefs
        )
    }

    @Provides
    @Singleton
    fun provideCategoryNewsDataSource(firestore: FirebaseFirestore, newsApiService: NewsApiService): RemoteCategoryNewsDataSource {
        return RemoteCategoryNewsDataSourceImpl(
            newsApiService,
            firestore
        )
    }

    @Provides
    @Singleton
    fun provideRemoteInternationalNewsDataSource(newsApiService: NewsApiService): RemoteInternationalNewsDataSource {
        return RemoteInternationalNewsDataSourceImpl(
            newsApiService,
        )
    }

    @Provides
    @Singleton
    fun provideCacheInternationalNewsDataSource(): CacheInternationalNewsDataSource {
        return CacheInternationalNewsDataSourceImpl()
    }

    @Provides
    @Singleton
    fun provideRemoteNationalNewsDataSource(newsApiService: NewsApiService): RemoteNationalNewsDataSource {
        return RemoteNationalNewsDataSourceImpl(
            newsApiService,
        )
    }

    @Provides
    @Singleton
    fun provideCacheNationalNewsDataSource(): CacheNationalNewsDataSource {
        return CacheNationalNewsDataSourceImpl()
    }

    @Provides
    @Singleton
    fun provideRemoteLocalNewsDataSource(auth: FirebaseAuth,firestoreDb: FirebaseFirestore, userPrefs: UserPrefs) : RemoteLocalNewsDataSource {
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