package engine;

import java.io.File;

import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

@SuppressWarnings({ "deprecation", "restriction" })
public class RuleEngine {

    private KieSession kieSession;
	private KnowledgeBase knowledgeBase;
	
    public RuleEngine(String ruleFile) { 
        knowledgeBase = createKnowledgeBase(ruleFile);
    }
	public KieSession getKieSession() {
		return knowledgeBase.newKieSession();
	}
	
	@SuppressWarnings("restriction")
	public KnowledgeBase createKnowledgeBase(String rulefile) {

		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		Resource r = ResourceFactory.newFileResource(new File(rulefile));

		kbuilder.add(r, ResourceType.DRL);
		// System.out.println("Loaded Resource File.");

		if (kbuilder.hasErrors()) {
			throw new RuntimeException(kbuilder.getErrors().toString());
		}
		
		knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
		knowledgeBase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		return knowledgeBase;
	}
	
	public void dispose() {
		kieSession.dispose();
	}

}
