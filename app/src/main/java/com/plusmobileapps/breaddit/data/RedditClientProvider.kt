package com.plusmobileapps.breaddit.data

import io.reactivex.Observable
import net.dean.jraw.RedditClient
import net.dean.jraw.http.OkHttpNetworkAdapter
import net.dean.jraw.http.UserAgent
import net.dean.jraw.oauth.Credentials
import net.dean.jraw.oauth.OAuthHelper
import java.util.*

private const val AUTH_URL = "https://www.reddit.com/api/v1/authorize.compact?client_id=%s" +
        "&response_type=code&state=%s&redirect_uri=%s&" +
        "duration=permanent&scope=identity"
private const val CLIENT_ID = "kV9k939bhxpvyg"
private const val REDIRECT_URI = "https://plusmobileapps.com/breaddit"
private const val STATE = "MY_RANDOM_STRING_1"
private const val ACCESS_TOKEN_URL = "https://www.reddit.com/api/v1/access_token"

class RedditClientProvider {

    fun redditClient(): Observable<RedditClient> {

        return Observable.create<RedditClient> {
            val credentials = Credentials.userlessApp(CLIENT_ID, UUID.randomUUID())
            val userAgent = UserAgent("android", "com.plusmobileapps.breaddit", "0.0.1", "u/coffeemaxed")
            val adapter = OkHttpNetworkAdapter(userAgent)
            val client = OAuthHelper.automatic(adapter, credentials)
            it.onNext(client)
            it.onComplete()
        }
    }
}