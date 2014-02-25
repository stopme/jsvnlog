package cn.jsvnlog.pojos;
import java.util.Map;

public class DifferencePoint {

    private String repositoryURL;
    private String fileName;
    private long revisonN;
    private long revisonM;
    private String content;
    private Map<String, String> details;
    
    public Map<String, String> getDetails() {
        return details;
    }
    public void setDetails(Map<String, String> details) {
        this.details = details;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getRepositoryURL() {
        return repositoryURL;
    }
    public void setRepositoryURL(String repositoryURL) {
        this.repositoryURL = repositoryURL;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public long getRevisonN() {
        return revisonN;
    }
    public void setRevisonN(long revisonN) {
        this.revisonN = revisonN;
    }
    public long getRevisonM() {
        return revisonM;
    }
    public void setRevisonM(long revisonM) {
        this.revisonM = revisonM;
    }
    
}
