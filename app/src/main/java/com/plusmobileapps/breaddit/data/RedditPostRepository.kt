package com.plusmobileapps.breaddit.data

import io.reactivex.Observable
import io.reactivex.Single
import net.dean.jraw.models.Listing
import net.dean.jraw.models.Submission
import net.dean.jraw.references.SubredditReference

class RedditPostRepository(val clientProvider: RedditClientProvider, val dao: RedditPostDao) {

    fun posts(subreddit: String): Single<List<RedditPost>> {

        val subRef: Observable<SubredditReference> = clientProvider.redditClient().map { client ->
            client.subreddit(subreddit)
        }

        val observables: Observable<Listing<Submission>> = subRef.flatMap {
            createSubmissionObservable(it)
        }

        return observables.map {
            it.children.map {submission ->
                mapNetworkToUiModel(submission)
            }
        }.singleOrError()
    }

    private fun createSubmissionObservable(ref: SubredditReference): Observable<Listing<Submission>> {
        return Observable.create<Listing<Submission>> {
            try {
                val submissions = ref.posts().build().next()
                it.onNext(submissions)
                it.onComplete()
            } catch (e: Exception) {
                it.onError(e)
            }
        }
    }

    fun getRedditPost(id: String): Observable<RedditPost> {
        return dao.getById(id)
    }

    private fun mapNetworkToUiModel(data: Submission): RedditPost {

        return RedditPost(
            id = data.id,
            author = data.author,
            title = data.title,
            selfText = data.selfText,
            subreddit_name_prefixed = data.subreddit,
            score = data.score,
            created = data.created.time,
            num_comments = data.commentCount,
            permalink = data.permalink,
            url = data.url,
            created_utc = data.created.time,
            subreddit_id = data.subredditFullName
        )
    }

}
