package com.example.projectactivitykot

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import kotlinx.android.synthetic.main.show_fragment.*

class ShowFragment : Fragment() {


    private lateinit  var locationViewModel: LocationViewModel
    private var start_clicked=false
    private lateinit var locationManager : LocationManager
    private lateinit var viewM : View
    private lateinit var buttonStart: Button
    private lateinit var  buttonStop: Button
    private lateinit var resultList: ListView
    private lateinit var progressBar: ProgressBar

    private val time = 1000 * 60 * 10
    private val dist = 500



    companion object{
        fun newInstance(): ShowFragment{
            return ShowFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        viewM = inflater.inflate(R.layout.show_fragment, container, false)

        buttonStart= viewM.findViewById(R.id.button_Start)
        buttonStop= viewM.findViewById(R.id.button_Stop)
        progressBar= viewM.findViewById(R.id.progressBar)
        resultList= viewM.findViewById(R.id.resultList)



        button_Start_Click()
        button_Stop_Click()

        progressBar.visibility=View.GONE

        if(!start_clicked){

            buttonStop.visibility=View.GONE
            buttonStart.visibility=View.VISIBLE
            resultList.visibility= View.GONE

        }else{

            buttonStart.visibility=View.GONE
            buttonStop.visibility=View.VISIBLE
            resultList.visibility=View.VISIBLE

        }

        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)


        //Observe for Location
        locationViewModel.getLocationList().observe(this, Observer { locations ->


            val adapter =  CustomAdapterLocation(context!!, R.layout.row_layout, locations!!)
            resultList.adapter= adapter
            progressBar.visibility=View.GONE

        })

        //Observer for error
        locationViewModel.getError().observe(this, Observer {
            Log.d("Connection", "error toast")
            Toast.makeText(this.context, "Impossible to retrieve data", Toast.LENGTH_LONG).show()
        })


        //Observer for progressBar
        locationViewModel.getDataLoading().observe(this, Observer { dataLoading ->

            progressBar.visibility= if(dataLoading!!) View.VISIBLE else View.GONE

        })


        //Set Location Manager
        locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationViewModel.setLocationManager(locationManager)

        return viewM
    }

    private fun button_Start_Click(){
        buttonStart.setOnClickListener{

            buttonStart.visibility=View.GONE
            progressBar.visibility=View.VISIBLE
            buttonStop.visibility=View.VISIBLE
            resultList.visibility=View.VISIBLE
            start_clicked=true

            //start update
            locationViewModel.setLocationUpdate(0, dist.toFloat() )


        }

    }

    private fun button_Stop_Click(){
        buttonStop.setOnClickListener {

            //remove Update
            locationViewModel.removeLocationUpdate()

            buttonStop.visibility=View.GONE
            buttonStart.visibility=View.VISIBLE
            resultList.visibility=View.GONE
            resultList.adapter=null
            start_clicked=false

        }
    }

}