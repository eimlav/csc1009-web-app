package views;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.h2.mvstore.MVMap;

import model.Account;
import model.Song;
import model.UserUpload;
import storage.DatabaseInterface;
import storage.FileStoreInterface;
import web.WebRequest;
import web.WebResponse;

public class FormSubmitted extends DynamicWebPage
{

	public FormSubmitted(DatabaseInterface db,FileStoreInterface fs)
	{
		super(db,fs);
	}

	public boolean process(WebRequest toProcess)
	{
		if(toProcess.path.equalsIgnoreCase("FormSubmitted"))
		{
			System.out.println("# # UPLOAD FORM SUBMITTED # #");
			UserUpload newUpload = new UserUpload();
			newUpload.trackName = toProcess.params.get("trackName");
			newUpload.artistName = toProcess.params.get("artistName");
			newUpload.album = toProcess.params.get("album");
			newUpload.albumArt = toProcess.params.get("albumThumbnailURL");
			newUpload.audioFile = toProcess.params.get("embeddedYoutubeLink");
			
			MVMap<String, UserUpload> uploads = db.s.openMap("Uploads");
			
			boolean trackAvailable = true;
			System.out.println("UserUpload Keys:");
			uploads.remove("test");
			for(String key : uploads.keySet()) {
				System.out.println("- " + key);
				if(newUpload.trackName.equals(key)) trackAvailable  = false; 
			}
			
			if (!trackAvailable)
			{
				System.out.println("ERROR: TRACK NAME TAKEN");
				String stringToSendToWebBrowser = "";
				stringToSendToWebBrowser += "<html>\n";
				stringToSendToWebBrowser += "  \n";
				stringToSendToWebBrowser += "  <head>\n";
				stringToSendToWebBrowser += "    <meta charset=\"utf-8\">\n";
				stringToSendToWebBrowser += "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n";
				stringToSendToWebBrowser += "    <script type=\"text/javascript\" src=\"http://cdnjs.cloudflare.com/ajax/libs/jquery/2.0.3/jquery.min.js\"></script>\n";
				stringToSendToWebBrowser += "    <script type=\"text/javascript\" src=\"http://netdna.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js\"></script>\n";
				stringToSendToWebBrowser += "    <link href=\"http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.3.0/css/font-awesome.min.css\"\n";
				stringToSendToWebBrowser += "    rel=\"stylesheet\" type=\"text/css\">\n";
				stringToSendToWebBrowser += "    <link href=\"http://pingendo.github.io/pingendo-bootstrap/themes/default/bootstrap.css\"\n";
				stringToSendToWebBrowser += "    rel=\"stylesheet\" type=\"text/css\">\n";
				stringToSendToWebBrowser += "	 <title>e.mu | Upload Error</title>\n";
				stringToSendToWebBrowser += "    <link  rel=\"icon\" href=\"\\favicon.png\">\n";
				stringToSendToWebBrowser += "  </head>\n";
				stringToSendToWebBrowser += "  \n";
				stringToSendToWebBrowser += "  <body>\n";
				stringToSendToWebBrowser += "    <div class=\"cover\">\n";
				stringToSendToWebBrowser += "      <div class=\"navbar\">\n";
				stringToSendToWebBrowser += "        <div class=\"container\">\n";
				stringToSendToWebBrowser += "          <div class=\"navbar-header\">\n";
				stringToSendToWebBrowser += "            <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-ex-collapse\">\n";
				stringToSendToWebBrowser += "              <span class=\"sr-only\">Toggle navigation</span>\n";
				stringToSendToWebBrowser += "              <span class=\"icon-bar\"></span>\n";
				stringToSendToWebBrowser += "              <span class=\"icon-bar\"></span>\n";
				stringToSendToWebBrowser += "              <span class=\"icon-bar\"></span>\n";
				stringToSendToWebBrowser += "            </button>\n";
				stringToSendToWebBrowser += "            <a class=\"navbar-brand\" href=\"Index.html\"><span>Return</span></a>\n";
				stringToSendToWebBrowser += "          </div>\n";
				stringToSendToWebBrowser += "          <div class=\"collapse navbar-collapse\" id=\"navbar-ex-collapse\"></div>\n";
				stringToSendToWebBrowser += "        </div>\n";
				stringToSendToWebBrowser += "      </div>\n";
				stringToSendToWebBrowser += "      <div class=\"background-image-fixed cover-image\" style=\"background-image : url('http://pingendo.github.io/pingendo-bootstrap/assets/blurry/800x600/6.jpg')\"></div>\n";
				stringToSendToWebBrowser += "      <div class=\"container\">\n";
				stringToSendToWebBrowser += "        <div class=\"row\">\n";
				stringToSendToWebBrowser += "          <div class=\"col-md-12 text-center\">\n";
				stringToSendToWebBrowser += "            <h1 class=\"text-inverse\">Track Name already taken!</h1>\n";
				stringToSendToWebBrowser += "            <p class=\"text-inverse\"></p>\n";
				stringToSendToWebBrowser += "            <br>\n";
				stringToSendToWebBrowser += "            <br>\n";
				stringToSendToWebBrowser += "            <a href=\"/Upload.html\" class=\"btn btn-lg btn-primary\">Try Again<br></a>\n";
				stringToSendToWebBrowser += "          </div>\n";
				stringToSendToWebBrowser += "        </div>\n";
				stringToSendToWebBrowser += "      </div>\n";
				stringToSendToWebBrowser += "    </div>\n";
				stringToSendToWebBrowser += "  </body>\n";
				stringToSendToWebBrowser += "\n";
				stringToSendToWebBrowser += "</html>\n";
				toProcess.r = new WebResponse( WebResponse.HTTP_OK, WebResponse.MIME_HTML, stringToSendToWebBrowser );
				return true;
			}
			
			System.out.println("TRACK NAME AVAILABLE");
			uploads.put(newUpload.trackName, newUpload);
			db.commit();
			
			// Add Song to database
			String addSongError = addSong(toProcess);
			if(!addSongError.isEmpty()) {
				toProcess.r = new WebResponse( WebResponse.HTTP_OK, WebResponse.MIME_HTML, addSongError);
				return true;
			}
			
			String stringToSendToWebBrowser = "";
			stringToSendToWebBrowser += "<html>\n";
			stringToSendToWebBrowser += "  \n";
			stringToSendToWebBrowser += "  <head>\n";
			stringToSendToWebBrowser += "    <meta charset=\"utf-8\">\n";
			stringToSendToWebBrowser += "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n";
			stringToSendToWebBrowser += "    <script type=\"text/javascript\" src=\"http://cdnjs.cloudflare.com/ajax/libs/jquery/2.0.3/jquery.min.js\"></script>\n";
			stringToSendToWebBrowser += "    <script type=\"text/javascript\" src=\"http://netdna.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js\"></script>\n";
			stringToSendToWebBrowser += "    <link href=\"http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.3.0/css/font-awesome.min.css\"\n";
			stringToSendToWebBrowser += "    rel=\"stylesheet\" type=\"text/css\">\n";
			stringToSendToWebBrowser += "    <link href=\"http://pingendo.github.io/pingendo-bootstrap/themes/default/bootstrap.css\"\n";
			stringToSendToWebBrowser += "    rel=\"stylesheet\" type=\"text/css\">\n";
			stringToSendToWebBrowser += "	 <title>e.mu | Upload Successful</title>\n";
			stringToSendToWebBrowser += "    <link  rel=\"icon\" href=\"\\favicon.png\">\n";
			stringToSendToWebBrowser += "  </head>\n";
			stringToSendToWebBrowser += "  \n";
			stringToSendToWebBrowser += "  <body>\n";
			stringToSendToWebBrowser += "    <div class=\"cover\">\n";
			stringToSendToWebBrowser += "      <div class=\"navbar\">\n";
			stringToSendToWebBrowser += "        <div class=\"container\">\n";
			stringToSendToWebBrowser += "          <div class=\"navbar-header\">\n";
			stringToSendToWebBrowser += "            <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-ex-collapse\">\n";
			stringToSendToWebBrowser += "              <span class=\"sr-only\">Toggle navigation</span>\n";
			stringToSendToWebBrowser += "              <span class=\"icon-bar\"></span>\n";
			stringToSendToWebBrowser += "              <span class=\"icon-bar\"></span>\n";
			stringToSendToWebBrowser += "              <span class=\"icon-bar\"></span>\n";
			stringToSendToWebBrowser += "            </button>\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "          <div class=\"collapse navbar-collapse\" id=\"navbar-ex-collapse\"></div>\n";
			stringToSendToWebBrowser += "        </div>\n";
			stringToSendToWebBrowser += "      </div>\n";
			stringToSendToWebBrowser += "      <div class=\"background-image-fixed cover-image\" style=\"background-image : url('http://pingendo.github.io/pingendo-bootstrap/assets/blurry/800x600/10.jpg')\"></div>\n";
			stringToSendToWebBrowser += "      <div class=\"container\">\n";
			stringToSendToWebBrowser += "        <div class=\"row\">\n";
			stringToSendToWebBrowser += "          <div class=\"col-md-12 text-center\">\n";
			stringToSendToWebBrowser += "            <h1 class=\"text-inverse\">Submit Successful!</h1>\n";
			stringToSendToWebBrowser += "            <p class=\"text-inverse\">You have successfully submitted your music!\n";
			stringToSendToWebBrowser += "              <br>\n";
			stringToSendToWebBrowser += "            </p>\n";
			stringToSendToWebBrowser += "            <br>\n";
			stringToSendToWebBrowser += "            <br>\n";
			stringToSendToWebBrowser += "            <a class=\"btn btn-lg btn-primary\" href=\"search.html\">Return to Main Page</a>\n";
			stringToSendToWebBrowser += "            <a class=\"btn btn-lg btn-primary\" href=\"upload.html\">Upload More Music</a>\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "        </div>\n";
			stringToSendToWebBrowser += "      </div>\n";
			stringToSendToWebBrowser += "    </div>\n";
			stringToSendToWebBrowser += "  </body>\n";
			stringToSendToWebBrowser += "\n";
			stringToSendToWebBrowser += "</html>\n";
			toProcess.r = new WebResponse( WebResponse.HTTP_OK, WebResponse.MIME_HTML, stringToSendToWebBrowser );
			return true;
		}
		return false;
	}

	public String addSong(WebRequest toProcess) {
		System.out.println("\n# ADD SONG TO DATABASE #");
		// Creating Song object
		String title = toProcess.params.get("trackName");
		String artist = toProcess.params.get("artistName");
		String album = toProcess.params.get("album");
		String runtime = toProcess.params.get("runtime");
		String albumThumbnailURL = toProcess.params.get("albumThumbnailURL");
		String primaryEmotion = toProcess.params.get("primaryEmotion");
		String secondaryEmotion = toProcess.params.get("secondaryEmotion");
		String genre = toProcess.params.get("genre");
		String backgroundURL = toProcess.params.get("backgroundURL");
		String purchaseLink = toProcess.params.get("purchaseLink");
		String youtubeEmbeddedURL = toProcess.params.get("youtubeEmbeddedURL");
		
		// Validation
		if(title==null || artist==null || album==null || runtime==null || albumThumbnailURL==null || primaryEmotion==null || secondaryEmotion==null || genre==null || backgroundURL==null || youtubeEmbeddedURL==null) {
			System.out.println("\nERROR: Null field found");
			return getErrorHTML("Ensure all fields are filled out");
		}
		
		if(title.isEmpty() || artist.isEmpty() || album.isEmpty() || runtime.isEmpty() || albumThumbnailURL.isEmpty() || primaryEmotion.isEmpty() || secondaryEmotion.isEmpty() || genre.isEmpty() || backgroundURL.isEmpty() || youtubeEmbeddedURL.isEmpty()) {
			System.out.println("\nERROR: Empty field found");
			return getErrorHTML("Ensure all fields are filled out");
		}
		
		if(purchaseLink.isEmpty()) purchaseLink = "";
		
		System.out.printf("# NO NULL OR EMPTY FIELDS #\nTitle: %s\nArtist: %s\nAlbum: %s\nRuntime: %s\nAlbum Thumbnail URL: %s\nPrimary Emotion: %s\nSecondary Emotion: %s\nGenre: %s\nBackground URL: %s\nPurchase Link: %s\nYouTube Link: %s", title, artist, album, runtime, albumThumbnailURL, primaryEmotion, secondaryEmotion, genre, backgroundURL, purchaseLink, youtubeEmbeddedURL);
		
		try {
			Integer.parseInt(runtime);
		} catch (Exception e) {
			System.out.println("\nERROR: Runtime not integer");
			return getErrorHTML("Ensure runtime is correct format i.e. 320");
		}
		
		if(!(checkForImageURL(albumThumbnailURL) && checkForImageURL(backgroundURL))) {
			System.out.println("\nERROR: Invalid image link found");
			return getErrorHTML("Ensure image links are valid");
		}
		
		boolean purchaseLinkValid = true;
		if(!purchaseLink.isEmpty()) checkForValidURL(purchaseLink);
		
		if(!(checkForValidURL(youtubeEmbeddedURL) && purchaseLinkValid)) {
			System.out.println("\nERROR: Broken links");
			return getErrorHTML("Ensure links are working");
		}
		
		if(!(youtubeEmbeddedURL.startsWith("https://www.youtube.com/embed/") || youtubeEmbeddedURL.startsWith("www.youtube.com/embed/"))) {
			System.out.println("\nERROR: YouTube link invalid");
			return getErrorHTML("Ensure YouTube link is valid");
		}
		
		System.out.println("# PASSED VALIDATION #");
		
		ArrayList<String> artistArray = new ArrayList<String>(Arrays.asList(artist));
		ArrayList<String> genreArray = new ArrayList<String>(Arrays.asList(genre));
		ArrayList<String> primaryArray = new ArrayList<String>(Arrays.asList(primaryEmotion));
		ArrayList<String> secondaryArray = new ArrayList<String>(Arrays.asList(secondaryEmotion));
		ArrayList<ArrayList<String>> purchaseArray = new ArrayList<ArrayList<String>>();
		
		if(!purchaseLink.isEmpty()) purchaseArray.add(new ArrayList<String>(Arrays.asList("Purchase", purchaseLink)));
		
		// Create Song object
		MVMap<String, Song> songsMap = db.s.openMap("Songs");
		Song song = new Song(Long.toString(System.currentTimeMillis()), title, Integer.parseInt(runtime), album, albumThumbnailURL, artistArray, genreArray, primaryArray, secondaryArray, purchaseArray, backgroundURL, youtubeEmbeddedURL);
		songsMap.put(song.Id, song);
		System.out.println("Song " + song.Id + " " + song.title + " added to database");
		
		return "";
	}
	
	public boolean checkForValidURL(String urlString) {
		try {
			URL url = new URL(urlString);
			url.openStream();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean checkForImageURL(String URL) {
		try {
			BufferedImage image = ImageIO.read(new URL(URL));  
	        if(image != null){  
	            return true;
	        } else{
	            return false;
	        }
		} catch (Exception e){
			return false;
		}
	}
	
	public String getErrorHTML(String error) {
		String stringToSendToWebBrowser = new String();
		stringToSendToWebBrowser += "<html>\n";
		stringToSendToWebBrowser += "  \n";
		stringToSendToWebBrowser += "  <head>\n";
		stringToSendToWebBrowser += "    <meta charset=\"utf-8\">\n";
		stringToSendToWebBrowser += "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n";
		stringToSendToWebBrowser += "    <script type=\"text/javascript\" src=\"http://cdnjs.cloudflare.com/ajax/libs/jquery/2.0.3/jquery.min.js\"></script>\n";
		stringToSendToWebBrowser += "    <script type=\"text/javascript\" src=\"http://netdna.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js\"></script>\n";
		stringToSendToWebBrowser += "    <link href=\"http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.3.0/css/font-awesome.min.css\"\n";
		stringToSendToWebBrowser += "    rel=\"stylesheet\" type=\"text/css\">\n";
		stringToSendToWebBrowser += "    <link href=\"http://pingendo.github.io/pingendo-bootstrap/themes/default/bootstrap.css\"\n";
		stringToSendToWebBrowser += "    rel=\"stylesheet\" type=\"text/css\">\n";
		stringToSendToWebBrowser += "	 <title>e.mu | Upload Error</title>\n";
		stringToSendToWebBrowser += "    <link  rel=\"icon\" href=\"\\favicon.png\">\n";
		stringToSendToWebBrowser += "  </head>\n";
		stringToSendToWebBrowser += "  \n";
		stringToSendToWebBrowser += "  <body>\n";
		stringToSendToWebBrowser += "    <div class=\"cover\">\n";
		stringToSendToWebBrowser += "      <div class=\"navbar\">\n";
		stringToSendToWebBrowser += "        <div class=\"container\">\n";
		stringToSendToWebBrowser += "          <div class=\"navbar-header\">\n";
		stringToSendToWebBrowser += "            <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-ex-collapse\">\n";
		stringToSendToWebBrowser += "              <span class=\"sr-only\">Toggle navigation</span>\n";
		stringToSendToWebBrowser += "              <span class=\"icon-bar\"></span>\n";
		stringToSendToWebBrowser += "              <span class=\"icon-bar\"></span>\n";
		stringToSendToWebBrowser += "              <span class=\"icon-bar\"></span>\n";
		stringToSendToWebBrowser += "            </button>\n";
		stringToSendToWebBrowser += "            <a class=\"navbar-brand\" href=\"Index.html\"><span>Return</span></a>\n";
		stringToSendToWebBrowser += "          </div>\n";
		stringToSendToWebBrowser += "          <div class=\"collapse navbar-collapse\" id=\"navbar-ex-collapse\"></div>\n";
		stringToSendToWebBrowser += "        </div>\n";
		stringToSendToWebBrowser += "      </div>\n";
		stringToSendToWebBrowser += "      <div class=\"background-image-fixed cover-image\" style=\"background-image : url('http://pingendo.github.io/pingendo-bootstrap/assets/blurry/800x600/6.jpg')\"></div>\n";
		stringToSendToWebBrowser += "      <div class=\"container\">\n";
		stringToSendToWebBrowser += "        <div class=\"row\">\n";
		stringToSendToWebBrowser += "          <div class=\"col-md-12 text-center\">\n";
		stringToSendToWebBrowser += "            <h1 class=\"text-inverse\">" + error + "</h1>\n";
		stringToSendToWebBrowser += "            <p class=\"text-inverse\"></p>\n";
		stringToSendToWebBrowser += "            <br>\n";
		stringToSendToWebBrowser += "            <br>\n";
		stringToSendToWebBrowser += "            <a href=\"/Upload.html\" class=\"btn btn-lg btn-primary\">Try Again<br></a>\n";
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