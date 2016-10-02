package com.kani.project.assisto;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.kani.project.assisto.adapter.MyAdapterMainChat;
import com.kani.project.assisto.connectionutils.Connection;
import com.kani.project.assisto.connectionutils.models.ChatModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainChat extends AppCompatActivity implements View.OnClickListener{

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    EditText editText;
    List<ChatModel> list;
    String movie="pink";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView=(RecyclerView)findViewById(R.id.recycle_chat);
        layoutManager=new LinearLayoutManager(MainChat.this);
        //layoutManager.setReverseLayout(true);
       // layoutManager.setStackFromEnd(true);

        list=new ArrayList<>();

        adapter=new MyAdapterMainChat(list,MainChat.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        editText=(EditText)findViewById(R.id.edit_chat);
        findViewById(R.id.button_send).setOnClickListener(this);
       // Intent intent=new Intent(MainChat.this,DoctorsPlace.class);
       // startActivity(intent);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        String userMessage=editText.getText().toString();
        editText.setText("");
        ChatModel chatModel=new ChatModel();
        chatModel.setMessage(userMessage);
        chatModel.setResponse(false);
        list.add(chatModel);
        if(userMessage.equals("consult_doctor"))
        {
            Intent intent=new Intent(MainChat.this,DoctorsPlace.class);
            startActivity(intent);
        }
        else if (userMessage.equals("movie_book"))
        {
            //book movie
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.paytm.com/movies"));
            startActivity(browserIntent);
        }
        else {
            userMessage = userMessage.replace(" ", "+");
            String url = "http://10.0.151.148:8080/?method=POST&name=" + userMessage;
            SendMessage sendMessage = new SendMessage(url, new JSONObject());
            sendMessage.execute();


            addMessage();
            if(userMessage.contains("consult_doctor"))
            {
                editText.setText("consult_doctor");
            }


        }




    }

    public void addMessage()
    {
        adapter=new MyAdapterMainChat(list,MainChat.this);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(list.size()-1);
    }

    public class SendMessage extends AsyncTask<Void,Void,String>
    {
        String url;
        JSONObject params;

        public SendMessage(String url,JSONObject params) {
            this.url=url;
            this.params=params;
        }


        @Override
        protected String doInBackground(Void... voids) {
            Connection connection=new Connection(url,params,MainChat.this);
            return connection.connectiontask();
        }

        @Override
        protected void onPostExecute(String s) {
            Log.v(getClass().getSimpleName(),s);
            try {
                JSONObject main=new JSONObject(s);
                String message=main.getString("response");
                if(!message.equals("")) {
                    ChatModel chatModel = new ChatModel();
                    chatModel.setMessage(message);
                    chatModel.setResponse(true);
                    list.add(chatModel);
                    addMessage();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
