package com.example.gamie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.gamie.R;
import com.example.gamie.api.IGDBGame;
import com.example.gamie.api.IGDBScreenshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GamesGridAdapter extends BaseAdapter {
    private Context context;
    private List<IGDBGame> games;

    public GamesGridAdapter(Context context, List<IGDBGame> games) {
        this.context = context;
        this.games = games;
    }

    @Override
    public int getCount() {
        return games.size();
    }

    @Override
    public Object getItem(int i) {
        return games.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.games_grid_game_item, null);
        }

        IGDBGame game = (IGDBGame) getItem(i);

        CardView cw = view.findViewById(R.id.gridCard);
        ImageView iw = view.findViewById(R.id.gridCardImage);
        TextView tw = view.findViewById(R.id.gridCardTextView);

        if (game.coverArt != null) {
            Picasso.with(context).load(game.coverArt.getImageUrl(IGDBScreenshot.IGDBScreenshotSize.COVER_BIG)).into(iw);
        } else {
            iw.setImageResource(R.drawable.placeholder_image);
        }

        tw.setText(game.name);

        return view;
    }


}
