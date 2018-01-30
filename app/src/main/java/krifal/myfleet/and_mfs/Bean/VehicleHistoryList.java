package krifal.myfleet.and_mfs.Bean;

/**
 * Created by chinn on 7/15/2017.
 */

public class VehicleHistoryList {

    private String distance,actime,actriptime,totalidle,idletrip,engineon,engineoff,date1;

    public VehicleHistoryList(String distance, String actime, String actriptime, String totalidle,
                              String idletrip, String engineon, String engineoff,String date1){
        this.distance = distance;
        this.actime = actime;
        this.actriptime = actriptime;
        this.totalidle = totalidle;
        this.idletrip = idletrip;
        this.engineon = engineon;
        this.engineoff = engineoff;
        this.date1 = date1;

    }

    public String getDate() {
        return date1;
    }

    public void setDate(String date) {
        this.date1 = date;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getActime() {
        return actime;
    }

    public void setActime(String actime) {
        this.actime = actime;
    }

    public String getActriptime() {
        return actriptime;
    }

    public void setActriptime(String actriptime) {
        this.actriptime = actriptime;
    }

    public String getTotalidle() {
        return totalidle;
    }

    public void setTotalidle(String totalidle) {
        this.totalidle = totalidle;
    }

    public String getIdletrip() {
        return idletrip;
    }

    public void setIdletrip(String idletrip) {
        this.idletrip = idletrip;
    }

    public String getEngineon() {
        return engineon;
    }

    public void setEngineon(String engineon) {
        this.engineon = engineon;
    }

    public String getEngineoff() {
        return engineoff;
    }

    public void setEngineoff(String engineoff) {
        this.engineoff = engineoff;
    }
}
