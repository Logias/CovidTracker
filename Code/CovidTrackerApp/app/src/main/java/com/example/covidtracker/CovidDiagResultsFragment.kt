package com.example.covidtracker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.chip.ChipGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.ArrayList

class CovidDiagResultsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_covid_diag_results, container, false)
        val resultTitle = root.findViewById<TextView>(R.id.resultTextView)
        val resultDescr = root.findViewById<TextView>(R.id.resultDescTextView)
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)

        val gson = Gson()
        val json: String? = sharedPref?.getString("Symptoms List", null)
        val type: Type = object : TypeToken<java.util.ArrayList<String?>?>() {}.type
        val symptomsSet: ArrayList<String> = gson.fromJson(json, type)
        //val symptomsSet = sharedPref?.getStringSet("Symptoms List", null)
        val symptomsDate = sharedPref?.getString("Symptoms Date", null)
        val ageEntry = sharedPref?.getInt("Age Entry", 0)
        if (symptomsSet != null) {
            println(
                symptomsSet.joinToString(
                    prefix = "[",
                    separator = ":",
                    postfix = "]")
            )
        }
        if (symptomsSet != null) {
            if(symptomsSet.contains("Trouble Breathing") || symptomsSet.contains("Persistent Pain or Pressure in Chest") || symptomsSet.contains("Bluish Face or Lips")) {
                resultTitle.text = "Emergency!"
                resultDescr.text = "Please seek medical attention immediately. Covid-19 may be the culprit or it could be likely that it is an effect from another deadlier infection."
            } else {
                if(symptomsDate == "Under 24 Hours") {
                    resultTitle.text = "Unlikely"
                    resultDescr.text = "Covid-19 symptoms may take from 2-14 days to show up. There is still a probability it is Covid-19 but it is most likely another infection."
                } else if (symptomsDate == "Over 2 Weeks") {
                    resultTitle.text = "Unlikely"
                    resultDescr.text = "Covid-19 symptoms usually expire after 2 weeks so it's more likely to be another infection."
                } else {
                    resultTitle.text = "Likely"
                    resultDescr.text = "Covid-19 symptoms show up 2-14 days after exposure with these symptoms. Please get tested and self isolate for 14 days."
                }
            }
        }
        return root
    }
}