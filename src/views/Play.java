package views;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.h2.mvstore.MVMap;

import model.Account;
import model.Playlist;
import model.Song;
import model.Utilities;
import storage.DatabaseInterface;
import storage.FileStoreInterface;
import web.WebRequest;
import web.WebResponse;

public class Play extends DynamicWebPage
{
		public Play(DatabaseInterface db,FileStoreInterface fs)
		{
			super(db,fs);
		}
		
		public boolean process(WebRequest toProcess)
		{
			if(toProcess.path.equalsIgnoreCase("Play"))
			{
				String songId = toProcess.params.get("song");
				String playlistId = toProcess.params.get("playlist");
				String index = toProcess.params.get("index");
				String add = toProcess.params.get("add");
				String playlistHTML = new String();
				String bannerHTML = new String();
				boolean playlistPresent = false;
				boolean loggedIn = false;
				MVMap<String, Song> songsMap = db.s.openMap("Songs");
				System.out.println("# # PLAY #ÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂ #\nParameters:\n- songId: " + songId + "\n- PlaylistId: " + playlistId + "\n- Index: " + index);
				
				if(verifyCookie(toProcess)) {
					loggedIn = true;
				}
				
				if(playlistId != null) {
					MVMap<String, Playlist> playlistsMap = db.s.openMap("PlaylistsMap");
					System.out.println("# # PLAYLISTS # #\n" + playlistsMap.keyList());
					Playlist playlist = playlistsMap.get(playlistId);
					if(playlist == null) {
						return errorEncountered(toProcess, "playlist");
					} else if(playlist.songIdList.size() == 0) {
						return errorEncountered(toProcess, "playlist");
					} else {
						if(add != null) {
							if(add.equalsIgnoreCase("true")) {
								if(verifyCookie(toProcess)) {
									String cookieUsername = toProcess.cookies.get("username");
									bannerHTML = addPlaylist(playlistId, cookieUsername);
								} else {
									bannerHTML = "<div id=\"playlist-red-banner\" class=\"text-center\">YOU MUST BE LOGGED IN TO USE THIS FEATURE</div>";
								}
							}
						}
						
						if(index == null) {
							index = "0";
						}
						
						int indexInt;
						try {
							indexInt = Integer.parseInt(index);
						} catch (Exception e) {
							indexInt = 0;
						}
						
						if(indexInt < 0 || indexInt > playlist.songIdList.size() - 1) {
							indexInt = 0;
						}
						playlistHTML += getPlaylistHTML(playlistsMap.get(playlistId), indexInt);
						playlistHTML += getRecommendationsHTML(songsMap.get(playlist.songIdList.get(indexInt)));
						playlistPresent = true;
						songId = playlist.songIdList.get(indexInt);
					}
				}
				
				if(songId == null) {
					return errorEncountered(toProcess, "song");
				}
				
				
				Song song = songsMap.get(songId);
				if(song == null) {
					return errorEncountered(toProcess, "song");
				} else {
					System.out.println("# # PLAY SONG #ÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂÃÂ #\nID: " + songId);
					System.out.println("Found song: " + song.title );
					String songTitle = song.title;
					String artists = Utilities.convertArrayListToString(song.artists);
					String genres = Utilities.convertArrayListToString(song.genres);
					String album = song.album;
					String primaryEmotions = Utilities.convertArrayListToString(song.primaryEmotionalCategories);
					String secondaryEmotions = Utilities.convertArrayListToString(song.secondaryEmotionalCategories);
					ArrayList<ArrayList<String>> purchaseLinks = new ArrayList<ArrayList<String>>();
					purchaseLinks = song.purchaseLinks;
					String backgroundURL = song.backgroundURL;
					String embeddedStreamingURL = song.embeddedStreamingURL;
					String ID = song.Id;
					
					System.out.println("\n# STREAMING '" + songTitle + "' #");
					System.out.println("Artists: " + artists);
					System.out.println("Genres: " + genres);
					System.out.println("Album: " + album);
					System.out.println("Primarys: " + primaryEmotions);
					System.out.println("Secondarys: " + secondaryEmotions);
					System.out.println("Purchase Links: " + purchaseLinks.toString());
					System.out.println("Background URL: " + backgroundURL);
					System.out.println("Embedded Streaming URL: " + embeddedStreamingURL);
					System.out.println("");
					
					if(!playlistPresent) {
						playlistHTML += getRecommendationsHTML(song);
					}
					
					// Send page to client
					String stringToSendToWebBrowser = getMainHTML(getStreamingHTML(songTitle, artists, album, genres, primaryEmotions, secondaryEmotions, purchaseLinks, backgroundURL, embeddedStreamingURL, ID) + playlistHTML, "Playing | " + songTitle, bannerHTML, loggedIn);
					toProcess.r = new WebResponse( WebResponse.HTTP_OK, WebResponse.MIME_HTML, stringToSendToWebBrowser );
				}
				
				return true;
			}
			return false;
		}
		
		public boolean verifyCookie(WebRequest toProcess) {
			String username = toProcess.cookies.get("username");
			String password = toProcess.cookies.get("password");
			
			MVMap<String, Account> accountsMap = db.s.openMap("Accounts");
			Account account = accountsMap.get(username);
			
			if(account != null) {
				if(account.password.equals(password)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		
		// Verify cookie before executing this
		public String addPlaylist(String playlistID, String cookieUsername) {
			MVMap<String, Account> accountsMap = db.s.openMap("Accounts");
			Account account = accountsMap.get(cookieUsername);
			if(account != null) {
				if(!account.playlistsLiked.contains(playlistID)) {
					MVMap<String, Playlist> playlistsMap = db.s.openMap("PlaylistsMap");
					if(playlistsMap.get(playlistID).userCreatedBy.equalsIgnoreCase(cookieUsername)) {
						return "<div id=\"playlist-red-banner\" class=\"text-center\">YOU CANNOT ADD YOUR OWN PLAYLIST</div>";
					} else {
						account.playlistsLiked.add(playlistID);
						accountsMap.put(cookieUsername, account);
						db.commit();
						System.out.println("Add playlist " + playlistID + " successful");
						return "<div id=\"playlist-green-banner\" class=\"text-center\">PLAYLIST ADDED</div>";
					}
				} else {
					System.out.println("Playlist " + playlistID + " already added");
					return "<div id=\"playlist-red-banner\" class=\"text-center\">YOU ALREADY ADDED THIS PLAYLIST</div>";
				}
			} else {
				System.out.println("ADD PLAYLIST FAILED");
				return "<div id=\"playlist-red-banner\" class=\"text-center\">ENCOUNTERED ERROR FINDING ACCOUNT</div>";
			}
			
			
		}
		
		public String getRecommendationsHTML(Song song) {
			System.out.println("# GENERATING RECOMMENDED SONGS #");
			Search search = new Search(db,fs);
			ArrayList<String> tempSongIDs = new ArrayList<String>();
			ArrayList<Song> tempSongList = new ArrayList<Song>();
			String chosenType = new String();
			boolean makingPlaylist = true;
			while(makingPlaylist) {
				
				Random random = new Random();
				switch(random.nextInt(4)) {
					case 0:
						chosenType = "Recommended song artists chosen: " + song.artists;
						tempSongList = search.findSongMatchesStandard(song.artists);
						break;
					case 1:
						chosenType = "Recommended song genres chosen: " + song.genres;
						tempSongList = search.findSongMatchesStandard(song.genres);
						break;
					case 2:
						chosenType = "Recommended song primary emotions chosen: " + song.primaryEmotionalCategories;
						tempSongList = search.findSongMatchesStandard(song.primaryEmotionalCategories);
						break;
					case 3:
						chosenType = "Recommended song secondary emotions chosen: " + song.secondaryEmotionalCategories;
						tempSongList = search.findSongMatchesStandard(song.secondaryEmotionalCategories);
						break;
				}
				
				if(tempSongList.size() == 1) {
					if(tempSongList.get(0).Id == song.Id) {
						tempSongList.clear();
					}
				}
				
				if(!tempSongList.isEmpty()) {
					System.out.println(chosenType);
					for(Song tempSong : tempSongList) {
						if(tempSong.Id != song.Id) {
							System.out.println("Song: '" + tempSong.title + "'");
							tempSongIDs.add(tempSong.Id);
						}
					}
					makingPlaylist = false;
				}
			}
			
			Playlist playlist = new Playlist("0", "RECOMMENDED", tempSongIDs, "e.mu", 0);
			return getPlaylistHTML(playlist, -2);
			
		}
		
		public boolean errorEncountered(WebRequest toProcess, String error) {
			boolean loggedIn = verifyCookie(toProcess);
			String stringToSendToWebBrowser = getMainHTML(getErrorHTML(error), "Play", "", loggedIn);
			toProcess.r = new WebResponse( WebResponse.HTTP_OK, WebResponse.MIME_HTML, stringToSendToWebBrowser );
			return true;
		}
		
		public String getErrorHTML(String error) {
			String stringToSendToWebBrowser = "";
			stringToSendToWebBrowser += "    <div class=\"section\" id=\"no-song-display\" wp-content=\"\" uploads=\"\" style=\"background: url(http://i.imgur.com/bguKSHt.jpg) no-repeat center center; background-size: cover;\">\n";
			stringToSendToWebBrowser += "      <div id=\"no-song-display-text\">\n";
			stringToSendToWebBrowser += "        <a href=\"enterPage.html\">\n";
			stringToSendToWebBrowser += "        <h3>THE " + error.toUpperCase() + " YOU'RE LOOKING FOR IS NOT AVAILABLE\n";
			stringToSendToWebBrowser += "          <br>CLICK HERE TO RETURN HOME</h3></a>\n";
			stringToSendToWebBrowser += "      </div>\n";
			stringToSendToWebBrowser += "    </div>\n";
			return stringToSendToWebBrowser;
		}
		
		/*
		 * Method for generating playlist HTML
		 * index > 0 | Play playlist at song of index
		 * index = -1 | Display playlist
		 * index = -2 | Display list of songs in playlist format
		 */
		public String getPlaylistHTML(Playlist playlist, int index) {
			String stringToSendToWebBrowser = "";
			String header = "";
			String sectionStyle = "";
			if(index == -1) {
				header = String.format("%1$s | %2$d Songs", playlist.name.toUpperCase(), playlist.songIdList.size());
			} else if(index == -2) { 
				header = String.format("%1$s", playlist.name.toUpperCase());
			} else {
				header = String.format("%1$s | %2$d of %3$d", playlist.name.toUpperCase(), index + 1, playlist.songIdList.size());
				sectionStyle = "style=\"background-color:#278ea3;\"";
			}
			
			stringToSendToWebBrowser += "    <div class=\"section\"" + sectionStyle + ">\n";
			stringToSendToWebBrowser += "      <div class=\"container\">\n";
			stringToSendToWebBrowser += "        <div class=\"row\">\n";
			stringToSendToWebBrowser += "          <div class=\"col-md-12\">\n";
			
			if(index > -1) {
				String previousString = new String();
				if(index <= 0) {
					previousString = "";
				} else {
					previousString += "              <h4 id=\"playlist-button\">\n";
					previousString += "                <a href=\"#\" onclick=\"playPreviousSong('" + playlist.Id + "+" + (index - 1) + "')\">PREVIOUS SONG</a>\n";
					previousString += "              </h4>\n";
				}
				
				
				String nextString = new String();
				if(index >= playlist.songIdList.size() - 1) {
					nextString = "";
				} else {
					nextString += "              <h4 id=\"playlist-button\">\n";
					nextString += "                <a href=\"#\" onclick=\"playNextSong('" + playlist.Id + "+" + (index + 1) + "')\">NEXT SONG</a>\n";
					nextString += "              </h4>\n";
				}
				
				
				stringToSendToWebBrowser += "            <div class=\"col-md-6 text-center\">\n";
				stringToSendToWebBrowser += previousString;
				stringToSendToWebBrowser += "            </div>\n";
				stringToSendToWebBrowser += "            <div class=\"col-md-6 text-center\">\n";
				stringToSendToWebBrowser += nextString;
				stringToSendToWebBrowser += "            </div>\n";
				stringToSendToWebBrowser += "            <br>\n";
			}
			
			stringToSendToWebBrowser += "            <h3 id=\"playlist-header\">" + header + "</h3>\n";
			stringToSendToWebBrowser += "		     <h4 style=\"display:inline;\" id=\"playlist-header\">by <i>"+ playlist.userCreatedBy + " </i></h4>";
			
			String addURLHTML = new String();
			if(index > -2)
				addURLHTML = "			 <a href=\"" + String.format("\\play?playlist=%1$s&index=%2$d&add=true", playlist.Id, index) + "\" id=\"add-playlist\">&nbsp;| Add playlist</a>\n" + "			 <a href=\"#\" id=\"add-playlist\" onclick=\"sharePrompt('" + String.format("localhost:8080/play?playlist=%1$s", playlist.Id) + "')\">&nbsp;| Share</a>\n";
			else
				addURLHTML = "";
			
			stringToSendToWebBrowser += addURLHTML;	
			stringToSendToWebBrowser += "            <ul class=\"media-list\">\n";
			stringToSendToWebBrowser += "              <li></li>\n";
			
			MVMap<String, Song> songsMap = db.s.openMap("Songs");
			int counter = 0;
			for(String id : playlist.songIdList) {
				Song song = songsMap.get(id);
				if(song != null) {
					String activeSong;
					if(counter == index) {
						activeSong =  " media-active";
					} else {
						activeSong = "";
					}
					String streamingURL = String.format("\\play?playlist=%1$s&index=%2$d", playlist.Id, counter);
					if(index == -2) {
						streamingURL = String.format("\\play?song=%1$s", id);
					}
					if(index == -2 && counter == 6) {
						break;
					}
					String songString = new String();
					songString += "              <li id=\"playlist-item\" class=\"media col-md-4" + activeSong + "\">\n";
					songString += "                <div class=\"media-object\"><a class=\"pull-left\" href=\"" + streamingURL + "\"><img src=\"" + song.albumThumbnailURL +"\" height=\"80\" width=\"80\"></a></div>\n";
					songString += "                <div class=\"media-body\">\n";
					songString += "                  <h4 class=\"media-heading text-center\">\n";
					songString += "                    <a href=\""+ streamingURL +"\">" + song.title.toUpperCase() + "</a>\n";
					songString += "                  </h4>\n";
					songString += "                  <h5 class=\"media-heading text-center\">\n";
					songString += "                    <a href=\"" + streamingURL + "\">" + Utilities.convertArrayListToString(song.artists).toUpperCase() + "</a>\n";
					songString += "                  </h5>\n";
					songString += "                </div>\n";
					songString += "              </li>\n";
					
					stringToSendToWebBrowser += songString;
				}
				counter++;
			}
			
			stringToSendToWebBrowser += "            </ul>\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "        </div>\n";
			stringToSendToWebBrowser += "      </div>\n";
			stringToSendToWebBrowser += "    </div>\n";
			
			return stringToSendToWebBrowser;
		}
		
		public String getStreamingHTML(String title, String artists, String album, String genres, String primaryEmotions, String secondaryEmotions, ArrayList<ArrayList<String>> purchaseLinks, String backgroundURL, String embeddedStreamingURL, String ID) {
			String stringToSendToWebBrowser = "";
			stringToSendToWebBrowser += "    <div class=\"section\" id=\"song-section\" style=\"background: url(" + backgroundURL + ") center center;\">\n";
			stringToSendToWebBrowser += "      <div class=\"container\">\n";
			stringToSendToWebBrowser += "        <div class=\"row\">\n";
			stringToSendToWebBrowser += "          <div class=\"col-md-6\">\n";
			stringToSendToWebBrowser += "            <div class=\"embed-responsive embed-responsive-16by9\">\n";
			stringToSendToWebBrowser += "              <iframe class=\"embed-responsive-item\" src=\"" + embeddedStreamingURL + "\" allowfullscreen=\"\"></iframe>\n";
			stringToSendToWebBrowser += "            </div>\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "          <div class=\"col-md-6\" id=\"song-description\">\n";
			stringToSendToWebBrowser += "            <h1 id=\"song-title\">" + title.toUpperCase() + "</h1>\n";
			stringToSendToWebBrowser += "            <h3 id=\"song-artist\">" + artists.toUpperCase() + "</h3>\n";
			stringToSendToWebBrowser += "            <p id=\"song-extra\">PRIMARY EMOTIONS | " + primaryEmotions +"\n";
			stringToSendToWebBrowser += "              <br>SECONDARY EMOTIONS | " + secondaryEmotions + "\n";
			stringToSendToWebBrowser += "              <br>ALBUM | " + album + "\n";
			stringToSendToWebBrowser += "              <br>GENRES | " + genres + "\n";
			
			String purchaseLinksString = "";
			for(ArrayList<String> linkArray : purchaseLinks) {
				purchaseLinksString += linkArray.get(0) + ": <a href=\"" + linkArray.get(1) + "\">" + linkArray.get(1) + "</a><br>\n";
			}
			
			stringToSendToWebBrowser += "              <br><br>" + purchaseLinksString + "\n";
			stringToSendToWebBrowser += "			   <br><a href=\"" + "\\playlists?type=addSong&songID=" + ID + "\" target=\"_blank\"> + ADD TO PLAYLIST</a>";				
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "        </div>\n";
			stringToSendToWebBrowser += "      </div>\n";
			stringToSendToWebBrowser += "    </div>\n";
			return stringToSendToWebBrowser;
		}
		
		public String getMainHTML(String additionalHTML, String title, String banner, boolean loggedIn) {
			String stringToSendToWebBrowser = "";
			stringToSendToWebBrowser += "<html><head>\n";
			stringToSendToWebBrowser += "    <meta charset=\"utf-8\">\n";
			stringToSendToWebBrowser += "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n";
			stringToSendToWebBrowser += "    <script type=\"text/javascript\" src=\"http://cdnjs.cloudflare.com/ajax/libs/jquery/2.0.3/jquery.min.js\"></script>\n";
			stringToSendToWebBrowser += "    <script type=\"text/javascript\" src=\"http://netdna.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js\"></script>\n";
			stringToSendToWebBrowser += "    <script type=\"text/javascript\" src=\"\\search.js\"></script>\n";
			stringToSendToWebBrowser += "    <script type=\"text/javascript\" src=\"\\play.js\"></script>\n";
			stringToSendToWebBrowser += "    <link href=\"http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.3.0/css/font-awesome.min.css\" rel=\"stylesheet\" type=\"text/css\">\n";
			stringToSendToWebBrowser += "    <link href=\"\\play.css\" rel=\"stylesheet\" type=\"text/css\">\n";
			stringToSendToWebBrowser += "    <title>e.mu | " + title + "</title>\n";
                        stringToSendToWebBrowser += "    <link  rel=\"icon\" href=\"\\favicon.png\">\n";
			stringToSendToWebBrowser += "  </head><body>\n";
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
			stringToSendToWebBrowser += "            <li class=\"disabled\">\n";
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
			stringToSendToWebBrowser += "              <form class=\"navbar-form navbar-left\" role=\"search\" id=\"nav-bar-search\" action=\"/search/resultsStn\">\n";
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
			stringToSendToWebBrowser += banner;
			stringToSendToWebBrowser += additionalHTML;
			
			stringToSendToWebBrowser += "    <div class=\"section\">\n";
			stringToSendToWebBrowser += "      <div class=\"container\">\n";
			stringToSendToWebBrowser += "        <div class=\"row\">\n";
			stringToSendToWebBrowser += "          <div class=\"col-md-12\">\n";
			stringToSendToWebBrowser += "            <div id=\"footer\">\n";
			stringToSendToWebBrowser += "              <p>e.mu 2017</p>\n";
			stringToSendToWebBrowser += "            </div>\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "        </div>\n";
			stringToSendToWebBrowser += "      </div>\n";
			stringToSendToWebBrowser += "    </div>\n";
			stringToSendToWebBrowser += "  \n";
			stringToSendToWebBrowser += "\n";
			stringToSendToWebBrowser += "</body></html>\n";
			return stringToSendToWebBrowser;
		}
}