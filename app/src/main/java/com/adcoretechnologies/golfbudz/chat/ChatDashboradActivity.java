package com.adcoretechnologies.golfbudz.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.core.base.BaseActivity;
import com.adcoretechnologies.golfbudz.utils.Common;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.adcoretechnologies.golfbudz.utils.Pref;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 1/17/2017.
 */

public class ChatDashboradActivity extends BaseActivity {

    @BindView(R.id.sendButton)
    ImageView sendButton;
    @BindView(R.id.messageArea)
    EditText messageArea;


    String channelId, chatWith,friendImageUrl;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.chats)
    RecyclerView recyclerView;
    private List<Post> allItems;
    private ChatUserAdapter adapter;
    String chatWithId,chatWithImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        init();
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if (!messageText.equals("")) {


                    saveChatDeatil(channelId, chatWith, messageText);
                    messageArea.setText("");
                    saveChannel(channelId, chatWith, messageText);
                    saveChannelFrom(channelId, chatWith, messageText,chatWithId);
                }

            }
        });


    }



    @Override
    public void init() {
        allItems = new ArrayList<>();
        Intent intent = getIntent();
        if (intent != null) {
            // BoJob   job= (BoJob) intent.getExtras().getSerializable(Const.EXTRA_JOB_DETAIL);
            channelId = intent.getStringExtra(Const.EXTRA_CHANNEL_ID);
            chatWith = intent.getStringExtra(Const.EXTRA_CHAT_WITH);
            chatWithId = intent.getStringExtra(Const.EXTRA_CHATWITH_ID);
            chatWithImage = intent.getStringExtra(Const.EXTRA_IMAGE_URL);

            setTitle(chatWith);
        }

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        if(channelId!=null)
            getMessages();
        else
            toast("channel Id is missing");

    }

    private void getMessages() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Query myTopPostsQuery = mDatabase.child(Const.FIREBASE_DB_CONVERSATIONS).child(channelId);
        ValueEventListener valueEventListener = myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                allItems.removeAll(allItems);
                for (com.google.firebase.database.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    post.postId = postSnapshot.getKey();
                    allItems.add(post);
                }
                updateAdapter(allItems);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                log("Operation cancelled");
                updateAdapter(null);
            }
        });
    }

    private void updateAdapter(List<Post> allItems) {
        progress.setVisibility(View.GONE);
        if (allItems != null && allItems.size() > 0) {
            adapter = new ChatUserAdapter(allItems, this);

            recyclerView.setAdapter(adapter);
            updateViews(allItems.size());
        }
    }

    @Override
    public void log(String message) {

    }


    private void updateViews(int size) {
        if (size == 0) {
            progress.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        } else {
            progress.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void saveChatDeatil(String channelId, String to, String message) {
        String from = Pref.Read(this, Const.PREF_USER_DISPLAY_NAME);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child(Const.FIREBASE_DB_CONVERSATIONS).child(channelId).push().getKey();
        final Post post = new Post(from, message, to);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + Const.FIREBASE_DB_CONVERSATIONS + "/" + channelId + "/" + key, postValues);
        mDatabase.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if (databaseError != null) {
                    Common.logException(getApplicationContext(), "Error while creating new post", databaseError, null);
                    return;
                }
                Toast.makeText(getApplicationContext(), getString(R.string.post_successfully_submit_toast), Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void saveChannel(String channelId, String name, String message) {
        String userId = Pref.Read(this, Const.PREF_USER_ID);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child(Const.FIREBASE_DB_CHANNELS).child(userId).push().getKey();
        final MyChat chat = new MyChat(name, chatWithImage, message,userId);
        Map<String, Object> postValues = chat.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + Const.FIREBASE_DB_CHANNELS + "/" + userId + "/" + channelId, postValues);
        mDatabase.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if (databaseError != null) {
                    Common.logException(getApplicationContext(), "Error while creating new post", databaseError, null);
                    return;
                }
                Toast.makeText(getApplicationContext(), getString(R.string.post_successfully_submit_toast), Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void saveChannelFrom(String channelId, String name, String message, String chatWithId) {
        String from = Pref.Read(this, Const.PREF_USER_DISPLAY_NAME);
        String userImg = Pref.Read(this, Const.PREF_USE_IMAGE_PATH);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
       // String key = mDatabase.child(Const.FIREBASE_DB_CHANNELS).child(chatWithId).push().getKey();
        final MyChat chat = new MyChat(from, userImg, message,chatWithId);
        Map<String, Object> postValues = chat.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + Const.FIREBASE_DB_CHANNELS + "/" + chatWithId + "/" + channelId, postValues);
        mDatabase.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if (databaseError != null) {
                    Common.logException(getApplicationContext(), "Error while creating new post", databaseError, null);
                    return;
                }
                Toast.makeText(getApplicationContext(), getString(R.string.post_successfully_submit_toast), Toast.LENGTH_SHORT).show();

            }
        });
    }
}