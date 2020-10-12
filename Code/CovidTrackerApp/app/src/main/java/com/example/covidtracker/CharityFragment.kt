package com.example.covidtracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
class CharityFragment : Fragment() {
    private var charityViewModel: CharityViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        charityViewModel = ViewModelProvider(this).get(CharityViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_charity, container, false)
        return root
    }
}