package com.app.bet.Activities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.bet.Data_Models.Sport;
import com.app.bet.Network_APIs.Network_Calls.EventsCalls;
import com.app.bet.R;
import com.app.bet.Utilities.Utilities;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button searchButton;
    Button addButton;
    Button stopButton;
    EditText teamEditText;
    Spinner sportsSpinner;
    LinearLayout subLinearLayout;
    KProgressHUD hud;
    ArrayList<Sport> sportList = new ArrayList<>();
    ArrayList<EditText> editTextList = new ArrayList<>();
    List<String> teamList = new ArrayList<>();
    String sportID = "";
    Handler myHandler = new Handler();
    private static final String TAG = "MainActivity";

    Runnable myRunnable = new Runnable(){
        @Override
        public void run(){
            // code goes here
            System.out.println("Code executing after 10 seconds");
            myHandler.postDelayed(this, 10000);
          //  Utilities.createNotification(getBaseContext(), "Hi Notification here!");

            searchFunctionShort();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchButton = (Button) findViewById(R.id.searchButton);
        addButton = (Button) findViewById(R.id.addButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        teamEditText = (EditText) findViewById(R.id.teamEditText);
        sportsSpinner = (Spinner) findViewById(R.id.sportsSpinner);
        subLinearLayout = (LinearLayout) findViewById(R.id.subLinearLayout);
        hud = Utilities.createHUD(MainActivity.this);
      //  startService();
     //   myHandler.postDelayed(myRunnable, 10000);


        populateSportList();
        ArrayAdapter<Sport> spinnerArrayAdapter = new ArrayAdapter<Sport>
                (getBaseContext(), android.R.layout.simple_spinner_item, sportList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        sportsSpinner.setAdapter(spinnerArrayAdapter);

        searchButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);

        sportsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Sport sport = (Sport) parent.getSelectedItem();
                Log.d(TAG, "Sport ID: " + sport.sportID + ",  Sport Name: " + sport.sportName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });





    }

    public void populateSportList() {
        sportList.add(new Sport(1, "Soccer"));
        sportList.add(new Sport(13, "Tennis"));
        sportList.add(new Sport(78, "Handball"));
        sportList.add(new Sport(17, "Ice Hockey"));
        sportList.add(new Sport(12, "American Football"));
        sportList.add(new Sport(83, "Futsal"));
        sportList.add(new Sport(92, "Table Tennis"));
        sportList.add(new Sport(8, "Rugby Union"));
        sportList.add(new Sport(36, "Australian Rules"));
        sportList.add(new Sport(9, "Boxing/UFC"));
        sportList.add(new Sport(90, "Floorball"));
        sportList.add(new Sport(110, "Water Polo"));
        sportList.add(new Sport(151, "E-sports"));

        sportList.add(new Sport(18, "Basketball"));
        sportList.add(new Sport(91, "Volleyball"));
        sportList.add(new Sport(16, "Baseball"));
        sportList.add(new Sport(14, "Snooker"));
        sportList.add(new Sport(3, "Cricket"));
        sportList.add(new Sport(15, "Darts"));
        sportList.add(new Sport(94, "Badminton"));
        sportList.add(new Sport(19, "Rugby League"));
        sportList.add(new Sport(66, "Bowls"));
        sportList.add(new Sport(75, "Gaelic Sports"));
        sportList.add(new Sport(	95, "Beach Volleyball"));
        sportList.add(new Sport(107, "Squash"));

    }

    public boolean validation(String sportID, String teamName) {
        if(sportID.length() > 0 && teamName.length() > 0) {
            if(teamName.length() > 3) {
                return true;
            }
            Utilities.showAlertDialog(MainActivity.this, "", getString(R.string.teamLengthError));
            return false;
        }
        Utilities.showAlertDialog(MainActivity.this, "", getString(R.string.fillTeamName));
        return false;
    }

    private String CHANNEL_ID;
    private void createNotificationChannel() {
        CharSequence channelName = CHANNEL_ID;
        String channelDesc = "channelDesc";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(channelDesc);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;
            NotificationChannel currChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
            if (currChannel == null)
                notificationManager.createNotificationChannel(channel);
        }
    }




    public void createNotification(String message) {

        CHANNEL_ID = getString(R.string.app_name);
        if (message != null ) {
            createNotificationChannel();

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            mBuilder.setSound(uri);


            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            int notificationId = (int) (System.currentTimeMillis()/4);
            notificationManager.notify(notificationId, mBuilder.build());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchButton:
                Sport sport = (Sport) sportsSpinner.getSelectedItem();
                sportID = "" + sport.sportID;
                String teamName = teamEditText.getText().toString().trim().toLowerCase();
                teamList.clear();
                if(validation(sportID, teamName)) {
                    teamList.add(teamName.toLowerCase());

                    if(editTextList.size() > 0) {
                        for(EditText editText: editTextList) {
                            if(editText.getText().length() > 0) {
                                teamList.add(editText.getText().toString().toLowerCase());
                            }
                        }
                    }

                    startService();
                    myHandler.postDelayed(myRunnable, 10000);
                }
                break;

            case R.id.addButton:
                EditText editText = new EditText(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(380, LinearLayout.LayoutParams.MATCH_PARENT);
                params.weight = 1.0f;
                params.gravity = Gravity.RIGHT;
                editText.setLayoutParams(params);
                editTextList.add(editText);
                subLinearLayout.addView(editText);
                break;

            case R.id.stopButton:
                stopService();
                Toast.makeText(getBaseContext(), R.string.stopServiceMessage, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    public void searchFunctionShort() {
        for(String team: teamList) {
            System.out.println("Team: " + team);
        }
        System.out.println("////");

     //   hud.show();
        EventsCalls.getUpcomingEvents(getBaseContext(), sportID, teamList, new Utilities.Callback() {
            @Override
            public void onResult(String message) {
       //         hud.dismiss();
                if (message.equals("success")) {

                }
            }
        });
    }

    public void searchFunction() {
        Sport sport = (Sport) sportsSpinner.getSelectedItem();
        String sportID = "" + sport.sportID;
        String teamName = teamEditText.getText().toString().trim().toLowerCase();
        List<String> teamList = new ArrayList<>();

        if(validation(sportID, teamName)) {
            teamList.add(teamName);

            if(editTextList.size() > 0) {
                for(EditText editText: editTextList) {
                    if(editText.getText().length() > 0) {
                        teamList.add(editText.getText().toString());
                    }
                }
            }

            for(String team: teamList) {
                System.out.println("Team: " + team);
            }
            System.out.println("////");

            hud.show();
            EventsCalls.getUpcomingEvents(getBaseContext(), sportID, teamList, new Utilities.Callback() {
                @Override
                public void onResult(String message) {
                    hud.dismiss();
                    if (message.equals("success")) {

                    }
                }
            });
        }
    }

    public void startService() {
        System.out.println(TAG + " startService got called");
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        ContextCompat.startForegroundService(this, serviceIntent);
    }
    public void stopService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
        myHandler.removeCallbacksAndMessages(null);
    }

}

