package com.example.ism2022;

import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Network extends AsyncTask<URL,Void, InputStream> {

    InputStream ist = null;
    FXRate fxRate;
    @Override
    protected InputStream doInBackground(URL... urls) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) urls[0].openConnection();
            conn.setRequestMethod("GET");
            ist = conn.getInputStream();
            //parse the ist object
            Parsing(ist);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ist;
    }
    public static Node getNodeByName(String nodeName, Node parentNode) throws Exception {

        if (parentNode.getNodeName().equals(nodeName)) {
            return parentNode;
        }

        NodeList list = parentNode.getChildNodes();
        for (int i = 0; i < list.getLength(); i++)
        {
            Node node = getNodeByName(nodeName, list.item(i));
            if (node != null) {
                return node;
            }
        }
        return null;
    }

    public static String getAttributeValue(Node node, String attrName) {
        try {
            return ((Element)node).getAttribute(attrName);
        }
        catch (Exception e) {
            return "";
        }
    }

    public void Parsing(InputStream ist)
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document domDoc = db.parse(ist);

            domDoc.getDocumentElement().normalize();

            fxRate = new FXRate();

            Node cube = getNodeByName("Cube", domDoc.getDocumentElement());
            if(cube!=null)
            {
                String data = getAttributeValue(cube, "date");
                fxRate.setDate(data);

                NodeList listaCopii = cube.getChildNodes();
                for(int i=0;i<listaCopii.getLength();i++)
                {
                    Node node = listaCopii.item(i);
                    String atribut = getAttributeValue(node, "currency");
                    if(atribut.equals("EUR"))
                        fxRate.setEuro(node.getTextContent());
                    if(atribut.equals("GBP"))
                        fxRate.setPound(node.getTextContent());
                    if(atribut.equals("USD"))
                        fxRate.setDolar(node.getTextContent());
                    if(atribut.equals("XAU"))
                        fxRate.setGold(node.getTextContent());
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
