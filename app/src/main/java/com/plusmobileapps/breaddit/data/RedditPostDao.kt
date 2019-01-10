package com.plusmobileapps.breaddit.data

import androidx.room.*
import io.reactivex.Observable

@Dao
interface RedditPostDao {

    @Query("SELECT * FROM redditpost")
    fun getPosts(): Observable<List<RedditPost>>

    @Query("SELECT * FROM redditpost WHERE id in (:id)")
    fun getById(id: String): Observable<RedditPost>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPost(post: RedditPost)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPosts(posts: List<RedditPost>)

    @Delete
    fun deletePost(post: RedditPost)

}