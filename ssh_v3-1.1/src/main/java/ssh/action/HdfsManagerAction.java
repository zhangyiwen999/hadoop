package ssh.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;

import org.apache.hadoop.ipc.RemoteException;
import org.apache.hadoop.security.AccessControlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ssh.model.HdfsRequestProperties;
import ssh.model.HdfsResponseProperties;
import ssh.service.HdfsService;
import ssh.util.HadoopUtils;
import ssh.util.Utils;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import demo.dfs.SensitiveWordFilter;

/**
 * HDFS 文件管理系统Action 每个方法前加入权限判断
 * 
 * @author fansy
 * 
 */
@Resource(name = "hdfsManagerAction")
public class HdfsManagerAction extends ActionSupport implements
		ModelDriven<HdfsRequestProperties> {
	private static final Logger log = LoggerFactory
			.getLogger(HdfsManagerAction.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HdfsRequestProperties hdfsFile = new HdfsRequestProperties();
	private HdfsService hdfsService;
	private int rows;
	private int page;

	private File file;

	private String fileFileName;

	private String fileContentType;

	@Override
	public HdfsRequestProperties getModel() {
		return hdfsFile;
	}

	/*
	 * 获取密钥
	 */
	public Key getKey(String strKey) {
		Key key = null;
		KeyGenerator _generator = null;
		try {
			_generator = KeyGenerator.getInstance("DES");
			_generator.init(new SecureRandom(strKey.getBytes()));
			key = (Key) _generator.generateKey();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return key;
	}

	/*
	 * 加密
	 */
	public File encrypt(File file, String password) throws Exception {
		File tmpFile = file;
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, getKey(password));
		InputStream is = new FileInputStream(file);
		OutputStream out = new FileOutputStream(tmpFile);
		CipherInputStream cis = new CipherInputStream(is, cipher);
		byte[] buffer = new byte[1024];
		int r;
		while ((r = cis.read(buffer)) > 0) {
			out.write(buffer, 0, r);
		}
		cis.close();
		is.close();
		out.close();
		return tmpFile;
	}

	/*
	 * 解密
	 */
	public void decrypt(String file, String password) throws Exception {
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, getKey(password));
		InputStream is = new FileInputStream(file);
		OutputStream out = new FileOutputStream(file);
		CipherOutputStream cos = new CipherOutputStream(out, cipher);
		byte[] buffer = new byte[1024];
		int r;
		while ((r = is.read(buffer)) >= 0) {
			System.out.println();
			cos.write(buffer, 0, r);
		}
		cos.close();
		out.close();
		is.close();
	}

	/*
	 * 敏感词检测
	 */
	public int sensitiveCheck() throws Exception {
		String contentString = null;
		// 对一串字符进行操作
		StringBuffer fileData = new StringBuffer();
		// 将file传入
		BufferedReader reader = new BufferedReader(new FileReader(file));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
		}
		// 缓冲区使用完必须关掉
		reader.close();
		contentString = fileData.toString();
		log.info("传入的文件内容为:" + contentString);
		// 进行敏感词检测
		SensitiveWordFilter filter = new SensitiveWordFilter();
		Set<String> set = filter.getSensitiveWord(contentString);
		log.info("语句中包含敏感词的个数为：" + set.size() + "。包含：" + set);
		return set.size();
	}

	/**
	 * 读取文件夹下面的文件和文件夹
	 * 
	 * @return
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws FileNotFoundException
	 */
	public void listFolder() throws FileNotFoundException,
			IllegalArgumentException, IOException {
		List<HdfsResponseProperties> files = this.hdfsService
				.listFolder(hdfsFile.getFolder());
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("total", files.size());
		jsonMap.put("rows", getProperFiles(files, page, rows));
		Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
		return;
	}

	private List<HdfsResponseProperties> getProperFiles(
			List<HdfsResponseProperties> files, int page, int rows) {

		return files.subList((page - 1) * rows,
				page * rows > files.size() ? files.size() : page * rows);
	}

	/**
	 * 检查目录权限或是否存在 权限由外部设定
	 * 
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public void checkExistAndAuth() throws IllegalArgumentException,
			IOException {
		Map<String, Object> map = new HashMap<>();
		boolean exist = this.hdfsService.checkExist(this.hdfsFile.getFolder());
		if (!exist) {
			map.put("flag", "nodir");
			Utils.write2PrintWriter(JSON.toJSONString(map));
			return;
		}
		// 读取并且执行权限
		boolean hasAuth = true;
		if (this.hdfsFile.getAuth() == null
				|| this.hdfsFile.getAuth().length() < 1
				|| this.hdfsFile.getAuth().length() > 3) {
			log.info("权限设置异常！authString:{}", this.hdfsFile.getAuth());
			map.put("flag", "false");
			map.put("msg", "后台错误，请联系管理员！");
			Utils.write2PrintWriter(JSON.toJSONString(map));
			return;
		}
		for (char a : this.hdfsFile.getAuth().toCharArray()) {
			hasAuth = hasAuth
					&& HadoopUtils.checkHdfsAuth(this.hdfsFile.getFolder(),
							String.valueOf(a));
		}
		if (!hasAuth) {
			map.put("flag", "false");
			map.put("msg", "目录操作没有权限！");
		}
		if (map.get("flag") == null) {
			map.put("flag", "true");
		}
		Utils.write2PrintWriter(JSON.toJSONString(map));
		return;
	}

	/**
	 * 移除文件夹
	 * 
	 * @return
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public void deleteFolder() throws IllegalArgumentException, IOException {
		boolean flag = false;
		Map<String, Object> map = new HashMap<>();
		boolean exist = this.hdfsService.checkExist(this.hdfsFile.getFolder());
		if (!exist) {
			map.put("flag", "false");
			map.put("msg", "目录不存在！");
			Utils.write2PrintWriter(JSON.toJSONString(map));
			return;
		}
		// 读取并且执行权限
		boolean auth = HadoopUtils
				.checkHdfsAuth(this.hdfsFile.getFolder(), "r")
				&& HadoopUtils.checkHdfsAuth(this.hdfsFile.getFolder(), "x");
		if (!auth) {
			map.put("msg", "没有权限!");
			map.put("flag", "false");
			Utils.write2PrintWriter(JSON.toJSONString(map));
			return;
		}

		try {
			flag = this.hdfsService.deleteFolder(this.hdfsFile.getFolder(),
					hdfsFile.isRecursive());
		} catch (RemoteException e) {
			if (e.getClassName().equals(
					"org.apache.hadoop.fs.PathIsNotEmptyDirectoryException")) {
				map.put("msg", "目录下有子目录!");
			}
		}
		if (flag) {// 目录删除成功
			map.put("flag", "true");
		} else {// 目录删除失败
			map.put("flag", "false");
		}
		Utils.write2PrintWriter(JSON.toJSONString(map));
		return;
	}

	/**
	 * 检索文件夹 先检查权限
	 * 
	 * @return
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws FileNotFoundException
	 */
	public void searchFolder() throws FileNotFoundException,
			IllegalArgumentException, IOException {

		List<HdfsResponseProperties> files = this.hdfsService.searchFolder(
				hdfsFile.getFolder(), hdfsFile.getName(), hdfsFile.getNameOp(),
				hdfsFile.getOwner(), hdfsFile.getOwnerOp());
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("total", files.size());
		jsonMap.put("rows", getProperFiles(files, page, rows));
		Utils.write2PrintWriter(JSON.toJSONString(jsonMap));
		return;
	}

	/**
	 * 新建文件夹
	 * 
	 * @return
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public void createFolder() throws IllegalArgumentException, IOException {
		Map<String, Object> map = new HashMap<>();
		boolean exist = this.hdfsService.checkExist(this.hdfsFile.getFolder());
		if (exist) {
			map.put("flag", "hasdir");
			Utils.write2PrintWriter(JSON.toJSONString(map));
			return;
		}
		boolean flag = false;
		try {
			flag = this.hdfsService.createFolder(this.hdfsFile.getFolder(),
					hdfsFile.isRecursive());
		} catch (AccessControlException e) {
			map.put("msg", "没有权限！");
		} catch (Exception e) {
			map.put("msg", "创建目录异常，请联系管理员！");
		}
		if (flag) {// 目录删除成功
			map.put("flag", "true");
		} else {// 目录删除失败
			map.put("flag", "false");
		}
		Utils.write2PrintWriter(JSON.toJSONString(map));
		return;
	}

	public void upload() throws Exception {
		Map<String, Object> map = new HashMap<>();
		// 敏感词检测
		int sensitvNum = sensitiveCheck();

		// 加密上传
		// file = encrypt(file, "123456");
		boolean flag = false;
		if (sensitvNum == 0) {
			try {
				flag = this.hdfsService.upload(file.getAbsolutePath(),
						hdfsFile.getFolder() + "/" + fileFileName);
			} catch (Exception e) {
				map.put("msg", "请联系管理员!");
				flag = false;
			}
		} else {
			map.put("msg", "该文件含有敏感词");
		}

		if (flag) {// 上传成功
			map.put("flag", "true");
		} else {// 失败
			map.put("flag", "false");

		}
		Utils.write2PrintWriter(JSON.toJSONString(map));
		return;
	}

	public void download() {
		Map<String, Object> map = new HashMap<>();

		boolean flag = false;
		boolean dir = false;
		try {
			dir = this.hdfsService.isDir(this.hdfsFile.getFileName());
		} catch (Exception e) {
			log.info("查看是否存在该目录时异常");
			map.put("msg", "请联系管理员!");
		}
		if (dir) {
			map.put("flag", "false");
			if (map.get("msg") == null)
				map.put("msg", "不能下载目录!");
			Utils.write2PrintWriter(JSON.toJSONString(map));
			return;
		}
		try {
			flag = this.hdfsService.download(hdfsFile.getFileName(),
					hdfsFile.getLocalFile());
			log.info("下载文件的本地目录:" + hdfsFile.getLocalFile());
		} catch (Exception e) {
			map.put("msg", "请联系管理员!");
			flag = false;
		}

		if (flag) {// 上传成功
			map.put("flag", "true");
		} else {// 失败
			map.put("flag", "false");

		}
		Utils.write2PrintWriter(JSON.toJSONString(map));
		return;
	}

	public void deleteFile() {
		Map<String, Object> map = new HashMap<>();

		boolean flag = false;

		try {
			flag = this.hdfsService.deleteFile(hdfsFile.getFileName());
		} catch (Exception e) {
			map.put("msg", "不能删除目录！");
			flag = false;
		}

		if (flag) {// 上传成功
			map.put("flag", "true");
		} else {// 失败
			map.put("flag", "false");
		}
		Utils.write2PrintWriter(JSON.toJSONString(map));
		return;
	}

	/**
	 * 读取HDFS文件 ；序列化和文本 读取记录数
	 */
	public void read() {
		Map<String, Object> map = new HashMap<>();
		boolean dir = false;
		try {
			dir = this.hdfsService.isDir(this.hdfsFile.getFileName());
		} catch (Exception e) {
			map.put("msg", "请联系管理员!");
		}
		if (dir) {
			map.put("flag", "false");
			if (map.get("msg") == null)
				map.put("msg", "不能读取目录!");
			Utils.write2PrintWriter(JSON.toJSONString(map));
			return;
		}
		String data = null;

		try {
			data = this.hdfsService.read(hdfsFile.getFileName(),
					hdfsFile.getTextSeq(), hdfsFile.getRecords());
		} catch (Exception e) {
			map.put("msg", "请检查文件！");
			data = null;
		}

		if (data != null) {// 上传成功
			map.put("flag", "true");
			map.put("data", data);
		} else {// 失败
			map.put("flag", "false");
		}
		Utils.write2PrintWriter(JSON.toJSONString(map));
		return;
	}

	public HdfsService getHdfsService() {
		return hdfsService;
	}

	@Resource
	public void setHdfsService(HdfsService hdfsService) {
		this.hdfsService = hdfsService;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}
}
