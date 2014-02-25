package cn.jsvnlog.service.impl;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import cn.jsvnlog.pojos.JRepository;
import cn.jsvnlog.pojos.Project;
import cn.jsvnlog.util.Md5Util;
import cn.jsvnlog.util.PrefixUtil;
import cn.jsvnlog.vo.DifferencePointVO;
import cn.jsvnlog.vo.JRepositoryVO;
import cn.jsvnlog.vo.JSvnLogEntryVO;

@Service("repositoryService")
public class RepositoryServiceImpl {
	
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String svnRoot;

    private String userName;

    private String password;

    private SVNRepository repository;
    @Autowired
    JsvnlogServiceImpl jsvnlogService;

    public RepositoryServiceImpl(){
    	
    }
    public RepositoryServiceImpl(String svnRoot, String userName, String password) {

        this.svnRoot = svnRoot;

        this.userName = userName;

        this.password = password;

    }

    private static void setupLibrary() {

        // 对于使用http://和https：//

        DAVRepositoryFactory.setup();

        // 对于使用svn：/ /和svn+xxx：/ /

        SVNRepositoryFactoryImpl.setup();

        // 对于使用file://

        FSRepositoryFactory.setup();

    }

    /***
     * 
     * 登录验证
     * 
     * @return
     * @throws SVNException 
     */

    public void configuration(Project project) throws SVNException{
    	     
    	     setupLibrary();
    	     String svnRoot = project.getUrl();
    		 String userName = project.getSvnUsername();
    		 String password = project.getSvnPassword();
             // 创建库连接
             repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(svnRoot));
             // 身份验证
             ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(userName, password);
             // 创建身份验证管理器
             repository.setAuthenticationManager(authManager);
    }
    public boolean login() {
        setupLibrary();
        try {
            // 创建库连接
            repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(this.svnRoot));
            // 身份验证
            ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(this.userName, this.password);
            // 创建身份验证管理器
            repository.setAuthenticationManager(authManager);
            return true;

        } catch (SVNException svne) {
            svne.printStackTrace();
            return false;
        }

    }

    @SuppressWarnings("rawtypes")
    public List<JRepository> listEntries(String path) {
	       Collection entries;
		try {
			entries = repository.getDir(path, -1, null,(Collection) null);
			Iterator iterator = entries.iterator();
		       List<JRepository> svns = new ArrayList<JRepository>();
		       while (iterator.hasNext()) {
		           SVNDirEntry entry = (SVNDirEntry) iterator.next();
		           JRepository node = new JRepository();
		           node.setKind(entry.getKind().toString());
		           String str = path.equals("") ? entry.getName() : path + "/"+entry.getName();
		           str = Md5Util.MD5(str);
		           node.setFileName(entry.getName());
		           node.setRepositoryRoot(entry.getRepositoryRoot().toString());
		           node.setRevision(entry.getRevision());
		           node.setSize(entry.getSize()/1024);
		           node.setRelativeUrl(path + "/"+entry.getName());
		           node.setId(str);
		           node.setState(node.getKind() == "file"?null:"closed");
		           svns.add(node);
		       }
		       return svns;
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	 }

	public void downloadRepositoryFromSVN(Project project)  {
		// TODO Auto-generated method stub
		//1.存svn仓库的根节点
		JRepositoryVO root = new JRepositoryVO();
		String rootId = Md5Util.MD5(project.getUrl());
		root.setId(rootId);
		root.setKind("dir");
		root.setFileName("root");
		root.setState("closed");
		root.setRepositoryRoot(project.getUrl());
		root.setRelativeUrl("");
		root.setRoot("root");
		if(project.getMaxrevision()<1){
			System.out.println("Maxrevision<1  开始创建svn仓库的跟节点");
			try {
				jsvnlogService.createSearchIndex(root);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//初始化数据，把svn上的节点下载到es中有时候日志显示不全所以要构建svn仓库
			initRepository(root);
		}
		 updateLogEntries(project);
		 analysisDiff(project);
		
 }
	private void initLogentries(JRepositoryVO root) {
		// TODO Auto-generated method stub
		long startRevision = 0;
		long endRevision = -1;
		Collection logEntries = null;
		String relativePath = root.getRelativeUrl();
		try {
			logEntries = repository.log(new String[]{relativePath}, null, startRevision, endRevision, true, true);
			for (Iterator entries = logEntries.iterator(); entries.hasNext();) {
	            SVNLogEntry logEntry = (SVNLogEntry) entries.next();
	                	 JSvnLogEntryVO jlog = new JSvnLogEntryVO();
				         jlog.setAuthor(logEntry.getAuthor());
				         jlog.setCommitMessage(logEntry.getMessage());
				         jlog.setDate(logEntry.getDate());
				         jlog.setRevision(logEntry.getRevision());
	                     System.out.print ("日志路径 "+relativePath);
	                     System.out.print (logEntry.getRevision()+" "+logEntry.getMessage());
	                     jlog.setRelativePath(relativePath);
	                     String jrlogId  = Md5Util.MD5(relativePath);
	                     String jlogId = Md5Util.MD5(System.currentTimeMillis()+"");
	                     jlog.setId(jlogId);
	                     jlog.setRepositoryId(jrlogId);
	                     jsvnlogService.createSearchIndex(jlog);
	                    
		  	}
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
	}
	public void analysisDiff(Project project) {
		System.out.println("开始分析日志");
		JRepositoryVO root = new JRepositoryVO();
		String rootId = Md5Util.MD5(project.getUrl());
		root.setId(rootId);
		try {
			root = (JRepositoryVO) jsvnlogService.getOneIndexById(root);
			System.out.println("从根节点开始分析  "+root.getId());
			doDiff(root);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void doDiff(JRepositoryVO parent){
		List<JRepositoryVO> children = null;
		try {
			children  = (List<JRepositoryVO>) jsvnlogService.getChildRePositoryFromESByJRrepository(parent);
			for (JRepositoryVO aChild : children ) {
				String kind = aChild.getKind();
				if(kind.equals("file")){
			    List<DifferencePointVO> allDiff = getAllDiffBy(aChild);
			    long add = 0;
                long delete = 0;
                long modify = 0;
			    for (DifferencePointVO oneDiff : allDiff) {
			    	  List<DiffHandler> diffHandlers = new ArrayList<DiffHandler>();
			    	  tongJiOneDiff(oneDiff,diffHandlers);
			    	  long addtemp=0;
                      long deletetemp=0;
                      long modifytemp=0;
                      for (DiffHandler handler : diffHandlers) {
                    	  add +=handler.getAdd();
                          addtemp += handler.getAdd();
                          delete +=handler.getDelete();
                          deletetemp  +=handler.getDelete();
                          modify += handler.getModify();
                          modifytemp += handler.getModify();
                      }
                      
                      JSvnLogEntryVO JSvnLogEntryVO = oneDiff.getjSvnLogEntryVO();
                      JSvnLogEntryVO.setAddSum(addtemp);
                      JSvnLogEntryVO.setModifySum(modifytemp);
                      JSvnLogEntryVO.setDeleteSum(deletetemp);
                      jsvnlogService.updateSearchIndex(JSvnLogEntryVO);
				}
			    if(allDiff.size()>0){
			    	 long oriRevision = allDiff.get(0).getjSvnLogEntryVO().getRevision();
			    	 String oriContent = checkFile(aChild.getRelativeUrl(), oriRevision);
			    	 String latestContent = checkFile(aChild.getRelativeUrl(), -1);
			    	 long oriSum = 0L;
					 long latestSum = 0L;
			    	 if(null !=oriContent){
			    		 oriSum  =oriContent.split("\\n").length;
			    	 }
			    	 if(null!=latestContent){
			    		 latestSum = latestContent.split("\\n").length;
			    	 }
					 aChild.setAddSum(add);
					 aChild.setModifySum(modify);
					 aChild.setDeleteSum(delete);
					 aChild.setOriginalSum(oriSum);
					 aChild.setLatestSum(latestSum);
					 jsvnlogService.updateSearchIndex(aChild);
			    }
//			    parent.setAddSum(add+parent.getAddSum());
//			    parent.setModifySum(add+parent.getModifySum());
//			    parent.setDeleteSum(add+parent.getDeleteSum());
//			    parent.setOriginalSum(aChild.getOriginalSum());
//				parent.setLatestSum(aChild.getLatestSum());
				}else{
					//如果是dir则统计其下file
					doDiff(aChild);
					
				}
				parent.setAddSum(aChild.getAddSum()+parent.getAddSum());
				parent.setDeleteSum(aChild.getDeleteSum()+parent.getDeleteSum());
				parent.setModifySum(aChild.getModifySum()+parent.getModifySum());
				parent.setOriginalSum(parent.getOriginalSum()+aChild.getOriginalSum());
				parent.setLatestSum(parent.getLatestSum()+aChild.getLatestSum());
			}
			 jsvnlogService.updateSearchIndex(parent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void tongJiOneDiff(DifferencePointVO diff,List<DiffHandler> diffHandlers){
		String contentDiff = diff.getContent();
		if(null != contentDiff){
			String[] contentDiffArray = contentDiff.split("\\n");
			if (contentDiffArray.length > 0) {

	            for (int i = 0; i < contentDiffArray.length; i++) {
	                String line = contentDiffArray[i];
	                if (line.startsWith("@@")) {
	                    Map<Integer, String> oldSvn = new HashMap<Integer, String>();
	                    Map<Integer, String> newSvn = new HashMap<Integer, String>();
	                    boolean doWhile = true;
	                    int addKey = 0;
	                    int deleteKey = 0;
	                    while (doWhile && i + 1 < contentDiffArray.length) {
	                        i++;
	                        String value = contentDiffArray[i];
	                        if(value.indexOf("\\ No newline at end of file")==0){
	                            continue;
	                        }
	                        if (value.startsWith("@@")) {
	                            i--;
	                            doWhile = false;
	                        } else if (value.startsWith("-")) {
	                            deleteKey++;
	                            oldSvn.put(deleteKey, value);
	                        } else if(value.startsWith("+")) {
	                            addKey++;
	                            newSvn.put(addKey, value);
	                        } else {
	                            deleteKey++;
	                            oldSvn.put(deleteKey, value);
	                            addKey++;
	                            newSvn.put(addKey, value);
	                        }

	                    }
	                    DiffHandler compareSVN = new DiffHandler(oldSvn, newSvn);
	                    diffHandlers.add(compareSVN);
	                }
	            }
	        
			}
		
		}
	}
	private void updateTongjiDaiMaLiang(JRepositoryVO aChild){
//		aChild.setAddSum(addSum);
		
	}
	private void initRepository(JRepositoryVO jr){
		
	    String path = jr.getRelativeUrl();
	    if(path.startsWith("/")){
	    	path = path.substring(1);	    
	       }
		    Collection entries;
			try {
				entries = repository.getDir(path, -1, null, (Collection) null);
				 Iterator iterator = entries.iterator();
			        List<JRepositoryVO> svns = new ArrayList<JRepositoryVO>();
			        while (iterator.hasNext()) {
			            SVNDirEntry entry = (SVNDirEntry) iterator.next();
			            JRepositoryVO jRepositoryVO = new JRepositoryVO();
			            jRepositoryVO.setRelativeUrl(path.equals("") ? entry.getName() : path + "/" + entry.getName());
			            String rPath = jRepositoryVO.getRelativeUrl();
	                    if(PrefixUtil.getRight(rPath)){
	                	   String id = Md5Util.MD5(jRepositoryVO.getRelativeUrl());
				            jRepositoryVO.setId(id);
				            jRepositoryVO.setParentId(jr.getId());	
				            jRepositoryVO.setKind(entry.getKind().toString());
				            jRepositoryVO.setFileName(entry.getName());
				            jRepositoryVO.setRepositoryRoot(jr.getRepositoryRoot());
				            jRepositoryVO.setState(jRepositoryVO.getKind() == "file" ? null : "closed");
				            svns.add(jRepositoryVO);
				            System.out.println("创建节点 = " +jRepositoryVO.getRelativeUrl());
				            jsvnlogService.createSearchIndex(jRepositoryVO);
			        	}
			            
//			            if(entry.getKind() == SVNNodeKind.FILE){
//			            	 initLogentries(svn);
//			            }
			            if (entry.getKind() == SVNNodeKind.DIR) {
			                initRepository(jRepositoryVO);
			            }
			           
			     }
			} catch (SVNException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	       
	}
	//通过svn日志更新es上的svn数据仓库
	private void updateRepositoryBySvnLog(JRepositoryVO jr){
		JRepositoryVO ajJRepositoryVO = null;
		try {
			ajJRepositoryVO = (JRepositoryVO) jsvnlogService.getOneIndexById(jr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(null == ajJRepositoryVO){
			String jrRelativePath = jr.getRelativeUrl();
			String relativePath = jrRelativePath.substring(0, jrRelativePath.lastIndexOf("/"));
			JRepositoryVO jrvoParent = new JRepositoryVO();
			jrvoParent.setAddDate(jr.getAddDate());
			jrvoParent.setFileName(relativePath.substring(relativePath.lastIndexOf("/")+1));
        	String jrlogId  = Md5Util.MD5(relativePath);
        	jrvoParent.setId(jrlogId);
        	jrvoParent.setKind("dir");
        	jrvoParent.setRelativeUrl(relativePath);
        	jrvoParent.setRepositoryRoot(jr.getRoot());
        	jr.setParentId(jrlogId);
        	try {
				jsvnlogService.createSearchIndex(jr);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	if(relativePath.indexOf("/")>0){
        		updateRepositoryBySvnLog(jrvoParent);
        	}
        	
		}
		
	}
	//这里创建svnlog日志记录
	public void updateLogEntries(Project project){
		
		System.out.println("创建仓库结束 。。。。 开始更新log.........");
		long startRevision = project.getMaxrevision();
		long endRevision = 0;
		try {
			endRevision = repository.getLatestRevision();
		} catch (SVNException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(startRevision < endRevision){
			Collection logEntries = null;
			project.setMaxrevision(endRevision);
			try {
				logEntries = repository.log(new String[]{}, null, startRevision, endRevision, true, true);
			} catch (SVNException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("得到的日志条数  = " +logEntries.size());
				 for (Iterator entries = logEntries.iterator(); entries.hasNext();) {
			            SVNLogEntry logEntry = (SVNLogEntry) entries.next();
			            if (logEntry.getChangedPaths().size() > 0) {
			                Set changedPathsSet = logEntry.getChangedPaths().keySet();
			                for (Iterator changedPaths = changedPathsSet.iterator(); changedPaths.hasNext();) {
			                    SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry.getChangedPaths().get(changedPaths.next());
			                	String rPath = entryPath.getPath();
			                    if(PrefixUtil.getRight(rPath)){
			                    	 JSvnLogEntryVO jlog = new JSvnLogEntryVO();
							         jlog.setAuthor(logEntry.getAuthor());
							         jlog.setCommitMessage(logEntry.getMessage());
							         jlog.setDate(logEntry.getDate());
							         jlog.setRevision(logEntry.getRevision());
				                
				                    String rootPath = project.getUrl();
				                    String rootES[] = rootPath.split("/");
				                    String rootName = rootES[rootES.length-1];
				                    String relativePath = entryPath.getPath();
				                   
				                    if(!rootName.equals("trunk")){
				                    	if(relativePath.indexOf("/"+rootName+"/")<0){
				                    		continue;
				                    	}
				                    }
				                    System.out.println("日志对应的文件 : ");
				                    System.out.print ("原始 路径"+relativePath);
				                    relativePath = relativePath.substring(relativePath.indexOf(rootName)+rootName.length()+1, relativePath.length());
				                    System.out.println("相对路径"+relativePath);

				                    JRepositoryVO jrvo = new JRepositoryVO();
				                    jrvo.setAddDate(logEntry.getDate());
				                    jrvo.setFileName(relativePath.substring(relativePath.lastIndexOf("/")+1));
			                    	String jrvoId  = Md5Util.MD5(relativePath);
			                    	jrvo.setId(jrvoId);
			                    	jrvo.setKind(entryPath.getKind().toString());
			                    	jrvo.setRelativeUrl(relativePath);
			                    	jrvo.setRepositoryRoot(project.getUrl());
				                    if(entryPath.getType()=='A'||entryPath.getType()=='D'){
					                   try {
										updateRepositoryBySvnLog(jrvo);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
				                    }
				                    if(entryPath.getKind()==SVNNodeKind.FILE){
				                    	jlog.setActionType(entryPath.getType());
				                    	String jlogId = Md5Util.MD5(System.currentTimeMillis()+"");
					                    jlog.setId(jlogId);
					                    jlog.setRepositoryId(jrvoId);
//					                    jlog.setRelativePath(relativePath);
				                    	try {
											jsvnlogService.createSearchIndex(jlog);
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
				                    }
			                    }
//			                    if(entryPath.getKind()==SVNNodeKind.DIR)
//			                    System.out.println(" "+ entryPath.getType() + "	"+ entryPath.getPath()+ ((entryPath.getCopyPath() != null) ? " (from "
//			                                    + entryPath.getCopyPath() + " revision "
//			                                    + entryPath.getCopyRevision() + ")" : ""));
			                }
			            }
				  	}
			
			project.setMaxrevision(endRevision);
			try {
				jsvnlogService.updateSearchIndex(project);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 得到一个节点所有版本的对比信息
	 * 例如版本为，1,2,3
	 * 则得到0和1,1和2,2和3的对比的diff文件信息
	 * @param jrvo
	 * @return
	 */
	public List<DifferencePointVO> getAllDiffBy(JRepositoryVO jrvo){
		setupLibrary();
		String url = jrvo.getRepositoryRoot();
		 SVNURL repositoryURL = null;
	        try {
	            repositoryURL = SVNURL.parseURIEncoded(url);
	        } catch (SVNException e) {
	            //
	        }
	        ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
	        SVNClientManager ourClientManager = SVNClientManager.newInstance((DefaultSVNOptions) options, this.userName,this.password);
	        List<JSvnLogEntryVO> jsVos = new ArrayList<JSvnLogEntryVO>();
	        List<DifferencePointVO> returnValue = new ArrayList<DifferencePointVO>();
	        Map<String, String> params = new HashMap<String, String>();
	        params.put("repositoryId", jrvo.getId());
	        List<JSvnLogEntryVO> jsVoList = null;
			try {
				//按照正序得到对应的所有版本
				jsVoList = (List<JSvnLogEntryVO>) jsvnlogService.getAllLogsFromESByParams(params,JSvnLogEntryVO.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        if(null == jsVoList || jsVoList.size()<1){
	        	return returnValue;
	        }
	        {
	        	JSvnLogEntryVO first = new JSvnLogEntryVO();
	        	first.setRevision(0);
	        	jsVos.add(first);
	        	jsVos.addAll(jsVoList);
	        }
	        System.out.println("@@@@  "+jsVos.size());
	        for (int i = 0; i+1 < jsVos.size(); i++) {
	        	DifferencePointVO diVo = new DifferencePointVO();
	        	diVo.setjRepositoryVO(jrvo);
	        	diVo.setjSvnLogEntryVO(jsVos.get(i+1));
	        	diVo.setRevisonM(jsVos.get(i).getRevision());
	        	diVo.setRevisonN(jsVos.get(i+1).getRevision());
	        	doDiff(diVo, ourClientManager);
	        	returnValue.add(diVo);
			}
		return returnValue;
	}
	private DifferencePointVO doDiff(DifferencePointVO divo, SVNClientManager ourClientManager){
//		
        String url = divo.getjRepositoryVO().getRepositoryRoot();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SVNDiffClient diffClient = ourClientManager.getDiffClient();
        String diffUrl = url + "/" + divo.getjRepositoryVO().getRelativeUrl();
        System.out.println("分析日志 "+diffUrl);
        long m = divo.getRevisonM();
        long n = divo.getRevisonN();
        System.out.println(m +" vs "+n);
        try {
			diffClient.doDiff(SVNURL.parseURIEncoded(diffUrl), SVNRevision.create(-1), SVNRevision.create(m), SVNRevision.create(n), SVNDepth.UNKNOWN, false, baos);
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String content = baos.toString();
        if (content.length() > 1) {
        	divo.setContent(content);
        }
        try {
			baos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return divo;
    }
	public String checkFile(String path,long revision){
		SVNProperties fileProperties = new SVNProperties();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String content = null;
        if(revision<1){
        	revision =  -1;
        }
        try {
            repository.getFile(path, revision, fileProperties, baos);
            content = baos.toString();
            System.out.println( "得到的源码 ： "+content);
            baos.close();
        } catch (SVNException e) {
            logger.error("error while fetching the file contents and properties: " + e.getMessage());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return content;
		
	}
}
