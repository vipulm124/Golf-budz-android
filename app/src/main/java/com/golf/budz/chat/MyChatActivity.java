package com.golf.budz.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.golf.budz.auth.BoUser;
import com.golf.budz.core.base.BaseActivity;
import com.golf.budz.core.base.BoEventData;
import com.golf.budz.core.components.FragmentDataLoader;
import com.golf.budz.friends.BoFriend;
import com.golf.budz.friends.FriendProfileActivity;
import com.golf.budz.friends.MyFriendAdapter;
import com.golf.budz.home.R;
import com.golf.budz.utils.Const;
import com.golf.budz.utils.Pref;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class MyChatActivity extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    MyChatAdapter adapter;
    private List<MyChat> allMyChat= new ArrayList<>();
    private FragmentDataLoader fragmentLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_recyclerview);

        ButterKnife.bind(this);
        fragmentLoader = (FragmentDataLoader) getSupportFragmentManager().findFragmentById(R.id.fragmentLoader);
        if (fragmentLoader == null)
            toast("null");
        else
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
                toast(getResources().getString(R.string.post_detail_error_toast));

            }
        });
    }
    private void updateViews(int size) {
        if (size == 0) {
            fragmentLoader.setDataEmpty("No Friends Found");
            recyclerView.setVisibility(View.GONE);
        } else {
            fragmentLoader.setDataAvailable();
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
    public void log(String message) {

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
                intent.putExtra(Const.EXTRA_IMAGE_URL, friend.getImage());
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
        }

        return super.onOptionsItemSelected(item);
    }
}