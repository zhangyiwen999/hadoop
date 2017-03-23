<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
//站点根目录
String path = request.getContextPath();
//协议名称+配置文件的服务器+配置端口+站点根目录
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<!-- 加载CSS文件 -->
<head>
<link rel="stylesheet" type="text/css" href="themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="themes/icon.css">
<link rel="stylesheet" type="text/css" href="css/demo.css">
<!-- 加载js文件 -->
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/utils.js"></script>
</head>

<body>
<title>中大校园云存储</title>
<div style="text-align=center;margin: 300 auto;font-size:56px;width: 570px;">
<p>校园分布式云存储平台</p>
</div>
<div style="margin: 700 auto;width: 600px;">
	<div id="login_window" class="easyui-window" title="用户登录" style="width:400px;"
		data-options="closable:false,collapsible:false,minimizable:false,maximizable:false,draggable:false,resizable:false,modal:false">
		<div style="padding:10px 60px 20px 60px;">
			<form id="login_ff" class="easyui-form" method="post" >
				<table cellpadding="5" align="center" >
					<tr>
						<td>邮箱:</td>
						<td><input class="easyui-textbox" type="text" name="email"
							style="height:30px;width: 200px;" value="robin@qq.com"
							data-options="validType:['email'],required:true,prompt:'email...'"></input></td>
					</tr>
					<tr>
						<td>密码:</td>
						<td><input class="easyui-textbox" type="password" name="password"
							style="height:30px;width: 200px;" value="123456"
							data-options="validType:['length[6,20]'],required:true,prompt:'password...'"></input></td>
					</tr>
				</table>
			</form>
			<div style="text-align:center;padding:5px">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="login()" iconCls="icon-man">登录</a> <a
					href="javascript:void(0)" iconCls="icon-clear"
					class="easyui-linkbutton" onclick="register()">注册</a>
			</div>
		</div>

	</div>
</div>

</body>