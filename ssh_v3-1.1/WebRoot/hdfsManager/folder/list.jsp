<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'upload.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

</head>

<body>
	<div style="padding: 15px">
		<div style="margin:20px 0;">
			<span>目录:</span> <input class="easyui-validatebox" type="text"
				id="hdfsManager_list_folder" data-options="required:true"
				style="width:300px" value="/" />
				<a  class="easyui-linkbutton" iconCls="icon-reload" onclick="list_data()">确定</a>
		</div>

		<table id="dg_hdfsManager_list" class="easyui-datagrid"></table>
	</div>
	<script type="text/javascript" src="js/hdfsManager/hdfsManager_utils.js"></script>
	<script type="text/javascript" src="js/hdfsManager/folder.js" ></script>
		
</body>
</html>
