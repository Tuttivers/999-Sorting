package eu.tuttivers.sort999;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Patterns;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class MainActivity extends AppCompatActivity implements AppRecyclerViewAdapter.ItemClickListener {

    @BindView(R.id.seacrh)
    SearchView searchView;

    @BindView(R.id.listView)
    RecyclerView listView;

    private ProgressDialog progressDialog;

    private List<Item> items;

    private boolean isResumed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Uri uri = Uri.parse(query);
                if (uri == null || !Patterns.WEB_URL.matcher(uri.toString()).matches()) {
                    AppDialog.showWarning(MainActivity.this, getString(R.string.invalid_url_address));
                    return true;
                }
                processUri(uri);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        listView.setLayoutManager(new LinearLayoutManager(this));

        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        Uri intentData = intent.getData();
        if (intentData != null) {
            searchView.setQuery(intentData.toString(), true);
        }
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(intent.getAction()) && type != null) {
            if ("text/plain".equals(type)) {
                searchView.setQuery(intent.getStringExtra(Intent.EXTRA_TEXT), true);
            }
        }
    }

    @Override
    public void onItemClick(Item item) {
        String url = Uri999Helper.BASE_999_URL + item.link;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String package999 = getString(R.string.app_package_999);
        if (getPackageManager().getLaunchIntentForPackage(package999) != null) {
            intent.setPackage(package999);
        }
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void processUri(Uri uri) {
        showProgressDialog(getString(R.string.loading));
        new AsyncJsoupParser() {

            @Override
            protected void onProgressUpdate(String... message) {
                super.onProgressUpdate(message);
                if (message[0].contains(getString(R.string.error))) {
                    hideProgressDialog();
                    AppDialog.showWarning(MainActivity.this, message[0].replace("Error: ", ""));
                } else {
                    showProgressDialog(message[0]);
                }
            }

            @Override
            protected void onPostExecute(List<Item> items) {
                super.onPostExecute(items);
                if (items != null && !items.isEmpty()) {
                    showProgressDialog(getString(R.string.sorting));
                    MainActivity.this.items = StreamSupport.stream(items)
                            .sorted((o1, o2) -> Long.compare(o1.getLongPrice(), o2.getLongPrice())).collect(Collectors.toList());
                    if (isResumed) {
                        listView.setAdapter(new AppRecyclerViewAdapter(MainActivity.this, MainActivity.this.items));
                    }
                }
                hideProgressDialog();
            }
        }.execute(uri);
    }

    private void showProgressDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
        }
        progressDialog.setMessage(message);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResumed = true;
        if (items != null) {
            listView.setAdapter(new AppRecyclerViewAdapter(this, items));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResumed = false;
    }
}
