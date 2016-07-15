/**
 * Jul 9, 2016
 * DictionaryConnect.java
 * MyVoc
 */
package src;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fr.idm.sk.publish.api.client.light.SkPublishAPI;
import fr.idm.sk.publish.api.client.light.SkPublishAPIException;

/**
 * @author helena
 *
 */
public class DictionaryConnect {
	SkPublishAPI api;//连接
	String dictCode;//选一个词典,9为Collins高级，0为Collins基础
	boolean connected = false;
	protected DictionaryConnect(int dict) throws JSONException, SkPublishAPIException{
		DefaultHttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());
        
		String baseUrl = "https://api.collinsdictionary.com";
		String key = "your key here";
		
        api = new SkPublishAPI(baseUrl + "/api/v1", key, httpClient);
        api.setRequestHandler(new SkPublishAPI.RequestHandler() {
            public void prepareGetRequest(HttpGet request) {
                request.setHeader("Accept", "application/json");
                System.out.println("request to " + request.getURI());//add by helena
            }
        });
        
        JSONArray dictionaries = new JSONArray(api.getDictionaries());//获取可用词典
        JSONObject dictList = dictionaries.getJSONObject(dict);//dict为想要用的词典的编号
        dictCode = dictList.getString("dictionaryCode");//获取此编号词典的内部码
        
        connected = true;
	}
	
	String getExplaination(String word) throws JSONException, SkPublishAPIException{//make a search
		
		JSONObject bestMatch;
		String result = "";
			bestMatch = new JSONObject(api.searchFirst(dictCode, word, "html"));
			result = bestMatch.getString("entryContent");
        //System.out.println(result);
		return result;
	}
	String getWOD(Date date) throws JSONException, SkPublishAPIException{
		String dateFormat = (new SimpleDateFormat("yyyy-MM-dd")).format(date);
		System.out.println(dateFormat);
		JSONObject wordOfDay;
		String result = "";
		wordOfDay = new JSONObject(api.getWordOfTheDay(dictCode, dateFormat, "html"));
		result = wordOfDay.getString("entryContent");
		return result;
	}
	String saveExplaination(String word, String content) throws FileNotFoundException{
		//parse explaination and examples
        File exeg = new File(word + ".txt");
        PrintWriter exegout = new PrintWriter(exeg);
        
        Document doc = Jsoup.parse(content);
        Elements explaination = doc.getElementsByClass("def");
        for(Element def: explaination){
        	//System.out.println(def.text());
        	exegout.write(def.text() + "\n");
        }
        
        Elements examples = doc.getElementsByClass("cit");
        for(Element eg: examples){
        	//System.out.println(eg.text());
        	exegout.write(eg.text() + "\n");
        }
        exegout.close();
        return "'s explaination has been saved";
	}
    String saveTemp(String word, String content) throws IOException{
            File outfile = new File("file.html");
            PrintWriter output = new PrintWriter(outfile);
			output.write("<meta charset=\"utf-8\">"+content);
	        output.close();
	           
	    return word + "'s explaination has been fetched from Collins Dictionary";
    }
}
