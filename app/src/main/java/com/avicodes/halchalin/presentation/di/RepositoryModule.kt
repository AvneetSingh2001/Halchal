package com.avicodes.halchalin.presentation.di

import android.provider.ContactsContract.CommonDataKinds.Phone
import com.avicodes.halchalin.data.repository.PhoneAuthRepositoryImpl
import com.avicodes.halchalin.data.repository.dataSource.PhoneAuthDataSource
import com.avicodes.halchalin.domain.repository.PhoneAuthRepository
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
    fun providePhoneAuthRepository(phoneAuthDataSource: PhoneAuthDataSource) : PhoneAuthRepository {
        return PhoneAuthRepositoryImpl(phoneAuthDataSource)
    }

}