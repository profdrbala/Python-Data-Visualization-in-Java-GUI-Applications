/**
 *
 * @author Dr.Bala
 */

import java.io.*;
import java.net.*;
import javax.swing.*;

public class JavaClientSwing extends javax.swing.JFrame {
    public JavaClientSwing() {
        initComponents();
        setTitle("Java Swing Client");    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        inputEnter = new javax.swing.JTextArea();
        sendData = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        outputShow = new javax.swing.JTextArea();
        exitApp = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        imageShow = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(550, 750));

        jLabel1.setText("Command input to Python Server");

        inputEnter.setColumns(20);
        inputEnter.setRows(5);
        jScrollPane1.setViewportView(inputEnter);

        sendData.setText("Send");
        sendData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendDataActionPerformed(evt);
            }
        });

        jLabel2.setText("Output from Python Server");

        outputShow.setColumns(20);
        outputShow.setRows(5);
        jScrollPane2.setViewportView(outputShow);

        exitApp.setText("Exit");
        exitApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitAppActionPerformed(evt);
            }
        });

        jLabel3.setText("Visualization from Python");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(imageShow)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
                            .addComponent(jScrollPane1))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sendData, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(exitApp, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(22, 22, 22))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(sendData))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(exitApp))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(imageShow)
                .addContainerGap(291, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sendDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendDataActionPerformed
        String output="";
        try{
            String sendData = inputEnter.getText().trim();
            inputEnter.setText("");
            String[] code = sendData.split("\n");
            if (sendData.length() > 0) {
                Socket s=null;
                DataInputStream in=null;
                DataOutputStream out=null;
             for (String xcode : code ) {
                s=new Socket("10.153.70.91",1234); //10.153.70.91
                in=new DataInputStream(s.getInputStream());
                out=new DataOutputStream(s.getOutputStream());
                output= ">>> " + xcode + "\n"; //+ result.getText();
                   out.write(xcode.trim().getBytes());
                   if(xcode.equals("exit()") || xcode.equals("quit()") ) {
                        s.close();
                        output += "Disconnected"+ "\n" + outputShow.getText();
                        outputShow.setText(output);
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
                          //Setting the image view parameters
                             ImageIcon icon = new ImageIcon("plot.jpg");
                             icon.getImage().flush();
                             imageShow.setIcon(icon);
                             imageShow.updateUI();
                             
                        }
                        else{
                        sdata=String.valueOf(in.read(bdata,0,bdata.length)); //numbers
                        edata = new String(bdata);                           //text                   
                        }
                   if(edata.trim().equals("")){
                        output += ">>> "+ sdata + "\n" + outputShow.getText();
                        outputShow.setText(output); // number data
                    }
                    else{
                        output += ">>> "+ edata + "\n" + outputShow.getText() ;
                        outputShow.setText(output);  //text data
                        if(edata.contains("Runtime Error: ")) {
                        output = "Connection Terminated, reconnect again..\n" + output; 
                        outputShow.setText(output); }
                    }
                    }
               }
            }
            else {
                output += "Type the command to send to the Python server..." + "\n" + outputShow.getText();
                outputShow.setText(output);
            }           
         }catch(Exception e1){ output += e1.toString() + "\n" + outputShow.getText(); outputShow.setText(output);}
    }//GEN-LAST:event_sendDataActionPerformed

    private void exitAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitAppActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitAppActionPerformed

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(JavaClientSwing.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);  }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JavaClientSwing().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton exitApp;
    private javax.swing.JLabel imageShow;
    private javax.swing.JTextArea inputEnter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea outputShow;
    private javax.swing.JButton sendData;
    // End of variables declaration//GEN-END:variables
}
