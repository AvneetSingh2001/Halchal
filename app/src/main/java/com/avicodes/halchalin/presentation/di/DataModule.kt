package com.avicodes.halchalin.presentation.di

import com.avicodes.halchalin.MainActivity
import com.avicodes.halchalin.data.repository.dataSource.PhoneAuthDataSource
import com.avicodes.halchalin.data.repository.dataSource.UserDataSource
import com.avicodes.halchalin.data.repository.dataSourceImpl.PhoneAuthDataSourceImpl
import com.avicodes.halchalin.data.repository.dataSourceImpl.UserDataSourceImpl
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

}