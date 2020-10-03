package com.example.covidtracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.covidtracker.R
import com.example.covidtracker.models.Article
import kotlinx.android.synthetic.main.article_preview.view.*

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {    //checks to see which lists are different and updates the ones that have changed
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url   //compares url because the articles we get from our api do not have a unique id but a unique url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {  //checks if the contents of the articles are the same
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.article_preview,
                parent,
                false   //don't attach to root layout
            )
        )
    }

    override fun getItemCount(): Int {  //get amount of items in our recycler view
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {   //set our views
        val article = differ.currentList[position]
        holder.itemView.apply { //reference each view directly
            Glide.with(this).load(article.urlToImage).into(ivArticleImage)
            tvSource.text = article.source.name
            tvTitle.text = article.title
            tvDescription.text = article.description
            tvPublishedAt.text = article.publishedAt

            //refers to the item view
            setOnClickListener{
                onItemClickListener?.let {it(article)}  //checks to see the listener is not null, if not null then call the lambda function with the current article
            }
        }
    }

    private var onItemClickListener: ((Article) -> Unit)? = null    //pass article when item is clicked to return the view of the web page

    fun setOnItemClickListener(listener: (Article)-> Unit) {
        onItemClickListener = listener
    }
}