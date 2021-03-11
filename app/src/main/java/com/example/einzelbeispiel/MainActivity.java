package com.example.einzelbeispiel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private Object TCPconnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView et = findViewById(R.id.tv_output);
        EditText inputText = (EditText) findViewById(R.id.inputField);
        Button btn1 = findViewById(R.id.btn);
        Button btn2 = findViewById(R.id.btn2);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TCPconnector connectorInstance = new TCPconnector(inputText.getText().toString());
                System.out.println("TEXT!!!:" + inputText.getText().toString());
                connectorInstance.start();
                try {
                    connectorInstance.join();
                } catch (InterruptedException e) {
                    System.out.println("Thread not finished");
                }
                et.setText(connectorInstance.output);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });


    }

    class TCPconnector extends Thread{
        private String serverAddress = "se2-isys.aau.at";
        private String port = "53212";
        private String msg=null;
        private PrintWriter pw;
        private BufferedReader br;
        private boolean isRunning;
        private String output=null;
        private String initial_msg;
        public TCPconnector(String initial_msg){
            this.initial_msg=initial_msg;
        }

        public void run(){
            try{
                InetAddress address = InetAddress.getByName(this.serverAddress);
                Socket socket = new Socket(address, Integer.parseInt(this.port));
                try{
                    //Create PrintWriter
                    pw = new PrintWriter(socket.getOutputStream());
                    pw.println(initial_msg);
                    pw.flush();
                    //Create BufferedReader
                    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    //Waiting for Server to answer
                    while (true) {
                        msg = br.readLine();
                        if (msg != null) {
                            this.output = msg;
                           break;
                        }
                    }
                } catch(Exception e2){
                    e2.printStackTrace();
                }
                finally{
                    socket.close();
                }
            } catch(Exception e1){
                e1.printStackTrace();
            }
        }
    }

    class NetworkThread extends Thread {


        NetworkThread(){

        }

        @Override
        public void run() {

        }
    }
}

