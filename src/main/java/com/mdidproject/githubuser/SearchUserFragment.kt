package com.mdidproject.githubuser

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdidproject.githubuser.adapter.UserListAdapter
import com.mdidproject.githubuser.databinding.FragmentSearchUserBinding
import com.mdidproject.githubuser.interfaces.ItemAdapterCallback
import com.mdidproject.githubuser.response.UserItem
import com.mdidproject.githubuser.viewmodel.UserSearchViewModel

class SearchUserFragment : Fragment() {
    private var _binding: FragmentSearchUserBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserSearchViewModel by viewModels()
    private val userList: MutableList<UserItem> = arrayListOf()
    private lateinit var rvAdapter: UserListAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchUserBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.hide()
        rvAdapter = UserListAdapter(userList)
        rvAdapter.setAdapterItemCallback(object : ItemAdapterCallback<UserItem> {
            override fun onItemClicked(view: View?, data: UserItem) {
                view?.let {
                    val action = SearchUserFragmentDirections.actionSearchUserFragmentToDetailUserActivity(data.username)
                    view.findNavController().navigate(action)
                }?: run {
                    Toast.makeText(activity, "failed to open detail ${data.username}", Toast.LENGTH_SHORT).show()
                }
            }
        })
        binding.apply {
            rvSearchUserList.layoutManager = LinearLayoutManager(context)
            rvSearchUserList.setHasFixedSize(true)
            rvSearchUserList.adapter = rvAdapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.pbSearch.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }

        binding.itSearch.addTextChangedListener(
            object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

                override fun afterTextChanged(text: Editable?) {
                    if (!text.isNullOrEmpty()){
                        viewModel.searchUsers(text.toString())
                        viewModel.userList.observe(viewLifecycleOwner){
                            userList.clear()
                            userList.addAll(it)
                            rvAdapter.notifyDataSetChanged()
                        }
                    }
                }

            }
        )
        viewModel.isError.observe(viewLifecycleOwner){
            if(it){
                Toast.makeText(activity, "${viewModel.errorMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}