package socketServer;

import java.io.InputStream;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import webServer.db.DataSourceConf;

public class MailServerConf {
	private static String smtpPropFile = "smtp_config.properties";
	private static Properties smtpProps = null;
	private static Session session;

	private static Properties loadSMTPProperties() {
		Properties smtpProp = new Properties();
		InputStream smtpPropStrm;
		try {
			// load a properties file from class path, inside static method
			smtpPropStrm = DataSourceConf.class.getClassLoader()
					.getResourceAsStream("smtp_config.properties");
			smtpProp.load(smtpPropStrm);
		} catch (Exception ex) {
			System.out.println("Unable to load JDBC properties file:  "
					+ smtpPropFile + " Exception: " + ex);
		}
		return smtpProp;
	}

	private static String getProp(String key) {
		return smtpProps.getProperty(key);
	}

	private static void setupSession() {
		if (smtpProps == null)
			smtpProps = loadSMTPProperties();
		System.out.println(getProp("mail.sender.add"));
		System.out.println(getProp("mail.sender.pas"));
		session = Session.getDefaultInstance(smtpProps, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(getProp("mail.sender.add"),
						getProp("mail.sender.pas"));
			}
		});
	}

	public static Session getSession() {
		if (session == null)
			setupSession();
		return session;
	}

	public static String getSenderEmailAddress() {
		return getProp("mail.sender.add");
	}

	public static String getEmailContentPath() {

		return System.getProperty("user.dir") + getProp("mail.content.path");
	}
}
