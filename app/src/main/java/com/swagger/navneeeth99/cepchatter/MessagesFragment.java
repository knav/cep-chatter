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

//        ParseQueryAdapter.QueryFactory<PMessage> mQueryFactor = new ParseQueryAdapter.QueryFactory<PMessage>() {
//            public ParseQuery<PMessage> create() {
//                ParseQuery<PMessage> query = new ParseQuery<>("PMessage");
//                if (ParseUser.getCurrentUser() != null) {
//                    query.whereEqualTo("to", ParseUser.getCurrentUser().getUsername());
//                }
//                return query;
//            }
//        };

        //final ParseQueryAdapter<Message> adapter = new ParseQueryAdapter<>(getActivity(), Message.class);
        final ParseQueryAdapter<PMessage> adapter = new CustomMessageAdapter(getActivity());
        ListView mInboxListView = (ListView)rootView.findViewById(R.id.incomingLV);
        mInboxListView.setAdapter(adapter);

        Button mMessageSender = (Button)rootView.findViewById(R.id.sendMessageBT);
        mMessageSender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PMessage newYo = new PMessage();
//                newYo.setmSender(ParseUser.getCurrentUser().getUsername());
//                newYo.setmReceiver(ParseUser.getCurrentUser().getUsername());
//                newYo.setmTitle("YO!");
//                newYo.setmContent("What a lonely life you lead! Do you want to find a way to escape the loneliness that tears at your heart? Well, there is actually no way to do that - so good day sir!");
//                newYo.saveInBackground();
//                adapter.notifyDataSetChanged();
//                adapter.loadObjects();
                DialogFragment mSendMsgDialogFrag = new SendMsgDialogFrag();
                mSendMsgDialogFrag.show(getActivity().getFragmentManager(), "sendMsg");
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
