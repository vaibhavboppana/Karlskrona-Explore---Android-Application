package com.android.mapapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.mapapp.data.PlaceData
import com.android.mapapp.data.TRANSPORTATION
import com.android.mapapp.databinding.RvPlaceBinding
import com.bumptech.glide.RequestManager
import java.util.*

class PlaceAdapter(val glide : RequestManager)  : RecyclerView.Adapter<PlaceAdapter.PhotosViewHolder>()  {

    class PhotosViewHolder(val binding : RvPlaceBinding) : RecyclerView.ViewHolder(binding.root)

    private var onItemClickListener: ((PlaceData) -> Unit)? = null

    fun setOnItemClickListener(position: (PlaceData) -> Unit) {
        onItemClickListener = position
    }


    private val diffCallback = object : DiffUtil.ItemCallback<PlaceData>() {

        override fun areContentsTheSame(oldItem: PlaceData, newItem: PlaceData): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: PlaceData, newItem: PlaceData): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var placesList : List<PlaceData>
        get() = differ.currentList
        set(value) = differ.submitList(value)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val binding = RvPlaceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return  PhotosViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return places.size
    }

    var places = mutableListOf<PlaceData>()

    fun filter(text: String) {
        var text = text
        places.clear()
        if (text.isEmpty()) {
            places.addAll(placesList)
        } else {
            text = text.lowercase(Locale.getDefault())
            for (item in placesList) {
                val name = item.name
                val description = item.description
                if (name.lowercase(Locale.getDefault()).contains(text) || description.lowercase(
                        Locale.getDefault()).contains(text)) {
                    places.add(item)
                }
            }
        }
        //  Toast.makeText(context,"items ${itemsList.size} and itemslist ${itemsList.size}", Toast.LENGTH_SHORT).show()
        notifyDataSetChanged()
    }


    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        val data = places[position]
        holder.itemView.apply {

            with(holder) {

                glide.load(data.photo).into(binding.ivPlace)
                binding.ivStar.text = data.rating.toString()
                binding.tvPlace.text = data.name

                if (data.type != TRANSPORTATION){
                    binding.ivStar.visibility = View.VISIBLE
                    binding.ivStaricon.visibility = View.VISIBLE
                }else{
                    binding.ivStar.visibility = View.GONE
                    binding.ivStaricon.visibility = View.GONE
                }

            }

            setOnClickListener {
                onItemClickListener?.let {
                        click ->
                    click(data)
                }
            }


        }
    }
}