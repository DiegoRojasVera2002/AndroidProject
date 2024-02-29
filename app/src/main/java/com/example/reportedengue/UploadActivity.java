package com.example.reportedengue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class UploadActivity extends AppCompatActivity {

    Button saveButton, getLocation;
    EditText uploadTopic, uploadDesc, uploadLang, uploadDescr,uploadDirec;

    Double Latitud;
    Double Longitud;

    FusedLocationProviderClient fusedLocationProviderClient;

    private final static int REQUEST_CODE=100;

    // Constante para la clave de la referencia de Firebase
    private static final String FIREBASE_REF = "Android Tutorials";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        uploadDesc = findViewById(R.id.uploadDesc);
        uploadTopic = findViewById(R.id.uploadTopic);
        uploadLang = findViewById(R.id.uploadLang);
        uploadDescr = findViewById(R.id.uploadDescr);
        uploadDirec = findViewById(R.id.uploadDirec);
        saveButton = findViewById(R.id.saveButton);
        getLocation = findViewById(R.id.get_location_btn);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    private void getLastLocation() {
        Log.d("UploadActivity", "Intentando obtener la última ubicación...");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            Log.d("UploadActivity", "Última ubicación obtenida exitosamente.");
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(UploadActivity.this, Locale.getDefault());
                                List<Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    if (addresses != null && !addresses.isEmpty()) {
                                        String direccion = addresses.get(0).getAddressLine(0);
                                        Latitud = addresses.get(0).getLatitude();
                                        Longitud = addresses.get(0).getLongitude();
                                        uploadDirec.setText("" + direccion);
                                        Toast.makeText(UploadActivity.this, "tu direccion: " + direccion, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.e("UploadActivity", "La lista de direcciones está vacía.");
                                        Toast.makeText(UploadActivity.this, "No se pudo obtener la dirección", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (IOException e) {
                                    Log.e("UploadActivity", "Error al obtener la dirección: " + e.getMessage());
                                    Toast.makeText(UploadActivity.this, "Error al obtener la dirección", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        } else {
            askPermision();
        }
    }



    private void askPermision() {
        ActivityCompat.requestPermissions(UploadActivity.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==REQUEST_CODE){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
            else {
                Toast.makeText(this, "Permiso Requerido", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void saveData(){
        String title = uploadTopic.getText().toString().trim();
        String desc = uploadDesc.getText().toString().trim();
        String lang = uploadLang.getText().toString().trim();
        String email = uploadDescr.getText().toString().trim();
        String direc = uploadDirec.getText().toString().trim();

        // Validación de campos no vacíos
        if (title.isEmpty() || desc.isEmpty() || lang.isEmpty()) {
            Toast.makeText(this, "Por Favor Completar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (lang.length() >= 10 || lang.length() < 9 ){
            Toast.makeText(this, "El numero no es valido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Ingrese un correo electrónico válido", Toast.LENGTH_SHORT).show();
            return;
        }

        DataClass dataClass = new DataClass(title, desc, lang, email,direc, Latitud, Longitud);

        // Inflar el layout de progreso
        View progressLayout = getLayoutInflater().inflate(R.layout.progress_layout, null);
        // Mostrar el layout de progreso
        setContentView(progressLayout);

        // Obtener la fecha y hora actual y formatearla
        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        currentDate = currentDate.replaceAll("[^a-zA-Z0-9]", "_");

        // Guardar en Firebase
        FirebaseDatabase.getInstance().getReference(FIREBASE_REF).child(currentDate)
                .setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(UploadActivity.this, "CASO GUARDADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(UploadActivity.this, "ERROR AL GUARDAR EL CASO", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}