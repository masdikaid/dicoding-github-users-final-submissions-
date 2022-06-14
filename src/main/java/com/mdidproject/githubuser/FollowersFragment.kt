package com.mdidproject.githubuser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdidproject.githubuser.adapter.UserListAdapter
import com.mdidproject.githubuser.databinding.FragmentFamousUserBinding
import com.mdidproject.githubuser.databinding.FragmentFollowersBinding
import com.mdidproject.githubuser.interfaces.ItemAdapterCallback
import com.mdidproject.githubuser.response.UserItem
import com.mdidproject.githubuser.viewmodel.DetailUserViewModel
import com.mdidproject.githubuser.viewmodel.FollowersViewModel
import com.mdidproject.githubuser.viewmodel.UserListViewModel

class FollowersFragment(private val username: String) : Fragment() {
    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FollowersViewModel by viewModels()
    private val userList: MutableList<UserItem> = arrayListOf()
    private lateinit var rvAdapter: UserListAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        rvAdapter = UserListAdapter(userList)
        binding.apply {
            rvUserFollowers.layoutManager = LinearLayoutManager(context)
            rvUserFollowers.setHasFixedSize(true)
            rvUserFollowers.adapter = rvAdapter
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFollowers(username)
        viewModel.followers.observe(viewLifecycleOwner) { data ->
            showList(data)
        }
    }

    private fun showList(users: List<UserItem>){
        rvAdapter.setAdapterItemCallback(object : ItemAdapterCallback<UserItem> {
            override fun onItemClicked(view: View?, data: UserItem) {
                view?.let {
                    val action = DetailUserFragmentDirections.actionDetailUserFragmentSelf(data.username)
                    view.findNavController().navigate(action)
                }?: run {
                    Toast.makeText(activity, "failed to open detail ${data.username}", Toast.LENGTH_SHORT).show()
                }
            }
        })
        userList.clear()
        userList.addAll(users)
        rvAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}