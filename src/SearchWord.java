/**
 * Jul 9, 2016
 * SearchWord.java
 * MyVoc
 */
package src;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import fr.idm.sk.publish.api.client.light.SkPublishAPIException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author helena
 *
 */
public class SearchWord extends Application{
	
	//GUI
	static final int width = 800, height = 500;
	static DictionaryConnect dc;
	String result;
	static File notebook = new File("notebook.txt");
	static FileWriter toNotebook;
	
	BorderPane pane = new BorderPane();
	
	TextField tf = new TextField();//输入框
	WebBrowser wb = new WebBrowser("file:///F:/mylib_H/MyVoc/file.html");//结果框（目前只能直接显示html格式）
	TextField statefield = new TextField();//状态栏
	Button btOk = new Button("add to notebook");
	Button btWOD = new Button("word of the day");
	VBox vBox = new VBox(10);//左侧栏
	ImageView imageView = new ImageView(new Image("file:///F:/mylib_H/MyVoc/image/commencement.JPG"));

	//DB
	static DBConnection dbc;
	
	public SearchWord() throws JSONException, SkPublishAPIException, IOException{
		dc = new DictionaryConnect(9);
		if(dc.connected) statefield.setText("successfully connected to Collins Learning Dictionary");
		toNotebook = new FileWriter(notebook, true);//追加写入
	}
	
	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		dbc = new DBConnection();
		// TODO Auto-generated method stub
		tf.setAlignment(Pos.BOTTOM_RIGHT);
		tf.appendText("enter a word");
		tf.setOnAction(e -> {
			String word = tf.getText();
			System.out.println("you entered " + word);
			
			//先在本地数据库里查，没有的话再去在线词典查
			
			//本地数据库
			ArrayList<String> exp = new ArrayList<String>();
			try {
				exp = dbc.getExplaination(word);
			} catch (Exception e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			if(exp != null && exp.size() > 0){
				try {
					result = emitHTML(word, exp);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else{//求助在线词典
				try {
					result = dc.getExplaination(word);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				try {
					result = dc.saveTemp(word, result);//又把result变成状态信息了，不太好
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			wb.refresh();
			pane.setCenter(wb);
			statefield.setText(result);
		});		
		
		statefield.setAlignment(Pos.BOTTOM_LEFT);
		statefield.setEditable(false);
		
		//按钮
		btOk.setOnAction(e -> {
			statefield.setText("\"" + tf.getText() + "\"" + " has been added to notebook");
			try {
				dc.saveExplaination(tf.getText(), result);//把单词释义和例句（目前是混合的）单独保存为一个html
				toNotebook.write(tf.getText() + "\n");//把单词本身追加到一个单词本
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		btWOD.setOnAction(e -> {
			//Date today = new Date();//有漏洞？
			String day = "2016-07-09";
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date today = dateFormat.parse(day);
				result = dc.getWOD(today);
				System.out.println(today.toString());
				Document doc = Jsoup.parse(result);
				Elements word = doc.getElementsByClass("hwd");
				for(Element el: word){
					statefield.setText("today's word of day is " + el.toString());
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		vBox.setPadding(new Insets(15,15,15,15));
		vBox.getChildren().add(btWOD);
		vBox.getChildren().add(btOk);
		//vBox.getChildren().add(imageView);
		
		pane.setTop(tf);
		pane.setLeft(vBox);
		pane.setCenter(wb);
		pane.setBottom(statefield);
		
		Scene scene = new Scene(pane, width, height);
		primaryStage.setTitle("MyVoc");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	/**
	 * @param word
	 * @param exp
	 * @return
	 * TODO
	 * @throws FileNotFoundException 
	 * 
	 */
	private String emitHTML(String word, ArrayList<String> exp) throws FileNotFoundException {
		String tmp = "";
		for(String e: exp){
			tmp = tmp.concat("*" + e);
			tmp = tmp.concat("<br/>");
		}
		File outfile = new File("file.html");//写入html文件
        PrintWriter output = new PrintWriter(outfile);
		output.write("<meta charset=\"utf-8\">"+ "<h3>" + word + "</h3>" + "<br/>" + tmp);
        output.close();
        return word + "'s explaination has been found in your database";
	}

	/**
	 * @param args
	 * TODO
	 * @throws IOException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		Application.launch(args);
		toNotebook.close();
	}
}
