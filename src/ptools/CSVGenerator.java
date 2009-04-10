package ptools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSVGenerator {
	public static char[] CHAR_SET = new char[36];
	public static String parse(String html) {
		
		String res="";/*
		Pattern p = Pattern.compile(
				"<table>([\\s]*<tr>([\\s]*(<th>|<td>)([a-zA-Z0-9\\s]*)(</th>|</td>)[\\s]*)+</tr>[\\s]*)+</table>",
				Pattern.DOTALL);
		Matcher m = p.matcher(html);
		while (m.find()) {
			System.out.println(m.group(4));
			System.out.println("---------");
		}*/
		res = html.replaceAll("\n","\b");
		res = res.replaceAll("\\s*<[/]*table>\\s*", "");
		res = res.replaceAll("</t[hd]>[\\s\b]*<t[hd]>", ",");
		
		res = res.replaceAll("</tr>[\\s\b]*<tr>","\n");
		res = res.replaceAll("<[/]*t[rhd]>","");
		//res = res.replaceAll("[\\s]", "a");
		/*
		res = res.replaceAll("<tr>[\\s\b]*<t[hd]>", "");
		res = res.replaceAll("</td>[\\s\b]*</tr>", "");
		*/
		//System.out.println(res);
	
		return "";
	}
	
	public static String detructKey(String key) {
		createChars();
		String ret = "";
		for (int i=0; i<key.length(); i++){
			char c = key.charAt(i);
			int l = (int)c;
			for (int j=0;j<l;j++){
				ret += String.valueOf(generateRandomChar());
			}
			ret += String.valueOf(key.charAt(i));
		}
		
		return ret;
	 }
	
	public static boolean isMatch(String des, String key) {
		int offset = 0;
		String res = "";
		for (int i=0;i<key.length();i++){
			char c = key.charAt(i);
			int l = (int)c;
			for (int j=0;j<l;j++) {
				offset++;
			}
			//offset++;
			res += String.valueOf(des.charAt(offset));
			
		}
		System.out.println(res);
		return true;
	}
	
	public static void createChars() {
		for (int i=0;i<10;i++) {
			CHAR_SET[i] = (char)(48 + i);
		}
		for(int i=10;i<36;i++) {
			CHAR_SET[i] = (char)(65 + i - 10);
		}
	}
	
	public static char generateRandomChar() {
		int val = (int)(Math.random()*36);
		return CHAR_SET[val];
	}
	
	public static void main(String[] args) {
		String html = 
			"<table> " +
			"  <tr><th>header a</th>" +
			"  <th>header b</th>" +
			"</tr>" +
			"<tr>" +
			"  <td>content1</td>\n" +
			"  <td>content2</td>\n" +
			"</tr>" +
			"<tr>" +
			"  <td>content1</td>\n" +
			"  <td>content2</td>\n" +
			"</tr>" +
			"<tr>" +
			"  <td>content1</td>\n" +
			"  <td>content2</td>\n" +
			"</tr>" +
			 "</table>";
		String res = CSVGenerator.parse(html);
		System.out.println(CSVGenerator.detructKey("ugi"));
		CSVGenerator.isMatch(CSVGenerator.detructKey("ugi"), "ugi");
	}
}
