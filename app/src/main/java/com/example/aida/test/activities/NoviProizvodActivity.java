package com.example.aida.test.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.aida.test.EditProductActivity;
import com.example.aida.test.MainActivity;
import com.example.aida.test.NewProductActivity;
import com.example.aida.test.R;
import com.example.aida.test.model.JSONHttpClient;
import com.example.aida.test.model.Product;
import com.example.aida.test.model.ServiceUrl;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class NoviProizvodActivity extends AppCompatActivity {
    EditText etName, etPrice, etDescription;
    String name, price, description;
    Button btnSpremi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novi_proizvod);

        initializeComponents();

        btnSpremi.setOnClickListener(buttonCreateProductClickListener);

    }

    private View.OnClickListener buttonCreateProductClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(etName.getText().length()>0)
                 name=etName.getText().toString();
            if(etPrice.getText().length()>0)
                price=etPrice.getText().toString();
            if(etDescription.getText().length()>0)
                description=etDescription.getText().toString();

            new SpremiProizvod().execute();
        }
    };

    private void initializeComponents() {
        etName=(EditText) findViewById(R.id.etName);
        etPrice=(EditText) findViewById(R.id.etPrice);
        etDescription=(EditText) findViewById(R.id.etDescription);

        name=" ";
        price=" ";
        description=" ";

        btnSpremi=(Button) findViewById(R.id.btnSpremiProizvod);

    }

    class SpremiProizvod extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(NoviProizvodActivity.this);
            progressDialog.setMessage("Spremanje proizvoda...");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> args = new ArrayList<NameValuePair>();

            args.add(new BasicNameValuePair("name",name));
            args.add(new BasicNameValuePair("price", price));
            args.add(new BasicNameValuePair("description", description));

            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            Product product = (Product) jsonHttpClient.PostParams(ServiceUrl.PRODUCT, args, Product.class);

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
        }
    }
}
