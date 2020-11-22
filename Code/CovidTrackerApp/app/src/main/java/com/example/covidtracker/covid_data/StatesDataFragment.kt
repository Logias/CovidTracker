package com.example.covidtracker.covid_data

import android.app.Activity
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.covidtracker.App
import com.example.covidtracker.R
import com.example.covidtracker.covid_data.database.StatesDatabase
import com.example.covidtracker.covid_data.database.USADatabase
import com.example.covidtracker.covid_data.model.StateDataModel
import com.example.covidtracker.covid_data.repo.CovidDataRepository
import com.example.covidtracker.databinding.FragmentStatesDataBinding
import kotlinx.android.synthetic.main.state_list_item.view.*
import java.text.SimpleDateFormat
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.setPeekHeight
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.example.covidtracker.databinding.BottomSheetLayoutBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.llollox.androidtoggleswitch.widgets.ToggleSwitch
import java.util.*

class StatesDataFragment : Fragment() {

    private lateinit var binding: FragmentStatesDataBinding

    private val adapter by lazy {
        StatesAdapter(requireActivity())
    }

    private val covidDataViewModel by lazy {
        val usaDao = USADatabase.getInstance(requireContext()).usaDao
        val stateDao = StatesDatabase.getInstance(requireContext()).stateDao
        val repository = CovidDataRepository(usaDao, stateDao)
        val factory = CovidDataViewModelFactory(repository)
        ViewModelProvider(requireActivity(), factory).get(CovidDataViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatesDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeStateCovidData()
        observeUpdateStatus()
        setRefreshButtonListener()
    }

    private fun setupRecyclerView() {
        binding.statesRv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.statesRv.adapter = adapter
    }

    private fun observeStateCovidData() {
        covidDataViewModel.statesData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (!it.isNullOrEmpty()) {
                adapter.setData(it)
                binding.lastUpdated.text = getDateTime(it.first().updated)
            }
        })
    }

    private fun setRefreshButtonListener() {
        binding.refreshButton.setOnClickListener {
            covidDataViewModel.getUpdatedData()
        }
    }

    private fun observeUpdateStatus() {
        covidDataViewModel.updateStatus.observe(viewLifecycleOwner, Observer { updateStatus ->
            updateStatus?.let { success ->
                if (success) {
                    Toast.makeText(requireContext(), "Update completed", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Update failed", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun getDateTime(updated: Long): String {
        val sdf = SimpleDateFormat("MMM dd, YYYY '@' hh:mm a z", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        val dateTime = Date(updated)
        return sdf.format(dateTime)
    }

}

class StatesAdapter(val context: Activity) : RecyclerView.Adapter<StatesAdapter.ViewHolder>() {

    private val stateDataModels = mutableListOf<StateDataModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.state_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stateDataModels.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stateData = stateDataModels[position]
        holder.bind(stateData)

        holder.itemView.setOnClickListener {
            showBottomSheet(stateData)
        }
    }

    private fun MaterialDialog.setPeekHeightByPercentage(percentage: Int): MaterialDialog {
        require(percentage in 0..100) {  }
        val size = Point()
        this.window?.windowManager?.defaultDisplay?.getSize(size)
        this.setPeekHeight(literal = size.y * percentage / 100)
        return this
    }

    private fun showBottomSheet(stateData: StateDataModel) {
        val bottomSheet = MaterialDialog(context, BottomSheet()).apply {
            cornerRadius(16f)
            customView(R.layout.bottom_sheet_layout)
            setPeekHeightByPercentage(80)
        }

        val bottomSheetView = bottomSheet.getCustomView()
        val binding = BottomSheetLayoutBinding.bind(bottomSheetView)
        val resources = App.getAppContext().resources

        val active = stateData.active.toFloat()
        val recovered = stateData.recovered.toFloat()
        val deaths = stateData.deaths.toFloat()

        val todayCases = stateData.todayCases.toFloat()
        val todayRecovered = 0F // TODO set this n/a
        val todayDeaths = stateData.todayDeaths.toFloat()

        stateData.apply {
            binding.totalCases.text = cases.toString()
            binding.totalRecovered.text = recovered.toInt().toString()
            binding.totalDeaths.text = deaths.toInt().toString()
            binding.todayCases.text = todayCases.toInt().toString()
            binding.todayRecovered.text = "Unknown"
            binding.todayDeaths.text = todayDeaths.toInt().toString()
            binding.countryLabel.text = state
        }

        binding.flag.setImageResource(getFlagDrawableID(stateData.state))

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

        bottomSheet.show()
    }

    private fun getFlagDrawableID(stateName: String): Int {
        val context = App.getAppContext()
        val drawableName = stateName.replace(" ", "_").toLowerCase()
        return context.resources.getIdentifier(drawableName, "drawable", context.packageName)
    }

    fun setData(stateDataModels: List<StateDataModel>) {
        this.stateDataModels.clear()
        this.stateDataModels.addAll(stateDataModels.sortedBy {
            it.state
        })
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var stateData: StateDataModel

        fun bind(stateData: StateDataModel) {
            this.stateData = stateData

            itemView.state_name.text = stateData.state
            itemView.total_cases.text = stateData.cases.toString()
            itemView.total_recovered.text = stateData.recovered.toString()
            itemView.total_deaths.text = stateData.deaths.toString()
            itemView.state_flag.setImageResource(getFlagDrawableID(stateData.state))
        }

        private fun getFlagDrawableID(stateName: String): Int {
            val context = App.getAppContext()
            val drawableName = stateName.replace(" ", "_").toLowerCase()
            return context.resources.getIdentifier(drawableName, "drawable", context.packageName)
        }

    }

}