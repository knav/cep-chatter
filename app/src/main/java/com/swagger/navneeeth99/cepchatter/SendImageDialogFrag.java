package com.swagger.navneeeth99.cepchatter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benjamin on 7/6/15.
 */
public class SendImageDialogFrag extends DialogFragment {
    public static final int GET_FROM_GALLERY = 555;
    private ParseUser mFriendSelected;
    private String mMessageTitle;
    private String mImageString;
    public byte[] image;
    public LinearLayout mLL = null;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater mLayoutInflater = getActivity().getLayoutInflater();
        mLL = (LinearLayout)mLayoutInflater.inflate(R.layout.fragment_send_image, null);


        final ImageButton mUploadImageButton = (ImageButton)mLL.findViewById(R.id.uploadImageButton);
        final EditText mMessageTitleET = (EditText)mLL.findViewById(R.id.newMsgTitleET);
        final Spinner mFriendSpinner = (Spinner)mLL.findViewById(R.id.friendSpinner);

        //ParseQueryAdapter<ParseUser> adapter = new CustomUserAdapter(this.getActivity());
        List<ParseUser> mCurrFriendsList = ParseUser.getCurrentUser().getList("friends");
        final ArrayList<ParseUser> mCurrFriendsArray = new ArrayList<>();
        for (ParseUser pUser : mCurrFriendsList){
            mCurrFriendsArray.add(pUser);
        }
        CustomFriendsDropdownAdapter adapter = new CustomFriendsDropdownAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, mCurrFriendsArray);
        //ArrayAdapter<ParseUser> adapter = new ArrayAdapter<>(getActivity(), R.layout.list_customuser, R.id.usernameTV, mCurrFriendsArray);
        mFriendSpinner.setAdapter(adapter);

        mFriendSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mFriendSelected = mCurrFriendsArray.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mUploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Send Image")
                .setView(mLL)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mImageString= Base64.encodeToString(image,Base64.DEFAULT);
                        mMessageTitle = mMessageTitleET.getText().toString();
                        PMessage newMsg = new PMessage();
                        newMsg.setmSender(ParseUser.getCurrentUser().getUsername());
                        newMsg.setmReceiver(mFriendSelected.getUsername());
                        newMsg.setmTitle(mMessageTitle);
                        newMsg.setmContent(mImageString);
                        newMsg.setmRead(false);
                        newMsg.setmIsImage(true);
                        newMsg.saveInBackground();
                        MessagesFragment.mUnreadMessagesAdapter.notifyDataSetChanged();
                        MessagesFragment.mUnreadMessagesAdapter.loadObjects();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SendImageDialogFrag.this.getDialog().cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public class CustomFriendsDropdownAdapter extends ArrayAdapter<String> {

        private ArrayList<ParseUser> mFriends;
        LayoutInflater inflater;

        public CustomFriendsDropdownAdapter(Context context, int textViewResourceId, ArrayList objects) {
            super(context, textViewResourceId, objects);
            mFriends = objects;
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        // This funtion called for each row ( Called data.size() times )
        public View getCustomView(int position, View convertView, ViewGroup parent) {
            View row = inflater.inflate(R.layout.list_customuser, parent, false);

            ParseUser mFriendPicked = mFriends.get(position);

            TextView usernameTV = (TextView)row.findViewById(R.id.usernameTV);
            final ImageView userprofile = (ImageView)row.findViewById(R.id.profilepicIV);
            usernameTV.setText(mFriendPicked.getUsername());
            ParseFile fileObject = (ParseFile)mFriendPicked.get("photo");
            if (fileObject == null){
                (userprofile).setImageResource(R.drawable.emptydp);
            } else {
                fileObject.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, ParseException e) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        userprofile.setImageBitmap(bmp);
                    }
                });
            }
            return row;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                image = stream.toByteArray();
                ParseFile file;
                file = new ParseFile("new_image.jpeg", image);
                file.saveInBackground();
                ((ImageButton)mLL.findViewById(R.id.uploadImageButton)).setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch(RuntimeException e){
                e.printStackTrace();
            }

        }
    }
}
