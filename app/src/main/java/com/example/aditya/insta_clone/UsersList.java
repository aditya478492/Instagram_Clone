package com.example.aditya.insta_clone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UsersList extends AppCompatActivity {

    ListView lst=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        lst=(ListView)findViewById(R.id.lst_vw_frnds);
       final ArrayList<String> arr=new ArrayList<String>();
       arr.add("   ");
       final ArrayAdapter adp=new ArrayAdapter(this,android.R.layout.simple_list_item_1,arr);
        ParseQuery<ParseUser> parseQuery=ParseUser.getQuery();
        parseQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(objects!=null){
                    for(ParseUser n:objects){
                        arr.add(n.getUsername().toString());
                        Log.i("plussing","errpr");
                    }
                    lst.setAdapter(adp);
                }else{
                    Log.i("opium","errpr");
                    e.printStackTrace();
                }
            }
        });

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent get_intent1=getIntent();
                Log.i("table name",get_intent1.getStringExtra("uname"));
                ParseObject parseObject=new ParseObject(get_intent1.getStringExtra("uname"));
                Log.i("clicked item",arr.get(i));
                parseObject.put("friends",arr.get(i));
                parseObject.saveInBackground();
            }
        });

    }
}
