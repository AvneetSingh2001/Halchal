package com.avicodes.halchalin.presentation.ui.home

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.avicodes.halchalin.data.models.*
import com.avicodes.halchalin.domain.repository.NewsRepository
import com.google.firebase.auth.FirebaseAuth
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.AdsRepository
import com.avicodes.halchalin.domain.repository.CityRepository
import com.avicodes.halchalin.domain.repository.UserRespository
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.GetUserByIdUseCase
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.GetUserUseCase
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.updateUserPicUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.transform
import kotlin.math.exp


class HomeActivityViewModel(
    val auth: FirebaseAuth,
    private val remoteNewsRepository: NewsRepository,
    private val adsRepository: AdsRepository,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val updateUserPicUseCase: updateUserPicUseCase,
    private val userRespository: UserRespository,
    private val cityRepository: CityRepository
) : ViewModel() {

    val nationalHeadlines: MutableLiveData<Result<NewsResponse>> =
        MutableLiveData(Result.NotInitialized)
    val worldHeadlines: MutableLiveData<Result<NewsResponse>> =
        MutableLiveData(Result.NotInitialized)
    val categoryHeadlines: MutableLiveData<Result<NewsResponse>> =
        MutableLiveData(Result.NotInitialized)
    val localHeadlines: MutableLiveData<Result<List<News>>> = MutableLiveData(Result.NotInitialized)
    val featuredAds: MutableLiveData<Result<List<Featured>>> = MutableLiveData()
    val linkNews: MutableLiveData<Result<News>> = MutableLiveData(Result.NotInitialized)
    val updateUserPic: MutableLiveData<Result<String>> = MutableLiveData(Result.NotInitialized)
    val commentUpdated: MutableLiveData<Result<String>> = MutableLiveData()
    val comments: MutableLiveData<Result<List<CommentProcessed>>> =
        MutableLiveData(Result.NotInitialized)
    val curUser: MutableLiveData<User?> = MutableLiveData()
    val linkCreated: MutableLiveData<Result<String>> = MutableLiveData()
    val remoteLinkCreated: MutableLiveData<Result<String>> = MutableLiveData()
    val sharedNews: MutableLiveData<Result<News>> = MutableLiveData()
    val categories: MutableLiveData<Result<List<Categories>>> =
        MutableLiveData(Result.NotInitialized)

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
        } catch (e: Exception) {
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
        } catch (e: Exception) {
            worldHeadlines.postValue(Result.Error(e))
        }
    }

    fun getCategoryNewsHeadlines(
        topic: String,
        country: String,
        lang: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        categoryHeadlines.postValue(Result.Loading("Loading"))
        try {
            val apiResult = remoteNewsRepository.getTopicHeadlines(
                topic = topic,
                country = country,
                lang = lang
            )
            categoryHeadlines.postValue(apiResult)
        } catch (e: Exception) {
            categoryHeadlines.postValue(Result.Error(e))
        }
    }

    fun getLocalNews(
    ) = viewModelScope.launch {
        val loc = curUser.value?.location
        remoteNewsRepository.getLocalNews(loc.toString()).collectLatest {
            localHeadlines.postValue(it)
            Log.e("Avneet Local viewmodel", it.toString())
        }
    }

    fun getComments(
        newsId: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        remoteNewsRepository.getComment(newsId)
            .collectLatest {
                when (it) {
                    is Result.Success -> {
                        it.data?.let { commentsList ->
                            var commentProcessed: MutableList<CommentProcessed> = mutableListOf()
                            for (comment in commentsList) {
                                val user = async { getUserById(comment.userId) }.await()
                                commentProcessed.add(
                                    CommentProcessed(
                                        comment.time,
                                        comment.comment,
                                        user!!
                                    )
                                )
                            }
                            comments.postValue(Result.Success(commentProcessed))
                        }
                    }
                    is Result.Loading -> {
                        comments.postValue(it)
                    }
                    is Result.Error -> {
                        comments.postValue(it)
                    }
                    else -> {}
                }

            }
    }

    fun getFeaturedAds() = viewModelScope.launch {
        adsRepository.getAllFeaturedAds().collectLatest {
            featuredAds.postValue(it)
        }
    }

    fun postComment(newsId: String, comment: String) {
        viewModelScope.launch(Dispatchers.IO) {
            remoteNewsRepository.postComment(
                newsId = newsId,
                comment = comment,
            ).collectLatest {
                commentUpdated.postValue(it)
            }
        }
    }

    suspend fun getUserById(userId: String): User? {
        return userRespository.getUserById(userId)
    }

    fun saveUserImage(image: String) = viewModelScope.launch {
        updateUserPicUseCase.execute(image, curUser.value?.userId!!).collectLatest {
            updateUserPic.postValue(it)
        }
    }

    fun saveUser(
        userId: String? = curUser.value?.userId,
        name: String? = curUser.value?.name,
        phone: String? = curUser.value?.mobile,
        location: String? = curUser.value?.location,
        image: String? = curUser.value?.imgUrl,
        about: String? = curUser.value?.about
    ) {
        val user = User(
            userId = userId.toString(),
            name = name.toString(),
            mobile = phone.toString(),
            location = location.toString(),
            imgUrl = image.toString(),
            about = about.toString()
        )
        viewModelScope.launch(Dispatchers.IO) {
            userRespository.saveUser(user)
        }
    }

    fun getUserLocally() {
        viewModelScope.launch(Dispatchers.IO) {
            userRespository.getUserLocally().collectLatest {
                curUser.postValue(it)
            }
        }
    }

    fun logout() = viewModelScope.launch(Dispatchers.IO) {
        userRespository.logout()
        auth.signOut()
    }

    fun getUser(uid: String) = liveData<Result<User>> {
        userRespository.getUserRemotely(uid).collectLatest {
            emit(it)
        }
    }

    fun getCities() = liveData {
        cityRepository.getAllCities().collectLatest {
            emit(it)
        }
    }

    fun createDeepLink(news: News) = viewModelScope.launch(Dispatchers.IO) {
        remoteNewsRepository.createDynamicLink(news).collectLatest {
            linkCreated.postValue(it)
        }
    }

    fun createRemoteDeepLink(newsRemote: NewsRemote) = viewModelScope.launch(Dispatchers.IO) {
        remoteNewsRepository.createRemoteNewsDynamicLink(newsRemote).collectLatest {
            remoteLinkCreated.postValue(it)
        }
    }

    fun getNewsById(newsId: String) = viewModelScope.launch(Dispatchers.IO) {
        remoteNewsRepository.getNewsById(newsId).collectLatest {
            sharedNews.postValue(it)
        }
    }

    fun getNewsByDeepLink(newsLink: String) {
        newsLink.let {
            Log.e("DeepLink: ", newsLink)
            var newsUri = newsLink.toUri()
            var newsId = newsUri.getQueryParameter("news")
            newsId?.let {
                getLocalNews()
                getNewsById(newsId)
                linkNews.postValue(Result.Success(News()))
            }
        }
    }

    fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            remoteNewsRepository.getCategories().collectLatest {
                categories.postValue(it)
            }
        }
    }
}