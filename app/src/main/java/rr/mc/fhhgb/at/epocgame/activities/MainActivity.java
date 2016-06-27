package rr.mc.fhhgb.at.epocgame.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.emotiv.insight.IEmoStateDLL;

import java.util.Timer;
import java.util.TimerTask;

import rr.mc.fhhgb.at.epocgame.model.EngineConnector;
import rr.mc.fhhgb.at.epocgame.model.EngineInterface;
import rr.mc.fhhgb.at.epocgame.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, EngineInterface {

    private static final String TAG = "brain2machine";
    static boolean isEPOC = false;
    int MY_PERMISSIONS_REQUEST_BLUETOOTH;
    EngineConnector engineConnector;
    TextView batteryStatus;
    TextView connectionStatus;
    ProgressDialog progressDialog;
    int MY_PERMISSIONS_REQUEST_ACCESSLOCATION;
    AlertDialog.Builder alertNotConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Alert for no Connection
        alertNotConnected = new AlertDialog.Builder(this);
        alertNotConnected.setMessage("Du bist nicht mit einem EPOC+ Gerät verbunden, trotzdem fortfahren?");


        //Alert for Connection to EPOC+
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Willst du dich mit einem EPOC+ Gerät verbinden?");
        alertDialogBuilder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isEPOC= true;
                EngineConnector.setContext(MainActivity.this);
                engineConnector = EngineConnector.shareInstance();
                EngineConnector.delegate = MainActivity.this;
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!bluetoothAdapter.isEnabled()) {
                    bluetoothAdapter.enable();
                }
            }
        });
        alertDialogBuilder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isEPOC=false;
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        accessPermissions(); //access permissions on runtime for android >6.0
        batteryStatus = (TextView) findViewById(R.id.batteryText);
        connectionStatus = (TextView) findViewById(R.id.signalText);
        connectionStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,ConnectionActivity.class);
                startActivity(i);
            }
        });


       // progressDialog = ProgressDialog.show(this,"Verbindungsaufbau","EPOC+ Gerät wird verbunden",true);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() { // TimerTask für die BatteryConnection
            @Override
            public void run() {
                runOnUiThread(new Runnable() { // Update View in Thread
                    @Override
                    public void run() {
                        if (engineConnector!= null) {
                            if (engineConnector.isConnected) {
                                connectionStatus.setTextColor(Color.parseColor("#2E7D32"));
                                connectionStatus.setText("Verbunden");
                                connectionStatus.setEnabled(true);
                                setBatteryStatus(IEmoStateDLL.IS_GetBatteryChargeLevel()[0]);
                                // progressDialog.dismiss();


                            } else {
                                connectionStatus.setTextColor(Color.RED);
                                connectionStatus.setText("Nicht verbunden");
                               // connectionStatus.setEnabled(false);
                                // set state so it doesnt jump in between values
                                setBatteryStatus(0);
                                //   progressDialog.show();

                            }
                        }
                    }
                });

            }
        };
        timer.schedule(timerTask, 0, 1000);

        //IEdk.IEE_EngineConnect(this,"");
        Button button = null;
        button = (Button) findViewById(R.id.buttonPlay);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.buttonPractise);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.buttonHowto);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.buttonHighscore);
        button.setOnClickListener(this);
    }


    /**
     * Setzt den Batterystatus anhand der ID die geliefert wird
     * @param id Die ID, die vom System kommt
     */
    private void setBatteryStatus(int id) {
        switch (id) {
            case -1:
                //batteryStatus.setText("-");
                break;
            case 0:
                batteryStatus.setText("-");
                break;
            case 1:
                batteryStatus.setText("Akku: 20%");
                break;
            case 2:
                batteryStatus.setText("Akku: 40%");
                break;
            case 3:
                batteryStatus.setText("Akku: 60%");
                break;
            case 4:
                batteryStatus.setText("Akku: 80%");
                break;
            case 5:
                batteryStatus.setText("Akku: 100%");
                break;
            default:
                batteryStatus.setText("-");
        }
    }

    @Override
    public void onClick(View _v) {
        switch (_v.getId()) {
            case R.id.buttonPlay: {
                if(engineConnector == null || !engineConnector.isConnected) {
                    if(isEPOC) {
                        alertNotConnected.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(MainActivity.this, TestPlayActivity.class);
                                startActivity(i);
                            }
                        });
                        alertNotConnected.setNegativeButton("Nein",null);
                        AlertDialog alertDialog = alertNotConnected.create();
                        alertDialog.show();
                    }else {
                        Intent i = new Intent(MainActivity.this, TestPlayActivity.class);
                        startActivity(i);
                    }

                }else {
                    Intent i = new Intent(MainActivity.this, PlayActivity.class);
                    startActivity(i);
                }
            }
            break;

            case R.id.buttonPractise: {
                if(engineConnector == null || !engineConnector.isConnected) {
                    if(isEPOC) {
                        alertNotConnected.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(MainActivity.this, PractiseActivity.class);
                                startActivity(i);
                            }
                        });
                        alertNotConnected.setNegativeButton("Nein",null);
                        AlertDialog alertDialog = alertNotConnected.create();
                        alertDialog.show();
                    }else {
                        Intent i = new Intent(MainActivity.this, PractiseActivity.class);
                        startActivity(i);
                    }

                }else {
                    Intent i = new Intent(MainActivity.this, PractiseActivity.class);
                    startActivity(i);
                }
                Log.i(TAG, "Button practise pressed");

            }
            break;

            case R.id.buttonHowto: {
                Log.i(TAG, "Button howto pressed");
                Intent i = new Intent(this, TestPlayActivity.class);
                startActivity(i);
            }
            break;
            case R.id.buttonHighscore: {
                Log.i(TAG, "Button highscore pressed");
                Intent i = new Intent(this, HighscoreActivity.class);
                startActivity(i);
            }
            break;
            default:
                Log.e(TAG, "unknown onClick ID encountered..");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_favorite:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void isConnected() {

    }
    @Override
    public void trainStarted() {

    }

    @Override
    public void trainSucceed() {

    }

    @Override
    public void trainFailed() {

    }

    @Override
    public void trainCompleted() {

    }

    @Override
    public void trainRejected() {

    }

    @Override
    public void trainReset() {

    }

    @Override
    public void trainErased() {

    }


    @Override
    public void currentAction(int typeAction, float power) {

    }

    private void accessPermissions() {
        // Request & set Permissions
        //Bluetooth
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // No explanation
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESSLOCATION);
            }
        }

        // access fineLocation
        int permissionCheck2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH);

        // MainActivity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.BLUETOOTH)) {
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_BLUETOOTH);
            }
        }
    }
}
