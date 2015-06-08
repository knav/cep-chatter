package com.swagger.navneeeth99.cepchatter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by navneeeth99 on 18/5/15.
 */
public class FriendPickerDialogFrag extends DialogFragment{

    private static ArrayList<ParseUser> mNewFriendsList = new ArrayList<>();
    private static RelativeLayout mLoadingOverlay;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final LinearLayout mLL;
        LayoutInflater mLayoutInflater = getActivity().getLayoutInflater();
        mLL = (LinearLayout)mLayoutInflater.inflate(R.layout.fragment_userlist, null);

        final ListView mAllUserListView = (ListView)mLL.findViewById(R.id.userLV);
        final TextView mNoUsersTextView = (TextView)mLL.findViewById(R.id.userEmptyTV);
        mLoadingOverlay = (RelativeLayout)mLL.findViewById(R.id.users_progress);
        Log.d("test", String.valueOf(mAllUserListView.getVisibility()));
        mAllUserListView.setEmptyView(mNoUsersTextView);
        CustomFriendPickerAdapter mAdapter = new CustomFriendPickerAdapter(getActivity());
        mAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<ParseUser>() {
            @Override
            public void onLoading() {
                mLoadingOverlay.setVisibility(View.VISIBLE);
                mNoUsersTextView.setVisibility(View.GONE);
            }

            @Override
            public void onLoaded(List<ParseUser> parseUsers, Exception e) {
                mLoadingOverlay.setVisibility(View.GONE);
            }
        });
        mAllUserListView.setAdapter(mAdapter);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add friends")
                .setView(mLL)
                .setPositiveButton("Add Friends", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("frPicker", mNewFriendsList.toString());
                        mNewFriendsList = new ArrayList<>(new LinkedHashSet<>(mNewFriendsList));
                        for (ParseUser pNewFr:mNewFriendsList){
                            ParseUser.getCurrentUser().add("friends", pNewFr);
                        }
                        ParseUser.getCurrentUser().saveInBackground();
                        FriendListFragment.adapter.notifyDataSetChanged();
                        mNewFriendsList.clear();
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

    public static class CustomFriendPickerAdapter extends ParseQueryAdapter<ParseUser> {

        public CustomFriendPickerAdapter(Context context) {
            super(context, new ParseQueryAdapter.QueryFactory<ParseUser>() {
                public ParseQuery create() {
                    ArrayList<String> mExcludeFromList = new ArrayList<>();
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    mExcludeFromList.add(ParseUser.getCurrentUser().getUsername());
                    for(ParseUser pUser : FriendListFragment.mFriendsList){
                        mExcludeFromList.add(pUser.getUsername());
                    }
                    Log.d("test", mExcludeFromList.toString());
                    query.whereNotContainedIn("username", mExcludeFromList);
                    return query;
                }
            });
        }

        // Customize the layout by overriding getItemView
        @Override
        public View getItemView(ParseUser object, View v, ViewGroup parent) {
            if (v == null) {
                v = View.inflate(getContext(), R.layout.list_pickfrienduser, null);
            }

            Log.d("test", "set invisible");

            super.getItemView(object, v, parent);

            // Add the title view
            final TextView titleTextView = (TextView) v.findViewById(R.id.frusernameTV);
            titleTextView.setText(object.getString("username"));

            final ImageView userprofile = (ImageView)v.findViewById(R.id.frprofilepicIV);

            ParseFile fileObject = (ParseFile)object.get("photo");
            if (fileObject == null){
                (userprofile).setImageResource(R.drawable.emptydp);
            } else {
                fileObject.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, com.parse.ParseException e) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        userprofile.setImageBitmap(bmp);
                    }
                });
            }

            // Add person to arrayList when checkbox checked
            CheckBox mPickFriendCheck = (CheckBox)v.findViewById(R.id.frcheckBox);
            mPickFriendCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        ParseQuery<ParseUser> query = ParseUser.getQuery();
                        query.whereEqualTo("username", titleTextView.getText());
                        query.findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> parseUsers, com.parse.ParseException e) {
                                if (e == null) {
                                    // The query was successful.
                                    ParseUser mNewFriend = parseUsers.get(0);
                                    mNewFriendsList.add(mNewFriend);
                                } else {
                                    Log.d("frPicker", e.toString());
                                }
                            }
                        });
                    } else {
                        ParseQuery<ParseUser> query = ParseUser.getQuery();
                        query.whereEqualTo("username", titleTextView.getText());
                        query.findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> parseUsers, com.parse.ParseException e) {
                                if (e == null) {
                                    // The query was successful.
                                    ParseUser mNewFriend = parseUsers.get(0);
                                    mNewFriendsList.remove(mNewFriend);
                                } else {
                                    Log.d("frPicker", e.toString());
                                }
                            }
                        });
                    }
                }
            });

            return v;
        }

    }

}
