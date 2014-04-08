package server;

import java.util.HashSet;
import org.kie.api.runtime.KieSession;

import com.sample.DroolsTest;

import engine.RuleEngine;

public class Main {
	public static final void main(String[] args) {
		new ESServer(5000, new ExpertSystem() {
			@Override
			public String getAnswer(String question) {
				String answer = DroolsTest.getAnswer(question);
				System.out.println(answer + "1");
				return answer;
			}
		});
	}
}
