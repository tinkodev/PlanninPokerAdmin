package com.example.planningpokerproject.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.planningpokerproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment {

    private EditText lUserName;
    private EditText lPassword;
    private Button lLoginButton, lRegisterButton;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String username,password;


    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String sUsername = "userNameKey";
    public static final String sPassword = "paswordKey";


    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstace){
        View view = inflater.inflate(R.layout.fragment_login,container, false);

        initialization(view);
        getPrefernces();
        register();
        login();

        return view;
    }

    private void register(){
        lRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr=getFragmentManager().beginTransaction();
                fr.replace(R.id.fragment_container,new RegisterFragment());
                fr.commit();
            }
        });
    }

    private void login(){
        lLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = lUserName.getText().toString();
                password = lPassword.getText().toString();

                userCheck(username);

                saveElement();
            }
        });
    }

    private void userCheck(final String username){
        // Read from the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Admins").child(username).child("Password");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String kod = dataSnapshot.getValue().toString();


                if(!kod.equals(password)){
                    Toast.makeText(getContext(),"Password or Username is incorrect", Toast.LENGTH_SHORT).show();
                }
                else{
                    CreateRoomFragment fragment = CreateRoomFragment.newInstance(username);

                    FragmentTransaction fr=getFragmentManager().beginTransaction();
                    fr.replace(R.id.fragment_container,fragment);
                    fr.commit();
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void initialization (View view){
        lLoginButton = view.findViewById(R.id.lLoginButton);
        lRegisterButton = view.findViewById(R.id.lRegisterButton);

        lUserName = view.findViewById(R.id.lLoginName);
        lPassword = view.findViewById(R.id.lLoginPassword);
    }

    //Vissza rakja az elmentett adatokat az EditTextbe
    private void getPrefernces(){
        SharedPreferences sp =this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        if(sp.contains("userNameKey")){
            String e = sp.getString("userNameKey","not found");
            lUserName.setText(e);
        }

        if(sp.contains("paswordKey")){
            String e = sp.getString("paswordKey","not found");
            lPassword.setText(e);
        }
    }

    //Elmenti a bejeletkezett felhasznalot hogy ne kelljen ujra beirja az adatait
    private void saveElement(){
        SharedPreferences sp =this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userNameKey",username);
        editor.putString("paswordKey",password);
        editor.apply();

    }
}
