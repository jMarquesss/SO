public class Floor {
    private int floorId;

    public Floor(int floorId) {
        this.floorId = floorId;
    }

    public int getFloorId() {
        return floorId;
    }

    public void setFloorId(int floorId) {
        this.floorId = floorId;
    }

    @Override
    public String toString() {
        return "Andar atual: " + floorId;
    }
}
