package com.ssafy.chatapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUser();
        initView();
        initFirebase();
    }

    String id;
    String name;
    public void initUser(){
        id = "19970423";
        name = "정호조";
    }

    MyAdapter adapter;
    ListView list;
    private void initView() {
        list = findViewById(R.id.list);
        EditText message = findViewById(R.id.message);
        Button button = findViewById(R.id.button);

        adapter = new MyAdapter(this, R.layout.list_item);
        list.setAdapter(adapter);

        button.setOnClickListener(view -> {
            ChatData data = new ChatData(id, name, message.getText().toString(), System.currentTimeMillis());
//            Toast.makeText(this, message.getText().toString(), Toast.LENGTH_SHORT).show();
//            adapter.add(data);        //chatdata 의 데이터는 adapter가 관리

            // firebase로 전송
            myRef.push().setValue(data);
            message.setText("");

        });
    }

    class MyAdapter extends ArrayAdapter<ChatData>{     //배열 형태로 데이터(채팅)를 관리하기 때문에 arrayadapter
        Context context;
        int resourse;
        public MyAdapter(@NonNull Context context, int resource/*리스트 즉, 하나하나를 보여주는 xml*/) {
            super(context, resource);
            this.context = context;  //보통 맴버로 지정해줌
            this.resourse = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {      //view들이 반복해서 들어감
            View view = null;
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(resourse, null);

//                view = LayoutInflater.from(context).inflate(resourse, null);
            }
            else{
                view = convertView;
            }

            ChatData data = getItem(position);
            LinearLayout leftLayer = view.findViewById(R.id.left_layer);
            LinearLayout myLayer = view.findViewById(R.id.my_layer);
            if(data.getId().equals(id)){    //내 글일 때
                leftLayer.setVisibility(View.GONE);
                myLayer.setVisibility(View.VISIBLE);

                ((TextView)view.findViewById(R.id.my_message)).setText(data.getMessage());
                ((TextView)view.findViewById(R.id.my_time)).setText(makeTime(data.getTime()));
                ((TextView)view.findViewById(R.id.my_name)).setText(data.getName());
            }
            else{   //다른 사람 글일 때
                leftLayer.setVisibility(View.VISIBLE);
                myLayer.setVisibility(View.GONE);

                ((TextView)view.findViewById(R.id.left_message)).setText(data.getMessage());
                ((TextView)view.findViewById(R.id.left_time)).setText(makeTime(data.getTime()));
                ((TextView)view.findViewById(R.id.left_name)).setText(data.getName());
            }

            return view;
//            return super.getView(position, convertView/* 데이터를 재사용할 때 사용, 컨버트뷰가 널이 아니면 어디선가 재사용 한 거임*/, parent);
        }

        private String makeTime(long timeMillis){
            Calendar calendar = Calendar.getInstance();     //getInstance : singletone 인스턴스(singletone이란?)
            calendar.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            calendar.setTimeInMillis(timeMillis);

            int hour = calendar.get(Calendar.HOUR_OF_DAY);      //HOUR : 12h,  HOUR_OF_DAY : 24h
            int min = calendar.get(Calendar.MINUTE);
            return String.format("%02d", hour)+":"+String.format("%02d", min);
        }
    }

    DatabaseReference myRef;
    private void initFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot/*데이터 들어오는 곳*/, @Nullable String previousChildName) {
                ChatData data = snapshot.getValue(ChatData.class);
                adapter.add(data);
                list.smoothScrollToPosition(adapter.getCount());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
