package model;

import java.util.ArrayList;

public class Account extends SmartSerializable
{
	private static final long serialVersionUID = 1L;
	//unique identifier
	public String id;
	public String username;
	public String email;
	public String password;
        public ArrayList<String> playlistsCreated = new ArrayList<String>();
	public ArrayList<String> playlistsLiked = new ArrayList<String>();
}