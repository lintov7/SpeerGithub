package com.example.speergithub.ui.followers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.speergithub.databinding.FragmentFollowersBinding
import com.example.speergithub.repository.models.User
import com.example.speergithub.util.EventObserver
import com.example.speergithub.util.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class FollowersFragment : Fragment() {
    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private var userName: String? = null
    private var userType: String? = null
    val followersViewModel by viewModels<FollowersViewModel>()
    @Inject
    lateinit var adapter: FollowersAdapter
    private val args: FollowersFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args.let {
            userName = it.username
            userType = it.type
        }
        followersViewModel.initialize(userName!!, userType!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if(_binding == null){
            _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        }
        return binding.root
    }

    companion object {
        const val USER_TYPE_FOLLOWING = "USER_TYPE_FOLLOWING"
        const val USER_TYPE_FOLLOWER = "USER_TYPE_FOLLOWER"

        @JvmStatic
        fun newInstance() = FollowersFragment()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.onItemClick = {
            val action = FollowersFragmentDirections.actionFollowersFragmentToProfileFragment(it.username)
            view.findNavController().navigate(action)
        }
        binding.rvFollowers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FollowersFragment.adapter
        }
        setUpScrollListener()
        setUpObservers()
    }


    private fun setUpScrollListener() {
        binding.srLayout.setOnRefreshListener {
            adapter.clearData()
            followersViewModel.reset()
            binding.srLayout.isRefreshing = false
        }
        binding.rvFollowers.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (followersViewModel.loadingLiveData.value != Status.LOADING) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == adapter.userList.size - 1) {
                        followersViewModel.goToNextPage()
                    }
                }
            }
        })
    }

    private fun setUpObservers(){
        followersViewModel.loadingLiveData.observe(viewLifecycleOwner){
            when(it){
                Status.LOADING -> {
                    binding.rvFollowers.post {
                        adapter.showLoading()
                    }
                }
                Status.SUCCESS -> {
                    binding.rvFollowers.post {
                        adapter.hideLoading()
                    }
                }
                Status.ERROR ->  binding.rvFollowers.post {
                    adapter.hideLoading()
                }
            }
        }
        followersViewModel.usersLiveData.observe(viewLifecycleOwner, EventObserver{
            binding.rvFollowers.post {
                adapter.addNewData(it)
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}