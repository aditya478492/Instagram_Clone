package com.example.aditya.insta_clone;

import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnKeyListener {

    Button btn;
    TextView txtvw;
    EditText et_uname;
    EditText et_pwd;

    int count = 0;

    public void create_user(){
        ParseObject parseObject=new ParseObject(et_uname.getText()+""+et_pwd.getText());
        parseObject.put("Feed","Hello this is me"+et_uname.getText());
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Toast.makeText(MainActivity.this, "Profile Created", Toast.LENGTH_SHORT).show();
                }else{
                    e.printStackTrace();
                }
            }
        });
        ParseObject classObj=new ParseObject("Classes");
        classObj.put("name",et_uname.getText()+""+et_pwd.getText());
        classObj.saveInBackground();
    }

    public void next(View view) {

        if (btn.getText() == "Sign In" || count==1) {
            ParseUser user = new ParseUser();
            if (et_uname.getText() != null && et_pwd != null) {
                user.setUsername(et_uname.getText().toString());
                user.setPassword(et_pwd.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Intent intent_frnds=new Intent(getApplicationContext(),User_page.class);
                            create_user();
                            intent_frnds.putExtra("uname",et_uname.getText()+""+et_pwd.getText());
                            //Toast.makeText(MainActivity.this, "SignUp Successfull !", Toast.LENGTH_SHORT).show();
                            startActivity(intent_frnds);
                        } else {
                            Toast.makeText(MainActivity.this, "SignUp Unsuccessfull !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else if (et_uname.getText() == null || et_pwd.getText() == null) {
                et_uname.setHint("Enter username !");
                et_pwd.setHint("Enter password !");
            }
        } else if (btn.getText() == "Log In" || count==0) {
            ParseUser.logInInBackground(et_uname.getText().toString(), et_pwd.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null) {
                        Intent intent_frnds=new Intent(getApplicationContext(),User_page.class);
                        intent_frnds.putExtra("uname",et_uname.getText()+""+et_pwd.getText());
                        Toast.makeText(MainActivity.this, "Login Successfull !", Toast.LENGTH_SHORT).show();
                        startActivity(intent_frnds);
                    } else {
                        Toast.makeText(MainActivity.this, "Login Unsuccessfull !", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void toogle_func(View view) {
        count++;
        btn.setText(txtvw.getText());
        if (count % 2 != 0) {
            txtvw.setText("Log In");
        } else if (count % 2 == 0) {
            txtvw.setText("Sign In");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_uname = (EditText) findViewById(R.id.txt_uname);
        et_pwd = (EditText) findViewById(R.id.txt_pwd);
        btn = (Button) findViewById(R.id.btn_next);
        txtvw = (TextView) findViewById(R.id.txtvw_2);
        RelativeLayout rel=(RelativeLayout)findViewById(R.id.rel_lay);
        ImageView img=(ImageView)findViewById(R.id.imageView2);
        rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
            }

        });
        et_pwd.setOnKeyListener(this);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENTER) {
            next(view);
        }
        return false;
    }
}
