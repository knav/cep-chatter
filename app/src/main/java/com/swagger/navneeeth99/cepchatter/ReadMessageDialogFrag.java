package com.swagger.navneeeth99.cepchatter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by navneeeth99 on 5/6/15.
 */
public class ReadMessageDialogFrag extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final LinearLayout mLL;
        LayoutInflater mLayoutInflater = getActivity().getLayoutInflater();
        mLL = (LinearLayout)mLayoutInflater.inflate(R.layout.fragment_fullmsgview, null);
        final String mFrom = getArguments().getString("sender");
        final String mTitle = getArguments().getString("title");
        final String mContent = getArguments().getString("msg");

        ((TextView)mLL.findViewById(R.id.fromTV)).setText(mFrom);
        ((TextView)mLL.findViewById(R.id.titleTV)).setText(mTitle);
        ((TextView)mLL.findViewById(R.id.contentTV)).setText(mContent);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Full Message")
                .setView(mLL)
                .setNegativeButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ParseQuery<PMessage> query = new ParseQuery<>("PMessage");
                        query.whereEqualTo("from", mFrom);
                        query.whereEqualTo("title", mTitle);
                        query.whereEqualTo("content", mContent);
                        query.getFirstInBackground(new GetCallback<PMessage>() {
                            @Override
                            public void done(PMessage pMessage, ParseException e) {
                                pMessage.setmRead("true");
                            }
                        });
                        ReadMessageDialogFrag.this.getDialog().cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
