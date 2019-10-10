package com.example.gamie.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.gamie.R;
import com.example.gamie.SingleGameActivity;
import com.example.gamie.activities.GridGamesActivity;
import com.example.gamie.api.IGDBGame;
import com.example.gamie.api.IGDBScreenshot;
import com.example.gamie.preferences.UserPreferences;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GamesGridAdapter extends BaseAdapter {
    private Context context;
    private List<IGDBGame> games;
    private List<Integer> preferredGames;

    public interface OnInteraction {
        void starClicked(int itemPos);
    }

    private OnInteraction onInteraction;

    public void setOnInteraction(OnInteraction onInteraction) {
        this.onInteraction = onInteraction;
    }


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
            view = LayoutInflater.from(context).inflate(R.layout.games_grid_item, null);
        }
        this.preferredGames = UserPreferences.getUserGamePrefences();

        IGDBGame game = (IGDBGame) getItem(i);
        CardView cw = view.findViewById(R.id.gridCard);
        ImageView iw = view.findViewById(R.id.gridCardImage);
        TextView tw = view.findViewById(R.id.gridCardTextView);
        ImageButton starBtn = view.findViewById(R.id.gridCardStar);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent singleGameIntent = new Intent(context, SingleGameActivity.class);
                singleGameIntent.putExtra(SingleGameActivity.SINGLE_GAME_EXTRA, game);
                singleGameIntent.putExtra(SingleGameActivity.PARENT_CLASS_EXTRA, context.getClass().getName());
                context.startActivity(singleGameIntent);
            }
        });

        updateStarButton(starBtn, game.id);

        starBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preferredGames.contains(game.id)) {
                    UserPreferences.removeUserGamePreferences(game.id);
                }
                else {
                    UserPreferences.appendUserGamePreferences(game.id);
                }
                preferredGames = UserPreferences.getUserGamePrefences();
                updateStarButton(starBtn, game.id);
                if (onInteraction != null) {
                    onInteraction.starClicked(i);
                }
            }
        });
        if (game.coverArt != null) {
            Picasso.with(context).load(game.coverArt.getImageUrl(IGDBScreenshot.IGDBScreenshotSize.COVER_BIG)).into(iw);
        } else {
            iw.setImageResource(R.drawable.placeholder_image);
        }

        tw.setText(game.name);

        return view;
    }

    private void updateStarButton(ImageButton btn, int id) {
        if (preferredGames.contains(id)) {
            btn.setImageDrawable(ContextCompat.getDrawable(context, android.R.drawable.btn_star_big_on));
        }
        else {
            btn.setImageDrawable(ContextCompat.getDrawable(context, android.R.drawable.btn_star_big_off));
        }
    }
}
