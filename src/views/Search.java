package views;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.h2.mvstore.MVMap;

import model.Account;
import model.Playlist;
import model.Song;
import model.Utilities;
import storage.DatabaseInterface;
import storage.FileStoreInterface;
import web.WebRequest;
import web.WebResponse;

public class Search extends DynamicWebPage
{
		public Search(DatabaseInterface db,FileStoreInterface fs)
		{
			super(db,fs);
		}
		
		public boolean process(WebRequest toProcess)
		{
			boolean loggedIn = verifyCookie(toProcess);
			
			if(toProcess.path.equalsIgnoreCase("Search"))
			{
				String stringToSendToWebBrowser = getMainHTML("", loggedIn);
				toProcess.r = new WebResponse( WebResponse.HTTP_OK, WebResponse.MIME_HTML, stringToSendToWebBrowser );
				return true;
			} else if(toProcess.path.equalsIgnoreCase("search/resultsAdv")) {
				ArrayList<String> songKeyWords = new ArrayList<String>(Utilities.trimArrayListString(Utilities.convertCSVStringToArrayList(toProcess.params.get("songKeyWords"))));
				ArrayList<String> artists = new ArrayList<String>(Utilities.convertCSVStringToArrayList(toProcess.params.get("artists")));
				ArrayList<String> primaryEmotions = new ArrayList<String>(Utilities.convertCSVStringToArrayList(toProcess.params.get("primaryEmotions")));
				ArrayList<String> secondaryEmotions = new ArrayList<String>(Utilities.convertCSVStringToArrayList(toProcess.params.get("secondaryEmotions")));
				ArrayList<String> genres = new ArrayList<String>(Utilities.convertCSVStringToArrayList(toProcess.params.get("genres")));
				ArrayList<Song> songMatches = new ArrayList<Song>();
				
				System.out.println("\n# # ADVANCED SEARCH QUERY # #");
				System.out.println("Song Key Words: " + songKeyWords.toString());
				System.out.println("Artists: " + artists.toString());
				System.out.println("Primarys: " + primaryEmotions.toString());
				System.out.println("Secondarys: " + secondaryEmotions.toString());
				System.out.println("Genres: " + genres.toString());
				
				System.out.println("\n# STARTING SEARCH...");
				songMatches = findSongMatchesAdvanced(songKeyWords, artists, primaryEmotions, secondaryEmotions, genres);
				System.out.println("\nMATCHES FOUND: " + songMatches.size());
				for(Song song : songMatches) {
					System.out.println("- " + song.title + " by " + song.artists);
					System.out.println(song.toString());
				}
				System.out.println("");
				
				String query = generateAdvSearchQueryString(songKeyWords, artists, primaryEmotions, secondaryEmotions, genres);
				
				// Send page to client
				String stringToSendToWebBrowser = getMainHTML(getResultsHTML(songMatches, query), loggedIn);
				toProcess.r = new WebResponse( WebResponse.HTTP_OK, WebResponse.MIME_HTML, stringToSendToWebBrowser );
				return true;
			} else if(toProcess.path.equalsIgnoreCase("search/resultsStn")) {
				ArrayList<String> querys = new ArrayList<String>(Utilities.trimArrayListString(Utilities.convertCSVStringToArrayList(toProcess.params.get("querys"))));
				System.out.println("\n# # STANDARD SEARCH QUERY # #");
				System.out.println("Search query words: " + querys);
				ArrayList<Song> songMatches = findSongMatchesStandard(querys);
				
				
				String stringToSendToWebBrowser = getMainHTML(getResultsHTML(songMatches, Utilities.convertArrayListToString(querys)), loggedIn);
				toProcess.r = new WebResponse( WebResponse.HTTP_OK, WebResponse.MIME_HTML, stringToSendToWebBrowser );
				return true;
			} else if(toProcess.path.equalsIgnoreCase("search/debug")) {
                                String songID = "111";
				if(toProcess.params.get("value").equalsIgnoreCase("add")) {
					/*
					 * ADDING A SONG
					 */
					ArrayList<ArrayList<String>> purchaseLinks = new ArrayList<ArrayList<String>>();
					purchaseLinks.add(new ArrayList<String>(Arrays.asList("Download/Stream", "http://smarturl.it/becausetheinternet")));
					Song song = new Song(Long.toString(System.currentTimeMillis()),"3005",235,"Because the Internet","https://upload.wikimedia.org/wikipedia/en/7/70/Childish-gambino-because-the-internet.gif",new ArrayList<String>(Arrays.asList("Childish Gambino")),new ArrayList<String>(Arrays.asList("randb-soul")), new ArrayList<String>(Arrays.asList("sadness","fear")),new ArrayList<String>(Arrays.asList("loneliness","nervousness")), purchaseLinks, "https://s-media-cache-ak0.pinimg.com/originals/b0/23/06/b023066408d2da8a3a8a7cd3913b2b10.jpg", "https://www.youtube.com/embed/tG35R8F2j8k");
					addSongToDatabase(song);
					System.out.println("Added song: " + song.title + " | " + song.Id);	
					System.out.println("# # ADDED DEBUG SONG #Â #");
				} else if(toProcess.params.get("value").equalsIgnoreCase("remove")) {
					/*
					 * REMOVING A SONG
					 */
					MVMap<String, Playlist> playlistsMap = db.s.openMap("PlaylistsMap");
					MVMap<String, Song> songsMap = db.s.openMap("Songs");
					for(String key : playlistsMap.keySet()) {
						if(playlistsMap.get(key).songIdList.contains(songID)) {
							Playlist playlist = playlistsMap.get(key);
							playlist.songIdList.remove(songID);
							playlist.totalRuntime -= songsMap.get(songID).runtime;
							playlistsMap.put(playlist.Id, playlist);
						}
					}
					songsMap.remove(songID);
					db.commit();
					System.out.println("# # REMOVED DEBUG SONG #Â #");
				}
				
				String stringToSendToWebBrowser = getMainHTML("", loggedIn);
				toProcess.r = new WebResponse( WebResponse.HTTP_OK, WebResponse.MIME_HTML, stringToSendToWebBrowser );
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
		
		public String generateAdvSearchQueryString(ArrayList<String> songKeyWords, ArrayList<String> artists, ArrayList<String> primaryEmotions, ArrayList<String> secondaryEmotions, ArrayList<String> genres) {
			String query = "";
			
			if(!Utilities.checkForSingleEmptyStringArrayList(songKeyWords))
				query += "Song Key Words | "  + Utilities.convertArrayListToString(songKeyWords) + "<br>";
			if(!Utilities.checkForSingleEmptyStringArrayList(artists))
				query += "Artists | "  + Utilities.convertArrayListToString(artists) + "<br>";
			if(!Utilities.checkForSingleEmptyStringArrayList(primaryEmotions))
				query += "Primary Emotions | "  + Utilities.convertArrayListToString(primaryEmotions) + "<br>";
			if(!Utilities.checkForSingleEmptyStringArrayList(secondaryEmotions))
				query += "Secondary Emotions | "  + Utilities.convertArrayListToString(secondaryEmotions) + "<br>";
			if(!Utilities.checkForSingleEmptyStringArrayList(genres))
				query += "Genres | "  + Utilities.convertArrayListToString(genres) + "<br>";
			
			return query;
		}
		
		public boolean updateSongInDatabase(Song song) {
			//Store song
			try {
				MVMap<String, Song> songsMap = db.s.openMap("Songs");
				songsMap.put(song.Id, song);
				db.commit();
				
				return true;
			} catch(Exception e) {
				System.out.println(e.getMessage());
				return false;
			}
			
		}
		
		public ArrayList<Song> getSongArrayFromDatabase() {
			ArrayList<Song> returnArray = new ArrayList<Song>();
			try {
				MVMap<String, Song> songsMap = db.s.openMap("Songs");
				List<String> songKeys = songsMap.keyList();
				ArrayList<Song> songList = new ArrayList<Song>();
				
				// Retrieve songs
				songKeys = songsMap.keyList();
				System.out.println("\n# RETRIEVING SONGS FROM DATABASE\nTotal songs stored: " + songKeys.size() + "\nsongKeys: " + songKeys);
				for(int i = 0; i < songKeys.size(); i++) {
					String songUniqueId = songKeys.get(i);
					Song song = songsMap.get(songUniqueId);
					songList.add(song);
					System.out.println("\nID: " + song.Id + "\nTitle: " + song.title);
				}
				System.out.println("");
				returnArray =  new ArrayList<Song>(songList);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			return returnArray;
			
		}
		
		public boolean addSongToDatabase(Song song) {
			boolean returnBool = false;
			try {
				MVMap<String, Song> songsMap = db.s.openMap("Songs");
				songsMap.put(song.Id, song);
				db.commit();
				returnBool = true;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			return returnBool;
		}
		
		public String getResultsHTML(ArrayList<Song> songs, String query) {
			String resultsPlural = " ";
			if(songs.size() > 1 || songs.size() == 0) {
				resultsPlural = "s ";
			}
			String stringToSendToWebBrowser = "";
			stringToSendToWebBrowser += "      <div class=\"section\" id=\"results-section\">\n";
			stringToSendToWebBrowser += "        <div class=\"container\">\n";
			stringToSendToWebBrowser += "          <div class=\"row\">\n";
			stringToSendToWebBrowser += "            <div class=\"col-md-12\">\n";
			stringToSendToWebBrowser += "              <hr>\n";
			stringToSendToWebBrowser += "            </div>\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "          <div class=\"row\">\n";
			stringToSendToWebBrowser += "            <div class=\"col-md-12\">\n";
			stringToSendToWebBrowser += "              <h1 id=\"results\">RESULTS</h1>\n";
			stringToSendToWebBrowser += "              <h4 id=\"results-found\">" + songs.size() + " result" + resultsPlural + "found for</h4>\n";
			stringToSendToWebBrowser += "              <h5 id=\"results-query\"><i>" + query + "</i></h4><br>\n";
			stringToSendToWebBrowser += "              <ul class=\"media-list\">\n";
			for(Song song : songs) {
				String streamingURL = "\\play?song=" + song.Id;
				String tempString = new String();
				tempString += "                <li class=\"media\">\n";
				tempString += "                  <a class=\"pull-left\" href=\"#\"><img class=\"media-object\" src=\"" + song.albumThumbnailURL + "\" height=\"80\" width=\"80\"></a>\n";
				tempString += "                  <div class=\"media-body\">\n";
				tempString += "                    <h4 class=\"media-heading\">\n";
				tempString += "                      <a href=\"" + streamingURL + "\">" + song.title.toUpperCase() + "</a>\n";
				tempString += "                    </h4>\n";
				tempString += "                    <h5 class=\"media-heading text-center\">\n";
				tempString += "                      <a href=\"" + streamingURL + "\">" + Utilities.convertArrayListToString(song.artists).toUpperCase() + "</a>\n";
				tempString += "                    </h5>\n";
				tempString += "                    <h6 class=\"media-heading text-center\">\n";
				tempString += "                      <a href=\"" + streamingURL + "\">" + Utilities.convertArrayListToString(song.primaryEmotionalCategories).toUpperCase() + " | " + Utilities.convertArrayListToString(song.secondaryEmotionalCategories).toUpperCase() + "</a>\n";
				tempString += "                    </h6>\n";
				tempString += "                    <h6 class=\"media-heading text-center\">\n";
				tempString += "                      <a href=\"" + streamingURL + "\">" + Utilities.convertArrayListToString(song.genres).toUpperCase() + "</a>\n";
				tempString += "                    </h6>\n";
				tempString += "                  </div>\n";
				tempString += "                </li>\n";
				
				stringToSendToWebBrowser += tempString;
			}
			stringToSendToWebBrowser += "              </ul>\n";
			stringToSendToWebBrowser += "            </div>\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "        </div>\n";
			stringToSendToWebBrowser += "      </div>\n";
			return stringToSendToWebBrowser;
			
		}
		
		public ArrayList<Song> findSongMatchesStandard(ArrayList<String> querys) {
			System.out.println("# PERFORMING SEARCH #");
			ArrayList<Song> songList = new ArrayList<Song>(getSongArrayFromDatabase());
			ArrayList<Song> songMatches = new ArrayList<Song>();
			
			HashMap<Song, Integer> songScores = new HashMap<Song, Integer>();
			for(Song song : songList) {
				System.out.println("Song: '" + song.title + "' | ID: " + song.Id);
				System.out.println("Song String: " + song.toString());
				HashMap<String, Integer> hashMapMatches = new HashMap<String, Integer>();
				int score = 0;
				
				for(String item : querys) {
					hashMapMatches.put(item, 0);
				}
				System.out.println("Hash Map: " + hashMapMatches.toString());
				
				
				List<String> songWords = Arrays.asList(song.toString().split(" "));

				for(String key : hashMapMatches.keySet()) {
					for(String word : songWords) {
						if(word.toLowerCase().contains(key.toLowerCase())) {
							System.out.println("! WORD MATCH: " + word);
							hashMapMatches.put(key, hashMapMatches.get(key).intValue() + 1);
							score++;
						}
					}
				}
				System.out.println("Final Hash Map: " + hashMapMatches.toString() + "\n");
				songScores.put(song, score);
			}
			
			ArrayList<Song> keysToRemove = new ArrayList<Song>();
			for(Song key : songScores.keySet()) {
				if(songScores.get(key).intValue() == 0) {
					keysToRemove.add(key);
				}
			}
			
			for(Song key : keysToRemove) {
				songScores.remove(key);
			}
			
			List<Entry<Song, Integer>> sortedSongList = sortSongsByScore(songScores);
			String finalSongs = new String();
			for(Entry<Song, Integer> song : sortedSongList) {
				songMatches.add(song.getKey());
				finalSongs += "Song: '" + song.getKey().title + "' | ID: " + song.getKey().Id + "\n";
			}
			System.out.println("Matches: \n" + finalSongs);
			
			return songMatches;
		}
		
		// Finds songs that exclusively match all conditions
		public ArrayList<Song> findSongMatchesAdvanced(ArrayList<String> songKeyWords, ArrayList<String> artists, ArrayList<String> primaryEmotions, ArrayList<String> secondaryEmotions, ArrayList<String> genres) {
			ArrayList<Song> songList = new ArrayList<Song>(getSongArrayFromDatabase());
			ArrayList<Song> songMatches = new ArrayList<Song>();
			ArrayList<Song> nonMatchSongs = new ArrayList<Song>();
			HashMap<Song, Integer> songScores = new HashMap<Song, Integer>();
			
			boolean searchSongKeyWords = false;
			boolean searchArtists = false;
			boolean searchPrimaryEmotions = false;
			boolean searchSecondaryEmotions = false;
			boolean searchGenres = false;
			
			// Search by song key words
			searchSongKeyWords = Utilities.checkForSingleEmptyStringArrayList(songKeyWords);
			System.out.println("\n# SEARCH BY SONG KEY WORDS: " + !searchSongKeyWords);
			if(!searchSongKeyWords) {
				for(Song song : songList) {
					for(String songTitleWord : Utilities.convertSpacedStringToArrayList(song.title)) {
						for(String songKeyWord : songKeyWords) {
							if(songTitleWord.equalsIgnoreCase(songKeyWord)) {
								System.out.println("ADDED: '" + song.title + "' to matches");
								if(songScores.containsKey(song)) {
									songScores.put(song, songScores.get(song).intValue() + 1);
								} else {
									songScores.put(song, 1);
								}
							}
						}
					}
				}
			} else {
				songScores = new HashMap<Song, Integer>();
				for(Song song : songList) {
					songScores.put(song, 0);
				}
			}
			
			// Search by artist
			searchArtists = Utilities.checkForSingleEmptyStringArrayList(artists);
			System.out.println("\n# SEARCH BY ARTIST: " + !searchArtists);
			if(!searchArtists) {
				for(Song song : songScores.keySet()) {
					boolean match = false;
					for(String songArtist : song.artists) {
						for(String artist : artists) {
							if(songArtist.equalsIgnoreCase(artist) || songArtist.toLowerCase().contains(artist.toLowerCase())) {
								System.out.println("! MATCH FOUND");
								if(songScores.containsKey(song)) {
									songScores.put(song, songScores.get(song).intValue() + 1);
								} else {
									songScores.put(song, 1);
								}
								match = true;
							}
						}
					}
					if(match == false) {
						System.out.println("! MATCH NOT FOUND - REMOVING: " + song.title);
						nonMatchSongs.add(song);
					}
				}
				
				for(int i = 0; i < songScores.size(); i++) {
					for(Song song : nonMatchSongs) {
						if(songScores.containsKey(song)) {
							songScores.remove(song);
						}
					}
				}
			}
			
			// Search by primary emotions
			searchPrimaryEmotions = Utilities.checkForSingleEmptyStringArrayList(primaryEmotions);
			System.out.println("\n# SEARCH BY PRIMARY EMOTIONS: " + !searchPrimaryEmotions);
			if(!searchPrimaryEmotions) {
				for(Song song : songScores.keySet()) {
					boolean match = false;
					System.out.println("\nCURRENT SONG: '" + song.title + "'\nCURRENT SONG PECs: " + song.primaryEmotionalCategories);
					System.out.println("SELECTED PECs: " + primaryEmotions);
					for(String songPrimaryEmotion : song.primaryEmotionalCategories) {
						for(String primaryEmotion : primaryEmotions) {
							if(songPrimaryEmotion.equalsIgnoreCase(primaryEmotion)) {
								System.out.println("! MATCH FOUND");
								if(songScores.containsKey(song)) {
									songScores.put(song, songScores.get(song).intValue() + 1);
								} else {
									songScores.put(song, 1);
								}
								match = true;
							}
						}
					}
					if(match == false) {
						System.out.println("! MATCH NOT FOUND - REMOVING: " + song.title);
						nonMatchSongs.add(song);
					}
				}
				
				for(int i = 0; i < songScores.size(); i++) {
					for(Song song : nonMatchSongs) {
						if(songScores.containsKey(song)) {
							songScores.remove(song);
						}
					}
				}
			}
			
			// Search by secondary emotions
			searchSecondaryEmotions = Utilities.checkForSingleEmptyStringArrayList(secondaryEmotions);
			System.out.println("\n# SEARCH BY SECONDARY EMOTIONS: " + !searchSecondaryEmotions);
			if(!searchSecondaryEmotions) {
				for(Song song : songScores.keySet()) {
					boolean match = false;
					System.out.println("\nCURRENT SONG: '" + song.title + "'\n CURRENT SONG SECs: " + song.secondaryEmotionalCategories);
					System.out.println("SELECTED SECs: " + secondaryEmotions);
					for(String songSecondaryEmotion : song.secondaryEmotionalCategories) {
						for(String secondaryEmotion : secondaryEmotions) {
							if(songSecondaryEmotion.equalsIgnoreCase(secondaryEmotion)) {
								System.out.println("! MATCH FOUND");
								if(songScores.containsKey(song)) {
									songScores.put(song, songScores.get(song).intValue() + 1);
								} else {
									songScores.put(song, 1);
								}
								match = true;
							}
						}
					}
					if(match == false) {
						System.out.println("! MATCH NOT FOUND - REMOVING: " + song.title);
						nonMatchSongs.add(song);
					}
				}
				
				for(int i = 0; i < songScores.size(); i++) {
					for(Song song : nonMatchSongs) {
						if(songScores.containsKey(song)) {
							songScores.remove(song);
						}
					}
				}
			}
			
			// Search by genres
			searchGenres = Utilities.checkForSingleEmptyStringArrayList(genres);
			System.out.println("\n# SEARCH BY GENRES: " + !searchGenres);
			if(!searchGenres) {
				for(Song song : songScores.keySet()) {
					boolean match = false;
					System.out.println("\nCURRENT SONG: '" + song.title + "'\n CURRENT SONG GENRES: " + song.genres);
					System.out.println("SELECTED GENRES: " + genres);
					for(String songGenre : song.genres) {
						for(String genre : genres) {
							if(songGenre.equalsIgnoreCase(genre)) {
								System.out.println("! MATCH FOUND");
								if(songScores.containsKey(song)) {
									songScores.put(song, songScores.get(song).intValue() + 1);
								} else {
									songScores.put(song, 1);
								}
								match = true;
							}
						}
					}
					if(match == false) {
						System.out.println("! MATCH NOT FOUND - REMOVING: " + song.title);
						nonMatchSongs.add(song);
					}
				}
				
				for(int i = 0; i < songScores.size(); i++) {
					for(Song song : nonMatchSongs) {
						if(songScores.containsKey(song)) {
							songScores.remove(song);
						}
					}
				}
			}	
			
			if(searchSongKeyWords && searchArtists && searchPrimaryEmotions && searchSecondaryEmotions && searchGenres) {
				songMatches.clear();
			}
			
			List<Entry<Song, Integer>> sortedSongList = sortSongsByScore(songScores);
			for(Entry<Song, Integer> entry : sortedSongList) {
				songMatches.add(entry.getKey());
			}
			
			return removeDuplicateSongs(songMatches);
		}
		
		public List<Entry<Song, Integer>> sortSongsByScore(HashMap<Song, Integer> songHashMap) {
	        Set<Entry<Song, Integer>> set = songHashMap.entrySet();
	        List<Entry<Song, Integer>> list = new ArrayList<Entry<Song, Integer>>(set);
	        Collections.sort(list, new Comparator<Map.Entry<Song, Integer>>() {
	            public int compare(Map.Entry<Song, Integer> o1, Map.Entry<Song, Integer> o2) {
	            	return o2.getValue().compareTo(o1.getValue());
	            }
	        });

	        System.out.println("# SORTING RESULTS #");
	        for (Entry<Song, Integer> entry : list) {
	            System.out.println(entry.getValue() + " | " + entry.getKey().title);

	        }
			return list;
		}
		
		public ArrayList<Song> removeDuplicateSongs(ArrayList<Song> songList) {
			List<Song> newSongList = new ArrayList<>(songList);
			// add elements to al, including duplicates
			Set<Song> hs = new HashSet<>();
			hs.addAll(newSongList);
			newSongList.clear();
			newSongList.addAll(hs);
			
			return new ArrayList<Song>(newSongList);
		}
		
	    public ArrayList<Song> getSongArray() {
			ArrayList<Song> songList = new ArrayList<Song>();
			
			ArrayList<ArrayList<String>> purchaseLinks = new ArrayList<ArrayList<String>>();
			purchaseLinks.add(new ArrayList<String>(Arrays.asList("Atlantic", "https://atlanti.cr/2singles")));
			Song song1 = new Song("1493810293","Shape of You",235,"Divide","https://images-na.ssl-images-amazon.com/images/I/81MVp7I%2Bp8L._SY355_.jpg",new ArrayList<String>(Arrays.asList("Ed Sheeran")),new ArrayList<String>(Arrays.asList("pop", "singer-songwriter")), new ArrayList<String>(Arrays.asList("love", "anger")),new ArrayList<String>(Arrays.asList("joy")), purchaseLinks, "http://themixradio.co.uk/wp-content/uploads/2017/01/ed-sheeran-011.jpg", "https://www.youtube.com/embed/_dK2tDK9grQ");
			
			purchaseLinks.clear();
			purchaseLinks.add(new ArrayList<String>(Arrays.asList("iTunes", "http://smarturl.it/CloseriT")));
			purchaseLinks.add(new ArrayList<String>(Arrays.asList("Spotify", "http://smarturl.it/CloserStream")));
			Song song2 = new Song("1503802938", "Closer", 261, "Collage", "https://upload.wikimedia.org/wikipedia/en/2/22/Collage%2C_album_cover.jpg", new ArrayList<String>(Arrays.asList("The Chainsmokers", "Halsey")), new ArrayList<String>(Arrays.asList("singer-songwriter")), new ArrayList<String>(Arrays.asList("love")), new ArrayList<String>(Arrays.asList("rage-disgust")), purchaseLinks, "http://inyourspeakers.com/files/images/news/the_chainsmokers-15674.jpg", "https://www.youtube.com/embed/PT2_F-1esPk");

			purchaseLinks.clear();
			purchaseLinks.add(new ArrayList<String>(Arrays.asList("iTunes", "http://www.smarturl.it/TS1989")));
			Song song3 = new Song("1503808666", "Shake it Off", 242, "1989", "https://upload.wikimedia.org/wikipedia/en/f/f6/Taylor_Swift_-_1989.png", new ArrayList<String>(Arrays.asList("Taylor Swift")), new ArrayList<String>(Arrays.asList("pop")), new ArrayList<String>(Arrays.asList("love", "joy")), new ArrayList<String>(Arrays.asList("pride")), purchaseLinks, "https://static.independent.co.uk/s3fs-public/thumbnails/image/2016/01/29/11/Taylor-Swift-revenge-nerds.jpg", "https://www.youtube.com/embed/nfWlot6h_JM");

			songList.add(song1);
			songList.add(song2);
			songList.add(song3);
			
			return songList;
		}
		
		public String getMainHTML(String additionalHTML, boolean loggedIn) {
			String stringToSendToWebBrowser = "";
			stringToSendToWebBrowser += "<html><head>\n";
			stringToSendToWebBrowser += "    <meta charset=\"utf-8\">\n";
			stringToSendToWebBrowser += "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n";
			stringToSendToWebBrowser += "    <script type=\"text/javascript\" src=\"http://cdnjs.cloudflare.com/ajax/libs/jquery/2.0.3/jquery.min.js\"></script>\n";
			stringToSendToWebBrowser += "    <script type=\"text/javascript\" src=\"http://netdna.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js\"></script>\n";
			stringToSendToWebBrowser += "    <script type=\"text/javascript\" src=\"\\search.js\"></script>\n";
			stringToSendToWebBrowser += "    <link href=\"http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.3.0/css/font-awesome.min.css\" rel=\"stylesheet\" type=\"text/css\">\n";
			stringToSendToWebBrowser += "    <link href=\"\\search.css\" rel=\"stylesheet\" type=\"text/css\">\n";
			stringToSendToWebBrowser += "    <link href=\"https://fonts.googleapis.com/css?family=Lato\" rel=\"stylesheet\">\n";
			stringToSendToWebBrowser += "    <link  rel=\"icon\" href=\"\\favicon.png\">\n";
                        stringToSendToWebBrowser += "    <title>e.mu | Search</title>\n";
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
			stringToSendToWebBrowser += "            <li class=\"active\">\n";
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
			stringToSendToWebBrowser += "    <div class=\"section\">\n";
			stringToSendToWebBrowser += "      <div class=\"container\">\n";
			stringToSendToWebBrowser += "        <div class=\"row\">\n";
			stringToSendToWebBrowser += "          <div class=\"col-md-12\">\n";
			stringToSendToWebBrowser += "            <h1 class=\"main-header\">SEARCH</h1>\n";
			stringToSendToWebBrowser += "            <br>\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "        </div>\n";
			stringToSendToWebBrowser += "        <div class=\"row\">\n";
			stringToSendToWebBrowser += "          <div class=\"col-md-12\">\n";
			stringToSendToWebBrowser += "            <form id=\"standard-search-form\" role=\"form\" mode=\"GET\" action=\"/search/resultsStn\">\n";
			stringToSendToWebBrowser += "              <div class=\"form-group\">\n";
			stringToSendToWebBrowser += "                <input class=\"form-control\" placeholder=\"Enter search query\" type=\"search\" id=\"inputSearchStn\">\n";
			stringToSendToWebBrowser += "              </div>\n";
			stringToSendToWebBrowser += "              <button type=\"submit\" class=\"btn btn-block btn-default\" s=\"\" onclick=\"performStnSearch('inputSearchStn', 'finalStnQuery')\">Search</button>\n";
			stringToSendToWebBrowser += "              <div style=\"display: table; margin: 0 auto;\">\n";
			stringToSendToWebBrowser += "                <a href=\"#\" id=\"adv-search-link\" onclick=\"displayAdvSearch()\"><br>Click here for advanced search</a>\n";
			stringToSendToWebBrowser += "              </div>\n";
			stringToSendToWebBrowser += "              <input type=\"hidden\" value=\"\" name=\"querys\" id=\"finalStnQuery\">\n";
			stringToSendToWebBrowser += "            </form>\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "        </div>\n";
			stringToSendToWebBrowser += "      </div>\n";
			stringToSendToWebBrowser += "    </div>\n";
			stringToSendToWebBrowser += "    <form id=\"adv-search-form\" role=\"form\" action=\"/search/resultsAdv#results-section\" style=\"display:none;\">\n";
			stringToSendToWebBrowser += "      <div class=\"section\">\n";
			stringToSendToWebBrowser += "        <div class=\"container\">\n";
			stringToSendToWebBrowser += "          <div class=\"row\">\n";
			stringToSendToWebBrowser += "            <div class=\"col-md-12\">\n";
			stringToSendToWebBrowser += "              <hr>\n";
			stringToSendToWebBrowser += "            </div>\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "          <div class=\"row\">\n";
			stringToSendToWebBrowser += "            <div class=\"col-md-12\">\n";
			stringToSendToWebBrowser += "              <h3 style=\"text-align:center\">ADVANCED SEARCH</h3>\n";
			stringToSendToWebBrowser += "              <br>\n";
			stringToSendToWebBrowser += "              <div class=\"form-group\">\n";
			stringToSendToWebBrowser += "                <div class=\"col-sm-2\">\n";
			stringToSendToWebBrowser += "                  <label for=\"inputSongKeyWords\" class=\"control-label\">Song Key Words</label>\n";
			stringToSendToWebBrowser += "                </div>\n";
			stringToSendToWebBrowser += "                <div class=\"col-sm-10\">\n";
			stringToSendToWebBrowser += "                  <input type=\"text\" class=\"form-control\" placeholder=\"Rain or for multiple words use spaces\" id=\"inputSongKeyWords\">\n";
			stringToSendToWebBrowser += "                </div>\n";
			stringToSendToWebBrowser += "                <div class=\"col-sm-2\">\n";
			stringToSendToWebBrowser += "                  <label for=\"inputArtists\" class=\"control-label\">Artists</label>\n";
			stringToSendToWebBrowser += "                </div>\n";
			stringToSendToWebBrowser += "                <div class=\"col-sm-10\">\n";
			stringToSendToWebBrowser += "                  <input type=\"text\" class=\"form-control\" placeholder=\"Guns n' Roses or for multiple artists &quot;Ed Sheeran&quot;,&quot;The Chainsmokers&quot;\" id=\"inputArtists\">\n";
			stringToSendToWebBrowser += "                </div>\n";
			stringToSendToWebBrowser += "              </div>\n";
			stringToSendToWebBrowser += "            </div>\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "        </div>\n";
			stringToSendToWebBrowser += "      </div>\n";
			stringToSendToWebBrowser += "      <div class=\"section\">\n";
			stringToSendToWebBrowser += "        <div class=\"container\">\n";
			stringToSendToWebBrowser += "          <div class=\"row\">\n";
			stringToSendToWebBrowser += "            <div class=\"col-md-12\">\n";
			stringToSendToWebBrowser += "              <h4>EMOTIONAL CATEGORIES</h4>\n";
			stringToSendToWebBrowser += "              <table class=\"table\" id=\"adv-search-table\">\n";
			stringToSendToWebBrowser += "                <tbody>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent adv-search-top\" rowspan=\"2\" onclick=\"advSearchClickCell(this.id)\" id=\"love\">Love</td>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-child adv-search-top\" id=\"affection\" onclick=\"advSearchClickCell(this.id)\">Affection</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-child\" id=\"lust-longing\" onclick=\"advSearchClickCell(this.id)\">Lust/Longing</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" rowspan=\"6\" onclick=\"advSearchClickCell(this.id)\" id=\"joy\">Joy</td>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-child\" onclick=\"advSearchClickCell(this.id)\" id=\"cheerfulness\">Cheerfulness</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-child\" onclick=\"advSearchClickCell(this.id)\" id=\"contentment\">Contentment</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-child\" onclick=\"advSearchClickCell(this.id)\" id=\"pride\">Pride</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-child\" onclick=\"advSearchClickCell(this.id)\" id=\"optimism\">Optimism</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-child\" onclick=\"advSearchClickCell(this.id)\" id=\"enthrallment\">Enthrallment</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-child\" onclick=\"advSearchClickCell(this.id)\" id=\"relief\">Relief</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" rowspan=\"1\" id=\"surprise\" onclick=\"advSearchClickCell(this.id)\">Surprise</td>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-child\" onclick=\"advSearchClickCell(this.id)\" id=\"surprise\"></td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" rowspan=\"5\" id=\"anger\" onclick=\"advSearchClickCell(this.id)\">Anger</td>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-child\" onclick=\"advSearchClickCell(this.id)\" id=\"irritation\">Irritation</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-child\" onclick=\"advSearchClickCell(this.id)\" id=\"exasperation\">Exasperation</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-child\" onclick=\"advSearchClickCell(this.id)\" id=\"rage-disgust\">Rage/Disgust</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-child\" onclick=\"advSearchClickCell(this.id)\" id=\"envy\">Envy</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-child\" onclick=\"advSearchClickCell(this.id)\" id=\"torment\">Torment</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" rowspan=\"6\" id=\"sadness\" onclick=\"advSearchClickCell(this.id)\">Sadness</td>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-child\" onclick=\"advSearchClickCell(this.id)\" id=\"suffering\">Suffering</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-child\" onclick=\"advSearchClickCell(this.id)\" id=\"disappointment\">Disappointment</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-child\" onclick=\"advSearchClickCell(this.id)\" id=\"shame\">Shame</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-child\" onclick=\"advSearchClickCell(this.id)\" id=\"neglect\">Neglect</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-child\" onclick=\"advSearchClickCell(this.id)\" id=\"sympathy\">Sympathy</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-child\" onclick=\"advSearchClickCell(this.id)\" id=\"loneliness\">Loneliness</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" rowspan=\"2\" id=\"fear\" onclick=\"advSearchClickCell(this.id)\">Fear</td>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-child\" onclick=\"advSearchClickCell(this.id)\" id=\"horror\">Horror</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-child\" onclick=\"advSearchClickCell(this.id)\" id=\"nervousness\">Nervousness</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                </tbody>\n";
			stringToSendToWebBrowser += "              </table>\n";
			stringToSendToWebBrowser += "              <!--<button type=\"submit\" class=\"btn btn-block btn-default\" s=\"\" onclick=\"advSearchPrintSelectedCells()\">Search</button>-->\n";
			stringToSendToWebBrowser += "            </div>\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "        </div>\n";
			stringToSendToWebBrowser += "      </div>\n";
			stringToSendToWebBrowser += "      <div class=\"section\">\n";
			stringToSendToWebBrowser += "        <div class=\"container\">\n";
			stringToSendToWebBrowser += "          <div class=\"row\">\n";
			stringToSendToWebBrowser += "            <div class=\"col-md-12\">\n";
			stringToSendToWebBrowser += "              <h4>GENRES</h4>\n";
			stringToSendToWebBrowser += "              <table class=\"table\" id=\"adv-search-table\">\n";
			stringToSendToWebBrowser += "                <tbody>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent adv-search-top\" onclick=\"advSearchClickCell(this.id)\" id=\"alternative\">Alternative</td>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent adv-search-top\" id=\"blues\" onclick=\"advSearchClickCell(this.id)\">Blues</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"childrens-music\">Children's Music</td>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"classical\">Classical</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"comedy\">Comedy</td>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"commercial\">Commercical</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"country\">Country</td>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"dance\">Dance</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"edm\">EDM</td>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"disney\">Disney</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"easy-listening\">Easy Listening</td>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"electronic\">Electronic</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"enka\">Enka</td>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"french-pop\">French Pop</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"german-folk\">German Folk</td>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"german-pop\">German Pop</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"fitness\">Fitness</td>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"hip-hop-rap\">Hip-Hop/Rap</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"holiday\">Holiday</td>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"indie-pop\">Indie Pop</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"industrial\">Industrial</td>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"inspirational\">Inspirational</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"instrumental\">Instrumental</td>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"j-pop\">J-Pop</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"jazz\">Jazz</td>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"k-pop\">K-Pop</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"karaoke\">Karaoke</td>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"latin\">Latin</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"new-age\">New Age</td>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"opera\">Opera</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"pop\">Pop</td>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"randb-soul\">R&amp;B/Soul</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"reggae\">Reggae</td>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"rock\">Rock</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"singer-songwriter\">Singer/Songwriter</td>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"spoken-word\">Spoken Word</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"soundtrack\">Soundtrack</td>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"tex-mex\">Tex-Mex</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                  <tr>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"vocal\">Vocal</td>\n";
			stringToSendToWebBrowser += "                    <td class=\"adv-search-parent\" onclick=\"advSearchClickCell(this.id)\" id=\"world\">World</td>\n";
			stringToSendToWebBrowser += "                  </tr>\n";
			stringToSendToWebBrowser += "                </tbody>\n";
			stringToSendToWebBrowser += "              </table>\n";
			stringToSendToWebBrowser += "              <button type=\"submit\" class=\"btn btn-block btn-default\" s=\"\" onclick=\"performAdvSearch()\">Search</button>\n";
			stringToSendToWebBrowser += "            </div>\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "        </div>\n";
			stringToSendToWebBrowser += "      </div>\n";
			stringToSendToWebBrowser += "	   <input type=\"hidden\" value=\"\" id=\"finalSongKeyWords\" name=\"songKeyWords\">";
			stringToSendToWebBrowser += "      <input type=\"hidden\" value=\"\" id=\"finalArtists\" name=\"artists\">\n";
			stringToSendToWebBrowser += "      <input type=\"hidden\" value=\"\" id=\"finalPrimaryEmotions\" name=\"primaryEmotions\">\n";
			stringToSendToWebBrowser += "      <input type=\"hidden\" value=\"\" id=\"finalSecondaryEmotions\" name=\"secondaryEmotions\">\n";
			stringToSendToWebBrowser += "      <input type=\"hidden\" value=\"\" id=\"finalGenres\" name=\"genres\">\n";
			stringToSendToWebBrowser += "    </form>\n";
			stringToSendToWebBrowser += additionalHTML;
			stringToSendToWebBrowser += "    <div class=\"section\" id=\"footer\">\n";
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