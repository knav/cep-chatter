package com.swagger.navneeeth99.cepchatter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import java.text.ParseException;

/**
 * Created by Benjamin on 3/6/15.
 */
public class ResetPasswordDialogFragment extends DialogFragment{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final LinearLayout mLL;
        LayoutInflater mLayoutInflater = getActivity().getLayoutInflater();
        mLL = (LinearLayout)mLayoutInflater.inflate(R.layout.fragment_reset_password, null);
        final EditText mResetEmailET = (EditText)mLL.findViewById(R.id.resetEmailET);


        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Reset Your Password")
                .setView(mLL)
                .setPositiveButton("Send Email", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String mResetEmail = mResetEmailET.getText().toString();
                        ParseUser.requestPasswordResetInBackground(mResetEmail, new RequestPasswordResetCallback() {
                            @Override
                            public void done(com.parse.ParseException e) {
                                if (e == null) {
                                    // An email was successfully sent with reset instructions.
//                                    Toast.makeText(getActivity(), "Email Sent!", Toast.LENGTH_LONG).show();
                                } else {
                                    // Something went wrong. Look at the ParseException to see what's up.
//                                    Toast.makeText(getActivity(), "Error"+e.toString(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ResetPasswordDialogFragment.this.getDialog().cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }



}
