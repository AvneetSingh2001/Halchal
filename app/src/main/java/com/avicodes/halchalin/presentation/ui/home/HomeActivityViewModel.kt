package com.avicodes.halchalin.presentation.ui.home

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.avicodes.halchalin.data.models.*
import com.google.firebase.auth.FirebaseAuth
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.*
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.GetUserByIdUseCase
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.GetUserUseCase
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.updateUserPicUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.Exception
import kotlin.math.exp


class HomeActivityViewModel(
    val auth: FirebaseAuth,
    private val remoteNewsRepository: RemoteNewsRepository,
    private val localNewsRepository: LocalNewsRepository,
    private val adsRepository: AdsRepository,
    private val updateUserPicUseCase: updateUserPicUseCase,
    private val userRespository: UserRespository,
    private val cityRepository: CityRepository
) : ViewModel() {

    val nationalHeadlines: MutableLiveData<Result<PagingData<NewsRemote>>> =
        MutableLiveData(Result.NotInitialized)

    val worldHeadlines: MutableLiveData<Result<PagingData<NewsRemote>>> =
        MutableLiveData(Result.NotInitialized)

    val localHeadlines: MutableLiveData<Result<List<News>>> = MutableLiveData(Result.NotInitialized)
    val featuredAds: MutableLiveData<Result<List<Featured>>> = MutableLiveData()
    val linkNews: MutableLiveData<Result<String>> = MutableLiveData(Result.NotInitialized)
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


    suspend fun getNationalNewsHeadlines(
        topic: String,
        country: String,
        lang: String,
    ) {
        remoteNewsRepository.getNews(
            lang = lang,
            topic = topic,
            country = country
        ).cachedIn(viewModelScope).collectLatest {
            nationalHeadlines.postValue(Result.Success(it))
        }
    }

    suspend fun getInternationalNewsHeadlines(
        topic: String,
        country: String,
        lang: String,
    ) {
        remoteNewsRepository.getNews(
            lang = lang,
            topic = topic,
            country = country
        ).cachedIn(viewModelScope).collectLatest {
            worldHeadlines.postValue(Result.Success(it))
        }
    }

    fun getCategoryNewsHeadlines(
        topic: String,
        country: String,
        lang: String,
    ): Flow<PagingData<NewsRemote>> {
        return remoteNewsRepository.getNews(
            lang = lang,
            topic = topic,
            country = country
        ).cachedIn(viewModelScope)
    }

    fun getLocalNews(
    ) = viewModelScope.launch {
        val loc = curUser.value?.location
        localNewsRepository.getNews(loc.toString())
        localNewsRepository.news.collectLatest {
            localHeadlines.postValue(it)
        }
    }

    fun updateLocalNews() = viewModelScope.launch(Dispatchers.IO) {
        val loc = curUser.value?.location
        localNewsRepository.updateNews(loc.toString())
        localNewsRepository.news.collectLatest {
            localHeadlines.postValue(it)
        }
    }

    fun getComments(
        newsId: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        localNewsRepository.getComment(newsId)
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
            localNewsRepository.postComment(
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
        localNewsRepository.createDynamicLink(news).collectLatest {
            linkCreated.postValue(it)
        }
    }

    fun getNewsById(newsId: String) = viewModelScope.launch(Dispatchers.IO) {
        localNewsRepository.getNewsById(newsId).collectLatest {
            sharedNews.postValue(it)
        }
    }

    fun getNewsByDeepLink(newsLink: String) {
        newsLink.let {
            Log.e("DeepLink: ", newsLink)
            var newsUri = newsLink.toUri()
            var newsId = newsUri.getQueryParameter("news")
            newsId?.let {
                getNewsById(newsId)
                linkNews.postValue(Result.Success("DeepLink"))
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