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

    /**
     * Creates a preset region with MachineLearning functionality built in, with inputs for file name and bundle count,
     * and access to all 3 functions
     * @param request the GetWeka request MachineLearning requires
     */
    public MachineLearningView(GetWeka request)
    {
        //Generate a MachineLearning
        ml = new MachineLearning(request);

        //Formatting the grid
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(10,10,10,10));

        //Adding labels and textboxes
        Label filelabel = new Label("File Name:");
        grid.add(filelabel, 0, 1);
        TextField fileTextField = new TextField();
        fileTextField.setText("data.arff");
        grid.add(fileTextField, 1, 1);
        Label bundlelabel = new Label("Bundles to Grab (default 60000):");
        grid.add(bundlelabel, 0, 2);
        TextField bundleTextField = new TextField();
        bundleTextField.setText("60000");
        grid.add(bundleTextField, 1, 2);

        //Add button to grab data in seperate thread
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
                e.printStackTrace();
                System.out.println("An Error Occurred");
                grab.setText("error");
            }
        });
        grid.add(grab, 0, 3);

        //Add button to train model in seperate thread
        Button train = new Button("Train Data");
        train.setOnAction((action) ->
        {   try
        {
            Task<Integer> task = new Task<Integer>()
            {
                @Override
                protected Integer call() throws Exception
                {
                    Platform.runLater(() -> {
                        train.setText("Working...");
                    });
                    ml.TrainData(fileTextField.getText());
                    Platform.runLater(() -> {
                        train.setText("Train Data");
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
                e.printStackTrace();
                System.out.println("An Error Occurred");
            }
        });
        grid.add(train, 1, 3);

        // Add button to predict data in seperate thread
        Button predict = new Button("Predict Data");
        predict.setOnAction((action) ->
        {
            try
            {
                Task<Integer> task = new Task<Integer>()
                {
                    @Override
                    protected Integer call() throws Exception
                    {
                        Platform.runLater(() -> {
                            predict.setText("Working...");
                        });
                        ml.predictData(fileTextField.getText());
                        Platform.runLater(() -> {
                            predict.setText("Predict Data");
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
                e.printStackTrace();
                System.out.println("An Error Occurred");
                grab.setText("error");
            }
        });
        grid.add(predict, 2, 3);
        HBox hBox = new HBox(grid);
        this.getChildren().add(hBox);
    }

}
