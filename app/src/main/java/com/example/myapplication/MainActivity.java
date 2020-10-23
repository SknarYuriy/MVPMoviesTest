package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private AppBarLayout appBarLayout;
    public MoviesPresenter presenter;
    ProgressDialog progressDialog;
    SearchView searchView;

    public void showProgress() {
        progressDialog = ProgressDialog.show(this, "", "Please wait...");
    }

    public void hideProgress() {
        if (progressDialog != null)
            if (progressDialog.isShowing())
                progressDialog.dismiss();
    }

    public void showAppBar()
    {
        appBarLayout.setVisibility(View.VISIBLE);
    }

    public void hideAppBar()
    {
        appBarLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appBarLayout = findViewById(R.id.appbar);
        searchView = findViewById(R.id.searchView);
        MoviesModel model = new MoviesModel();
        presenter = new MoviesPresenter(model);
        presenter.attachView(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SharedPreferences preferences = getApplication().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("SearchString", query);
                editor.commit();
                Toast.makeText(MainActivity.this, "onQueryTextSubmit"  + "\"" + query + "\"", Toast.LENGTH_LONG).show();
                presenter.loadSearchPage(0, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                SharedPreferences preferences = getApplication().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("SearchString", newText);
                editor.commit();
                Toast.makeText(MainActivity.this, "onQueryTextChange" + "\"" + newText + "\"", Toast.LENGTH_LONG).show();
                if (!searchView.isIconified() && newText.equalsIgnoreCase("")) {
                    searchView.setIconified(true);
                    searchView.onActionViewCollapsed();
                    searchView.clearFocus();
                    presenter.loadMovies();
                }
                return false;
            }

        });

    }
}