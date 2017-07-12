package com.creativelair.handyphone.Screens;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import com.creativelair.handyphone.R;

public class About extends AppCompatActivity {

    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_about);

        actionBar = getSupportActionBar();
        actionBar.setTitle("About");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        TextView textView =(TextView)findViewById(R.id.fblink);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='https://www.facebook.com/creativelair'>Give us a like!</a>";
        textView.setText(Html.fromHtml(text));

        TextView textView2 =(TextView)findViewById(R.id.instaLink);
        textView2.setClickable(true);
        textView2.setMovementMethod(LinkMovementMethod.getInstance());
        String text2 = "<a href='https://www.instagram.com/the.creative.lair'>Follow us on Instagram!</a>";
        textView2.setText(Html.fromHtml(text2));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
