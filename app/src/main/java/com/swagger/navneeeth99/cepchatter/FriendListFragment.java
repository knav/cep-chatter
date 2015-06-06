package com.swagger.navneeeth99.cepchatter;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
* Created by navneeeth99 on 17/5/15.
*/
public class FriendListFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static CustomFriendsAdapter adapter;
    public static ArrayList<ParseUser> mFriendsList = new ArrayList<>();

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FriendListFragment newInstance(int sectionNumber) {
        FriendListFragment fragment = new FriendListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FriendListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_friendslist, container, false);
        final ListView listView = (ListView)rootView.findViewById(R.id.friendsLV);
        final TextView mNoFrndTextView = (TextView)rootView.findViewById(R.id.noFriendsTV);

        if (ParseUser.getCurrentUser() != null) {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
            query.include("friends");
            query.getFirstInBackground(new GetCallback<ParseUser>() {
                ProgressBar mLoading = (ProgressBar)rootView.findViewById(R.id.friends_progress);
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    mLoading.setVisibility(View.VISIBLE);
                    Log.d("progress", String.valueOf(mLoading.getVisibility()));
                    mFriendsList.clear();
                    List<ParseUser> mTempList = parseUser.getList("friends");
                    for (ParseUser pUser : mTempList) {
                        mFriendsList.add(pUser);
                    }
                    //Log.d("test", mFriendsList.toString());
                    listView.setEmptyView(mNoFrndTextView);
                    adapter = new CustomFriendsAdapter(getActivity(), R.layout.list_customuser, mFriendsList);
                    listView.setAdapter(adapter);
                    mLoading.setVisibility(View.GONE);
                    Log.d("progress", String.valueOf(mLoading.getVisibility()));
                }
            });
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ParseUser mChosenFriend = (ParseUser)listView.getItemAtPosition(position);
                    QuickProfileInfoDialogFrag quickInfoDF = new QuickProfileInfoDialogFrag();
                    Bundle args = new Bundle();
                    args.putString("frndID", mChosenFriend.getObjectId());
                    quickInfoDF.setArguments(args);
                    quickInfoDF.show(getActivity().getFragmentManager(), "quick view");
                }
            });
        }

        Button mAddFriends = (Button)rootView.findViewById(R.id.friendAddBT);
        mAddFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment mPickFriendsDialogFrag = new FriendPickerDialogFrag();
                mPickFriendsDialogFrag.show(getActivity().getFragmentManager(), "addFrnd");
            }
        });

        return rootView;
    }

    public class CustomFriendsAdapter extends ArrayAdapter<ParseUser>{
        private int mResource;
        private ArrayList<ParseUser> mListFriends;

        public CustomFriendsAdapter(Context context, int resource, ArrayList<ParseUser> friends) {
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
