package com.example.aida.test.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aida.test.AllProductsActivity;
import com.example.aida.test.NewProductActivity;
import com.example.aida.test.R;
import com.example.aida.test.model.JSONHttpClient;
import com.example.aida.test.model.Product;
import com.example.aida.test.model.ServiceUrl;

import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SviProizvodiActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<HashMap<String,String>> proizvodiLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svi_proizvodi);

        listView=(ListView) findViewById(R.id.ProizvodiListView);
        proizvodiLista=new ArrayList<HashMap<String, String>>();

        new UcitajProizvode().execute();

    }

    class UcitajProizvode extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SviProizvodiActivity.this);
            progressDialog.setMessage("Ucitavanje proizvoda. Molimo sacekajte...");
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            JSONHttpClient jsonHttpClient = new JSONHttpClient();
            Product[] proizvodi = jsonHttpClient.Get(ServiceUrl.PRODUCT, nameValuePairs, Product[].class);

            if (proizvodi.length > 0) {

                for (Product product : proizvodi) {
                    HashMap<String, String> mapProduct = new HashMap<String, String>();
                    mapProduct.put(Product.PRODUCT_ID, String.valueOf(product.getId()));
                    mapProduct.put(Product.PRODUCT_NAME, product.getName());
                    mapProduct.put(Product.PRODUCT_PRICE, String.valueOf(product.getPrice()));
                    proizvodiLista.add(mapProduct);
                }

            } else {
                Intent intent = new Intent(getApplicationContext(), NoviProizvodActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            listView.setAdapter(new BaseAdapter()
            {
                @Override
                public int getCount()
                {
                    return proizvodiLista.size();
                }

                @Override
                public Object getItem(int position)
                {
                    return null;
                }

                @Override
                public long getItemId(int position)
                {
                    return 0;
                }

                @Override
                public View getView(int position, View view, ViewGroup parent)
                {
                    progressDialog.dismiss();
                    HashMap<String, String> x = proizvodiLista.get(position);

                    if( view == null)
                    {
                        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        view = inflater.inflate(R.layout.stavka_proizvod, parent, false);
                    }

                    TextView tvProductID = (TextView) view.findViewById(R.id.tvProductID);
                    TextView tvName = (TextView) view.findViewById(R.id.tvName);
                    TextView tvPrice = (TextView) view.findViewById(R.id.tvPrice);

                    tvPrice.setText(x.get(Product.PRODUCT_ID));
                    tvName.setText(x.get(Product.PRODUCT_NAME));
                    tvPrice.setText(x.get(Product.PRODUCT_PRICE));

                    return view;
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    HashMap<String,String> x = proizvodiLista.get(position);
                    int Id=Integer.parseInt(x.get(Product.PRODUCT_ID));

                    Intent i = new Intent(SviProizvodiActivity.this, UrediProizvodActivity.class);
                    i.putExtra(Product.PRODUCT_ID, Id);

                    startActivity(i);
                }
            });


        }
    }
}
