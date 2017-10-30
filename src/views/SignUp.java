package views;

import storage.DatabaseInterface;
import storage.FileStoreInterface;
import web.WebRequest;
import web.WebResponse;

public class SignUp extends DynamicWebPage
{

	public SignUp(DatabaseInterface db,FileStoreInterface fs)
	{
		super(db,fs);
	}

	public boolean process(WebRequest toProcess)
	{
		if(toProcess.path.equalsIgnoreCase("SignUp"))
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
			stringToSendToWebBrowser += "            <a class=\"navbar-brand\" href=\"Index\" e=\"\">Return</a>\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "          <div class=\"collapse navbar-collapse\" id=\"navbar-ex-collapse\">\n";
			stringToSendToWebBrowser += "            <ul class=\"nav navbar-nav navbar-right\"></ul>\n";
			stringToSendToWebBrowser += "          </div>\n";
			stringToSendToWebBrowser += "        </div>\n";
			stringToSendToWebBrowser += "      </div>\n";
			stringToSendToWebBrowser += "      <div class=\"background-image-fixed cover-image\" style=\"background-image : url('http://pingendo.github.io/pingendo-bootstrap/assets/blurry/800x600/7.jpg')\"></div>\n";
			stringToSendToWebBrowser += "      <div class=\"container\">\n";
			stringToSendToWebBrowser += "        <div class=\"row\">\n";
			stringToSendToWebBrowser += "          <div class=\"col-md-12 text-center\">\n";
			stringToSendToWebBrowser += "            <h1 class=\"text-inverse\">Sign Up</h1>\n";
			stringToSendToWebBrowser += "            <p class=\"text-inverse\">Sign up now completely free.</p>\n";
			stringToSendToWebBrowser += "            <form role=\"form\" class=\"text-left\" method=\"GET\"\n";
			stringToSendToWebBrowser += "            action=\"/SignUpSuccess\">\n";
			stringToSendToWebBrowser += "              <div class=\"form-group\">\n";
			stringToSendToWebBrowser += "                <label class=\"control-label\" for=\"exampleInputPassword1\">Username</label>\n";
			stringToSendToWebBrowser += "                <input class=\"form-control\" id=\"exampleInputPassword1\"\n";
			stringToSendToWebBrowser += "                placeholder=\"Username\" type=\"text\" name=\"username\">\n";
			stringToSendToWebBrowser += "              </div>\n";
			stringToSendToWebBrowser += "              <div class=\"form-group\">\n";
			stringToSendToWebBrowser += "                <label class=\"control-label\">Email Address</label>\n";
			stringToSendToWebBrowser += "                <input class=\"form-control\" type=\"email\" placeholder=\"Email Address\"\n";
			stringToSendToWebBrowser += "                name=\"email\">\n";
			stringToSendToWebBrowser += "              </div>\n";
			stringToSendToWebBrowser += "              <div class=\"form-group\">\n";
			stringToSendToWebBrowser += "                <label class=\"control-label\">Password</label>\n";
			stringToSendToWebBrowser += "                <input class=\"form-control\" type=\"password\" placeholder=\"Password\"\n";
			stringToSendToWebBrowser += "                name=\"password\">\n";
			stringToSendToWebBrowser += "              </div>\n";
			stringToSendToWebBrowser += "              <div class=\"form-group\">\n";
			stringToSendToWebBrowser += "                <label class=\"control-label\">Confirm Password</label>\n";
			stringToSendToWebBrowser += "                <input class=\"form-control\" type=\"password\" placeholder=\"Confirm Password\"\n";
			stringToSendToWebBrowser += "                name=\"confirmPassword\">\n";
			stringToSendToWebBrowser += "              </div>\n";
			stringToSendToWebBrowser += "              <div class=\"text-center\">\n";
			stringToSendToWebBrowser += "                <div class=\"col-md-12\">\n";
			stringToSendToWebBrowser += "                  <button class=\"btn btn-lg btn-success new\" type=\"submit\">Create Account</button>\n";
			stringToSendToWebBrowser += "                </div>\n";
			stringToSendToWebBrowser += "              </div>\n";
			stringToSendToWebBrowser += "            </form>\n";
			stringToSendToWebBrowser += "            <br>\n";
			stringToSendToWebBrowser += "            <br>\n";
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