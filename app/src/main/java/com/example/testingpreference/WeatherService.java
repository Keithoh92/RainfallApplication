package com.example.testingpreference;
import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Button;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class WeatherService extends IntentService {

    private static final String CHANNEL_ID = "9876";
    private int result = Activity.RESULT_CANCELED;
    public static final String LOCATION = "location";
    public static final String DAY1 = "day1";
    public static final String DAY2 = "day2";
    public static final String DAY3 = "day3";
    public static final String DAY4 = "day4";
    public static final String DAY5 = "day5";
    public static final String MM9 = "mm9";
    public static final String MM12 = "mm12";
    public static final String MM15 = "mm15";
    public static final String MM18 = "mm18";
    public static final String MM21 = "mm21";
    public static final String MM00 = "mm00";
    public static final String DESC9 = "desc9";
    public static final String DESC12 = "desc12";
    public static final String DESC15 = "desc15";
    public static final String DESC18 = "desc18";
    public static final String DESC21 = "desc21";
    public static final String DESC00 = "desc00";
    public static final String NOOFDAYS = "noOfDays";
    public static final String  RAINFALLPREF = "rainfallPref";
    public static final String RESULT = "result";

    public WeatherService() {
        super("WeatherService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("testing", "Downloading API response");
        String userLocation = intent.getStringExtra("usersLocation");
        Log.d("testing", "intent usersLocation=" +userLocation);
        int noOfDays = intent.getIntExtra("noofdays", 0);
        Log.d("testing", "intent noofDays=" +noOfDays);
        int rainfallPref = intent.getIntExtra("rainfall", 0);
        Log.d("testing", "intent rainfall Preferences=" +rainfallPref);
        //pass users location to weatherClient and download API response
        String data = ((new WeatherHttpClient().getWeatherData(userLocation)));

        try {


            //Load the response from API call
            JSONObject jObj = new JSONObject(data);
            // Get the weather array inside API response
            JSONArray jArr = jObj.getJSONArray("list");

            Double mm9 = 0.0;
            Double mm12 = 0.0;
            Double mm15 = 0.0;
            Double mm18 = 0.0;
            Double mm21 = 0.0;
            Double mm00 = 0.0;
            Double day1 = 0.0;
            Double day2 = 0.0;
            Double day3 = 0.0;
            Double day4 = 0.0;
            Double day5 = 0.0;
            Double day6 = 0.0;
            String description9 = "";
            String description12 = "";
            String description15 = "";
            String description18 = "";
            String description21 = "";
            String description00 = "";

            int counter = 0;

            //Initialise loop to 40 iterations
            while(counter < jArr.length()) {
                //Get list array
                JSONArray windArray = jObj.getJSONArray("list");

                //Loop through each object in listArray(windArray)
                JSONObject time2 = windArray.getJSONObject(counter);

                //Get weather array inside list array
                JSONArray weatherArray = time2.getJSONArray("weather");
                JSONObject weatherObject = weatherArray.getJSONObject(0);

                //Get description of weather at each timestamp in the next 18 hours(6 timestamps)
                if(counter == 0) {description9 = weatherObject.getString("description");}
                if(counter == 1) {description12 = weatherObject.getString("description");}
                if(counter == 2) {description15 = weatherObject.getString("description");}
                if(counter == 3) {description18 = weatherObject.getString("description");}
                if(counter == 4) {description21 = weatherObject.getString("description");}
                if(counter == 6) {description00 = weatherObject.getString("description");}
                //Check that rainfall has occurred for that day
                if(time2.has("rain")) {
                    //if it has get the amount that fell
                    JSONObject wind2 = time2.getJSONObject("rain");

                    Double s = wind2.getDouble("3h");
                    //Get the percipiation for each day starting from current time today
                    if(counter <= 0) {mm9 = s;}
                    if(counter == 1) {mm12 = s;}
                    if(counter == 2) {mm15 = s;}
                    if(counter == 3) {mm18 = s;}
                    if(counter == 4) {mm21 = s;}
                    if(counter == 5) {mm00 = s;}
                    //^^used for setting the precipitation for the next day in UI

                    //Get total rainfall for next 5 days 40 timestamps 8 timestamps for each day
                    if(counter <= 5) {day1 = day1 + s;} //Day1
                    if(counter >= 6 && counter <= 13) {day2 = day2 + s;} //Day2
                    if(counter >= 14 && counter <= 21) {day3 = day3 + s;} //day3
                    if(counter >= 22 && counter <= 29) {day4 = day4 + s;} //day4
                    if(counter >= 30 && counter <= 37) {day5 = day5 + s;} //day5
                    if(counter >= 38 && counter <= 40) {day6 = day6 + s;} //day6
                    Log.d("testing", "Got all values of rainfall");

                    counter++;
                }
                else { //if no rainfall for current time stamp increment counter and go to next time stamp
                    counter++;
                }
            }
            result = Activity.RESULT_OK;


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
            Intent notIntent = new Intent(this, MainActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, notIntent, 0);



            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.icon_rain)
                    .setContentTitle("Rainfall Alert")
                    .setContentText(day1.toString() +"mm of rain expected over the next " +noOfDays +"days")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true);

            //Notification logic that checks users preferences and sends notification to user if the rainfall is above threshold over the set number of days
            if(noOfDays == 1 && rainfallPref <= day1) {
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(0, builder.build());
                Log.d("testing", "Sent notification for exceeding 1 day rainfall threshold");
            }
            if(noOfDays == 2 && rainfallPref <= day1+day2) {
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(1, builder.build());
                Log.d("testing", "Sent notification for exceeding 2 day rainfall threshold");
            }
            if(noOfDays == 3 && rainfallPref <= day1+day2+day3) {
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(2, builder.build());
                Log.d("testing", "Sent notification for exceeding 3 day rainfall threshold");

            }
            if(noOfDays == 4 && rainfallPref < day1+day2+day3+day4) {
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(3, builder.build());
                Log.d("testing", "Sent notification for exceeding 4 day rainfall threshold");

            }
            if(noOfDays == 5 && rainfallPref < day1+day2+day3+day4+day5) {
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(4, builder.build());
                Log.d("testing", "Sent notification for exceeding 5 day rainfall threshold");

            }

            //Pass data to ppublishResults method that will send the data in a broadcast to broadcast receiver in mainActivity
            publishResults(noOfDays, rainfallPref, day1, day2, day3, day4, day5, mm9, mm12, mm15, mm18, mm21, mm00, description9, description12, description15, description18, description21, description00, result);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    public void publishResults(int noOfDays, int rainfallPref, Double day1, Double day2, Double day3, Double day4, Double day5, Double mm9, Double mm12, Double mm15, Double mm18, Double mm21, Double mm00, String description9, String description12,String description15,String description18,String description21,String description00, int result){
                //Send data in intent to BroadCast receiver
                Intent intent = new Intent("MyBroadcast");
                intent.putExtra(DAY1, day1);
                intent.putExtra(DAY2, day2);
                intent.putExtra(DAY3, day3);
                intent.putExtra(DAY4, day4);
                intent.putExtra(DAY5, day5);
                intent.putExtra(MM9, mm9);
                intent.putExtra(MM12, mm12);
                intent.putExtra(MM15, mm15);
                intent.putExtra(MM18, mm18);
                intent.putExtra(MM21, mm21);
                intent.putExtra(MM00, mm00);
                intent.putExtra(DESC9, description9);
                intent.putExtra(DESC12, description12);
                intent.putExtra(DESC15, description15);
                intent.putExtra(DESC18, description18);
                intent.putExtra(DESC21, description21);
                intent.putExtra(DESC00, description00);
                intent.putExtra(NOOFDAYS, noOfDays);
                intent.putExtra(NOOFDAYS, rainfallPref);
                intent.putExtra(RESULT, result);
                sendBroadcast(intent);
        Log.d("testing", "Sent broadcast back got called");
    }
}
