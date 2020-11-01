package com.example.covidtracker.covid_data

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import java.util.*

class StatesDataFragment : Fragment() {

    private lateinit var binding: FragmentStatesDataBinding

    private val adapter by lazy {
        StatesAdapter()
    }

    private val covidDataViewModel by lazy {
        val usaDao = USADatabase.getInstance(requireContext()).usaDao
        val stateDao = StatesDatabase.getInstance(requireContext()).stateDao
        val repository = CovidDataRepository(usaDao, stateDao)
        val factory = CovidDataViewModelFactory(repository)
        ViewModelProvider(this, factory).get(CovidDataViewModel::class.java)
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

        binding.statesRv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.statesRv.adapter = adapter

        covidDataViewModel.statesData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (!it.isNullOrEmpty()) {
                adapter.setData(it)
                binding.lastUpdated.text = getDateTime(it.first().updated)
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

class StatesAdapter : RecyclerView.Adapter<StatesAdapter.ViewHolder>() {

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