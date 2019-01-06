public class Doors {
    private DoorState doorState;

    public Doors(DoorState doorState) {
        this.doorState = doorState;
    }

    @Override
    public String toString() {
        return "Doors{" +
                "doorState=" + doorState +
                '}';
    }

    public DoorState getDoorState() {
        return doorState;
    }

    public void setDoorState(DoorState doorState) {
        this.doorState = doorState;
    }
}
