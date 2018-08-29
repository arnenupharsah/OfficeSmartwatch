package com.example.aulick.officesmartwatch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

//implementing onclicklistener
public class MainActivity extends Activity implements View.OnClickListener {

    //View Objects
    private Button buttonScan;
    private TextView textViewName, textViewAddress;

    static String EMAIL = "abhi1aa@outlook.com";
//    static String qrcode;

    //qr code scanner object
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //View objects
        buttonScan = (Button) findViewById(R.id.buttonScan);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);

        //intializing scan object
        qrScan = new IntentIntegrator(this);

        //attaching onclick listener.make
        buttonScan.setOnClickListener(this);



        OneSignal.sendTag("User id",EMAIL);

        OneSignal.getTags(new OneSignal.GetTagsHandler() {
            @Override
            public void tagsAvailable(JSONObject tags) {
                Log.d("debug", "log 1"+tags.toString());
                //Toast.makeText(ApplicationClass.this, tags.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        Toast.makeText(this, EMAIL, Toast.LENGTH_SHORT).show();
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    textViewName.setText(obj.getString("name"));
                    textViewAddress.setText(obj.getString("address"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    notify(result.getContents().toString());
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onClick(View view) {
        //initiating the qr code scan
        qrScan.initiateScan();
    }




    public void notify(final String qrcode){

        AsyncTask.execute((new Runnable() {
            @Override
            public void run() {

                int SDK_INT = Build.VERSION.SDK_INT;
                if(SDK_INT > 8){
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy((policy));
                    String send_email;
                    if(EMAIL.equals("abhi1aa@outlook.com")){
                        send_email = "abhi2aa@outlook.com";
                    }  else{
                        send_email = "abhi1aa@outlook.com";
                    }

                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection)url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic NGEwMGZmMjItY2NkNy0xMWUzLTk5ZDUtMDAwYzI5NDBlNjJj");
                        con.setRequestMethod("POST");

                        String strJsonBody = "{"
                                +   "\"app_id\": \"d153ab9b-1844-4a46-ad13-10bf8b8e13e0\","
                                +   "\"include_player_ids\": [\""+qrcode+"\"],"
                                +   "\"data\": {\"foo\": \"bar\"},"
                                +   "\"buttons\": [{\"id\": \"id1\", \"text\": \"Accept\",\"icon\": \"ic_menu_share\" }, {\"id\": \"id2\", \"text\": \"Decline\",\"icon\": \"ic_menu_share\" }],"
                                +   "\"contents\": {\"en\": \"English Message\"}"
                                + "}";


                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        if (  httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch(Throwable t) {
                        t.printStackTrace();
                    }


                }


            }
        }));


    }
}