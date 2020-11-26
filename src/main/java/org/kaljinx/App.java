package org.kaljinx;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class App extends Application {
    Stage window;
    File f;
    ListView<String> blist;
    String nl;
    File file;
    File tempfile;

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
        blist = new ListView<>();
        FileChooser fc = new FileChooser();
        nl = System.lineSeparator();

        toadd.getChildren().addAll(fileinput,select,add);
        bottom.getChildren().addAll(set,delete);
        root.getChildren().addAll(toadd,blist,bottom);
        root.setAlignment(Pos.TOP_CENTER);
        toadd.setAlignment(Pos.CENTER);
        bottom.setAlignment(Pos.CENTER);
        tempfile = new File("src/resources/tempfile.txt");
        load("src/resources/imglocation.txt");

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
            //TODO Write code for Background change.
            System.out.println("INCOMPLETE:");
        });
    }
    private void closefunction(){
        boolean answer = ComfirmBox.display("Quit","Are you sure?");
        if (answer){window.close();file.delete();tempfile.delete();}

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
                if (i==line){break;}
                i=i+1;
            }
        }
        catch (FileNotFoundException e){
            System.out.println("File Not Found");
        }
        return st;
    }

    private void writefile(String writing, File fl,boolean append){
        try {
            FileWriter writer = new FileWriter(fl,append);
            writer.append(writing);
            writer.append(nl);
            writer.close();
            System.out.println("Successfully wrote to the file. "+fl.getPath());
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    private void delete(int line){
        try {
            int i = 0;
            int j = 0;
            if (tempfile.exists()){tempfile.delete();}
            tempfile.createNewFile();
            file.setWritable(true);
            file.setReadable(true);
            Scanner fr = new Scanner(file);
            while (fr.hasNextLine()){
                if (i==line){i=i+1;fr.nextLine();continue;}
                fr.nextLine();
                writefile(getline(i,file),tempfile,true);
                i=i+1;
            }
            Scanner ck = new Scanner(tempfile);
            while (ck.hasNextLine()){
                if (j==0){j=j+1;ck.nextLine();writefile(getline(j,tempfile),file,false);}
                writefile(getline(j,tempfile),file,true);
                ck.nextLine();
            }
            blist.getItems().remove(line);

        }

        catch (FileNotFoundException e){System.out.println("File Does Not Exist: [delete()]");}
        catch (NullPointerException e){ System.out.println("Nothing in file: [delete()]"); }
        catch (IOException e){System.out.println("IOExeption: [delete()]");}
        catch (Exception e){e.printStackTrace(); System.out.println("Unknown Exeption [delete()]");}

    }

    private void load(String location){
        try {
            file = new File(location);
            file.createNewFile();
            file.setWritable(true);
            file.setReadable(true);
            Scanner fl = new Scanner(file);
            while (fl.hasNextLine()){
                String s = fl.nextLine();
                if (!(s.equals(""))){
                    File file = new File(s);
                    listadd(file.getName());
                }
            }
            System.out.println("File Loaded: "+file.getAbsolutePath());
        }
        catch (FileNotFoundException e){System.out.println("File Does Not Exist: [load()]");}
        catch (NullPointerException e){ System.out.println("Nothing in file: [load()]"); }
        catch (IOException e){System.out.println("IOExeption: [load()]");}
        catch (Exception e){e.printStackTrace(); System.out.println("Unknown Exeption [load()]");}
    }

    public static void main(String[] args) {
        launch();
    }

}