package cn.jsvnlog.pojos;

import java.util.Date;


public class JSvnLogEntry {
	 private String id ;
	 private  JRepository repository;
	 private String commitMessage;
     private Date date;
     private String kind;
     private String name;
     private long revision;
     private long preRevision;//
     private long size;
     private String author;
     private long addSum=0;
     private long deleteSum=0;
     private long modifySum=0;
     private long originalSum=0;
     private String diffContent;
     private Project project;
     
     
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public JRepository getRepository() {
		return repository;
	}
	public void setRepository(JRepository repository) {
		this.repository = repository;
	}
	public String getCommitMessage() {
		return commitMessage;
	}
	public void setCommitMessage(String commitMessage) {
		this.commitMessage = commitMessage;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getRevision() {
		return revision;
	}
	public void setRevision(long revision) {
		this.revision = revision;
	}
	public long getPreRevision() {
		return preRevision;
	}
	public void setPreRevision(long preRevision) {
		this.preRevision = preRevision;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public long getAddSum() {
		return addSum;
	}
	public void setAddSum(long addSum) {
		this.addSum = addSum;
	}
	public long getDeleteSum() {
		return deleteSum;
	}
	public void setDeleteSum(long deleteSum) {
		this.deleteSum = deleteSum;
	}
	public long getModifySum() {
		return modifySum;
	}
	public void setModifySum(long modifySum) {
		this.modifySum = modifySum;
	}
	public long getOriginalSum() {
		return originalSum;
	}
	public void setOriginalSum(long originalSum) {
		this.originalSum = originalSum;
	}
	public String getDiffContent() {
		return diffContent;
	}
	public void setDiffContent(String diffContent) {
		this.diffContent = diffContent;
	}
     
}
