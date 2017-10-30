package model;

import java.util.ArrayList;

public class Playlist extends SmartSerializable {
	public String Id;
	public String name;
	public ArrayList<String> songIdList;
	public String userCreatedBy;
	public int totalRuntime;
	
	public Playlist() {
		this.Id = null;
		this.name = null;
		this.songIdList = new ArrayList<String>();
		this.userCreatedBy = null;
		this.totalRuntime = 0;
	}
	
	public Playlist(String id, String name, ArrayList<String> songIdList, String userCreatedBy, int totalRuntime) {
		this.Id = id;
		this.name = name;
		this.songIdList = songIdList;
		this.userCreatedBy = userCreatedBy;
		this.totalRuntime = totalRuntime;
	}
	
	public String getRuntimeString() {
		if(totalRuntime == 0) {
			return "0:00";
		} else {
			// Calculate total runtime in HH:MM:SS format
			int hours  = this.totalRuntime / 3600;
			int minutes = (this.totalRuntime % 3600) / 60;
			int seconds = this.totalRuntime % 60;
			String runtimeString = new String();
			if(hours == 0) 
				runtimeString = String.format("%02d:%02d", minutes, seconds);
			else
				runtimeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
			return runtimeString;
		}
	}
}
