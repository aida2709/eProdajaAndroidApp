package com.example.aida.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.aida.test.activities.NoviProizvodActivity;
import com.example.aida.test.activities.SviProizvodiActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.CollationElementIterator;


//import com.example.aida.test.Helper.RetrieveFeedTask;


public class MainActivity extends AppCompatActivity {

    Button buttonViewProducts, buttonNewProduct;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeComponents();
    }

    private void initializeComponents() {
        //To change body of created methods use File | Settings | File Templates.
        buttonViewProducts = (Button) findViewById(R.id.buttonViewProducts);
        buttonNewProduct = (Button) findViewById(R.id.buttonAddNewProduct);

        buttonViewProducts.setOnClickListener(buttonViewProductsClickListener);
        buttonNewProduct.setOnClickListener(buttonNewProductClickListener);
    }

    private View.OnClickListener buttonViewProductsClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            //To change body of implemented methods use File | Settings | File Templates.
            Intent intent = new Intent(getApplicationContext(), SviProizvodiActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener buttonNewProductClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            //To change body of implemented methods use File | Settings | File Templates.
            Intent intent = new Intent(getApplicationContext(), NoviProizvodActivity.class);
            startActivity(intent);
        }
    };




   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RetrieveFeedTask ret=new RetrieveFeedTask(MainActivity.this,this);

        Button btnStartQuery=(Button) findViewById(R.id.queryButton);

        btnStartQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ret.execute();
            }
        });
    }

    public class RetrieveFeedTask extends AsyncTask<Void, Void, String> {
        private Context context;
        private Activity activity;

        private Exception exception;

        ProgressBar progressBar;
        EditText emailText;
        TextView responseView;
        String mail;

        public RetrieveFeedTask(Context context, Activity activity) {
            this.activity = activity;
            this.context = context;

            this.progressBar = (ProgressBar) activity.findViewById(R.id.progressBar);
            this.emailText = (EditText) activity.findViewById(R.id.emailText);
            this.responseView = (TextView) activity.findViewById(R.id.responseView);
            this.mail = emailText.getText().toString();
        }

        public void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            responseView.setText("");
        }

        public String doInBackground(Void... urls) {
            //   (and httpURLConnection.getResponseCode()).

            try {

                URL url = new URL("https://api.fullcontact.com/v2/person.json?" + "email=" + mail + "&apiKey=" + "a415c299e7ae2806");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                // try {
                //urlConnection.connect();
                InputStream inputStream=urlConnection.getInputStream();
                InputStreamReader inputStreamReader= new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
                // } finally {
                //urlConnection.disconnect();
                //}
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        public void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }
            progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);
            responseView.setText(response);
        }

        public void execute()
        {
            this.onPreExecute();
            String result=this.doInBackground();
            this.onPostExecute(result);
        }

    }*/
}


