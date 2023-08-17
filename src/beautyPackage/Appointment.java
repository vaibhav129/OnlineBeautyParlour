package beautyPackage;
public class Appointment {

    private int id;
    private int customerId;
    private int beauticianId;
    private int serviceId;
    private String date;
    private String time;

    public Appointment(int id, int customerId, int beauticianId, int serviceId, String date, String time) {
        this.id = id;
        this.customerId = customerId;
        this.beauticianId = beauticianId;
        this.serviceId = serviceId;
        this.date = date;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getBeauticianId() {
        return beauticianId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
