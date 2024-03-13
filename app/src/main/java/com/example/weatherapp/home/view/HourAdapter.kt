package com.example.weatherapp.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.HourItemBinding
import com.example.weatherapp.model.Hour

class HourAdapter (var context: Context):
    ListAdapter<Hour, HourAdapter.ProductViewHolder>(HourDiffUtil()) {


    lateinit var binding: HourItemBinding


    class ProductViewHolder(var binding: HourItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        binding = HourItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val current = getItem(position)
        holder.binding.degree.text = current.temp.toString()
        holder.binding.hour.text = current.feels_like.toString()
        //  Glide.with(context).load(current.).into(holder.binding.img)
//        holder.binding.btnFavRemove.setOnClickListener{
//            listener.onClick(current)
//        }


    }
}

    class HourDiffUtil : DiffUtil.ItemCallback<Hour>(){
        override fun areItemsTheSame(oldItem: Hour, newItem: Hour): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Hour, newItem: Hour): Boolean {
            return newItem == oldItem
        }

    }





