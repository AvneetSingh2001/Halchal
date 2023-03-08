package com.avicodes.halchalin.presentation.di

import android.provider.ContactsContract.CommonDataKinds.Phone
import com.avicodes.halchalin.data.repository.*
import com.avicodes.halchalin.data.repository.dataSource.*
import com.avicodes.halchalin.domain.repository.*
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

    @Provides
    @Singleton
    fun provideUserRepository(userDataSource: UserDataSource): UserRespository {
        return UserRepositoryImpl(userDataSource)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(remoteNewsDataSource: RemoteNewsDataSource, localNewsDataSource: LocalNewsDataSource, userDataSource: UserDataSource): NewsRepository {
        return NewsRepositoryImpl(remoteNewsDataSource, localNewsDataSource, userDataSource)
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
}