package routing.handlers

import Main.Main.system

import java.util.Properties
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.{Message, PasswordAuthentication, Session, Transport}
import scala.concurrent.Future
import scala.concurrent.ExecutionContextExecutor
object MailSender {
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  def send(teachers: String) = Future{

    val username = "suraubaev04@mail.ru"
    val password = "65YMuLGmtGYmPy42NswD"

    val props = new Properties()
    props.put("mail.smtp.auth", "true")
    props.put("mail.smtp.starttls.enable", "true")
    props.put("mail.smtp.host", "smtp.mail.ru")
    props.put("mail.smtp.port", "587")

    val session = Session.getInstance(props,
      new javax.mail.Authenticator() {
        override def getPasswordAuthentication(): PasswordAuthentication = {
          new PasswordAuthentication(username, password)
        }
      })
    teachers.split(";").foreach{teach: String =>
      try {
        val message = new MimeMessage(session)
        message.setFrom(new InternetAddress(username))
        message.setRecipients(Message.RecipientType.TO, teach)
        message.setSubject("Testing Mail")
        message.setText("This is a test email sent from Scala.")

        Transport.send(message)

        println("Email sent successfully!")
      } catch {
        case e: Exception =>
          println("Failed to send email. Error message: " + e.getMessage)
      }
    }
    Future.unit

  }
}
