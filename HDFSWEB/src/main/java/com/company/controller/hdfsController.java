package com.company.controller;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.company.bean.hdfsBean;
import com.company.service.hdfsService;



@Controller
@RequestMapping("/hdfs")
public class hdfsController {
	
	
	
	private hdfsService hdfsService;	
	public hdfsService getHdfsService() {
		return hdfsService;
	}
    @Resource
	public void setHdfsService(hdfsService hdfsService) {
		this.hdfsService = hdfsService;
	}

	@RequestMapping("/delete")  
    public String toDelete(@RequestParam(value="path",required=false) String path)
       throws IOException{  
		  hdfsService.delete(path);          	
          return "success"; 		
}


	 @RequestMapping(value="/ls",method=RequestMethod.GET)
	    public ModelAndView home(@RequestParam(value="path",required=false) String path, HttpServletRequest request, HttpServletResponse response) 
	       throws Exception {
		    ModelAndView model = new ModelAndView();
	        if (StringUtils.isEmpty(path)) {
	            path = "/";
	        } 
			List<hdfsBean> hdfsFiles =hdfsService.ls(path);
	        model.addObject("file", hdfsFiles);
	        model.setViewName("/ls");
	        return model;
	    }
	 
	 
	 @RequestMapping("/download")  
	    public String toDownload(@RequestParam(value="path",required=false) String path)
	       throws IOException{  
			 hdfsService.download(path);          	
	          return "success";  		
	}
	 
	 
	 @RequestMapping(value="/upload")
	 public String upLoad(HttpServletRequest request, HttpServletResponse response) 
	            throws IllegalStateException, IOException{
	        //解析器解析request的上下文
	        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext()); 
	        //先判断request中是否包涵multipart类型的数据，
	        if(multipartResolver.isMultipart(request)) {
	            //再将request中的数据转化成multipart类型的数据
	            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
	            Iterator<String> iter = multiRequest.getFileNames();
	            while(iter.hasNext()) {
	                MultipartFile file = multiRequest.getFile(iter.next());
	                if(file != null) {
	                	String FileName = file.getOriginalFilename();
	                	System.out.println(FileName);
	                    CommonsMultipartFile cf= (CommonsMultipartFile)file; 
	                    DiskFileItem fi = (DiskFileItem)cf.getFileItem(); 
	                    File inputFile = fi.getStoreLocation();
	                    hdfsService.createFile(inputFile, "hdfs://192.168.12.232:9000/upload/"+FileName);                                       
	                }
	            }
	        }
	        return "success";
	    }
	    
	 
	 
	 
}