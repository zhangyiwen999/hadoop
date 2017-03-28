package demo.dfs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SensitiveWordInit {
	public HashMap<String, Object> sensitiveWordMap;

	public SensitiveWordInit() {
		super();
	}

	/*
	 * 初始化
	 */
	public Map<String, Object> initSensitiveWord() {
		try {
			Set<String> sensitiveWordSet = readTxtSensitiveWordFile();
			addSensitiveWordToHashMap(sensitiveWordSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sensitiveWordMap;
	}

	/*
	 * 读取敏感词库，将敏感词放入HashMap中，构建DFA算法模型 其构造过程如下，比如敏感词库中有：小日本，小日本鬼子
	 * 1、在hashMap中查询“小”，看其是否存在，如果不存在，则证明以“小”开头的敏感词还不存在，则我们直接构建新的hashMap。
	 * 2、如果在hashMap中查找到了，表明存在以“小”开头的敏感词，设置hashMap =
	 * hashMap.get("小")，跳至1，依次匹配“日”、“本”、“鬼”、“子”。
	 * 3、判断该字是否为该词中的最后一个字。若是，表示敏感词结束，设置标志位isEnd = 1，否则设置标志位isEnd = 0；
	 * 构造成功后，其结构如下 { 小={ isEnd=0, 日={ isEnd=0, 本={ isEnd=1, 鬼={ isEnd = 0,
	 * 子={isEnd=1} } } } } }
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addSensitiveWordToHashMap(Set<String> sensitiveWordSet) {
		// 初始化敏感词容器，减少扩容操作
		sensitiveWordMap = new HashMap(sensitiveWordSet.size());
		Map nowMap = null;
		Map<String, String> newWorMap = null;

		for (String key : sensitiveWordSet) {
			nowMap = sensitiveWordMap;
			for (int i = 0; i < key.length(); i++) {
				// 转换成char型
				char keyChar = key.charAt(i);
				// 获取
				Object wordMap = nowMap.get(keyChar);
				// 如果存在该key，直接赋值
				if (wordMap != null) {
					nowMap = (Map) wordMap;
				} else { // 不存在，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
					newWorMap = new HashMap<String, String>();
					newWorMap.put("isEnd", "0");
					nowMap.put(keyChar, newWorMap);
					nowMap = newWorMap;
				}

				if (i == key.length() - 1) {
					// 最后一个
					nowMap.put("isEnd", "1");
				}
			}
		}

	}

	/*
	 * 读取敏感词所属txt配置文件
	 */
	@SuppressWarnings("resource")
	private Set<String> readTxtSensitiveWordFile() throws Exception {
		Set<String> set = null;
		/*
		 * File file = new File("src/main/java/demo/dfs/SensitiveWord.txt");
		 * FileInputStream read = new FileInputStream(file); try { if
		 * (file.isFile() && file.exists()) { set = new HashSet<String>();
		 * BufferedReader bufferedReader = new BufferedReader( new
		 * UnicodeReader(read, Charset.defaultCharset().name())); String txt =
		 * null; while ((txt = bufferedReader.readLine()) != null) {
		 * set.add(txt.toLowerCase()); } } else { throw new Exception(""); } }
		 * catch (Exception e) { throw e; } finally { read.close(); }
		 */
		set = new HashSet<String>();
		set.add("fuck");
		set.add("泽东");
		return set;
	}
}
