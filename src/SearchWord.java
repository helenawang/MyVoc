/**
 * Jul 9, 2016
 * SearchWord.java
 * MyVoc
 */
package src;

import org.json.JSONException;

import fr.idm.sk.publish.api.client.light.SkPublishAPIException;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * @author helena
 *
 */
public class SearchWord extends Application {
	static DictionaryConnect dc;
	String result;
	public SearchWord() throws JSONException, SkPublishAPIException{
		dc = new DictionaryConnect(9);
	}
	
	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		BorderPane pane = new BorderPane();
		
		TextField tf = new TextField();//输入框
		WebBrowser wb = new WebBrowser("file:///F:/mylib_H/MyVoc/file.html");//结果框（目前只能直接显示html格式）
		TextField statefield = new TextField();//状态栏
		Button btOk = new Button("add to notebook");
		
		tf.setAlignment(Pos.BOTTOM_RIGHT);
		tf.appendText("enter a word");
		tf.setOnAction(e -> {
			String word = tf.getText();
			System.out.println("you entered " + word);
			result = dc.getExplaination(word);
			dc.save(result);
			wb.refresh();
			pane.setCenter(wb);
		});		
		
		statefield.setAlignment(Pos.BOTTOM_LEFT);
		statefield.setEditable(false);
		
		//按钮
		btOk.setOnAction(e -> {//lambda表达式，省去类和对象的定义
			statefield.setText("\"" + tf.getText() + "\"" + " has been added to notebook");
			//add to notebook
		});
		
		pane.setTop(tf);
		pane.setLeft(btOk);
		pane.setCenter(wb);
		pane.setBottom(statefield);
		
		Scene scene = new Scene(pane, 800, 500);
		primaryStage.setTitle("Search a word");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	/**
	 * @param args
	 * TODO
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Application.launch(args);
	}

}
