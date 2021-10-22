package me.litz.model;

import java.io.Serializable;

public class Revision implements Serializable {

    private static final long serialVersionUID = 9072057591548752598L;

    private Integer revision;

    private Integer previousRevision;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public Integer getPreviousRevision() {
        return previousRevision;
    }

    public void setPreviousRevision(Integer previousRevision) {
        this.previousRevision = previousRevision;
    }
}
