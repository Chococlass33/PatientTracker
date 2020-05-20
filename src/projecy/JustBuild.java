package projecy;

import meka.classifiers.multilabel.BR;
import meka.core.MLUtils;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;


// Code found on https://github.com/Waikato/meka/blob/master/src/main/java/mekaexamples/classifiers/JustBuild.java

public class JustBuild {

    public static void main(String[] args) throws Exception {
        if (args.length != 1)
            throw new IllegalArgumentException("Required arguments: <dataset>");

        System.out.println("Loading data: " + args[0]);
        Instances data = ConverterUtils.DataSource.read(args[0]);
        MLUtils.prepareData(data);

        System.out.println("Build BR classifier");
        BR classifier = new BR();
        // further configuration of classifier
        classifier.buildClassifier(data);
    }
}