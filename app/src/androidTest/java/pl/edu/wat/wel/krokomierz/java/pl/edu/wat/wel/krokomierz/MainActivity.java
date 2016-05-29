package pl.edu.wat.wel.krokomierz;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.annotation.TargetApi;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.content.Context;
import android.widget.TextView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnClickListener, OnMapReadyCallback {

    private TextView textViewSteps;
    private TextView textViewCalories;
    private SensorManager sensorManager;
    private float acceleration;
    private float previousY;
    private float currentY;
    int Z = 0;
    private int numSteps = 0;
    private int numSteps2;
    private float threshold;
    private float kcal;
    float kalorie;
    ViewFlipper viewFlipper;
    ViewFlipper viewFlippermax;
    Context context;

    RecyclerView recyclerView;
    MainActivity.MyAdapter adapter;
    static Calendar c = Calendar.getInstance();
    static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static String formattedDate = df.format(c.getTime());
    static TextView first_line;
    static TextView second_line;
    static TextView third_line;

    ArrayList<String> dates;
    Random random;
    BarChart barChart;
    ArrayList<BarEntry> barEntries;

    private GoogleMap mMap;

    int danewzrostu;
    int danewagi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new MainActivity.MyAdapter(this);
        recyclerView.setAdapter(adapter);

        barChart = (BarChart) findViewById(R.id.bargraph);

        createRandomBarGraph("2016/05/01", "2016/05/16");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        setUpMapIfNeeded();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewFlipper = (ViewFlipper) this.findViewById(R.id.viewflipper);
        viewFlipper.setOnClickListener(this);
        viewFlippermax = (ViewFlipper) this.findViewById(R.id.viewflippermax);
        viewFlippermax.setOnClickListener(this);

        Intent getDane = getIntent();
        danewzrostu = getDane.getIntExtra("danewzrostu", 0);
        danewagi = getDane.getIntExtra("danewagi", 0);
        Z = getDane.getIntExtra("Z", 0);
        numSteps2 = getDane.getIntExtra("numsteps", 0);


        if (danewzrostu == 0) {
            threshold = 0;
            if (danewagi >= 0 && danewagi < 50) {
                kcal = (float) 0.01458;
            }
            if (danewagi >= 50 && danewagi < 65) {
                kcal = (float) 0.01944;
            }
            if (danewagi >= 65 && danewagi < 80) {
                kcal = (float) 0.0243;
            }
            if (danewagi >= 80 && danewagi < 95) {
                kcal = (float) 0.02916;
            }
            if (danewagi >= 95 && danewagi <= 150) {
                kcal = (float) 0.03402;
            }
        }
        if (danewzrostu > 0 && danewzrostu < 160) {
            threshold = 2;
            if (danewagi >= 0 && danewagi < 50) {
                kcal = (float) 0.01458;
            }
            if (danewagi >= 50 && danewagi < 65) {
                kcal = (float) 0.01944;
            }
            if (danewagi >= 65 && danewagi < 80) {
                kcal = (float) 0.0243;
            }
            if (danewagi >= 80 && danewagi < 95) {
                kcal = (float) 0.02916;
            }
            if (danewagi >= 95 && danewagi <= 150) {
                kcal = (float) 0.03402;
            }
        }
        if (danewzrostu >= 160 && danewzrostu < 165) {
            threshold = 2;
            if (danewagi >= 0 && danewagi < 50) {
                kcal = (float) 0.01638;
            }
            if (danewagi >= 50 && danewagi < 65) {
                kcal = (float) 0.02184;
            }
            if (danewagi >= 65 && danewagi < 80) {
                kcal = (float) 0.0273;
            }
            if (danewagi >= 80 && danewagi < 95) {
                kcal = (float) 0.03276;
            }
            if (danewagi >= 95 && danewagi <= 150) {
                kcal = (float) 0.03822;
            }
        }
        if (danewzrostu >= 165 && danewzrostu < 170) {
            threshold = 3;
            if (danewagi >= 0 && danewagi < 50) {
                kcal = (float) 0.01638;
            }
            if (danewagi >= 50 && danewagi < 65) {
                kcal = (float) 0.02184;
            }
            if (danewagi >= 65 && danewagi < 80) {
                kcal = (float) 0.0273;
            }
            if (danewagi >= 80 && danewagi < 95) {
                kcal = (float) 0.03276;
            }
            if (danewagi >= 95 && danewagi <= 150) {
                kcal = (float) 0.03822;
            }
        }
        if (danewzrostu >= 170 && danewzrostu < 175) {
            threshold = 4;
            if (danewagi >= 0 && danewagi < 50) {
                kcal = (float) 0.01818;
            }
            if (danewagi >= 50 && danewagi < 65) {
                kcal = (float) 0.02424;
            }
            if (danewagi >= 65 && danewagi < 80) {
                kcal = (float) 0.0303;
            }
            if (danewagi >= 80 && danewagi < 95) {
                kcal = (float) 0.03636;
            }
            if (danewagi >= 95 && danewagi <= 150) {
                kcal = (float) 0.04242;
            }
        }
        if (danewzrostu >= 175 && danewzrostu < 180) {
            threshold = 5;
            if (danewagi >= 0 && danewagi < 50) {
                kcal = (float) 0.01818;
            }
            if (danewagi >= 50 && danewagi < 65) {
                kcal = (float) 0.02424;
            }
            if (danewagi >= 65 && danewagi < 80) {
                kcal = (float) 0.0303;
            }
            if (danewagi >= 80 && danewagi < 95) {
                kcal = (float) 0.03636;
            }
            if (danewagi >= 95 && danewagi <= 150) {
                kcal = (float) 0.04242;
            }
        }
        if (danewzrostu >= 180 && danewzrostu < 185) {
            threshold = 6;
            if (danewagi >= 0 && danewagi < 50) {
                kcal = (float) 0.01998;
            }
            if (danewagi >= 50 && danewagi < 65) {
                kcal = (float) 0.02664;
            }
            if (danewagi >= 65 && danewagi < 80) {
                kcal = (float) 0.0333;
            }
            if (danewagi >= 80 && danewagi < 95) {
                kcal = (float) 0.03996;
            }
            if (danewagi >= 95 && danewagi <= 150) {
                kcal = (float) 0.04662;
            }
        }
        if (danewzrostu >= 185 && danewzrostu < 190) {
            threshold = (float) 7.5;
            if (danewagi >= 0 && danewagi < 50) {
                kcal = (float) 0.01998;
            }
            if (danewagi >= 50 && danewagi < 65) {
                kcal = (float) 0.02664;
            }
            if (danewagi >= 65 && danewagi < 80) {
                kcal = (float) 0.0333;
            }
            if (danewagi >= 80 && danewagi < 95) {
                kcal = (float) 0.03996;
            }
            if (danewagi >= 95 && danewagi <= 150) {
                kcal = (float) 0.04662;
            }
        }
        if (danewzrostu >= 190 && danewzrostu < 195) {
            threshold = 8;
            if (danewagi >= 0 && danewagi < 50) {
                kcal = (float) 0.01998;
            }
            if (danewagi >= 50 && danewagi < 65) {
                kcal = (float) 0.02664;
            }
            if (danewagi >= 65 && danewagi < 80) {
                kcal = (float) 0.0333;
            }
            if (danewagi >= 80 && danewagi < 95) {
                kcal = (float) 0.03996;
            }
            if (danewagi >= 95 && danewagi <= 150) {
                kcal = (float) 0.04662;
            }
        }
        if (danewzrostu >= 195 && danewzrostu <= 210) {
            threshold = 9;
            if (danewagi >= 0 && danewagi < 50) {
                kcal = (float) 0.01998;
            }
            if (danewagi >= 50 && danewagi < 65) {
                kcal = (float) 0.02664;
            }
            if (danewagi >= 65 && danewagi < 80) {
                kcal = (float) 0.0333;
            }
            if (danewagi >= 80 && danewagi < 95) {
                kcal = (float) 0.03996;
            }
            if (danewagi >= 95 && danewagi <= 150) {
                kcal = (float) 0.04662;
            }
        }

        textViewSteps = (TextView) findViewById(R.id.textSteps);
        textViewCalories = (TextView) findViewById(R.id.kalorie);
        previousY = 0;
        currentY = 0;
        acceleration = 0.00f;
        enableAccelerometerListening();

    }


    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private void enableAccelerometerListening() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            currentY = y;
            if (Math.abs(currentY - previousY) > threshold) {
                numSteps++;
                kalorie = (float) (numSteps * kcal);
                if (Z==1){
                    numSteps2++;
                    numSteps = numSteps2;
                    textViewSteps.setText(String.valueOf(numSteps2));
                    DecimalFormat df = new DecimalFormat("#.##");
                    textViewCalories.setText(String.valueOf(df.format(kalorie)));
                }
                textViewSteps.setText(String.valueOf(numSteps));
                DecimalFormat df = new DecimalFormat("#.##");
                textViewCalories.setText(String.valueOf(df.format(kalorie)));
            }
            previousY = y;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    public void onClick(View v) {
        viewFlipper.showNext();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    int X = 1;

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.wyzeruj_kroki) {
             AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Potwierdzenie zresetowania aktywności");

        // Setting Dialog Message
        alertDialog.setMessage("Zresetować bierzący wynik ?");


        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("TAK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            numSteps = 0;
            kalorie = 0;
            textViewSteps.setText(String.valueOf(numSteps));
            textViewCalories.setText(String.valueOf(kalorie));
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NIE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
        }

        if (X == 1) {
            if (id == R.id.krok) {

            } else if (id == R.id.mapa) {
                viewFlippermax.showNext();
                X = 2;

            } else if (id == R.id.lista) {
                viewFlippermax.showNext();
                viewFlippermax.showNext();
                X = 3;

            } else if (id == R.id.wykres) {
                viewFlippermax.showPrevious();
                X = 4;
            }
        }

        if (X == 2) {
            if (id == R.id.krok) {
                viewFlippermax.showPrevious();
                X = 1;

            } else if (id == R.id.mapa) {

            } else if (id == R.id.lista) {
                viewFlippermax.showNext();
                X = 3;

            } else if (id == R.id.wykres) {
                viewFlippermax.showNext();
                viewFlippermax.showNext();
                X = 4;
            }
        }

        if (X == 3) {
            if (id == R.id.krok) {
                viewFlippermax.showNext();
                viewFlippermax.showNext();
                X = 1;

            } else if (id == R.id.mapa) {
                viewFlippermax.showPrevious();
                X = 2;

            } else if (id == R.id.lista) {

            } else if (id == R.id.wykres) {
                viewFlippermax.showNext();
                X = 4;
            }
        }
        if (X == 4) {
            if (id == R.id.krok) {
                viewFlippermax.showNext();
                X = 1;

            } else if (id == R.id.mapa) {
                viewFlippermax.showNext();
                viewFlippermax.showNext();
                X = 2;

            } else if (id == R.id.lista) {
                viewFlippermax.showPrevious();

            } else if (id == R.id.wykres) {

            }
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void clickStart(View view) {
        final Intent getStats = new Intent(this, AddActivity.class);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Potwierdzenie zapisu aktywności");

        // Setting Dialog Message
        alertDialog.setMessage("Zapisać bierzący wynik ?");


        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("TAK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                getStats.putExtra("liczbakrokow", numSteps);
                getStats.putExtra("danewzrostu", danewzrostu);
                getStats.putExtra("danewagi", danewagi);
                startActivity(getStats);
                finish();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NIE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }


    /////////////////////////// MAP ACTIVITY ////////////////////////////////////////////////////

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }
    private void setUpMap() {
        // Enable MyLocation Layer of Google Map
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {

            LatLng target = new LatLng(location.getLatitude(), location.getLongitude());
            CameraPosition position = this.mMap.getCameraPosition();

            CameraPosition.Builder builder = new CameraPosition.Builder();
            builder.zoom(20);
            builder.target(target);
            builder.bearing(90)                // Sets the orientation of the camera to east
                    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder


            this.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()));
            mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("You are here!").snippet("Consider yourself located"));

        } else Toast.makeText(this, "No localization", Toast.LENGTH_SHORT).show();

    }

    public void trybnormalny(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public void trybsatelita(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    public void trybhybryda(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    public void trybteren(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
    /////////////////// END MAP ACTIVITY ////////////////////////////////////////////////////


    //////////////////// LISTA ACTIVITY //////////////////////////////
    public static class MyVievHolder extends RecyclerView.ViewHolder {


        public MyVievHolder(View itemView) {
            super(itemView);
            first_line = (TextView) itemView.findViewById(R.id.first_line);
            second_line = (TextView) itemView.findViewById(R.id.second_line);
            third_line = (TextView) itemView.findViewById(R.id.third_line);
        }
    }

    public static class MyAdapter extends RecyclerView.Adapter<MyVievHolder> {

        Context context;
        List<String> data;
        List<String> data2;
        List<String> data3;

        public MyAdapter(Context context) {
            this.context = context;
            data = new ArrayList<>();
            data2 = new ArrayList<>();
            data3 = new ArrayList<>();
            loadDate();
        }

        public void loadDate() {
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new FileReader(context.getFilesDir().getPath() + "/recycleview.txt"));
                data = new ArrayList<>();
                data2 = new ArrayList<>();
                data3 = new ArrayList<>();
                String line;
                String line2;
                String line3;
                while ((line = bufferedReader.readLine()) != null && (line2 = bufferedReader.readLine()) != null && (line3 = bufferedReader.readLine()) != null) {
                    data.add(line);
                    data2.add(line2);
                    data3.add(line3);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public MyVievHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_rows, parent, false);
            MyVievHolder myViewHolder = new MyVievHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyVievHolder holder, int position) {
            first_line.setText("Ilość kroków:  " + data.get(position));
            second_line.setText("Data:  " + data2.get(position));
            third_line.setText(data3.get(position));

        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    /////////////////// END LISTA ACTIVITY ////////////////////////////////////////////////////

    //////////////// WYKRES ACTIVITY ////////////////////
    public void createRandomBarGraph(String Date1, String Date2) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

        try {
            Date date1 = simpleDateFormat.parse(Date1);
            Date date2 = simpleDateFormat.parse(Date2);

            Calendar mDate1 = Calendar.getInstance();
            Calendar mDate2 = Calendar.getInstance();
            mDate1.clear();
            mDate2.clear();

            mDate1.setTime(date1);
            mDate2.setTime(date2);

            dates = new ArrayList<>();
            dates = getList(mDate1, mDate2);

            barEntries = new ArrayList<>();
            float max = 0f;
            float value = 0f;
            random = new Random();
            for (int j = 0; j < dates.size(); j++) {
                max = 20000f;
                value = random.nextFloat() * max;
                DecimalFormat df = new DecimalFormat("#");
                barEntries.add(new BarEntry(Float.parseFloat(df.format(value)), j));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Kroki");
        BarData barData = new BarData(dates, barDataSet);
        barChart.setData(barData);
        barChart.setDescription("");

    }

    public ArrayList<String> getList(Calendar startDate, Calendar endDate) {
        ArrayList<String> list = new ArrayList<String>();
        while (startDate.compareTo(endDate) <= 0) {
            list.add(getDate(startDate));
            startDate.add(Calendar.DAY_OF_MONTH, 1);
        }
        return list;
    }

    public String getDate(Calendar cld) {
        String curDate = cld.get(Calendar.YEAR) + "/" + (cld.get(Calendar.MONTH) + 1) + "/"
                + cld.get(Calendar.DAY_OF_MONTH);
        try {
            Date date = new SimpleDateFormat("yyyy/MM/dd").parse(curDate);
            curDate = new SimpleDateFormat("yyy/MM/dd").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return curDate;
    }
    //////////////// END WYKRES ACTIVITY ////////////////////

}


