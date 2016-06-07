package com.davidthigs.david.taskbreaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Tag;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class DiagramView extends AppCompatActivity {
    private Document doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagram_view);
        String diagram = "";

        //ActionBar actionBar = getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        Task task = (Task)intent.getSerializableExtra(MainActivity.TASK_FOR_DIAGRAM_EXTRA);
        JSONObject jsonObject = task.getJSONObject();
        WebView diagramWebview = (WebView) findViewById(R.id.webview);
        diagramWebview.getSettings().setBuiltInZoomControls(true);
        //diagramWebview.getSettings().setLoadWithOverviewMode(true);
        diagramWebview.getSettings().setUseWideViewPort(true);
        diagram = buildDiagram(jsonObject);
        diagramWebview.loadData(diagram, "text/html", "UTF-8");

    }
    public String buildDiagram(JSONObject jsonObject){
        doc = Jsoup.parse("");

        String css = "/*Now the CSS*/\n" +
                "* {margin: 0; padding: 0;}\n" +
                "\n" +
                ".tree ul {\n" +
                "\tpadding-top: 20px; position: relative;\n" +
                "\t\n" +
                "\ttransition: all 0.5s;\n" +
                "\t-webkit-transition: all 0.5s;\n" +
                "\t-moz-transition: all 0.5s;\n" +
                "}\n" +
                "\n" +
                ".tree li {\n" +
                "\tfloat: left; text-align: center;\n" +
                "\tlist-style-type: none;\n" +
                "\tposition: relative;\n" +
                "\tpadding: 20px 5px 0 5px;\n" +
                "\t\n" +
                "\ttransition: all 0.5s;\n" +
                "\t-webkit-transition: all 0.5s;\n" +
                "\t-moz-transition: all 0.5s;\n" +
                "}\n" +
                "\n" +
                "/*We will use ::before and ::after to draw the connectors*/\n" +
                "\n" +
                ".tree li::before, .tree li::after{\n" +
                "\tcontent: '';\n" +
                "\tposition: absolute; top: 0; right: 50%;\n" +
                "\tborder-top: 1px solid #ccc;\n" +
                "\twidth: 50%; height: 20px;\n" +
                "}\n" +
                ".tree li::after{\n" +
                "\tright: auto; left: 50%;\n" +
                "\tborder-left: 1px solid #ccc;\n" +
                "}\n" +
                "\n" +
                "/*We need to remove left-right connectors from elements without \n" +
                "any siblings*/\n" +
                ".tree li:only-child::after, .tree li:only-child::before {\n" +
                "\tdisplay: none;\n" +
                "}\n" +
                "\n" +
                "/*Remove space from the top of single children*/\n" +
                ".tree li:only-child{ padding-top: 0;}\n" +
                "\n" +
                "/*Remove left connector from first child and \n" +
                "right connector from last child*/\n" +
                ".tree li:first-child::before, .tree li:last-child::after{\n" +
                "\tborder: 0 none;\n" +
                "}\n" +
                "/*Adding back the vertical connector to the last nodes*/\n" +
                ".tree li:last-child::before{\n" +
                "\tborder-right: 1px solid #ccc;\n" +
                "\tborder-radius: 0 5px 0 0;\n" +
                "\t-webkit-border-radius: 0 5px 0 0;\n" +
                "\t-moz-border-radius: 0 5px 0 0;\n" +
                "}\n" +
                ".tree li:first-child::after{\n" +
                "\tborder-radius: 5px 0 0 0;\n" +
                "\t-webkit-border-radius: 5px 0 0 0;\n" +
                "\t-moz-border-radius: 5px 0 0 0;\n" +
                "}\n" +
                "\n" +
                "/*Time to add downward connectors from parents*/\n" +
                ".tree ul ul::before{\n" +
                "\tcontent: '';\n" +
                "\tposition: absolute; top: 0; left: 50%;\n" +
                "\tborder-left: 1px solid #ccc;\n" +
                "\twidth: 0; height: 20px;\n" +
                "}\n" +
                "\n" +
                ".tree li a{\n" +
                "\tborder: 1px solid #ccc;\n" +
                "\tpadding: 5px 10px;\n" +
                "\ttext-decoration: none;\n" +
                "\tcolor: #666;\n" +
                "\tfont-family: arial, verdana, tahoma;\n" +
                "\tfont-size: 12px;\n" +
                "\tdisplay: inline-block;\n" +
                "\t\n" +
                "\tborder-radius: 5px;\n" +
                "\t-webkit-border-radius: 5px;\n" +
                "\t-moz-border-radius: 5px;\n" +
                "\t\n" +
                "\ttransition: all 0.5s;\n" +
                "\t-webkit-transition: all 0.5s;\n" +
                "\t-moz-transition: all 0.5s;\n" +
                "}\n" +
                "\n" +
                "/*Time for some hover effects*/\n" +
                "/*We will apply the hover effect the the lineage of the element also*/\n" +
                ".tree li a:hover, .tree li a:hover+ul li a {\n" +
                "\tbackground: #c8e4f8; color: #000; border: 1px solid #94a0b4;\n" +
                "}\n" +
                "/*Connector styles on hover*/\n" +
                ".tree li a:hover+ul li::after, \n" +
                ".tree li a:hover+ul li::before, \n" +
                ".tree li a:hover+ul::before, \n" +
                ".tree li a:hover+ul ul::before{\n" +
                "\tborder-color:  #94a0b4;\n" +
                "}";

        Element link = doc.createElement("style");
        link.attr("media", "screen");
        link.attr("type", "text/css");
        link.text(css);


        doc.head().appendChild(link);
        Node div1 = doc.createElement("div");
        div1.attr("class","tree");
        doc.body().appendChild(div1);
        //Element ul1 = doc.createElement("ul");
        Element div = doc.body().children().get(0);
        Element list = div.appendElement("ul");
        try{
            Element element = jsonObjectToListItem(jsonObject);
            list.appendChild(element);
        }catch (JSONException ex){
            ex.printStackTrace();
        }

        try{
            PrintWriter printWriter = new PrintWriter(new File("tree.html"));
            printWriter.write(doc.toString());
            printWriter.close();

        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }

        return doc.toString();
    }

    public Element jsonObjectToListItem(JSONObject object) throws JSONException {

        Element element = new Element(Tag.valueOf("li"),"");
        Element child = doc.createElement("a");
        child.attr("href","#");
        //Element ul1 = doc.createElement("ul");

        //JSONArray jsonArray = (JSONArray)object.get("children");
        String name = object.getString("name");
        String description = object.getString("description");

        child.text(name);

        JSONArray childrenArray = object.getJSONArray("children");
        element.appendChild(child);
        if(childrenArray.length()>0){

            Element list = element.appendElement("ul");
            for(int i = 0;i<childrenArray.length();i++){
                Element grandchild = jsonObjectToListItem(childrenArray.getJSONObject(i));
                list.appendChild(grandchild);
            }

        }

        return element;

    }
}
