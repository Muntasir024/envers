package com.example.envers.model;

import java.util.HashMap;
import java.util.Map;

public class ProductDifference {
    private Number revisionId;
    private Map<String, ValueDifference> fieldDifferences = new HashMap<>();

    public Number getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(Number revisionId) {
        this.revisionId = revisionId;
    }

    public Map<String, ValueDifference> getFieldDifferences() {
        return fieldDifferences;
    }

    public void addDifference(String fieldName, Object oldValue, Object newValue) {
        fieldDifferences.put(fieldName, new ValueDifference(oldValue, newValue));
    }

    public static class ValueDifference {
        private final Object oldValue;
        private final Object newValue;

        public ValueDifference(Object oldValue, Object newValue) {
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        public Object getOldValue() {
            return oldValue;
        }

        public Object getNewValue() {
            return newValue;
        }
    }
}

