package org.drools.pmml.pmml_4_2;

import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.dmg.pmml.pmml_4_2.descr.PMML;
import org.drools.compiler.compiler.io.Path;
import org.kie.api.io.Resource;
import org.xml.sax.SAXException;

/**
 * Created by lleveric on 7/12/17.
 */
public class PMMLTreeUnit extends AbstractPMMLUnit {

    public static final PMMLModelType treeModelType = PMMLModelType.TREE;
    private Schema schema;

    PMMLTreeUnit(String modelId) {
        super(modelId, treeModelType);
    }

    PMMLTreeUnit(String modelId, Resource modelResource) {
        super(modelId, treeModelType, modelResource);
        String filePath = modelResource.getSourcePath();
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            schema = schemaFactory.newSchema(Thread.currentThread().getContextClassLoader().getResource(filePath));
        } catch (SAXException e) {
        }
    }

    public PMML getPmmlFromResource() {
        PMML retObject = null;
        try {
            JAXBContext context = JAXBContext.newInstance("PMML",PMML.class.getClassLoader());
            Unmarshaller unmarshaller = context.createUnmarshaller();
            JAXBElement<PMML> element = (JAXBElement<PMML>) unmarshaller.unmarshal(new StreamSource(getModelResource().getInputStream()));
            retObject = element.getValue();
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retObject;
    }


    @Override
    public String getTheory(PMML pmml) {
        // Generate java pojos based on the information in the
        // dataDictionary entries
        String modelDataType = getModelDataType(pmml);
        return modelDataType;
    }
}
