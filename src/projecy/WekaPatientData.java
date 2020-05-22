package projecy;

import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DateType;

public class WekaPatientData
{
    private Boolean sex = null;
    private float bmi = -1;
    private float bloodpressure = -1;
    private DateType birthdate = null;
    private DateTimeType testdate = null;
    private Boolean smoking = null;
    private float cholesterol = -1;

    public void setBloodpressure(float bloodpressure)
    {
        this.bloodpressure = bloodpressure;
    }

    public void setBmi(float bmi)
    {
        this.bmi = bmi;
    }

    public void setCholesterol(float cholesterol)
    {
        this.cholesterol = cholesterol;
    }

    public void setSex(Boolean sex)
    {
        this.sex = sex;
    }

    public void setSmoking(Boolean smoking)
    {
        this.smoking = smoking;
    }

    public Boolean getSex()
    {
        return sex;
    }

    public Boolean getSmoking()
    {
        return smoking;
    }

    public float getBloodpressure()
    {
        return bloodpressure;
    }

    public float getBmi()
    {
        return bmi;
    }

    public float getCholesterol()
    {
        return cholesterol;
    }

    public Boolean getHighRisk()
            // https://www.betterhealth.vic.gov.au/health/conditionsandtreatments/cholesterol#lp-h-2 states 5.5 mmol/L or 212.7 mg/dL to be high
    {
        if (cholesterol == -1) return null;
        if (cholesterol >= 212.7)  return true;
        else return false;
    }


    public DateType getBirthdate()
    {
        return birthdate;
    }

    public int getAge()
    {
        int difference = testdate.getYear() - birthdate.getYear();
        return difference;

    }

    public void setTestdate(DateTimeType testdate)
    {
        this.testdate = testdate;
    }

    public void setBirthdate(DateType birthdate)
    {
        this.birthdate = birthdate;
    }

    public DateTimeType getTestdate()
    {
        return testdate;
    }

    public boolean full()
    {
        if (bmi != -1 && bloodpressure != -1 && smoking != null && cholesterol != -1)
        {
            return true;
        }
        return false;
    }

    /**
     * Generates an ARFF compliant string
     * @return data as string
     */
    public String getData()
    {
        String data = "";
        if (this.getSex()) data += "0,"; else data +="1,";
        data += getAge() + ",";
        if (this.getBmi() == -1) data += "?,";else data += getBmi() + ",";
        if (this.getBloodpressure() == -1) data += "?,";else data += getBloodpressure() + ",";
        if (this.getSmoking() == null) data += "?,";
        else if (this.getSmoking()) data +="1,"; else data += "0,";
        if (this.getHighRisk()) data += "1"; else data += "0";
        data += "\n";
        return data;
    }
}
