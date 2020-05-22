package projecy;


public class BasePatient {
    /**
     * Class for a basic implementation of Patients with a name and ID
     */
    protected String name;
    protected String id;

    public BasePatient(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getID() {return id;}

    @Override
    public boolean equals(Object o) {
        /**
         * Override so that if ID matches, the patients are equal regardless of other attributes
         */
        if (o == null) {
            return false;
        }

        if (o instanceof BasePatient) {
            BasePatient patient = (BasePatient) o;
            if (patient.getID().compareTo(this.getID()) == 0) {
                return true;
            }
        }
        return false;
    }
}
