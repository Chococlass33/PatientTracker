package projecy;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {
        Requests requests = new Requests("https://fhir.monash.edu/hapi-fhir-jpaserver/fhir");
        MonitoredPatients monitoredPatients = new MonitoredPatients(requests);
        PractitionerPatientList practitionerPatients = new PractitionerPatientList(requests);
        primaryStage.setTitle("Projecty");
        VBox monitorView = new MainMonitorView(monitoredPatients);
        VBox patientListView = new AddPatientsTableView(practitionerPatients, monitoredPatients);
        Scene primaryScene = new Scene(new HBox(patientListView, monitorView));
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });
        primaryStage.setScene(primaryScene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
