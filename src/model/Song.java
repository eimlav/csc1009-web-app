package model;

import java.util.ArrayList;

public class Song extends SmartSerializable
{
	private static final long serialVersionUID = 1L;
	// unqiue identifier
	public String Id;
	public String title;
	// runtime in seconds
	public int runtime;
	public String album;
	public String albumThumbnailURL;
	public ArrayList<String> artists;
	public ArrayList<String> genres;
	public ArrayList<String> primaryEmotionalCategories;
	public ArrayList<String> secondaryEmotionalCategories;
	public ArrayList<ArrayList<String>> purchaseLinks;
	public String backgroundURL;
	// embedded YouTube link
        public String embeddedStreamingURL;   
    
        public Song() {
		super();
	}

	public Song(String id, String title, int runtime, String album, String albumThumbnailURL,
			ArrayList<String> artists, ArrayList<String> genres, ArrayList<String> primaryEmotionalCategories,
			ArrayList<String> secondaryEmotionalCategories, ArrayList<ArrayList<String>> purchaseLinks,
			String backgroundURL, String embeddedStreamingURL) {
		super();
		Id = id;
		this.title = title;
		this.runtime = runtime;
		this.album = album;
		this.albumThumbnailURL = albumThumbnailURL;
		this.artists = artists;
		this.genres = genres;
		this.primaryEmotionalCategories = primaryEmotionalCategories;
		this.secondaryEmotionalCategories = secondaryEmotionalCategories;
		this.purchaseLinks = purchaseLinks;
		this.backgroundURL = backgroundURL;
		this.embeddedStreamingURL = embeddedStreamingURL;
	}

    @Override
    public String toString() {
    	String artistsString = Utilities.convertArrayListToString(this.artists);
    	String genresString = Utilities.convertArrayListToString(this.genres);
    	String primaryEmotionString = Utilities.convertArrayListToString(this.primaryEmotionalCategories);
    	String secondaryEmotionString = Utilities.convertArrayListToString(this.secondaryEmotionalCategories);
    	
    	return String.format("%1$s %2$s %3$s %4$s %5$s %6$s", this.title, this.album, artistsString, genresString, primaryEmotionString, secondaryEmotionString);
    }
}