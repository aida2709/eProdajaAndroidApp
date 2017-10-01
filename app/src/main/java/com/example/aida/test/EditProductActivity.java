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
import com.example.aida.test.model.ResultCode;
import com.example.aida.test.model.ServiceUrl;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class EditProductActivity extends AppCompatActivity {

    private EditText editTextName, editTextPrice, editTextDescription;
    private Button buttonSave, buttonDelete;
    private int productId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.activity_edit_product);

        initializeComponents();

        Intent intent = getIntent();
        productId = intent.getIntExtra(Product.PRODUCT_ID, 0);
        new GetProductDetails().execute();

    }

    private void initializeComponents() {
        //To change body of created methods use File | Settings | File Templates.
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        editTextPrice = (EditText) findViewById(R.id.editTextPrice);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);

        buttonSave.setOnClickListener(buttonSaveClickListener);
        buttonDelete.setOnClickListener(buttonDeleteClickListener);
    }

    private View.OnClickListener buttonDeleteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new DeleteProduct().execute();
        }
    };
    private View.OnClickListener buttonSaveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new SaveProductDetails().execute();

        }
    };

    class DeleteProduct extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();    //To change body of overridden methods use File | Settings | File Templates.
            progressDialog = new ProgressDialog(EditProductActivity.this);
            progressDialog.setMessage("Deleting Product...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair(Product.PRODUCT_ID, String.valueOf(productId)));
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            if (jsonHttpClient.Delete(ServiceUrl.PRODUCT, args)) {
                Intent intent = getIntent();
                setResult(ResultCode.PRODUCT_DELETE_SUCCESS);
                finish();
            }
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    class SaveProductDetails extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();    //To change body of overridden methods use File | Settings | File Templates.
            progressDialog = new ProgressDialog(EditProductActivity.this);
            progressDialog.setMessage("Saving product...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            Product product = new Product();
            product.setId(productId);
            product.setName(editTextName.getText().toString());
            product.setDescription(editTextDescription.getText().toString());
            product.setPrice(Double.parseDouble(editTextPrice.getText().toString()));
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            product = (Product) jsonHttpClient.PostObject(ServiceUrl.PRODUCT, product, Product.class);
            Intent intent = getIntent();
            setResult(ResultCode.PRODUCT_UPDATE_SUCCESS, intent);
            finish();
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    class GetProductDetails extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();    //To change body of overridden methods use File | Settings | File Templates.
            progressDialog = new ProgressDialog(EditProductActivity.this);
            progressDialog.setMessage("Loading product details. Please wait...");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> args = new ArrayList<NameValuePair>();
            args.add(new BasicNameValuePair(Product.PRODUCT_ID, String.valueOf(productId)));
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            final Product product = jsonHttpClient.Get(ServiceUrl.PRODUCT, args, Product.class);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (product != null) {
                        editTextName.setText(product.getName());
                        editTextPrice.setText(String.valueOf(product.getPrice()));
                        editTextDescription.setText(product.getDescription());
                    }
                }
            });
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
        }
    }
}
