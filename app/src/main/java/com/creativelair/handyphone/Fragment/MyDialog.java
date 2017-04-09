package com.creativelair.handyphone.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.creativelair.handyphone.Helpers.Contacts;
import com.creativelair.handyphone.Helpers.Preference;
import com.creativelair.handyphone.R;
import com.creativelair.handyphone.Screens.EditContact;

/**
 * Created by AHSAN on 4/3/2017.
 */

public class MyDialog extends DialogFragment implements View.OnClickListener {

    private LayoutInflater inflater;
    private View view;
    private ImageView iv;
    private ImageButton call, msg;
    private TextView name, phone, initial;
    private Contacts contacts;
    private ImageButton edit;
    private Preference preference;

    public MyDialog(Contacts contacts){
        super();
        this.contacts = contacts;
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

        if (contacts.getIcon() != null) {
            iv.setImageBitmap(contacts.getIcon());
            initial.setText("");
        } else {
            initial.setText("" + contacts.getName().toUpperCase().charAt(0));
            iv.setImageDrawable(new ColorDrawable(Color.parseColor("#303F9F")));
        }


        call.setOnClickListener(this);
        msg.setOnClickListener(this);
        edit.setOnClickListener(this);


        ViewGroup.LayoutParams params = iv.getLayoutParams();
        params.height = (height/3) * 2 - 100;
        iv.setLayoutParams(params);

    /*    int IVheight = iv.getHeight();

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0 , IVheight/2, 0, 0);
        initial.setLayoutParams(lp);

*/
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

                if(isPermissionGranted()){
                    call_action();
                }

                break;
            case R.id.edit:

                preference.setName(contacts.getName());
                preference.setPhone(contacts.getNumber());
                preference.setGroup(contacts.getGroup());
                preference.setPic(contacts.getIcon());
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
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
                    call_action();
                } else {
                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void call_action() {

        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + contacts.getNumber()));
        startActivity(intent);

    }

}
