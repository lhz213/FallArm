package socketServer;

//import javax.activation.DataHandler;
//import javax.activation.DataSource;
//import javax.activation.FileDataSource;
import java.io.File;
import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.FileUtils;

public class EmailSender {
	private static Session session;
	private static String senderEmailAddress;
	private static String emailContentFilePath;

	private static String getEmailContent() {
		String content = null;
		File file = new File(emailContentFilePath);
		try {
			content = FileUtils.readFileToString(file);
			System.out.println(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	private static void setupEmailSender() {
		session = MailServerConf.getSession();
		senderEmailAddress = MailServerConf.getSenderEmailAddress();
		emailContentFilePath = MailServerConf.getEmailContentPath();
	}

	public static void sendEamil(String patidentName, String targetEmailAddress) {
		try {
			if (session == null || senderEmailAddress == null
					|| emailContentFilePath == null) {
				setupEmailSender();
			}
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(senderEmailAddress));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(targetEmailAddress));
			message.setSubject("Patient " + patidentName
					+ " MAYBE fall down, please help!");

			Multipart multipart = new MimeMultipart();
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			// fill message
			messageBodyPart.setText(getEmailContent());
			multipart.addBodyPart(messageBodyPart);

			// // Part two is attachment
			// String fileAttachment = "F:\\niubi.txt";
			// messageBodyPart = new MimeBodyPart();
			// DataSource source = new FileDataSource(fileAttachment);
			// messageBodyPart.setDataHandler(new DataHandler(source));
			// messageBodyPart.setFileName(fileAttachment);
			// multipart.addBodyPart(messageBodyPart);

			// Put parts in message
			message.setContent(multipart);
			Transport.send(message);
			System.out.println("Done");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String args[]) {
		String targetEmailAddress = "lhz213@hotmail.com";
		System.out.println("Email to: " + targetEmailAddress);
		session = MailServerConf.getSession();
		senderEmailAddress = MailServerConf.getSenderEmailAddress();
		emailContentFilePath = MailServerConf.getEmailContentPath();
		sendEamil("haizhou", targetEmailAddress);
	}
}
