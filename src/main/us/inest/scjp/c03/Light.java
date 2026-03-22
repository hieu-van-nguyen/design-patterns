package us.inest.scjp.c03;

/*
 * JavaBean example
 */
public class Light {
    private int noOfWatts;
    private String location;
    private boolean indicator;

    public Light(int noOfWatts, boolean indicator, String site) {
        String location;
        this.noOfWatts = noOfWatts;
        indicator = indicator;
        location = site;
        this.superfluous();
    }

    public void superfluous() {
        System.out.println(this);
    }

    public int getNoOfWatts() {
        return noOfWatts;
    }

    public void setNoOfWatts(int noOfWatts) {
        this.noOfWatts = noOfWatts;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isIndicator() {
        return indicator;
    }

    public void setIndicator(boolean indicator) {
        this.indicator = indicator;
    }

    public static void main(String[] args) {
        Light light = new Light(100, true, "loft");
        System.out.println(light.getNoOfWatts());
        System.out.println(light.isIndicator());
        System.out.println(light.getLocation());
    }
}
