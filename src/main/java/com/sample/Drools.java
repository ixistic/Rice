package com.sample;

import java.util.HashSet;
import java.util.Iterator;

import org.kie.api.runtime.KieSession;

import engine.RuleEngine;

public class Drools {
	
private KieSession kieSession;
private Message message;
	
	public Drools() {
		RuleEngine engineProvince = new RuleEngine();
		engineProvince.addRuleFile("rules", "rules.drl");
		kieSession = engineProvince.buildKnowledgeSession();
	}
    
    public String getAnswer(String question){
    	StringBuilder answer = new StringBuilder();
    	String[] arr = question.split(" ");
    	
    	HashSet<String> resultSet = new HashSet<String>();
    	for(int i = 0; i < arr.length ; i++){
    		HashSet<String> tempSet = new HashSet<String>();
    		
    		message = new Message(arr[i]);
	        tempSet.addAll(getResult(message,kieSession));
	        
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
    
    private HashSet<String> getResult(Message mss,KieSession ks) {
    	HashSet<String> set = new HashSet<String>();
    	ks.insert(mss);
    	ks.fireAllRules();
    	if(mss.getSet().isEmpty()) {
    		System.out.println(mss.getMessage());
    		set.add(mss.getMessage());
    	}
    	else {
    		Iterator<String> iter = mss.getSet().iterator();
    		while(iter.hasNext()) {
    			set.addAll(getResult(new Message(iter.next()),ks));
    		}
    	}
		return set;
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
