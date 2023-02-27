package com.avicodes.halchalin.presentation.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avicodes.halchalin.data.models.FeaturedAds
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.domain.repository.NewsRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.AdsRepository
import kotlinx.coroutines.flow.collectLatest


class HomeActivityViewModel(
    val auth: FirebaseAuth,
    private val remoteNewsRepository: NewsRepository,
    private val adsRepository: AdsRepository
): ViewModel() {

    val nationalHeadlines: MutableLiveData<Result<NewsResponse>> = MutableLiveData(Result.NotInitialized)
    val worldHeadlines: MutableLiveData<Result<NewsResponse>> = MutableLiveData(Result.NotInitialized)
    val localHeadlines: MutableLiveData<Result<List<News>>> = MutableLiveData(Result.NotInitialized)
    val featuredAds: MutableLiveData<Result<List<FeaturedAds>>> = MutableLiveData()
    val exploreNewsTab: MutableLiveData<Result<Int>> = MutableLiveData(Result.NotInitialized)

    val likesCount: MutableLiveData<Int> = MutableLiveData()

    fun getNationalNewsHeadlines(
        country: String,
        lang: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        nationalHeadlines.postValue(Result.Loading("Loading"))

        try {
            val apiResult = remoteNewsRepository.getNationalHeadlines(
                country = country,
                lang = lang
            )
            nationalHeadlines.postValue(apiResult)
        }catch (e: Exception) {
            nationalHeadlines.postValue(Result.Error(e))
        }

    }

    fun getWorldNewsHeadlines(
        topic: String,
        country: String,
        lang: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        worldHeadlines.postValue(Result.Loading("Loading"))

        try {
            val apiResult = remoteNewsRepository.getTopicHeadlines(
                topic = topic,
                country = country,
                lang = lang
            )
            worldHeadlines.postValue(apiResult)
        }catch (e: Exception) {
            worldHeadlines.postValue(Result.Error(e))
        }
    }

    fun getLocalNews(
        location: String
    ) = viewModelScope.launch{
        remoteNewsRepository.getLocalNews(location).collectLatest {
            localHeadlines.postValue(it)
        }
    }

    fun getFeaturedAds() = viewModelScope.launch {
        adsRepository.getAllFeaturedAds().collectLatest {
            featuredAds.postValue(it)
        }
    }

    fun logout() {
        auth.signOut()
    }
}