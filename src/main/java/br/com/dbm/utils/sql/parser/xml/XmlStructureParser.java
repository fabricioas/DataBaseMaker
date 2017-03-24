package br.com.dbm.utils.sql.parser.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.com.dbm.utils.sql.model.CheckConstraint;
import br.com.dbm.utils.sql.model.Constraint;
import br.com.dbm.utils.sql.model.ForeignKey;
import br.com.dbm.utils.sql.model.Metadata;
import br.com.dbm.utils.sql.model.TableAttribute;
import br.com.dbm.utils.sql.parser.StructureParser;

/**
 *
 * @author ltonietto
 */
public class XmlStructureParser implements StructureParser {

    private String comment;

    @Override
    public Metadata readMetadata(String filename) throws Exception {
        Document doc = parseDocument(filename);
        NodeList temp = doc.getElementsByTagName("table");
        return readTableElement(temp.item(0));
    }

    public Document parseDocument(String filename) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(filename);
    }

    private void parseXmlFile(String filename) throws Exception {
//        SAXParserFactory spf = SAXParserFactory.newInstance();
//        SAXParser sp = spf.newSAXParser();
//        sp.parse(filename, this);
    }

    private Metadata readTableElement(Node element) throws Exception {
        Metadata metadata = new Metadata();
        NamedNodeMap attrs = element.getAttributes();
        String name = attrs.getNamedItem("name").getTextContent();
        metadata.setTableName(name);

        NodeList nodes = element.getChildNodes();

        int length = nodes.getLength();
        for (int i = 0; i < length; i++) {
            Node node = nodes.item(i);
            if ("owner".equals(node.getNodeName())) {
                metadata.setTableOwner(node.getTextContent());
            } else if ("comment".equals(node.getNodeName())) {
                metadata.setTableComment(node.getTextContent());
            } else if ("sequence".equals(node.getNodeName())) {
                metadata.setSequenceName(node.getTextContent());
            } else if ("trigger".equals(node.getNodeName())) {
                NodeList trgItems = node.getChildNodes();
                for (int j = 0; j < trgItems.getLength(); j++) {
                    Node trgNode = trgItems.item(j);
                    if ("name".equals(trgNode.getNodeName())) {
                        metadata.setTriggerName(trgNode.getTextContent());
                    } else if ("comment".equals(trgNode.getNodeName())) {
                        metadata.setTriggerComment(trgNode.getTextContent());
                    }
                }
            } else if ("attributes".equals(node.getNodeName())) {
                readAttributes(metadata, node.getChildNodes());
            }
        }

        return metadata;
    }

    private void readAttributes(Metadata metadata, NodeList attributes) throws Exception {
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attrNode = attributes.item(i);
            if ("attribute".equals(attrNode.getNodeName())) {
                TableAttribute attr = new TableAttribute();
                NamedNodeMap params = attrNode.getAttributes();
                attr.setMandatory("false".equals(params.getNamedItem("nullable").getTextContent()));
                NodeList attrItems = attrNode.getChildNodes();
                for (int j = 0; j < attrItems.getLength(); j++) {
                    Node item = attrItems.item(j);
                    if ("name".equals(item.getNodeName())) {
                        attr.setAttrName(item.getTextContent());
                    } else if ("constraint".equals(item.getNodeName())) {
                        NamedNodeMap cnstParams = item.getAttributes();
                        String name = cnstParams.getNamedItem("name").getTextContent();
                        Constraint constraint = Constraint.createInstance(name);
                        if (constraint instanceof ForeignKey) {
                            ForeignKey fk = (ForeignKey) constraint;
                            NodeList cnstItems = item.getChildNodes();
                            for (int k = 0; k < cnstItems.getLength(); k++) {
                                Node cnstItem = cnstItems.item(k);
                                if ("referenceTable".equals(cnstItem.getNodeName())) {
                                    fk.setReferenceTable(cnstItem.getTextContent());
                                } else if ("referenceId".equals(cnstItem.getNodeName())) {
                                    fk.setReferenceAttribute(cnstItem.getTextContent());
                                }
                            }
                        } else if (constraint instanceof CheckConstraint) {
                            CheckConstraint ck = (CheckConstraint) constraint;
                            NodeList cnstItems = item.getChildNodes();
                            for (int k = 0; k < cnstItems.getLength(); k++) {
                                Node cnstItem = cnstItems.item(k);
                                if ("checkValues".equals(cnstItem.getNodeName())) {
                                    NodeList values = cnstItem.getChildNodes();
                                    for (int l = 0; l < values.getLength(); l++) {
                                        Node value = values.item(l);
                                        if("value".equals(value.getNodeName()))
                                        ck.addValue(value.getTextContent());
                                    }
                                }
                            }
                        }
                        attr.setConstraint(constraint);
                    } else if ("type".equals(item.getNodeName())) {
                        attr.setAttrType(item.getTextContent());
                    } else if ("size".equals(item.getNodeName())) {
                        attr.setAttrSize(Integer.valueOf(item.getTextContent()));
                    } else if ("decimal".equals(item.getNodeName())) {
                        attr.setAttrDecimalSize(Integer.valueOf(item.getTextContent()));
                    } else if ("default".equals(item.getNodeName())) {
                        attr.setDefaultValue(item.getTextContent());
                    } else if ("comment".equals(item.getNodeName())) {
                        attr.setAttrComment(item.getTextContent());
                    }
                }
                metadata.addAttribute(attr);
            }
        }
    }

}
