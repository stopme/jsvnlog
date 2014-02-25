package cn.jsvnlog.pojos;

import java.io.Serializable;
import java.util.Date;

public class Project implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = -1343485542131846326L;
    private String id;
    private String name;
    private Date createDate;
    private String note;
    private String url;
    private long maxrevision;
    private String svnUsername;
    private String svnPassword;
    
    
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public long getMaxrevision() {
        return maxrevision;
    }
    public void setMaxrevision(long maxrevision) {
        this.maxrevision = maxrevision;
    }
    public String getSvnUsername() {
        return svnUsername;
    }
    public void setSvnUsername(String svnUsername) {
        this.svnUsername = svnUsername;
    }
    public String getSvnPassword() {
        return svnPassword;
    }
    public void setSvnPassword(String svnPassword) {
        this.svnPassword = svnPassword;
    }

    
}
