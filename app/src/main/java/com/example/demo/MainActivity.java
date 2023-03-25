package com.example.demo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/*
public class MainActivity extends AppCompatActivity {

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShutdownDialog();
            }
        });
    }
    private void showShutdownDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to power off your device?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Perform device shutdown
                        Intent i = new Intent(Intent.ACTION_SHUTDOWN);
                        i.putExtra("android.intent.extra.KEY_CONFIRM", true);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Dismiss dialog and do nothing
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}*/
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ENABLE_ADMIN = 1;

    private DevicePolicyManager devicePolicyManager;
    private ComponentName deviceAdminComponentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        deviceAdminComponentName = new ComponentName(this, DeviceAdminReceiver.class);

        if (!devicePolicyManager.isAdminActive(deviceAdminComponentName)) {
            // Request the necessary permission
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdminComponentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "This app requires admin access to shutdown the device.");
            startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
        } else {
            // Shutdown the device
            devicePolicyManager.reboot(deviceAdminComponentName);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ENABLE_ADMIN) {
            if (resultCode == RESULT_OK) {
                // Permission granted, shutdown the device
                devicePolicyManager.reboot(deviceAdminComponentName);
            } else {
                // Permission denied
                Toast.makeText(this, "Admin permission required to shutdown the device.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}