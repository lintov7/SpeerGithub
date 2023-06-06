package com.example.speergithub.ui.followers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.speergithub.databinding.FollowerItemBinding
import com.example.speergithub.databinding.LoadingItemBinding
import com.example.speergithub.repository.models.User
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

class FollowersAdapter @Inject constructor(): RecyclerView.Adapter<ViewHolder>() {
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    var onItemClick: ((User) -> Unit)? = null
    val userList: ArrayList<User?> = arrayListOf()

    override fun getItemViewType(position: Int): Int {
        return if (userList[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val binding = FollowerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            FollowersViewHolder(binding){
                onItemClick?.invoke(userList[it]!!)
            }
        } else {
            val binding = LoadingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LoadingViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is FollowersViewHolder) {
            holder.bind(userList[position]!!)
        }
    }

    fun addNewData(users:List<User?>){
        val initialPosition = userList.size
        userList.addAll(users)
        notifyItemRangeInserted(initialPosition, users.size)
    }

    fun showLoading(){
        userList.add(null)
        notifyItemInserted(userList.size -1)
    }

    fun hideLoading(){
        if(userList.size> 0 && userList.last() == null){
            userList.removeAt(userList.size - 1)
            notifyItemRemoved(userList.size)
        }
    }

    fun clearData(){
        userList.clear()
        notifyDataSetChanged()
    }


}


class FollowersViewHolder(val binding: FollowerItemBinding, private val onItemClick: (Int) -> Unit) : ViewHolder(binding.root){
    init {
        binding.cvFollower.setOnClickListener {
            onItemClick.invoke(adapterPosition)
        }
    }
    fun bind(user:User){
        binding.tvFollower.text = user.username
        Glide
            .with(binding.ivFollower)
            .load(user.avatar)
            .centerCrop()
            .into(binding.ivFollower)
    }
}


class LoadingViewHolder(binding: LoadingItemBinding) : ViewHolder(binding.root){

}