package com.dicoding.android_capstoneproject.ui.news

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.dicoding.android_capstoneproject.api.ApiClient
import com.dicoding.android_capstoneproject.api.ApiInterface
import com.dicoding.android_capstoneproject.R
import kotlinx.android.synthetic.main.fragment_news.*
import com.dicoding.android_capstoneproject.model.Article
import com.dicoding.android_capstoneproject.model.News
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsFragment: Fragment(), SwipeRefreshLayout.OnRefreshListener {

    var articles: ArrayList<Article> = ArrayList()

    private lateinit var newsAdapter: NewsAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefresh.setOnRefreshListener(this)
        swipeRefresh.setColorSchemeResources(R.color.colorAccent)

        viewManager = GridLayoutManager(context, 1)

        if(activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewManager = GridLayoutManager(context, 2)
        }

        recyclerView.layoutManager = viewManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.isNestedScrollingEnabled = false
        loadJSON()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewManager = GridLayoutManager(context, 2)
            recyclerView.layoutManager = viewManager
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            viewManager = GridLayoutManager(context, 1)
            recyclerView.layoutManager = viewManager
        }
    }

    private fun loadJSON() {
        val apiInterface: ApiInterface? = ApiClient.getApiClient?.create(ApiInterface::class.java)

        val call: Call<News>? = apiInterface?.getNews(
            getString(R.string.news_api_country),
            getString(R.string.news_api_category),
            getString(R.string.news_api_key)
        )

        call?.enqueue(object : Callback<News> {
            override fun onFailure(call: Call<News>?, t: Throwable?) {
                Toast.makeText(activity, "No Result", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<News>?, response: Response<News>?) {
                if (response!!.isSuccessful && response.body()?.article != null) {
                    articles = (response.body()?.article as ArrayList<Article>?)!!
                    newsAdapter = NewsAdapter(context, articles)
                    if(recyclerView == null) {
                        return
                    }
                    recyclerView.adapter = newsAdapter
                    newsAdapter.notifyDataSetChanged()

                } else {
                    val errorCode: String = when {
                        response.code() == 404 -> "404 Not Found"
                        response.code() == 500 -> "500 Server Failed"
                        else -> "Unknown Error"
                    }
                    Toast.makeText(activity, errorCode, Toast.LENGTH_SHORT).show()
                }
                swipeRefresh.isRefreshing = false
            }
        })
    }

    override fun onRefresh() {
        loadJSON()
    }
}