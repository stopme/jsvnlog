package cn.jsvnlog.vo;
import java.io.Serializable;
import java.util.Date;

import cn.jsvnlog.pojos.JRepository;


public class JSvnLogEntryVO implements Serializable{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 9055597780941453691L;
	private String id ;
	 private  String jRepositoryId;
	 private String commitMessage;
     private Date date;
     private String kind;//是file or dir
     private char actionType;//是添加 删除 修改
     private String name;
     private long revision;
     private long preRevision;//
     private long size;
     private String author;
     private long addSum;
     private long deleteSum;
     private long modifySum;
     private long originalSum;
     private String diffContent;
     private String projectId;
     private  String repositoryId;
     private  String relativePath;
     
	
	public String getRelativePath() {
		return relativePath;
	}
	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}
	public String getRepositoryId() {
		return repositoryId;
	}
	public void setRepositoryId(String repositoryId) {
		this.repositoryId = repositoryId;
	}
	public char getActionType() {
		return actionType;
	}
	public void setActionType(char actionType) {
		this.actionType = actionType;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	public String getjRepositoryId() {
		return jRepositoryId;
	}
	public void setjRepositoryId(String jRepositoryId) {
		this.jRepositoryId = jRepositoryId;
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
