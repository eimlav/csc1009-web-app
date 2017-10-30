package views;

import org.h2.mvstore.MVMap;

import model.Account;
import storage.DatabaseInterface;
import storage.FileStoreInterface;
import web.WebRequest;
import web.WebResponse;

public class SignUpSuccess extends DynamicWebPage
{

	public SignUpSuccess(DatabaseInterface db,FileStoreInterface fs)
	{
		super(db,fs);
	}

	public boolean process(WebRequest toProcess)
	{
		if(toProcess.path.equalsIgnoreCase("SignUpSuccess.html") || toProcess.path.equalsIgnoreCase("SignUpSuccess"))
		{
			
			Account newAccount = new Account();
			newAccount.username = toProcess.params.get("username");
			newAccount.email = toProcess.params.get("email");
			newAccount.password = toProcess.params.get("password");
			
			MVMap<String, Account> accounts = db.s.openMap("Accounts");
			
			if(accounts.containsKey(newAccount.username))
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
			stringToSendToWebBrowser += "    <link href=\"http://pingendo.github.io/pingendo-bootstrap/themes/default/bootstrap.css\"\n";
			stringToSendToWebBrowser += "    rel=\"stylesheet\" type=\"text/css\">\n";
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
			stringToSendToWebBrowser += "            <h1 class=\"text-inverse\">Username already taken!</h1>\n";
			stringToSendToWebBrowser += "            <p class=\"text-inverse\"></p>\n";
			stringToSendToWebBrowser += "            <br>\n";
			stringToSendToWebBrowser += "            <br>\n";
			stringToSendToWebBrowser += "            <a href=\"/SignUp.html\" class=\"btn btn-lg btn-primary\">Try Again<br></a>\n";
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
			
			accounts.put(newAccount.username, newAccount);
			db.commit();
			
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
			stringToSendToWebBrowser += "      <div class=\"cover-image\" style=\"background-image : url('http://pingendo.github.io/pingendo-bootstrap/assets/blurry/800x600/15.jpg')\"></div>\n";
			stringToSendToWebBrowser += "      <div class=\"col-md-12 text-center\">\n";
			stringToSendToWebBrowser += "        <h1 class=\"text-inverse\">Account Created!</h1>\n";
			stringToSendToWebBrowser += "        <p class=\"text-inverse\">You have successfully created your account!\n";
			stringToSendToWebBrowser += "          <br>\n";
			stringToSendToWebBrowser += "        </p>\n";
			stringToSendToWebBrowser += "        <br>\n";
			stringToSendToWebBrowser += "        <br>\n";
			stringToSendToWebBrowser += "        <a class=\"btn btn-danger btn-lg\" href=\"Index.html\">Log in now!</a>\n";
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
