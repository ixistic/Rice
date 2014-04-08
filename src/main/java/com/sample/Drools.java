package com.sample;

import java.util.HashSet;

import org.kie.api.runtime.KieSession;

import engine.RuleEngine;

public class Drools {
	
private KieSession ksProvince;
private KieSession ksDisease;
private KieSession ksEnvironment;
private KieSession ksETC;
	
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
    	String answer = "";
    	String[] arr = question.split(" ");
    	
    	HashSet<String> set = new HashSet<String>();
    	for(int i = 0; i < arr.length ; i++){
    		Message mss = new Message();
	        mss.setMessage(arr[i]);
	        this.ksProvince.insert(mss);
	        this.ksProvince.fireAllRules();
	        
	        if(set.isEmpty()) {
	        	if(!mss.getSet().isEmpty())
	        		set = mss.getSet();
	        }
	        else {
	        	if(!mss.getSet().isEmpty())
	        		set.retainAll(mss.getSet());
	        }
	        
    		mss = new Message();
	        mss.setMessage(arr[i]);
	        ksDisease.insert(mss);
	        ksDisease.fireAllRules();
	        
	        if(!mss.getSet().isEmpty())
	        	set.retainAll(mss.getSet());
	        
	      
    		mss = new Message();
	        mss.setMessage(arr[i]);
	        ksEnvironment.insert(mss);
	        ksEnvironment.fireAllRules();
	        if(!mss.getSet().isEmpty())
	        	set.retainAll(mss.getSet());
	        
    		mss = new Message();
	        mss.setMessage(arr[i]);
	        ksETC.insert(mss);
	        ksETC.fireAllRules();
	        if(!mss.getSet().isEmpty())
	        	set.retainAll(mss.getSet());
    	}
        for(String s : set) {
        	answer += s + " ";
        }
        System.out.println(answer);
        return answer;
    }

    public static class Message {

        private String message;
        
        private HashSet<String> set = new HashSet<String>();;

        private int status;

        public String getMessage() {
            return this.message;
        }

        public void addResult(String s) {
        	set.add(s);
        }
        
        public HashSet<String> getSet() {
        	return set;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }

        public int getStatus() {
            return this.status;
        }
        
        public void setStatus(int status) {
            this.status = status;
        }

    }

}
