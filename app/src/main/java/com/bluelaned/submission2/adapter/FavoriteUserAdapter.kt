package com.bluelaned.submission2.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bluelaned.submission2.activity.DetailsActivity
import com.bluelaned.submission2.database.FavoriteUser
import com.bluelaned.submission2.databinding.ListFollowersBinding

class FavoriteUserAdapter :
    ListAdapter<FavoriteUser, FavoriteUserAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListFollowersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    inner class MyViewHolder(private val binding: ListFollowersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(itemName: FavoriteUser) {
            binding.tvNameFollower.text = itemName.username
            Glide.with(binding.root)
                .load(itemName.avatarUrl)
                .into(binding.imgFollower)

            binding.root.setOnClickListener {
                val intentDetail = Intent(binding.root.context, DetailsActivity::class.java)
                intentDetail.putExtra("ID", itemName.name)
                intentDetail.putExtra("USERNAME", itemName.username)
                intentDetail.putExtra("AVATAR", itemName.avatarUrl)
                binding.root.context.startActivity(intentDetail)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteUser>() {
            override fun areItemsTheSame(oldItem: FavoriteUser, newItem: FavoriteUser): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FavoriteUser, newItem: FavoriteUser): Boolean {
                return oldItem == newItem
            }
        }
    }
}