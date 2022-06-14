package com.mdidproject.githubuser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdidproject.githubuser.adapter.UserListAdapter
import com.mdidproject.githubuser.databinding.FragmentFamousUserBinding
import com.mdidproject.githubuser.interfaces.ItemAdapterCallback
import com.mdidproject.githubuser.response.UserItem
import com.mdidproject.githubuser.viewmodel.UserListViewModel

class FamousUserFragment : Fragment() {
    private var _binding: FragmentFamousUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFamousUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserListViewModel::class.java)
        viewModel.userList.observe(viewLifecycleOwner) { data ->
            showList(data)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = getString(R.string.app_name)
            show()
        }
    }

    private fun showList(users: List<UserItem>){
        val adapter = UserListAdapter(users)
        adapter.setAdapterItemCallback(object : ItemAdapterCallback<UserItem>{
            override fun onItemClicked(view: View?, data: UserItem) {
                view?.let {
                    val action = FamousUserFragmentDirections.actionFamousUserFragmentToDetailUserActivity(data.username)
                    view.findNavController().navigate(action)
                }?: run {
                    Toast.makeText(activity, "failed to open detail ${data.username}", Toast.LENGTH_SHORT).show()
                }
            }
        })
        binding.apply {
            rvFamousUserList.setHasFixedSize(true)
            rvFamousUserList.layoutManager = LinearLayoutManager(context)
            rvFamousUserList.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}