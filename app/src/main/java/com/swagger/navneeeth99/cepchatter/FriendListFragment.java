package com.swagger.navneeeth99.cepchatter;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.Objects;

/**
* Created by navneeeth99 on 17/5/15.
*/
public class FriendListFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

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

        ParseQueryAdapter<ParseUser> adapter = new CustomUserAdapter(getActivity());
        ListView listView = (ListView)rootView.findViewById(R.id.friendsLV);
        listView.setAdapter(adapter);

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

    public static class CustomUserAdapter extends ParseQueryAdapter<ParseUser> {

        public CustomUserAdapter(Context context) {
            super(context, new ParseQueryAdapter.QueryFactory<ParseUser>() {
                public ParseQuery create() {
                    ParseQuery query = new ParseQuery(ParseUser.class);
//                    for (Object friendUser : ParseUser.getCurrentUser().get("friends")){
//
//                    }
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
}
