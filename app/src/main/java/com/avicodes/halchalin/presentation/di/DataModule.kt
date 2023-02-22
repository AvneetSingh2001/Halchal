package com.avicodes.halchalin.presentation.di

import com.avicodes.halchalin.MainActivity
import com.avicodes.halchalin.data.API.NewsApiService
import com.avicodes.halchalin.data.repository.dataSource.*
import com.avicodes.halchalin.data.repository.dataSourceImpl.*
import com.bumptech.glide.load.DataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    fun provideUserDataSource(auth:FirebaseAuth, firestoreDb: FirebaseFirestore) : UserDataSource{
        return UserDataSourceImpl(
            auth, firestoreDb
        )
    }


    @Provides
    @Singleton
    fun provideRemoteNewsDataSource(newsApiService: NewsApiService): RemoteNewsDataSource {
        return RemoteNewsDataSourceImpl(newsApiService)
    }

    @Provides
    @Singleton
    fun provideLocalNewsDataSource(firestoreDb: FirebaseFirestore) : LocalNewsDataSource{
        return LocalNewsDataSourceImpl(firestoreDb)
    }

    @Provides
    @Singleton
    fun provideAdsDataSource(firestoreDb: FirebaseFirestore): AdsDataSource {
        return AdsDataSouceImpl(firestoreDb)
    }

}