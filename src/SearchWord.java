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
import java.text.SimpleDateFormat;
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
		// TODO Auto-generated method stub
		tf.setAlignment(Pos.BOTTOM_RIGHT);
		tf.appendText("enter a word");
		tf.setOnAction(e -> {
			String word = tf.getText();
			try {
				result = dc.getExplaination(word);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			System.out.println("you entered " + word);
			try {
				dc.saveTemp(word, result);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			wb.refresh();
			pane.setCenter(wb);
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
	 * @param args
	 * TODO
	 * @throws IOException 
	 * 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Application.launch(args);
		toNotebook.close();
	}

}
