package com.company.serviceImpl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.bean.hdfsBean;
import com.company.service.hdfsService;

@Service("hdfsService")
public class hdfsServiceImpl implements hdfsService {

	// private File file;
	// private String fileUploadFileName;
	private String uri = "hdfs://192.168.41.128:9000";
	private String localPath = "D:/HDFS-download";

	Configuration conf = new Configuration();
	@Resource
	private hdfsBean hdfs;

	public hdfsBean getHdfs() {
		return hdfs;
	}

	@Autowired
	public void setHdfs(hdfsBean hdfs) {
		this.hdfs = hdfs;
	}

	public void mkdir(String filePath) throws IOException {
		Path path = new Path(uri + filePath);
		System.out.println("创建新目录:" + path);
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		boolean isSuccess = true;
		try {
			isSuccess = fs.mkdirs(path);
		} catch (Exception exception) {
			isSuccess = false;
		}
		System.out.println(isSuccess ? "创建目录成功" : "创建目录失败");
	}

	public void download(String filePath) throws IOException {
		Path path = new Path(uri + filePath);
		System.out.println("Download uri:" + uri);
		System.out.println("Download filepath:" + filePath);
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		fs.copyToLocalFile(false, path, new Path(localPath), true);
		fs.close();
	}

	public void delete(String filePath) throws IOException {
		Path path = new Path(uri + filePath);
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		fs.deleteOnExit(path);
		fs.close();

	}

	public List ls(String folder) throws IOException {
		Path path = new Path(uri + folder);
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		FileStatus[] list = fs.listStatus(path);
		List<hdfsBean> h = new ArrayList();
		// DateFormat dateTimeformat = new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (FileStatus f : list) {
			hdfsBean hdfs = new hdfsBean();
			hdfs.setFileName(f.getPath().toUri().getPath()); // 文件路径
			hdfs.setFileSize(f.getLen()); // 文件大小
			hdfs.setOwner(f.getOwner());
			hdfs.setModificationTime(new Date(f.getModificationTime()));
			hdfs.setPermission(f.getPermission());
			System.out.println(f.getModificationTime() + " ;" + f.getOwner()
					+ " ;" + f.getPermission());
			h.add(hdfs);
		}
		for (hdfsBean hdfss : h) {
			System.out.println("service: " + hdfss.getFileName());
		}
		fs.close();
		return h;
	}

	public void upload(String local, String hdfsPath) throws IOException {
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		fs.copyFromLocalFile(new Path(local), new Path(hdfsPath));
		System.out.println("copy from: " + local + " to " + hdfsPath);
		fs.close();
	}

	public void createFile(File local, String hdfsPath) throws IOException {
		InputStream in = null;
		try {
			FileSystem fileSystem = FileSystem.get(URI.create(hdfsPath), conf);
			FSDataOutputStream out = fileSystem.create(new Path(hdfsPath));
			in = new BufferedInputStream(new FileInputStream(local));
			System.out.println(local);
			IOUtils.copyBytes(in, out, 4096, false);
			out.hsync();
			out.close();
			System.out.println("create file in hdfs:" + hdfsPath);
		} finally {
			IOUtils.closeStream(in);
		}
	}

}
