package cn.jsvnlog.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.jsvnlog.pojos.Project;
import cn.jsvnlog.service.impl.JsvnlogServiceImpl;
import cn.jsvnlog.service.impl.RepositoryServiceImpl;
import cn.jsvnlog.util.JsonUtil;
import cn.jsvnlog.util.Md5Util;
import cn.jsvnlog.vo.DifferencePointVO;
import cn.jsvnlog.vo.JRepositoryVO;

@Controller
@RequestMapping({"/svnlog"})
public class SvnlogController
{
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  JsvnlogServiceImpl jsvnlogService;

  @Autowired
  RepositoryServiceImpl repositoryService;

  @RequestMapping(value={"/createProject"}, method={org.springframework.web.bind.annotation.RequestMethod.GET, org.springframework.web.bind.annotation.RequestMethod.POST})
  @ResponseBody
  public String createProject(Project project) throws Exception { 
    this.logger.info("To allrepositorys...");
    String id = Md5Util.MD5(System.currentTimeMillis()+"");
    project.setId(id);
    project.setCreateDate(new Date());
    project.setMaxrevision(0L);
    String result = this.jsvnlogService.createSearchIndex(project);
    repositoryService.configuration(project);
    repositoryService.downloadRepositoryFromSVN(project);
    return result; 
  } 
  @RequestMapping(value={"/repository"}, method={org.springframework.web.bind.annotation.RequestMethod.GET, org.springframework.web.bind.annotation.RequestMethod.POST})
  @ResponseBody
  public String createResoitory(@RequestParam("uri") String uri, Project project) throws Exception {
    this.logger.info("To allrepositorys...");
    List<JRepositoryVO> svns = new ArrayList<JRepositoryVO>();
    if(uri.equals("")){
    	if(null ==project ||null== project.getUrl()){
        	return JsonUtil.object2Json(svns);
        }
        this.repositoryService.configuration(project);
    }
    JRepositoryVO parent = new JRepositoryVO();
    String parentId = "";
    if (uri.length()>1) {
       parentId = Md5Util.MD5(uri);
       
    }else{
    	parentId =Md5Util.MD5(project.getUrl());;
    }
    parent.setId(parentId);
    svns = (List<JRepositoryVO>) this.jsvnlogService.getChildRePositoryFromESByJRrepository(parent);
    return JsonUtil.object2Json(svns);
  }
  
  @RequestMapping(value={"/showDiff"}, method={org.springframework.web.bind.annotation.RequestMethod.GET, org.springframework.web.bind.annotation.RequestMethod.POST})
  @ResponseBody
  public String showDiffByJRepository(JRepositoryVO jrVo) throws Exception {
    this.logger.info("To showDiff...");
    System.out.println("@@@@@@@@@@@  "+jrVo.getId());
    List<DifferencePointVO> svns = new ArrayList<DifferencePointVO>();
    svns =  this.repositoryService.getAllDiffBy(jrVo);
    for (int i = 0; i < svns.size(); i++) {
    	String content = svns.get(i).getContent();
    	if(null !=content && content.length()>0){
    		content = content.replaceAll("\\n", "<br>");
    		svns.get(i).setContent(content);
    	}
	}
    return JsonUtil.object2Json(svns);
  }
  
  @RequestMapping(value={"/listProject"}, method={org.springframework.web.bind.annotation.RequestMethod.GET, org.springframework.web.bind.annotation.RequestMethod.POST})
  @ResponseBody
  public String getAllProjects() throws Exception { 
	this.logger.info("To allprojects...");
    Project project = new Project();
    List<Project> querySearchIndex = (List<Project>) this.jsvnlogService.queryAllSearchIndex(project);
    return JsonUtil.object2Json(querySearchIndex);
  }

}