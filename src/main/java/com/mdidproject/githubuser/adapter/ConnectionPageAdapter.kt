package com.mdidproject.githubuser.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mdidproject.githubuser.FollowersFragment
import com.mdidproject.githubuser.FollowingFragment

class ConnectionPageAdapter(activity: AppCompatActivity, private val username: String) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment? = when (position) {
            0 -> FollowersFragment(username)
            1 -> FollowingFragment(username)
            else -> null
        }
        return fragment as Fragment
    }
}