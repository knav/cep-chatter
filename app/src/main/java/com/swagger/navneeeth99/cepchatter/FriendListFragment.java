package com.swagger.navneeeth99.cepchatter;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
* Created by navneeeth99 on 17/5/15.
*/
public class FriendListFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static CustomFriendsAdapter adapter;
    //public static ArrayAdapter<ParseUser> adapter;
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

        ParseQuery query = ParseUser.getQuery();
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.include("friends");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                Log.d("test", list.toString());
                List<ParseUser> mTempList = list.get(0).getList("friends");
                for (ParseUser pUser:mTempList){
                    mFriendsList.add(pUser);
                }
                Log.d("test", mFriendsList.toString());
                adapter = new CustomFriendsAdapter(getActivity(), R.layout.list_customuser, mFriendsList);
                listView.setAdapter(adapter);
            }
        });

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
