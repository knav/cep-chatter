package com.swagger.navneeeth99.cepchatter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

/**
 * Created by navneeeth99 on 18/5/15.
 */
public class FriendPickerDialogFrag extends DialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //if (getArguments().containsKey(HomeworkListFragment.DEL_OBJ_NAME)) {
            //mDeleteObjName = getArguments().getString(HomeworkListFragment.DEL_OBJ_NAME);
        //}

        final LinearLayout mLL;
        LayoutInflater mLayoutInflater = getActivity().getLayoutInflater();
        mLL = (LinearLayout)mLayoutInflater.inflate(R.layout.fragment_userlist, null);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add friends")
                .setView(mLL)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FriendPickerDialogFrag.this.getDialog().cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
