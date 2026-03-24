package com.example.newsapp

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.newsapp.databinding.NewsListItemBinding

class NewsAdapter(val a: Activity, val articles: ArrayList<Article>) :
    RecyclerView.Adapter<NewsAdapter.NewViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewViewHolder {
        val b = NewsListItemBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return NewViewHolder(b)
    }

    override fun onBindViewHolder(
        holder: NewViewHolder,
        position: Int
    ) {
        holder.binding.articleText.text = articles[position].title
        Glide
            .with(holder.binding.newsIv.context)
            .load(articles[position].urlToImage)
            .error(R.drawable.broken_image)
            .transition(DrawableTransitionOptions.withCrossFade(1000))
            .into(holder.binding.newsIv)
        val url = articles[position].url

        holder.binding.newsCv.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW,url.toUri())
            a.startActivity(i)
        }

        holder.binding.shareFab.setOnClickListener {
            ShareCompat
                .IntentBuilder(a)
                .setType("text/plain")
                .setChooserTitle("Share article with :")
                .setText(url)
                .startChooser()
        }
    }

    override fun getItemCount() = articles.size

    class NewViewHolder(val binding: NewsListItemBinding) : RecyclerView.ViewHolder(binding.root)

}