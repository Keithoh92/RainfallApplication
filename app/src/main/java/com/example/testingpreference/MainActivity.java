package com.example.testingpreference;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final String CHANNEL_ID = "9876";
    public static final String NOOFDAYS = "noofdays";
    public static final String RAINFALLTHRESHOLD = "rainfall";
    public static final String USERSLOCATION = "userslocation";

    static TextView day1, day2, day3, day4, day5, day2Rain, day3Rain, day4Rain, day5Rain, mm9, mm12, mm15, mm18, mm21, mm00, des9, des12, des15, des18, des21, des00;
    static ImageView image9, image12, image15, image18, image21, image00;
    TextView myLocationTV, time9, time12, time15, time18, time21, time00;
    String lat, lon;
    Button button;
    Calendar calendar;

    Double Rainday1 = 0.0;
    Double Rainday2 = 0.0;
    Double Rainday3 = 0.0;
    Double Rainday4 = 0.0;
    Double Rainday5 = 0.0;

    //Declare BroadcastReceiver from API response
    ConnectionReceiver receiver;
    IntentFilter intentFilter;

    private boolean gps_enable = false;
    private boolean network_enable = false;

    public LocationManager locationManager;
    public LocationListener locationListener = new MyLocationListener();

    Geocoder geocoder;
    List<Address> myAddress;

    SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
    Date d = new Date();
    String dayOfTheWeek = sdf.format(d);

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myLocationTV = (TextView) findViewById(R.id.textView2);
        day1 = (TextView) findViewById(R.id.textView5);
        day2 = (TextView) findViewById(R.id.textView4);
        day3 = (TextView) findViewById(R.id.textView6);
        day4 = (TextView) findViewById(R.id.textView7);
        day5 = (TextView) findViewById(R.id.textView8);
        mm9 = (TextView) findViewById(R.id.mm9);
        mm12 = (TextView) findViewById(R.id.mm12);
        mm15 = (TextView) findViewById(R.id.mm15);
        mm18 = (TextView) findViewById(R.id.mm18);
        mm21 = (TextView) findViewById(R.id.mm21);
        mm00 = (TextView) findViewById(R.id.mm00);
        des9 = (TextView) findViewById(R.id.des9);
        des12 = (TextView) findViewById(R.id.des12);
        des15 = (TextView) findViewById(R.id.des15);
        des18 = (TextView) findViewById(R.id.des18);
        des21 = (TextView) findViewById(R.id.des21);
        des00 = (TextView) findViewById(R.id.des00);
        image9 = (ImageView) findViewById(R.id.image9);
        image12 = (ImageView) findViewById(R.id.image12);
        image15 = (ImageView) findViewById(R.id.image15);
        image18 = (ImageView) findViewById(R.id.image18);
        image21 = (ImageView) findViewById(R.id.image21);
        image00 = (ImageView) findViewById(R.id.image00);
        time9 = (TextView) findViewById(R.id.textView);
        time12 = (TextView) findViewById(R.id.textView9);
        time15 = (TextView) findViewById(R.id.textView10);
        time18 = (TextView) findViewById(R.id.textView11);
        time21 = (TextView) findViewById(R.id.textView12);
        time00 = (TextView) findViewById(R.id.textView13);
        button = (Button) findViewById(R.id.button);

        day2Rain = (TextView) findViewById(R.id.day2Rainfall);
        day3Rain = (TextView) findViewById(R.id.day3Rainfall);
        day4Rain = (TextView) findViewById(R.id.day4Rainfall);
        day5Rain = (TextView) findViewById(R.id.day5Rainfall);

        //Set the right timestamps in MainActivity
        Calendar currentTime = Calendar.getInstance();
        Calendar t1 = Calendar.getInstance();
        Calendar t2 = Calendar.getInstance();
        Calendar t3 = Calendar.getInstance();
        Calendar t4 = Calendar.getInstance();
        Calendar t5 = Calendar.getInstance();
        Calendar t6 = Calendar.getInstance();

        t1.set(Calendar.HOUR_OF_DAY, 9);
        t2.set(Calendar.HOUR_OF_DAY, 11);
        t2.set(Calendar.MINUTE, 59);
        t3.set(Calendar.HOUR_OF_DAY, 15);
        t4.set(Calendar.HOUR_OF_DAY, 18);
        t5.set(Calendar.HOUR_OF_DAY, 21);
        t6.set(Calendar.HOUR_OF_DAY, 23);
        t6.set(Calendar.MINUTE, 59);

        if(currentTime.after(t1) && currentTime.before(t2)){
            time9.setText("12.00");
            time12.setText("15.00");
            time15.setText("18.00");
            time18.setText("21.00");
            time21.setText("00.00");
            time00.setText("03.00");
        }
        if(currentTime.after(t2) && currentTime.before(t3)){
            time9.setText("15.00");
            time12.setText("18.00");
            time15.setText("21.00");
            time18.setText("00.00");
            time21.setText("03.00");
            time00.setText("06.00");
        }
        if(currentTime.after(t3) && currentTime.before(t4)){
            time9.setText("18.00");
            time12.setText("21.00");
            time15.setText("00.00");
            time18.setText("03.00");
            time21.setText("06.00");
            time00.setText("09.00");
        }
        if(currentTime.after(t4) && currentTime.before(t5)){
            time9.setText("21.00");
            time12.setText("00.00");
            time15.setText("03.00");
            time18.setText("06.00");
            time21.setText("09.00");
            time00.setText("12.00");
        }
        if(currentTime.after(t5) && currentTime.before(t6)){
            time9.setText("00.00");
            time12.setText("03.00");
            time15.setText("06.00");
            time18.setText("09.00");
            time21.setText("12.00");
            time00.setText("15.00");
        }
        if(currentTime.after(t6) && currentTime.before(t1)){
            time9.setText("03.00");
            time12.setText("06.00");
            time15.setText("09.00");
            time18.setText("12.00");
            time21.setText("15.00");
            time00.setText("18.00");
        }

        //Initialise BroadcastReceiver for retrieving API response
        receiver = new ConnectionReceiver();
        intentFilter = new
                IntentFilter("com.example.testingpreference.ACTION");

        //Set the first day to current day and next 4 days
        if(dayOfTheWeek.equals("monday")){
            day2.setText("Tuesday");
            day3.setText("Wednesday");
            day4.setText("Thursday");
            day5.setText("Friday");
        }
        if(dayOfTheWeek.equals("tuesday")){
            day2.setText("Wednesday");
            day3.setText("Thursday");
            day4.setText("Friday");
            day5.setText("Saturday");

        }
        if(dayOfTheWeek.equals("wednesday")){
            day2.setText("Thursday");
            day3.setText("Friday");
            day4.setText("Saturday");
            day5.setText("Sunday");
        }
        if(dayOfTheWeek.equals("thursday")){
            day2.setText("Friday");
            day3.setText("Saturday");
            day4.setText("Sunday");
            day5.setText("Monday");

        }
        if(dayOfTheWeek.equals("friday")){
            day2.setText("Saturday");
            day3.setText("Sunday");
            day4.setText("Monday");
            day5.setText("Tuesday");
        }
        if(dayOfTheWeek.equals("saturday")){
            day2.setText("Sunday");
            day3.setText("Monday");
            day4.setText("Tuesday");
            day5.setText("Wednesday");
        }
        if(dayOfTheWeek.equals("sunday")){
            day2.setText("Monday");
            day3.setText("Tuesday");
            day4.setText("Wednesday");
            day5.setText("Thursday");
        }

        day1.setText(dayOfTheWeek);

        //Get users preferences for no of days they want and rainfall threshold for alerts
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String noOfDaysString = preferences.getString("range", "1");
        String rainFallThreshold = preferences.getString("alerts", "1");
        final int noOfDays = Integer.parseInt(noOfDaysString);
        final int rainfall = Integer.parseInt(rainFallThreshold);

        //check which location the user wants to use in preferences
        SharedPreferences switchPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean switchPrefValue = switchPref.getBoolean("myLocation", false);
        String locationListItem = switchPref.getString("town", "Choose town");
        if(switchPrefValue){ //if user selects use my location retrieve users current location
            getMyLocation();
        }else{ // else use the location chosen in the list of locations
            myLocationTV.setText(locationListItem);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherService.class);
                intent.putExtra("usersLocation", myLocationTV.getText());
                intent.putExtra("noofdays", noOfDays);
                intent.putExtra("rainfall", rainfall);
                startService(intent);
            }
        });
        //Set the API to be called at 8am in the morning and trigger the notification in the API service class
        Calendar alarmStartTime = Calendar.getInstance();
        calendar = Calendar.getInstance();
        alarmStartTime.setTimeInMillis(System.currentTimeMillis());
        alarmStartTime.set(Calendar.HOUR_OF_DAY, 8);
        alarmStartTime.set(Calendar.MINUTE, 0);
        alarmStartTime.set(Calendar.SECOND, 0);
        if(calendar.after(alarmStartTime)){
            alarmStartTime.add(Calendar.DATE, 1);
        }
        //Use pending intent to start BroadcastReceiver at 8am every morning that then calls the weather API service class
        //Pass the users preferences into intent
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        intent.setAction("AlarmBroadcast");
        intent.putExtra(USERSLOCATION, myLocationTV.getText());
        intent.putExtra(NOOFDAYS, noOfDays);
        intent.putExtra(RAINFALLTHRESHOLD, rainfall);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, 3456, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), pendingIntent);
        Log.d("testing", "pendingIntent got called");

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Log.d("testing", "onCreate got called");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity, menu);
        return true;
    }

    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        Log.d("testing", "onPause got called");
    }

    //Broadcast receiver that is called by alarmmanager and starts WeatherAPIservice
    public static class MyBroadcastReceiver extends BroadcastReceiver {
        public MyBroadcastReceiver(){

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("testing", "MyBroadcastReceiver got called");
            if(intent.getAction().equals("AlarmBroadcast")) {
                int noOfDaysString = intent.getExtras().getInt(MainActivity.NOOFDAYS);
                int rainfallThreshold = intent.getExtras().getInt(MainActivity.RAINFALLTHRESHOLD);
                String usersLocation = intent.getExtras().getString(MainActivity.USERSLOCATION);

                Intent intent1 = new Intent(context.getApplicationContext(), WeatherService.class);
                Log.d("testing", "Create MyBroadcastReceiver intent");

                intent1.putExtra("usersLocation", usersLocation);
                intent1.putExtra("noofdays", noOfDaysString);
                intent1.putExtra("rainfall", rainfallThreshold);
                context.getApplicationContext().startService(intent1);
                Log.d("testing", "Started service from myBroadcastReceiver");

            }
        }
    }
    //BroadcastReceiver that receives the parsed data back from the WeatherAPIService and updates the MainActivity UI
    public class ConnectionReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals("MyBroadcast")){

                Log.d("testing", "Receiveing broadcast data");
                int resultCode = intent.getExtras().getInt(WeatherService.RESULT);
                Double day1rain = intent.getExtras().getDouble(WeatherService.DAY1);
                Double day2rain = intent.getExtras().getDouble(WeatherService.DAY2);
                Double day3rain = intent.getExtras().getDouble(WeatherService.DAY3);
                Double day4rain = intent.getExtras().getDouble(WeatherService.DAY4);
                Double day5rain = intent.getExtras().getDouble(WeatherService.DAY5);
                Double rain9 = intent.getExtras().getDouble(WeatherService.MM9);
                Double rain12 = intent.getExtras().getDouble(WeatherService.MM12);
                Double rain15 = intent.getExtras().getDouble(WeatherService.MM15);
                Double rain18 = intent.getExtras().getDouble(WeatherService.MM18);
                Double rain21 = intent.getExtras().getDouble(WeatherService.MM21);
                Double rain00 = intent.getExtras().getDouble(WeatherService.MM00);
                String desc9 = intent.getExtras().getString(WeatherService.DESC9);
                String desc12 = intent.getExtras().getString(WeatherService.DESC12);
                String desc15 = intent.getExtras().getString(WeatherService.DESC15);
                String desc18 = intent.getExtras().getString(WeatherService.DESC18);
                String desc21 = intent.getExtras().getString(WeatherService.DESC21);
                String desc00 = intent.getExtras().getString(WeatherService.DESC00);
                int noOfDays = intent.getExtras().getInt(WeatherService.NOOFDAYS);
                int rainfallPref = intent.getExtras().getInt(WeatherService.RAINFALLPREF);

                // Check what the weather description is for the next 3 hours and set the correct icon in UI
               ////////////////desc9/////////////////////////////////////////
                if (desc9.contains("thunderstorm")) {
                    image9.setImageResource(R.drawable.icon_thunderstorm);
                }
                if (desc9.contains("drizzle") && !desc9.contains("rain")) {
                    image9.setImageResource(R.drawable.icon_drizzle);
                }
                if (desc9.contains("rain") && !desc9.contains("snow")) {
                    image9.setImageResource(R.drawable.icon_rain);
                }
                if (desc9.contains("snow")) {
                    image9.setImageResource(R.drawable.icon_snow);
                }
                if (desc9.contains("clear")) {
                    image9.setImageResource(R.drawable.icon_clear);
                }
                if (desc9.contains("clouds") && !desc9.contains("overcast clouds")) {
                    image9.setImageResource(R.drawable.icon_clouds);
                }
                if (desc9.contains("overcast clouds")) {
                    image9.setImageResource(R.drawable.icon_overcast);
                }
                ////////////desc12////////////////////////////
                if (desc12.contains("thunderstorm")) {
                    image12.setImageResource(R.drawable.icon_thunderstorm);
                }
                if (desc12.contains("drizzle") && !desc12.contains("rain")) {
                    image12.setImageResource(R.drawable.icon_drizzle);
                }
                if (desc12.contains("rain") && !desc12.contains("snow")) {
                    image12.setImageResource(R.drawable.icon_rain);
                }
                if (desc12.contains("snow")) {
                    image12.setImageResource(R.drawable.icon_snow);
                }
                if (desc12.contains("clear")) {
                    image12.setImageResource(R.drawable.icon_clear);
                }
                if (desc12.contains("clouds") && !desc12.contains("overcast clouds")) {
                    image12.setImageResource(R.drawable.icon_clouds);
                }
                if (desc12.contains("overcast clouds")) {
                    image12.setImageResource(R.drawable.icon_overcast);
                }
                ////////////desc15////////////////////////////
                if (desc15.contains("thunderstorm")) {
                    image15.setImageResource(R.drawable.icon_thunderstorm);
                }
                if (desc15.contains("drizzle") && !desc15.contains("rain")) {
                    image15.setImageResource(R.drawable.icon_drizzle);
                }
                if (desc15.contains("rain") && !desc15.contains("snow")) {
                    image15.setImageResource(R.drawable.icon_rain);
                }
                if (desc15.contains("snow")) {
                    image15.setImageResource(R.drawable.icon_snow);
                }
                if (desc15.contains("clear")) {
                    image15.setImageResource(R.drawable.icon_clear);
                }
                if (desc15.contains("clouds") && !desc15.contains("overcast clouds")) {
                    image15.setImageResource(R.drawable.icon_clouds);
                }
                if (desc15.contains("overcast clouds")) {
                    image15.setImageResource(R.drawable.icon_overcast);
                }
                ////////////desc18////////////////////////////
                if (desc18.contains("thunderstorm")) {
                    image18.setImageResource(R.drawable.icon_thunderstorm);
                }
                if (desc18.contains("drizzle") && !desc18.contains("rain")) {
                    image18.setImageResource(R.drawable.icon_drizzle);
                }
                if (desc18.contains("rain") && !desc18.contains("snow")) {
                    image18.setImageResource(R.drawable.icon_rain);
                }
                if (desc18.contains("snow")) {
                    image18.setImageResource(R.drawable.icon_snow);
                }
                if (desc18.contains("clear")) {
                    image18.setImageResource(R.drawable.icon_clear);
                }
                if (desc18.contains("clouds") && !desc18.contains("overcast clouds")) {
                    image18.setImageResource(R.drawable.icon_clouds);
                }
                if (desc18.contains("overcast clouds")) {
                    image18.setImageResource(R.drawable.icon_overcast);
                }
                ////////////desc21////////////////////////////
                if (desc21.contains("thunderstorm")) {
                    image21.setImageResource(R.drawable.icon_thunderstorm);
                }
                if (desc21.contains("drizzle") && !desc21.contains("rain")) {
                    image21.setImageResource(R.drawable.icon_drizzle);
                }
                if (desc21.contains("rain") && !desc21.contains("snow")) {
                    image21.setImageResource(R.drawable.icon_rain);
                }
                if (desc21.contains("snow")) {
                    image21.setImageResource(R.drawable.icon_snow);
                }
                if (desc21.contains("clear")) {
                    image21.setImageResource(R.drawable.icon_clear);
                }
                if (desc21.contains("clouds") && !desc21.contains("overcast clouds")) {
                    image21.setImageResource(R.drawable.icon_clouds);
                }
                if (desc21.contains("overcast clouds")) {
                    image21.setImageResource(R.drawable.icon_overcast);
                }
                ////////////desc00////////////////////////////
                if (desc00.contains("thunderstorm")) {
                    image00.setImageResource(R.drawable.icon_thunderstorm);
                }
                if (desc00.contains("drizzle") && !desc00.contains("rain")) {
                    image00.setImageResource(R.drawable.icon_drizzle);
                }
                if (desc00.contains("rain") && !desc00.contains("snow")) {
                    image00.setImageResource(R.drawable.icon_rain);
                }
                if (desc00.contains("snow")) {
                    image00.setImageResource(R.drawable.icon_snow);
                }
                if (desc00.contains("clear")) {
                    image00.setImageResource(R.drawable.icon_clear);
                }
                if (desc00.contains("clouds") && !desc00.contains("overcast clouds")) {
                    image00.setImageResource(R.drawable.icon_clouds);
                }
                if (desc00.contains("overcast clouds")) {
                    image00.setImageResource(R.drawable.icon_overcast);
                }

                // Create the NotificationChannel, but only on API 26+ because
                // the NotificationChannel class is new and not in the support library
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence name = getString(R.string.channel_name);
                    String description = getString(R.string.channel_description);
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                    channel.setDescription(description);
                    // Register the channel with the system; you can't change the importance
                    // or other notification behaviors after this
                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);

                }


                if(resultCode == RESULT_OK){

                    mm9.setText(rain9.toString() +"mm");
                    mm12.setText(rain12.toString() +"mm");
                    mm15.setText(rain15.toString() +"mm");
                    mm18.setText(rain18.toString() +"mm");
                    mm21.setText(rain21.toString() +"mm");
                    mm00.setText(rain00.toString() +"mm");
                    des9.setText(desc9);
                    des12.setText(desc12);
                    des15.setText(desc15);
                    des18.setText(desc18);
                    des21.setText(desc21);
                    des00.setText(desc00);
                    day2Rain.setText(day2rain.toString() +"mm");
                    day3Rain.setText(day3rain.toString() +"mm");
                    day4Rain.setText(day4rain.toString() +"mm");
                    day5Rain.setText(day5rain.toString() +"mm");
                    Rainday1 = day1rain;
                    Rainday2 = day2rain;
                    Rainday3 = day3rain;
                    Rainday4 = day4rain;
                    Rainday5 = day5rain;

                    Log.d("testing", "Received data");

                }
            }

        }
    };

    public void onResume() {
        super.onResume();
        //Register Broadcast Receiver
        final IntentFilter filter = new IntentFilter();
        filter.addAction("MyBroadcast");
        registerReceiver(receiver, filter);
        Log.d("testing", "onResume got called");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        boolean test = preferences.getBoolean("myLocation", false);
        String locationListItem = preferences.getString("town", "Choose town");
        if(test){
            getMyLocation();
        }else{
            myLocationTV.setText(locationListItem);
        }
    }
    //Get users preferences when the app is opened back up and update the UI with next days weather forecast
    public void onStart() {
        super.onStart();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String noOfDaysString = preferences.getString("range", "1");
        String rainFallThreshold = preferences.getString("alerts", "1");
        final int noOfDays = Integer.parseInt(noOfDaysString);
        final int rainfall = Integer.parseInt(rainFallThreshold);
        Intent intent = new Intent(MainActivity.this, WeatherService.class);
        intent.putExtra("usersLocation", myLocationTV.getText());
        intent.putExtra("noofdays", noOfDays);
        intent.putExtra("rainfall", rainfall);
        startService(intent);
        Log.d("testing", "onStart got called");
    }

    public void onStop() {
        super.onStop();

        Log.d("testing", "onStop got called");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d("testing", "onDestroy got called");
    }
    //Menu item logic
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.activity_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;


        }
        Log.d("testing", "onStart got called");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
        //Location Listener that listens for updates on users location
        //Returns the city(Locality)
    class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(@NonNull Location location) {
            if (location != null) {
                locationManager.removeUpdates(locationListener);
                lat = "" + location.getLatitude();
                lon = "" + location.getLongitude();

                geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                try {
                    myAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String address1 = myAddress.get(0).getLocality();
                myLocationTV.setText(address1);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }
    }
    //Method that is called when the user chooses to use their location
    //Gets permission from user
    public void getMyLocation() {
        try {
            gps_enable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
        }

        try {
            network_enable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
        }
        if (!gps_enable && network_enable) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Attention");
            builder.setMessage("Location not available, Please enable your location");

            builder.create();
        }
        if (gps_enable) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
        if(network_enable){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }

    }

    private boolean checkLocationPermission(){
        int location = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int location1 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        List<String> listPermission = new ArrayList<>();

        if(location != PackageManager.PERMISSION_GRANTED){
            listPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(location1 != PackageManager.PERMISSION_GRANTED){
            listPermission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }if(!listPermission.isEmpty()){
            ActivityCompat.requestPermissions(this,listPermission.toArray(new String[listPermission.size()]),
                    1);
        }
        return true;

    }

}



