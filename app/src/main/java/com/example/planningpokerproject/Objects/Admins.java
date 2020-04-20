package com.example.planningpokerproject.Objects;

import java.util.ArrayList;

public class Admins {
    private String roomID;
    private ArrayList<Question> questions;

    public Admins(String roomID, ArrayList<Question> questions) {
        this.roomID = roomID;
        this.questions = questions;
    }

    public Admins() {

    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "Admins{" +
                "roomID='" + roomID + '\'' +
                ", questions=" + questions +
                '}';
    }
}
