package cn.jsvnlog.util;

public class PrefixUtil {

	public static boolean getRight(String relativPath) {
		if (relativPath.endsWith(".jpg") 
				|| relativPath.endsWith(".metadata")
				|| relativPath.endsWith(".doc")
				|| relativPath.endsWith(".txt")
				|| relativPath.endsWith(".png")
				|| relativPath.endsWith(".class")
				|| relativPath.endsWith(".dic")
				|| relativPath.endsWith(".gif")
				|| relativPath.endsWith(".sql") 
				|| relativPath.endsWith(".jar")
				|| relativPath.endsWith(".xlsx")
				|| relativPath.endsWith(".rar")
				|| relativPath.endsWith("编辑1")
				|| relativPath.endsWith("编辑2")
				|| relativPath.endsWith(".war")
				|| relativPath.endsWith(".zip")
				|| relativPath.endsWith(".dll")
				|| relativPath.endsWith(".war")
				|| relativPath.endsWith(".docx")
				|| relativPath.endsWith(".xls")
				|| relativPath.endsWith(".bak")
				|| relativPath.endsWith("单字")
				|| relativPath.endsWith("单字-all")
				|| relativPath.endsWith(".swf")
				) {
			return false;
		} else {
			return true;
		}
	}
}
