package com.mdidproject.githubuser

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdidproject.githubuser.adapter.UserListAdapter
import com.mdidproject.githubuser.data.local.entity.UserEntity
import com.mdidproject.githubuser.data.local.entity.UserWithFavStatusLiveData
import com.mdidproject.githubuser.databinding.FragmentFavoritesBinding
import com.mdidproject.githubuser.interfaces.ItemAdapterCallback
import com.mdidproject.githubuser.viewmodel.UserViewModel
import com.mdidproject.githubuser.viewmodel.ViewModelFactory


class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val userList = ArrayList<UserEntity>()
    private lateinit var rvAdapter: UserListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "Favorites"

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: UserViewModel by viewModels {
            factory
        }

        rvAdapter = UserListAdapter(userList, viewLifecycleOwner, viewModel)

        binding.apply {
            rvFavorites.setHasFixedSize(true)
            rvFavorites.layoutManager = LinearLayoutManager(context)
            rvFavorites.adapter = rvAdapter
        }

        rvAdapter.setAdapterItemCallback(object : ItemAdapterCallback<UserWithFavStatusLiveData> {
            override fun onItemClicked(view: View?, data: UserWithFavStatusLiveData) {
                view?.let {
                    val action = FavoritesFragmentDirections.actionFavoritesFragmentToDetailUserFragment(data.username)
                    view.findNavController().navigate(action)
                }?: run {
                    Toast.makeText(activity, "failed to open detail ${data.username}", Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.getFavUser().observe(viewLifecycleOwner) {
            binding.pbFavorites.visibility = View.GONE
            if (it.isEmpty()){
                binding.tvNoFavorites.visibility = View.VISIBLE
            }

            showList(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.removeItem(R.id.favorites_menu)
        menu.removeItem(R.id.search_menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun showList(users: List<UserEntity>){
        userList.clear()
        userList.addAll(users)
        rvAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}