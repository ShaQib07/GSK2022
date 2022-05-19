package com.shakib.gsk2022.presentation.screens.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.shakib.gsk2022.databinding.ItemImageBinding
import com.shakib.gsk2022.data.model.Image

class ImageAdapter(private val imageList: ArrayList<Image>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ImageViewHolder(
        ItemImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.binding.ivImage.apply {
            Glide.with(context)
                .load(imageList[position].uri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(this)
        }
        holder.binding.tvName.text = imageList[position].title
    }

    override fun getItemCount() = imageList.size

    class ImageViewHolder(val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root)
}