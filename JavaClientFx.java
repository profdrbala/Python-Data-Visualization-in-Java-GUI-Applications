/**
 *
 * @author Dr.Bala
 */
import java.io.*;
import java.net.*;
import javafx.application.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class JavaClientFx  extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        Label l1 = new Label("Command input to Python Server");
        Label l2 = new Label("Data from Python server");
        Label l3 = new Label("Visualization from Python server");
        TextArea command = new TextArea();
        command.setMaxWidth(450);
        TextArea result=new TextArea();
        //result.setEditable(false);
        result.setMaxWidth(450);
        Button sendBtn = new Button("Send");
        Button exitBtn = new Button("Exit");
        ImageView imageView = new ImageView();
        
        sendBtn.setOnAction(e -> { 
        String output="";
        try{
            String sendData = command.getText().trim();
            command.setText("");
            String[] code = sendData.split("\n");
            if (sendData.length() > 0) {
                Socket s=null;
                DataInputStream in=null;
                DataOutputStream out=null;
             for (String xcode : code ) {
                s=new Socket("10.153.70.91",1234); //10.153.70.91
                in=new DataInputStream(s.getInputStream());
                out=new DataOutputStream(s.getOutputStream());
                output= ">>> "+ xcode + "\n"; //+ result.getText();
                   out.write(xcode.trim().getBytes());
                   if(xcode.equals("exit()") || xcode.equals("quit()") ) {
                        s.close();
                        output += "Disconnected"+ "\n" + result.getText();
                        result.setText(output);
                    }
                    else{ //receive from python
                       byte[] bdata=new byte[64000];
                       String sdata="",edata="";
                       if(xcode.equals("chart")){
                           File f=new File("plot.jpg");
                           if(f.exists())  f.delete();
                           FileOutputStream fileOutputStream = new FileOutputStream("plot.jpg");
                           fileOutputStream.write(bdata, 0, in.read(bdata,0,bdata.length));
                           fileOutputStream.close();
                           fileOutputStream.flush();
                         //creating the image object
                          InputStream stream = new FileInputStream("plot.jpg");
                          //Creating the image view
                          Image image = new Image(stream);
                          
                          //Setting image to the image view
                          
                          imageView.setImage(image);
                          //Setting the image view parameters
                          //imageView.setPreserveRatio(true);
                        }
                        else{
                        sdata=String.valueOf(in.read(bdata,0,bdata.length)); //numbers
                        edata = new String(bdata);                           //text                   
                        }
                   if(edata.trim().equals("")){
                        output += ">>> "+ sdata + "\n" + result.getText();
                        result.setText(output); // number data
                    }
                    else{
                        output += ">>> "+ edata + "\n" + result.getText() ;
                        result.setText(output);  //text data
                        if(edata.contains("Runtime Error: ")) {
                        output = "Connection Terminated, reconnect again..\n" + output; 
                        result.setText(output); }
                    }
                    }
               }
            }
            else {
                output += "Type the command to send to the Python server..." + "\n" + result.getText();
                result.setText(output);
            }           
         }catch(Exception e1){ output += e1.toString() + "\n" + result.getText(); result.setText(output);}
        });

        exitBtn.setOnAction(e -> Platform.exit());
      GridPane gridPane = new GridPane();    //Creating a Grid Pane 
      gridPane.setPadding(new Insets(10, 10, 10, 10)); //Setting the padding 
      //Arranging all the nodes in the grid 
      gridPane.add(l1, 0, 0); 
      gridPane.add(command, 0, 1); 
      gridPane.add(sendBtn, 1, 1);
      gridPane.add(l2, 0, 2); 
      gridPane.add(result, 0, 3); 
      gridPane.add(exitBtn, 1, 3); 
      gridPane.add(l3, 0, 4);
      gridPane.add(imageView, 0, 5); 
      
        Scene scene = new Scene(gridPane,550,750);
        stage.setScene(scene);
        stage.setTitle("JavaFx Client");
        stage.show();
    }
}
