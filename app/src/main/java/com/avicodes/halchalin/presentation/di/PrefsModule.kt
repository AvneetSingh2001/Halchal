package com.avicodes.halchalin.presentation.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.avicodes.halchalin.data.prefs.UserPrefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PrefsModule {

    @Provides
    @Singleton
    fun provideUserDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = {
                appContext.preferencesDataStoreFile("USER_PREFS")
            }
        )

    @Provides
    @Singleton
    fun provideUserPrefs(dataStore: DataStore<Preferences>): UserPrefs {
        return UserPrefs(dataStore)
    }
}