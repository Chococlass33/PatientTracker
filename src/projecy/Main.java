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
        GetPatientsCholesterol patientGetter = new Requests("https://fhir.monash.edu/hapi-fhir-jpaserver/fhir");
        PatientList practitionerPatients = new PatientList(patientGetter);
        MonitoredPatientList monitoredPatients = new MonitoredPatientList(patientGetter);
        //Initialize view classes
        Region monitorView = new MonitorPatientsTableView(monitoredPatients);
        Region patientListView = new AddPatientsTableView(practitionerPatients, monitoredPatients);
        //Add view classes to scene
        Scene primaryScene = new Scene(new HBox(patientListView, monitorView));
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

    public static void main(String[] args) {
        Application.launch(args);
    }
}
