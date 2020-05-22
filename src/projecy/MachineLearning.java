package projecy;

import org.hl7.fhir.r4.model.*;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.supervised.instance.SMOTE;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;


public class MachineLearning
{
    GetWeka request;
    RandomForest bn;

    /**
     * Generates a MachineLearning object, which holds a classifier and a GetWeka to request data from. Has functions to
     * grab data, train data and predict data.
     * @param request The GetWeka Request file to grab from.
     */
    public MachineLearning(GetWeka request)
    {
        this.request = request;
    }


    /**
     * Takes a filename to an cholesterol ARFF file with unknown cholesterol, and stores the predictions in prediction.arff
     * @param text
     * @throws Exception
     */
    public void predictData(String text) throws Exception
    {
        //Grabs the file as a source to use
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(text);
        //Generates the Instances of data from file to predict
        Instances test = source.getDataSet();
        //Set the class
        if (test.classIndex() == -1)
            test.setClassIndex(test.numAttributes() - 1);
        //Generate a clone to manipulate
        Instances prediction = new Instances(test);
        //For each instance of data, put the data through the classifier, then save it to prediction
        for (int i = 0; i < prediction.numInstances(); i++)
        {
            double Label = this.bn.classifyInstance(test.instance(i));
            prediction.instance(i).setClassValue(Label);
            System.out.println(prediction.instance(i));
        }

        //Writing prediction to file
        try
        {
            File datafile = new File("prediction.arff");
            if(datafile.createNewFile())
            {
            }
            else
            {
                datafile.delete();
                datafile.createNewFile();
            }
            FileWriter datawriter = new FileWriter("prediction.arff",false);
            datawriter.write(prediction.toString());
            datawriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


    }

    /**
     * Takes a filename to test out, prepares the data then uses the set classifier to generate a model.
     * @param test Filename of ARFF file to open
     * @throws Exception
     */
    public void TrainData(String test) throws Exception
    {
        // Set up preset configurations for the classifier and the filter used for upsampling
        String[] bnoptions = weka.core.Utils.splitOptions("-K 0 -M 1.0 -V 0.001 -S 1" );
        String[] smoteoptions = weka.core.Utils.splitOptions("-C 0 -K 5 -P 6650.0 -S 1");

        // Grab the data and create instances
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(test);
        Instances data = source.getDataSet();
        if (data.classIndex() == -1)
            data.setClassIndex(data.numAttributes() - 1);

        // Randomize the data
        data.randomize(new Random(1));

        // Generate SMOTE filter used for upscaling
        SMOTE smote = new SMOTE();
        smote.setOptions(smoteoptions);
        smote.setInputFormat(data);

        // Use SMOTE to upsample data
        Instances upsampleddata = SMOTE.useFilter(data, smote);

        // Randomize upsampled data
        upsampleddata.randomize(new Random(1));

        // Generate the classifier and feed it the upsampled data for training
        this.bn = new RandomForest();
        this.bn.setOptions(bnoptions);
        this.bn.buildClassifier(upsampleddata);

        // Evaluate the model using the original data
        Evaluation eval = new Evaluation(data);
        eval.evaluateModel(this.bn,data);

        // Store evaluation to file
        try
        {
            File datafile = new File("results.txt");
            if(datafile.createNewFile())
            {
            }
            else
            {
                datafile.delete();
                datafile.createNewFile();
            }
            FileWriter datawriter = new FileWriter("results.txt",false);
            datawriter.write(eval.toSummaryString());
            datawriter.write(eval.toClassDetailsString());

            datawriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.print(eval.toSummaryString());
        System.out.print(eval.toClassDetailsString());



    }

    /**
     * Uses Requests to grab bundleCount number of bundles for each observation, generates a dictionary of patients in
     * WekaPatientData format, then searches each patients details to complete the data. Stores the data to the filename
     * as an ARFF file.
     *
     * @param filename Filename to write to
     * @param bundleCount Bundles to get, should be more than 200 and a multiple of 200
     */
    public void grabData(String filename, int bundleCount)
    {
        // Generate a dictionary to store WekaPatientData
        Hashtable<String, WekaPatientData> patientDictionary = new Hashtable<>();

        //Request for all observations
        List<List<Bundle>> patientdata = request.getAllOfObservation(bundleCount);

        //Unpack each seperate cholesterol bundle into this new list
        List<Bundle.BundleEntryComponent> cholesterolbundles = new ArrayList<>();
        patientdata.get(0).forEach((bundle) ->
        {
            cholesterolbundles.addAll(bundle.getEntry());
        });

        //Go through each cholesterol reading and assign to the dictionary where able.
        for (Bundle.BundleEntryComponent entry : cholesterolbundles)
        {
            // Generate key of Patient ID
            String patient = entry.getResource().getNamedProperty("subject").getValues().get(0).getNamedProperty("reference").getValues().get(0).toString();

            // Get cholesterol data
            Base cholesterolquantity = entry.getResource().getNamedProperty("valueQuantity").getValues().get(0);
            float cholesterol = cholesterolquantity.castToQuantity(cholesterolquantity).getValue().floatValue();

            // If there is no entry in dictionary add one
            if (patientDictionary.get(patient) == null)
            {
                WekaPatientData newdata = new WekaPatientData();
                newdata.setCholesterol(cholesterol);
                Base testdatebase = entry.getResource().getNamedProperty("effectiveDateTime").getValues().get(0);
                DateTimeType testdate = testdatebase.castToDateTime(testdatebase);
                newdata.setTestdate(testdate);
                patientDictionary.put(patient, newdata);
            }
        }

        // Unpack all blood pressure bundles
        List<Bundle.BundleEntryComponent> bloodpressurebundles = new ArrayList<>();
        patientdata.get(1).forEach((bundle) ->
        {
            bloodpressurebundles.addAll(bundle.getEntry());
        });

        // For each observation, check if the patient exists in dictionary with no prior bloodpressure set, then set it
        for (Bundle.BundleEntryComponent entry : bloodpressurebundles)
        {
            String patient = entry.getResource().getNamedProperty("subject").getValues().get(0).getNamedProperty("reference").getValues().get(0).toString();
            Base bloodpressurebase = entry.getResource().getNamedProperty("component").getValues().get(0).getNamedProperty("valueQuantity").getValues().get(0);
            float bloodpressure = bloodpressurebase.castToQuantity(bloodpressurebase).getValue().floatValue();
            if (patientDictionary.get(patient) != null)
            {
                if(patientDictionary.get(patient).getBloodpressure() == -1)
                {
                    patientDictionary.get(patient).setBloodpressure(bloodpressure);
                }
            }
        }

        // Unpack all bmi bundles
        List<Bundle.BundleEntryComponent> bmibundles = new ArrayList<>();
        patientdata.get(2).forEach((bundle) ->
        {
            bmibundles.addAll(bundle.getEntry());
        });

        //For each observation, check if the patient exists in dictionary with no prior bmi set, then set it
        for (Bundle.BundleEntryComponent entry : bmibundles)
        {
            String patient = entry.getResource().getNamedProperty("subject").getValues().get(0).getNamedProperty("reference").getValues().get(0).toString();
            Base bmibase = entry.getResource().getNamedProperty("valueQuantity").getValues().get(0);
            float bmi = bmibase.castToQuantity(bmibase).getValue().floatValue();
            if (patientDictionary.get(patient) != null)
            {
                if (patientDictionary.get(patient).getBmi() != -1)
                {
                    patientDictionary.get(patient).setBmi(bmi);
                }
                patientDictionary.get(patient).setBmi(bmi);
            }
        }
        List<Bundle.BundleEntryComponent> smokingbundles = new ArrayList<>();
        //Go through each cholesterol reading and assign to the dictionary where able.
        patientdata.get(3).forEach((bundle) ->
        {
            smokingbundles.addAll(bundle.getEntry());
        });

        //For each observation, check if the patient exists in dictionary with no prior bloodpressure set, then set it
        for (Bundle.BundleEntryComponent entry : smokingbundles)
        {
            String patient = entry.getResource().getNamedProperty("subject").getValues().get(0).getNamedProperty("reference").getValues().get(0).toString();
            Base smokingbase = entry.getResource().getNamedProperty("valueCodeableConcept").getValues().get(0).getNamedProperty("text").getValues().get(0);
            String smoking = smokingbase.toString();
            Boolean smoked = true;
            if (smoking.contains("Never smoker"))
            {
                smoked = false;
            }
            if (patientDictionary.get(patient) != null)
            {

                if (patientDictionary.get(patient).getSmoking() == null)
                {
                    patientDictionary.get(patient).setSmoking(smoked);
                }
            }
        }
        // Print data to ARFF formatted file
        try
        {
            File datafile = new File(filename);
            if(datafile.createNewFile())
            {
            }
            else
            {
                datafile.delete();
                datafile.createNewFile();
            }
            FileWriter datawriter = new FileWriter(filename,false);

            // Write the ARFF header
            datawriter.write("@relation 'Cholesterol’\n" +
                    "@attribute gender {0,1}\n" +
                    "@attribute age numeric\n" +
                    "@attribute bmi numeric\n" +
                    "@attribute bloodpressure numeric\n" +
                    "@attribute smoking {0,1}\n" +
                    "@attribute highcholesterol {0,1}\n" +
                    "\n" +
                    "@data\n");

            // Grab extra patient details, then writes to file
            patientDictionary.forEach((k,v) ->
            {
                Patient patient = request.getPatientWeka(k);
                String patientgender = patient.getGender().toString();
                DateType patientbirth = patient.getBirthDateElement();
                v.setSex(patientgender == "FEMALE");
                v.setBirthdate(patientbirth);
                try
                {
                    datawriter.write(v.getData());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            });

            datawriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
