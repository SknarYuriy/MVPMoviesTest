package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    private Context context;
    private static RecyclerViewClickListener itemListener;
    List<Movie> data = new ArrayList<>();

    public MovieAdapter(Context context, RecyclerViewClickListener itemListener) {
        this.context = context;
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Movie> movies) {
        data.clear();
        data.addAll(movies);
        notifyDataSetChanged();
        Log.d("qweee", "size  = " + getItemCount());
    }

    static class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txtTitle;
        TextView txtOverview;
        ImageView imgPoster;

        public MovieHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtOverview = (TextView) itemView.findViewById(R.id.txtOverview);
            imgPoster = (ImageView) itemView.findViewById(R.id.imgPoster);
            itemView.setOnClickListener(this);
        }

        void bind(Movie movie) {
            txtTitle.setText(movie.getTitle());
            txtOverview.setText(movie.getOverview());
            imgPoster.setImageResource(android.R.color.transparent);
            new DownloadImageTask((ImageView) imgPoster).execute(String.format("https://image.tmdb.org/t/p/w200%s",movie.getPosterPath()));
        }

        @Override
        public void onClick(View v) {
            itemListener.recyclerViewListClicked(v, this.getLayoutPosition());
        }
    }

    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if (bmImage != null)
                bmImage.setImageBitmap(result);
        }
    }
}
