package com.plusmobileapps.breaddit.viewmodels

import androidx.lifecycle.ViewModel
import com.plusmobileapps.breaddit.data.RedditPost
import com.plusmobileapps.breaddit.data.RedditPostRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel(private val repository: RedditPostRepository) : ViewModel() {

    private val breadSubreddits = listOf(
        "breddit",
        "Breadit",
        "BreadStapledToTrees",
        "GarlicBreadMemes"
    )

    val redditPosts by lazy {
        val subredditPosts: List<Single<List<RedditPost>>> = breadSubreddits.map {
            repository.posts(it)
        }

        Single.merge(subredditPosts)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}