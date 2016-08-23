package com.mbv.framework.response;

/**
 * Created by arindamnath on 01/03/16.
 */
public class EmailResponses {

    public EmailResponses() { }

    public String getSignUpMail(String name) {
        return "<html>\n" +
                "\t<head>\n" +
                "\t\t<link href='https://fonts.googleapis.com/css?family=Pacifico' rel='stylesheet' type='text/css'>\n" +
                "\t\t<link href='https://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>\n" +
                "\t\t<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/css/materialize.min.css\">\n" +
                "\t\t<script src=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/js/materialize.min.js\"></script>\n" +
                "\t</head>\n" +
                "\t<body style=\"background-color: #129ba8; height:100%; padding: 5%\">\n" +
                "\t\t<h1 style=\"font-family: 'Pacifico', cursive; font-weight: 400; color: white; text-align: center; font-size: 400%;\">Welcome to mPokket</h1>\n" +
                "\t\t</br>\n" +
                "\t\t<span><p style=\"font-family: 'Open Sans', sans-serif; font-weight: 300; color: white; font-size: 100%;\">Hi " + name + ",<p></span>\n" +
                "\t\t<span>\n" +
                "\t\t\t<p style=\"font-family: 'Open Sans', sans-serif; font-weight: 300; color: white; font-size: 100%;\">\n" +
                "\t\t\t\tGreetings from the mPokket team!</br>\n" +
                "\t\t\t\tExperience financial freedom! Use our app to access funds for your short term needs without having to ask for help from friends or family.\n" +
                "\t\t\t</p>\n" +
                "\t\t</span>\n" +
                "\t\t<span>\n" +
                "\t\t\t<p style=\"font-family: 'Open Sans', sans-serif; font-weight: 300; color: white; font-size: 100%;\">From,</p>\n" +
                "\t\t\t<p style=\"font-family: 'Pacifico', cursive; font-weight: 400; color: white; font-size: 150%;\">Team mPokket</p>\n" +
                "\t\t</span>\n" +
                "\t</body>\n" +
                "</html>";
    }

    public String getVerificationMail(String name, String link) {
        return "<html>\n" +
                "\t<head>\n" +
                "\t\t<link href='https://fonts.googleapis.com/css?family=Pacifico' rel='stylesheet' type='text/css'>\n" +
                "\t\t<link href='https://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>\n" +
                "\t\t<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/css/materialize.min.css\">\n" +
                "\t\t<script src=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/js/materialize.min.js\"></script>\n" +
                "\t</head>\n" +
                "\t<body style=\"background-color: #129ba8; height:100%; padding: 5%\">\n" +
                "\t\t</br>\n" +
                "\t\t<span><p style=\"font-family: 'Open Sans', sans-serif; font-weight: 300; color: white; font-size: 100%;\">Hi " + name + ",<p></span>\n" +
                "\t\t<span>\n" +
                "\t\t\t<p style=\"font-family: 'Open Sans', sans-serif; font-weight: 300; color: white; font-size: 100%;\">\n" +
                "\t\t\t\tClick on the link below to successfully verify your email address with us. This is important if you wish to do any transactions using our platform.\n" +
                "\t\t\t</p>\n" +
                "\t\t</span>\n" +
                "\t\t<span>\n" +
                "\t\t\t<center><a class=\"waves-effect waves-light btn-large yellow black-text\" \n" +
                "\t\t\t\thref=\"" + link + "\">Verify Email</a></center>\n" +
                "\t\t</span>\n" +
                "\t\t<span>\n" +
                "\t\t\t<p style=\"font-family: 'Open Sans', sans-serif; font-weight: 300; color: white; font-size: 100%;\">From,</p>\n" +
                "\t\t\t<p style=\"font-family: 'Pacifico', cursive; font-weight: 400; color: white; font-size: 150%;\">Team mPokket</p>\n" +
                "\t\t</span>\n" +
                "\t</body>\n" +
                "</html>";
    }

    public String getUserEmailRecoveryBody(String name, String pass) {
        return "<html>\n" +
                "\t<head>\n" +
                "\t\t<link href='https://fonts.googleapis.com/css?family=Pacifico' rel='stylesheet' type='text/css'>\n" +
                "\t\t<link href='https://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>\n" +
                "\t\t<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/css/materialize.min.css\">\n" +
                "\t\t<script src=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/js/materialize.min.js\"></script>\n" +
                "\t</head>\n" +
                "\t<body style=\"background-color: #129ba8; height:100%; padding: 5%\">\n" +
                "\t\t</br>\n" +
                "\t\t<span><p style=\"font-family: 'Open Sans', sans-serif; font-weight: 300; color: white; font-size: 100%;\">Hi " + name + ",<p></span>\n" +
                "\t\t<span>\n" +
                "\t\t\t<p style=\"font-family: 'Open Sans', sans-serif; font-weight: 300; color: white; font-size: 100%;\">\n" +
                "\t\t\t\tYour password for mPokket is, <b>" + pass + "</b>.\n" +
                "\t\t\t</p>\n" +
                "\t\t</span>\n" +
                "\t\t<span>\n" +
                "\t\t\t<p style=\"font-family: 'Open Sans', sans-serif; font-weight: 300; color: white; font-size: 100%;\">From,</p>\n" +
                "\t\t\t<p style=\"font-family: 'Pacifico', cursive; font-weight: 400; color: white; font-size: 150%;\">Team mPokket</p>\n" +
                "\t\t</span>\n" +
                "\t</body>\n" +
                "</html>";
    }

    public String getLoanApplicationEmailBody(String name, Long amount, Long period, String loanId) {
        return "<html>\n" +
                "\t<head>\n" +
                "\t\t<link href='https://fonts.googleapis.com/css?family=Pacifico' rel='stylesheet' type='text/css'>\n" +
                "\t\t<link href='https://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>\n" +
                "\t\t<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/css/materialize.min.css\">\n" +
                "\t\t<script src=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/js/materialize.min.js\"></script>\n" +
                "\t</head>\n" +
                "\t<body style=\"background-color: #129ba8; height:100%; padding: 5%\">\n" +
                "\t\t</br>\n" +
                "\t\t<span><p style=\"font-family: 'Open Sans', sans-serif; font-weight: 300; color: white; font-size: 100%;\">Hi " + name + ",<p></span>\n" +
                "\t\t<span>\n" +
                "\t\t\t<p style=\"font-family: 'Open Sans', sans-serif; font-weight: 300; color: white; font-size: 100%;\">\n" +
                "\t\t\t\tYou have requested an amount of ₹" + amount +" for a period of " + period + " months. Please wait while we process your request. " +
                "Your reference id for this borrow request is <b>" + loanId + "</b>.\n" +
                "\t\t\t</p>\n" +
                "\t\t</span>\n" +
                "\t\t<span>\n" +
                "\t\t\t<p style=\"font-family: 'Open Sans', sans-serif; font-weight: 300; color: white; font-size: 100%;\">From,</p>\n" +
                "\t\t\t<p style=\"font-family: 'Pacifico', cursive; font-weight: 400; color: white; font-size: 150%;\">Team mPokket</p>\n" +
                "\t\t</span>\n" +
                "\t</body>\n" +
                "</html>";
    }

    public String getLoanApprovalEmailBody(String borrowerName, String approverName, Long amount, Long period, String loanId) {
        return "<html>\n" +
                "\t<head>\n" +
                "\t\t<link href='https://fonts.googleapis.com/css?family=Pacifico' rel='stylesheet' type='text/css'>\n" +
                "\t\t<link href='https://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>\n" +
                "\t\t<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/css/materialize.min.css\">\n" +
                "\t\t<script src=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/js/materialize.min.js\"></script>\n" +
                "\t</head>\n" +
                "\t<body style=\"background-color: #129ba8; height:100%; padding: 5%\">\n" +
                "\t\t</br>\n" +
                "\t\t<span><p style=\"font-family: 'Open Sans', sans-serif; font-weight: 300; color: white; font-size: 100%;\">Hi " + borrowerName + ",<p></span>\n" +
                "\t\t<span>\n" +
                "\t\t\t<p style=\"font-family: 'Open Sans', sans-serif; font-weight: 300; color: white; font-size: 100%;\">\n" +
                "\t\t\t\tYour borrow request of ₹" + amount + " for a period of " + period + " months has been approved by " + approverName + "." +
                " Your reference id for this borrow request is <b>" + loanId + "</b>.\n" +
                "\t\t\t</p>\n" +
                "\t\t</span>\n" +
                "\t\t<span>\n" +
                "\t\t\t<p style=\"font-family: 'Open Sans', sans-serif; font-weight: 300; color: white; font-size: 100%;\">From,</p>\n" +
                "\t\t\t<p style=\"font-family: 'Pacifico', cursive; font-weight: 400; color: white; font-size: 150%;\">Team mPokket</p>\n" +
                "\t\t</span>\n" +
                "\t</body>\n" +
                "</html>";
    }

    public String getLoanGivenEmailBody(String borrowerName, String approverName, Long amount, Long period, String loanId) {
        return "<html>\n" +
                "\t<head>\n" +
                "\t\t<link href='https://fonts.googleapis.com/css?family=Pacifico' rel='stylesheet' type='text/css'>\n" +
                "\t\t<link href='https://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css'>\n" +
                "\t\t<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/css/materialize.min.css\">\n" +
                "\t\t<script src=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/js/materialize.min.js\"></script>\n" +
                "\t</head>\n" +
                "\t<body style=\"background-color: #129ba8; height:100%; padding: 5%\">\n" +
                "\t\t</br>\n" +
                "\t\t<span><p style=\"font-family: 'Open Sans', sans-serif; font-weight: 300; color: white; font-size: 100%;\">Hi " + approverName + ",<p></span>\n" +
                "\t\t<span>\n" +
                "\t\t\t<p style=\"font-family: 'Open Sans', sans-serif; font-weight: 300; color: white; font-size: 100%;\">\n" +
                "\t\t\t\tYou have lent out the amount of ₹" + amount + " for a period of " + period + " months to " + borrowerName +". " +
                "Your reference id for this lending request is <b>" + loanId + "</b>.\n" +
                "\t\t\t</p>\n" +
                "\t\t</span>\n" +
                "\t\t<span>\n" +
                "\t\t\t<p style=\"font-family: 'Open Sans', sans-serif; font-weight: 300; color: white; font-size: 100%;\">From,</p>\n" +
                "\t\t\t<p style=\"font-family: 'Pacifico', cursive; font-weight: 400; color: white; font-size: 150%;\">Team mPokket</p>\n" +
                "\t\t</span>\n" +
                "\t</body>\n" +
                "</html>";
    }
}
