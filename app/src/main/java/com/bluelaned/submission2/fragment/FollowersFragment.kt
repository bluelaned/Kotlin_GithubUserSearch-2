package com.bluelaned.submission2.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bluelaned.submission2.adapter.ListFollowersAdapter
import com.bluelaned.submission2.databinding.FragmentFollowersBinding
import com.bluelaned.submission2.viewmodel.DetailViewModel
import com.bluelaned.submission2.viewmodel.ViewModelFactory


class FollowersFragment : Fragment() {
    var position = 0
    var username: String = ""

    private lateinit var binding: FragmentFollowersBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val ARG_USERNAME = "0"
        const val ARG_POSITION = "Bearer "
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailViewModel = obtainDetailViewModel(requireActivity())

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME) ?: "Joshua Feliex"
        }

        detailViewModel.getFollowing(username)
        detailViewModel.getFollower(username)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollowers.layoutManager = layoutManager

        detailViewModel.loading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        if (position == 1) {
            detailViewModel.userFollower.observe(viewLifecycleOwner) {
                val adapter = ListFollowersAdapter()
                adapter.submitList(it)
                binding.rvFollowers.adapter = adapter
            }
        } else {
            detailViewModel.userFollowing.observe(viewLifecycleOwner) {
                val adapter = ListFollowersAdapter()
                adapter.submitList(it)
                binding.rvFollowers.adapter = adapter
            }
        }
    }

    private fun obtainDetailViewModel(activity: FragmentActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(requireActivity(), factory)[DetailViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
    }
}