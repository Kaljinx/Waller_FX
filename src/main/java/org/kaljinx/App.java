package org.kaljinx;

import com.sun.jna.win32.W32APIOptions;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import com.sun.jna.Library;
import com.sun.jna.Native;


import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class App extends Application {
    Stage window;
    File f;
    ListView<String> blist = new ListView<>();
    String nl = System.lineSeparator();
    File file;

    @Override
    public void start(Stage primarystage) {
        //Stage
        window = primarystage;
        window.setTitle("Waller");

        VBox root = new VBox(10);
        HBox toadd = new HBox(10);
        TextField fileinput = new TextField();
        Button add = new Button("Add");
        Button select = new Button("Select");
        Button set = new Button("Set");
        Button delete = new Button("Delete");
        HBox bottom = new HBox(10);
        FileChooser fc = new FileChooser();

        toadd.getChildren().addAll(fileinput,select,add);
        bottom.getChildren().addAll(set,delete);
        root.getChildren().addAll(toadd,blist,bottom);
        root.setAlignment(Pos.TOP_CENTER);
        toadd.setAlignment(Pos.CENTER);
        bottom.setAlignment(Pos.CENTER);
        load("imglocation.txt",true);
        fc.setTitle("Find Wallpaper");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files","*.png","*.jpg","*.jpeg"));

        select.setOnAction(event -> {
            try {
                f= fc.showOpenDialog(primarystage);
                fileinput.setText(f.getPath());
            }
            catch (NullPointerException e){fileinput.setText("");}
        });
        //Add to List
        add.setOnAction(event -> {
            f = new File(fileinput.getText());
            if(f.exists()){
                blist.getItems().add(f.getName());
                writefile(fileinput.getText(),true,true);
            }
            else {
                AlertBox.display("File Not Found","The File You Selected does not exist");
            }
        });
        //delete button
        delete.setOnAction(event -> {
            delete(blist.getSelectionModel().getSelectedIndex());
        });

        //Show
        Scene scene = new Scene(root, 400, 500);
        window.setResizable(false);
        window.setScene(scene);
        window.show();

        //Close
        window.setOnCloseRequest(e -> {
            e.consume();
            closefunction();
        });

        //Set
        set.setOnAction(event -> {
            System.out.println(getitem(blist.getSelectionModel().getSelectedIndex(),file));
            changer(getitem(blist.getSelectionModel().getSelectedIndex(),file));
        });
    }
    private void closefunction(){
        boolean answer = ComfirmBox.display("Quit","Are you sure?");
        if (answer){window.close();}

    }
    private String getitem(int line, File f){
        String st=null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String[] storage = br.readLine().split("::");
            st=storage[line];
        }
        catch (FileNotFoundException e){ System.out.println("File Not Found"); }
        catch (NoSuchElementException e){System.out.println("Line Does Not Exist");}
        catch (IOException e) { e.printStackTrace(); }
        return st;
    }

    private void writefile(String writing,boolean append,boolean splitter){
        try {
            FileWriter writer = new FileWriter("imglocation.txt",append);
            if (splitter) writer.write(writing+"::");
            else writer.write(writing);
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    private void delete(int line){
        try {
            BufferedReader br = new BufferedReader(new FileReader("imglocation.txt"));
            String[] storage = br.readLine().split("::");
            for (String s : storage){
                if (s==storage[0]){writefile("",false,false);}
                if (s==storage[line]){continue;}
                writefile(s,true,true);
            }
            blist.getItems().remove(line);

        }
        catch (FileNotFoundException e){System.out.println("File Does Not Exist: [delete()]");}
        catch (NullPointerException e){ System.out.println("Nothing in file: [delete()]"); }
        catch (Exception e){e.printStackTrace(); System.out.println("Unknown Exeption [delete()]\n");e.printStackTrace();}
    }

    private void load(String location,boolean startup){
        try {
            file = new File(location);
            if (startup){
                BufferedReader fl = new BufferedReader(new FileReader(location));
                String[] str = fl.readLine().split("::");
                for (String i : str){
                    File ld = new File(i);
                    blist.getItems().add(ld.getName());
                }
                fl.close();
                System.out.println("File Loaded: "+file.getAbsolutePath());
            }
        }
        catch (FileNotFoundException e){System.out.println("File Does Not Exist: [load()]");}
        catch (NullPointerException e){ System.out.println("Nothing in file: [load()]"); }
        catch (Exception e){e.printStackTrace(); System.out.println("Unknown Exeption [load()]");}
    }

    public interface User32 extends Library {
        User32 inst = (User32) Native.load("user32",User32.class, W32APIOptions.DEFAULT_OPTIONS);
        boolean SystemParametersInfo(int uiAction,int uiPram,String pvParam,int fWinIni);
    }
    private void changer(String filepath){
        User32.inst.SystemParametersInfo(0x0014,0,filepath,1);
    }

    public static void main(String[] args) {
        launch();
    }

}