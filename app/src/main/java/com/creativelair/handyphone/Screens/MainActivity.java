package com.creativelair.handyphone.Screens;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.creativelair.handyphone.Adapters.SectionsPagerAdapter;
import com.creativelair.handyphone.EmergencyContact;
import com.creativelair.handyphone.Fragment.Frequent;
import com.creativelair.handyphone.Helpers.Preference;
import com.creativelair.handyphone.R;
import com.creativelair.handyphone.Search;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private Preference pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = new Preference(this);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search) {
            Intent i = new Intent(this, Search.class);
            startActivity(i);
        }

        if (id == R.id.emergency) {
            if(pref.getEmergency()){
                if(pref.getCALL() || pref.getCALLMSG()){
                    if(isPermissionGranted()) {
                        call();
                    }
                }

                if(pref.getCALLMSG() || pref.getMSG()){
                    msg();
                }
            } else {
                Intent i = new Intent(this, EmergencyContact.class);
                startActivity(i);
            }
        }

        if(id==R.id.setting){
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
        }

        if(id==R.id.about){
            Intent intent2 = new Intent(this, About.class);
            startActivity(intent2);
        }



        return super.onOptionsItemSelected(item);
    }


    public void call(){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + pref.getEnumber()));
        startActivity(intent);
    }

    protected void msg() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        2);
            }
        } else {
            sendMessage();
        }
    }

    public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted.", Toast.LENGTH_SHORT).show();
                    call();
                } else {
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                }
            }

            case 2: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        sendMessage();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }

   /*         case 100 : {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    Frequent.LoadContactsAyscn loadContactsAyscn = new Frequent.LoadContactsAyscn();
                    loadContactsAyscn.execute();
                } else {
                    Toast.makeText(this, "Until you grant the permission, we can't display the names", Toast.LENGTH_SHORT).show();
                }
            }
     */   }
    }

    public void sendMessage(){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(pref.getEnumber(), null, pref.getMSGTEXT(), null, null);
        Toast.makeText(getApplicationContext(), "SMS sent.",
                Toast.LENGTH_LONG).show();
    }


}