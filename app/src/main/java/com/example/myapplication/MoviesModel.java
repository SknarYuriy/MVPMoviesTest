package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

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
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MoviesModel {
    private List<Movie> list = new ArrayList();

    public void loadTrendingMovies(LoadMoviesCallback callback) {
            list = new ArrayList<>();
        LoadMoviesTask loadMoviesTask = new LoadMoviesTask(callback);
        loadMoviesTask.execute("https://api.themoviedb.org/3/trending/movie/week?api_key=50b9d8ea89914f28c2d20f4c8514868d");
    }
    public void loadTrendingMoviesPage(LoadMoviesCallback callback, int page) {
        LoadMoviesTask loadMoviesTask = new LoadMoviesTask(callback);
        loadMoviesTask.execute(String.format("https://api.themoviedb.org/3/trending/movie/week?api_key=50b9d8ea89914f28c2d20f4c8514868d&page=%d", page+1));
    }

    public void loadSearchMoviesPage(LoadSearchMoviesCallback callback, int page, String query) {
        if (page == 0)
            list = new ArrayList<>();
        LoadSearchMoviesTask loadSeacrhMoviesTask = new LoadSearchMoviesTask(callback);
        loadSeacrhMoviesTask.execute(String.format("https://api.themoviedb.org/3/search/movie?api_key=50b9d8ea89914f28c2d20f4c8514868d&language=en-US&query=%s&page=%d&include_adult=false", query, page + 1));
    }

    public void loadMovieDetail(LoadDetailCallback callback, int position) {
        LoadMovieDetailTask loadDetailsTask = new LoadMovieDetailTask(callback);
        loadDetailsTask.execute(String.format("https://api.themoviedb.org/3/movie/%d?api_key=50b9d8ea89914f28c2d20f4c8514868d&language=en-US&append_to_response=credits", list.get(position).getId()));
    }

    interface LoadSearchMoviesCallback {
        void preLoad();
        void onLoad(List<Movie> list);
    }

    interface LoadMoviesCallback {
        void preLoad();
        void onLoad(List<Movie> list);
    }

    interface LoadDetailCallback {
        void preLoad();
        void onLoad(JSONObject detail);
    }

    interface CompleteCallback {
        void onComplete();
    }

    class LoadMoviesTask extends AsyncTask<String, String, String> {

        private final LoadMoviesCallback callback;

        LoadMoviesTask(LoadMoviesCallback callback) {
            this.callback = callback;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            if (callback != null) {
                callback.preLoad();
            }
        }

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
                }
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            JSONObject json;
            try {
                json = new JSONObject(result);
                JSONArray jsonArray = json.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    list.add(new Movie(jsonArray.getJSONObject(i)));
                }
//                movieAdapter.setData(list);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("!!!", result);
            if (callback != null) {
                callback.onLoad(list);
            }
        }
    }

    class LoadSearchMoviesTask extends AsyncTask<String, String, String> {

        private final LoadSearchMoviesCallback callback;

        LoadSearchMoviesTask(LoadSearchMoviesCallback callback) {
            this.callback = callback;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            if (callback != null) {
                callback.preLoad();
            }
        }

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);
                }
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            JSONObject json;
            try {
                json = new JSONObject(result);
                JSONArray jsonArray = json.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    list.add(new Movie(jsonArray.getJSONObject(i)));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("!!!", result);
            if (callback != null) {
                callback.onLoad(list);
            }
        }
    }

    class LoadMovieDetailTask extends AsyncTask<String, String, String> {

        private final LoadDetailCallback callback;

        LoadMovieDetailTask(LoadDetailCallback callback) {
            this.callback = callback;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            if (callback != null) {
                callback.preLoad();
            }
        }

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
                }
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            JSONObject json = new JSONObject();
            try {
                json = new JSONObject(result);
//                JSONArray jsonArray = json.getJSONArray("results");
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    list.add(new Movie(jsonArray.getJSONObject(i)));
//                }
//                movieAdapter.setData(list);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("!!!", result);
            if (callback != null) {
                callback.onLoad(json);
            }
        }
    }

}
