package ssh.dao;

import ssh.model.HdfsUser;

/*
 * 对数据库中的Entity进行操作
 */
public interface HdfsUserDao {
	public int save(HdfsUser hdfsUser);

	public void update(HdfsUser hdfsUser);

	public HdfsUser loadByEmail(String email);
}
