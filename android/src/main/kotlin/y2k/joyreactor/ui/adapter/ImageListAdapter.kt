package y2k.joyreactor.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import y2k.joyreactor.R
import y2k.joyreactor.common.ComplexViewHolder
import y2k.joyreactor.common.inflate
import y2k.joyreactor.enteties.Image
import y2k.joyreactor.image.JoyImageUtils

/**
 * Created by Oleg on 06.03.2016.
 */
class ImageListAdapter() : RecyclerView.Adapter<ComplexViewHolder>() {

    private var images: List<Image> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplexViewHolder {
        return ImageViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ComplexViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int {
        return images.size
    }

    fun setPostImages(imageList: List<Image>) {
        this.images = imageList
        notifyDataSetChanged()
    }

    internal inner class ImageViewHolder(parent: ViewGroup) : ComplexViewHolder(parent.inflate(R.layout.item_image)) {

        var image: ImageView

        init {
            image = itemView.findViewById(R.id.image) as ImageView
        }

        override fun bind() {
            JoyImageUtils.load(image, images[adapterPosition])
        }
    }
}