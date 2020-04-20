package com.example.planningpokerproject.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.planningpokerproject.Objects.User;
import com.example.planningpokerproject.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterFragment extends Fragment {

    private EditText rUserName;
    private EditText rPassword;
    private Button rRegisterButton;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String username,password;

    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstace){
        View view = inflater.inflate(R.layout.fragment_register1,container, false);



        initialization(view);
        register();



        return view;
    }

    private void register(){
        rRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = rUserName.getText().toString();
                password = rPassword.getText().toString();

                User user = new User(username,password);
                user.setUsername(username);
                user.setPassword(password);

                if(!user.getUsername().isEmpty() && !user.getPassword().isEmpty())
                {
                    registerToDatabase(user);

                    FragmentTransaction fr=getFragmentManager().beginTransaction();
                    fr.replace(R.id.fragment_container,new LoginFragment());
                    fr.commit();
                }
                else
                {
                    Toast.makeText(getContext(), "Username or Password is empty", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    private void registerToDatabase(User user){

        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("Admins").child(user.getUsername()).child("Password").setValue(user.getPassword());

    }

    private void initialization (View view){
        rRegisterButton = view.findViewById(R.id.rRegisterButton);
        rUserName = view.findViewById(R.id.rRegisterName);
        rPassword = view.findViewById(R.id.rRegisterPassword);

    }
}
