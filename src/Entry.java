/**
 * Jul 17, 2016
 * Entry.java
 * MyVoc
 */
package src;

import java.util.ArrayList;

/**
 * @author helena
 *
 */
public class Entry {
	String word;
	int numOfExplaination = 0;
	ArrayList<String> defination;
	Entry(String word){
		this.word = word;
		numOfExplaination = 0;
		defination = new ArrayList<String>();
	}
	String addExplaination(String exp){
		defination.add(exp);
		numOfExplaination++;
		return "explaination added.";
	}
}
