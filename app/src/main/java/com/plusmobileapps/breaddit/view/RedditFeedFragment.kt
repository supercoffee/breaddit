package com.plusmobileapps.breaddit.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.plusmobileapps.breaddit.R
import com.plusmobileapps.breaddit.viewmodels.MainViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.viewmodel.ext.android.viewModel


class RedditFeedFragment : Fragment() {

    val viewModel: MainViewModel by viewModel()

    val adapter by lazy { RedditPostListAdapter(viewModel) }

    lateinit var subscriptions: CompositeDisposable

    private lateinit var recyclerview: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerview = view.findViewById(R.id.recyclerview)
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.dispose()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscriptions = CompositeDisposable()
        recyclerview.adapter = adapter
//        viewModel.redditPosts.observe(this, Observer { redditPosts ->
//            adapter.submitList(redditPosts)
//        })

        val dataObservable = viewModel.redditPosts.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data ->
                adapter.submitList(data)
            }, {error ->

            })
        subscriptions.add(dataObservable)

        viewModel.getOpenRedditPostDetails().observe(this, Observer {  redditPostId ->
            fragmentManager?.transaction {
                replace(R.id.content_frame, RedditPostDetailFragment.newInstance(redditPostId))
                addToBackStack(null)
            }
        })
    }
}
