package com.tgcs.tgcp.bridge.common;

import com.tgcs.tgcp.bridge.checoperations.model.NodeType;
import com.tgcs.tgcp.bridge.checoperations.model.ResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import java.io.StringReader;
import java.io.StringWriter;

public class ObjectToXmlConverter {
    private static Logger logger = LoggerFactory.getLogger(ObjectToXmlConverter.class);

    public static String jaxbObjectToXML(Object object) {
        return jaxbObjectToXML(object, Boolean.TRUE, Boolean.TRUE);
    }

    public static String jaxbObjectToXML(Object object, boolean addSoepHeader) {
        return jaxbObjectToXML(object, Boolean.TRUE, addSoepHeader);
    }

    public static String jaxbObjectToXML(Object object, boolean formatted, boolean addSoepHeader) {
        if (object == null) object = "";
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(object instanceof JAXBElement ?
                    ((JAXBElement<?>) object).getDeclaredType() : object.getClass());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller(); // Create Marshaller
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formatted); // Required formatting?
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(object, sw);
            String xml = sw.toString();

            if (xml == null || !xml.startsWith("<?")) {
                xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + xml;
            }

            if (object instanceof JAXBElement) object = ((JAXBElement<?>) object).getValue();

            if (addSoepHeader && object instanceof ResponseType) {
                xml = addSoepHeader(xml, ((ResponseType) object).getMessageType());
            }

            return xml;
        } catch (JAXBException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public static String addSoepHeader(String xml, String messageType) {
        return "soeps~Session-Id=1|Message-Type=" + messageType + "|~" + xml;
    }

    public static <T> T jaxbXMLToObject(XmlDocument message, Class<T> cls) throws Exception {
        // create an instance of `JAXBContext`
        JAXBContext context = JAXBContext.newInstance(cls);

        // create an instance of `Unmarshaller`
        Unmarshaller unmarshaller = context.createUnmarshaller();

        // convert XML  to  object
        T object = (T) unmarshaller.unmarshal(new StringReader(message.getOuterXml()));

        return object;
    }

    public static <T> JAXBElement createDynamicNodeName(String rootNodeName, T object) {
        String nodeType = object instanceof NodeType ? ((NodeType) object).getNodeType() : "";
        return new JAXBElement(new QName(rootNodeName + nodeType), object.getClass(), object);
    }

}
