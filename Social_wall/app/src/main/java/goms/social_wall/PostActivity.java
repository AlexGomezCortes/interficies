package goms.social_wall;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class PostActivity extends AppCompatActivity implements View.OnClickListener
{

    private Button buttonSend;
    private EditText text;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    BottomNavigationView mBottomNav;

protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.post_layout);

            buttonSend = findViewById(R.id.send_b);
            text = findViewById(R.id.textName);

            mBottomNav = findViewById(R.id.bottomNavigationView);

            buttonSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if(v == buttonSend)
                    {
                        SubmitMessage();
                    }
                }
            });

            mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch(menuItem.getItemId())
                    {
                        case R.id.home:
                            startActivity(new Intent(PostActivity.this, UserProfileActivity.class));
                            break;

                        case R.id.news:
                            //startActivity(new Intent(UserProfileActivity.this, post_layout.class));
                            break;

                        case R.id.post:
                            startActivity(new Intent(PostActivity.this, PostActivity.class));
                            break;

                        default:
                            return false;
                    }
                    return true;
                }
            });

        }

        void SubmitMessage()
        {

            final String msg = text.getText().toString().trim();

            Map<String, Object> message = new HashMap<>();
            message.put("content", msg);

            db.collection("messages").add(message);

        }

    @Override
    public void onClick (View view)
    {



    }



}
