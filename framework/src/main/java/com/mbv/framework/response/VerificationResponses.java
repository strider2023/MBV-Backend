package com.mbv.framework.response;

/**
 * Created by arindamnath on 01/03/16.
 */
public class VerificationResponses {

    public VerificationResponses() { }

    public String getSuccessResponse() {
        return "<html>\n" +
                "\t<head>\n" +
                "\t\t<link href='https://fonts.googleapis.com/css?family=Pacifico' rel='stylesheet' type='text/css'>\n" +
                "\t\t<link href='https://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>\n" +
                "\t\t<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/css/materialize.min.css\">\n" +
                "\t\t<script src=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/js/materialize.min.js\"></script>\n" +
                "\t</head>\n" +
                "\t<body style=\"background-color: #129ba8; height:100%\">\n" +
                "\t\t<h1 style=\"font-family: 'Pacifico', cursive; font-weight: 400; color: white; text-align: center; font-size: 600%;\">mPokket</h1>\n" +
                "\t\t</br>\n" +
                "\t\t<center><img src=\"https://s3-ap-southeast-1.amazonaws.com/mbv-pokket/public-images/email_verified.png\" style=\"width: auto;height: 30%;\"/></center>\n" +
                "\t\t<span>\n" +
                "\t\t\t<p style=\"font-family: 'Open Sans', sans-serif; font-weight: 300; color: white; text-align: center; font-size: 250%;\">Congratulations! Your email address has been verified.</p>\n" +
                "\t\t\t<p style=\"font-family: 'Open Sans', sans-serif; font-weight: 300; color: white; text-align: center; font-size: 150%;\">You can now continue back to our application.</p>\n" +
                "\t\t</span>\n" +
                "\t</body>\n" +
                "</html>";
    }

    public String getErrorResponse() {
        return "<html>\n" +
                "\t<head>\n" +
                "\t\t<link href='https://fonts.googleapis.com/css?family=Pacifico' rel='stylesheet' type='text/css'>\n" +
                "\t\t<link href='https://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>\n" +
                "\t\t<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/css/materialize.min.css\">\n" +
                "\t\t<script src=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/js/materialize.min.js\"></script>\n" +
                "\t</head>\n" +
                "\t<body style=\"background-color: #129ba8;\">\n" +
                "\t\t<h1 style=\"font-family: 'Pacifico', cursive; font-weight: 400; color: white; text-align: center; font-size: 600%;\">mPokket</h1>\n" +
                "\t\t</br>\n" +
                "\t\t<center><img src=\"https://s3-ap-southeast-1.amazonaws.com/mbv-pokket/public-images/alert.png\" style=\"width: auto; height: 30%;\"/></center>\n" +
                "\t\n" +
                "\t\t<span>\n" +
                "\t\t\t<p style=\"font-family: 'Open Sans', sans-serif; font-weight: 300; color: white; text-align: center; font-size: 250%;\">OOPS! We where unable to verify your email address.</p>\n" +
                "\t\t\t<p style=\"font-family: 'Open Sans', sans-serif; font-weight: 300; color: white; text-align: center; font-size: 150%;\">Please try again. If the problem persists do contact us!</p>\n" +
                "\t\t</span>\n" +
                "\t\t<center><a class=\"waves-effect waves-light btn-large yellow black-text\" href=\"mailto:support@maybrightventures.com\">Contact Us</a></center>\n" +
                "\t</body>\n" +
                "</html>";
    }
}
