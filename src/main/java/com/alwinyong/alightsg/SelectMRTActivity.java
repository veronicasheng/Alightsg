package com.alwinyong.alightsg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class SelectMRTActivity extends FragmentActivity {

    private int selected = -1;

    private Spinner select_spinner;
    private Spinner start_spinner;
    private Spinner end_spinner;

    private ArrayList<String> NS_Adapter_Array = new ArrayList<>();
    private ArrayList<String> EW_Adapter_Array = new ArrayList<>();
    private ArrayList<String> CG_Adapter_Array = new ArrayList<>();
    private ArrayList<String> NE_Adapter_Array = new ArrayList<>();
    private ArrayList<String> CC_Adapter_Array = new ArrayList<>();
    private ArrayList<String> CE_Adapter_Array = new ArrayList<>();
    private ArrayList<String> DT_Adapter_Array = new ArrayList<>();
    private ArrayList<String> BP_Adapter_Array = new ArrayList<>();
    private ArrayList<String> SE_Adapter_Array = new ArrayList<>();
    private ArrayList<String> SW_Adapter_Array = new ArrayList<>();
    private ArrayList<String> PW_Adapter_Array = new ArrayList<>();
    private ArrayList<String> PE_Adapter_Array = new ArrayList<>();
//  private ArrayList<String> SK_Adapter_Array = new ArrayList<>();
//  private ArrayList<String> PG_Adapter_Array = new ArrayList<>();

    private GoogleMap gmap;
    private SupportMapFragment gmapFragment;

    // Initial Setup
    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activty_select_mrt);

        start_spinner = (Spinner) findViewById(R.id.spinner_select_start);
        end_spinner = (Spinner) findViewById(R.id.spinner_select_end);

        // Setting up Select MRT Line Select
        select_spinner = (Spinner) findViewById(R.id.spinner_select_line);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MRTStations.MRT_Line_List);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        select_spinner.setAdapter(spinnerArrayAdapter);

        // Setup Array for Adapters
        for (int i = 0; i < MRTStations.NSLine.length; i++) {
            NS_Adapter_Array.add(MRTStations.NSLine[i][0]);
        }
        for (int i = 0; i < MRTStations.EWLine.length; i++) {
            EW_Adapter_Array.add(MRTStations.EWLine[i][0]);
        }
        for (int i = 0; i < MRTStations.CGLine.length; i++) {
            CG_Adapter_Array.add(MRTStations.CGLine[i][0]);
        }
        for (int i = 0; i < MRTStations.NELine.length; i++) {
            NE_Adapter_Array.add(MRTStations.NELine[i][0]);
        }
        for (int i = 0; i < MRTStations.CCLine.length; i++) {
            CC_Adapter_Array.add(MRTStations.CCLine[i][0]);
        }
        for (int i = 0; i < MRTStations.CELine.length; i++) {
            CE_Adapter_Array.add(MRTStations.CELine[i][0]);
        }
        for (int i = 0; i < MRTStations.DTLine.length; i++) {
            DT_Adapter_Array.add(MRTStations.DTLine[i][0]);
        }
        for (int i = 0; i < MRTStations.BPLRT.length; i++) {
            BP_Adapter_Array.add(MRTStations.BPLRT[i][0]);
        }
        for (int i = 0; i < MRTStations.SELRT.length; i++) {
            SE_Adapter_Array.add(MRTStations.SELRT[i][0]);
        }
        for (int i = 0; i < MRTStations.SWLRT.length; i++) {
            SW_Adapter_Array.add(MRTStations.SWLRT[i][0]);
        }
        for (int i = 0; i < MRTStations.PELRT.length; i++) {
            PE_Adapter_Array.add(MRTStations.PELRT[i][0]);
        }
        for (int i = 0; i < MRTStations.PWLRT.length; i++) {
            PW_Adapter_Array.add(MRTStations.PWLRT[i][0]);
        }
        //  for (int i = 0; i < q_location_data.SKLRT.length;  i++) { SK_Adapter_Array.add(q_location_data.SKLRT[i][0]);  }
        //  for (int i = 0; i < q_location_data.PGLRT.length;  i++) { PG_Adapter_Array.add(q_location_data.PGLRT[i][0]);  }

        // Setup Adapters
        final ArrayAdapter<String> NS_Adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, NS_Adapter_Array);
        NS_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final ArrayAdapter<String> EW_Adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, EW_Adapter_Array);
        EW_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final ArrayAdapter<String> CG_Adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CG_Adapter_Array);
        CG_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final ArrayAdapter<String> NE_Adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, NE_Adapter_Array);
        NE_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final ArrayAdapter<String> CC_Adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CC_Adapter_Array);
        CC_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final ArrayAdapter<String> CE_Adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CE_Adapter_Array);
        CE_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final ArrayAdapter<String> DT_Adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, DT_Adapter_Array);
        DT_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final ArrayAdapter<String> BP_Adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, BP_Adapter_Array);
        BP_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final ArrayAdapter<String> SE_Adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, SE_Adapter_Array);
        SE_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final ArrayAdapter<String> SW_Adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, SW_Adapter_Array);
        SW_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final ArrayAdapter<String> PE_Adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, PE_Adapter_Array);
        PE_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final ArrayAdapter<String> PW_Adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, PW_Adapter_Array);
        PW_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //  final ArrayAdapter<String> SK_Adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, SK_Adapter_Array);
        //  SK_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //  final ArrayAdapter<String> PG_Adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, PG_Adapter_Array);
        //  PG_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        select_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View selectedItemView, int pos, long id) {

                // Relative to q_location_data.MRT_Line_List
                switch (pos) {
                    // North South
                    case 0:
                        start_spinner.setAdapter(NS_Adapter);
                        end_spinner.setAdapter(NS_Adapter);
                        selected = pos;
                        break;
                    // East West
                    case 1:
                        start_spinner.setAdapter(EW_Adapter);
                        end_spinner.setAdapter(EW_Adapter);
                        selected = pos;
                        break;
                    // Changi Airport Branch
                    case 2:
                        start_spinner.setAdapter(CG_Adapter);
                        end_spinner.setAdapter(CG_Adapter);
                        selected = pos;
                        break;
                    // North East
                    case 3:
                        start_spinner.setAdapter(NE_Adapter);
                        end_spinner.setAdapter(NE_Adapter);
                        selected = pos;
                        break;
                    // Circle
                    case 4:
                        start_spinner.setAdapter(CC_Adapter);
                        end_spinner.setAdapter(CC_Adapter);
                        selected = pos;
                        break;
                    // Circle Extension
                    case 5:
                        start_spinner.setAdapter(CE_Adapter);
                        end_spinner.setAdapter(CE_Adapter);
                        selected = pos;
                        break;
                    // Down Town
                    case 6:
                        start_spinner.setAdapter(DT_Adapter);
                        end_spinner.setAdapter(DT_Adapter);
                        selected = pos;
                        break;
                    // Bukit Panjang
                    case 7:
                        start_spinner.setAdapter(BP_Adapter);
                        end_spinner.setAdapter(BP_Adapter);
                        selected = pos;
                        break;
                    // Sengkang East
                    case 8:
                        start_spinner.setAdapter(SE_Adapter);
                        end_spinner.setAdapter(SE_Adapter);
                        selected = pos;
                        break;
                    // Sengkang West
                    case 9:
                        start_spinner.setAdapter(SW_Adapter);
                        end_spinner.setAdapter(SW_Adapter);
                        selected = pos;
                        break;
                    // Punggol East
                    case 10:
                        start_spinner.setAdapter(PE_Adapter);
                        end_spinner.setAdapter(PE_Adapter);
                        selected = pos;
                        break;
                    // Punggol West
                    case 11:
                        start_spinner.setAdapter(PW_Adapter);
                        end_spinner.setAdapter(PW_Adapter);
                        selected = pos;
                        break;
                    /* Sengkang
                    case 8:
                        start_spinner.setAdapter(SK_Adapter);
                        end_spinner.setAdapter(SK_Adapter);
                        selected = pos;
                        break;
                    // Punggol
                    case 9:
                        start_spinner.setAdapter(PG_Adapter);
                        end_spinner.setAdapter(PG_Adapter);
                        selected = pos;
                        break;
                    */
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        // Adding Listeners for Submit Button
        Button btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int start_index = start_spinner.getSelectedItemPosition();
                int end_index = end_spinner.getSelectedItemPosition();

                if (start_index == end_index) {

                    Toast.makeText(getApplicationContext(), "You have reached!", Toast.LENGTH_LONG).show();

                } else {

                    Intent intent = new Intent(SelectMRTActivity.this, TrackingMRTActivity.class);

                    Bundle extras = new Bundle();
                    extras.putInt("line", selected);
                    extras.putInt("start", start_index);
                    extras.putInt("end", end_index);
                    intent.putExtras(extras);

                    startActivity(intent);

                }

            }
        });

    }

    public void onBackPressed() {
        Intent mainmenu = new Intent(SelectMRTActivity.this, LandingActivity.class);
        startActivity(mainmenu);
        finish();
    }

    //google map apis
    //SETTING UP GOOGLE MAPS
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (gmap == null) {
            // Try to obtain the map from the SupportMapFragment.
            gmapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
            gmap = gmapFragment.getMap();

            if (gmap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
       //todo-set up map
    }

    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

}
