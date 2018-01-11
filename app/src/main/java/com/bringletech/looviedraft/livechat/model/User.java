package com.bringletech.looviedraft.livechat.model;


import java.util.Date;

public class User {
    public String name;
    public String email;
    public String avata;
    public Status status;
    public Message message;
    private  long  createAt;
    private Date BirthDate;
    private String avatar;
    private String photo2;
    private String photo3;
    private int yoursexe;  // 0 - FEMALE   /  1  -  MALE
    private int targetage ;


    public User(){
        status = new Status();
        message = new Message();
        status.isOnline = false;
        status.timestamp = 0;
        message.idReceiver = "0";
        message.idSender = "0";
        message.text = "";
        message.timestamp = 0;

    }
}
