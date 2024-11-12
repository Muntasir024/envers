package com.example.envers.model;

import lombok.Data;

@Data
public class EntityRev<T> {
    private Revision revision;

    private T entity;

    public EntityRev(Revision revision, T revisionListObject) {
    }
}
