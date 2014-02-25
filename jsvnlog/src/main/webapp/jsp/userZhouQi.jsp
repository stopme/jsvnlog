<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<% String path = request.getContextPath();

    String basePath = request.getScheme() + "://"

           + request.getServerName() + ":" + request.getServerPort()

           + path + "/"; %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<title>jsvnlog --repository</title>
<meta charset="UTF-8">
	<title>Basic TreeGrid - jQuery EasyUI Demo</title>
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=basePath%>css/demo.css">
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.8.0.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>js/jquery.easyui.min.js"></script>
</head>
<body>
	<h2>SVN Repository </h2>
	<div class="demo-info">
		<div class="demo-tip icon-tip"></div>
		<div>联系方式 : QQ 382827186 zero</div>
	</div>
	<div style="margin:10px 0;"></div>
	<script type="text/javascript">
	$(function() {
	      createTree();
	    });

	function createTree() {
		
		var params={};
		params.id = 1;
		params.url = "http://svn.svnkit.com/repos/svnkit/trunk/svnkit-cli";
		params.svnUserName = "anonymous";
		params.svnPassword = "anonymous";
	  $('#lizhitree').treegrid({
	    title : 'SVN Repository',
	    nowrap : false,
	    rownumbers : true,
	    collapsible : false,
	    url:'svnlog/repository.do?uri=',
	    idField : 'id',
	    treeField : 'relativeUrl',
	    queryParams :params,
	    frozenColumns : [[{
	          title : '地址',
	          field : 'relativeUrl',
	          width : 500,
	          formatter : function(value) {
	        	  var returnValue="";
					if(value.indexOf("/")>0){
						returnValue = value.substr(value.lastIndexOf("/"));
					}else{
						returnValue ="/"+value;
					}
					return '<span style="color:red">'
							+ decodeURI(returnValue) + '</span>';
	          }
	          
	        }]],
	    
	    columns : [[{
	          field : 'fileName',
	          title : '文件名称',
	          width : 120
	        }, {
	          field : 'size',
	          title : '现有代码(行)',
	          width : 120,
	          rowspan : 2
	          
	        },{
	          field : 'originalSum',
	          title : '原始代码(行)',
	          width : 80,
	          rowspan : 2
	          
	        }, {
	          field : 'revision',
	          title : '版本号',
	          width : 80,
	          rowspan : 2
	        }, {
	          field : 'kind',
	          title : '操作',
	          width : 120,
	          align : 'center',
	          rowspan : 2,
	          formatter : function(value) {
	            return value == 'file'
	                ? '<a onclick="displayAA()" style="cursor: pointer;color:red">变迁记录</a><a onclick="viewHistory()" style="margin-left:5px; cursor: pointer;color:red">历史版本</a>'
	                : '';
	          }
	          
	        }]],
	    onBeforeExpand : function(row, param) {
	      $(this).treegrid('options').url = 'svnlog/repository.do?uri=' + encodeURI(decodeURI(row.relativeUrl));
	    },
	  });
	}
</script>
   <table id="lizhitree" title="Folder Browser" class="easyui-treegrid">
		<thead>
			<tr>
				<th>地址</th>
				<th>文件名</th>
				<th>大小</th>
				<th>原始</th>
				<th>版本号</th>
				<th>操作</th>
			</tr>
		</thead>
	</table>
</html>