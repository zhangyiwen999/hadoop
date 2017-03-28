package demo.dfs;

/**
 * @author Administrator
 *
 */
public class ReplaceUtils {

	/*
	 * 所有非字母、数字、中文的，均替换成空
	 */
	public static String getSimpleReplaceTxt(String txt) {
		String pattern = "[^(a-zA-Z0-9\\u4e00-\\u9fa5)]";
		return txt.replaceAll(pattern, "");
	}

	// 字符替换
	public static String getReplaceTxt(String txt) {
		return txt.replace(" ", "").replace("，", "").replace("\\", "")
				.replace("+", "").replace("-", "").replace("!", "")
				.replace("(", "").replace(")", "").replace(":", "")
				.replace("^", "").replace("[", "").replace("]", "")
				.replace("\"", "").replace("{", "").replace("}", "")
				.replace("~", "").replace("*", "").replace("?", "")
				.replace("|", "").replace("&", "").replace(";", "")
				.replace("/", "").replace("\"", "").replace("\'", "");
	}
}
