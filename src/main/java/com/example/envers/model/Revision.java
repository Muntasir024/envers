package com.example.envers.model;

import com.example.envers.listener.EntityRevisionListener;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import javax.persistence.*;

import java.util.Date;

@RevisionEntity(value = EntityRevisionListener.class)
public class Revision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    private Long revisionId;

    @RevisionTimestamp
    private Date revisionDate;

    @Column
    private String ip;

    @Column
    private String user;

    public Long getRevisionId() {
        return revisionId;
    }

    public Date getRevisionDate() {
        return revisionDate;
    }

    public String getIp() {
        return ip;
    }

    public String getUser() {
        return user;
    }

    public void setRevisionId(Long revisionId) {
        this.revisionId = revisionId;
    }

    public void setRevisionDate(Date revisionDate) {
        this.revisionDate = revisionDate;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Revision(Long revisionId, Date revisionDate, String ip, String user) {
    }

    public Revision() {
    }
}
