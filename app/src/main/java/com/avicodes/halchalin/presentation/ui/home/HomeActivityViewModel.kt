package com.avicodes.halchalin.presentation.ui.home

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.avicodes.halchalin.data.models.*
import com.google.firebase.auth.FirebaseAuth
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.*
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.Exception


class HomeActivityViewModel(
    val auth: FirebaseAuth,
    private val remoteNewsRepository: RemoteNewsRepository,
    private val localNewsRepository: LocalNewsRepository,
    private val adsRepository: AdsRepository,
    private val userRespository: UserRespository,
    private val cityRepository: CityRepository,
    private val articleRepository: ArticleRepository
) : ViewModel() {


    val nationalHeadlines: MutableLiveData<Result<PagingData<NewsRemote>>> =
        MutableLiveData(Result.NotInitialized)

    val worldHeadlines: MutableLiveData<Result<PagingData<NewsRemote>>> =
        MutableLiveData(Result.NotInitialized)

    private val _localHeadlines: MutableLiveData<Result<List<News>>> = MutableLiveData(Result.NotInitialized)
    val localHeadlines : LiveData<Result<List<News>>> = _localHeadlines

    val featuredAds: MutableLiveData<Result<List<Featured>>> =
        MutableLiveData(Result.NotInitialized)
    val linkNews: MutableLiveData<Result<String>> = MutableLiveData(Result.NotInitialized)
    val updateUserPic: MutableLiveData<Result<String>> = MutableLiveData(Result.NotInitialized)
    val comments: MutableLiveData<Result<List<CommentProcessed>>> =
        MutableLiveData(Result.NotInitialized)

    val curUser: MutableLiveData<User> = MutableLiveData()

    val linkCreated: MutableLiveData<Result<String>> = MutableLiveData(Result.NotInitialized)
    val articleLinkCreated: MutableLiveData<Result<String>> = MutableLiveData(Result.NotInitialized)
    val remoteLinkCreated: MutableLiveData<Result<String>> = MutableLiveData()
    val sharedNews: MutableLiveData<Result<News>> = MutableLiveData(Result.NotInitialized)
    val sharedArticle: MutableLiveData<Result<Article>> = MutableLiveData(Result.NotInitialized)
    val categories: MutableLiveData<Result<List<Categories>>> =
        MutableLiveData(Result.NotInitialized)
    val adsData: MutableLiveData<Result<List<ads>>> = MutableLiveData(Result.NotInitialized)
    val articleUploaded: MutableLiveData<Result<String>> = MutableLiveData(Result.NotInitialized)
    val allArticles: MutableLiveData<Result<List<ArticleProcessed>>> =
        MutableLiveData(Result.NotInitialized)
    val featuredArticles: MutableLiveData<Result<List<ArticleProcessed>>> =
        MutableLiveData(Result.NotInitialized)

    val processedArticle: MutableLiveData<Result<ArticleProcessed>> =
        MutableLiveData(Result.NotInitialized)

    val userArticles: MutableLiveData<Result<List<ArticleProcessed>>> =
        MutableLiveData(Result.NotInitialized)

    val topAds: MutableLiveData<Result<List<String>>> = MutableLiveData(Result.NotInitialized)

    var remoteIndiaNewsFetched = false
    var remoteWorldNewsFetched = false

    fun getAllTopAds() = viewModelScope.launch {
        adsRepository.getAllTopAds().collectLatest {
            when (it) {
                is Result.Success -> {
                    it.data?.let { topAdsList ->
                        val topAdsProcessed = topAdsList.map { it.imgUrl }
                        topAds.postValue(Result.Success(topAdsProcessed))
                    }
                }

                is Result.Loading -> {
                    topAds.postValue(it)
                }

                is Result.Error -> {
                    topAds.postValue(it)
                }

                else -> {}
            }
        }
    }

    fun getFeaturedArticles() = viewModelScope.launch {
        articleRepository.getFeaturedArticles().collectLatest {
            when (it) {
                is Result.Success -> {
                    it.data?.let { articleList ->
                        var articles: MutableList<ArticleProcessed> = mutableListOf()
                        for (article in articleList) {
                            val user = async { getUserById(article.userId) }.await()
                            user?.let {
                                articles.add(
                                    ArticleProcessed(
                                        article.articleId,
                                        article.articleTitle,
                                        article.articleImage,
                                        article.articleDesc,
                                        article.articleTag,
                                        user,
                                        article.date,
                                        article.commentEnabled,
                                        featured = article.featured
                                    )
                                )
                            }
                        }
                        featuredArticles.postValue(Result.Success(articles))
                    }
                }

                is Result.Loading -> {
                    featuredArticles.postValue(it)
                }

                is Result.Error -> {
                    featuredArticles.postValue(it)
                }

                else -> {}
            }

        }
    }

    fun getAllArticles() = viewModelScope.launch {
        articleRepository.getAllArticles().collectLatest {
            when (it) {
                is Result.Success -> {
                    it.data?.let { articleList ->
                        Log.e("Avneet articles vm", articleList.toString())
                        var articles: MutableList<ArticleProcessed> = mutableListOf()
                        for (article in articleList) {
                            val user = async { getUserById(article.userId) }.await()
                            user?.let {
                                articles.add(
                                    ArticleProcessed(
                                        article.articleId,
                                        article.articleTitle,
                                        article.articleImage,
                                        article.articleDesc,
                                        article.articleTag,
                                        user,
                                        article.date,
                                        article.commentEnabled,
                                        article.featured
                                    )
                                )
                            }
                        }
                        allArticles.postValue(Result.Success(articles))
                    }
                }

                is Result.Loading -> {
                    allArticles.postValue(it)
                }

                is Result.Error -> {
                    allArticles.postValue(it)
                }

                else -> {}
            }

        }
    }


    fun getUserArticles(userId: String) = viewModelScope.launch {
        articleRepository.getUserArticles(userId).collectLatest {
            when (it) {
                is Result.Success -> {
                    it.data?.let { articleList ->
                        Log.e("Avneet articles vm", articleList.toString())
                        var articles: MutableList<ArticleProcessed> = mutableListOf()
                        for (article in articleList) {
                            val user = async { getUserById(article.userId) }.await()
                            user?.let {
                                articles.add(
                                    ArticleProcessed(
                                        article.articleId,
                                        article.articleTitle,
                                        article.articleImage,
                                        article.articleDesc,
                                        article.articleTag,
                                        user,
                                        article.date,
                                        article.commentEnabled,
                                        article.featured
                                    )
                                )
                            }
                        }
                        userArticles.postValue(Result.Success(articles))
                    }
                }

                is Result.Loading -> {
                    userArticles.postValue(it)
                }

                is Result.Error -> {
                    userArticles.postValue(it)
                }

                else -> {}
            }

        }
    }

    suspend fun getArticleProcessedFromArticle(article: Article) =
        viewModelScope.launch(Dispatchers.IO) {
            processedArticle.postValue(Result.Loading("Loading"))
            try {
                val user = async { getUserById(article.userId) }.await()
                user?.let {
                    val processed = ArticleProcessed(
                        article.articleId,
                        article.articleTitle,
                        article.articleImage,
                        article.articleDesc,
                        article.articleTag,
                        user,
                        article.date,
                        article.commentEnabled,
                        article.featured
                    )
                    processedArticle.postValue(Result.Success(processed))
                }
            } catch (e: Exception) {
                processedArticle.postValue(Result.Error(e))
            }
        }

    suspend fun uploadArticle(
        title: String,
        desc: String,
        tag: String,
        imgUri: Uri,
        userId: String,
        enableComment: Boolean
    ) {
        viewModelScope.launch {
            articleRepository.uploadArticle(
                title, desc, tag, imgUri, userId, enableComment
            ).collectLatest {
                articleUploaded.postValue(it)
            }
        }
    }

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

    fun getLocalNews(loc: String)  = viewModelScope.launch {
        localNewsRepository.getNews(loc).stateIn(viewModelScope).collectLatest {
            _localHeadlines.postValue(it)
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
                                user?.let {
                                    commentProcessed.add(
                                        CommentProcessed(
                                            comment.commentId,
                                            comment.time,
                                            comment.comment,
                                            user,
                                            comment.itemId
                                        )
                                    )
                                }
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

    fun getNormaldAds() = viewModelScope.launch {
        adsRepository.getAllNormalAds().collectLatest {
            adsData.postValue(it)
        }
    }

    fun postComment(newsId: String, comment: String, userId: String): Flow<Result<String>> {
        return localNewsRepository.postComment(
            newsId = newsId,
            comment = comment,
            userId = userId
        )
    }

    suspend fun getUserById(userId: String): User? {
        return userRespository.getUserById(userId)
    }

    fun saveUserImage(image: String) = viewModelScope.launch {
        userRespository.updateUserPic(image, curUser.value?.userId!!).collectLatest {
            updateUserPic.postValue(it)
        }
    }

    fun saveUser(
        userId: String? = curUser.value?.userId,
        name: String? = curUser.value?.name,
        location: String? = curUser.value?.location,
        image: String? = curUser.value?.imgUrl,
        about: String? = curUser.value?.about
    ) {
        val user = User(
            userId = userId.toString(),
            name = name.toString(),
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

    fun createArticleDeepLink(article: ArticleProcessed) = viewModelScope.launch(Dispatchers.IO) {
        articleRepository.createArticleDynamicLink(article).collectLatest {
            articleLinkCreated.postValue(it)
        }
    }

    fun getNewsById(newsId: String) = viewModelScope.launch(Dispatchers.IO) {
        localNewsRepository.getNewsById(newsId).collectLatest {
            sharedNews.postValue(it)
        }
    }

    fun getNewsByDeepLink(newsLink: String) {
        newsLink.let {
            getNewsById(newsLink)
            linkNews.postValue(Result.Success("News"))
        }
    }

    fun getArticleByDeepLink(articleLink: String) {
        articleLink.let { articleId ->
            getArticleById(articleId)
            linkNews.postValue(Result.Success("Article"))
        }
    }

    fun getArticleById(articleId: String) = viewModelScope.launch(Dispatchers.IO) {
        articleRepository.getArticleById(articleId).collectLatest {
            sharedArticle.postValue(it)
        }
    }

    fun getCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            remoteNewsRepository.getCategories().collectLatest {
                categories.postValue(it)
            }
        }
    }

    fun deleteComment(commentId: String): Flow<Result<String>> {
        return localNewsRepository.deleteComment(commentId)
    }

    fun deleteArticle(articleId: String) : Flow<Result<String>> {
        return articleRepository.deleteArticle(articleId = articleId)
    }
}