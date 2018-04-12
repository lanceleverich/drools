package org.kie.scorecards;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.drools.compiler.compiler.PMMLCompiler;
import org.kie.dmg.pmml.pmml_4_2.descr.PMML;
import org.kie.scorecards.parser.AbstractScorecardParser;
import org.kie.scorecards.parser.ScorecardParseException;
import org.kie.scorecards.parser.xls.XLSScorecardParser;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

public class ScorecardCompiler {
	private PMML pmmlDocument;
	private List<ScorecardError> scorecardErrors;
	private DrlType drlType;
	private final static Logger logger = LoggerFactory.getLogger(ScorecardCompiler.class);
	private PMMLCompiler pmmlCompiler;
	
	public ScorecardCompiler(DrlType drlType) {
		this.drlType = drlType;
	}
	
	public ScorecardCompiler() {
		this.drlType = DrlType.INTERNAL_DECLARED_TYPES;
	}
	
	public DrlType getDrlType() {
		return drlType;
	}

	public void setDrlType(DrlType drlType) {
		this.drlType = drlType;
	}


	public PMML getPmmlDocument() {
		return pmmlDocument;
	}

	public void setPmmlDocument(PMML pmmlDocument) {
		this.pmmlDocument = pmmlDocument;
	}

	public boolean compileFromExcel(final String pathToFile, final String worksheetName) {
		try (FileInputStream inputStream = new FileInputStream(pathToFile)) {
			if (inputStream != null) {
				try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
					return compileFromExcel(bufferedInputStream,worksheetName);
				}
			}
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(),e);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
		return false;
	}
	
	public boolean compileFromExcel(final InputStream inputStream, final String worksheetName) {
		try {
			AbstractScorecardParser parser = new XLSScorecardParser();
			scorecardErrors = parser.parseFile(inputStream, worksheetName);
			if (scorecardErrors.isEmpty()) {
				pmmlDocument = parser.getPMMLDocument();
				return true;
			}
		} catch (ScorecardParseException e) {
			logger.error(e.getMessage(),e);
		}
		return false;
	}

	public static enum DrlType {
		INTERNAL_DECLARED_TYPES, EXTERNAL_OBJECT_MODEL;
	}
}
