package views;

import java.util.ArrayList;

import org.h2.mvstore.MVMap;

import model.Account;
import model.Playlist;
import model.Song;
import model.Utilities;
import storage.DatabaseInterface;
import storage.FileStoreInterface;
import web.WebRequest;
import web.WebResponse;

public class Playlists extends DynamicWebPage
{

	public Playlists(DatabaseInterface db,FileStoreInterface fs)
	{
		super(db,fs);
	}

	public boolean process(WebRequest toProcess)
	{
		if(toProcess.path.equalsIgnoreCase("playlists"))
		{
			String taskType = toProcess.params.get("type");
			String stringToSendToBrowser = "";
			String playlistHTML = "";
			boolean loggedIn = verifyCookie(toProcess);
			
			// Verify cookie
			if(!loggedIn) {
				return errorEncountered(toProcess, "you must be logged in to use this feature");
			}
			
			String cookieUsername = toProcess.cookies.get("username");
			Account account = new Account();
			MVMap<String, Account> accountsMap = db.s.openMap("Accounts");
			account = accountsMap.get(cookieUsername);
			
			// Perform any create/remove task
			String banner = new String();
			if(taskType != null) {
				if(taskType.equalsIgnoreCase("create")) {
					String playlistName = toProcess.params.get("playlistName");
					banner = createPlaylist(playlistName, cookieUsername);
					//stringToSendToBrowser += getMainHTML(playlistHTML, banner, selectOptions, "Playlists");
				} else if(taskType.equalsIgnoreCase("remove")) {
					String playlistID = toProcess.params.get("playlistID");
					banner = removePlaylist(playlistID, cookieUsername);
					//stringToSendToBrowser += getMainHTML(playlistHTML, banner, selectOptions, "Playlists");
				} else if(taskType.equalsIgnoreCase("addSong")) {
					String songID = toProcess.params.get("songID");
					String playlistID = toProcess.params.get("playlistID");
					if(playlistID == null && songID != null) {
						String selectOptions = new String();
						MVMap<String, Playlist> playlistsMap = db.s.openMap("PlaylistsMap");
						for(String eachPlaylistID : account.playlistsCreated) {
							if(playlistsMap.get(eachPlaylistID).userCreatedBy.equalsIgnoreCase(cookieUsername)) {
								selectOptions += "                  <option value=\"" + eachPlaylistID + "\">" + playlistsMap.get(eachPlaylistID).name + " by " + playlistsMap.get(eachPlaylistID).userCreatedBy + " | " + playlistsMap.get(eachPlaylistID).songIdList.size() + " Songs" + "</option>\n";
							}
						}
						stringToSendToBrowser += getMainHTML(getAddSongHTML(selectOptions, songID), "", "", "Add Song to Playlist", false, loggedIn);
						toProcess.r = new WebResponse( WebResponse.HTTP_OK, WebResponse.MIME_HTML, stringToSendToBrowser);
						return true;
					} else if(playlistID != null && songID != null) {
						banner = addSongtoPlaylist(songID, playlistID, cookieUsername);
					}
					
				} else if(taskType.equalsIgnoreCase("removeSong")) {
					String playlistID = toProcess.params.get("playlistID");
					String songID = toProcess.params.get("songID");
					banner = removeSongFromPlaylist(playlistID, songID, cookieUsername);
				}
				else {
					// Do nothing
				}
			} else {
				if(true) {
					banner = "";
					//stringToSendToBrowser += getMainHTML(playlistHTML, "", selectOptions, "Playlists");
				}
			}
			
			// Get available playlists HTML and add to remove select
			MVMap<String, Playlist> playlistsMap = db.s.openMap("PlaylistsMap");
			System.out.println("Playlists created: " + account.playlistsCreated);
			System.out.println("Playlists liked: " + account.playlistsLiked);
			for(String key : playlistsMap.keyList()) {
				System.out.println(playlistsMap.get(key).Id + " " + playlistsMap.get(key).name);
			}
			
			String selectOptions = new String();
			for(String playlistID : account.playlistsCreated) {
				if(playlistsMap.get(playlistID).userCreatedBy.equalsIgnoreCase(cookieUsername)) {
					selectOptions += "                  <option value=\"" + playlistID + "\">" + playlistsMap.get(playlistID).name + " by " + playlistsMap.get(playlistID).userCreatedBy + " | " + playlistsMap.get(playlistID).songIdList.size() + " Songs" + "</option>\n";
					playlistHTML += getPlaylistHTML(playlistsMap.get(playlistID), true);
				}
			}
			
			for(String playlistID : account.playlistsLiked) {
				selectOptions += "                  <option value=\"" + playlistID + "\">" + playlistsMap.get(playlistID).name + " by " + playlistsMap.get(playlistID).userCreatedBy + " | " + playlistsMap.get(playlistID).songIdList.size() + " Songs" + "</option>\n";
				playlistHTML += getPlaylistHTML(playlistsMap.get(playlistID), false);
			}
			
			stringToSendToBrowser += getMainHTML(playlistHTML, banner, selectOptions, "Playlists", true, loggedIn);
			
			toProcess.r = new WebResponse( WebResponse.HTTP_OK, WebResponse.MIME_HTML, stringToSendToBrowser);
			return true;
		}
		return false;
	}
	
	/*
	 * Test Account Details
	 * username = testuser
	 * password = password
	 * In browser address bar enter:
	 * javascript: document.cookie = "username=testuser"
	 * javascript: document.cookie = "password=password"
	 */
	public boolean verifyCookie(WebRequest toProcess) {
		String username = toProcess.cookies.get("username");
		String password = toProcess.cookies.get("password");
		
		MVMap<String, Account> accountsMap = db.s.openMap("Accounts");
		Account account = accountsMap.get(username);
		
		if(account != null) {
			if(account.password.equals(password)) {
				System.out.println("Verified cookie of user: " + username);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public String removeSongFromPlaylist(String playlistID, String songID, String cookieUsername) {
		System.out.println("# REMOVE SONG #\nPlaylist ID:" + playlistID + "\nSong ID: " + songID);
		if(playlistID != null && songID != null) {
			MVMap<String, Playlist> playlistsMap = db.s.openMap("PlaylistsMap");
			Playlist playlist = playlistsMap.get(playlistID);
			if(playlist.userCreatedBy.equalsIgnoreCase(cookieUsername)) {
				if(playlist.songIdList.contains(songID)) {
					playlist.songIdList.remove(songID);
					MVMap<String, Song> songsMap = db.s.openMap("Songs");
					int runtimeToTake = songsMap.get(songID).runtime;
					playlist.totalRuntime -= runtimeToTake;
					playlistsMap.put(playlistID, playlist);
					db.commit();
					System.out.println("Song removed");
					return "<div id=\"playlist-created-banner\" class=\"text-center\">SONG REMOVED FROM '" + playlist.name.toUpperCase() + "'</div>";
				} else {
					return "<div id=\"playlist-removed-banner\" class=\"text-center\">SONG NOT FOUND IN '" + playlist.name.toUpperCase() + "'</div>";
				}
				
			}
		}
		System.out.println("Encountered error");
		return "<div id=\"playlist-removed-banner\" class=\"text-center\">ENCOUNTERED ERROR</div>";
	}
	
	public String addSongtoPlaylist(String songID, String playlistID, String cookieUsername) {
		System.out.println("# ADD SONG TO PLAYLIST #");
		MVMap<String, Playlist> playlistsMap = db.s.openMap("PlaylistsMap");
		Playlist playlist = playlistsMap.get(playlistID);
		if(playlist != null) {
			if(playlist.userCreatedBy.equalsIgnoreCase(cookieUsername)) {
				MVMap<String, Song> songsMap = db.s.openMap("Songs");
				Song song = songsMap.get(songID);
				if(song != null) {
					if(!playlist.songIdList.contains(songID)) {
						playlist.songIdList.add(songID);
						playlist.totalRuntime += song.runtime;
						playlistsMap.put(playlistID, playlist);
						db.commit();
						return "<div id=\"playlist-created-banner\" class=\"text-center\">'" + song.title.toUpperCase() + "' SUCCESSFULLY ADDED TO '" + playlist.name.toUpperCase() + "'</div>";
					} else {
						return "<div id=\"playlist-removed-banner\" class=\"text-center\">SONG ALREADY IN PLAYLIST</div>";
					}
					
				} else {
					return "<div id=\"playlist-removed-banner\" class=\"text-center\">SONG NOT FOUND</div>";
				}
			}
		} else {
			return "<div id=\"playlist-removed-banner\" class=\"text-center\">PLAYLIST NOT FOUND</div>";
		}
		return "";
	}
	
	public boolean errorEncountered(WebRequest toProcess, String error) {
		boolean loggedIn = verifyCookie(toProcess);
		String stringToSendToWebBrowser = getMainHTML(getErrorHTML(error), "", "", "Error", false, loggedIn);
		toProcess.r = new WebResponse( WebResponse.HTTP_OK, WebResponse.MIME_HTML, stringToSendToWebBrowser );
		return true;
	}
	
	public String createPlaylist(String playlistName, String cookieUsername) {
		Playlist playlist = new Playlist();
		playlist = new Playlist(Long.toString(System.currentTimeMillis()), playlistName, new ArrayList<String>(), cookieUsername, 0);
		MVMap<String, Playlist> playlistMap = db.s.openMap("PlaylistsMap");
		playlistMap.put(playlist.Id, playlist);
		db.commit();
		MVMap<String, Account> accountsMap = db.s.openMap("Accounts");
		Account account = accountsMap.get(cookieUsername);
		account.playlistsCreated.add(playlist.Id);
		accountsMap.put(cookieUsername, account);
		db.commit();
		System.out.println("# # CREATE PLAYLIST # #\nCreated playlist '" + playlist.name + "' | ID: " + playlist.Id);
		return "<div id=\"playlist-created-banner\" class=\"text-center\">PLAYLIST CREATED</div>";
	}
	
	public String removePlaylist(String playlistID, String cookieUsername) {
		MVMap<String, Playlist> playlistMap = db.s.openMap("PlaylistsMap");
		Playlist playlist = new Playlist();
		playlist = playlistMap.get(playlistID);
		System.out.println("# # REMOVE PLAYLIST # #");
		if(playlist != null) {
			MVMap<String, Account> accountsMap = db.s.openMap("Accounts");
			Account account = accountsMap.get(cookieUsername);
			account.playlistsCreated.remove(playlistID);
			account.playlistsLiked.remove(playlistID);
			accountsMap.put(account.username, account);
			db.commit();
			if(playlist.userCreatedBy.equalsIgnoreCase(cookieUsername)) {
				playlistMap.remove(playlistID);
			}
			System.out.println("Removed playlist: " + playlistID);
			return "<div id=\"playlist-removed-banner\" class=\"text-center\">PLAYLIST REMOVED</div>";
			
		} else {
			System.out.println("Invalid playlist");
			return "<div id=\"playlist-removed-banner\" class=\"text-center\">PLAYLIST INVALID</div>";
		}
	}
	
	public String getAddSongHTML(String selectOptions, String songID) {
		MVMap<String, Song> songsMap = db.s.openMap("Songs");
		Song song = songsMap.get(songID);
		
		String stringToSendToWebBrowser = "";
		
		stringToSendToWebBrowser += "    <div class=\"section\">\n";
		stringToSendToWebBrowser += "      <div class=\"container\">\n";
		stringToSendToWebBrowser += "        <div class=\"row\">\n";
		stringToSendToWebBrowser += "          <div class=\"col-md-12\">\n";
		stringToSendToWebBrowser += "            <h1 class=\"main-header\">PLAYLISTS</h1>\n";
		stringToSendToWebBrowser += "            <br>\n";
		stringToSendToWebBrowser += "          </div>\n";
		stringToSendToWebBrowser += "        </div>\n";
		stringToSendToWebBrowser += "        <div class=\"row\">\n";
		stringToSendToWebBrowser += "          <div class=\"col-md-12\">\n";
		stringToSendToWebBrowser += "            <h3 style=\"text-align:center\">ADD '" + song.title.toUpperCase() + "' TO A PLAYLIST</h3>\n";
		stringToSendToWebBrowser += "            <br>\n";
		stringToSendToWebBrowser += "            <form role=\"form\" mode=\"GET\" action=\"playlists\" id=\"playlist-form-add\">\n";
		stringToSendToWebBrowser += "              <div class=\"form-group\">\n";
		stringToSendToWebBrowser += "                <select class=\"form-control\" id=\"formAddValue\">\n";
		stringToSendToWebBrowser += "                  <option value=\"null\" selected=\"selected\">Select a playlist to add to</option>\n";
		stringToSendToWebBrowser += selectOptions;
		stringToSendToWebBrowser += "                </select>\n";
		stringToSendToWebBrowser += "              </div>\n";
		stringToSendToWebBrowser += "              <input type=\"hidden\" value=\"addSong\" name=\"type\">";
		stringToSendToWebBrowser += "              <button type=\"submit\" class=\"btn btn-block btn-default\" s=\"\" onclick=\"addSong()\"\">Add</button>\n";
		stringToSendToWebBrowser += "              <input type=\"hidden\" value=\"\" name=\"playlistID\" id=\"finalAddPlaylistID\">\n";
		stringToSendToWebBrowser += "              <input type=\"hidden\" value=\""+ song.Id +"\" name=\"songID\" id=\"finalSongID\">\n";
		stringToSendToWebBrowser += "            </form>\n";
		stringToSendToWebBrowser += "          </div>\n";
		stringToSendToWebBrowser += "        </div>\n";
		
		return stringToSendToWebBrowser;
	}
	
	public String getTaskHTML(int value) {
		return null;
	}
	
	public String getPlaylistHTML(Playlist playlist, boolean removeSong) {
		String stringToSendToWebBrowser = "";
		stringToSendToWebBrowser += "        <div class=\"section\">\n";
		stringToSendToWebBrowser += "          <div class=\"container\">\n";
		stringToSendToWebBrowser += "            <div class=\"row\">\n";
		stringToSendToWebBrowser += "              <div class=\"col-md-12\">\n";
		stringToSendToWebBrowser += "                <hr>\n";
		stringToSendToWebBrowser += "              </div>\n";
		stringToSendToWebBrowser += "            </div>\n";
		stringToSendToWebBrowser += "            <div class=\"row\">\n";
		stringToSendToWebBrowser += "              <div class=\"col-md-12\">\n";
		
		stringToSendToWebBrowser += "                <h3 id=\"playlist-header\">" + playlist.name.toUpperCase() + " | " + playlist.songIdList.size() + " Songs" + "</h3>\n";
		stringToSendToWebBrowser += "                <h4 id=\"playlist-header\">by " + playlist.userCreatedBy + "</h4>\n";
		stringToSendToWebBrowser += "                <h4 id=\"playlist-header\">" + playlist.getRuntimeString() + "</h4>\n";
		
		stringToSendToWebBrowser += "                <ul class=\"media-list\">\n";
		stringToSendToWebBrowser += "                  <li></li>\n";

		MVMap<String, Song> songsMap = db.s.openMap("Songs");
		for(int i = 0; i < playlist.songIdList.size(); i++) {
			String playlistItem = "";
			String streamingURL = String.format("\\play?playlist=%1$s&index=%2$d", playlist.Id, i);
			if(i == 5 && playlist.songIdList.size() > 6) {
				Song song = songsMap.get(playlist.songIdList.get(i));
				int additionalSongs = playlist.songIdList.size() - 5;
				playlistItem += "                  <li class=\"media col-md-4\" id=\"playlist-item\">\n";
				playlistItem += "                    <div>\n";
				playlistItem += "                      <a class=\"pull-left\" href=\"" + streamingURL + "\"><img class=\"media-object\" src=\"" + song.albumThumbnailURL + "\" height=\"80\" width=\"80\"></a>\n";
				playlistItem += "                    </div>\n";
				playlistItem += "                    <div class=\"media-body\">\n";
				playlistItem += "                      <br>\n";
				playlistItem += "                      <h4 class=\"media-heading text-center\">\n";
				playlistItem += "                        <a href=\"" + streamingURL + "\">+ " + additionalSongs + " SONGS</a>\n";
				playlistItem += "                      </h4>\n";
				playlistItem += "                    </div>\n";
				playlistItem += "                  </li>\n";
				stringToSendToWebBrowser += playlistItem;
				break;
			} else {
				Song song = songsMap.get(playlist.songIdList.get(i));
				playlistItem += "                  <li class=\"media col-md-4\" id=\"playlist-item\">\n";
				playlistItem += "                    <div class=\"media-object\">\n";
				playlistItem += "                      <a href=\"" + streamingURL + "\" class=\"pull-left\"><img src=\"" + song.albumThumbnailURL +"\" height=\"80\" width=\"80\"></a>\n";
				if(removeSong)
					playlistItem += "					   <a href=\"\\playlists?type=removeSong&playlistID=" + playlist.Id + "&songID=" + song.Id + "\" style=\"position:absolute;top:0;left:0;\"><img src=\"x-symbol.png\" height=\"20\" width=\"20\"></a>";
				playlistItem += "                    </div>\n";
				playlistItem += "                    <div class=\"media-body\">\n";
				playlistItem += "                      <h4 class=\"media-heading text-center\">\n";
				playlistItem += "                        <a href=\"" + streamingURL + "\">" + song.title.toUpperCase() + "</a>\n";
				playlistItem += "                      </h4>\n";
				playlistItem += "                      <h5 class=\"media-heading text-center\">\n";
				playlistItem += "                        <a href=\"" + streamingURL + "\">" + Utilities.convertArrayListToString(song.artists).toUpperCase() + "</a>\n";
				playlistItem += "                      </h5>\n";
				playlistItem += "                      <h6 class=\"media-heading text-center\">\n";
				playlistItem += "                        <a href=\"" + streamingURL + "\">" + Utilities.convertArrayListToString(song.primaryEmotionalCategories).toUpperCase() + "</a>\n";
				playlistItem += "                      </h6>\n";
				playlistItem += "                      <h6 class=\"media-heading text-center\">\n";
				playlistItem += "                        <a href=\"" + streamingURL + "\">" + Utilities.convertArrayListToString(song.genres).toUpperCase() + "</a>\n";
				playlistItem += "                      </h6>\n";
				playlistItem += "                    </div>\n";
				playlistItem += "                  </li>\n";
				stringToSendToWebBrowser += playlistItem;
			}
		}
		
		if(playlist.songIdList.size() == 0) {
			String playlistItem = new String();
			playlistItem += "                  <li>\n";
			playlistItem += "                      <br>\n";
			playlistItem += "                      <h4 class=\"text-center\">\n";
			playlistItem += "                        Add some songs here by playing a song and clicking the ' + Add to playlist' button\n";
			playlistItem += "                      </h4>\n";
			playlistItem += "                  </li>\n";
			stringToSendToWebBrowser += playlistItem;
		}
	
		stringToSendToWebBrowser += "                </ul>\n";
		stringToSendToWebBrowser += "              </div>\n";
		stringToSendToWebBrowser += "            </div>\n";
		stringToSendToWebBrowser += "          </div>\n";
		stringToSendToWebBrowser += "        </div>\n";
		
		return stringToSendToWebBrowser;
	}
	
	public String getErrorHTML(String error) {
		String stringToSendToWebBrowser = "";
		stringToSendToWebBrowser += "    <div class=\"section\" wp-content=\"\" uploads=\"\" style=\"background: url(http://i.imgur.com/bguKSHt.jpg) no-repeat center center; background-size: cover;\">\n";
		stringToSendToWebBrowser += "      <div id=\"no-song-display-text\">\n";
		stringToSendToWebBrowser += "        <a href=\"enterpage.html\">\n";
		stringToSendToWebBrowser += "        <h3>" + error.toUpperCase() + "\n";
		stringToSendToWebBrowser += "          <br>CLICK HERE TO RETURN HOME</h3></a>\n";
		stringToSendToWebBrowser += "      </div>\n";
		stringToSendToWebBrowser += "    </div>\n";
		return stringToSendToWebBrowser;
	}
	
	public String getMainHTML(String additionalHTML, String banner, String selectOptions, String title, boolean showMenu, boolean loggedIn) {
		String stringToSendToWebBrowser = "";
		stringToSendToWebBrowser += "<html>\n";
		stringToSendToWebBrowser += "  \n";
		stringToSendToWebBrowser += "  <head>\n";
		stringToSendToWebBrowser += "    <meta charset=\"utf-8\">\n";
		stringToSendToWebBrowser += "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n";
		stringToSendToWebBrowser += "    <script type=\"text/javascript\" src=\"http://cdnjs.cloudflare.com/ajax/libs/jquery/2.0.3/jquery.min.js\"></script>\n";
		stringToSendToWebBrowser += "    <script type=\"text/javascript\" src=\"http://netdna.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js\"></script>\n";
		stringToSendToWebBrowser += "    <script type=\"text/javascript\" src=\"search.js\"></script>\n";
		stringToSendToWebBrowser += "    <script type=\"text/javascript\" src=\"playlists.js\"></script>\n";
		stringToSendToWebBrowser += "    <link href=\"http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.3.0/css/font-awesome.min.css\"\n";
		stringToSendToWebBrowser += "    rel=\"stylesheet\" type=\"text/css\">\n";
		stringToSendToWebBrowser += "    <link href=\"playlists.css\" rel=\"stylesheet\" type=\"text/css\">\n";
		stringToSendToWebBrowser += "    <link href=\"https://fonts.googleapis.com/css?family=Lato\" rel=\"stylesheet\">\n";
                stringToSendToWebBrowser += "    <link  rel=\"icon\" href=\"\\favicon.png\">\n";
  		stringToSendToWebBrowser += "    <title>e.mu | " + title + "</title>\n";
		stringToSendToWebBrowser += "  </head>\n";
		stringToSendToWebBrowser += "  \n";
		stringToSendToWebBrowser += "  <body>\n";
		stringToSendToWebBrowser += "    <div class=\"navbar navbar-default navbar-static-top\">\n";
		stringToSendToWebBrowser += "      <div class=\"container\">\n";
		stringToSendToWebBrowser += "        <div class=\"navbar-header\">\n";
		stringToSendToWebBrowser += "          <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-ex-collapse\">\n";
		stringToSendToWebBrowser += "            <span class=\"sr-only\">Toggle navigation</span>\n";
		stringToSendToWebBrowser += "            <span class=\"icon-bar\"></span>\n";
		stringToSendToWebBrowser += "            <span class=\"icon-bar\"></span>\n";
		stringToSendToWebBrowser += "            <span class=\"icon-bar\"></span>\n";
		stringToSendToWebBrowser += "          </button>\n";
		stringToSendToWebBrowser += "          <a class=\"navbar-brand\" href=\"#\"><span>e.mu</span></a>\n";
		stringToSendToWebBrowser += "        </div>\n";
		stringToSendToWebBrowser += "        <div class=\"collapse navbar-collapse\" id=\"navbar-ex-collapse\">\n";
		stringToSendToWebBrowser += "          <ul class=\"nav navbar-nav navbar-right\">\n";
		stringToSendToWebBrowser += "            <li class=\"disabled\">\n";
		stringToSendToWebBrowser += "              <a href=\"\\index\">HOME</a>\n";
		stringToSendToWebBrowser += "            </li>\n";
		stringToSendToWebBrowser += "            <li class=\"disabled\">\n";
		stringToSendToWebBrowser += "              <a href=\"\\search\">SEARCH</a>\n";
		stringToSendToWebBrowser += "            </li>\n";
		stringToSendToWebBrowser += "            <li class=\"active\">\n";
		stringToSendToWebBrowser += "              <a href=\"\\playlists\">PLAYLISTS</a>\n";
		stringToSendToWebBrowser += "            </li>\n";
		stringToSendToWebBrowser += "            <li class=\"disabled\">\n";
		stringToSendToWebBrowser += "              <a href=\"\\upload\">UPLOAD</a>\n";
		stringToSendToWebBrowser += "            </li>\n";
		
		if(loggedIn) {
			stringToSendToWebBrowser += "            <li class=\"disabled\">\n";
			stringToSendToWebBrowser += "              <a href=\"\\index\" onclick=\"logout()\">LOGOUT</a>\n";
			stringToSendToWebBrowser += "            </li>\n";
		}
		
		stringToSendToWebBrowser += "            <li>\n";
		stringToSendToWebBrowser += "              <form class=\"navbar-form navbar-left\" role=\"search\" id=\"nav-bar-search\"\n";
		stringToSendToWebBrowser += "              action=\"/search/resultsStn\">\n";
		stringToSendToWebBrowser += "                <div class=\"form-group\">\n";
		stringToSendToWebBrowser += "                  <input type=\"text\" class=\"form-control\" placeholder=\"Search\" id=\"nav-bar-search-input\">\n";
		stringToSendToWebBrowser += "                  <input type=\"hidden\" value=\"\" name=\"querys\" id=\"finalStnQueryNav\">\n";
		stringToSendToWebBrowser += "                </div>\n";
		stringToSendToWebBrowser += "              </form>\n";
		stringToSendToWebBrowser += "            </li>\n";
		stringToSendToWebBrowser += "          </ul>\n";
		stringToSendToWebBrowser += "        </div>\n";
		stringToSendToWebBrowser += "      </div>\n";
		stringToSendToWebBrowser += "    </div>\n";
		
		if(showMenu) {
			stringToSendToWebBrowser += "    <div class=\"section\">\n";
			stringToSendToWebBrowser += banner;
			stringToSendToWebBrowser += "      <div class=\"container\">\n";
			stringToSendToWebBrowser += "        <div class=\"row\">\n";
			stringToSendToWebBrowser += "          <div class=\"col-md-12\">\n";
			stringToSendToWebBrowser += "            <h1 class=\"main-header\">PLAYLISTS</h1>\n";
			stringToSendToWebBrowser += "            <br>\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "        </div>\n";
			stringToSendToWebBrowser += "        <div class=\"row\">\n";
			stringToSendToWebBrowser += "          <div class=\"col-md-12\">\n";
			stringToSendToWebBrowser += "            <h3 style=\"text-align:center\">CREATE PLAYLIST</h3>\n";
			stringToSendToWebBrowser += "            <br>\n";
			stringToSendToWebBrowser += "            <form role=\"form\" mode=\"GET\" action=\"playlists\" id=\"playlist-form-create\">\n";
			stringToSendToWebBrowser += "              <div class=\"form-group\">\n";
			stringToSendToWebBrowser += "                <input placeholder=\"Enter new playlist name\" type=\"text\" id=\"formCreateValue\" class=\"form-control\">\n";
			stringToSendToWebBrowser += "              </div>\n";
			stringToSendToWebBrowser += "              <button type=\"submit\" class=\"btn btn-block btn-default\" s=\"\" onclick=\"createPlaylist()\">Create</button>\n";
			stringToSendToWebBrowser += "              <input type=\"hidden\" value=\"create\" name=\"type\">";
			stringToSendToWebBrowser += "              <input type=\"hidden\" value=\"\" name=\"playlistName\" id=\"finalCreateName\">\n";
			stringToSendToWebBrowser += "              <input type=\"hidden\" value=\"\" name=\"username\" id=\"finalCreateUser\">\n";
			stringToSendToWebBrowser += "            </form>\n";
			stringToSendToWebBrowser += "            <br>\n";
			stringToSendToWebBrowser += "            <h3 style=\"text-align:center\">REMOVE PLAYLIST</h3>\n";
			stringToSendToWebBrowser += "            <br>\n";
			stringToSendToWebBrowser += "            <form role=\"form\" mode=\"GET\" action=\"playlists\" id=\"playlist-form-remove\">\n";
			stringToSendToWebBrowser += "              <div class=\"form-group\">\n";
			stringToSendToWebBrowser += "                <select class=\"form-control\" id=\"formRemoveValue\">\n";
			stringToSendToWebBrowser += "                  <option value=\"null\" selected=\"selected\">Select a playlist to remove</option>\n";
			stringToSendToWebBrowser += selectOptions;
			stringToSendToWebBrowser += "                </select>\n";
			stringToSendToWebBrowser += "              </div>\n";
			stringToSendToWebBrowser += "              <input type=\"hidden\" value=\"remove\" name=\"type\">";
			stringToSendToWebBrowser += "              <button type=\"submit\" class=\"btn btn-block btn-default\" s=\"\" onclick=\"removePlaylist()\">Remove</button>\n";
			stringToSendToWebBrowser += "              <input type=\"hidden\" value=\"\" name=\"playlistID\" id=\"finalRemovePlaylistID\">\n";
			stringToSendToWebBrowser += "            </form>\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "        </div>\n";
		}
		
		stringToSendToWebBrowser += additionalHTML;
	
		stringToSendToWebBrowser += "        <div class=\"section\" id=\"footer\">\n";
		stringToSendToWebBrowser += "          <div class=\"container\">\n";
		stringToSendToWebBrowser += "            <div class=\"row\">\n";
		stringToSendToWebBrowser += "              <div class=\"col-md-12\">\n";
		stringToSendToWebBrowser += "                <div id=\"footer\">\n";
		stringToSendToWebBrowser += "                  <p>e.mu 2017</p>\n";
		stringToSendToWebBrowser += "                </div>\n";
		stringToSendToWebBrowser += "              </div>\n";
		stringToSendToWebBrowser += "            </div>\n";
		stringToSendToWebBrowser += "          </div>\n";
		stringToSendToWebBrowser += "        </div>\n";
		stringToSendToWebBrowser += "      </div>\n";
		stringToSendToWebBrowser += "    </div>\n";
		stringToSendToWebBrowser += "  </body>\n";
		stringToSendToWebBrowser += "\n";
		stringToSendToWebBrowser += "</html>\n";
		
		return stringToSendToWebBrowser;
	}

}