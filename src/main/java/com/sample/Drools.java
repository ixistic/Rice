package com.sample;

import java.util.HashSet;
import java.util.Iterator;

import org.kie.api.runtime.KieSession;

import engine.RuleEngine;

public class Drools {
	
private KieSession ksProvince;
private KieSession ksDisease;
private KieSession ksEnvironment;
private KieSession ksETC;
private Message message;
	
	public Drools() {
		RuleEngine engineProvince = new RuleEngine();
		engineProvince.addRuleFile("rules", "province.drl");
		this.ksProvince = engineProvince.buildKnowledgeSession();
		
		RuleEngine engineDisease = new RuleEngine();
		engineDisease.addRuleFile("rules", "disease.drl");
		this.ksDisease = engineDisease.buildKnowledgeSession();
		
		RuleEngine engineEnvironment = new RuleEngine();
		engineEnvironment.addRuleFile("rules", "envi.drl");
		this.ksEnvironment = engineEnvironment.buildKnowledgeSession();
		
		RuleEngine engineETC = new RuleEngine();
		engineETC.addRuleFile("rules", "etc.drl");
		this.ksETC = engineETC.buildKnowledgeSession();
	}
    
    public String getAnswer(String question){
    	StringBuilder answer = new StringBuilder();
    	String[] arr = question.split(" ");
    	
    	HashSet<String> resultSet = new HashSet<String>();
    	for(int i = 0; i < arr.length ; i++){
    		HashSet<String> tempSet = new HashSet<String>();
    		
    		message = new Message(arr[i]);
    		ksProvince.insert(message);
	        ksProvince.fireAllRules();
	        //tempSet.addAll(message.getSet());
	        
	        Iterator<String> iter = message.getSet().iterator();
	        HashSet<String> set = new HashSet<String>();
	        while(iter.hasNext()) {
	        	message = new Message(iter.next());
	        	ksProvince.insert(message);
	        	ksProvince.fireAllRules();
	        	if(message.getSet().isEmpty()) 
	        		tempSet.add(message.getMessage());
	        	else set.addAll(message.getSet());
	        }
	        iter = set.iterator();
	        HashSet<String> set2 = (HashSet<String>) set.clone();
	        while(iter.hasNext()) {
	        	message = new Message(iter.next());
	        	ksProvince.insert(message);
	        	ksProvince.fireAllRules();
	        	if(message.getSet().isEmpty()) 
	        		tempSet.add(message.getMessage());
	        	else set2.addAll(message.getSet());
	        }
	        
	        
	        message = new Message(arr[i]);
	        ksDisease.insert(message);
	        ksDisease.fireAllRules();
	        tempSet.addAll(message.getSet());
	        
    		message = new Message(arr[i]);
	        ksEnvironment.insert(message);
	        ksEnvironment.fireAllRules();
	        tempSet.addAll(message.getSet());
	        
	        message = new Message(arr[i]);
	        ksETC.insert(message);
	        ksETC.fireAllRules();
	        tempSet.addAll(message.getSet());
	        
	        if (i == 0) {
	        	resultSet = tempSet;
	        } else {
	        	resultSet.retainAll(tempSet);
	        }
    	}
    	Iterator<String> iter = resultSet.iterator();
    	if (iter.hasNext()) {
    		answer.append(iter.next());
    	}
    	while (iter.hasNext()) {
    		answer.append("," + iter.next());
    	}
        
        return answer.toString();
    }
    
    private HashSet<String> getResult(Message mss) {
    	HashSet<String> set = new HashSet<String>();
    	ksProvince.insert(mss);
    	ksProvince.fireAllRules();
		return null;
    }

    public static class Message {

        private String message;
        
        private HashSet<String> set = new HashSet<String>();;

        public Message(String message) {
        	this.message = message;
        }
        
        public String getMessage() {
            return this.message;
        }
        
        public void setMessage(String message) {
        	this.message = message;
        }

        public void addResult(String s) {
        	set.add(s);
        }
        
        public HashSet<String> getSet() {
        	return set;
        }

    }

}
