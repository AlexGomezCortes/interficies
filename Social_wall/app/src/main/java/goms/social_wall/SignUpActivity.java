package goms.social_wall;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener
{
    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextUsername;
    private TextView textViewSignin;
    private FirebaseUser user;

    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    FirebaseFirestore db;


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        buttonRegister = findViewById(R.id.buttonSignUp);
        editTextPassword = findViewById(R.id.TextPassword);
        editTextEmail = findViewById(R.id.TextEmail);
        editTextUsername = findViewById(R.id.TextUsername);
        textViewSignin = findViewById(R.id.textViewLogin);

        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();

        //mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void registerUser()
    {
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String username = editTextUsername.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
           editTextEmail.setError("Email necessary");
           editTextEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Minimum length for password must be 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length()<6)
        {
            editTextPassword.setError("Minimum length for password must be 6 characters");
            editTextPassword.requestFocus();
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
             @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_SHORT).show();

                            user = mAuth.getCurrentUser();

                            Map<String, Object> user_ = new HashMap<>();
                            user_.put("mail", email);
                            user_.put("password", password);
                            user_.put("username", username);

                            db.collection("users")
                                    .document("people")
                                    .set(user_);


                            if (user != null)
                            {
                                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username)
                                        .build();

                                user.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(getApplicationContext(), "Username set", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "User loged out", Toast.LENGTH_SHORT).show();
                            }


                } else
                    {
                    //FirebaseAuthException e = (FirebaseAuthException )task.getException();
                    Toast.makeText(getApplicationContext(), "Unsuccessful Register", Toast.LENGTH_SHORT).show();
                }
            }
        });






    }

    public void onClick (View view)
    {
        if (view == buttonRegister)
        {
            registerUser();
        }

        if (view == textViewSignin)
        {
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        }
    }

}
