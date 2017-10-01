package com.example.aida.test.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aida.test.EditProductActivity;
import com.example.aida.test.MainActivity;
import com.example.aida.test.R;
import com.example.aida.test.model.JSONHttpClient;
import com.example.aida.test.model.Product;
import com.example.aida.test.model.ResultCode;
import com.example.aida.test.model.ServiceUrl;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UrediProizvodActivity extends AppCompatActivity {
    EditText etName, etPrice, etDescription;
    String name="TEST", price="TEST", description="TEST";
    Button btnSpremi;
    Button btnObrisi;
    int Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uredi_proizvod);

        initializeComponents();

        Id = getIntent().getIntExtra(Product.PRODUCT_ID,0);

        new UcitajProizvod().execute();

        btnSpremi.setOnClickListener(buttonSaveClickListener);
        btnObrisi.setOnClickListener(buttonObrisiClickListener);
    }


    private void initializeComponents() {
        etName=(EditText) findViewById(R.id.etNameEdited);
        etPrice=(EditText) findViewById(R.id.etPriceEdited);
        etDescription=(EditText) findViewById(R.id.etDescriptionEdited);

        name=" ";
        price=" ";
        description=" ";

        btnSpremi=(Button) findViewById(R.id.btnSpremiProizvodEdited);
        btnObrisi=(Button) findViewById(R.id.btnObrisiProizvod);


    }

    private View.OnClickListener buttonSaveClickListener = new View.OnClickListener() {
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

    private View.OnClickListener buttonObrisiClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new ObrisiProizvod().execute();
        }
    };

    class ObrisiProizvod extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(UrediProizvodActivity.this);
            progressDialog.setMessage("Brisanje u toku...");
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair(Product.PRODUCT_ID, String.valueOf(Id)));
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            jsonHttpClient.Delete(ServiceUrl.PRODUCT, args);

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

    class SpremiProizvod extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(UrediProizvodActivity.this);
            progressDialog.setMessage("Detalji proizvoda se spremaju. Molimo sacekajte...");
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {
            Product proizvod=new Product();
            proizvod.setId(Id);
            proizvod.setName(name);
            proizvod.setPrice(Double.parseDouble(price));
            proizvod.setDescription(description);

            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            proizvod = (Product) jsonHttpClient.PostObject(ServiceUrl.PRODUCT, proizvod, Product.class);

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

    class UcitajProizvod extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(UrediProizvodActivity.this);
            progressDialog.setMessage("Detalji proizvoda se ucitavaju. Molimo sacekajte...");
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair(Product.PRODUCT_ID, String.valueOf(Id)));
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            final Product product = jsonHttpClient.Get(ServiceUrl.PRODUCT, args, Product.class);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (product != null) {
                        etName.setText(product.getName());
                        etPrice.setText(String.valueOf(product.getPrice()));
                        etDescription.setText(product.getDescription());
                    }
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
           progressDialog.dismiss();
        }
    }
}
