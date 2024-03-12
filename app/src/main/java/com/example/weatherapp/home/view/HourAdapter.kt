//package com.example.weatherapp.home.view
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//
//class HourAdapter (var context: Context, var listener: OnFavoriteClickListener):
//    ListAdapter<Product, HourAdapter.ProductViewHolder>(ProductDiffUtil()) {
//
//
//    lateinit var binding: FavItemBinding
//
//
//    class ProductViewHolder (var binding: FavItemBinding) : RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
//        binding = FavItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ProductViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
//        val current = getItem(position)
//        holder.binding.degree.text=current.title
//        holder.binding.hour.text= current.price
//        Glide.with(context).load(current.thumbnail).into(holder.binding.img)
//        holder.binding.btnFavRemove.setOnClickListener{
//            listener.onClick(current)
//        }
//
//
//
//    }
//
//
//
//}
//
//
//
//class ProductDiffUtil : DiffUtil.ItemCallback<Product>(){
//    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
//        return oldItem.title == newItem.title
//    }
//
//    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
//        return newItem == oldItem
//    }
//
//}