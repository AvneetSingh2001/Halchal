package com.avicodes.halchalin.presentation.di

import com.avicodes.halchalin.MainActivity
import com.avicodes.halchalin.data.API.NewsApiService
import com.avicodes.halchalin.data.prefs.UserPrefs
import com.avicodes.halchalin.data.repository.dataSource.*
import com.avicodes.halchalin.data.repository.dataSourceImpl.*
import com.bumptech.glide.load.DataSource
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
    fun provideUserDataSource(auth:FirebaseAuth, firestoreDb: FirebaseFirestore, storage: FirebaseStorage, userPrefs: UserPrefs) : UserDataSource{
        return UserDataSourceImpl(
            auth, firestoreDb, storage, userPrefs
        )
    }


    @Provides
    @Singleton
    fun provideRemoteNewsDataSource(firestore: FirebaseFirestore, newsApiService: NewsApiService): RemoteNewsDataSource {
        return RemoteNewsDataSourceImpl(
            newsApiService,
            firestore
        )
    }

    @Provides
    @Singleton
    fun provideLocalNewsDataSource(auth: FirebaseAuth,firestoreDb: FirebaseFirestore, userPrefs: UserPrefs) : LocalNewsDataSource{
        return LocalNewsDataSourceImpl(
            auth, firestoreDb, userPrefs
        )
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