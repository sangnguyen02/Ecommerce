package com.example.ecommerce.Employee.Driver.Activities;

import com.example.ecommerce.R;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class SendNotificationActivityDriver extends AppCompatActivity {
    String idOrder;
    // Notification channel ID
    private static final String CHANNEL_ID = "ride_request_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_request_driver);
        idOrder = getIntent().getStringExtra("idOrder");

        createNotificationChannel();
        showRideRequestNotification();
    }

    // Function to create a notification channel
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Ride Request",
                NotificationManager.IMPORTANCE_HIGH
        );
        channel.setDescription("New ride request notification");
        channel.enableLights(true);
        channel.setLightColor(Color.RED);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    // Function to show the ride request notification
    private void showRideRequestNotification() {
        // Intent to open the app when the notification is clicked
        Intent intent = new Intent(this, ReceiveOrderActivityDriver.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Intent to handle "Accept" action
        Intent acceptIntent = new Intent(this, AcceptOrderActivityDriver.class);
        acceptIntent.putExtra("idOrder", idOrder);
        PendingIntent acceptPendingIntent = PendingIntent.getActivity(this, 0, acceptIntent, PendingIntent.FLAG_IMMUTABLE);

        // Intent to handle "Cancel" action

        // Build the notification with actions
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_notification)
                .setContentTitle("New Ride Request")
                .setContentText("John Doe has requested a ride.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent) // Set the main intent
                .addAction(R.drawable.icon_accept, "Accept", acceptPendingIntent);

        // Add additional actions or customize the notification further as needed

        // NotificationManager to display the notification
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

}

