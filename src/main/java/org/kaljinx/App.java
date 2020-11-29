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
import com.sun.jna.Platform;


import java.io.*;
import java.security.cert.Extension;
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
        load("src/resources/imglocation.txt",true);
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
                listadd(f.getName());
                writefile(fileinput.getText(),file,true);
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
            System.out.println(getline(blist.getSelectionModel().getSelectedIndex(),file));
            changer(getline(blist.getSelectionModel().getSelectedIndex(),file));
        });
    }
    private void closefunction(){
        boolean answer = ComfirmBox.display("Quit","Are you sure?");
        if (answer){window.close();}

    }

    private void listadd(String item){
        blist.getItems().add(item);
    }

    private String  getline(int line, File f){
        String st=null;
        try {
            Scanner fl = new Scanner(f);
            int i = 0;
            while (fl.hasNextLine()){
                st = fl.nextLine();
                if (i!=line&&!(fl.hasNextLine())){
                   fl.nextLine();
                }
                if (i==line){
                    break;
                }
                i=i+1;
            }
            fl.close();
        }
        catch (FileNotFoundException e){
            System.out.println("File Not Found");
        }
        catch (NoSuchElementException e){System.out.println("Line Does Not Exist");}
        return st;
    }

    private void writefile(String writing, File fl,boolean append){
        try {
            FileWriter writer = new FileWriter(fl,append);
            writer.append(writing);
            writer.append(nl);
            writer.close();
            //System.out.println("Successfully wrote to the file. "+fl.getPath());
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    private void delete(int line){
        try {
            Scanner sc = new Scanner(file);
            String store[] = new String[blist.getItems().size()];
            store[0]=null;
            int i=0;
            while (sc.hasNextLine()&&i<blist.getItems().size()){
                if (i==line){
                    sc.nextLine();
                }
                if (sc.hasNextLine()){
                    store[i]=sc.nextLine();
                    i=i+1;
                }
            }
            sc.close();
            if (store[0]==null){
                PrintWriter pw = new PrintWriter(file);
                pw.close();
            }
            else {
                writefile(store[0],file,false);
                for (int j=1;j<store.length-1;j++){
                    System.out.println(j+" : "+store[j]);
                    writefile(store[j],file,true);
                }
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
            file.createNewFile();
            file.setWritable(true);
            file.setReadable(true);
            if (startup){
                Scanner fl = new Scanner(file);
                while (fl.hasNextLine()){
                    String s = fl.nextLine();
                    if (!(s.equals(""))){
                        File file = new File(s);
                        listadd(file.getName());
                    }
                }
                fl.close();
                System.out.println("File Loaded: "+file.getAbsolutePath());
            }
        }
        catch (FileNotFoundException e){System.out.println("File Does Not Exist: [load()]");}
        catch (NullPointerException e){ System.out.println("Nothing in file: [load()]"); }
        catch (IOException e){System.out.println("IOExeption: [load()]");}
        catch (Exception e){e.printStackTrace(); System.out.println("Unknown Exeption [load()]");}
    }

    public static interface User32 extends Library {
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