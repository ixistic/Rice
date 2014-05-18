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
	private HashSet<String> ruleCheck = new HashSet<String>();
	private ConfigLoader configLoader;

	public Drools() {
		filename = "xrules.drl";
		RuleEngine engine = new RuleEngine(filename);
		kieSession = engine.getKieSession();
		configLoader = new ConfigLoader();
		configLoader.loadConfig();
	}

	public void reInitiate() {
		String username = configLoader.getConfig("dbuser");
		String password = configLoader.getConfig("dbpassword");
		String databaseName = configLoader.getConfig("dbname");
		String host = configLoader.getConfig("dbhost");
		String port = configLoader.getConfig("dbport");

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
		HashSet<String> forTest = new HashSet();
		for (int i = 1; i < arr.length; i++) {
//			System.out.println(i);
			message = new Message(arr[i], "input");

			HashSet<Message> temp = getResult(message, kieSession);

			resultSet.addAll(temp);
			if(i==1) {
				Iterator<Message> iter = temp.iterator();
				while(iter.hasNext()) {
					Message tt = iter.next();
					if(tt.getType().equals(type))
						forTest.add(tt.getMessage());
				}
			}
			HashSet<String> test2 = new HashSet();
			Iterator<Message> iter2 = temp.iterator();
			while(iter2.hasNext()) {
				test2.add(iter2.next().getMessage());
			}
			forTest.retainAll(test2);
		}
		Iterator<Message> iter = resultSet.iterator();
		HashSet<String> set = new HashSet<String>();
		while (iter.hasNext()) {
			Message t = iter.next();
			if (forTest.contains(t.getMessage())) {
				if(t.getType().equals(type))
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
		ruleCheck.add(mss.getMessage());
		ks.insert(mss);
		ks.fireAllRules();
		if (mss.getSet().isEmpty()) {
			set.add(mss);
		} else {
			Iterator<Message> iter = mss.getSet().iterator();
			while (iter.hasNext()) {
				Message t = iter.next();
				if(!ruleCheck.contains(t.getMessage())) {
					ruleCheck.add(t.getMessage());
					set.addAll(getResult(t, ks));		
				}
				set.add(t);
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