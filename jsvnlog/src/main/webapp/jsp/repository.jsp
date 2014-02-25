<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();

	String basePath = request.getScheme() + "://"

	+ request.getServerName() + ":" + request.getServerPort()

	+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<title>jsvnlog --repository</title>
<meta charset="UTF-8">
<title>Jsvnlog repository</title>
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>css/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/icon.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>css/demo.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>/css/jqueryui/base/jquery.ui.all.css">
<script type="text/javascript" src="<%=basePath%>js/jquery-1.8.0.min.js"></script>
<script type="text/javascript"  src="<%=basePath%>js/jquery.easyui.min.js"></script>
<script src="<%=basePath%>js/jqueryui/external/jquery.bgiframe-2.1.2.js"></script>
<script src="<%=basePath%>js/jqueryui/ui/jquery.ui.core.js"></script>
<script src="<%=basePath%>js/jqueryui/ui/jquery.ui.widget.js"></script>
<script src="<%=basePath%>js/jqueryui/ui/jquery.ui.mouse.js"></script>
<script src="<%=basePath%>js/jqueryui/ui/jquery.ui.button.js"></script>
<script src="<%=basePath%>js/jqueryui/ui/jquery.ui.draggable.js"></script>
<script src="<%=basePath%>js/jqueryui/ui/jquery.ui.position.js"></script>
<script src="<%=basePath%>js/jqueryui/ui/jquery.ui.resizable.js"></script>
<script src="<%=basePath%>js/jqueryui/ui/jquery.ui.dialog.js"></script>
<script src="<%=basePath%>js/jqueryui/ui/jquery.ui.effect.js"></script>
<script src="<%=basePath%>js/jsvnlog/repository.js"></script>
<style>
input.text {
	margin-bottom: 12px;
	width: 95%;
	padding: .4em;
}

fieldset {
	padding: 0;
	border: 0;
	margin-top: 25px;
}

h1 {
	font-size: 1.2em;
	margin: .6em 0;
}

div#users-contain {
	width: 350px;
	margin: 20px 0;
}

div#users-contain table {
	margin: 1em 0;
	border-collapse: collapse;
	width: 100%;
}

div#users-contain table td,div#users-contain table th {
	border: 1px solid #eee;
	padding: .6em 10px;
	text-align: left;
}

.ui-dialog .ui-state-error {
	padding: .3em;
}

.validateTips {
	border: 1px solid transparent;
	padding: 0.3em;
}

.ui-combobox {
	position: relative;
	display: inline-block;
}

.ui-combobox-toggle {
	position: absolute;
	top: 0;
	bottom: 0;
	margin-left: -1px;
	padding: 0;
	/* adjust styles for IE 6/7 */
	*height: 1.7em;
	*top: 0.1em;
}

.ui-combobox-input {
	margin: 0;
	padding: 0.3em;
}
select  {
	font-size: 14px;
	font-family: 微软雅黑;
}
.treeSpace input{
    font-size: 20px;
	font-family: 微软雅黑;
}
</style>
<script type="text/javascript">
$(function(){
	$("#selectProject").combobox({
		url:'svnlog/listProject.do',
		valueField:'id',
		textField:'name',
	    formatter: function(row){
		   var s = '<span style="font-weight:bold;font-size: 14px;font-family: 微软雅黑;">' + row.name + '</span><br/>' +
			'<span style="color:#888">' + row.url + '</span>';
	      return s;
	   },
	   onSelect : function(record){
			var params={};
			params.id = record.id;
			params.url = record.url;
			params.svnUserName = record.svnUserName;
			params.svnPassword = record.svnPassword;
			// $('#lizhitree').treegrid('load', params);
			$.post('svnlog/repository.do?uri=', params, function(data) {
		        $('#lizhitree').treegrid('loadData', data);
		      }, 'json');
		}	
		});
	}
  );
	</script>
</head>
<body>
	<h2>SVN Repository</h2>
	<div class="demo-info">
		<div class="demo-tip icon-tip"></div>
		<div>暂不统计后缀为dic，class，txt，doc，docx，sql，jpg，jar，gif，png</div>
	</div>
	<div style="margin: 10px 0;"></div>
	<div style="width: 1000px;" class='treeSpace'>
		<div align='right' style="margin: 10px 0;"></div>
		<div align='right' style="margin: 10px 0;">
			<input id="selectProject" name="project" value="" style="width: 200px;height: 28px;border:none;">
			<button id="create-project">添加SVN仓库</button>
		</div>
		
		<div>
			<table id="lizhitree" title="Folder Browser" class="easyui-treegrid"
				style="width: 1000px;">
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
		</div>
	</div>
	<div id="dialog-form" title="添加svn仓库">
	<div id="dialog-modal" title="消息框"></div>
	<div class="demo-info">
		<div class="demo-tip icon-tip"></div>
		<div class="validateTips">请填写下列项目,如没有密码可以留空.</div>
	</div>
		<form id="createProjectFrom">
			<fieldset>
				<label for="name" >svn工程名</label> 
				<input type="text" name="name" id="name" class="text ui-widget-content ui-corner-all" title="svn工程名"/> 
				<label for="url" >svn地址</label> 
				<input type="text" name="url" id="url" class="text ui-widget-content ui-corner-all" title="svn地址"/>
				<label for="svnUsername" >svn用户名</label> 
				<input type="text" name="svnUsername" id="svnUsername" class="text ui-widget-content ui-corner-all" title="svn用户名"/> 
				<label for="svnPassword">svn密码</label> 
				<input type="text" name="svnPassword" id="svnPassword"  class="text ui-widget-content ui-corner-all" />
			</fieldset>
		</form>
	</div>
	<div id="dlg" class="easyui-dialog" title="diff变迁" data-options="" style="width:400px;height:600px ;padding:10px ;">
	<table id="showDiffData"  style="width:750px">
		<thead>
			<tr>
				<th>对比版本号</th>
				<th>变更内容</th>
			</tr>
		</thead>
	</table>
	</div>
	<script type="text/javascript">
	$('#dlg').dialog({
	  autoOpen : false,
      width: 750,
      noheader : true,
      cache: false,
      modal: true
    });
	</script>
	<div style="display: none; left: 373px; top: 48.5px;" class="datagrid-mask-msg">程序运行中, 请耐心等待 ...</div>
	</body>
</html>