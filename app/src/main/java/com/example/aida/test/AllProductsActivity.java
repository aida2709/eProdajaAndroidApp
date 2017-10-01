package com.example.aida.test;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.aida.test.model.JSONHttpClient;
import com.example.aida.test.model.Product;
import com.example.aida.test.model.RequestCode;
import com.example.aida.test.model.ResultCode;
import com.example.aida.test.model.ServiceUrl;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllProductsActivity extends AppCompatActivity {
    ArrayList<HashMap<String, String>> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);

        initializeComponents();

        productList = new ArrayList<HashMap<String, String>>();
        new LoadAllProducts().execute();
    }

    private void initializeComponents() {
        ListView listView = (ListView) findViewById(R.id.ListaProizvodi);
        listView.setOnItemClickListener(listViewItemClickListener);
    }

    private AdapterView.OnItemClickListener listViewItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int productId = Integer.parseInt(((TextView) view.findViewById(R.id.textViewId)).getText().toString());
            Intent intent = new Intent(getApplicationContext(), EditProductActivity.class);
            intent.putExtra(Product.PRODUCT_ID, productId);
            startActivityForResult(intent, RequestCode.PRODUCT_DETAILS);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);    //To change body of overridden methods use File | Settings | File Templates.
        if (resultCode == ResultCode.PRODUCT_UPDATE_SUCCESS || resultCode == ResultCode.PRODUCT_DELETE_SUCCESS) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    class LoadAllProducts extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            Product[] products = jsonHttpClient.Get(ServiceUrl.PRODUCT, nameValuePairs, Product[].class);
            if (products.length > 0) {

                for (Product product : products) {
                    HashMap<String, String> mapProduct = new HashMap<String, String>();
                    mapProduct.put(Product.PRODUCT_ID, String.valueOf(product.getId()));
                    mapProduct.put(Product.PRODUCT_NAME, product.getName());
                    productList.add(mapProduct);
                }

            } else {
                Intent intent = new Intent(getApplicationContext(), NewProductActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();    //To change body of overridden methods use File | Settings | File Templates.
            progressDialog = new ProgressDialog(AllProductsActivity.this);
            progressDialog.setMessage("Loading products. Please wait...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void setListAdapter(ListAdapter listAdapter) {

                }

                @Override
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(AllProductsActivity.this, productList, R.layout.list_item, new String[]{"Id", "Name"}, new int[]{R.id.textViewId, R.id.textViewName});
                    setListAdapter(adapter);
                }


            });
        }
    }
}
