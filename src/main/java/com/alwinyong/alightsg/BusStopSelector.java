package com.alwinyong.alightsg;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import com.alwinyong.alightsg.R;

public class BusStopSelector extends Activity {

    // JSON Node names
    private static final String TAG_no = "no";
    private static final String TAG_lat = "lat";
    private static final String TAG_long = "long";
    private static final String TAG_name = "name";
    // URL to get contacts JSON
    private static String url1 = "http://cheeaun.github.io/busrouter-sg/data/2/bus-services.json";
    private static String url2 = "http://cheeaun.github.io/busrouter-sg/data/2/bus-services/";
    private static WakerSettings wakerSettings;
    JSONObject jsonObject;
    JSONArray jsonBusArray = null;
    JSONArray jsonBusStopArray = null;
    JSONObject obj2;
    JSONArray obj22;
    ArrayAdapter<String> buslistadapter1;
    ArrayAdapter<String> buslistadapter2;
    //	private Spinner busstopSpinner1;
//	private Spinner busstopSpinner2;
    // contacts JSONArray
    JSONArray services = null;
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> busList = new ArrayList<HashMap<String, String>>(); // used to display in list view
    private ProgressDialog pDialog;
    private ArrayList<String> buslistarray = new ArrayList<String>();
    private ArrayList<String> namearray = new ArrayList<String>();
    private ArrayList<Integer> routesarray = new ArrayList<Integer>();
    private ArrayList<String> busindexarray1 = new ArrayList<String>();
    private ArrayList<String> busindexarray2 = new ArrayList<String>();
    private ArrayList<String> temp = new ArrayList<String>();
    private ArrayList<String> busstopnamesarray = new ArrayList<String>();
    private ArrayList<String> busstopnumbersarray = new ArrayList<String>();
    private ArrayList<String> finalbusstoparray1 = new ArrayList<String>();
    private ArrayList<String> finalbusstoparray2 = new ArrayList<String>();
    private String nameselection;
    private SpinnerTrigger busstopSpinner1;
    private SpinnerTrigger busstopSpinner2;
    private int numberofservices;
    private String numberselection;
    private int index1 = 0;
    private int index2 = 0;
    private Button starttrackingbtn;
    private String retrievedString;
    private String jsonBus;
    private String jsonBusStop;
    private String selectedbus = "2";
    private String selectedbusstop1;
    private String selectedbusstop2;
    private String selectedbusstopindex1;
    private String selectedbusstopindex2;
    private Boolean finishedonce = false;
    private Boolean secondroute = true;
    private SpinnerTrigger buslistSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.busstopselector);


        busstopnamesarray = (ArrayList<String>) getIntent().getSerializableExtra("busstopnames");
        busstopnumbersarray = (ArrayList<String>) getIntent().getSerializableExtra("stopnumbers");

        busstopSpinner1 = (SpinnerTrigger) findViewById(R.id.busstopspinner1);
        busstopSpinner2 = (SpinnerTrigger) findViewById(R.id.busstopspinner2);


        busstopSpinner1.setVisibility(View.INVISIBLE);
        busstopSpinner2.setVisibility(View.INVISIBLE);

        TextView t1 = (TextView) findViewById(R.id.route1text);
        TextView t2 = (TextView) findViewById(R.id.route2text);
        TextView t3 = (TextView) findViewById(R.id.cdbstext);
        t1.setVisibility(View.INVISIBLE);
        t2.setVisibility(View.INVISIBLE);
        t3.setVisibility(View.INVISIBLE);

        TextView t4 = (TextView) findViewById(R.id.r1startend);
        t4.setVisibility(View.INVISIBLE);
        TextView t5 = (TextView) findViewById(R.id.r2startend);
        t5.setVisibility(View.INVISIBLE);

        Button r1btn = (Button) findViewById(R.id.route1selectbtn);
        Button r2btn = (Button) findViewById(R.id.route2selectbtn);
        r1btn.setVisibility(View.INVISIBLE);
        r2btn.setVisibility(View.INVISIBLE);
        new GetBuses().execute();

        Button gobtn = (Button) findViewById(R.id.gobtn);
        gobtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberofservices == 1) {
                    TextView t2 = (TextView) findViewById(R.id.route2text);
                    t2.setVisibility(View.INVISIBLE);

                    TextView t1 = (TextView) findViewById(R.id.r2startend);
                    t1.setVisibility(View.INVISIBLE);
                    busstopSpinner2 = (SpinnerTrigger) findViewById(R.id.busstopspinner2);
                    busstopSpinner2.setVisibility(View.INVISIBLE);
                    Button r2btn = (Button) findViewById(R.id.route2selectbtn);
                    r2btn.setVisibility(View.INVISIBLE);
                    secondroute = false;


                } else if (numberofservices == 2) {

                    secondroute = true;
                }
                new GetStops1().execute();

            }
        });


        r1btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();


                intent.putExtra("selectedbusstop", selectedbusstop1);
                intent.putExtra("selectedbusstopindex", selectedbusstopindex1);
                setResult(1, intent);


                finish();


            }
        });

        r2btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("selectedbusstop", selectedbusstop2);
                intent.putExtra("selectedbusstopindex", selectedbusstopindex2);
                setResult(1, intent);


                finish();

            }
        });

    }

    public void onBackPressed() {


        finish();

    }

    public class GetBuses extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            // Showing progress dialog

            super.onPreExecute();
            pDialog = new ProgressDialog(BusStopSelector.this);
            pDialog.setMessage("Downloading list of all buses. Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();


            jsonBus = sh.makeServiceCall(url1, ServiceHandler.GET);


            if (jsonBus != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonBus);
                    jsonBusArray = jsonObj.getJSONArray("services");

                    for (int i = 0; i < jsonBusArray.length(); i++)  // using for loop for parsing
                    {
                        JSONObject c = jsonBusArray.getJSONObject(i);
                        String no = c.getString("no");
                        String name = c.getString("name");

                        Integer routes = c.getInt("routes");

                        buslistarray.add(no);
                        namearray.add(name);
                        routesarray.add(routes);


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            buslistSpinner = (SpinnerTrigger) findViewById(R.id.buslistspinner);
            ArrayAdapter<String> buslistadapter = new ArrayAdapter<String>(
                    BusStopSelector.this, android.R.layout.simple_spinner_item, buslistarray);
            buslistadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            buslistSpinner.setAdapter(buslistadapter);

            buslistSpinner.setSelection(0, true);

            buslistSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int pos, long id) {

                    finalbusstoparray1.clear();
                    finalbusstoparray2.clear();
                    selectedbus = (String) parent.getItemAtPosition(pos);
                    //	 if(finishedonce == true){
                    //	 buslistadapter1.notifyDataSetChanged();
                    //	 buslistadapter2.notifyDataSetChanged();
                    //	 }

                    //	 selectedbusstop1 = (String) parent.getItemAtPosition(pos);
                    //	Integer numberofservices = routesarray.get(pos);
                    numberofservices = routesarray.get(pos);

          /*

            		if (numberofservices == 1){
            			TextView t2 = (TextView)findViewById(R.id.route2text);
            			t2.setVisibility(View.INVISIBLE);
            			busstopSpinner2 = (SpinnerTrigger) findViewById(R.id.busstopspinner2);
            			busstopSpinner2.setVisibility(View.INVISIBLE);
            			Button r2btn = (Button) findViewById(R.id.route2selectbtn);
            			r2btn.setVisibility(View.INVISIBLE);
            			secondroute = false;


            		}else if (numberofservices == 2){

            			 secondroute = true;
            		}


            		*/

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }

            });


        }
    }

    public class GetStops1 extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            // Showing progress dialog

            super.onPreExecute();

            finalbusstoparray1.clear();
            finalbusstoparray2.clear();

            busindexarray1.clear();
            busindexarray2.clear();

            //	 if(finishedonce == true){
            //	 buslistadapter1.notifyDataSetChanged();
            //	 buslistadapter2.notifyDataSetChanged();
            //	 }
            //	 busstopSpinner1 = (Spinner) findViewById(R.id.busstopspinner1);

            //	 ArrayAdapter<String> temp1 = new ArrayAdapter<String>(
            //              BusStopSelectorActivity.this, android.R.layout.simple_spinner_item, finalbusstoparray1);
            //	 temp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //     busstopSpinner1.setAdapter(temp1);


            //		 busstopSpinner1.setAdapter(null);

            //	 if (secondroute = true){
            //	 busstopSpinner2 = (Spinner) findViewById(R.id.busstopspinner2);
            //	 busstopSpinner2.setAdapter(null);
            //	 }
            pDialog = new ProgressDialog(BusStopSelector.this);
            pDialog.setMessage("Obtaining stops of bus service " + selectedbus + ". Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();


            jsonBusStop = sh.makeServiceCall(url2 + selectedbus + ".json", ServiceHandler.GET);


            Log.d("Response: ", "> " + jsonBusStop);

            if (jsonBusStop != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonBusStop);

                    JSONObject obj1 = jsonObj.getJSONObject("1");
                    JSONArray obj11 = obj1.getJSONArray("stops");

                    if (secondroute == true) {
                        obj2 = jsonObj.getJSONObject("2");
                        obj22 = obj2.getJSONArray("stops");
                    }

                    for (int i = 0; i < obj11.length(); i++)  // using for loop for parsing
                    {
                        String c = obj11.getString(i);

                        busindexarray1.add(c);


                    }

                    for (int i = 0; i < busindexarray1.size(); i++) {
                        index1 = busstopnumbersarray.indexOf(busindexarray1.get(i));
                        if(index1 != -1)
                        finalbusstoparray1.add(busstopnamesarray.get(index1));
                    }

                    if (secondroute == true) {
                        for (int i = 0; i < obj22.length(); i++)  // using for loop for parsing
                        {
                            String c = obj22.getString(i);

                            busindexarray2.add(c);


                        }


                        for (int i = 0; i < busindexarray2.size(); i++) {
                            index2 = busstopnumbersarray.indexOf(busindexarray2.get(i));
                            if(index2 != -1)
                            finalbusstoparray2.add(busstopnamesarray.get(index2));
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();


            TextView t1 = (TextView) findViewById(R.id.route1text);
            TextView t2 = (TextView) findViewById(R.id.route2text);
            TextView t3 = (TextView) findViewById(R.id.cdbstext);
            Button r1btn = (Button) findViewById(R.id.route1selectbtn);
            Button r2btn = (Button) findViewById(R.id.route2selectbtn);
            busstopSpinner1 = (SpinnerTrigger) findViewById(R.id.busstopspinner1);
            busstopSpinner2 = (SpinnerTrigger) findViewById(R.id.busstopspinner2);


            t1.setVisibility(View.VISIBLE);

            t3.setVisibility(View.VISIBLE);

            TextView t44 = (TextView) findViewById(R.id.r1startend);
            t44.setVisibility(View.VISIBLE);
            //   TextView t5 = (TextView)findViewById(R.id.r2startend);
            //  t5.setVisibility(View.INVISIBLE);
            busstopSpinner1 = (SpinnerTrigger) findViewById(R.id.busstopspinner1);
            buslistadapter1 = new ArrayAdapter<String>(
                    BusStopSelector.this, android.R.layout.simple_spinner_item, finalbusstoparray1);
            buslistadapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


            busstopSpinner1.setAdapter(buslistadapter1);


            busstopSpinner1.setVisibility(View.VISIBLE);
            r1btn.setVisibility(View.VISIBLE);
            busstopSpinner1.setSelection(0, true);

            int array1size = finalbusstoparray1.size();
            TextView t4 = (TextView) findViewById(R.id.r1startend);
            t4.setText(finalbusstoparray1.get(0) + " to " + finalbusstoparray1.get(array1size - 1));
            selectedbusstop1 = (String) busstopSpinner1.getItemAtPosition(0);
            selectedbusstopindex1 = busindexarray1.get(0);
            busstopSpinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int pos, long id) {

                    selectedbusstop1 = (String) parent.getItemAtPosition(pos);
                    selectedbusstopindex1 = busindexarray1.get(pos);
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }

            });

            if (secondroute == true) {
                t2.setVisibility(View.VISIBLE);


                TextView t5 = (TextView) findViewById(R.id.r2startend);
                t5.setVisibility(View.VISIBLE);

                busstopSpinner2 = (SpinnerTrigger) findViewById(R.id.busstopspinner2);
                buslistadapter2 = new ArrayAdapter<String>(
                        BusStopSelector.this, android.R.layout.simple_spinner_item, finalbusstoparray2);
                buslistadapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                busstopSpinner2.setAdapter(buslistadapter2);
                busstopSpinner2.setVisibility(View.VISIBLE);
                r2btn.setVisibility(View.VISIBLE);
                busstopSpinner2.setSelection(0, true);

                int array2size = finalbusstoparray2.size();
                TextView t55 = (TextView) findViewById(R.id.r2startend);
                t55.setText(finalbusstoparray2.get(0) + " to " + finalbusstoparray2.get(array2size - 1));

                selectedbusstop2 = (String) busstopSpinner2.getItemAtPosition(0);
                selectedbusstopindex2 = busindexarray2.get(0);

                busstopSpinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int pos, long id) {

                        selectedbusstop2 = (String) parent.getItemAtPosition(pos);
                        selectedbusstopindex2 = busindexarray2.get(pos);


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }

                });


                //    finalbusstoparray1.clear();
                //    finalbusstoparray2.clear();
            }
          /*  busstopSpinner1.setOnItemSelectedListener(new OnItemSelectedListener(){
            	public void onItemSelected(AdapterView<?> parent, View view,
        				int pos, long id) {


            		Toast.makeText(BusStopSelectorActivity.this, "Downloading database. Please wait a moment.", Toast.LENGTH_LONG).show();

            		Integer numberofservices = routesarray.get(pos);

            		if (numberofservices == 1){
            			 busstopSpinner1.setEnabled(true);
            			 busstopSpinner2.setVisibility(View.INVISIBLE);


            		}else if (numberofservices == 2){
            			 busstopSpinner1.setEnabled(true);
            			 busstopSpinner2.setVisibility(View.VISIBLE);
            		}



            	}

    			@Override
    			public void onNothingSelected(AdapterView<?> arg0) {
    				// TODO Auto-generated method stub

    			}

            });

            */
        }
    }

}