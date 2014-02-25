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
	<h2>Custom Format in ComboBox</h2>
	<div class="demo-info" style="margin-bottom:10px">
		<div class="demo-tip icon-tip"></div>
		<div>This sample shows how to custom the format of list item.</div>
	</div>
	<input class="easyui-combobox" name="language" data-options="
				url: 'jsp/combobox_data1.json',
				valueField: 'id',
				textField: 'text',
				panelWidth: 350,
				panelHeight: 'auto',
				formatter: formatItem
			">
	<script type="text/javascript">
		function formatItem(row){
			var s = '<span style="font-weight:bold">' + row.text + '</span><br/>' +
					'<span style="color:#888">' + row.desc + '</span>';
			return s;
		}
	</script>
	
	
	<div style="margin:10px 0;">
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#dlg').dialog('open')">Open</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#dlg').dialog('close')">Close</a>
	</div>
	<div id="dlg" class="easyui-dialog" title="Basiclizhi Dialog" data-options="href:'svnlog/listProject.do'," style="width:400px;height:200px;padding:10px ;">
		The dialog content.
	</div>
	
</body>
</html>