package com.example.testingpreference;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.widget.TextView;
import com.google.android.gms.location.FusedLocationProviderClient;


public class LocationActivity extends AppCompatActivity {

    TextView cityTV = (TextView) findViewById(R.id.cityTV);
    TextView myCity = (TextView) findViewById(R.id.myCity);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        try {
            setSupportActionBar(myToolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String hi = "Hi there";
        cityTV.setText(hi);


    }
}
//        //Ask user for permission and if accepted get the location
//        //Get location method retrieves the address
//
//        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//        LocationListener locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(@NonNull Location location) {
//                Intent intent = new Intent(LocationActivity.this, MainActivity.class);
//                String result = location.toString();
//                intent.setAction("action");
//                intent.putExtra("location_retrieved", result);
//                sendBroadcast(intent);
//            }
//
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//            }
//
//            @Override
//            public void onProviderEnabled(@NonNull String provider) {
//            }
//
//            @Override
//            public void onProviderDisabled(@NonNull String provider) {
//            }
//        };
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        final Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//    class GetLocation extends AsyncTask<String, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            Geocoder geocoder = new Geocoder(LocationActivity.this, Locale.getDefault());
//            List<Address> addresses = null;
//                    try {
//                        addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
//                    }catch(Exception e) {
//                        e.printStackTrace();
//                    }
//                    if(addresses != null && addresses.size() > 0) {
//                        String address = addresses.get(0).getLocality();
//                        return address;
//                    }else{
//                        return "No address found";
//                    }
//                }
//        }
//    }
//    }
//}

