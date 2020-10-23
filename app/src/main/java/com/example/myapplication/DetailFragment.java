package com.example.myapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailFragment extends Fragment {
    MoviesPresenter presenter;
    TextView txtTitle, txtOverview, txtOther;
    ImageView imgPoster;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    public void loadInformation(JSONObject jsonObject)
    {
        try {
            txtOverview.setText(jsonObject.getString("overview"));
            txtTitle.setText(jsonObject.getString("original_title"));
            JSONArray genres = jsonObject.getJSONArray("genres");
            if (genres.length() > 0)
            {
                txtOther.setText("Genres:\n");
                for (int i = 0; i < genres.length(); i++) {
                    txtOther.append(" - " + ((JSONObject)genres.get(i)).getString("name") + "\n");
                }
            }
            String date = jsonObject.getString("release_date");
            if (date != null)
            {
                txtOther.append("\nRelease Date:\n\t" + date);
            }
            JSONObject credits = jsonObject.getJSONObject("credits");
            JSONArray crew = credits.getJSONArray("crew");
            JSONArray cast = credits.getJSONArray("cast");
            if (crew != null && crew.length() > 0) {
                txtOther.append("\nCrew:\n");
                for (int i = 0; i < crew.length(); i++) {
                    txtOther.append(((JSONObject)crew.get(i)).getString("name") + " - " + ((JSONObject)crew.get(i)).getString("job") + "\n");
                }
            }
            if (cast != null && cast.length() > 0) {
                txtOther.append("\nCast:\n");
                for (int i = 0; i < cast.length(); i++) {
                    txtOther.append(((JSONObject)cast.get(i)).getString("name") + " - " + ((JSONObject)cast.get(i)).getString("character") + "\n");
                }
            }
            new MovieAdapter.DownloadImageTask((ImageView) imgPoster).
                    execute(String.format("https://image.tmdb.org/t/p/w200%s", jsonObject.getString("poster_path")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detachDetailFragment();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).hideAppBar();
        presenter = ((MainActivity) getActivity()).presenter;
        presenter.attachDetailFragment(this);
        presenter.loadDetail();
        imgPoster = (ImageView) view.findViewById(R.id.imgDetailPoster);
        txtTitle = (TextView) view.findViewById(R.id.txtDetailTitle);
        txtOverview = (TextView) view.findViewById(R.id.txtDetailOverview);
        txtOther = (TextView) view.findViewById(R.id.txtDetailOther);

    }
}
