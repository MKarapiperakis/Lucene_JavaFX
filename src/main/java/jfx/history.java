package jfx;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class history {
    public void start(Stage stage) throws IOException {

        ObservableList<String> obv1 = FXCollections.observableArrayList();
        
        File file = new File("history.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while((line = br.readLine())!=null)
        {
            obv1.add(line);
        }
        br.close();
        ListView lv = new ListView<>();
        lv.setStyle("-fx-font-size:20.0");
        lv.setItems(obv1);

        Button clear = new Button("clear");
        clear.setPrefSize(90, 70);
        clear.setStyle("-fx-border-radius: 20px;-fx-background-radius: 20 20 20 20;-fx-font-size:15px;");

        Button delete = new Button("delete");
        delete.setPrefSize(90, 70);
        delete.setStyle("-fx-border-radius: 20px;-fx-background-radius: 20 20 20 20;-fx-font-size:15px;");
        delete.setDisable(true);


        VBox vb = new VBox();
        vb.setPrefSize(600, 400);
        vb.getChildren().addAll(lv,clear,delete);
        vb.setSpacing(10);



        //event handlers
        clear.setOnAction((event) -> {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.print("");
                writer.close();
                obv1.clear();
                lv.setItems(obv1);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        delete.setOnAction((event) -> {
            
            
            String currentLine = "";
            File inputFile = new File("history.txt");
            File tempFile = new File("temptest.txt");
            String remove = lv.getSelectionModel().getSelectedItem().toString();
            System.out.print(remove);
            BufferedReader reader;

            try {
                reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer;
                try {
                    writer = new BufferedWriter(new FileWriter(tempFile));
                    while((currentLine = reader.readLine()) != null)
                    {
                        String trimmedLine = currentLine.trim();
                        if(trimmedLine.equals(remove)) continue;
                        writer.write(currentLine + System.getProperty("line.separator"));
                    }
                    writer.close();
                    reader.close();


                    Path source = Paths.get("temptest.txt");
                     File del = new File("history.txt");

                     del.delete();

                     Files.move(source, source.resolveSibling("history.txt"), StandardCopyOption.REPLACE_EXISTING);
                     obv1.remove(lv.getSelectionModel().getSelectedIndex());
                     if(obv1.size() == 0)
                        delete.setDisable(true);
                    else
                        delete.setDisable(false);
                    
                } catch (IOException e) {
                   
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                
                e.printStackTrace();
            }
        });

        lv.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if(lv.getSelectionModel().getSelectedIndex() == -1)
                    delete.setDisable(true);
                else
                    delete.setDisable(false);                
            }

           
            
        });

        Dialog<Integer> dialog = new Dialog<>();
        dialog.initOwner(stage);
        dialog.setTitle("Search History" );
        dialog.getDialogPane().setContent(vb);
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
