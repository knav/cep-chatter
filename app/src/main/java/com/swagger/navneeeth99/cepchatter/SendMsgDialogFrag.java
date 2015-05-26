package com.swagger.navneeeth99.cepchatter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
* Created by Benjamin on 19/5/15.
*/
public class SendMsgDialogFrag extends DialogFragment {
    private String mFriendSelected;
    private String mMessageText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final LinearLayout mLL;
        LayoutInflater mLayoutInflater = getActivity().getLayoutInflater();
        mLL = (LinearLayout)mLayoutInflater.inflate(R.layout.fragment_send_message, null);


        final EditText mMessageET = (EditText)mLL.findViewById(R.id.newMsgET);
        final Spinner mFriendSpinner = (Spinner)mLL.findViewById(R.id.friendSpinner);

        //ParseQueryAdapter<ParseUser> adapter = new CustomUserAdapter(this.getActivity());
        List<ParseUser> mCurrFriendsList = ParseUser.getCurrentUser().getList("friends");
        ArrayList<ParseUser> mCurrFriendsArray = new ArrayList<>();
        for (ParseUser pUser : mCurrFriendsList){
            mCurrFriendsArray.add(pUser);
        }
        CustomFriendsDropdownAdapter adapter = new CustomFriendsDropdownAdapter(getActivity(), R.layout.list_customuser, mCurrFriendsArray);
        mFriendSpinner.setAdapter(adapter);

        mFriendSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMessageText = mMessageET.getText().toString();
                //mFriendSelected = parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Send Message To: ")
                .setView(mLL)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PMessage newMsg = new PMessage();
                        newMsg.setmSender(ParseUser.getCurrentUser().getUsername());
                        Log.d("Message", ParseUser.getCurrentUser().getUsername());
                        newMsg.setmReceiver(mFriendSelected);
                        Log.d("Message", mFriendSelected);
                        newMsg.setmTitle("Blank for now");
                        newMsg.setmContent(mMessageText);
                        Log.d("Message", mMessageText + " and i had typed: " + mMessageET.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SendMsgDialogFrag.this.getDialog().cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public static class CustomUserAdapter extends ParseQueryAdapter<ParseUser> {

        public CustomUserAdapter(Context context) {
            super(context, new ParseQueryAdapter.QueryFactory<ParseUser>() {
                public ParseQuery create() {
                    ParseQuery query = new ParseQuery(ParseUser.class);
                    return query;
                }
            });
        }

        // Customize the layout by overriding getItemView
        @Override
        public View getItemView(ParseUser object, View v, ViewGroup parent) {
            if (v == null) {
                v = View.inflate(getContext(), R.layout.list_customuser, null);
            }

            super.getItemView(object, v, parent);

            // Add the title view
            TextView titleTextView = (TextView) v.findViewById(R.id.usernameTV);
            titleTextView.setText(object.getString("username"));
            return v;
        }

    }

    public class CustomFriendsDropdownAdapter extends ArrayAdapter<ParseUser> {
        private int mResource;
        private ArrayList<ParseUser> mListFriends;

        public CustomFriendsDropdownAdapter(Context context, int resource, ArrayList<ParseUser> friends) {
            super(context, resource, friends);
            mListFriends = friends;
            mResource = resource;
        }

        @Override
        public View getView(int position, View row, ViewGroup parent) {
            if (row == null) {
                row = getActivity().getLayoutInflater().inflate(mResource, parent, false);
            }

            // Add the title view
            TextView titleTextView = (TextView)row.findViewById(R.id.usernameTV);
            titleTextView.setText(mListFriends.get(position).getUsername());
            return row;

        }
    }

}

