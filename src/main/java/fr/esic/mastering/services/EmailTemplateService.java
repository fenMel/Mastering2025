package fr.esic.mastering.services;

import org.springframework.stereotype.Service;

@Service
public class EmailTemplateService {


    public static String getProfileCompletionEmailTemplate(String baseUrl, String firstName, String lastName, String profileUrl) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>Complétez votre profil</title>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }" +
                ".container { max-width: 600px; margin: 0 auto; background-color: #ffffff; }" +
                ".header { background-color: #ffffff; padding: 20px; text-align: center; border-bottom: 1px solid #eaeaea; }" +
                "/* Removed logo class as we're using text */" +
                ".content { padding: 30px; color: #333333; }" +
                "h1 { color: #2c3e50; font-size: 24px; margin-bottom: 20px; }" +
                "p { font-size: 16px; line-height: 1.5; margin-bottom: 20px; }" +
                ".button-container { text-align: center; margin: 30px 0; }" +
                ".button { display: inline-block; background-color: #4682B4; color: white; text-decoration: none; padding: 12px 30px; " +
                "border-radius: 4px; font-weight: bold; font-size: 16px; }" +
                ".footer { padding: 20px; text-align: center; color: #999999; font-size: 14px; background-color: #f9f9f9; }" +
                ".expiration { font-size: 14px; color: #777777; font-style: italic; text-align: center; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1 style='color: #4682B4; font-size: 28px; font-weight: bold; margin: 0;'>MASTERING</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<h1>Bonjour " + firstName + " " + lastName + ",</h1>" +
                "<p>Vous avez été inscrit sur la plateforme MASTERING. Pour finaliser votre inscription, veuillez compléter votre profil en cliquant sur le bouton ci-dessous.</p>" +
                "<div class='button-container'>" +
                "<a href='" + profileUrl + "' class='button'>Compléter mon profil</a>" +
                "</div>" +
                "<p class='expiration'>Ce lien expire dans 24 heures.</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>© " + java.time.Year.now().getValue() + " MASTERING. Tous droits réservés.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }


    /**
     * Generates an email with PDF attached.
     *
     * @param firstName      The first name of the recipient.
     * @param lastName       The last name of the recipient.
     * @param convocationTitle The title of the convocation.
     * @return A string containing the HTML email template.
     */
    public static String getConvocationEmailTemplate(String firstName, String lastName, String convocationTitle) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>Convocation</title>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }" +
                ".container { max-width: 600px; margin: 0 auto; background-color: #ffffff; }" +
                ".header { background-color: #ffffff; padding: 20px; text-align: center; border-bottom: 1px solid #eaeaea; }" +
                ".content { padding: 30px; color: #333333; }" +
                "h1 { color: #2c3e50; font-size: 24px; margin-bottom: 20px; }" +
                ".brand { color: #4682B4; font-size: 28px; font-weight: bold; margin: 0; }" +
                "p { font-size: 16px; line-height: 1.5; margin-bottom: 20px; }" +
                ".attachment-note { background-color: #f9f9f9; padding: 15px; border-left: 4px solid #4682B4; margin: 20px 0; }" +
                ".footer { padding: 20px; text-align: center; color: #999999; font-size: 14px; background-color: #f9f9f9; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1 class='brand'>MASTERING</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<h1>Convocation: " + convocationTitle + "</h1>" +
                "<p>Bonjour " + lastName + " " + firstName + ",</p>" +
                "<p>Veuillez trouver ci-joint votre convocation pour <strong>" + convocationTitle + "</strong>.</p>" +
                "<div class='attachment-note'>" +
                "<p>Un document PDF a été joint à cet email. Veuillez l'ouvrir pour consulter les détails de votre convocation.</p>" +
                "</div>" +
                "<p>Cordialement,<br>Votre Organisation</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>© " + java.time.Year.now().getValue() + " MASTERING. Tous droits réservés.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}