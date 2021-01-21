package com.example.safezone;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class Register_number extends AppCompatActivity {
    DatabaseHelper db;
    EditText name, number;
    Button btn_save, btn_view,btn_send;
    private GoogleMap mMap;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private long MIN_time = 10000;
    private long MIN_DIST = 10;
    private LatLng latLng;
    Cursor cur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_number);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_view = (Button) findViewById(R.id.btn_view);

        db = new DatabaseHelper(this);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = db.insertdata(name.getText().toString(), number.getText().toString());
                if (result) {
                    Toast.makeText(getApplicationContext(), "save successfully ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "error !! Could not saved ,please try again later  ", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cur = db.getAllData();
                StringBuffer stringBuffer = new StringBuffer();
                if (cur.getCount() == 0) {
                    //Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_SHORT).show();
                    showmessage("Saved Data", "No Data found");
                }
                while (cur.moveToNext()) {

                    stringBuffer.append("Name: " + cur.getString(1) + "\n");
                    stringBuffer.append("Phone Number : " + cur.getString(2) + "\n");
                    stringBuffer.append("\n");


                }
                showmessage(" Saved Data", stringBuffer.toString());


            }
        });

    }

    private void showmessage(String data, String stringBuffer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(data);
        builder.setMessage(stringBuffer);
        builder.show();
    }


}
