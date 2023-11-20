package utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailTask extends AsyncTask<Void, Void, Boolean> {
    private Context context;
    private String email;
    private String subject;
    private String message;

    public SendMailTask(Context context, String email, String subject, String message) {
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return sendEmail();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        if (result) {
            // Email sent successfully
            Toast.makeText(context,"Email Sent!", Toast.LENGTH_SHORT).show();
        } else {
            // Email sending failed
            Toast.makeText(context,"Error Occurred On Sending Email ", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean sendEmail() {
        // Set your email credentials and server information
        final String username = "khaihkttran@gmail.com";
        final String password = "cjao qztq clcn fzor";
        String host = "smtp.gmail.com";
        int port = 587;

        // Set properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        // Create a Session object
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a MimeMessage object
            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(username));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);

            // Send the email
            Transport.send(mimeMessage);

            return true; // Email sent successfully

        } catch (MessagingException e) {
            e.printStackTrace();
            return false; // Email sending failed
        }
    }
     
}