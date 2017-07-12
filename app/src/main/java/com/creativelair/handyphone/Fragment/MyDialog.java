package com.creativelair.handyphone.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.creativelair.handyphone.Helpers.Contacts;
import com.creativelair.handyphone.Helpers.Preference;
import com.creativelair.handyphone.R;
import com.creativelair.handyphone.Screens.EditContact;

import static com.creativelair.handyphone.R.id.image;
import static com.creativelair.handyphone.R.id.imageView;


public class MyDialog extends DialogFragment implements View.OnClickListener {

    String color;
    private LayoutInflater inflater;
    private View view;
    private ImageView iv;
    private ImageButton call, msg;
    private TextView name, phone, initial;
    private Contacts contacts;
    private ImageButton edit;
    private Preference preference;
    private boolean i;
    
    @SuppressLint("ValidFragment")
    public MyDialog(Contacts contacts, String color) {
        super();
        this.contacts = contacts;
        this.color = color;
    }

    public MyDialog() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.popup_dialog , null);
        preference = new Preference(getActivity());

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        iv = (ImageView) view.findViewById(R.id.image);
        call = (ImageButton) view.findViewById(R.id.call);
        msg = (ImageButton) view.findViewById(R.id.msg);
        name = (TextView) view.findViewById(R.id.name);
        phone = (TextView) view.findViewById(R.id.phone);
        edit = (ImageButton) view.findViewById(R.id.edit);
        initial = (TextView) view.findViewById(R.id.initial);

        name.setText(contacts.getName());
        phone.setText(contacts.getNumber());

        if(contacts.getPic()!=null){
            iv.setImageBitmap(contacts.getPic());
            initial.setText("");
            i = true;
        }else if (contacts.getIcon()!=null) {
                if(!contacts.getIcon().equals("")){
                    Glide.with(this).load(contacts.getIcon()).into(iv);
                    initial.setText("");
                    i = true;
                }else {
                    initial.setText("" + contacts.getName().toUpperCase().charAt(0));
                    iv.setImageDrawable(new ColorDrawable(Color.parseColor(contacts.getColor())));
                    i = false;
                }
            } else {
                initial.setText("" + contacts.getName().toUpperCase().charAt(0));
                iv.setImageDrawable(new ColorDrawable(Color.parseColor(contacts.getColor())));
                i = false;
            }
        call.setOnClickListener(this);
        msg.setOnClickListener(this);
        edit.setOnClickListener(this);

        ViewGroup.LayoutParams params = iv.getLayoutParams();
        params.height = (height/3) * 2 - 100;
        iv.setLayoutParams(params);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onResume() {

        getDialog().setCanceledOnTouchOutside(true);
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();

        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.call:

                preference.setCallNumber(contacts.getNumber());

                if(isPermissionGranted()){
                    call_action();
                }

                break;
            case R.id.edit:

                preference.setName(contacts.getName());
                preference.setPhone(contacts.getNumber());
                preference.setGroup(contacts.getGroup());

                if(i) {
                    BitmapDrawable drawable = (BitmapDrawable) iv.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    preference.setBitmap(bitmap);
                } else {
                    preference.setBitmap(null);
                }

                preference.setId(contacts.getId());

                Intent i = new Intent(getActivity(), EditContact.class);
                startActivity(i);
                break;

            case R.id.msg:

                MessageDialog myDialog = new MessageDialog(contacts);
                myDialog.show(getActivity().getFragmentManager(), "my_dialog");

                break;
        }
    }

    public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    private void call_action() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + contacts.getNumber()));
        startActivity(intent);
    }
}
