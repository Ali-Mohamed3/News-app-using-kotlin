package com.example.newsapp

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.newsapp.databinding.NewsListItemBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class NewsAdapter(val a: Activity, val articles: ArrayList<Article>) :
    RecyclerView.Adapter<NewsAdapter.NewViewHolder>() {
    // we are wrapping the view here in view holder, the recycler view only generates a few number of
        // view holders, then recycles them
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewViewHolder {
        val b = NewsListItemBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return NewViewHolder(b)
    }

    // here we attaching data to the view holder
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
        holder.binding.favFab.setOnClickListener {
            val article = hashMapOf(
                "title" to articles[position].title,
                "url" to articles[position].url,
                "urlToImage" to articles[position].urlToImage
            )

            val db = Firebase.firestore

            db.collection("news")
                .add(article)
                .addOnSuccessListener { documentReference ->
                    Log.d("success", "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.d("error", "Error adding document", e)
                }

            Toast.makeText(a, "Added to favourites", Toast.LENGTH_SHORT).show();



        }
    }

    override fun getItemCount() = articles.size

    class NewViewHolder(val binding: NewsListItemBinding) : RecyclerView.ViewHolder(binding.root)

}