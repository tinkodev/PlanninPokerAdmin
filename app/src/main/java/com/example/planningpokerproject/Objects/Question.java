package com.example.planningpokerproject.Objects;

public class Question {

    private String questionID;
    private String questionPASS;
    private String question;
    private String questionANSWEAR;


    public Question(String questionID, String questionPASS, String question) {
        this.questionID = questionID;
        this.questionPASS = questionPASS;
        this.question = question;
    }

    public Question() {
    }

    public Question(String questionID, String question) {
        this.questionID = questionID;
        this.question = question;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getQuestionPASS() {
        return questionPASS;
    }

    public void setQuestionPASS(String questionPASS) {
        this.questionPASS = questionPASS;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionANSWEAR() {
        return questionANSWEAR;
    }

    public void setQuestionANSWEAR(String questionANSWEAR) {
        this.questionANSWEAR = questionANSWEAR;
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionID='" + questionID + '\'' +
                ", questionPASS='" + questionPASS + '\'' +
                ", question='" + question + '\'' +
                ", questionANSWEAR='" + questionANSWEAR + '\'' +
                '}';
    }
}