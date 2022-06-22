package com.example.stressmeter.ui.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stressmeter.R
import com.example.stressmeter.ui.image.PicturesFragment
import com.example.stressmeter.ui.image.PicturesViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader


class ChartFragment : Fragment() {

    val STRESS_SCORES = ArrayList<Int>()
    val STRESS_TIME = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentResultsView = inflater.inflate(R.layout.fragment_chart, container, false)

        // Picture View Modal helps in turining on and off ringer and vibrations
        val picturesViewModel = ViewModelProvider(requireActivity())[PicturesViewModel::class.java]
        if(picturesViewModel.VIBRATION == true){
            PicturesFragment.ringPlayer.stop()
            PicturesFragment.vibrator.cancel()
            picturesViewModel.VIBRATION = false
        }

        // Loads the CSV Data
        loadCSVData(fragmentResultsView)
        // Creates the CSV Data
        createTable(fragmentResultsView)
        // Creates the Table
        createChart(fragmentResultsView)
        return fragmentResultsView
    }

    fun createChart(fragmentResultsView: View){
        // Chart library that was used.
        // https://github.com/PhilJay/MPAndroidChart
        val stressLineChart = fragmentResultsView.findViewById<LineChart>(R.id.stress_chart_id)
        stressLineChart.setNoDataTextColor(getResources().getColor(R.color.purple_200))
        stressLineChart.setNoDataText("No Chart Data Available");

        stressLineChart.setTouchEnabled(true)
        stressLineChart.setScaleEnabled(true)
        stressLineChart.setDrawGridBackground(false)

        val xAxis= stressLineChart.getXAxis()
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.purple_200)


        val yAxis = stressLineChart.getAxisLeft()
        yAxis.textColor = ContextCompat.getColor(requireContext(), R.color.purple_200)

        val yAxis2 = stressLineChart.getAxisRight()
        yAxis2.textColor = ContextCompat.getColor(requireContext(), R.color.purple_200)

        val description = Description()
        description.text = "Stress Level Chart"

        stressLineChart.description = description

        val lineEntry = ArrayList<Entry>();
        var index = 0
        for (stress_score in STRESS_SCORES){
            lineEntry.add(Entry(index.toFloat(), stress_score.toFloat()))
            index += 1
        }

        val lineDataSet = LineDataSet(lineEntry, "Instances")
        lineDataSet.color = ContextCompat.getColor(requireContext(), R.color.purple_200)

        val resultantData = LineData(lineDataSet)
        stressLineChart.data = resultantData
    }

    fun createTable(fragmentResultsView: View){
        val timeScoreTable = fragmentResultsView.findViewById<TableLayout>(R.id.time_score_table)
        timeScoreTable.setStretchAllColumns(true)

        val numOfRows = STRESS_TIME.size - 1
        for (row in 0..numOfRows){
            val customRowParams = TableLayout.LayoutParams()
            customRowParams.setMargins(2)

            val tableRow = TableRow(requireActivity())
            tableRow.layoutParams = customRowParams
            tableRow.setBackgroundColor(getResources().getColor(R.color.white))

            val timeView = TextView(requireActivity())
            timeView.textSize = 15f
            timeView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            timeView.setText(STRESS_TIME[row])
            tableRow.addView(timeView)

            val stressView = TextView(requireActivity())
            stressView.textSize = 15f
            stressView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            stressView.setText(STRESS_SCORES[row].toString())
            tableRow.addView(stressView)
            timeScoreTable.addView(tableRow)
        }


    }
    fun loadCSVData(fragmentResultsView: View){
        val filename = "stress_timestamp.csv"
        val csvFile = File(requireActivity().getExternalFilesDir(null),filename)
        if (!csvFile.exists()){
            return
        }

        val fileInputStream = FileInputStream(csvFile)
        val inputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader = BufferedReader(inputStreamReader)
        var text: String? = null
        while ({ text = bufferedReader.readLine(); text }() != null) {
            val time_score = text?.split(',')
            val time = time_score?.get(0)
            val score = time_score?.get(1)?.toInt()
            if (score != null) {
                STRESS_SCORES.add(score)
            }
            if (time != null) {
                STRESS_TIME.add(time)
            }
        }
        fileInputStream.close()
    }
}