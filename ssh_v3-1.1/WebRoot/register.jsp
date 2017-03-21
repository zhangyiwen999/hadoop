<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<head>
<link rel="stylesheet" type="text/css" href="themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="themes/icon.css">
<link rel="stylesheet" type="text/css" href="css/demo.css">
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/utils.js"></script>
</head>
<body>
<div style="margin: 500 auto;width: 400px;">
	<div id="register_window" class="easyui-window" title="用户注册" style="width:400px;"
		data-options="closable:false,collapsible:false,minimizable:false,maximizable:false,draggable:false,resizable:false,modal:false">
		<div style="padding:10px 60px 20px 60px;">
			<form id="register_ff" class="easyui-form" method="post">
				<table cellpadding="5" align="center" >
					<tr>
						<td>用户名:</td>
						<td><input class="easyui-textbox" type="text" name="name"
							style="height:30px;width: 180px;" value="fansy"
							data-options="validType:['length[3,20]'] ,required:true,prompt:'username...'"></input></td>
					</tr>
					<tr>
						<td>邮箱:</td>
						<td><input class="easyui-textbox" type="text" name="email" id="register_eamil"
							style="height:30px;width: 180px;" value="fansy@qq.com"
							data-options="validType:['email'],required:true,prompt:'email...'"></input></td>
					</tr>
					<tr>
						<td>密码:</td>
						<td><input class="easyui-textbox" type="password" name="password"
							style="height:30px;width: 180px;" value=""
							data-options="validType:['length[3,20]'],required:true,prompt:''"></input></td>
					</tr>
				</table>
			</form>
			<div style="text-align:center;padding:5px">
				<a href="javascript:void(0)" class="easyui-linkbutton" id="registerBtn"
					 iconCls="icon-man">注册</a> 
					 <a href="login.jsp" class="easyui-linkbutton" 
					 iconCls="icon-man">登录</a> 
			</div>
		</div>

	</div>
</div>

</body>