package com.example.aida.test;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.aida.test.model.JSONHttpClient;
import com.example.aida.test.model.Product;
import com.example.aida.test.model.ServiceUrl;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class NewProductActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private EditText editTextName, editTextPrice, editTextDescription;
    private Button buttonCreateProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uredi_proizvod);

        initializeComponents();
    }

    private void initializeComponents() {
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextPrice = (EditText) findViewById(R.id.editTextPrice);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        //buttonCreateProduct = (Button) findViewById(R.id.buttonCreateProduct);
        buttonCreateProduct.setOnClickListener(buttonCreateProductClickListener);
    }

    private View.OnClickListener buttonCreateProductClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new CreateNewProduct().execute();
        }
    };

    class CreateNewProduct extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(NewProductActivity.this);
            progressDialog.setMessage("Creating Product...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair("name", editTextName.getText().toString()));
            args.add(new BasicNameValuePair("price", editTextPrice.getText().toString()));
            args.add(new BasicNameValuePair("description", editTextDescription.getText().toString()));

            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            Product product = (Product) jsonHttpClient.PostParams(ServiceUrl.PRODUCT, args, Product.class);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
            return null;
        }
    }
}
