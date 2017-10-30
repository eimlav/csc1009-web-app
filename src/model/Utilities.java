package model;

import java.util.ArrayList;
import java.util.Arrays;

public class Utilities {
	
	// Converts space separated String and places each word into a ArrayList<String>
    public static ArrayList<String> convertSpacedStringToArrayList(String spacedString) {
    	String tempString = new String(spacedString);
		ArrayList<String> tempArray = new ArrayList<String>(Arrays.asList(tempString.split(" ")));
		
		return tempArray;
    }
	
	// Checks if an ArrayList<String> contains a single empty string
	// false = single empty string
	// true = filled string/multiple strings
	public static boolean checkForSingleEmptyStringArrayList(ArrayList<String> list) {
		if(list.size() == 1) {
			if(list.get(0).equals("")) {
				// Single null found
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	// Converts CSV style String to an ArrayList<String> containing each comma separated value
	public static ArrayList<String> convertCSVStringToArrayList(String csvString) {
		String tempString = new String(csvString);
		ArrayList<String> tempArray = new ArrayList<String>(Arrays.asList(tempString.split(",")));
		
		return tempArray;
	}
	
	// Removes a specified String sequence from each String in a ArrayList<String>
	public static ArrayList<String> removeWordFromArrayListString(ArrayList<String> list, String word) {
		ArrayList<String> newList = new ArrayList<String>();
		for(String item : list) {
			if(item.contains(word)) {
				newList.add(item.substring(item.indexOf(word) + word.length(), item.length()));
			}
		}
		return newList;
	}
	
	// Converts ArrayList<String> to String and removes brackets
	public static String convertArrayListToString(ArrayList<String> list) {
		return list.toString().substring(1, list.toString().length() -1);
	}
	
	// Removes any trailing or ending whitespace from each item of an ArrayList<String> 
	public static ArrayList<String> trimArrayListString(ArrayList<String> list) {
		ArrayList<String> tempList = new ArrayList<String>();
		for(String item : list) {
			tempList.add(item.trim());
		}
		return tempList;
	}
}
