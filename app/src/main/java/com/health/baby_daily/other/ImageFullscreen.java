package com.health.baby_daily.other;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.health.baby_daily.R;
import com.squareup.picasso.Picasso;

public class ImageFullscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_fullscreen);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        getSupportActionBar().setDisplayShowTitleEnabled(false); // remove title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back navigation

        FrameLayout exitFrame = (FrameLayout) findViewById(R.id.exitFrame);
        FrameLayout loadingFrame = (FrameLayout) findViewById(R.id.loadingFrame);
        ImageView imageFullscreen = (ImageView) findViewById(R.id.imageFullscreen);

        // get url from intent, and set to imageview if not null
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            String image_url = extras.getString("image_url");

            Picasso.get().load(image_url).into(imageFullscreen);

            loadingFrame.setVisibility(View.GONE);
        }

        exitFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    // override default back navigation action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // close
            case android.R.id.home:
                // todo: goto back activity from here

                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
