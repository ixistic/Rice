package com.sample;

import java.util.HashSet;
import java.util.Iterator;

import org.kie.api.runtime.KieSession;

import server.ExpertSystem;

import engine.RuleEngine;

public class Drools implements ExpertSystem {

	private KieSession kieSession;
	private Message message;
	private String filename;

	public Drools() {
		filename = "xrules.drl";
		RuleEngine engine = new RuleEngine(filename);
		kieSession = engine.getKieSession();
	}

	public void reInitiate() {
		String username = "root";
		String password = "";
		String databaseName = "kekm";
		String host = "localhost";
		String port = "3306";
		
		String connectionString = "jdbc:mysql://" + host + ":" + port + "/"
				+ databaseName + "?useUnicode=true&characterEncoding=UTF-8";
		DatabaseConnector db = new DatabaseConnector(connectionString,
				username, password);
		RuleDownloader rd = new RuleDownloader(db, filename);
		rd.download();
		
		RuleEngine engine = new RuleEngine(filename);
		kieSession = engine.getKieSession();
		System.out.println("RELOAD");
	}

	public String getAnswer(String question) {
		StringBuilder answer = new StringBuilder();
		String[] arr = question.split(" ");
		String type = arr[0];
		HashSet<Message> resultSet = new HashSet<Message>();
		for (int i = 1; i < arr.length; i++) {
			HashSet<Message> tempSet = new HashSet<Message>();
//			System.out.println(i);
			message = new Message(arr[i], "input");
			tempSet.addAll(getResult(message, kieSession));

			if (i == 1) {
				resultSet = tempSet;
//				System.out.println("first");
			} else {
				resultSet.retainAll(tempSet);
//				System.out.println(">1");
			}
		}
		Iterator<Message> iter = resultSet.iterator();
		HashSet<String> set = new HashSet<String>();
		while (iter.hasNext()) {
			Message t = iter.next();
			if (t.getType().equals(type)) {
				set.add(t.getMessage());
			}
		}
		Iterator<String> tIter = set.iterator();
		if (tIter.hasNext())
			answer.append(tIter.next());
		while (tIter.hasNext()) {
			answer.append("," + tIter.next());
		}
		return answer.toString();
	}

	private HashSet<Message> getResult(Message mss, KieSession ks) {
		HashSet<Message> set = new HashSet<Message>();
		ks.insert(mss);
		ks.fireAllRules();
		if (mss.getSet().isEmpty()) {
			set.add(mss);
		} else {
			Iterator<Message> iter = mss.getSet().iterator();
			while (iter.hasNext()) {
				set.addAll(getResult(iter.next(), ks));
			}
		}
		return set;
	}

	public static class Message {

		private String message;

		private String type;

		private HashSet<Message> set = new HashSet<Message>();;

		public Message(String message, String type) {
			this.message = message;
			this.type = type;
		}

		public String getMessage() {
			return this.message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public void addResult(String name, String type) {
			set.add(new Message(name, type));
		}

		public HashSet<Message> getSet() {
			return set;
		}

		public String getType() {
			return this.type;
		}

	}

}
