package com.example.speergithub.ui.followers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.speergithub.repository.Repository
import com.example.speergithub.repository.models.User
import com.example.speergithub.util.Event
import com.example.speergithub.util.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

const val PAGE_SIZE = 10

@HiltViewModel
class FollowersViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    val users = arrayListOf<User?>()
    val loadingLiveData = MutableLiveData<Status>()
    val usersLiveData = MutableLiveData<Event<List<User?>>>()
    private var page: Int = 1
    private var isLastPage = false
    private var username: String = ""
    private var type: String = ""

    fun initialize(username: String, type: String) {
        this.username = username
        this.type = type
        page = 1
        loadUsers()
    }

    private fun loadUsers() {
        loadingLiveData.value = Status.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            val response: Response<List<User>> = if (type == FollowersFragment.USER_TYPE_FOLLOWER) {
                repository.getFollowers(username, page, PAGE_SIZE)
            } else {
                repository.getFollowing(username, page, PAGE_SIZE)
            }
            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.code() == 200) {
                    loadingLiveData.value = Status.SUCCESS
                    if (response.body() != null) {
                        val newUsers = response.body()!!
                        if(newUsers.size < PAGE_SIZE){
                            isLastPage = true
                        }
                        users.addAll(newUsers)
                        usersLiveData.value = Event(newUsers)
                    }
                } else {
                    isLastPage = true
                    loadingLiveData.value  = Status.ERROR
                }
            }

        }
    }

    fun goToNextPage() {
        if (!isLastPage) {
            page++
            loadUsers()
        }
    }

    fun reset() {
        users.clear()
        page = 1
        loadUsers()
    }
}