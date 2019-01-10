package com.plusmobileapps.breaddit.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.plusmobileapps.breaddit.R
import com.plusmobileapps.breaddit.viewmodels.RedditPostDetailViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_reddit_post_detail.*
import org.koin.android.viewmodel.ext.android.viewModel


class RedditPostDetailFragment : Fragment() {

    private val viewModel: RedditPostDetailViewModel by viewModel()
    private var redditPostId: String? = null

    private lateinit var subscription: Disposable

    companion object {

        private const val REDDIT_POST_ID = "reddit post id"

        @JvmStatic
        fun newInstance(id: String) = RedditPostDetailFragment().apply {
            arguments = bundleOf(REDDIT_POST_ID to id)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            redditPostId = it.getString(REDDIT_POST_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_reddit_post_detail, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        subscription.dispose()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (redditPostId == null) {
            throw IllegalStateException("No id set for the reddit post to be retrieved")
        }

        subscription = viewModel.loadRedditPost(redditPostId!!).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {redditPost ->
                title.text = redditPost.title
                author.text = "u/${redditPost.author}"
                Glide.with(this).load(redditPost.url).into(imageView)
                self_text.text = redditPost.selfText
            }
    }
}
