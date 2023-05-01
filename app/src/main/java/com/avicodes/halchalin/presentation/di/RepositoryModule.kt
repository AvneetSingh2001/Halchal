package com.avicodes.halchalin.presentation.di

import com.avicodes.halchalin.data.repository.*
import com.avicodes.halchalin.data.repository.ads.featured.AdsDataSource
import com.avicodes.halchalin.data.repository.ads.featured.AdsRepositoryImpl
import com.avicodes.halchalin.data.repository.auth.PhoneAuthDataSource
import com.avicodes.halchalin.data.repository.auth.PhoneAuthRepositoryImpl
import com.avicodes.halchalin.data.repository.news.remote.category.CategoryNewsRepositoryImpl
import com.avicodes.halchalin.data.repository.news.remote.category.dataSource.RemoteCategoryNewsDataSource
import com.avicodes.halchalin.data.repository.news.remote.international.InternationalNewsRepositoryImpl
import com.avicodes.halchalin.data.repository.news.remote.international.dataSource.CacheInternationalNewsDataSource
import com.avicodes.halchalin.data.repository.news.remote.international.dataSource.RemoteInternationalNewsDataSource
import com.avicodes.halchalin.data.repository.news.local.LocalNewsRepositoryImpl
import com.avicodes.halchalin.data.repository.news.local.dataSource.CacheLocalNewsDataSource
import com.avicodes.halchalin.data.repository.news.local.dataSource.RemoteLocalNewsDataSource
import com.avicodes.halchalin.data.repository.news.remote.national.NationalNewsRepositoryImpl
import com.avicodes.halchalin.data.repository.news.remote.national.dataSource.CacheNationalNewsDataSource
import com.avicodes.halchalin.data.repository.news.remote.national.dataSource.RemoteNationalNewsDataSource
import com.avicodes.halchalin.data.repository.settings.city.CityDataSource
import com.avicodes.halchalin.data.repository.settings.city.CityRespositoryImpl
import com.avicodes.halchalin.data.repository.settings.user.UserDataSource
import com.avicodes.halchalin.data.repository.settings.user.UserRepositoryImpl
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
    fun provideLocalNewsRepository(
        remoteLocalNewsDataSource: RemoteLocalNewsDataSource,
        cacheLocalNewsDataSource: CacheLocalNewsDataSource
    ): LocalNewsRepository {
        return LocalNewsRepositoryImpl(
            remoteLocalNewsDataSource = remoteLocalNewsDataSource,
            cacheLocalNewsDataSource = cacheLocalNewsDataSource
        )
    }

    @Provides
    @Singleton
    fun provideNationalNewsRepository(cacheNationalNewsDataSource: CacheNationalNewsDataSource, remoteNationalNewsDataSource: RemoteNationalNewsDataSource): NationalNewsRepository {
        return NationalNewsRepositoryImpl(
            cacheNationalNewsDataSource = cacheNationalNewsDataSource,
            remoteNationalNewsDataSource = remoteNationalNewsDataSource
        )
    }

    @Provides
    @Singleton
    fun provideInternationalNewsRepository(cacheInternationalNewsDataSource: CacheInternationalNewsDataSource, remoteInternationalNewsDataSource: RemoteInternationalNewsDataSource): InternationalNewsRepository {
        return InternationalNewsRepositoryImpl(
            cacheInternationalNewsDataSource = cacheInternationalNewsDataSource ,
            remoteInternationalNewsDataSource = remoteInternationalNewsDataSource
        )
    }

    @Provides
    @Singleton
    fun provideCategoryNewsRepository(remoteCategoryNewsDataSource: RemoteCategoryNewsDataSource): CategoryNewsRepository {
        return CategoryNewsRepositoryImpl(
            remoteCategoryNewsDataSource = remoteCategoryNewsDataSource
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
}