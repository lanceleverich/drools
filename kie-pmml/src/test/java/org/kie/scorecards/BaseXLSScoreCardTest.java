package org.kie.scorecards;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.drools.compiler.kie.builder.impl.KieRepositoryImpl;
import org.drools.compiler.kproject.ReleaseIdImpl;
import org.drools.core.builder.conf.impl.ScoreCardConfigurationImpl;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.InternalRuleUnitExecutor;
import org.drools.core.impl.KnowledgeBaseImpl;
import org.junit.Ignore;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.ReleaseId;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.pmml.PMML4Result;
import org.kie.api.pmml.PMMLRequestData;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.rule.RuleUnit;
import org.kie.api.runtime.rule.RuleUnitExecutor;
import org.kie.internal.builder.ScoreCardConfiguration;
import org.kie.internal.builder.ScoreCardConfiguration.SCORECARD_INPUT_TYPE;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;
import org.kie.pmml.pmml_4_2.DroolsAbstractPMMLTest;

public class BaseXLSScoreCardTest extends DroolsAbstractPMMLTest {

	private static final String SCORECARD_BASE_DIR = "org/kie/scorecards/";
	private static final String SIMPLE_SCORECARD = SCORECARD_BASE_DIR+"scoremodel_c.xls";
	private static final String SCORECARD_EXTERNAL = SCORECARD_BASE_DIR+"scoremodel_externalmodel.xls";
	
	@Test
	public void testSimpleScoreCard() {
		RuleUnitExecutor executor = createExecutor(SIMPLE_SCORECARD,null);
		assertNotNull(executor);
		
        PMMLRequestData requestData = createRequest("123", "SampleScore", 33.0, "SKYDIVER", "KN", true);
        PMML4Result resultHolder = new PMML4Result();
        data.insert(requestData);
        resultData.insert(resultHolder);

        List<String> possiblePackages = calculatePossiblePackageNames("Sample Score", "org.drools.scorecards.example");
        Class<? extends RuleUnit> unitClass = getStartingRuleUnit("RuleUnitIndicator",(InternalKnowledgeBase)kbase,possiblePackages);
        assertNotNull(unitClass);
        
        executor.run(unitClass);
        assertEquals("OK",resultHolder.getResultCode());
        Double score = resultHolder.getResultValue("CalculatedScore", "value", Double.class).orElse(null);
        assertEquals(41.0, score, 1e-6);
	}
	
	@Test
	@Ignore
	public void testSimpleScoreCardWithWorksheetName() {
		RuleUnitExecutor executor = createExecutor(SCORECARD_EXTERNAL,"scorecards_initialscore");
		assertNotNull(executor);
		
        PMMLRequestData requestData = createRequest("123", "SampleScore", 33.0, "PROGRAMMER", "AP", true);
        PMML4Result resultHolder = new PMML4Result();
        data.insert(requestData);
        resultData.insert(resultHolder);

        List<String> possiblePackages = calculatePossiblePackageNames("Sample Score", "org.drools.scorecards.example");
        Class<? extends RuleUnit> unitClass = getStartingRuleUnit("RuleUnitIndicator",(InternalKnowledgeBase)kbase,possiblePackages);
        assertNotNull(unitClass);
        
        executor.run(unitClass);
        assertEquals("OK",resultHolder.getResultCode());
        Double score = resultHolder.getResultValue("CalculatedScore", "value", Double.class).orElse(null);
        assertEquals(36.0, score, 1e-6);
		
	}

    protected PMMLRequestData createRequest(String correlationId, 
            String model, 
            Double age, 
            String occupation, 
            String residenceState, 
            boolean validLicense) {
        PMMLRequestData data = new PMMLRequestData(correlationId,model);
        data.addRequestParam("age", age);
        data.addRequestParam("occupation", occupation);
        data.addRequestParam("residenceState", residenceState);
        data.addRequestParam("validLicense", validLicense);
        return data;
    }
	
	
	protected RuleUnitExecutor createExecutor(String sourceName, String worksheetName) {
		KieServices ks = KieServices.Factory.get();
		KieRepository kr = ks.getRepository();
		ReleaseId releaseId = new ReleaseIdImpl("org.kie:pmmlTest:1.0-SNAPSHOT");
		((KieRepositoryImpl)kr).setDefaultGAV(releaseId);
		ScoreCardConfiguration scconf = new ScoreCardConfigurationImpl();
		scconf.setInputType(SCORECARD_INPUT_TYPE.EXCEL);
		scconf.setWorksheetName(worksheetName != null ? worksheetName:"scorecards");
    	Resource res = ResourceFactory.newClassPathResource(sourceName).setResourceType(ResourceType.SCARD).setConfiguration(scconf);
    	
    	kbase = new KieHelper().addResource(res, ResourceType.SCARD).build();
    	
    	assertNotNull(kbase);
    	
    	RuleUnitExecutor executor = RuleUnitExecutor.create().bind(kbase);
    	KieContainer kc = ((KnowledgeBaseImpl)((InternalRuleUnitExecutor)executor).getKieSession().getKieBase()).getKieContainer();
    	InternalKieModule ikm = (InternalKieModule)kr.getKieModule(releaseId);
    	try (FileOutputStream fos = new FileOutputStream("/tmp/outputModule.jar")) {
    		fos.write(ikm.getBytes());
    	} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	kc.getKieBaseNames().forEach(n -> {System.out.println(n);});
        data = executor.newDataSource("request");
        resultData = executor.newDataSource("results");
        pmmlData = executor.newDataSource("pmmlData");

		return executor;
	}
	
	
}
