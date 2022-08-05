package com.shakib.gsk2022.presentation.screens.picker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.shakib.gsk2022.common.extensions.invisible
import com.shakib.gsk2022.common.extensions.showLongToast
import com.shakib.gsk2022.common.extensions.visible
import com.shakib.gsk2022.data.model.Image
import com.shakib.gsk2022.databinding.ItemImageBinding

class MediaPickerAdapter(
    private val selectedImages: ArrayList<Image>,
    private val maxSelection: Int
) : RecyclerView.Adapter<MediaPickerAdapter.ImageViewHolder>() {

    private val imageList: ArrayList<Image> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ImageViewHolder(
        ItemImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentImage = imageList[position]
        holder.binding.apply {
            ivSelect.invisible()
            ivImage.apply {
                Glide.with(context)
                    .load(currentImage.uri)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(this)
            }
            tvName.text = currentImage.title
            itemView.setOnClickListener {
                if (selectedImages.size >= maxSelection)
                    it.context.showLongToast("Max selection reached")
                else {
                    if (selectedImages.contains(currentImage)) {
                        selectedImages.remove(currentImage)
                        ivSelect.invisible()
                    } else {
                        selectedImages.add(currentImage)
                        ivSelect.visible()
                    }
                }
            }
        }
    }

    override fun getItemCount() = imageList.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(imageList: List<Image>) {
        this.imageList.clear()
        this.imageList.addAll(imageList)
        notifyDataSetChanged()
    }

    class ImageViewHolder(val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root)
}
