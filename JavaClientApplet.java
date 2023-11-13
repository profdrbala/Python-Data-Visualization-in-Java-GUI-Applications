
/**
 *
 * @author bala.veerasamy
 */
import java.applet.*; 
import java.awt.*;  
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class JavaClientApplet extends Applet implements ActionListener {  
  Image picture=null;  
  Label label1,label2,label3;
  TextArea instructions, result;
  Button send,exit;
  @Override
  public void init() {  
    try{
        resize(550, 750); 
        label1 = new Label("Command input to Python ");
        label2 = new Label("Output from Python");
        label3 = new Label("Visualization from Python ");
        instructions=new TextArea();
        result=new TextArea();
	//result.setEditable(false);
        send= new Button("Send");
        send.addActionListener(this);
        exit= new Button("Exit");
        exit.addActionListener(this);
        add(label1); add(instructions); add(send);
        add(label2); add(result);       add(exit);
        add(label3);
          
    }catch(Exception e){System.out.println(e);}
  }  
    
  @Override
  public void paint(Graphics g) { 
      //super.paint(g);
     // g.clearRect(30, 420, 500, 700);
      if(picture!=null){
          g.drawImage(picture, 30,420, this); 
      } 
      
  } 

    @Override
    public void actionPerformed(ActionEvent e) {
        String output="";
        if(e.getActionCommand().equals("Send")){
        try{
            String sendData = instructions.getText().trim();
            instructions.setText("");
            String[] code = sendData.split("\n");
            if (sendData.length() > 0) {
                Socket s=null;
                DataInputStream in=null;
                DataOutputStream out=null;
             for (String xcode : code ) {
                s=new Socket("10.153.70.91",1234); //192.168.0.106 //10.153.70.91   
                in=new DataInputStream(s.getInputStream());
                out=new DataOutputStream(s.getOutputStream());
                output= ">>> " + xcode ; //+ result.getText();
                   out.write(xcode.trim().getBytes());
                   if(xcode.equals("exit()") || xcode.equals("quit()") ) {
                        s.close();
                        output += "Disconnected"+ "\n" + result.getText();
                        result.setText(output);
                    }
                    else{ //receive from python
                       byte[] bdata=new byte[64000];
                       String sdata="",ndata="";
                       if(xcode.equals("chart")){
                           File f=new File("plot.jpg");
                           if(f.exists())  f.delete();
                           FileOutputStream fileOutputStream = new FileOutputStream("plot.jpg");
                           fileOutputStream.write(bdata, 0, in.read(bdata,0,bdata.length));
                           fileOutputStream.close();
                           fileOutputStream.flush();
                          //Setting the image view parameters
                             String dir = "file:" + System.getProperty("user.dir") + "/plot.jpg";
                             URL url=new URL(dir);
                             picture=getImage(url);
                             picture.flush();
                             repaint();
                               
                        }
                        else{
                        ndata=String.valueOf(in.read(bdata,0,bdata.length)); //numbers
                        sdata = new String(bdata);                           //text                   
                        }
                   if(sdata.trim().equals("")){ // number data
                        output = output + "\n" +">>>  "+ ndata + "\n" + result.getText();
                        result.setText(output); 
                    }
                    else{                       //text data
                       
                        output = output + "\n" + ">>> "+ sdata.trim() + "\n" + result.getText();
                        result.setText(output); 
                        if(sdata.contains("Runtime Error: ")) {
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
            
        }else   System.exit(0);
    }
  }  