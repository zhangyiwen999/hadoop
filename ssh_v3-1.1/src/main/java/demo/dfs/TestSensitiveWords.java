/**
 * 
 */
package demo.dfs;

import java.util.Set;

/**
 * @author Administrator
 *
 */
public class TestSensitiveWords {

	public static void main(String[] args) {
		SensitiveWordFilter filter = new SensitiveWordFilter();
		System.out.println("敏感词的数量：" + filter.sensitiveWordMap.size());
		String string = "fuck";
		long beginTime = System.currentTimeMillis();
		Set<String> set = filter.getSensitiveWord(string);
		long endTime = System.currentTimeMillis();
		System.out.println("语句中包含敏感词的个数为：" + set.size() + "。包含：" + set);
		System.out.println("总共消耗时间为：" + (endTime - beginTime));
	}
}
