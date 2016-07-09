/**
 * Jul 9, 2016
 * WebBrowser.java
 * MyVoc
 */
package src;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * @author helena
 * reference: http://www.javafxchina.net/blog/2015/07/html_webview_2/
 */

public class WebBrowser extends Region{
	final WebView browser = new WebView();
	final WebEngine webEngine = browser.getEngine();
	String htmlFile;
	
	public WebBrowser(String htmlFile){
		this.htmlFile = htmlFile;
		//webEngine.load(htmlFile);
		getChildren().add(browser);
	}
	public void refresh(){//重新载入html文件
		webEngine.load(htmlFile);
		//getChildren().add(browser);
	}
//	private Node createSpacer(){
//		Region spacer = new Region();
//		HBox.setHgrow(spacer, Priority.ALWAYS);
//		return spacer;
//	}
	@Override protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(browser,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
    }
 
    @Override protected double computePrefWidth(double height) {
        return 900;
    }
 
    @Override protected double computePrefHeight(double width) {
        return 600;
    }
}