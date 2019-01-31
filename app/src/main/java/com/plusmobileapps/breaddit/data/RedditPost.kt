package com.plusmobileapps.breaddit.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RedditPost(

    @PrimaryKey(autoGenerate = false)
    val id: String,

    val author: String,
    val title: String,
    val selfText: String?,
    val subreddit_name_prefixed: String,
    val score: Int,
    val created: Long,
    val subreddit_id: String,
    val num_comments: Int,
    val permalink: String,
    val url: String,
    val created_utc: Long

)