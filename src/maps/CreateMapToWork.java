/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package maps;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.creole.ResourceInstantiationException;
import gate.util.GateException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import twitter4j.GeoLocation;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.json.DataObjectFactory;

public class CreateMapToWork {

       
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {

		try {
                    Gate.init();
                    String encoding = "UTF-8";
                    File inDir=new File("./data/analysed");
                    File[] flist=inDir.listFiles();
                    String floc;
               
                 
                    Document d;
                   
                    Annotation tweet;
                    AnnotationSet tweets;
                    AnnotationSet locations;
                    AnnotationSet lookups;
                    AnnotationSet orgs;
                    AnnotationSet URLs;
                    String app="???";
                    AnnotationSet persons;
                    FeatureMap fm;
                    
                    Map<Object,Object> mp;
                    Map<Object,Object> mp_usr;
                    ArrayList<Double> coordinates;
                    Double lati, longi;
                    String usrName;
                    String id;
                    String creation;
                    String text;
                    int sent;
                    int num_locs=0;
                    int num_orgs=0;
                    int num_urls=0;
                    int num_pers=0;
                    String newTextHeat = "";
                    String newTextCircle = "";
                    String color;
                    color = "#FF00FF";
                    String posColor="...";
                    String negColor="...";
                    String neuColor="....";
                    String textToWriteHeat = "";
                    String textToWriteCircle = "";
                    String sentiLabel;
                    for(int f=0;f<flist.length;f++){
                        newTextHeat = "";
                        newTextCircle= "";
                        floc=flist[f].getAbsolutePath();
                        System.out.println(floc);
                        d=Factory.newDocument(new URL("file:///"+floc), encoding);
                        tweets=d.getAnnotations("Original markups").get("Tweet");
                        tweet=tweets.iterator().next();
                        fm=tweet.getFeatures();
                        mp=(Map<Object, Object>) fm.get("geo");
                        mp_usr=(Map<Object, Object>) fm.get("user");
                        id=(String) fm.get("id");
                        creation=(String) fm.get("created_at");
                        usrName=(String) mp_usr.get("screen_name");
                        
                        // TO COMPLETE: extract app used to tweet!!!
                        
                        
                        text=d.getContent().toString();
                        if(mp!=null) {
                            coordinates=(ArrayList<Double>)mp.get("coordinates");
                            lati=coordinates.get(0);
                            longi=coordinates.get(1);
                            num_locs=NumLocs(d);
                            // TO COMPLETE compute number of URLs
                            // TO COMPLETE compute number of Person(s)
                            // TO COMPLETE compute number of Organization(s)
                            
                            // TO COMPLETE  Compute sentiment of tweet
                            sentiLabel=Sentiment(d);
                            
                            //---- HEAT MAP ----
                            newTextHeat = "new google.maps.LatLng("+lati+","+longi+"),";
                            
                            //---- CRCL MAP ----
                           
                            String newtext = "";
                            text=text.replace("\n", " ").replace("'", " ");
                            for(String token : text.split(" ")){
                                    if(token.contains("http")) {
                                            newtext += "<a href=\""+token+"\" target=\"_blank\"> link </a>";
                                    }
                                    else newtext += token + " ";
                            }
                            // TO COMPLETE  change color according to sentiment
                            // IF CODE HERE 
                                   
                        
                            newTextCircle = "  id"+ id +": {center: {lat: "+ lati +", lng: "+longi+"},"+
                                    
							"color: '" +color+"',"+
							"user: '"  +usrName+" "+id+" ·+',"+
                                    "application: '"+app+"',"+
							"time: '"  +creation+"',"+
							"text: '(SENT: "+sentiLabel+", Locations:....) "  +newtext+"',"+
                                                    
							"},";
                                                
                        }
                        textToWriteHeat=textToWriteHeat +  newTextHeat + "\n";
                        textToWriteCircle=textToWriteCircle +  newTextCircle + "\n";
                    }
                    
                    String fs = System.getProperty("file.separator");
                    
                   
                    
                    String inputFileHeat = "maps"+fs+ "heat-map.html";
                    String inputFileCircle = "maps"+fs+ "circle-map.html";
                    
                  
                              
                        
                        //other if here to decide which kind of map to create
                        MapsUtils.createNewMap(inputFileHeat, textToWriteHeat);
                        MapsUtils.createNewMap(inputFileCircle, textToWriteCircle);
                        
                } catch (ResourceInstantiationException ex) {
                Logger.getLogger(CreateFromAnalysis.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(CreateFromAnalysis.class.getName()).log(Level.SEVERE, null, ex);
            } catch (GateException ex) {
                Logger.getLogger(CreateFromAnalysis.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
        // EXAMPLE OF EXTRACTION
        public static int NumLocs(Document doc) {
            return doc.getAnnotations().get("Location").size();
        }
        // TO COMPLETE
        // Returns POS, NEG o NEU
        public static String Sentiment(Document doc) {
            String sentiment="???";
            int senti=0;
            AnnotationSet lookups=doc.getAnnotations().get("Lookup");
            Annotation lookup;
            FeatureMap fm;
            Iterator<Annotation> ite=lookups.iterator();
            String label;
            while(ite.hasNext()) {
                // compute sentiment value
                
            }
            // TO COMPLETE compute sentiment label according to senti value
            // IF STATMENT HERE
            return sentiment;
        }
}