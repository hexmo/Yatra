package com.example.yatra.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.yatra.R;
import com.example.yatra.SplashScreenActivity;
import com.example.yatra.models.BookingModel;
import com.example.yatra.models.BusModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;


public class BroadcastReceiverClass extends BroadcastReceiver {

    FirebaseFirestore db;
    SimpleDateFormat formatter;
    Date date;
    String todayDate;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("bCast", "BroadcastReceiverClass was called.");

        db = FirebaseFirestore.getInstance();

        //get and store date so we can get notifications.
        //https://www.javatpoint.com/java-get-current-date
        formatter = new SimpleDateFormat("MMM dd, yyyy");
        date = new Date();
        todayDate = formatter.format(date);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //for compatibility
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("ID001", "Yatra notification.", importance);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = ContextCompat.getSystemService(context, NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        //intents
        Intent intent2 = new Intent(context, SplashScreenActivity.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent2, 0);

        //get all booking
        //https://firebase.google.com/docs/firestore/query-data/get-data
        //run a for loop around them.
        db.collection("booking")
                .whereEqualTo("bookerID", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("bCast", "Successfully fetched data.");
                                //City city = documentSnapshot.toObject(City.class);

                                BookingModel bookingModel = document.toObject(BookingModel.class);

                                Log.d("bCast", "Today date: " + todayDate);
                                Log.d("bCast", "Ticket date: " + bookingModel.getBookedDate());
                                if (bookingModel.getBookedDate().equals(todayDate)) {

                                    DocumentReference docRef = db.collection("bus").document(bookingModel.getBusID());
                                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            BusModel busModel = documentSnapshot.toObject(BusModel.class);
                                            String contextText = busModel.getJourneyDate() +" > " + busModel.getEndDestination() + " at: " + busModel.getDepartureTime();
                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ID001").setSmallIcon(R.mipmap.ic_app_icon).
                                                    setContentTitle("Reminder! You have ticket booked for today.")
                                                    .setContentText(contextText)
                                                    .setContentIntent(pendingIntent)
                                                    .setPriority(NotificationCompat.PRIORITY_HIGH).setAutoCancel(true);


                                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

                                            int number = Integer.parseInt(bookingModel.getBookerContact().substring(5));

                                            notificationManager.notify(number, builder.build());
                                        }
                                    });






                                }

                            }
                        } else {
                            Log.d("Notification:", "Error getting documents: ", task.getException());
                        }
                    }

                });


    }
}

