package calPolyScheduler;

import com.amazon.speech.speechlet.servlet.SpeechletServlet;

public class SchedulerServlet extends SpeechletServlet {
	public SchedulerServlet() {
		this.setSpeechlet(new SchedulerSpeechlet());
	}
}
