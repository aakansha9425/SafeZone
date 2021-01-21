package com.example.safezone;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

public class home extends AppCompatActivity {
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    DatabaseHelper db;
    private GoogleMap mMap;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private long MIN_time = 0;
    private long MIN_DIST = 0;
    private LatLng latLng;
    Button btn_send, btn_register,btn_womensafety,btn_escapefromthreat,btn_selfdefence,btn_instruct;
    Cursor cur;
    TextView tvAddress;
    String mymessage;
    String url, latitude_url, longitude_url;
    AppLocationService appLocationService;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        btn_womensafety=findViewById(R.id.womensafetytips);
        btn_escapefromthreat=findViewById(R.id.escape_from_threat);
        btn_selfdefence=findViewById(R.id.self_defence_video);
        appLocationService = new AppLocationService(
                home.this);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        db = new DatabaseHelper(this);
        btn_send = (Button) findViewById(R.id.send);
        btn_register = (Button) findViewById(R.id.register);
        btn_instruct=findViewById(R.id.inztructions);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("sending","message sending started");
                sendsms();
                  //Toast.makeText(home.this, "sending.... smapt ", Toast.LENGTH_SHORT).show();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Register_number.class);
                startActivity(intent);

            }
        });

        btn_womensafety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),women_safety_tips.class);
                startActivity(intent);
            }
        });
        btn_escapefromthreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),Escape_from_threatActivity.class);
                startActivity(intent);
            }
        });
        btn_selfdefence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),self_defence_video_Activity.class);
                startActivity(intent);
            }
        });
        btn_instruct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),InstructionActivity.class);
                startActivity(intent);
            }
        });

    }



    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 12) {
                Toast.makeText(getApplicationContext(), "Shake event detected", Toast.LENGTH_SHORT).show();
                sendsms();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };



    public void sendsms() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {


                try {

                    Log.i("function call ","insid try block of function call ");
                    // declaration
                    StringBuffer phonenumbers_detail;
                    String allphonenumbers;
                    String allpno[];
                    String mymsg_is_null = null;

                    //inialization
                    phonenumbers_detail = new StringBuffer();
                    SmsManager smsManager = SmsManager.getDefault();
                    cur = db.getAllData();
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    Location gpsLocation = appLocationService
                            .getLocation(LocationManager.GPS_PROVIDER);

                    Location networkLocation = appLocationService
                            .getLocation(LocationManager.NETWORK_PROVIDER);

                    Location location1 = appLocationService
                            .getLocation(LocationManager.NETWORK_PROVIDER);


                    // mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in myposition"));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    // String phonenumber[] = {"8962739432","8602168534"};


                    while (cur.moveToNext()) {

                        phonenumbers_detail.append(cur.getString(2));
                        phonenumbers_detail.append(" ");
                    }

                    //phonenumber_detail ko string mai convert kiya
                    allphonenumbers = phonenumbers_detail.toString();

                    // string mai saare no. the to split kar diya jisse ek ek phone number alag ho jaaye
                    allpno = allphonenumbers.split(" ");

                    //get latitude and longitude
                    if (gpsLocation != null) {
                        double latitude = gpsLocation.getLatitude();
                        double longitude = gpsLocation.getLongitude();
                        url = " " + " http://maps.google.com/maps?daddr=" + gpsLocation.getLatitude() + "," + gpsLocation.getLongitude();
                        mymessage = "Hello ! i need your help , i am in danger please help me by reaching the given location "+ url;


                    } else if (networkLocation != null) {
                        double latitude = networkLocation.getLatitude();
                        double longitude = networkLocation.getLongitude();
                        url = " " + " http://maps.google.com/maps?daddr=" + networkLocation.getLatitude() + "," + networkLocation.getLongitude();
                        mymessage = "Hello ! i need your help , i am in danger please help me by reaching the given location "+ url;
                        mymsg_is_null = "Latitude: "
                                + networkLocation.getLatitude() + " Longitude: "
                                + networkLocation.getLongitude() + " " + url;
                    } else {
                        showSettingsAlert();
                    }


                    //get address from latitude and longitude
                    if (location1 != null) {
                        String msg;
                        double latitude = location1.getLatitude();
                        double longitude = location1.getLongitude();
                        url = " " + " http://maps.google.com/maps?daddr=" + location1.getLatitude() + "," + location1.getLongitude();
                        //LocationAddress locationAddress = new LocationAddress();
                        msg =  "Hello ! i need your help , i am in danger please help me by reaching the given location "+ url;
                        mymessage = msg;
                        // Toast.makeText(home.this, mymessage+"location1", Toast.LENGTH_SHORT).show();
                        if (mymessage == null) {
                            mymessage = mymsg_is_null;
                            // Toast.makeText(home.this, mymessage+"", Toast.LENGTH_SHORT).show();
                        }
                        //Toast.makeText(home.this, "inside send btn "+ mymessage, Toast.LENGTH_SHORT).show();
                    } else {
                        showSettingsAlert();
                    }


                    //String mylatitude = String.valueOf(location.getLatitude());
                    //String mylongitude = String.valueOf(location.getLongitude());
                    //mymessage = "my location cordinates \nLatitude : " + mylatitude + "\nLongitude : " + mylongitude;

                    for (int i = 0; i < allpno.length; i++) {
                        // Toast.makeText(home.this, "sms pahuchane aaye hai ", Toast.LENGTH_SHORT).show();
                        smsManager.sendTextMessage(allpno[i], null, mymessage, null, null);
                    }
                    Toast.makeText(getApplicationContext(), "Message Sent !!", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error could not send sms", Toast.LENGTH_SHORT);
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        try {
            Log.i("request123","inside request updates ");
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_time, MIN_DIST, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_time, MIN_DIST, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                home.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        home.this.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    public class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }

        }
    }

    @Override
    protected void onResume() {
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
}
