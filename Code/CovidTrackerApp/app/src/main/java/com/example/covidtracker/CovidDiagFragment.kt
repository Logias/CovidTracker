package com.example.covidtracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.fragment_covid_diag.*

class CovidDiagFragment : Fragment() {
    private var covidDiagViewModel: CovidDiagViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        covidDiagViewModel = ViewModelProvider(this).get(CovidDiagViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_covid_diag, container, false)

        val chipGroup1 = root.findViewById<TextView>(R.id.chipGroup)

/**
        chipGroup.setOnCheckedChangeListener { chipGroup, i ->
            val chip: Chip = chipGroup.findViewById(i)
            if (chip != null) Toast.makeText(
                getApplicationContext(),
                "Chip is " + chip.chipText,
                Toast.LENGTH_SHORT
            ).show()
        }

        val chip: Chip = findViewById(R.id.chip)
        chip.setOnCloseIconClickListener {
            Toast.makeText(getApplicationContext(), "Close is Clicked", Toast.LENGTH_SHORT)
                .show()
        }
*/
        return root
    }
}