package com.mdidproject.githubuser

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.mdidproject.githubuser.adapter.ConnectionPageAdapter
import com.mdidproject.githubuser.databinding.FragmentDetailUserBinding
import com.mdidproject.githubuser.viewmodel.DetailUserViewModel

class DetailUserFragment : Fragment() {
    companion object {
        private val TAB_TITLES = arrayListOf(
            R.string.followers_heading,
            R.string.following_heading
        )
    }

    private var _binding: FragmentDetailUserBinding? = null
    private val binding get() = _binding!!
    private val args: DetailUserFragmentArgs by navArgs()
    private val viewModel: DetailUserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailUserBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as AppCompatActivity
        val pager = ConnectionPageAdapter(activity, args.username)
        binding.vpConnectionList.adapter = pager

        viewModel.getUser(args.username)
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.INVISIBLE
            binding.vpConnectionList.visibility = if (!it) View.VISIBLE else View.INVISIBLE
            binding.tlConnection.visibility = if (!it) View.VISIBLE else View.INVISIBLE
        }
        viewModel.user.observe(viewLifecycleOwner) { data ->
            binding.apply {
                tvHeadingFollowers.visibility = View.VISIBLE
                tvHeadingFollowing.visibility = View.VISIBLE
                tvHeadingRepositories.visibility = View.VISIBLE
                ibShare.visibility = View.VISIBLE
                Glide.with(activity)
                    .load(data.avatarUrl)
                    .circleCrop()
                    .into(ivAvatar)
                tvName.text = data.name
                tvUsername.text = "@" + data.username
                tvCompany.text = data.company
                tvLocation.text = data.location
                tvFollowersNumber.text = data.followers.toString()
                tvFollowingNumber.text = data.following.toString()
                tvRepositoriesNumber.text = data.publicRepos.toString()
                ibShare.setOnClickListener{
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, "Hey Look that awesome ${data.name} Github !")
                        putExtra(Intent.EXTRA_TEXT, "Hey Look that awesome ${data.name} Github ! \nhttps://github.com/${data.username.lowercase()}")
                    }

                    startActivity(Intent.createChooser(intent, "Share link!"))
                }

                TabLayoutMediator(tlConnection, vpConnectionList) { tab, position ->
                    tab.text = resources.getString(TAB_TITLES[position])
                }.attach()
            }

        }

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