package com.plusmobileapps.breaddit.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.reactivex.Observable
import io.reactivex.Single

class RedditPostRepository(val apiFactory: RedditApiFactory, val dao: RedditPostDao) {

    private val breadSubreddits = listOf(
        "breddit",
        "Breadit",
        "BreadStapledToTrees",
        "GarlicBreadMemes"
    )

    fun load(): Single<List<RedditPost>> {

        val requests = breadSubreddits.map { subreddit ->
            apiFactory.createApi().getFeed(subreddit).map {
                it.data.children.map{ item ->
                    mapNetworkToUiModel(item.data)
                }
            }.doOnNext {
                dao.insertPosts(it)
            }
        }

        return Observable.merge(requests).toList()
            .map{ it.flatten() }
    }

    fun getRedditPost(id: String): Observable<RedditPost> {
        return dao.getById(id)
    }

    private fun mapNetworkToUiModel(data: ApiRedditPost): RedditPost {
        return RedditPost(
                    id = data.id,
                    author = data.author,
                    title = data.title,
                    selfText = data.selftext,
                    subreddit_name_prefixed = data.subreddit_name_prefixed,
                    score = data.score,
                    created = data.created,
                    num_comments = data.num_comments,
                    permalink = data.permalink,
                    url = data.url,
                    subreddit_subscribers = data.subreddit_subscribers,
                    created_utc = data.created_utc,
//                    media = data.media,
                    is_video = data.is_video,
                    subreddit_id = data.subreddit_id
                )
    }

}


data class RedditFeedResponse(
    val kind: String,
    val data: RedditFeedData
)

data class RedditFeedData(
    val modhash: String,
    val dist: Int,
    val children: List<RedditFeedChild>,
    val after: String
)

data class RedditFeedChild(
    val kind: String,
    val data: ApiRedditPost
)

data class ApiRedditPost(
    val id: String,
    val author: String,
    val title: String,
    val selftext: String,
    val subreddit_name_prefixed: String,
    val score: Int,
    val created: Int,
    val subreddit_id: String,
    val num_comments: Int,
    val permalink: String,
    val url: String,
    val subreddit_subscribers: Int,
    val created_utc: Int,
    val media: Media?,
    val is_video: Boolean
)

@Entity
data class Media(
    @PrimaryKey(autoGenerate = true)
    val id: Int = -1,
    val oembed: Embedded,
    val type: String
)


@Entity
data class Embedded(
    @PrimaryKey(autoGenerate = true)
    val id: Int = -1,
    val provider_url: String,
    val description: String,
    val title: String,
    val url: String,
    val type: String,
    val thumbnail_width: Int,
    val height: Int,
    val width: Int,
    val html: String,
    val version: String,
    val provider_name: String,
    val thumbnail_url: String,
    val thumbnail_height: Int
)