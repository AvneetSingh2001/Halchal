package com.avicodes.halchalin.presentation.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.domain.repository.NewsRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.avicodes.halchalin.data.utils.Result


class HomeActivityViewModel(
    val auth: FirebaseAuth,
    private val remoteNewsRepository: NewsRepository
): ViewModel() {

    val nationalHeadlines: MutableLiveData<Result<NewsResponse>> = MutableLiveData()
    val worldHeadlines: MutableLiveData<Result<NewsResponse>> = MutableLiveData()

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

    fun logout() {
        auth.signOut()
    }
}