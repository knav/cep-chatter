package com.swagger.navneeeth99.cepchatter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by navneeeth99 on 7/6/15.
 */
public class DeleteMessageDialogFrag extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you wish to delete " + getArguments().getString("msgTitle") + "?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ParseQuery<PMessage> query = new ParseQuery<>("PMessage");
                        query.whereEqualTo("objectId", getArguments().getString("msgID"));
                        query.getFirstInBackground(new GetCallback<PMessage>() {
                            @Override
                            public void done(PMessage pMessage, ParseException e) {
                                try {
                                    pMessage.delete();
                                    MessagesFragment.mReadMessagesAdapter.notifyDataSetChanged();
                                    MessagesFragment.mReadMessagesAdapter.loadObjects();
                                    MessagesFragment.mUnreadMessagesAdapter.notifyDataSetChanged();
                                    MessagesFragment.mUnreadMessagesAdapter.loadObjects();
                                } catch (ParseException error) {
                                    Log.d("error", error.toString());
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DeleteMessageDialogFrag.this.getDialog().cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
