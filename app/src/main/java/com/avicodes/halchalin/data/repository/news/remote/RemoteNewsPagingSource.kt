package com.avicodes.halchalin.data.repository.news.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.avicodes.halchalin.data.API.NewsApiService
import com.avicodes.halchalin.data.models.Categories
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.models.NewsResponse
import com.bumptech.glide.load.HttpException
import java.io.IOException

class RemoteNewsPagingSource(
    private val service: NewsApiService,
    private var topic: String,
    private var lang: String,
    private var country: String,
    private var category: String,
) : PagingSource<String, NewsRemote>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, NewsRemote> {
        val pageIndex = params.key ?: "null"
        return try {
            if (category == "national") {
                val response = service.getTopHeadlines(
                    country = country,
                    lang = lang,
                    page = pageIndex,
                )

                if (response.isSuccessful && response.body() != null) {
                    val pageResponse = response.body()
                    val news = pageResponse?.results ?: ArrayList()
                    val nextKey = pageResponse?.nextPage
                    LoadResult.Page(
                        data = news,
                        prevKey = null,
                        nextKey = nextKey
                    )
                } else {
                    LoadResult.Page(
                        data = ArrayList(),
                        prevKey = null,
                        nextKey = null
                    )
                }

            } else {
                val response = service.getTopicHeadlines(
                    topic = topic,
                    country = country,
                    lang = lang,
                    page = pageIndex,
                )

                Log.e("Avneet Data Source", response.body()?.results.toString())
                if (response.isSuccessful && response.body() != null) {
                    val pageResponse = response.body()
                    val news = pageResponse?.results ?: ArrayList()
                    val nextKey = pageResponse?.nextPage
                    LoadResult.Page(
                        data = news,
                        prevKey = null,
                        nextKey = nextKey
                    )
                } else {
                    LoadResult.Page(
                        data = ArrayList(),
                        prevKey = null,
                        nextKey = null
                    )
                }

            }
        } catch (exception: IOException) {
            Log.e("Avneet Error", exception.toString())
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            Log.e("Avneet Error", exception.toString())
            return LoadResult.Error(exception)
        }
    }

    /**
     * The refresh key is used for subsequent calls to PagingSource.Load after the initial load.
     */
    override fun getRefreshKey(state: PagingState<String, NewsRemote>): String? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index.
        return null
    }
}