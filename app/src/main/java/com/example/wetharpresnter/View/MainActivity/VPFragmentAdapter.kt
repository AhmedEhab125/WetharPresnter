package com.example.wetharpresnter.View.MainActivity

import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class VPFragmentAdapter(var list: List<Fragment>, fragmentManager: FragmentManager,lifecycle: Lifecycle) :FragmentStateAdapter(fragmentManager,lifecycle){
    override fun getItemCount(): Int {
       return list.size

    }

    override fun createFragment(position: Int): Fragment {
        return list.get(position)
    }


}