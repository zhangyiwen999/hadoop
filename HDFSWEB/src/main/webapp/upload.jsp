<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">  
<html>  
<head>  
</head>  
<body>  
    <h3>上传文件 :</h3>
    选择本地需要上传的文件路径 :<br />
    <form action="hdfs/upload.do" method="post" enctype="multipart/form-data">
        <input type="file" name="file" size="50" />
        <br/>
    <input type="submit" value="上传" />
    <input type="button" value="返回主页"   onclick="javascript:window.location='http://localhost:8080/HDFSWEB/'" >
    </form> 
</body>  
</html>