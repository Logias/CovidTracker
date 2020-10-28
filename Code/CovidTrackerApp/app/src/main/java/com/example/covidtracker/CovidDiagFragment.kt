package com.example.covidtracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
class CovidDiagFragment : Fragment() {
    private var covidDiagViewModel: CovidDiagViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        covidDiagViewModel = ViewModelProvider(this).get(CovidDiagViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_covid_diag, container, false)
        return root
    }
}