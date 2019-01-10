package com.plusmobileapps.breaddit.data

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface RedditApi {

    @GET("r/{sub}.json")
    fun getFeed(@Path("sub") subReddit: String): Observable<RedditFeedResponse>
}