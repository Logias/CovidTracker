package com.example.covidtracker.covid_data

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.covidtracker.R
import com.example.covidtracker.covid_data.database.USADatabase
import com.example.covidtracker.covid_data.repo.CovidDataRepository
import com.example.covidtracker.databinding.FragmentCovidDataBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class CovidDataFragment : Fragment() {

    private lateinit var binding: FragmentCovidDataBinding

    private val usaDao by lazy {
        USADatabase.getInstance(requireContext()).usaDao
    }

    private val repository: CovidDataRepository by lazy {
        CovidDataRepository(usaDao)
    }

    private val covidDataViewModel by lazy {
        val factory = CovidDataViewModelFactory(repository)
        ViewModelProvider(this, factory).get(CovidDataViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_covid_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCovidDataBinding.bind(view)
        observeUSAData()
    }

    private fun observeUSAData() {
        covidDataViewModel.usaData?.observe(viewLifecycleOwner, Observer { usaData ->

            usaData?.apply {
                // using German Locale for dot separator
                val nf = NumberFormat.getInstance(Locale.GERMAN)

                binding.population.text =  nf.format(population)
                binding.totalCases.text = nf.format(cases)
                binding.activeCases.text = nf.format(active)
                binding.newCases.text = nf.format(todayCases)
                binding.newDeaths.text = nf.format(todayDeaths)
                binding.totalDeaths.text = nf.format(deaths)
                binding.newRecovered.text = nf.format(todayRecovered)
                binding.totalRecovered.text = nf.format(recovered)
                binding.lastUpdated.text = getDateTime(updated)

            } ?: Log.d("DEBUG:CovidDataFragment", "usaData is Null")

        })
    }

    private fun getDateTime(updated: Long): String {
        val sdf = SimpleDateFormat("MMM dd, YYYY '@' hh:mm a", Locale.ROOT)
        val dateTime = Date(updated)
        return sdf.format(dateTime)
    }





}