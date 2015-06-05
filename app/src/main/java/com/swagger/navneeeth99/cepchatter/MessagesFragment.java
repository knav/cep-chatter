package com.swagger.navneeeth99.cepchatter;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import org.w3c.dom.Text;

/**
* Created by navneeeth99 on 17/5/15.
*/
public class MessagesFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static ParseQueryAdapter<PMessage> mMessagesAdapter = null;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MessagesFragment newInstance(int sectionNumber) {
        MessagesFragment fragment = new MessagesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MessagesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);

        TextView mReadEmpty = (TextView)rootView.findViewById(R.id.readEmptyTV);
        ListView mReadListView = (ListView)rootView.findViewById(R.id.readLV);
        mReadListView.setEmptyView(mReadEmpty);

        TextView mInboxEmpty = (TextView)rootView.findViewById(R.id.inboxEmptyTV);
        ListView mInboxListView = (ListView)rootView.findViewById(R.id.incomingLV);
        mInboxListView.setEmptyView(mInboxEmpty);
        mMessagesAdapter = new CustomMessageAdapter(getActivity());
        mInboxListView.setAdapter(mMessagesAdapter);

        Button mMessageSender = (Button)rootView.findViewById(R.id.sendMessageBT);
        mMessageSender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mListSize = ParseUser.getCurrentUser().getList("friends").size();
                Log.d("test", "number of friends: " + mListSize);
                if (mListSize == 0){
                    //Some kind of warning dialog maybe?
                } else {
                    DialogFragment mSendMsgDialogFrag = new SendMsgDialogFrag();
                    mSendMsgDialogFrag.show(getActivity().getFragmentManager(), "sendMsg");
                }
            }
        });

        return rootView;
    }

    public static class CustomMessageAdapter extends ParseQueryAdapter<PMessage> {

        public CustomMessageAdapter(Context context) {
            super(context, new ParseQueryAdapter.QueryFactory<PMessage>() {
                public ParseQuery<PMessage> create() {
                    ParseQuery<PMessage> query = new ParseQuery<>("PMessage");
                    if (ParseUser.getCurrentUser() != null) {
                        query.whereEqualTo("to", ParseUser.getCurrentUser().getUsername());
                    }
                    return query;
                }
            });
        }

        // Customize the layout by overriding getItemView
        @Override
        public View getItemView(PMessage object, View v, ViewGroup parent) {
            if (v == null) {
                v = View.inflate(getContext(), R.layout.list_custommessage, null);
            }

            super.getItemView(object, v, parent);

            // Add the title view
            TextView titleTextView = (TextView)v.findViewById(R.id.msgTitleTV);
            titleTextView.setText(object.getString("title"));

            // Add a brief of the content
            TextView contentTextView = (TextView)v.findViewById(R.id.msgContentBriefTV);
            contentTextView.setText(object.getString("content"));
            return v;
        }

    }
}
