package com.example.speergithub.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.speergithub.repository.Repository
import com.example.speergithub.repository.models.User
import com.example.speergithub.util.Event
import com.example.speergithub.util.EventObserver
import com.example.speergithub.util.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: Repository): ViewModel() {
    val loadingLiveData = MutableLiveData<Status>() // For updating the UI State
    val userLiveData = MutableLiveData<Event<User>>() // For updating the data

    /*
    * This function will try to retrieve the user with the given username
    * */
    fun searchUsers(username:String){
        loadingLiveData.postValue(Status.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.findUser(username)
            withContext(Dispatchers.Main){
                if(response.isSuccessful && response.code() == 200){
                    loadingLiveData.value = Status.SUCCESS
                    userLiveData.postValue(Event(response.body()!!))
                }else{
                    loadingLiveData.postValue(Status.ERROR)
                }
            }

        }
    }
}