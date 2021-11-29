package com.wanda.idn.newsapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.wanda.idn.newsapplication.databinding.ActivityDetailBinding
import com.wanda.idn.newsapplication.model.ArticlesItem

class DetailActivity : AppCompatActivity() {
    private lateinit var detailBinding: ActivityDetailBinding
    private lateinit var news : ArticlesItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding= ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)
        news = intent.getParcelableExtra<ArticlesItem>(EXTRA_NEWS) as ArticlesItem
        supportActionBar?.hide()
        showDetailData()
    }

    private fun showDetailData() {
        detailBinding.apply {
            tvDetailTitle.text = news.title
            tvNameDetail.text = news.author
            tvDateDetail.text = news.publishedAt
            tvDescDetail.text = news.description
            tvContentDetail.text = news.content


            Glide.with(this@DetailActivity).load(news.urlToImage).into(detailBinding.ivDetail)
        }
    }

    companion object{
        const val EXTRA_NEWS = "extra_news"
    }

}