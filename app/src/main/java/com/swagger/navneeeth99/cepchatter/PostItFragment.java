package com.swagger.navneeeth99.cepchatter;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by navneeeth99 on 8/6/15.
 */
public class PostItFragment extends android.support.v4.app.Fragment{

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static PostsAdapter postAdapter;

    public static PostItFragment newInstance(int sectionNumber) {
        PostItFragment fragment = new PostItFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PostItFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_postits, container, false);

        Button mCreatePostIt = (Button)rootView.findViewById(R.id.postPostIt);
        mCreatePostIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostPostItDialogFrag postDF = new PostPostItDialogFrag();
                postDF.show(getActivity().getFragmentManager(), "post post it");
            }
        });

        final GridView mPostBoard = (GridView)rootView.findViewById(R.id.postBoardGV);
        postAdapter = new PostsAdapter(getActivity());
        mPostBoard.setAdapter(postAdapter);

        return rootView;
    }

    public static class PostsAdapter extends ParseQueryAdapter<PostIt> {
        public PostsAdapter(Context context) {
            super(context, new ParseQueryAdapter.QueryFactory<PostIt>() {
                public ParseQuery<PostIt> create() {
                    ParseQuery<PostIt> query = new ParseQuery<>("PostIt");
                    return query;
                }
            });
        }

        // Customize the layout by overriding getItemView
        @Override
        public View getItemView(PostIt object, View v, ViewGroup parent) {
            if (v == null) {
                v = View.inflate(getContext(), R.layout.grid_postit, null);
            }

            super.getItemView(object, v, parent);

            // Add the title view
            TextView headTextView = (TextView) v.findViewById(R.id.postHeadTV);
            headTextView.setText(object.getString("header"));

            // Add the content
            TextView contentTextView = (TextView) v.findViewById(R.id.postDescTV);
            contentTextView.setText(object.getString("desc"));

            // Add the creator
            TextView creatorTextView = (TextView) v.findViewById(R.id.postCreatorTV);
            creatorTextView.setText(object.getString("poster"));

            return v;
        }
    }

}
