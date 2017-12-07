package org.kie.pmml.pmml_4_2;

import java.io.FileOutputStream;

import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.drools.compiler.kie.builder.impl.KieRepositoryImpl;
import org.drools.compiler.kproject.ReleaseIdImpl;
import org.kie.api.KieServices;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.Results;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;

public class KjarBuilder {
	private static final String USAGE_MESSAGE = ""
			+ "Arguments are missing \n"
			+ "Correct usage is...\n"
			+ "java -cp kie-pmml -jar KjarBuilder gav=desiredReleaseId pmml=pmmlFileName [out=outputDirectoryName] \n"
			+ "\n"
			+ "The optional output directory is used to capture the generated rules and java class files";
	private static final String BASE_PKG = KjarBuilder.class.getPackage().getName();
	private static final String BASE_PKG_DIR = BASE_PKG.replaceAll("\\.", "/")+"/";
    static final String RESOURCES_ROOT = "src/main/resources/";
	private ReleaseId releaseId;
	private String pmmlFilePath;
	private String outputFilePath;
	
	public KjarBuilder() {
		// TODO Auto-generated constructor stub
	}
	
	
	private int buildKjar() {
		KieServices ks = KieServices.Factory.get();
		KieRepository kr = ks.getRepository();
		KieHelper kh = new KieHelper();

		((KieRepositoryImpl)kr).setDefaultGAV(releaseId);
		
		Resource res = ResourceFactory.newFileResource(pmmlFilePath);
		kh.addResource(res);

		KieContainer kc = kh.getKieContainer();
		Results results = kh.verify();
		if (results.hasMessages(Message.Level.ERROR)) {
			System.out.println(results.getMessages().toString());
			return 1;
		}
		InternalKieModule ikm = (InternalKieModule)kr.getKieModule(releaseId);
		try (FileOutputStream fos = new FileOutputStream("/tmp/outputmodule.kjar")) {
			fos.write(ikm.getBytes());
		} catch(Exception e) {
			System.out.println("Error writing container output: "+e.getMessage());
			return 1;
		}
		
		return 0;
	}

	public static void main(String[] args) {
		KjarBuilder builder = new KjarBuilder();
		System.out.println("KJAR From PMML");
		for (int x = 0; x < args.length; x++) {
			if (args[x].startsWith("gav")) {
				String gav = args[x].split("=")[1];
				builder.setReleaseId(new ReleaseIdImpl(gav));
			} else if (args[x].startsWith("pmml")) {
				String fname = args[x].split("=")[1];
				builder.setPmmlFilePath(fname);
			} else if (args[x].startsWith("out")) {
				String outputDirectory = args[x].split("=")[1];
				builder.setOutputFilePath(outputDirectory);
			}
		}
		if (builder.getReleaseId() == null || builder.getPmmlFilePath() == null) {
			System.err.println(USAGE_MESSAGE);
			System.exit(1);
		}
		int exit = builder.buildKjar();
		if (exit == 0) {
			System.out.println("KJAR complete");
		}
		System.exit(exit);
	}

	public ReleaseId getReleaseId() {
		return releaseId;
	}

	public void setReleaseId(ReleaseId releaseId) {
		this.releaseId = releaseId;
	}

	public String getPmmlFilePath() {
		return pmmlFilePath;
	}

	public void setPmmlFilePath(String pmmlFilePath) {
		this.pmmlFilePath = pmmlFilePath;
	}

	public String getOutputFilePath() {
		return outputFilePath;
	}

	public void setOutputFilePath(String outputFilePath) {
		this.outputFilePath = outputFilePath;
	}

}
