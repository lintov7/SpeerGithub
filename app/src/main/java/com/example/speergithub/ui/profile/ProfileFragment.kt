package com.example.speergithub.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.speergithub.databinding.FragmentProfileBinding
import com.example.speergithub.repository.models.User
import com.example.speergithub.ui.followers.FollowersFragment
import com.example.speergithub.util.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profileViewModel by viewModels<ProfileViewModel>()
    private val args: ProfileFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        args.apply {
            if(user != null){
                profileViewModel.setUser(user)
            }else{
                profileViewModel.getProfile(username)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpListeners()
        setUpObservers()
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }

    private fun setUpListeners(){
        binding.llFollowers.setOnClickListener {view->
            profileViewModel.userLiveData.value?.let {
                val action = ProfileFragmentDirections.actionProfileFragmentToFollowersFragment(it.username, FollowersFragment.USER_TYPE_FOLLOWER)
                view.findNavController().navigate(action)
            }
        }

        binding.llFollowing.setOnClickListener {view->
            profileViewModel.userLiveData.value?.let {
                val action = ProfileFragmentDirections.actionProfileFragmentToFollowersFragment(it.username, FollowersFragment.USER_TYPE_FOLLOWING)
                view.findNavController().navigate(action)
            }
        }
    }

    private fun setUpObservers(){
        profileViewModel.loadingLiveData.observe(viewLifecycleOwner){
            when(it){
                Status.LOADING -> {
                    binding.flLoading.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    binding.flLoading.visibility = View.GONE
                }
                Status.ERROR -> {
                    binding.flLoading.visibility = View.GONE
                }
            }
        }

        profileViewModel.userLiveData.observe(viewLifecycleOwner){user->
            binding.apply {
                Glide
                    .with(this@ProfileFragment)
                    .load(user.avatar)
                    .centerCrop()
                    .into(ivProfile)
                tvUsername.text = user.username
                tvName.text = user.name
                tvDescription.text = user.description
                tvFollowers.text = user.followers.toString()
                tvFollowing.text = user.following.toString()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}