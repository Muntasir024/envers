package com.example.envers.model;

import com.example.envers.listener.EntityRevisionListener;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import javax.persistence.*;

@Entity
@RevisionEntity(value = EntityRevisionListener.class)
public class Revision extends DefaultRevisionEntity{
    @Column
    private String ip;

    @Column
    private String username;

    public String getIp() {
        return ip;
    }

    public String getUsername() {
        return username;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Revision(String ip, String username) {
    }

    public Revision() {
    }
}
