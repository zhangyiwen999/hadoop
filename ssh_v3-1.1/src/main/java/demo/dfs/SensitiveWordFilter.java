package demo.dfs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SensitiveWordFilter {
	/*
	 * 敏感词Map，如果敏感词库中有：小日本，小日本鬼子，则sensitiveWordMap结构如下 {小={isEnd=0, 日={isEnd=0,
	 * 本={鬼={isEnd=0, 子={isEnd=1}}, isEnd=1}}}}
	 * isEnd为敏感词标志位，如果明敏感词结束，则设置成1，否则设置成0
	 */
	public Map<String, Object> sensitiveWordMap = null;

	/*
	 * 初始化
	 */
	public SensitiveWordFilter() {
		sensitiveWordMap = new SensitiveWordInit().initSensitiveWord();
	}

	/*
	 * 将敏感词放入到hashSet中
	 */
	public Set<String> getSensitiveWord(String txt) {
		Set<String> sensitiveWordSet = new HashSet<String>();
		txt = ReplaceUtils.getReplaceTxt(txt);
		for (int i = 0; i < txt.length(); i++) {
			List<Integer> list = CheckSensitiveWord(txt, i);
			if (list.size() > 0) {
				for (Integer length : list) {
					sensitiveWordSet.add(txt.substring(i, i + length));
				}
			}
		}
		return sensitiveWordSet;
	}

	/*
	 * 检查文字中是否包含敏感词 函数传入参数如下， txt：String类型，需要检验的文字；beginIndex：int类型，检验起始位置
	 * 返回参数List<Integer>，为匹配的长度
	 * 比如敏感词库中有：小日本，小日本鬼子两个敏感词，输入文本中有小日本鬼子，含则返回参数为：[3,5]
	 */
	@SuppressWarnings({ "rawtypes" })
	public List<Integer> CheckSensitiveWord(String txt, int beginIndex) {
		List<Integer> list = new ArrayList<Integer>();
		int matchFlag = 0; // 匹配标识数默认为0
		char word = 0;
		Map nowMap = sensitiveWordMap;
		for (int i = beginIndex; i < txt.length(); i++) {
			word = txt.toLowerCase().charAt(i);
			nowMap = (Map) nowMap.get(word); // 获取指定key
			if (nowMap != null) { // 存在，则判断是否为最后一个
				matchFlag++; // 找到相应key，匹配标识+1
				if ("1".equals(nowMap.get("isEnd"))) { // 如果为最后一个匹配规则,则添加
					list.add(matchFlag);
				}
			} else { // 不存在，直接返回
				break;
			}
		}
		if (matchFlag < 2) {
			list = new ArrayList<Integer>();
		}
		return list;
	}

}
