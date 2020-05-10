package projecy;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {
        Requests requests = new Requests("https://fhir.monash.edu/hapi-fhir-jpaserver/fhir");
        MonitoredPatients monitoredPatients = new MonitoredPatients(requests);
        primaryStage.setTitle("Projecty");
        VBox patientView = new PatientTableView(monitoredPatients);
        Scene primaryScene = new Scene(patientView);
        primaryStage.setScene(primaryScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
