package projecy;

import org.hl7.fhir.r4.model.*;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.evaluation.Evaluation;
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
    private GetMeka request;
    public MachineLearning(GetMeka request)
    {
        this.request = request;
    }

    public void testData(String test) throws Exception
    {
        String[] bnoptions = weka.core.Utils.splitOptions("-D -Q weka.classifiers.bayes.net.search.local.K2 -- -P 1 -S BAYES -E weka.classifiers.bayes.net.estimate.SimpleEstimator -- -A 0.5");
        String[] smoteoptions = weka.core.Utils.splitOptions("-C 0 -K 5 -P 6600.0 -S 1");
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(test);
        Instances train = source.getDataSet();
        if (train.classIndex() == -1)
            train.setClassIndex(train.numAttributes() - 1);
        SMOTE smote = new SMOTE();
        smote.setOptions(smoteoptions);
        smote.setInputFormat(train);
        Instances upsampledtrain = SMOTE.useFilter(train, smote);
        BayesNet bn = new BayesNet();
        bn.setOptions(bnoptions);
        Evaluation eval = new Evaluation(upsampledtrain);
        eval.crossValidateModel(bn,upsampledtrain,10,new Random());

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
            FileWriter datawriter = new FileWriter("results.txt");
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
    public void grabData(String filename, int bundleCount)
    {
        Hashtable<String, mekaPatientData> patientDictionary = new Hashtable<>();
        List<List<Bundle>> patientdata = request.getAllOfObservation(bundleCount);
        List<Bundle.BundleEntryComponent> cholesterolbundles = new ArrayList<>();
        //Go through each cholesterol reading and assign to the dictionary where able.
        patientdata.get(0).forEach((bundle) ->
        {
            cholesterolbundles.addAll(bundle.getEntry());
        });

        for (Bundle.BundleEntryComponent entry : cholesterolbundles)
        {
            String patient = entry.getResource().getNamedProperty("subject").getValues().get(0).getNamedProperty("reference").getValues().get(0).toString();
            Base cholesterolquantity = entry.getResource().getNamedProperty("valueQuantity").getValues().get(0);
            float cholesterol = cholesterolquantity.castToQuantity(cholesterolquantity).getValue().floatValue();
            if (patientDictionary.get(patient) == null)
            {
                mekaPatientData newdata = new mekaPatientData();
                newdata.setCholesterol(cholesterol);
                Base testdatebase = entry.getResource().getNamedProperty("effectiveDateTime").getValues().get(0);
                DateTimeType testdate = testdatebase.castToDateTime(testdatebase);
                newdata.setTestdate(testdate);
                patientDictionary.put(patient, newdata);
            }
        }
        List<Bundle.BundleEntryComponent> bloodpressurebundles = new ArrayList<>();
        //Go through each cholesterol reading and assign to the dictionary where able.
        patientdata.get(1).forEach((bundle) ->
        {
            bloodpressurebundles.addAll(bundle.getEntry());
        });
        for (Bundle.BundleEntryComponent entry : bloodpressurebundles)
        {
            String patient = entry.getResource().getNamedProperty("subject").getValues().get(0).getNamedProperty("reference").getValues().get(0).toString();
            Base bloodpressurebase = entry.getResource().getNamedProperty("component").getValues().get(0).getNamedProperty("valueQuantity").getValues().get(0);
            float bloodpressure = bloodpressurebase.castToQuantity(bloodpressurebase).getValue().floatValue();
            if (patientDictionary.get(patient) != null)
            {
                patientDictionary.get(patient).setBloodpressure(bloodpressure);
            }
        }
        List<Bundle.BundleEntryComponent> bmibundles = new ArrayList<>();
        //Go through each cholesterol reading and assign to the dictionary where able.
        patientdata.get(2).forEach((bundle) ->
        {
            bmibundles.addAll(bundle.getEntry());
        });
        for (Bundle.BundleEntryComponent entry : bmibundles)
        {
            String patient = entry.getResource().getNamedProperty("subject").getValues().get(0).getNamedProperty("reference").getValues().get(0).toString();
            Base bmibase = entry.getResource().getNamedProperty("valueQuantity").getValues().get(0);
            float bmi = bmibase.castToQuantity(bmibase).getValue().floatValue();
            if (patientDictionary.get(patient) != null)
            {
                patientDictionary.get(patient).setBmi(bmi);
            }
        }
        List<Bundle.BundleEntryComponent> smokingbundles = new ArrayList<>();
        //Go through each cholesterol reading and assign to the dictionary where able.
        patientdata.get(3).forEach((bundle) ->
        {
            smokingbundles.addAll(bundle.getEntry());
        });
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
                patientDictionary.get(patient).setSmoking(smoked);
            }
        }

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
            FileWriter datawriter = new FileWriter(filename);
            datawriter.write("@relation 'Cholesterol’\n" +
                    "@attribute gender {0,1}\n" +
                    "@attribute age numeric\n" +
                    "@attribute bmi numeric\n" +
                    "@attribute bloodpressure numeric\n" +
                    "@attribute smoking {0,1}\n" +
                    "@attribute highcholesterol {0,1}\n" +
                    "\n" +
                    "@data\n");
            patientDictionary.forEach((k,v) ->
            {
                Patient patient = request.getPatientMeka(k);
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
