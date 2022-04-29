package jfx;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.Alert;
import javafx.stage.Modality;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.ext.Extensions;



public class App extends Application {
  
  public static void main(String[] args) {
    launch(args);
  }

 

  @Override
  public void start(Stage stage) throws IOException  {
        TextField searchField = new TextField();

        searchField.setPrefWidth(800);
        searchField.setPrefHeight(60);
        searchField.setFont(Font.font("Arial", 30));
        searchField.setPromptText("Search...");
        searchField.setFocusTraversable(false);
        searchField.setStyle("-fx-border-radius: 20px;-fx-background-radius: 20 20 20 20;");

        TextField searchField2 = new TextField();
        searchField2.setPrefWidth(800);
        searchField2.setPrefHeight(60);
        searchField2.setFont(Font.font("Arial", 30));
        searchField2.setPromptText("Search...");
        searchField2.setFocusTraversable(false);
        searchField2.setVisible(false);
        searchField2.setStyle("-fx-border-radius: 20px;-fx-background-radius: 20 20 20 20;");

        Button insertButton = new Button("Insert File");
        insertButton.setPrefSize(100, 50);
        insertButton.setStyle("-fx-border-radius: 20px;-fx-background-radius: 20 20 20 20;-fx-background-color: lime;-fx-text-fill:white;-fx-font-size:15px;");

        Button deleteButton = new Button("Delete File");
        deleteButton.setPrefSize(100, 50);
        deleteButton.setDisable(true);
        deleteButton.setStyle("-fx-border-radius: 20px;-fx-background-radius: 20 20 20 20;-fx-background-color: red;-fx-text-fill:white;-fx-font-size:15px;");

        Button searchButton = new Button("Search");
        searchButton.setPrefSize(100, 50);
        searchButton.setStyle("-fx-border-radius: 20px;-fx-background-radius: 20 20 20 20;-fx-background-color: skyblue;-fx-text-fill:white;-fx-font-size:15px;");
        searchButton.setVisible(false);

        Button historyButton = new Button("History");
        historyButton.setPrefSize(80, 40);
        historyButton.setStyle("-fx-border-radius: 20px;-fx-background-radius: 10 10 10 10;-fx-font-size:15px;");
        
        
      
        List<String> files= new ArrayList<>();
        ObservableList<String> obv1 = FXCollections.observableArrayList();
       
       

        ListView <String> lv = new ListView<>(obv1);
        lv.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        lv.setPrefSize(260, 400);
        
        ListView <String> lv2 = new ListView<>(obv1);
        lv2.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        lv2.setPrefSize(260, 400);
        lv2.setVisible(false);
        lv2.getSelectionModel().select(0);

        ObservableList<String> fileName = FXCollections.observableArrayList();
        ObservableList<String> hitRate = FXCollections.observableArrayList();
       
        
        

        

       

        


        final ToggleGroup group = new ToggleGroup();
        
        RadioButton btn1 = new RadioButton("Simple Search");
        btn1.setUserData("A");
        RadioButton btn2 = new RadioButton("Boolean Search");
        btn2.setUserData("B");
        RadioButton btn3 = new RadioButton("Text Compare");
        btn3.setUserData("C");
        RadioButton btn4 = new RadioButton("Phrase Search");
        btn4.setUserData("D");
        btn1.setSelected(true);
        btn1.setToggleGroup(group);
        btn2.setToggleGroup(group);
        btn3.setToggleGroup(group);
        btn4.setToggleGroup(group);

        TilePane tp = new TilePane(btn1,btn2,btn3,btn4,historyButton);
        tp.setPadding(new Insets(5,0,5,0));

        final ToggleGroup group2 = new ToggleGroup();     //type of field for simple search
        
        RadioButton b1 = new RadioButton("Places Field");
        b1.setUserData("places");
        RadioButton b2 = new RadioButton("Title Field");
        b2.setUserData("title");
        RadioButton b3 = new RadioButton("People Field");
        b3.setUserData("people");
        RadioButton b4 = new RadioButton("Body Field");
        b4.setUserData("body");
        b1.setSelected(true);
        b1.setToggleGroup(group2);
        b2.setToggleGroup(group2);
        b3.setToggleGroup(group2);
        b4.setToggleGroup(group2);

      //<<----------------------------------------------------- <Boolean_search_controls> ---------------------------------------------->>  

        final ToggleGroup group4 = new ToggleGroup();       //type of field for simple search
        RadioButton b5 = new RadioButton("Places Field");
        b5.setUserData("places");
        RadioButton b6 = new RadioButton("Title Field");
        b6.setUserData("title");
        RadioButton b7 = new RadioButton("People Field");
        b7.setUserData("people");
        RadioButton b8 = new RadioButton("Body Field");
        b8.setUserData("body");
        b5.setSelected(true);
        b5.setToggleGroup(group4);
        b6.setToggleGroup(group4);
        b7.setToggleGroup(group4);
        b8.setToggleGroup(group4);
        b5.setVisible(false);
        b6.setVisible(false);
        b7.setVisible(false);
        b8.setVisible(false);

        final ToggleGroup group3 = new ToggleGroup();
        RadioButton bo1 = new RadioButton("MUST");
        bo1.setUserData("MUST");
        RadioButton bo2 = new RadioButton("SHOULD");
        bo2.setUserData("SHOULD");
        RadioButton bo3 = new RadioButton("NOT");
        bo3.setUserData("NOT");

        bo1.setToggleGroup(group3);
        bo2.setToggleGroup(group3);
        bo3.setToggleGroup(group3);
        bo1.setSelected(true);
        bo1.setVisible(false);
        bo2.setVisible(false);
        bo3.setVisible(false);

        final ToggleGroup group5 = new ToggleGroup();
        RadioButton bo4 = new RadioButton("MUST");
        bo4.setUserData("MUST");
        RadioButton bo5 = new RadioButton("SHOULD");
        bo5.setUserData("SHOULD");
        RadioButton bo6 = new RadioButton("NOT");
        bo6.setUserData("NOT");

        bo4.setToggleGroup(group5);
        bo5.setToggleGroup(group5);
        bo6.setToggleGroup(group5);
        bo4.setSelected(true);
        bo4.setVisible(false);
        bo5.setVisible(false);
        bo6.setVisible(false);

        
        
        Label label1 = new Label("Term1");
        Label label2 = new Label("Term2");
        label1.setVisible(false);
        label2.setVisible(false);

        TilePane tp2 = new TilePane(b1,b2,b3,b4,label1,bo1,bo2,bo3);
        tp2.setPadding(new Insets(5,0,5,15));

        TilePane tp3 = new TilePane(b5,b6,b7,b8,label2,bo4,bo5,bo6);
        tp3.setPadding(new Insets(5,0,5,15));

        Button submit = new Button("Submit");
        submit.setVisible(false);
        submit.setStyle("-fx-border-radius: 20px;-fx-background-radius: 20 20 20 20;");
        //<<----------------------------------------------------- </Boolean_search_controls> ---------------------------------------------->>  
       

        
        File f = new File("Data");

        String[] pathnames;
        // Populates the array with names of files and directories
        pathnames = f.list();

        // For each pathname in the pathnames array
        for (String pathname : pathnames) {
            obv1.add(pathname);
        }

        LuceneTester lt = new LuceneTester();
        lt.start();
        
        //<<-----------------------------------------------Event Handlers -- Listeners------------------------------------->>

        //listener for the radio-buttons
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
          @Override
          public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
  
            
              //System.out.println(group.getSelectedToggle().getUserData().toString());
              if(group.getSelectedToggle().getUserData().toString() == "A")
              {
                b1.setVisible(true);
                b2.setVisible(true);
                b3.setVisible(true);
                b4.setVisible(true);
                b5.setVisible(false);
                b6.setVisible(false);
                b7.setVisible(false);
                b8.setVisible(false);
                bo1.setVisible(false);
                bo2.setVisible(false);
                bo3.setVisible(false);
                bo4.setVisible(false);
                bo5.setVisible(false);
                bo6.setVisible(false);
                label1.setVisible(false);
                label2.setVisible(false);
                submit.setVisible(false);
                searchField.setText("");
                searchField.setPromptText("Search...");
                searchField2.setVisible(false);
                lv2.setVisible(false);
                insertButton.setVisible(true);
                deleteButton.setVisible(true);
                lv.setVisible(true);
                searchField.setDisable(false);
                searchButton.setVisible(false);
              }
              else if(group.getSelectedToggle().getUserData().toString() == "B")
              {
                b1.setVisible(true);
                b2.setVisible(true);
                b3.setVisible(true);
                b4.setVisible(true);
                b5.setVisible(true);
                b6.setVisible(true);
                b7.setVisible(true);
                b8.setVisible(true);
                bo1.setVisible(true);
                bo2.setVisible(true);
                bo3.setVisible(true);
                bo4.setVisible(true);
                bo5.setVisible(true);
                bo6.setVisible(true);
                label1.setVisible(true);
                label2.setVisible(true);
                submit.setVisible(true);
                searchField2.setVisible(true);
                searchField.setText("");
                searchField2.setText("");
                searchField.setPromptText("term1:");
                searchField2.setPromptText("term2:");
                lv2.setVisible(false);
                insertButton.setVisible(true);
                deleteButton.setVisible(true);
                lv.setVisible(true);
                searchField.setDisable(false);
                searchButton.setVisible(false);
              }
              else if(group.getSelectedToggle().getUserData().toString() == "C")
              {
                b1.setVisible(false);
                b2.setVisible(false);
                b3.setVisible(false);
                b4.setVisible(false);
                b5.setVisible(false);
                b6.setVisible(false);
                b7.setVisible(false);
                b8.setVisible(false);
                bo1.setVisible(false);
                bo2.setVisible(false);
                bo3.setVisible(false);
                bo4.setVisible(false);
                bo5.setVisible(false);
                bo6.setVisible(false);
                submit.setVisible(false);
                searchField.setPromptText("Search...");
                searchField2.setVisible(false);
                label1.setVisible(false);
                label2.setVisible(false);
                lv2.setVisible(true);
                insertButton.setVisible(false);
                deleteButton.setVisible(false);
                lv.setVisible(false);
                searchField.setDisable(true);
                searchButton.setVisible(true);
                lv2.getSelectionModel().select(0);
                lv2.getFocusModel().focus(0);
              }
              else if(group.getSelectedToggle().getUserData().toString() == "D")
              {
                b1.setVisible(false);
                b2.setVisible(false);
                b3.setVisible(false);
                b4.setVisible(false);
                b5.setVisible(false);
                b6.setVisible(false);
                b7.setVisible(false);
                b8.setVisible(false);
                bo1.setVisible(false);
                bo2.setVisible(false);
                bo3.setVisible(false);
                bo4.setVisible(false);
                bo5.setVisible(false);
                bo6.setVisible(false);
                submit.setVisible(false);
                searchField.setPromptText("Search...");
                searchField2.setVisible(false);
                label1.setVisible(false);
                label2.setVisible(false);
                lv2.setVisible(false);
                insertButton.setVisible(true);
                deleteButton.setVisible(true);
                lv.setVisible(true);
                searchField.setDisable(false);
                searchButton.setVisible(false);
              }
          }
      });

        //insert button event handler
        insertButton.setOnAction((event) -> {  

          FileChooser fc = new FileChooser();
          fc.setInitialDirectory(new File ("Reuters_articles//"));
          fc.getExtensionFilters().addAll(new ExtensionFilter("txt files", "*.txt"));
          File selectedFile = fc.showOpenDialog(null);
          
          File destFile = new File("Data\\" + selectedFile.getName());
          
          try (FileReader fin = new FileReader(selectedFile)) 
          {
            try (FileWriter fout = new FileWriter(destFile, true)) {
              int c;  
              while ((c = fin.read()) != -1) 
              {  
                fout.write(c);  
              }  
              obv1.clear();
              String[] pathnames2;
              pathnames2 = f.list();
              for (String pathname : pathnames2) 
              {
                obv1.add(pathname);
              }
              lv.setItems(obv1);
              fin.close();  
              fout.close();
              try
              {
                lt.DeleteIndexFiles();
                lt.start();
              }
              catch (ParseException e3)
              {
                e3.printStackTrace();
              }
            } 
            catch (IOException e1) 
            {
              e1.printStackTrace();
            }
          }
          catch (IOException e1) 
          {
            e1.printStackTrace();
          }
        


        });
          
        //we disable the delete button when we can't use it
        lv.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){

        @Override
        public void changed(ObservableValue<?   extends String> observable, String oldValue, String newValue)
        {
          int position = lv.getSelectionModel().getSelectedIndex();
          if(position == -1)
            deleteButton.setDisable(true);
          else
            deleteButton.setDisable(false);
        }
        });
        
        
        //delete button event handler
        deleteButton.setOnAction((event) -> {
          
          int position = lv.getSelectionModel().getSelectedIndex();
          
          String delete = "Data\\" + obv1.get(position);
          File temp = new File(delete);
          System.out.println(delete);
          if (temp.delete()) { 
            System.out.println("Deleted the file: " + temp.getName());
          } else {
            System.out.println("Failed to delete the file.");
          } 
          obv1.remove(position);
          lv.setItems(obv1);
          
          try
          {
            lt.DeleteIndexFiles();
            lt.start();
          }
          catch (ParseException e3)
          {
            e3.printStackTrace();
          }
          catch (IOException e2)
          {
            e2.printStackTrace();
          }
          
          
        });

        
        File historyFile = new File("history.txt");
        //searchField event handler
        searchField.setOnAction((event) -> {  
            try 
            {
              if(searchField.getLength() > 0)
              {
                String TypeOfSearch = group.getSelectedToggle().getUserData().toString();
                if(TypeOfSearch == "A")
                {
                  fileName.clear();
                  hitRate.clear();
                  
                  String field = group2.getSelectedToggle().getUserData().toString();
                  lt.search(searchField.getText(),field);
                  hitRate.addAll(lt.getHits());
                  fileName.addAll(lt.getFiles());
                  
                  if(hitRate.size() == 0)
                  {
                    Alert alert = new Alert(AlertType.WARNING);

                    
                    alert.setTitle("Search Failed");
                    alert.setHeaderText("No results found");

                    
                    alert.initModality(Modality.WINDOW_MODAL);

                    
                    alert.initOwner(stage);

                    alert.getDialogPane().setExpandableContent(new StackPane(new Label("We did not find any articles that match with your search")));                    
                    alert.showAndWait();

      
                  }
                  else
                  {
                    
                    FileWriter fileWriter = new FileWriter(historyFile,true);
                    PrintWriter writer = new PrintWriter(fileWriter);
                    

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy~HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    writer.print(lt.getKeyWord()+"~"+dtf.format(now) + '\n');
                    

                     writer.close();
                    fileWriter.close();
                    results rs = new results();   //results for the first 
                  
                    rs.start(stage,fileName,hitRate,lt.getKeyWord(),lt.getField());
                  }
                  
                }
                else if(TypeOfSearch == "D")
                {
                  fileName.clear();
                  hitRate.clear();
                  
                  lt.PhraseSearch(searchField.getText());

                  hitRate.addAll(lt.getHits());
                  fileName.addAll(lt.getFiles());
                  

                  if(hitRate.size() == 0)
                  {
                    Alert alert = new Alert(AlertType.WARNING);

                    
                    alert.setTitle("Search Failed");
                    alert.setHeaderText("No results found");

                    
                    alert.initModality(Modality.WINDOW_MODAL);

                    
                    alert.initOwner(stage);

                    alert.getDialogPane().setExpandableContent(new StackPane(new Label("We did not find any articles that match with your search")));                    
                    alert.showAndWait();

      
                  }
                  else
                  {
                    FileWriter fileWriter = new FileWriter(historyFile,true);
                    PrintWriter writer = new PrintWriter(fileWriter);
                    

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy~HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    writer.print(lt.getKeyWord()+"~"+dtf.format(now) + '\n');
                    

                     writer.close();
                    fileWriter.close();
                    phraseResults prs = new phraseResults();
                    prs.start(stage,fileName,hitRate,lt.getKeyWord());
                  }
                  
                }
              }
            } 
            catch (IOException e) 				
            {
                e.printStackTrace();
            } 
            catch (ParseException e) 			
            {
              e.printStackTrace();
            }
        });

        //submit button event handler
        submit.setOnAction((event) -> {
          try 
          {
            if(searchField.getLength() > 0  && searchField2.getLength()>0)
            {
              String TypeOfSearch = group.getSelectedToggle().getUserData().toString();
              if(TypeOfSearch == "B")
              {
                String field1 = group2.getSelectedToggle().getUserData().toString();
                String field2 = group4.getSelectedToggle().getUserData().toString();
              

                String op1 = group3.getSelectedToggle().getUserData().toString();
                String op2 = group5.getSelectedToggle().getUserData().toString();

                fileName.clear();
                hitRate.clear();

                lt.search(searchField.getText(),searchField2.getText(),field1,field2,op1,op2);

                  hitRate.addAll(lt.getHits());
                  fileName.addAll(lt.getFiles());
                 
                  
                  if(hitRate.size() == 0)
                  {
                    Alert alert = new Alert(AlertType.WARNING);

                    
                    alert.setTitle("Search Failed");
                    alert.setHeaderText("No results found");

                    
                    alert.initModality(Modality.WINDOW_MODAL);

                    
                    alert.initOwner(stage);

                    alert.getDialogPane().setExpandableContent(new StackPane(new Label("We did not find any articles that match with your search")));                    
                    alert.showAndWait();

      
                  }
                  else
                  {
                    FileWriter fileWriter = new FileWriter(historyFile,true);
                    PrintWriter writer = new PrintWriter(fileWriter);
                    

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy~HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    writer.print(lt.getKeyWord()+ "~" + lt.getKeyWord2() + "~"+dtf.format(now) + '\n');
                    

                     writer.close();
                    fileWriter.close();
                    booleanResults brs = new booleanResults();
                    brs.start(stage, fileName, hitRate, lt.getKeyWord(), lt.getKeyWord2(), lt.getField(), lt.getField2());
                  }
              }
            }
          } 
          catch (IOException e) 				
          {
              e.printStackTrace();
          } 
          catch (ParseException e) 			
          {
            e.printStackTrace();
          }
  
        });

        historyButton.setOnAction((event) -> {
            history h = new history();
            try {
              h.start(stage);
            } catch (IOException e1) {
              e1.printStackTrace();
            }

          });
        searchButton.setOnAction((event) -> {
          
         //System.out.println(lv2.getSelectionModel().getSelectedItem());
         try {

          fileName.clear();
          hitRate.clear();

          lt.FileCompare(lv2.getSelectionModel().getSelectedItem());

          hitRate.addAll(lt.getHits());
          fileName.addAll(lt.getFiles());

          if(hitRate.size() == 0)
          {
            Alert alert = new Alert(AlertType.WARNING);

            
            alert.setTitle("Search Failed");
            alert.setHeaderText("No results found");

            
            alert.initModality(Modality.WINDOW_MODAL);

            
            alert.initOwner(stage);

            alert.getDialogPane().setExpandableContent(new StackPane(new Label("We did not find any articles that match with your search")));                    
            alert.showAndWait();


          }
          else
          {
          
            textResults trs = new textResults();
            trs.start(stage, fileName, hitRate);
          } 


        } catch (IOException | ParseException e1) {
          e1.printStackTrace();
        }
        });
        
        //if user decides to leave, we delete the files inside the folder "Index"
        stage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, (e) -> {
          
          try 
          {
            lt.DeleteIndexFiles();
          } 
          catch (IOException e2) 				
          {
              e2.printStackTrace();
          } 
          catch (ParseException e2) 			
          {
            e2.printStackTrace();
          }
        });
                
          
        FlowPane fp = new FlowPane();
        fp.setPadding(new Insets(5));
        fp.getChildren().addAll(insertButton,lv,deleteButton,lv2,searchButton);
        fp.setHgap(20);
        
      
        VBox vb = new VBox();
        vb.getChildren().addAll(searchField,searchField2,tp,tp2,tp3,submit,fp);
        vb.setPadding(new Insets(10));
        vb.setSpacing(7);
        vb.setStyle("-fx-background-color: BEIGE;");
        
        

        Scene scene = new Scene(vb, 1000, 700);
        stage.setScene(scene);
        stage.setMinHeight(750);
        stage.setMinWidth(1000);
        stage.setTitle("TReSA Search Machine");
        stage.getIcons().add(new Image("file:src/main/java/jfx/images/logo.jfif"));
        stage.show();
  }

  
}
