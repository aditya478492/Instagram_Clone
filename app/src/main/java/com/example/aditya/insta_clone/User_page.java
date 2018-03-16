package com.example.aditya.insta_clone;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class User_page extends AppCompatActivity {
    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.add_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults!=null){
            getPhoto();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.adfrnd){

            Intent intent=new Intent(this,UsersList.class);
            Intent get_intent=getIntent();
            intent.putExtra("uname",get_intent.getStringExtra("uname"));
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    ImageButton imgbtn=null;
    EditText et_twt=null;
    Button twtbtn=null;
    ListView lst=null;
    ListView lstimg=null;
    ArrayList<String>  arr=new ArrayList<>();
    ArrayList<ImgPOJO> lst1=new ArrayList<>();
    ArrayAdapter adp;

    public void getPhoto(){
        Intent open_gallery=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(open_gallery,1);
    }

    public void select_image(View view){
        Log.i("img button","image button clicked!!!");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                Log.i("aa","permission if");
            }
        }else{
            getPhoto();
            Log.i("aa","permission else");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null && resultCode==RESULT_OK){
            Uri selected_image=data.getData();
            try {
                Bitmap bitmap_image=MediaStore.Images.Media.getBitmap(this.getContentResolver(),selected_image);
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap_image.compress(Bitmap.CompressFormat.PNG,100,stream);
                byte[] bytearray=stream.toByteArray();
                ParseFile file=new ParseFile("Image.png",bytearray);
                Intent get_intent=getIntent();
                ParseObject objImg=new ParseObject(get_intent.getStringExtra("uname"));
                Log.i("ds",""+get_intent.getStringExtra("uname"));
                objImg.put("Image1",file);
                objImg.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            Toast.makeText(User_page.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(User_page.this, "Problem in Uploading !!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void post(View view){
        String typed_tweet=et_twt.getText().toString();
        if(!typed_tweet.equalsIgnoreCase("")){
            Log.i("typed tweet",typed_tweet);
            Intent get_intent=getIntent();
            ParseObject insert_twt=new ParseObject(get_intent.getStringExtra("uname"));
            insert_twt.put("Feed",typed_tweet);
            insert_twt.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Toast.makeText(User_page.this, "Tweet Saved !!", Toast.LENGTH_SHORT).show();
                    et_twt.setText("");
                }
            });
        }else if(typed_tweet.matches("")){
            Toast.makeText(this, "Enter something !!", Toast.LENGTH_SHORT).show();
        }
    }

    public void String_search(final String nm){

        ParseQuery<ParseObject> newquery=ParseQuery.getQuery("Classes");
        newquery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for(ParseObject obj2:objects){
                    String ref=obj2.getString("name");
                    if(ref.matches(nm+"(.*)")){
                        ParseQuery<ParseObject> search_query=ParseQuery.getQuery(ref);
                        search_query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                for(ParseObject feedObj:objects){
                                    arr.add(feedObj.getString("Feed"));
                                    //+++++
                                    if(feedObj.getParseFile("Image1")!=null) {
                                        ParseFile image=feedObj.getParseFile("Image1");
                                        image.getDataInBackground(new GetDataCallback() {
                                            @Override
                                            public void done(byte[] data, ParseException e) {
                                                if(e==null) {
                                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                    lst1.add(new ImgPOJO(bitmap));
                                                    Log.i("Image","Image added");
                                                }else{
                                                    e.printStackTrace();
                                                    Log.i("Image","Image error");
                                                }
                                            }
                                        });
                                        Custom_img_adp cust_adp=new Custom_img_adp(getApplicationContext(),R.layout.layout,lst1);
                                        lstimg.setAdapter(cust_adp);
                                        Log.i("Image","Image adapter set");
                                    }else{
                                        Log.i("Image","no image");
                                    }
                                }
                                //lst.setAdapter(adp);
                            }
                        });
                    }
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        adp=new ArrayAdapter(this,android.R.layout.simple_list_item_1,arr);
        imgbtn=(ImageButton)findViewById(R.id.imgbtn);
        twtbtn=(Button)findViewById(R.id.tweetbtn);
        et_twt=(EditText)findViewById(R.id.tweet);
        lst=(ListView)findViewById(R.id.lst_vw_frndpst);
        lstimg=(ListView)findViewById(R.id.lstvw_img);

        Intent get_intent=getIntent();
        ParseQuery<ParseObject> parseQuery=ParseQuery.getQuery(get_intent.getStringExtra("uname"));
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){
                    for(ParseObject obj:objects){
                        String frnd_name=obj.getString("friends");
                        String_search(frnd_name);
                    }
                }
            }
        });
    }
}
