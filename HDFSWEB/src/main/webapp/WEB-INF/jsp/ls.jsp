<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false" %> 
<%String ref = request.getHeader("REFERER");%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<body>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<table border="1" align="center">
	<tr>
		<td>文件名</td> 
		<td>文件大小  </td>
		<td>拥有者 </td>
		<td>修改时间  </td>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;操作</td>
	</tr>
     <c:forEach var="file" items="${file}">
     <tr>
			 <td><a href="${pageContext.request.contextPath}/hdfs/ls.do?path=${file.fileName}" style="color:#666666;text-decoration:none;">
			      ${file.fileName}</a></td>
			 <td> ${file.getFormatFileSize()}</td>	
			 <td> ${file.owner}</td>
			 <td> ${file.modificationTime}</td>	
			 <td>
			 <a href="${pageContext.request.contextPath}/hdfs/mkdir.do?path=${file.fileName}" style="color:#0000ff;">
			 创建目录</a>
			 <a href="${pageContext.request.contextPath}/hdfs/delete.do?path=${file.fileName}" style="color:#0000ff;"
			  class="button border-dot button-little" onclick="return confirm('确认删除?')" >
			                   删除  </a>
			 &nbsp;&nbsp;
			 <a href="${pageContext.request.contextPath}/hdfs/download.do?path=${file.fileName}" style="color:#0000ff;">
			 下载</a></td> 
    </tr>
	</c:forEach>
</table>
 
<div style="text-align: center" >
 <!--<input type="button" value="返回"   onclick="javascript:window.location='<%=ref%>'" >-->
 <a href="javascript:window.location='<%=ref%>'" style="color:#0000ff;">
 返回</a>
<a href="javascript:window.location='http://localhost:8080/HDFSWEB/'" style="color:#0000ff;">
 返回主页</a>
 </div>
</body>
</html>