package engine;

import java.io.File;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message.Level;
import org.kie.api.io.KieResources;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
//import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

public class RuleEngine {

	private KieServices kieServices; 
    private KieContainer kieContainer; 
    private KieSession kieSession;
    private KieResources kieResources;
    private KieFileSystem kieFileSystem; 
    private KieRepository kieRepository;
    //private KnowledgeBase knowledgeBase;
	
    public RuleEngine() { 
        this.kieServices = KieServices.Factory.get(); 
        this.kieResources = kieServices.getResources(); 
        this.kieFileSystem = kieServices.newKieFileSystem(); 
        this.kieRepository = kieServices.getRepository(); 
    } 
    
    public KieSession buildKnowledgeSession() { 
        KieBuilder kb = kieServices.newKieBuilder(kieFileSystem); 

        kb.buildAll(); 

        if (kb.getResults().hasMessages(Level.ERROR)) { 
            throw new RuntimeException("Build Errors:\n" + kb.getResults().toString()); 
        } 

        kieContainer = kieServices.newKieContainer(kieRepository.getDefaultReleaseId()); 

        kieSession = this.kieContainer.newKieSession(); 

        return kieSession; 
    } 
    
	public void addRuleFile(String packagename, String rulefile) { 
		Resource resource = kieResources.newClassPathResource(packagename+"/"+rulefile); 
		
        packagename = packagename.replace(".","/"); 

        String resourcepath = "src/main/resources/"+packagename+"/"+rulefile;

        kieFileSystem.write(resourcepath, resource);
		//createKnowledgeBase(rulefile);
    }
	
	public void deleteRuleFile(String packagename, String rulefile) {
		String resourcepath = "src/main/resources/"+packagename+"/"+rulefile;
		kieFileSystem.delete(resourcepath);
	}
	/*private void createKnowledgeBase(String rulefile) {

		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		Resource r = ResourceFactory.newFileResource(new File(rulefile));

		kbuilder.add(r, ResourceType.DRL);
		// System.out.println("Loaded Resource File.");

		if (kbuilder.hasErrors()) {
			throw new RuntimeException(kbuilder.getErrors().toString());
		}
		this.knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
		knowledgeBase.addKnowledgePackages(kbuilder.getKnowledgePackages());
	}*/
	
	public void dispose() {
		kieSession.dispose();
	}
}
