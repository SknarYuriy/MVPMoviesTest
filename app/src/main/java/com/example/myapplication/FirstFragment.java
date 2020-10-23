package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment implements RecyclerViewClickListener {

    private MovieAdapter movieAdapter;
    private List<Movie> list = new ArrayList();
    JSONObject json;
    private EndlessScrollEventListener endlessScrollEventListener;
    public MoviesPresenter presenter;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void showMovies(List<Movie> list) {
        movieAdapter.setData(list);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).showAppBar();

        presenter = ((MainActivity)getActivity()).presenter;
        presenter.attachFirstFragment(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView movieList = (RecyclerView) view.findViewById(R.id.list);
        movieList.setLayoutManager(layoutManager);
        movieAdapter = new MovieAdapter(getContext(), this);
        movieList.setAdapter(movieAdapter);
        presenter.loadMovies();
        endlessScrollEventListener = new EndlessScrollEventListener(layoutManager) {
            @Override
            public void onLoadMore(int pageNum, RecyclerView recyclerView) {
                SharedPreferences preferences = getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                String query = preferences.getString("SearchString", "");

                if (query.length() > 0)
                    presenter.loadSearchPage(pageNum, query);
                else
                    presenter.loadTrendingPage(pageNum);
            }
        };

        movieList.addOnScrollListener(endlessScrollEventListener);
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        presenter.setClickedPosition(position);
        NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_DetailFragment);
    }

}