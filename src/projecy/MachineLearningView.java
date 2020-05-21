package projecy;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class MachineLearningView extends Region
{

    GridPane grid = new GridPane();
    MachineLearning ml;
    public MachineLearningView(GetMeka request)
    {
        ml = new MachineLearning(request);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(10,10,10,10));
        Label filelabel = new Label("File Name:");
        grid.add(filelabel, 0, 1);
        TextField fileTextField = new TextField();
        fileTextField.setText("data.arff");
        grid.add(fileTextField, 1, 1);
        Label bundlelabel = new Label("Bundles to Grab (default 30000):");
        grid.add(bundlelabel, 0, 2);
        TextField bundleTextField = new TextField();
        bundleTextField.setText("30000");
        grid.add(bundleTextField, 1, 2);
        Button grab = new Button("Grab Data");
        grab.setOnAction((action) ->
        {
            try
            {
                Task<Integer> task = new Task<Integer>()
                {
                    @Override
                    protected Integer call() throws Exception
                    {
                        Platform.runLater(() -> {
                            grab.setText("Working...");
                        });
                        ml.grabData(fileTextField.getText(), Integer.parseInt(bundleTextField.getText()));
                        Platform.runLater(() -> {
                            grab.setText("Grab Data");
                        });
                        return 1;
                    }
                };
                Thread thread = new Thread(task);
                thread.setDaemon(true);
                thread.start();
            }
            catch (Exception e)
            {
                System.out.println("An Error Occurred");
                grab.setText("error");
            }
        });
        grid.add(grab, 0, 3);
        Button analyse = new Button("Analyse Data");
        analyse.setOnAction((action) ->
        {
            try
            {
                ml.testData(fileTextField.getText());
            }
            catch (Exception e)
            {
                System.out.println("An Error Occurred");
            }
        });
        grid.add(analyse, 1, 3);
        HBox hBox = new HBox(grid);
        this.getChildren().add(hBox);
    }

}
