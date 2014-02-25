package cn.jsvnlog.pojos;

import java.util.Date;
import java.util.List;

public class JRepository {

	private String id;
	private String parentId;
	private String repositoryRoot;//知识库的根目录
	private String relativeUrl;//所在目录路径
	private String fileName;//文件名称
    private String kind;//文件类型是文件夹还是文件
    private String mimyType;//文件类型是否是代码文件
    private long   size=0;
    private long   originalSum=0;
    private long   latestSum; 
    private String state; 
    private long   revision;
    private long   addSum=0;
    private long   deleteSum=0;
    private long   modifySum=0;
    private Date   addDate;
    private boolean   root;
    
    
    public boolean isRoot() {
		return root;
	}
	public void setRoot(boolean root) {
		this.root = root;
	}
	private List<JSvnLogEntry> jSvnLogEntrys;
    
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getRepositoryRoot() {
		return repositoryRoot;
	}
	public void setRepositoryRoot(String repositoryRoot) {
		this.repositoryRoot = repositoryRoot;
	}
	
	
	public String getRelativeUrl() {
		return relativeUrl;
	}
	public void setRelativeUrl(String relativeUrl) {
		this.relativeUrl = relativeUrl;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getMimyType() {
		return mimyType;
	}
	public void setMimyType(String mimyType) {
		this.mimyType = mimyType;
	}
	public long getOriginalSum() {
		return originalSum;
	}
	public void setOriginalSum(long originalSum) {
		this.originalSum = originalSum;
	}
	public long getLatestSum() {
		return latestSum;
	}
	public void setLatestSum(long latestSum) {
		this.latestSum = latestSum;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public long getRevision() {
		return revision;
	}
	public void setRevision(long revision) {
		this.revision = revision;
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
	public Date getAddDate() {
		return addDate;
	}
	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}
	public List<JSvnLogEntry> getjSvnLogEntrys() {
		return jSvnLogEntrys;
	}
	public void setjSvnLogEntrys(List<JSvnLogEntry> jSvnLogEntrys) {
		this.jSvnLogEntrys = jSvnLogEntrys;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
    

}
