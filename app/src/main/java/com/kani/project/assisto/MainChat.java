package com.kani.project.assisto;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.kani.project.assisto.adapter.MyAdapterMainChat;
import com.kani.project.assisto.connectionutils.Connection;
import com.kani.project.assisto.connectionutils.models.ChatModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainChat extends AppCompatActivity implements View.OnClickListener{

    ImageView send;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    EditText editText;
    List<ChatModel> list;
    MainChat mainChat;
    int REQUEST_CODE=1234;
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
        send=(ImageView)findViewById(R.id.button_send);
        mainChat=this;
        send.setImageResource(R.drawable.microphone);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==0)
                {
                    send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                            startActivityForResult(intent, REQUEST_CODE);
                        }
                    });
                    send.setImageResource(R.drawable.microphone);

                }
                else
                {
                    findViewById(R.id.button_send).setOnClickListener(mainChat);
                    send.setImageResource(R.drawable.forward_arrow);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
       // Intent intent=new Intent(MainChat.this,DoctorsPlace.class);
       // startActivity(intent);



    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {


           ArrayList<String> matches_text = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            editText.setText(matches_text.get(0));


        }
        super.onActivityResult(requestCode, resultCode, data);
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
                    if(message.contains("consult_doctor"))
                    {
                        editText.setText("consult_doctor");
                    }
                    else if (message.contains("movie_book"))
                    {
                        editText.setText("movie_book");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
