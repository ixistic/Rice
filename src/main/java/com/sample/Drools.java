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
    	String type = arr[0];
    	HashSet<Message> resultSet = new HashSet<Message>();
    	for(int i = 1; i < arr.length ; i++){
    		HashSet<Message> tempSet = new HashSet<Message>();
    		
    		message = new Message(arr[i],"input");
	        tempSet.addAll(getResult(message,kieSession));
	        
	        if (i == 1) {
	        	resultSet = tempSet;
	        } else {
	        	resultSet.retainAll(tempSet);
	        }
    	}
    	Iterator<Message> iter = resultSet.iterator();
    	while (iter.hasNext()) {
    		Message t = iter.next();
    		if(answer.length() < 1) {
    			if(t.getType().equals(type))
    				answer.append(iter.next());
    		}
    		else {
    			if(t.getType().equals(type))
    				answer.append("," + iter.next());
    		}
    	}
        
        return answer.toString();
    }
    
    private HashSet<Message> getResult(Message mss,KieSession ks) {
    	HashSet<Message> set = new HashSet<Message>();
    	ks.insert(mss);
    	ks.fireAllRules();
    	if(mss.getSet().isEmpty()) {
    		set.add(mss);
    	}
    	else {
    		Iterator<Message> iter = mss.getSet().iterator();
    		while(iter.hasNext()) {
    			set.addAll(getResult(iter.next(),ks));
    		}
    	}
		return set;
    }

    public static class Message {

        private String message;
        
        private String type;
        
        private HashSet<Message> set = new HashSet<Message>();;

        public Message(String message,String type) {
        	this.message = message;
        	this.type = type;
        }
        
        public String getMessage() {
            return this.message;
        }
        
        public void setMessage(String message) {
        	this.message = message;
        }

        public void addResult(String name,String type) {
        	set.add(new Message(name,type));
        }
        
        public HashSet<Message> getSet() {
        	return set;
        }
        
        public String getType() {
        	return this.type;
        }

    }

}
