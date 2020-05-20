package projecy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class mekaPatientData
{
    private String name = "";
    private Boolean sex = null;
    private float bmi = -1;
    private float bloodpressure = -1;
    private Date birthdate = null;
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

    public void setName(String name)
    {
        this.name = name;
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
            // https://www.medicalnewstoday.com/articles/315900#recommended-levels states 240 mg/dL to be high for adults, 200 high for children.
    {
        if (cholesterol == -1) return null;
        if (this.getAge() > 18)
        {
            if (cholesterol >= 240)  return true;
            else return false;
        }
        else
        {

            if (cholesterol >= 200)  return true;
            else return false;
        }
    }

    public void setBirthdate(String date) throws ParseException
    {
        this.birthdate = new SimpleDateFormat("yyyy/MM/dd").parse(date);
    }

    public Date getBirthdate()
    {
        return birthdate;
    }

    public int getAge()
    {
        Date currentday = new Date();

        long difference = currentday.getTime() - birthdate.getTime();
        long days = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
        int years = (int)(days/365.25);
        return years;

    }

    public String getName()
    {
        return name;
    }
}
