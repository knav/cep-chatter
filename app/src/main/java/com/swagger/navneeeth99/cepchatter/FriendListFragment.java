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
    private static ArrayList<ParseUser> mCurrentFriendList = new ArrayList<>();
    public static CustomFriendsAdapter adapter;

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

        //ParseQueryAdapter<ParseUser> adapter = new CustomUserAdapter(getActivity());
        ParseQuery query = ParseUser.getQuery();
        query.include("friends");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                Log.d("test", "At first done of query");
            }

            public void done(ParseUser o, Throwable throwable) {
                Log.d("test", "At second done of query");
                o.getList("friends");
                adapter = new CustomFriendsAdapter(getActivity(), R.layout.list_customuser, mCurrentFriendList);
                ListView listView = (ListView)rootView.findViewById(R.id.friendsLV);
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

    /*public static class CustomUserAdapter extends ParseQueryAdapter<ParseUser> {

        public CustomUserAdapter(Context context) {
            super(context, new ParseQueryAdapter.QueryFactory<ParseUser>() {
                public ParseQuery create() {
                    ParseQuery query = new ParseQuery(ParseUser.class);
                    query.include("friends");
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> list, ParseException e) {
                            for (ParseUser pUser:list){
                                mCurrentFriendList.add(pUser);
                            }
                        }

                        public void done(ParseUser o, Throwable throwable) {
                            o.getList("friends");
                        }
                    });
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

    }*/

    public class CustomFriendsAdapter extends ArrayAdapter<ParseUser>{
        private int mResource;
        private ArrayList<ParseUser> mListFriends;

        public CustomFriendsAdapter(Context context, int resource, ArrayList<ParseUser> mListFriends) {
            super(context, resource);
            this.mListFriends = mListFriends;
            mResource = resource;
        }

        @Override
        public View getView(int position, View row, ViewGroup parent) {
            if (row == null) {
                row = getActivity().getLayoutInflater().inflate(mResource, parent, false);
            }

            // Add the title view
            TextView titleTextView = (TextView)row.findViewById(R.id.usernameTV);
            titleTextView.setText(mListFriends.get(position).getString("username"));
            return row;

        }
    }
}
