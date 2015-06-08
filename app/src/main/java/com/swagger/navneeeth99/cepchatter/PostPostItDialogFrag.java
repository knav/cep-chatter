package com.swagger.navneeeth99.cepchatter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by navneeeth99 on 9/6/15.
 */
public class PostPostItDialogFrag extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final LinearLayout mLL;
        LayoutInflater mLayoutInflater = getActivity().getLayoutInflater();
        mLL = (LinearLayout)mLayoutInflater.inflate(R.layout.fragment_newpost, null);

        final EditText mPostHeadET = (EditText)mLL.findViewById(R.id.newHeadET);
        final EditText mPostDescET = (EditText)mLL.findViewById(R.id.newDescET);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Send Message")
                .setView(mLL)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String mMessageText = mPostDescET.getText().toString();
                        String mMessageTitle = mPostHeadET.getText().toString();
                        //Creating a new PostIt
                        PostIt mNewPI = new PostIt();
                        mNewPI.setmPoster(ParseUser.getCurrentUser().getUsername());
                        mNewPI.setmHeader(mMessageTitle);
                        mNewPI.setmDescription(mMessageText);
                        ArrayList<ParseUser> tempList = new ArrayList<>();
                        mNewPI.setmLikedBy(tempList);
                        mNewPI.saveInBackground();
                        PostItFragment.postAdapter.notifyDataSetChanged();
                        PostItFragment.postAdapter.loadObjects();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PostPostItDialogFrag.this.getDialog().cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
