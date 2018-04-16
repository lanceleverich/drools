package org.kie.scorecards.parser;

import java.io.InputStream;
import java.util.List;

import org.kie.dmg.pmml.pmml_4_2.descr.PMML;
import org.kie.scorecards.parser.ScorecardError;
import org.kie.scorecards.parser.ScorecardParseException;

public abstract class AbstractScorecardParser {

    public abstract List<ScorecardError> parseFile(InputStream inStream, String worksheetName) throws ScorecardParseException;

    public abstract PMML getPMMLDocument();
}
