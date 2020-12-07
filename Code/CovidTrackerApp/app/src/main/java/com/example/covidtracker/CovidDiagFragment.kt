package com.example.covidtracker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.gson.Gson
import java.lang.Integer.parseInt


class CovidDiagFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_covid_diag, container, false)

        val chipGroup1 = root.findViewById<ChipGroup>(R.id.chipGroup)
        val chipGroup2 = root.findViewById<ChipGroup>(R.id.chipGroup2)
        val analyzeButton = root.findViewById<Button>(R.id.analyzeButton)
        val ageEntry = root.findViewById<EditText>(R.id.ageEntry)
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)

        analyzeButton.setOnClickListener(View.OnClickListener {
            var symptomsList = ArrayList<String>()
            var symptomsDate = ""
            val chips1Count = chipGroup1.childCount
            val chips2Count = chipGroup2.childCount

            var i = 0
            while (i < chips2Count) {
                val chip = chipGroup2.getChildAt(i) as Chip
                if (chip.isChecked) {
                    symptomsDate = chip.text.toString()
                    break
                }
                i++
            }

            var numeric = true
            try {
                val num = parseInt(ageEntry.text.toString())
            } catch (e: NumberFormatException) {
                numeric = false
            }

            if (symptomsDate == "") {
                Toast.makeText(
                    activity,
                    "Please select the time indicating how long you've been experiencing symptoms.",
                    Toast.LENGTH_LONG
                ).show()
            } else if (!numeric || ageEntry.text.toString() == "0") {
                Toast.makeText(activity, "Please enter a valid number for age", Toast.LENGTH_LONG)
                    .show()
            } else {
                i = 0
                while (i < chips1Count) {
                    val chip = chipGroup1.getChildAt(i) as Chip
                    if (chip.isChecked) {
                        symptomsList.add(chip.text.toString())
                    }
                    i++
                }
                if (sharedPref != null) {
                    with(sharedPref.edit()) {
                        val gson = Gson()
                        val json: String = gson.toJson(symptomsList)
                        putString("Symptoms List", json)
                        putString("Symptoms Date", symptomsDate)
                        putInt("Age Entry", parseInt(ageEntry.text.toString()))
                        apply()
                    }
                }

                /*Uses the activity's navigation controller to change fragments to the results fragment*/
                val navController = Navigation.findNavController(
                    requireActivity().findViewById(R.id.nav_host_fragment)
                )
                navController.navigate(R.id.action_diag_to_result)
            }
        })
        return root
    }
}