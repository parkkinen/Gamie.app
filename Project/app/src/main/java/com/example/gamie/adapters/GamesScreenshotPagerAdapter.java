package com.example.gamie.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.example.gamie.R;
import com.example.gamie.SingleGameActivity;
import com.example.gamie.api.IGDBGame;
import com.example.gamie.api.IGDBScreenshot;
import com.example.gamie.preferences.UserPreferences;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GamesScreenshotPagerAdapter extends PagerAdapter {

    private Context context;
    List<IGDBScreenshot> screenshots;

    public GamesScreenshotPagerAdapter(Context context, List<IGDBScreenshot> screenshots) {
        this.context = context;
        this.screenshots = screenshots;
    }

    @Override
    public int getCount() {
        return screenshots.size();
    }

    @Override
    @NonNull
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.game_screenshot_item, null);
        ImageView imageView = view.findViewById(R.id.screenshotImage);
        IGDBScreenshot screenshot = screenshots.get(position);
        if (screenshot.url != null) {
            Picasso.with(context).load(screenshot.getImageUrl(IGDBScreenshot.IGDBScreenshotSize.FHD)).into(imageView);
        } else {
            imageView.setImageResource(R.drawable.placeholder_image);
        }

        container.addView(view);
        return view;
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
