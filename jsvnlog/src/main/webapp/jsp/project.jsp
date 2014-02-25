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
<link rel="stylesheet" type="text/css"
	href="<%=basePath%>/css/jqueryui/base/jquery.ui.all.css">
<script type="text/javascript" src="<%=basePath%>js/jquery-1.8.0.min.js"></script>
<script type="text/javascript"
	src="<%=basePath%>js/jquery.easyui.min.js"></script>
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
</head>
<body>
	<h2>svn工程仓库</h2>
	<div class="demo-info">
		<div class="demo-tip icon-tip"></div>
		<div>现在不能分页</div>
	</div>
	<div style="margin:10px 0;"></div>
	<table id="dg" title="Custom DataGrid Pager" style="width:750px"
			data-options="rownumbers:true,singleSelect:true,pagination:true,url:'svnlog/listProject.do',method:'get',row:'10,20'">
		<thead>
			<tr>
				<th data-options="field:'name',width:200">工程名字</th>
				<th data-options="field:'svnUsername',width:100">用户名字</th>
				<th data-options="field:'svnPassword',width:100">用户密码</th>
				<th data-options="field:'url',width:300">仓库地址</th>
			</tr>
		</thead>
	</table>
	<script type="text/javascript">
		$(function(){
			var pager = $('#dg').datagrid().datagrid('getPager');	// get the pager of datagrid
			pager.pagination({
				  pageSize: 10,//每页显示的记录条数，默认为10  
			      pageList: [10,20,50],//可以设置每页记录条数的列表  
			      beforePageText: '第',//页数文本框前显示的汉字  
			      afterPageText: '页    共 {pages} 页',  
			      displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录',  
				buttons:[{
					iconCls:'icon-search',
					handler:function(){
						alert('search');
					}
				},{
					iconCls:'icon-add',
					handler:function(){
						alert('add');
					}
				},{  
					iconCls:'icon-edit',
					handler:function(){
						var row = $('#dg').datagrid('getSelected');
						alert(row.name);
					}
				}]
			});			
		})
	</script>
</body>
</html>