package cn.jsvnlog.vo;
import java.util.Map;

public class DifferencePointVO {
	
	private JRepositoryVO jRepositoryVO;
	private JSvnLogEntryVO jSvnLogEntryVO;
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
    
    public JRepositoryVO getjRepositoryVO() {
		return jRepositoryVO;
	}
	public void setjRepositoryVO(JRepositoryVO jRepositoryVO) {
		this.jRepositoryVO = jRepositoryVO;
	}
	
	public JSvnLogEntryVO getjSvnLogEntryVO() {
		return jSvnLogEntryVO;
	}
	public void setjSvnLogEntryVO(JSvnLogEntryVO jSvnLogEntryVO) {
		this.jSvnLogEntryVO = jSvnLogEntryVO;
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
