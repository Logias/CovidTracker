package com.example.covidtracker.covid_data

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.bumptech.glide.Glide
import com.example.covidtracker.R
import com.example.covidtracker.covid_data.database.StatesDatabase
import com.example.covidtracker.covid_data.database.USADatabase
import com.example.covidtracker.covid_data.model.USADataModel
import com.example.covidtracker.covid_data.repo.CovidDataRepository
import com.example.covidtracker.databinding.FragmentCountryDataBinding
import com.example.covidtracker.databinding.FragmentCovidDataBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.llollox.androidtoggleswitch.widgets.ToggleSwitch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CountryDataFragment : Fragment() {

    private lateinit var binding: FragmentCountryDataBinding

    private val covidDataViewModel by lazy {
        val usaDao = USADatabase.getInstance(requireContext()).usaDao
        val stateDao = StatesDatabase.getInstance(requireContext()).stateDao
        val repository = CovidDataRepository(usaDao, stateDao)
        val factory = CovidDataViewModelFactory(repository)
        ViewModelProvider(requireActivity(), factory).get(CovidDataViewModel::class.java)
    }

    private val loadingProgressBar by lazy {
        MaterialDialog(requireContext()).customView(R.layout.loading_status)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCountryDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUSAData()
        observeUpdateStatus()
        setRefreshButtonListener()

    }

    private fun observeUpdateStatus() {
        covidDataViewModel.updateStatus.observe(viewLifecycleOwner, Observer { updateStatus ->
            updateStatus?.let { success ->
                loadingProgressBar.dismiss()
                if (success) {
                    Toast.makeText(requireContext(), "Update completed", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Update failed", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun observeUSAData() {
        covidDataViewModel.usaData.observe(viewLifecycleOwner, Observer { usaData ->

            if (usaData != null) {
                setupPieChart(usaData)
                usaData.apply {
                    binding.totalCases.text = cases.toString()
                    binding.totalRecovered.text = recovered.toString()
                    binding.totalDeaths.text = deaths.toString()
                    binding.todayCases.text = todayCases.toString()
                    binding.todayRecovered.text = todayRecovered.toString()
                    binding.todayDeaths.text = todayDeaths.toString()
                    binding.lastUpdated.text = getDateTime(updated)
                }
            }

        })
    }

    private fun setupPieChart(usaData: USADataModel) {

        val active = usaData.active.toFloat()
        val recovered = usaData.recovered.toFloat()
        val deaths = usaData.deaths.toFloat()

        val todayCases = usaData.todayCases.toFloat()
        val todayRecovered = usaData.todayRecovered.toFloat()
        val todayDeaths = usaData.todayDeaths.toFloat()

        Glide.with(this)
            .load(usaData.flagUrl)
            .override(120, 120)
            .into(binding.flag)

        val entries = ArrayList<PieEntry>()

        entries.add(PieEntry(active, "Active"))
        entries.add(PieEntry(recovered, "Recovered"))
        entries.add(PieEntry(deaths, "Deaths"))

        val set = PieDataSet(entries, null)

        val colors = ArrayList<Int>()
        colors.add(resources.getColor(R.color.active, null))
        colors.add(resources.getColor(R.color.recovered, null))
        colors.add(resources.getColor(R.color.deaths, null))

        set.colors = colors

        val data = PieData(set)
        data.setValueTextSize(15f)
        data.setValueTextColor(Color.WHITE)

        val legend = binding.pieChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.textSize = 15f
        legend.setDrawInside(false)

        binding.pieChart.holeRadius = 35f
        binding.pieChart.transparentCircleRadius = 40f
        binding.pieChart.data = data
        binding.pieChart.description.isEnabled = false
        binding.pieChart.isHighlightPerTapEnabled = true
        binding.pieChart.isSelected = true
        binding.pieChart.setNoDataText("Database not updated yet!")
        binding.pieChart.setNoDataTextColor(Color.DKGRAY)
        binding.pieChart.invalidate()

        binding.toggle.setCheckedPosition(0)

        binding.toggle.onChangeListener = object : ToggleSwitch.OnChangeListener {
            override fun onToggleSwitchChanged(position: Int) {
                when (position)  {
                    0 -> {
                        binding.pieChart.data = null

                        val entries = ArrayList<PieEntry>()
                        entries.add(PieEntry(active, "Active"))
                        entries.add(PieEntry(recovered, "Recovered"))
                        entries.add(PieEntry(deaths, "Deaths"))
                        val set = PieDataSet(entries, null)
                        val colors = ArrayList<Int>()
                        colors.add(resources.getColor(R.color.active, null))
                        colors.add(resources.getColor(R.color.recovered, null))
                        colors.add(resources.getColor(R.color.deaths, null))
                        set.colors = colors

                        val data = PieData(set)
                        data.setValueTextSize(15f)
                        data.setValueTextColor(Color.WHITE)

                        binding.pieChart.data = data
                        binding.pieChart.data.setValueTextSize(15f)
                        binding.pieChart.data.setValueTextColor(Color.WHITE)
                        binding.pieChart.notifyDataSetChanged()
                        binding.pieChart.invalidate()
                    } else -> {
                    binding.pieChart.data = null

                    val entries = ArrayList<PieEntry>()
                    entries.add(PieEntry(todayCases, "Active"))
                    entries.add(PieEntry(todayRecovered, "Recovered"))
                    entries.add(PieEntry(todayDeaths, "Deaths"))
                    val set = PieDataSet(entries, null)
                    val colors = ArrayList<Int>()
                    colors.add(resources.getColor(R.color.active, null))
                    colors.add(resources.getColor(R.color.recovered, null))
                    colors.add(resources.getColor(R.color.deaths, null))
                    set.colors = colors

                    val data = PieData(set)
                    data.setValueTextSize(15f)
                    data.setValueTextColor(Color.WHITE)

                    binding.pieChart.data = data
                    binding.pieChart.data.setValueTextSize(15f)
                    binding.pieChart.data.setValueTextColor(Color.WHITE)
                    binding.pieChart.notifyDataSetChanged()
                    binding.pieChart.invalidate()

                    // show data not updated message instead of blank screen when values are 0s
                    if (todayCases == 0f && todayRecovered == 0f && todayDeaths == 0f) {
                        binding.pieChart.clear()
                    }
                }
                }
            }

        }

    }

    private fun getDateTime(updated: Long): String {
        val sdf = SimpleDateFormat("MMM dd, YYYY '@' hh:mm a z", Locale.US)
//        sdf.timeZone = TimeZone.getTimeZone("GMT")
        val dateTime = Date(updated)
        return sdf.format(dateTime)
    }

    private fun setRefreshButtonListener() {
        binding.refreshButton.setOnClickListener {
            loadingProgressBar.show()
            covidDataViewModel.getUpdatedData()
        }
    }


}

