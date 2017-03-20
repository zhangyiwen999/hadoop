package com.company.bean;

import java.util.Date;

import org.apache.hadoop.fs.permission.FsPermission;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;


@Component("hdfsBean")
public class hdfsBean {
	

		private String remote;
		private String local;
		private String hdfsPath;
		private long fileSize;
		
		private Date modificationTime;	
		private String fileName;
		private String fileDir;
		private boolean isDir;
		private String dirName;
		private String Owner;
		private FsPermission permission;

		public FsPermission getPermission() {
			return permission;
		}
		public void setPermission(FsPermission permission) {
			this.permission = permission;
		}

		@DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
		public Date getModificationTime() {
			return modificationTime;
		}
		
		@DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
		public void setModificationTime(Date modificationTime) {
			 
			 this.modificationTime =modificationTime;
		}
		public String getOwner() {
			return Owner;
		}
		public void setOwner(String owner) {
			Owner = owner;
		}
		public String getDirName() {
			return dirName;
		}
		public void setDirName(String dirName) {
			this.dirName = dirName;
		}
		public String getRemote() {
			return remote;
		}
		public void setRemote(String remote) {
			this.remote = remote;
		}
		public String getLocal() {
			return local;
		}
		public void setLocal(String local) {
			this.local = local;
		}
		public String getHdfsPath() {
			return hdfsPath;
		}
		public void setHdfsPath(String hdfsPath) {
			this.hdfsPath = hdfsPath;
		}
		public long getFileSize() {
			return fileSize;
		}
		public void setFileSize(long fileSize) {
			this.fileSize = fileSize;
		}
		
		public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		public String getFileDir() {
			return fileDir;
		}
		public void setFileDir(String fileDir) {
			this.fileDir = fileDir;
		}
		public boolean isDir() {
			return isDir;
		}
		public void setDir(boolean isDir) {
			this.isDir = isDir;
		}
		
		
	}


