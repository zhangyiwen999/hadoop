package com.company.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface hdfsService {
	public void mkdir(String filePath) throws IOException;

	public void download(String remote) throws IOException;

	public void delete(String filePath) throws IOException;

	public List ls(String folder) throws IOException;

	public void upload(String localPath, String hdfsPath) throws IOException;

	public void createFile(File local, String hdfsPath) throws IOException;

}
