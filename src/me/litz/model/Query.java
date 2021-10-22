package me.litz.model;

import java.io.Serializable;
import java.util.Date;

public class Query implements Serializable {

    private static final long serialVersionUID = 2011003626965525458L;

    private Integer revision;

    private String id;

    private String dbid = "info";

    private String title;

    private String query;

    private String note;

    private Date created;

    private String creator;

    private Date modified;

    private String updater;

    private String planstatus = "NE";

    private String revisionNote;

    private boolean old = false;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDbid() {
        return dbid;
    }

    public void setDbid(String dbid) {
        this.dbid = dbid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getPlanstatus() {
        return planstatus;
    }

    public void setPlanstatus(String planstatus) {
        this.planstatus = planstatus;
    }

    public String getRevisionNote() {
        return revisionNote;
    }

    public void setRevisionNote(String revisionNote) {
        this.revisionNote = revisionNote;
    }

    public boolean isOld() {
        return old;
    }

    public void setOld(boolean old) {
        this.old = old;
    }

    @Override
    public String toString() {
        return "Query{" +
                "revision=" + revision +
                ", id='" + id + '\'' +
                ", dbid='" + dbid + '\'' +
                ", title='" + title + '\'' +
                ", query='" + query + '\'' +
                ", note='" + note + '\'' +
                ", created=" + created +
                ", creator='" + creator + '\'' +
                ", modified=" + modified +
                ", updater='" + updater + '\'' +
                ", planstatus='" + planstatus + '\'' +
                ", revisionNote='" + revisionNote + '\'' +
                '}';
    }
}
