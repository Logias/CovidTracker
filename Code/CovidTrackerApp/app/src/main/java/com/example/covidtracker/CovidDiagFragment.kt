package com.example.covidtracker

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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

        val chipGroup1 = root.findViewById<ChipGroup>(R.id.chipGroup)
        val chipGroup2 = root.findViewById<ChipGroup>(R.id.chipGroup2)
        val analyzeButton = root.findViewById<Button>(R.id.analyzeButton)
        val ageEntry = root.findViewById<EditText>(R.id.ageEntry)


        chipGroup2.setOnCheckedChangeListener { group, checkedId: Int ->
            val chip: Chip? = root.findViewById(checkedId)
            chip?.let {
                // Show the checked chip text on toast message
                Log.e(TAG, chip.text as String)
                // Do code here to pass Chip data to analyze frag using FragmentManager
            }
        }


        //this button should pass all the data including the age into next frag
        analyzeButton.setOnClickListener(View.OnClickListener {
            var msg = "Chips checked are:"
            val chipsCount = chipGroup1.childCount
            if (chipsCount == 0) {
                msg += " None!!"
            } else {
                var i = 0
                while (i < chipsCount) {
                    val chip = chipGroup1.getChildAt(i) as Chip
                    if (chip.isChecked) {
                        msg += chip.text.toString() + " "
                    }
                    i++
                }
            }
            // show message
            Log.e(TAG, msg)
        })


        return root
    }
}
