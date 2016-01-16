package com.alwinyong.alightsg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.alwinyong.alightsg.R;

public class SelectMRTStationActivity extends Activity {

    int selectedstation;
    int selectedline;
    private Spinner spinner;
    private Spinner spinner2;
    private String selectedstn = "Aljunied";

    //SETTING UP THE INTERFACE
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.stationselector);

        //SETUP THE TWO SPINNERS WITH RELEVANT DATA
        spinner = (Spinner) findViewById(R.id.mrtlinespinner);
        spinner2 = (Spinner) findViewById(R.id.stationnamespinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.mrt_lines, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);
        spinner.setSelection(0);

        final ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.eastwestline, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.northsouthline, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,
                R.array.northeastline, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(this,
                R.array.circleline, android.R.layout.simple_spinner_item);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final ArrayAdapter<CharSequence> adapter6 = ArrayAdapter.createFromResource(this,
                R.array.downtownline, android.R.layout.simple_spinner_item);
        adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        //SETTING UP SPINNER 1: THE MRT LINE SPINNER
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                switch (pos) {

                    //IF EAST WEST LINE IS SELECTED
                    case 0:
                        spinner2.setAdapter(adapter2);
                        spinner2.setSelection(0);
                        spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view,
                                                       int pos, long id) {
                                int position = 0;
                                selectedstn = (String) parent.getItemAtPosition(pos);
                            }

                            public void onNothingSelected(AdapterView<?> parent) {
                                // Another interface callback
                            }
                        });
                        break;

                    //IF NORTH SOUTH LINE IS SELECTED
                    case 1:
                        spinner2.setAdapter(adapter3);
                        spinner2.setSelection(0);
                        spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view,
                                                       int pos, long id) {
                                int position = 0;
                                selectedstn = (String) parent.getItemAtPosition(pos);
                            }

                            public void onNothingSelected(AdapterView<?> parent) {
                                // Another interface callback
                            }
                        });
                        break;

                    //IF NORTH EAST LINE IS SELECTED
                    case 2:
                        spinner2.setAdapter(adapter4);
                        spinner2.setSelection(0);
                        spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view,
                                                       int pos, long id) {
                                int position = 0;
                                selectedstn = (String) parent.getItemAtPosition(pos);
                            }

                            public void onNothingSelected(AdapterView<?> parent) {
                                // Another interface callback
                            }
                        });
                        break;

                    //IF CIRCLE LINE IS SELECTED
                    case 3:
                        spinner2.setAdapter(adapter5);
                        spinner2.setSelection(0);
                        spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view,
                                                       int pos, long id) {
                                int position = 0;
                                selectedstn = (String) parent.getItemAtPosition(pos);
                            }

                            public void onNothingSelected(AdapterView<?> parent) {
                                // Another interface callback
                            }
                        });
                        break;

                    case 4:
                        spinner2.setAdapter(adapter6);
                        spinner2.setSelection(0);
                        spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
                            public void onItemSelected(AdapterView<?> parent, View view,
                                                       int pos, long id) {
                                int position = 0;
                                selectedstn = (String) parent.getItemAtPosition(pos);
                            }

                            public void onNothingSelected(AdapterView<?> parent) {
                                // Another interface callback
                            }
                        });
                        break;
                    case 5:
                        break;
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        //SETTING UP THE SELECT STATION AND RETURN BUTTON
        Button returnBtn = (Button) findViewById(R.id.returnbtn);
        returnBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                //CARRYING THE SELECTED STATION NAME BACK TO MAIN ACTIVITY
                intent.putExtra("selectedstn", selectedstn);
                setResult(1, intent);

                //DESTROYING STATION SELECTOR ACTIVITY AND RETURNING TO MAIN ACTIVITY
                SelectMRTStationActivity.this.finish();
            }
        });
    }
    //END OF ONCREATE METHOD
}
