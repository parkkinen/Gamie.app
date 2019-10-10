package com.example.gamie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.example.gamie.R;
import com.example.gamie.api.IGDBGame;
import com.example.gamie.api.IGDBScreenshot;
import com.example.gamie.preferences.UserPreferences;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GamesPagerAdapter extends PagerAdapter {

    private Context context;
    private List<IGDBGame> games;
    private List<Integer> preferredGames;

    public GamesPagerAdapter(Context context, List<IGDBGame> games) {
        this.context = context;
        this.games = games;
    }

    @Override
    public int getCount() {
        return games.size();
    }

    @Override
    @NonNull
    public Object instantiateItem(ViewGroup container, int position) {
        this.preferredGames = UserPreferences.getUserGamePrefences();
        View view = LayoutInflater.from(context).inflate(R.layout.games_grid_item, null);
        ImageView imageView = view.findViewById(R.id.gridCardImage);
        TextView gameTitle = view.findViewById(R.id.gridCardTextView);
        IGDBGame game = (IGDBGame) this.games.get(position);
        imageView.getLayoutParams().height = 0;
        ImageButton starBtn = view.findViewById(R.id.gridCardStar);

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
            }
        });
        if (game.coverArt != null) {
            Picasso.with(context).load(game.coverArt.getImageUrl(IGDBScreenshot.IGDBScreenshotSize.HD)).into(imageView);
        } else {
            imageView.setImageResource(R.drawable.placeholder_image);
        }
        gameTitle.setText(game.name);

        container.addView(view);
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

    @Override
    public int getItemPosition(Object o) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object == view;
    }
}
