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

import com.parse.ParseUser;

/**
 * Created by navneeeth99 on 5/6/15.
 */
public class ReadMessageDialogFrag extends DialogFragment {

    /*public static ReadMessageDialogFrag newInstance(String from, String title, String message) {
        ReadMessageDialogFrag f = new ReadMessageDialogFrag();
        Bundle args = new Bundle();
        args.putString("sender", from);
        args.putString("title", title);
        args.putString("msg", message);
        f.setArguments(args);
        return f;
    }

    public ReadMessageDialogFrag(){}*/

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final LinearLayout mLL;
        LayoutInflater mLayoutInflater = getActivity().getLayoutInflater();
        mLL = (LinearLayout)mLayoutInflater.inflate(R.layout.fragment_fullmsgview, null);
        String mFrom = getArguments().getString("sender");
        String mTitle = getArguments().getString("title");
        String mContent = getArguments().getString("msg");

        ((TextView)mLL.findViewById(R.id.fromTV)).setText(mFrom);
        ((TextView)mLL.findViewById(R.id.titleTV)).setText(mTitle);
        ((TextView)mLL.findViewById(R.id.contentTV)).setText(mContent);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(mTitle)
                .setView(mLL)
                .setNegativeButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ReadMessageDialogFrag.this.getDialog().cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
