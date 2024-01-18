package hr.algebra.carcassonnegame2.utils;

import hr.algebra.carcassonnegame2.model.GameMove;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static hr.algebra.carcassonnegame2.configuration.GameConfiguration.MOVES_XML_FILE_NAME;

public class XmlUtils {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void saveGameMove(GameMove gameMove) {

        List<GameMove> gameMoveList = readGameMoves();
        gameMoveList.add(gameMove);

        try {
            Document document = createDocument("gameMoves");

            for (GameMove gameMoveXmlElement : gameMoveList) {

                Element gameMoveElement = document.createElement("gameMove");
                document.getDocumentElement().appendChild(gameMoveElement);

                gameMoveXmlElement.toXml(gameMoveElement, document, formatter);
            }
            saveDocument(document, MOVES_XML_FILE_NAME);
        } catch (ParserConfigurationException | TransformerException ex) {
            ex.printStackTrace();
        }
    }

    private static Document createDocument(String element) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation domImplementation = builder.getDOMImplementation();
        return domImplementation.createDocument(null, element, null);
    }

    public static Node createElement(Document document, String tagName, String data) {
        Element element = document.createElement(tagName);
        Text text = document.createTextNode(data);
        element.appendChild(text);
        return element;
    }

    private static void saveDocument(Document document, String fileName) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(document), new StreamResult(new File(fileName)));
    }

    public static List<GameMove> readGameMoves() {

        List<GameMove> gameMoveList = new ArrayList<>();

        File xmlFile = new File(MOVES_XML_FILE_NAME);

        if (xmlFile.exists()) {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(new File(MOVES_XML_FILE_NAME));
                gameMoveList.addAll(
                        processGameMoveNodes(document.getDocumentElement(), ""));
            } catch (ParserConfigurationException | SAXException | IOException ex) {
                ex.printStackTrace();
            }
        }

        return gameMoveList;
    }

    private static List<GameMove> processGameMoveNodes(Node node, String indent) {

        List<GameMove> gameMoveList = new ArrayList<>();

        if (node.getNodeType() == Node.ELEMENT_NODE) {

            Element nodeElement = (Element) node;

            NodeList nodeList = nodeElement.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++) {

                if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element childElement = (Element) nodeList.item(i);
                    gameMoveList.add(GameMove.fromXml(childElement, formatter));
                }
            }
        }

        return gameMoveList;
    }

    public static String getChildElementText(Element parentElement, String childTagName) {
        Element childElement = (Element) parentElement.getElementsByTagName(childTagName).item(0);
        if (childElement == null)
            return null;
        return childElement.getTextContent();
    }

    public static void createNewReplayFile() {
        try {
            Document document = createDocument("gameMoves");
            saveDocument(document, MOVES_XML_FILE_NAME);
        } catch (ParserConfigurationException | TransformerException ex) {
            ex.printStackTrace();
        }
    }


}
