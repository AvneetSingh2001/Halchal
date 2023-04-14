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
import com.google.firebase.auth.FirebaseAuth
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.*
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
    private val categoryNewsRepository: CategoryNewsRepository,
    private val internationalNewsRepository: InternationalNewsRepository,
    private val localNewsRepository: LocalNewsRepository,
    private val nationalNewsRepository: NationalNewsRepository,
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

    fun getNationalNewsHeadlines(
    ) = viewModelScope.launch(Dispatchers.IO) {
        nationalNewsRepository.getNews()
        nationalNewsRepository.news.collectLatest {
            nationalHeadlines.postValue(it)
        }
    }

    fun getInternationalNewsHeadlines(
    ) = viewModelScope.launch(Dispatchers.IO) {
        internationalNewsRepository.getNews()
        internationalNewsRepository.news.collectLatest {
            worldHeadlines.postValue(it)
        }
    }

    fun getCategoryNewsHeadlines(
        topic: String,
        country: String,
        lang: String,
        page: String?
    ) = viewModelScope.launch(Dispatchers.IO) {
        categoryHeadlines.postValue(Result.Loading("Started Loading"))
        categoryNewsRepository.getNews(
            topic = topic,
            country = country,
            lang = lang,
            page = page
        )
        categoryNewsRepository.news.collectLatest {
            categoryHeadlines.postValue(it)
        }
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

    fun updateInternationalNews(page: String?) = viewModelScope.launch {
        internationalNewsRepository.updateNews(page)
        internationalNewsRepository.news.collectLatest {
            worldHeadlines.postValue(it)
        }
    }

    fun updateNationalNews(page: String?) = viewModelScope.launch {
        nationalNewsRepository.updateNews(page)
        nationalNewsRepository.news.collectLatest {
            nationalHeadlines.postValue(it)
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
            categoryNewsRepository.getCategories().collectLatest {
                categories.postValue(it)
            }
        }
    }


}