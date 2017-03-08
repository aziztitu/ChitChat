package com.ash.chitchat.others;

/**
 * Created by aziz titu2 on 12/3/2015.
 */
public class AshMessage {
    String sender;
    String data;

    private AshMessage(String s, String d)
    {
        sender=s;
        data=d;
    }

    private AshMessage(String d)
    {
        sender="";
        data=d;
    }

    public static AshMessage parseMessage(String msg)
    {
        String[] s=msg.split(QuickTools.SPLITTER_MSG);
        if(s.length==1)
        {
            return new AshMessage(msg);
        }

        s[1]=s[1].replace(QuickTools.SPLITTER_NEW_LINE, "\n");
        return new AshMessage(s[0],s[1]);
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
