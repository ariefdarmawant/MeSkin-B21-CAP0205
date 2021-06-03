package com.dicoding.android_capstoneproject.ui.news

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.dicoding.android_capstoneproject.R
import com.dicoding.android_capstoneproject.model.Article
import kotlinx.android.synthetic.main.item_article.view.*

class NewsAdapter(private val context: Context?, private val list: ArrayList<Article>) : RecyclerView.Adapter<NewsAdapter.ViewHolderView>() {

    private var noImageUrl = "https://i.stack.imgur.com/y9DpT.jpg"
    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderView {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_article, parent, false)
        return ViewHolderView(view, onItemClickListener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int) {
        }
    }

    override fun onBindViewHolder(holder: ViewHolderView, position: Int) {
        val model:Article = list[position]

        val requestOptions = RequestOptions()
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
        requestOptions.centerCrop()

        if(context != null) {
            val imgUrl = if (model.urlToImage == null) noImageUrl else model.urlToImage
            Glide.with(context)
                .load(imgUrl)
                .apply(requestOptions)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.progressBar.visibility = View.GONE
                        return false
                    }

                }).transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView)
        }

        if(model.description == null) {
            holder.desc.visibility = View.GONE
        }

        holder.title.text = model.title
        holder.desc.text = model.description
        holder.itemView.setOnClickListener {
            model.url?.let { it1 -> Log.d("hi", it1) }
            val intent = Intent(context, NewsDetail::class.java)
            intent.putExtra("url", model.url)
            context?.startActivity(intent)
        }
    }

    class ViewHolderView(view: View, onItemClickListener: OnItemClickListener?) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val title: TextView = view.article_title
        val desc: TextView = view.article_description
        val imageView: ImageView = view.article_urlToImage
        val progressBar: ProgressBar = view.article_progressBar

        private val onItemClickListeners = onItemClickListener

        override fun onClick(v: View?) {
            onItemClickListeners?.onItemClick(v, absoluteAdapterPosition)
        }
    }
}