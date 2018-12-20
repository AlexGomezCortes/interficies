package goms.social_wall;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;


public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener
{

    private Button buttonSubmit;
    private EditText textName;
    private EditText textDescription;
    private FirebaseUser user;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private Button home;
    private Button news;
    private Button post;

    BottomNavigationView mBottomNav;
    private Menu bottomMenu;


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        mAuth = FirebaseAuth.getInstance();

        textName = findViewById(R.id.textName);
        textDescription = findViewById(R.id.textDesc);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        db = FirebaseFirestore.getInstance();

        user = mAuth.getCurrentUser();
        textName.setText((CharSequence)user.getUid()); //db.collection("users").document(user.getUid())

        buttonSubmit.setOnClickListener(this);

        mBottomNav = findViewById(R.id.bottomNavigationView);
        bottomMenu = mBottomNav.getMenu();

        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId())
                {
                    case R.id.home:
                        startActivity(new Intent(UserProfileActivity.this, UserProfileActivity.class));
                        break;

                    case R.id.news:
                        //startActivity(new Intent(UserProfileActivity.this, post_layout.class));
                        break;

                    case R.id.post:
                        startActivity(new Intent(UserProfileActivity.this, PostActivity.class));
                        break;
                }
                return true;
            }
        });




    }

    void SubmitChanges()
    {
        final String desc = textDescription.getText().toString().trim();
        final String name = textName.getText().toString().trim();
        Map<String, Object> user_ = new HashMap<>();
        user_.put("username", name);
        user_.put("description", desc);

        user = mAuth.getCurrentUser();

        db.collection("users")
                .document(user.getUid())
                .set(user_, SetOptions.merge());
    }


    @Override
    public void onClick (View view)
    {
        if(view == buttonSubmit)
        {
            SubmitChanges();
        }


    }
}
