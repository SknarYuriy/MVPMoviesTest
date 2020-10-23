package com.example.myapplication;

import org.json.JSONObject;

import java.util.List;

public class MoviesPresenter {

    private MainActivity view;
    FirstFragment listFragment;
    DetailFragment detailFragment;
    private final MoviesModel model;

    public int getClickedPosition() {
        return clickedPosition;
    }

    public void setClickedPosition(int clickedPosition) {
        this.clickedPosition = clickedPosition;
    }

    int clickedPosition;

    public MoviesPresenter(MoviesModel model) {
        this.model = model;
    }

    public void attachView(MainActivity mainActivity) {
        view = mainActivity;
    }
    public void detachView() {
        view = null;
    }
    public void attachFirstFragment(FirstFragment firstFragment) {
        listFragment = firstFragment;
    }
    public void detachFirstFragment() {
        listFragment = null;
    }
    public void attachDetailFragment(DetailFragment detailFragment) {
        this.detailFragment = detailFragment;
    }
    public void detachDetailFragment() {
        detailFragment = null;
        clickedPosition = 0;
    }

    public void viewIsReady() {
        loadMovies();
    }

    public void loadMovies() {
        model.loadTrendingMovies(new MoviesModel.LoadMoviesCallback() {
            @Override
            public void preLoad() {
                view.showProgress();
            }

            @Override
            public void onLoad(List<Movie> list) {
                view.hideProgress();
                listFragment.showMovies(list);
            }
        }) ;
    }

    public void loadTrendingPage(int pageNumber)
    {
        model.loadTrendingMoviesPage(new MoviesModel.LoadMoviesCallback() {
            @Override
            public void preLoad() {
                view.showProgress();
            }

            @Override
            public void onLoad(List<Movie> list) {
                view.hideProgress();
                listFragment.showMovies(list);
            }
        }, pageNumber); ;
    }

    public void loadSearchPage(int pageNumber, String query) {
        model.loadSearchMoviesPage(new MoviesModel.LoadSearchMoviesCallback() {
            @Override
            public void preLoad() {
                view.showProgress();
            }

            @Override
            public void onLoad(List<Movie> list) {
                view.hideProgress();
                listFragment.showMovies(list);
            }
        }, pageNumber, query);
    }

    public void loadDetail()
    {
        model.loadMovieDetail(new MoviesModel.LoadDetailCallback() {
            @Override
            public void preLoad() {
                view.showProgress();
            }

            @Override
            public void onLoad(JSONObject detail) {
                view.hideProgress();
                detailFragment.loadInformation(detail);
            }
        }, clickedPosition);
    }

}
