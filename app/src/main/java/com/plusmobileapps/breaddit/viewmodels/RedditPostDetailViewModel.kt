package com.plusmobileapps.breaddit.viewmodels

import androidx.lifecycle.ViewModel
import com.plusmobileapps.breaddit.data.RedditPost
import com.plusmobileapps.breaddit.data.RedditPostRepository
import io.reactivex.Observable

class RedditPostDetailViewModel(private val redditPostRepository: RedditPostRepository) : ViewModel() {

    fun loadRedditPost(id: String): Observable<RedditPost> {
        return redditPostRepository.getRedditPost(id)
    }

}