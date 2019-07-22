package com.example.projectactivityld;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;



public class ShowFragment extends Fragment {

    int time= 1000*60*10;
    int dist= 500;

    View view;
    ListView resultList;
    ProgressBar progressBar;
    LocationViewModel locationViewModel;
    Button button_Start;
    Button button_Stop;
    LocationManager locationManager;
    static boolean start_clicked=false;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.show_fragment, container, false);
        resultList = view.findViewById(R.id.resultList);
        progressBar = view.findViewById(R.id.progressBar);
        button_Start = view.findViewById(R.id.button_Start);
        button_Stop = view.findViewById(R.id.button_Stop);


        button_Start_Click();
        button_Stop_Click();

        progressBar.setVisibility(View.GONE);

        if(!start_clicked) {
            button_Stop.setVisibility(View.GONE);
            button_Start.setVisibility(View.VISIBLE);
            resultList.setVisibility(View.GONE);
        }else{
            button_Start.setVisibility(View.GONE);
            button_Stop.setVisibility(View.VISIBLE);
            resultList.setVisibility(View.VISIBLE);
        }

        //get viewModel
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);

        //Observe Location LiveData
        locationViewModel.getLocationList().observe(this, locations -> {

            CustomAdapterLocation adapter = new CustomAdapterLocation(this.getContext(), R.layout.row_layout, locations);
            progressBar.setVisibility(View.GONE);
            resultList.setAdapter(adapter);

        });

        //Observe Error LiveData
        locationViewModel.getError().observe(this, error -> {

            Log.d("Connection", "error toast");
            Toast.makeText(this.getContext(),"Impossible to retrieve data", Toast.LENGTH_LONG).show();

        });


        //Observe dataLoading for progressBar
        locationViewModel.getDataLoading().observe(this, dataLoading ->{

           int state= (dataLoading) ? View.VISIBLE : View.GONE;
           progressBar.setVisibility(state);

        });


        //Set location Manager
        locationManager= (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationViewModel.setLocationManager(locationManager);

        //Set request queue
        locationViewModel.setRequestQueue(this.getContext());

        return view;

    }






    private void button_Start_Click(){
         button_Start.setOnClickListener((v)->{

             button_Start.setVisibility(View.GONE);
             progressBar.setVisibility(View.VISIBLE);
             button_Stop.setVisibility(View.VISIBLE);
             resultList.setVisibility(View.VISIBLE);
             start_clicked=true;

             //start location update
             locationViewModel.setLocationUpdate(0,dist);


         } );
    }

    private void button_Stop_Click(){
        button_Stop.setOnClickListener((v)->{

            locationViewModel.removeLocationUpdate();


            button_Stop.setVisibility(View.GONE);
            button_Start.setVisibility(View.VISIBLE);
            resultList.setVisibility(View.GONE);
            resultList.setAdapter(null);
            start_clicked=false;


        });
    }

}
