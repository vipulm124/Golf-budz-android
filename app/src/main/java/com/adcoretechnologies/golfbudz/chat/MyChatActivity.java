package com.adcoretechnologies.golfbudz.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.adcoretechnologies.golfbudz.R;
import com.adcoretechnologies.golfbudz.core.base.BaseActivity;
import com.adcoretechnologies.golfbudz.core.base.BoEventData;
import com.adcoretechnologies.golfbudz.utils.Const;
import com.adcoretechnologies.golfbudz.utils.Pref;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class MyChatActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    MyChatAdapter adapter;
    private List<MyChat> allMyChat= new ArrayList<>();
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    @BindView(R.id.noUsersText)
    TextView noUsersText;
    @BindView(R.id.progress)
    ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_chat);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        ButterKnife.bind(this);
        init();

    }





    @Override
    public void init() {

        LinearLayoutManager manager = new LinearLayoutManager(this);
        allMyChat = new ArrayList<>();
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        getAllChatUsers();




    }

    @Override
    public void log(String message) {

    }

    private void getAllChatUsers() {
        String userId = Pref.Read(this, Const.PREF_USER_ID);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child(Const.FIREBASE_DB_CHANNELS).child(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    MyChat chat = postSnapshot.getValue(MyChat.class);
                    chat.setChatId(postSnapshot.getKey());
                    allMyChat.add(chat);
                }
                bindData(allMyChat);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                log("Operation cancelled");


            }
        });
    }
    private void updateViews(int size) {
        if (size == 0) {
            progress.setVisibility(View.GONE);
            noUsersText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            progress.setVisibility(View.GONE);
            noUsersText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void bindData(List<MyChat> allMyChat) {
        adapter = new MyChatAdapter(allMyChat,this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        updateViews(allMyChat.size());
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    public void onEventMainThread(BoEventData eventData) {

        int eventType = eventData.eventType;
        int id = eventData.getId();
        String data = eventData.getData();
        Object object = eventData.getObject();
        switch (eventType) {
            case BoEventData.EVENT_CHAT_ITEM_CLICK: {
                MyChat  friend= (MyChat) object;
                Intent intent = new Intent(this, ChatDashboradActivity.class);
                intent.putExtra(Const.EXTRA_CHAT_WITH, friend.getName());
                intent.putExtra(Const.EXTRA_CHANNEL_ID, friend.chatId);
                intent.putExtra(Const.EXTRA_CHATWITH_ID, Integer.parseInt(friend.chatWithId));

                startActivity(intent);
                break;
            }

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}