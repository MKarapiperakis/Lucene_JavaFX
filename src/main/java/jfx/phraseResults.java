package jfx;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class phraseResults {
    
    public void start(Stage stage,ObservableList fileName, ObservableList hitRate, String keyWord) throws IOException {

        ObservableList<String> obv = FXCollections.observableArrayList();
        ObservableList<String> obv2 = FXCollections.observableArrayList();
        
        ListView lv1 = new ListView<>();
        ListView lv2 = new ListView<>();

        lv1.setItems(fileName);
        lv2.setItems(hitRate);

        Label label1 = new Label("FileNames");
        Label label2 = new Label("Score");
        Label label3 = new Label("File content");
        Label label4 = new Label("Keyword");
        label1.setPadding(new Insets(0, 0, 0, 2));
        label2.setPadding(new Insets(0, 0, 0, 2));
        label3.setPadding(new Insets(0, 0, 0, 2));
        label4.setPadding(new Insets(0, 0, 0, 2));

        label1.setFont(Font.font("Arial", 25));
        label2.setFont(Font.font("Arial", 25));
        label3.setFont(Font.font("Arial", 25));
        label4.setFont(Font.font("Arial", 25));

        TextArea ta = new TextArea();
        ta.setEditable(false);
        ta.setFont(Font.font("Arial", 15));
        ta.setWrapText(true);
        ta.setPrefSize(350, 400);

        TextArea ta2 = new TextArea();
        ta2.setEditable(false);
        ta2.setFont(Font.font("Arial", 15));
        ta2.setWrapText(true);
        ta2.setPrefSize(350, 400);

        HBox hb = new HBox();
        VBox vb1 = new VBox(label1,lv1);
        VBox vb2 = new VBox(label2,lv2);
        VBox vb3 = new VBox(label3,ta);
        VBox vb4 = new VBox(label4,ta2);

        ComboBox<Integer> cb = new ComboBox<>();
        for(int i=1;i<=hitRate.size();i++)
        {
            cb.getItems().add(i);
        }
        
        cb.getSelectionModel().select(hitRate.size()-1);
        cb.setEditable(false);
        cb.setPrefSize(80, 30);
        cb.setMinSize(80, 30);
        cb.setStyle("-fx-border-radius: 20px;-fx-background-radius: 20 20 20 20;-fx-font-size:15px;");
        
        hb.getChildren().addAll(vb1,vb2,vb3,vb4,cb);
        hb.setPrefSize(1200, 600);

        cb.setOnAction((event) -> {  
            obv.clear();
            obv2.clear();
            
            if(cb.getSelectionModel().getSelectedIndex() <= hitRate.size()+1)
            {
                ta2.clear();
                ta.clear();
                int limit = cb.getSelectionModel().getSelectedIndex();
                for(int i=0;i<=limit;i++)
                {
                    obv.add((String) fileName.get(i));
                    obv2.add((String) hitRate.get(i));
                }
                    lv1.setItems(obv);
                    lv2.setItems(obv2);
                ta2.setText(keyWord);
            }
        });






        ta2.setText(keyWord);




        lv1.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                //System.out.println(lv1.getSelectionModel().getSelectedItem().toString());
               
                lv2.scrollTo(lv1.getSelectionModel().getSelectedIndex());
                lv1.scrollTo(lv1.getSelectionModel().getSelectedIndex());
                lv2.getSelectionModel().select(lv1.getSelectionModel().getSelectedIndex());
                File file = new File("Data\\" + lv1.getSelectionModel().getSelectedItem().toString());

                try (FileReader fileReader = new FileReader(file)) {
                    int character;
                    String word = "";
                    String Places = "";
                    String People = "";
                    String Title = "";
                    String Body = "";
                    try {
                        while((character = fileReader.read()) != -1) {
                            
                            word = word + (char)character;
                            if(word.contains("</PLACES>"))
                            {
                                word = word.replaceAll("</PLACES>","");
                                word = word.replaceAll("<PLACES>","");
                                Places = word;
                                word = "";
                            }
                            
                            if(word.contains("</PEOPLE>"))
                            {
                                word = word.replaceAll("</PEOPLE>","");
                                word = word.replaceAll("<PEOPLE>","");
                                People = word;
                                word = "";
                            }
                            
                            if(word.contains("</TITLE>"))
                            {
                                word = word.replaceAll("</TITLE>","");
                                word = word.replaceAll("<TITLE>","");
                                Title = word;
                                word = "";
                            }
                            
                            if(word.contains("</BODY>"))
                            {
                                word = word.replaceAll("</BODY>","");
                                word = word.replaceAll("<BODY>","");
                                Body = word;
                                word = "";
                            }
                        }
                        People = People.replace("\n", "").replace("\r", "");	
			            Title = Title.replace("\n", "").replace("\r", "");	
                        Body = Body.replace("\n", "").replace("\r", "");
                        ta.setText("Places: " + Places + "\n" + "People: " + People + "\n" + "Title: " + Title + "\n" + "Body: " + Body + "(" + file.getName() + ")" );
                        
                       
                        fileReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

               
            }
            
        });



        Dialog<Integer> dialog = new Dialog<>();
        dialog.initOwner(stage);
        dialog.setTitle("Results" );
        dialog.getDialogPane().setContent(hb);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        dialog.setResultConverter((button) -> {
        
        if (button == ButtonType.OK) {
            
            return 1;
        }
        else
            return 0;
            
        });

        Optional<Integer> result = dialog.showAndWait();
        
    
    }
}
