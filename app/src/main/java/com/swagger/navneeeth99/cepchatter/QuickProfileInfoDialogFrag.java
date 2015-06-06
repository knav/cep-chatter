package com.swagger.navneeeth99.cepchatter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by navneeeth99 on 6/6/15.
 */
public class QuickProfileInfoDialogFrag extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final RelativeLayout mLL;
        LayoutInflater mLayoutInflater = getActivity().getLayoutInflater();
        mLL = (RelativeLayout)mLayoutInflater.inflate(R.layout.fragment_quickprofview, null);

        final String pickedFriend = getArguments().getString("frndID");
        final ImageView mProfPicIV = (ImageView)mLL.findViewById(R.id.profPicIV);
        final TextView mUsernameTV = (TextView)mLL.findViewById(R.id.profNameTV);
        final TextView mStatusTV = (TextView)mLL.findViewById(R.id.profStatusTV);
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", pickedFriend);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                ParseFile fileObject = (ParseFile)parseUser.get("photo");
                if (fileObject == null){
                    mProfPicIV.setImageResource(R.drawable.emptydp);
                } else {
                    fileObject.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, ParseException e) {
                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            mProfPicIV.setImageBitmap(bmp);
                        }
                    });
                }
                mUsernameTV.setText(parseUser.getUsername());
                if (parseUser.get("status") == null){
                    mStatusTV.setText("~ NO STATUS ~");
                } else if (parseUser.get("status").equals("")){
                    mStatusTV.setText("~ NO STATUS ~");
                } else {
                    mStatusTV.setText(parseUser.get("status").toString());
                }
            }
        });

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Quick Friend View")
                .setView(mLL)
                .setPositiveButton("Message", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Send a message
                    }
                });

        //getDialog().setCanceledOnTouchOutside(true);
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(true);
        return getView();
    }

}
