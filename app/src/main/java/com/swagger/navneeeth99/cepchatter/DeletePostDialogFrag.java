package com.swagger.navneeeth99.cepchatter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

/**
 * Created by navneeeth99 on 9/6/15.
 */
public class DeletePostDialogFrag extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you wish to delete " + getArguments().getString("postTitle") + "?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ParseQuery<PostIt> query = new ParseQuery<>("PostIt");
                        query.whereEqualTo("objectId", getArguments().getString("postID"));
                        query.getFirstInBackground(new GetCallback<PostIt>() {
                            @Override
                            public void done(PostIt postIt, ParseException e) {
                                try {
                                    postIt.delete();
                                    PostItFragment.postAdapter.notifyDataSetChanged();
                                    PostItFragment.postAdapter.loadObjects();
                                } catch (ParseException error) {
                                    Log.d("error", error.toString());
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DeletePostDialogFrag.this.getDialog().cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
