package views;

import storage.DatabaseInterface;
import storage.FileStoreInterface;
import web.WebRequest;
import web.WebResponse;

public class EnterPage extends DynamicWebPage
{

	public EnterPage(DatabaseInterface db,FileStoreInterface fs)
	{
		super(db,fs);
	}

	public boolean process(WebRequest toProcess)
	{
		if(toProcess.path.equalsIgnoreCase("EnterPage"))
		{
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
			stringToSendToWebBrowser += "    <link href=\"enterPage.css\" rel=\"stylesheet\" type=\"text/css\">\n";
			stringToSendToWebBrowser += "    <title>e.mu | Enter Page</title>\n";
			stringToSendToWebBrowser += "    <link  rel=\"icon\" href=\"\\favicon.png\">\n";
			stringToSendToWebBrowser += "  </head>\n";
			stringToSendToWebBrowser += "  \n";
			stringToSendToWebBrowser += "  <body>\n";
			stringToSendToWebBrowser += "    <div class=\"cover\">\n";
			stringToSendToWebBrowser += "      <div class=\"navbar\">\n";
			stringToSendToWebBrowser += "        <div class=\"container\">\n";
			stringToSendToWebBrowser += "          <div class=\"navbar-header\">\n";
			stringToSendToWebBrowser += "            <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\".navbar-collapse\">\n";
			stringToSendToWebBrowser += "              <span class=\"sr-only\">Toggle navigation</span>\n";
			stringToSendToWebBrowser += "              <span class=\"icon-bar\"></span>\n";
			stringToSendToWebBrowser += "              <span class=\"icon-bar\"></span>\n";
			stringToSendToWebBrowser += "              <span class=\"icon-bar\"></span>\n";
			stringToSendToWebBrowser += "            </button>\n";
			stringToSendToWebBrowser += "            <a class=\"navbar-brand\"></a>\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "          <div class=\"collapse navbar-collapse navbar-ex1-collapse\">\n";
			stringToSendToWebBrowser += "            <ul class=\"lead navbar-nav navbar-right nav\">\n";
			stringToSendToWebBrowser += "              <li class=\"small-title\">\n";
			stringToSendToWebBrowser += "                <a href=\"index.html\"></a>\n";
			stringToSendToWebBrowser += "              </li>\n";
			stringToSendToWebBrowser += "            </ul>\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "        </div>\n";
			stringToSendToWebBrowser += "      </div>\n";
			stringToSendToWebBrowser += "      <div class=\"background-image-fixed cover-image\" style=\"background-image : url('Listeningedit.jpg')\"></div>\n";
			stringToSendToWebBrowser += "      <div class=\"container\">\n";
			stringToSendToWebBrowser += "        <div class=\"row\">\n";
			stringToSendToWebBrowser += "          <div class=\"col-md-12 text-center title-box\">\n";
			stringToSendToWebBrowser += "            <h1 class=\"text-inverse large-text\">Listen.</h1>\n";
			stringToSendToWebBrowser += "            <p class=\"text-muted small-title\">Sit back and listen to all your favourite artists, all on demand.</p>\n";
			stringToSendToWebBrowser += "            <br>\n";
			stringToSendToWebBrowser += "            <br>\n";
			stringToSendToWebBrowser += "            <a class=\"btn btn-lg btn-primary new\" href=\"search\">Enter</a>\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "        </div>\n";
			stringToSendToWebBrowser += "      </div>\n";
			stringToSendToWebBrowser += "    </div>\n";
			stringToSendToWebBrowser += "    <div class=\"cover\">\n";
			stringToSendToWebBrowser += "      <div class=\"background-image-fixed cover-image\" style=\"background-image : url('Making2edit.jpg')\"></div>\n";
			stringToSendToWebBrowser += "      <div class=\"container\">\n";
			stringToSendToWebBrowser += "        <div class=\"row\">\n";
			stringToSendToWebBrowser += "          <div class=\"col-md-12 text-center\">\n";
			stringToSendToWebBrowser += "            <h1 class=\"text-inverse\">Be Heard.</h1>\n";
			stringToSendToWebBrowser += "            <p class=\"text-muted small-title\">Share your very own music across the globe and listen to thousands more\n";
			stringToSendToWebBrowser += "              independent artists.</p>\n";
			stringToSendToWebBrowser += "            <br>\n";
			stringToSendToWebBrowser += "            <br>\n";
			stringToSendToWebBrowser += "            <a class=\"btn btn-lg btn-primary new\" href=\"Independent.html\">Enter</a>\n";
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

}