package com.sample;

import java.io.IOException;
import java.util.HashSet;
import java.util.StringTokenizer;

import org.kie.api.runtime.KieSession;

import engine.RuleEngine;

/**
 * This is a sample class to launch a rule.
 */
public class DroolsTest {
	
    public static final void main(String[] args) throws IOException {
    	//BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    	StringTokenizer token = new StringTokenizer("กรุงเทพ ชลประทาน ใบขีดสีน้ำตาล");
    	String[] arr = new String[3];
    	for(int i = 0 ; i < 3 ; i++) {
    		arr[i] = token.nextToken();
    		System.out.println(arr[i]);
    	}
        RuleEngine engine = new RuleEngine();
        engine.addRuleFile("rules", "province.drl");
        KieSession kspro = engine.buildKnowledgeSession();
        Message mss1 = new Message();
        mss1.setMessage(arr[0]);
        kspro.insert(mss1);
        kspro.fireAllRules();
        engine.deleteRuleFile("rules", "province.drl");
        /*engine.addRuleFile("rules","envi.drl");
        KieSession ksenvi = engine.buildKnowledgeSession();
        ksenvi = engine.buildKnowledgeSession();
        Message mss2 = new Message();
        mss2.setMessage(arr[1]);
        ksenvi.insert(mss2);
        ksenvi.fireAllRules();
        engine.deleteRuleFile("rules", "envi.drl");*/
        engine.addRuleFile("rules","disease.drl");
        KieSession ksdis = engine.buildKnowledgeSession();
        ksdis = engine.buildKnowledgeSession();
        Message mss3 = new Message();
        mss3.setMessage(arr[2]);
        ksdis.insert(mss3);
        ksdis.fireAllRules();
        HashSet<String> set = mss1.getSet();
        //set.retainAll(mss2.getSet());
        set.retainAll(mss3.getSet());
        for(String s : set) {
        	System.out.println(s);
        }
        
   
    }
    
    public static String getAnswer(String question){
    	String answer = "";
    	String[] arr = question.split(" ");
    	RuleEngine engine = new RuleEngine();
    	HashSet<String> set = new HashSet<String>();
    	for(int i = 0; i < arr.length ; i++){
    		engine.addRuleFile("rules", "province.drl");
    		KieSession kspro = engine.buildKnowledgeSession();
    		Message mss = new Message();
	        mss.setMessage(arr[i]);
	        kspro.insert(mss);
	        kspro.fireAllRules();
	        engine.deleteRuleFile("rules", "province.drl");
	        if(set.isEmpty()) {
	        	if(!mss.getSet().isEmpty())
	        		set = mss.getSet();
//	        	for(String s : set) {
//		        	System.out.println(s);
//		        }
	        }
	        else {
	        	if(!mss.getSet().isEmpty())
	        		set.retainAll(mss.getSet());
	        }
	        
	        engine.addRuleFile("rules", "disease.drl");
    		kspro = engine.buildKnowledgeSession();
    		mss = new Message();
	        mss.setMessage(arr[i]);
	        kspro.insert(mss);
	        kspro.fireAllRules();
	        engine.deleteRuleFile("rules", "disease.drl");
	        if(!mss.getSet().isEmpty())
	        	set.retainAll(mss.getSet());
	        
	        engine.addRuleFile("rules", "envi.drl");
    		kspro = engine.buildKnowledgeSession();
    		mss = new Message();
	        mss.setMessage(arr[i]);
	        kspro.insert(mss);
	        kspro.fireAllRules();
	        engine.deleteRuleFile("rules", "envi.drl");
	        if(!mss.getSet().isEmpty())
	        	set.retainAll(mss.getSet());
	        
	        engine.addRuleFile("rules", "etc.drl");
    		kspro = engine.buildKnowledgeSession();
    		mss = new Message();
	        mss.setMessage(arr[i]);
	        kspro.insert(mss);
	        kspro.fireAllRules();
	        engine.deleteRuleFile("rules", "etc.drl");
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

        public static final int HELLO = 0;
        public static final int GOODBYE = 1;

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
