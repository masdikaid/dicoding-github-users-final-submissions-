package com.mdidproject.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdidproject.githubuser.databinding.UserItemBinding
import com.mdidproject.githubuser.interfaces.ItemAdapterCallback
import com.mdidproject.githubuser.response.UserItem

class UserListAdapter(private val listUser: List<UserItem>): RecyclerView.Adapter<UserListAdapter.ListViewHolder>() {

    private lateinit var itemCallback: ItemAdapterCallback<UserItem>

    class ListViewHolder(var binding: UserItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(vGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(vGroup.context), vGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, pos: Int) {
        val (avatar, username) = listUser[pos]
        holder.binding.apply {
            tvListUsername.text = username

            Glide.with(holder.itemView.context)
                .load(avatar)
                .circleCrop()
                .into(ivListAvatar)

            holder.itemView.setOnClickListener { view -> itemCallback.onItemClicked(view, listUser[holder.adapterPosition])}
        }
    }

    override fun getItemCount(): Int = listUser.size

    fun setAdapterItemCallback(callback: ItemAdapterCallback<UserItem>){
        this.itemCallback = callback
    }
}