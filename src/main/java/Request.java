public class Request {
    private int floor;

    public Request(int floor) {
        this.floor = floor;
    }

    @Override
    public String toString() {
        return "Request{" +
                "floor=" + floor +
                '}';
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }
}
