//package name HelloWorld
package HelloWorld;

//importing the necessary packages
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.File;
import java.util.ArrayList;
import java.awt.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;


//class called test2
public class ExtractingImages {

    //setting up a private static variable of HttpURLConnection
    private static HttpURLConnection connection;

    
//here we have the method called main
    public static void main(String[] args){
        //creating variables of BufferedReader, String and StringBuffer
        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();

    //try to connect to the unsplash.api, if encountered errors printstacktrace().
    try{
        //url for api.unsplash
     URL url = new URL("https://api.unsplash.com/search/photos?query="Your_Query"&client_id="YourClientID");
     //opening the connection between you and the computer.
     connection = (HttpURLConnection)url.openConnection();
            
            //set request method to get because we are retrieving things from the website.
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            //set it to timeout if the time is more than 5000ms.
            connection.setReadTimeout(5000);

            //here we get the respond code, if it is above 299, it imposes an error.
            int status = connection.getResponseCode();
            System.out.println(status);
            //if it is more than 299,error will appear and we will read the error stream message with the code.
            if(status>299){
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while((line =reader.readLine())!= null){
                    responseContent.append(line);
                }
                reader.close();
            }
            // if it is lesser than 299, we are able to get the contents we want here.
            else{
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while((line = reader.readLine())!= null){
                    responseContent.append(line);
                };
                reader.close();
                
            }
            //print the content of the page to string
            System.out.println(responseContent.toString());
            //parsing it to string and finally extracting the contents that we want.
            parse(responseContent.toString());


    }catch(MalformedURLException e){
        e.printStackTrace();
    }catch (IOException e){
        e.printStackTrace();
    }
    finally{
        //disconnect.
        connection.disconnect();
    }
}

//having another method called parse to convert the whole chunk of messages that we retrieved from the json array into the string that we want.
public static String parse(String responseBody){
    //get the entire responsebody,
    String jsonString = responseBody;
    //setting the string to be a jsonobject.
    JSONObject obj = new JSONObject(jsonString);
    //setting array called albums from the obj in the previous statement.
    JSONArray albums = obj.getJSONArray("results");
   
    ArrayList<String> Links = new ArrayList<String>();
    
    for(int i=0;i<albums.length();i++){
        //for however many objects in the jsonobject called albums, we retrieve the particular url of that picture that we want.
        JSONObject album = albums.getJSONObject(i);
        String pagename = album.getJSONObject("urls").getString("regular")+".jpg";
        
        //printing out the page name
        System.out.println(pagename);
        //adding the page name to the array of links.
        Links.add(pagename);
    }
        //for the amount of links, saving the amount of pictures and converting it into an image with text by reading the image and writing inside of the image using java.
        for(int j=0;j<Links.size();j++){
        BufferedImage image = null;
       try{ 
       URL url = new URL(Links.get(j));
        image = ImageIO.read(url);
        Graphics g = image.getGraphics();
        int width = image.getWidth();
        int height = image.getHeight();
        Font f = new Font("TimesRoman",Font.PLAIN, width/20);
        g.setFont(f); 
        g.drawString("That is wife",width/3,height/3);
        
        g.dispose();
        ImageIO.write(image, "jpg", new File("outputpic"+j+".jpg"));
        

       }catch(MalformedURLException e){
        e.printStackTrace();
       }
       catch(IOException e){

       }
    }

return null;


}



}
