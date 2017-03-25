<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>校园分布式云存储平台</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<!-- 加载CSS文件 -->
<link rel="stylesheet" type="text/css" href="themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="themes/icon.css">
<link rel="stylesheet" type="text/css" href="css/demo.css">
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/utils.js"></script>
</head>

<body class="easyui-layout" style="width:100%;height:100%;">

	<!-- 主体界面 -->
	<div data-options="region:'north'" style="height:80px;padding-top: 5px">
		<table align="center" border="0" width="100%">
			<tr style="font-size: 30px;text-align: center;font-weight: bold;"><td>
				校园分布式云存储平台</td>
		</tr>
		<!-- <p style="text-align: right;padding-right: 180px;font-size: 13px;padding-top: 0px"> -->
		<tr style="font-size: 13px;text-align: right;">
		<td>
			用户名：
			<s:property value="#session.user" />
			<s:if test='#session.user==null'>
				<script type="text/javascript">
    			  window.location.href="login.jsp";
    			  </script>
			</s:if>
			<a href="javascript:void(0)" id="mb" class="easyui-menubutton"
				data-options="menu:'#mm',iconCls:'icon-help'">帮助</a>
		</td>
		</tr>
		</table>
			<div id="mm" style="width:150px; ">
				<div data-options="iconCls:'icon-man'" onclick="logout()">注销用户</div>
			</div>
	</div>
	<!-- <div data-options="region:'south',split:true,collapsed:true"
		style="height:50px;"></div> -->
	<div data-options="region:'west',split:true" title="菜单栏"
		style="width:180px;">
		<ul class="easyui-tree" id="eastTree"
			data-options="url:'json/hdfsManager.json',method:'get',animate:true,dnd:true"></ul>
	</div>
	<!--  
	<div data-options="region:'west',split:true,collapsed:true" title="项目"
		style="width:100px;">
		<div class="easyui-accordion" data-options="fit:true,border:false">
			<div title="HDFS" style="padding:10px;">
				<a onclick="update_project('HDFS文件管理','json/hdfsManager.json')"
					class="easyui-linkbutton">HDFS文件管理系统</a>
			</div>
			<div title="博客推荐系统" data-options="selected:true"
				style="padding:10px;">
				<div title="HDFS" style="padding:10px;">
					<a onclick="update_project('博客推荐系统：未完成','json/tree_data1.json')"
						class="easyui-linkbutton">博客推荐系统</a>
				</div>
			</div>
			<div title="待续" style="padding:10px">待续</div>
		</div>
	</div>
	-->
	<div id="centerTitleId"
		data-options="region:'center',title:'Hadoop文件管理',iconCls:'icon-ok'">
		<div id="centerTab" class="easyui-tabs"
			data-options="fit:true,border:false,plain:true">
			<div title="毕设简介" data-options="href:'about.jsp'"
				style="padding:10px"></div>

		</div>
	</div>
	</div>
</body>
</html>
