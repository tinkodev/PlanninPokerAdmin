package com.example.planningpokerproject.Fragments;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planningpokerproject.Objects.MyAdapter;
import com.example.planningpokerproject.Objects.Question;
import com.example.planningpokerproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.ContentValues.TAG;


public class CreateRoomFragment extends Fragment {

    private EditText rID, rPassword, rQuestion;
    private Button rCreate,rDate,rTime;
    private String ID, Password, Question1, username,dateTimeText;
    private int gposition,act=0;
    private static final String USERNAME = "userName";
    private DatabaseReference myRef;
    private DatabaseReference mRef;
    private FirebaseDatabase database;
    private ArrayList<Question> listing;
    private MyAdapter adapter;
    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_create_room, container, false);
        initialization(view);


        dateTimePicker();
        create();
        list(view);


        return view;
    }

    //kerdes aktivalasa
    private void activate(){

        adapter.setOnClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mRef = FirebaseDatabase.getInstance().getReference();
                myRef.child(listing.get(position).getQuestionID()).child("Activated").setValue(listing.get(position).getQuestion());
                mRef.child("Groups").child(listing.get(position).getQuestionID()).child("Activated").setValue(listing.get(position).getQuestion());
                Toast.makeText(getContext(),listing.get(position).getQuestion() + " IS ACTIVATED", Toast.LENGTH_LONG).show();

            }
        });

    }

    //datum es ido kivalaszto ablakok
    private void dateTimePicker(){
        rDate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setDate();
            }
        });

        rTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime();
            }
        });

    }

    //datum kivalasztas
    private void setDate() {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);
                dateTimeText = DateFormat.format("dd/MMM/yyyy", calendar1).toString();


            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();
    }

    //ido kivalasztas
    private void setTime() {

        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);

        boolean is24HourFormat = DateFormat.is24HourFormat(getActivity());

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Log.i(TAG, "onTimeSet: " + hour + minute);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR, hour);
                calendar1.set(Calendar.MINUTE, minute);
                dateTimeText = dateTimeText+ "/"+  DateFormat.format("HH:mm", calendar1).toString();
                Log.d("ido", dateTimeText);


            }
        }, HOUR, MINUTE, is24HourFormat);

        timePickerDialog.show();
    }

    //sajat szobak es a bennuk levo kerdesek listazasa
    private void list(final View view){
        myRef = FirebaseDatabase.getInstance().getReference();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        listing = new ArrayList<Question>();

        myRef = FirebaseDatabase.getInstance().getReference("Admins").child(username);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot i: dataSnapshot.getChildren()){
                    Question  newQestion = null;
                    Question question = new Question();
                    if(!i.getKey().equals("Password")) {
                        question.setQuestionID(i.getKey());

                    }
                    for(DataSnapshot j: i.getChildren()) {
                        if(!j.getKey().equals("Question")){
                            question.setQuestionPASS(j.getValue().toString());
                            if (newQestion != null) {
                                newQestion.setQuestionPASS(j.getValue().toString());
                            }
                        }
                        for (DataSnapshot k : j.getChildren()) {

                            question.setQuestion(k.getKey());
                            newQestion =new Question(question.getQuestionID(),question.getQuestion());
                            listing.add(newQestion);
                        }
                    }
                }
                adapter = new MyAdapter(view.getContext(),listing);
                recyclerView.setAdapter(adapter);
                activate();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //szoba kitoltesehez szukseges adatok bevitele
    private void create() {

        rCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ID = rID.getText().toString();
                Password = rPassword.getText().toString();
                Question1 = rQuestion.getText().toString();
                if(!ID.isEmpty() && !Password.isEmpty() && !Question1.isEmpty() && !dateTimeText.isEmpty()) {
                    Question question = new Question(ID, Password, Question1);
                    question.setQuestionID(ID);
                    question.setQuestionPASS(Password);
                    question.setQuestion(Question1);
                    act=0;
                    checkID(question);
                }
                else{
                    Toast.makeText(getContext(),"Please fill all inputs", Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    // szoba illetve kerdes letrehozasa az adatbazisban
    @SuppressLint("CommitPrefEdits")
    private void createQuestion(Question question) {

        myRef = FirebaseDatabase.getInstance().getReference();

        if(act==1){
            myRef.child("Groups").child(listing.get(gposition).getQuestionID()).child("Active Room").setValue(listing.get(gposition).getQuestion());

        }
        else{
            myRef.child("Groups").child(question.getQuestionID()).child("Question").child(question.getQuestion()).child("1").setValue("");
            myRef.child("Groups").child(question.getQuestionID()).child("Question").child(question.getQuestion()).child("2").setValue("");
            myRef.child("Groups").child(question.getQuestionID()).child("Question").child(question.getQuestion()).child("3").setValue("");
            myRef.child("Groups").child(question.getQuestionID()).child("Question").child(question.getQuestion()).child("4").setValue("");
            myRef.child("Groups").child(question.getQuestionID()).child("Question").child(question.getQuestion()).child("5").setValue("");
            myRef.child("Groups").child(question.getQuestionID()).child("Question").child(question.getQuestion()).child("Expire Date").setValue(dateTimeText);

            myRef.child("Groups").child(question.getQuestionID()).child("RoomCode").setValue(question.getQuestionPASS());


            if (username != null) {

                myRef.child("Admins").child(username).child(question.getQuestionID()).child("RoomCode").setValue(question.getQuestionPASS());
                myRef.child("Admins").child(username).child(question.getQuestionID()).child("Question").child(question.getQuestion()).child("Expire Date").setValue(dateTimeText);

            }
        }



    }

    // az adatbazisban megnezi ha a Groups-ban van e ilyen id
    private void checkID(final Question question) {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Groups").child(ID);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    checkMyRooms(question);

                } else {
                    createQuestion(question);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    // az adatbazisban megnezi ha a Adminba van e ilyen id ha nincs se a Groupba se az Adminba akkor letrehozza
    private void checkMyRooms(final Question question) {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Admins").child(username).child(ID);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    createQuestion(question);
                } else {
                    Toast.makeText(getContext(), "This ID is already used", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initialization(View view) {
        rID = view.findViewById(R.id.roomID);
        rPassword = view.findViewById(R.id.roomPassword);
        rQuestion = view.findViewById(R.id.roomQuestion);
        rCreate = view.findViewById(R.id.buttonRoomCreate);
        recyclerView = view.findViewById(R.id.room_list);
        rDate = view.findViewById(R.id.datePicker);
        rTime = view.findViewById(R.id.timePicker);


        if (getArguments() != null) {
            username = getArguments().getString(USERNAME);
        }

    }

    public static CreateRoomFragment newInstance(String text) {
        CreateRoomFragment fragment = new CreateRoomFragment();
        Bundle args = new Bundle();
        args.putString(USERNAME, text);
        fragment.setArguments(args);
        return fragment;
    }

}
