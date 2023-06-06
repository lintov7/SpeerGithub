package com.example.speergithub.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.speergithub.databinding.FragmentSearchBinding
import com.example.speergithub.ui.followers.FollowersFragment
import com.example.speergithub.util.EventObserver
import com.example.speergithub.util.Status
import com.example.speergithub.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel by viewModels<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObservers()
        setUpListeners()
    }

    private fun setUpListeners(){
        binding.btnSearch.setOnClickListener {
            val searchUsername = binding.etSearch.text.toString()
            if(searchUsername.length > 3){
                searchViewModel.searchUsers(searchUsername)
            }else{
                requireContext().showToast("Please enter a valid username with more than 3 characters")
            }
        }

    }

    private fun setUpObservers(){
        searchViewModel.loadingLiveData.observe(viewLifecycleOwner){
            when(it){
                Status.LOADING -> {
                    binding.flLoading.visibility = View.VISIBLE
                    binding.btnSearch.isEnabled = false
                    binding.tvNotFound.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    binding.flLoading.visibility = View.GONE
                    binding.btnSearch.isEnabled = true
                    binding.tvNotFound.visibility = View.GONE
                }
                Status.ERROR -> {
                    binding.flLoading.visibility = View.GONE
                    binding.btnSearch.isEnabled = true
                    binding.tvNotFound.visibility = View.VISIBLE
                }
            }
        }

        searchViewModel.userLiveData.observe(viewLifecycleOwner, EventObserver{
            val action = SearchFragmentDirections.actionSearchFragmentToProfileFragment(it.username, it)
            binding.root.findNavController().navigate(action)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}