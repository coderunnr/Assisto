package com.kani.project.assisto;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.kani.project.assisto.adapter.MyAdapterDoctorPlace;
import com.kani.project.assisto.adapter.MyAdapterMainChat;
import com.kani.project.assisto.connectionutils.Connection;
import com.kani.project.assisto.connectionutils.models.ChatModel;
import com.kani.project.assisto.connectionutils.models.DoctorsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 1/10/16.
 */
public class DoctorsPlace extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

        GoogleApiClient mGoogleApiClient;
    List<DoctorsModel> doctorsModelList=new ArrayList<>();
    int j=0;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    RecyclerView.Adapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_doctors);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar_doctor);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .addApi(LocationServices.API)
                    .enableAutoManage(this, this)
                    .build();
        }

        recyclerView=(RecyclerView)findViewById(R.id.doctors_result);
        layoutManager=new LinearLayoutManager(DoctorsPlace.this);
        //layoutManager.setReverseLayout(true);
        // layoutManager.setStackFromEnd(true);


        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();

        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();

        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

if(mLastLocation!=null) {
    String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude() + "&radius=5000&type=doctor&key=AIzaSyBwikWKrzi7cwJkEZF7G4QYjofAMGsVgc8";
    SearchDoctors searchDoctors = new SearchDoctors(url, new JSONObject());
    searchDoctors.execute();
}


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    public class SearchDoctors extends AsyncTask<Void,Void,String>
    {
        String url;
        JSONObject params;

        public SearchDoctors(String url,JSONObject params) {
            this.url=url;
            this.params=params;
        }


        @Override
        protected String doInBackground(Void... voids) {
            Connection connection=new Connection(url,params,DoctorsPlace.this);
            return connection.connectiontask();
        }

        @Override
        protected void onPostExecute(String s) {
            Log.v(getClass().getSimpleName(),s);
            try {
                doctorsModelList.clear();
                JSONObject main=new JSONObject(s);
                String status=main.getString("status");

                if(status.equalsIgnoreCase("OK"))
                {
                    JSONArray array=main.getJSONArray("results");
                    for(int i=0;i<array.length();i++)
                    {
                        DoctorsModel doctorsModel=new DoctorsModel();
                        JSONObject jsonObject=array.getJSONObject(i);
                        String placeid=jsonObject.getString("place_id");
                        String name=jsonObject.getString("name");
                        String vicinity=jsonObject.getString("vicinity");
                        int price=300;
                        doctorsModel.setPrice(price);
                        doctorsModel.setName(name);
                        doctorsModel.setVicinity(vicinity);
                        doctorsModel.setPlace_id(placeid);
                        doctorsModelList.add(doctorsModel);
                        Log.v(getClass().getSimpleName(),name+vicinity);

                    }
                    adapter=new MyAdapterDoctorPlace(doctorsModelList,DoctorsPlace.this);
                    recyclerView.setAdapter(adapter);
                  //  getPlaceDetails();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }




}
