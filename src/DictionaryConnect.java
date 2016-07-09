/**
 * Jul 9, 2016
 * DictionaryConnect.java
 * MyVoc
 */
package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.idm.sk.publish.api.client.light.SkPublishAPI;
import fr.idm.sk.publish.api.client.light.SkPublishAPIException;

/**
 * @author helena
 *
 */
public class DictionaryConnect {
	SkPublishAPI api;//连接
	String dictCode;//选一个词典,9为Collins高级，0为Collins基础
	protected DictionaryConnect(int dict) throws JSONException, SkPublishAPIException{
		DefaultHttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());
        
		String baseUrl = "https://api.collinsdictionary.com";
		String key = "...your key here...";
		
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
	}
	String getExplaination(String word){//make a search
		JSONObject bestMatch;
		String result = " ";
		try {
			bestMatch = new JSONObject(api.searchFirst(dictCode, word, "html"));
			result = bestMatch.getString("entryContent");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SkPublishAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //System.out.println(result);
		return result;
	}
    String save(String content){
            File outfile = new File("file.html");
            PrintWriter output;
			try {
				output = new PrintWriter(outfile);
				output.print("<meta charset=\"utf-8\">"+content);
	            output.close();
	            return "'s explaination has been saved";
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "saved fail!";
    }
}
