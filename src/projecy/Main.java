package projecy;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {
        //Initialize data and request classes
        Requests baseRequests = new Requests("https://fhir.monash.edu/hapi-fhir-jpaserver/fhir");
        PatientList practitionerPatients = new PatientList(baseRequests);
        MonitoredPatientList monitoredPatients = new MonitoredPatientList(baseRequests);
        //Initialize view classes
        Region monitorView = new MonitorPatientsTableView(monitoredPatients);
        MachineLearning machineLearning = new MachineLearning(baseRequests);
        Region machineview = new MachineLearningView(baseRequests);
        Region patientListView = new AddPatientsTableView(practitionerPatients, monitoredPatients);
        //Add view classes to scene
        Scene primaryScene = new Scene(new HBox(machineview,patientListView, monitorView));
        //Shut down application properly upon close of window
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });
        //Organise primary stage and show
        primaryStage.setTitle("Projecy");
        primaryStage.setScene(primaryScene);
        primaryStage.show();

    }

    public static void Main(String[] args) {
        Application.launch(args);
    }
}
