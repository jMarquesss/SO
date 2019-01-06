import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Semaphore;

public class Elevator implements Runnable {

    private Doors doors = new Doors(DoorState.CLOSE);
    private Engine engine = new Engine(EngineState.UP);
    private ArrayList floorList = new ArrayList();
    private Floor CURRENTFLOOR;
    private ArrayList requestList = new ArrayList();
    private Request CURRENTREQUEST = new Request(-1);
    private ArrayList doorRequest;
    private Semaphore sem;
    private Buttons b;

    public Elevator(Semaphore sem) {
        this.sem = sem;
        this.b = new Buttons(sem);
    }

    public Buttons getB() {
        return b;
    }

    public void setB(Buttons b) {
        this.b = b;
    }

    public ArrayList getRequestList() {
        return requestList;
    }

    public void setRequestList(ArrayList requestList) {
        this.requestList = requestList;
    }

    public Floor getCURRENTFLOOR() {
        return CURRENTFLOOR;
    }

    public void setCURRENTFLOOR(Floor CURRENTFLOOR) {
        this.CURRENTFLOOR = CURRENTFLOOR;
    }

    public ArrayList getFloorList() {
        return floorList;
    }

    public void setFloorList(ArrayList floorList) {
        this.floorList = floorList;
    }


    @Override
    public void run() {
        System.out.println(Thread.currentThread().toString());
        try {
            sem.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Permits no elevator " + sem.availablePermits());
        ArrayList floor = new ArrayList();
        floor.add(new Floor(1));
        floor.add(new Floor(2));
        floor.add(new Floor(3));
        floor.add(new Floor(4));
        this.floorList = floor;
        this.CURRENTFLOOR = (Floor) floorList.get(0);
        Buttons b = new Buttons(sem);
        Thread t1 = new Thread(b);
        t1.start();
        while (b.isOn()) {
            if (!b.getDoorRequest().isEmpty() && doorRequest!= b.getDoorRequest()) {
                doorRequest = b.getDoorRequest();
                doors = (Doors) doorRequest.get(doorRequest.size()-1);
                System.out.println("Portas estao: "+doors.getDoorState().toString());
                while (doors.getDoorState() != DoorState.CLOSE) {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    doorRequest = b.getDoorRequest();
                    doors = (Doors) doorRequest.get(doorRequest.size()-1);
                }
            }
            if (requestList.isEmpty()) {
                try {
                    Thread.sleep(5000);
                    requestList = b.getRequestList();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                doors.setDoorState(DoorState.CLOSE);
                System.out.println("Portas estao: "+doors.getDoorState().toString());
                requestList = b.getRequestList();
                if (engine.getEngineState() == EngineState.UP) {
                    ArrayList tempRequestList = new ArrayList();
                    for (int i = 0; i < requestList.size(); i++) {
                        Request tempRequest = (Request) requestList.get(i);
                        if (tempRequest.getFloor() >= CURRENTFLOOR.getFloorId()) {
                            tempRequestList.add(tempRequest.getFloor());
                        }
                    }
                    Collections.sort(tempRequestList);
                    if (!tempRequestList.isEmpty()) {
                        CURRENTREQUEST.setFloor((int) tempRequestList.get(0));
                    }
                    System.out.println("ANDAR ATUAL " + CURRENTFLOOR);
                    while (CURRENTFLOOR.getFloorId() != CURRENTREQUEST.getFloor()) {
                        CURRENTFLOOR.setFloorId(CURRENTFLOOR.getFloorId() + 1);
                        try {
                            Thread.sleep(1000);
                            System.out.print("..........");
                            System.out.print(" Tou a subir ");
                            System.out.println("...........");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("CHEGUEI PUTOS, ANDAR " + CURRENTFLOOR);
                    doors.setDoorState(DoorState.OPEN);
                    System.out.println("Portas estao: "+doors.getDoorState().toString());
                    if (!tempRequestList.isEmpty()) {
                        for (int i = 0; i < requestList.size(); i++) {
                            Request tempRequest = (Request) requestList.get(i);
                            if (tempRequest.getFloor() == (int) tempRequestList.get(0)) {
                                requestList.remove(i);
                            }
                        }
                        tempRequestList.remove(0);
                    } else {
                        engine.setEngineState(EngineState.DOWN);
                    }

                } else {
                    ArrayList tempRequestList = new ArrayList();
                    for (int i = 0; i < requestList.size(); i++) {
                        Request tempRequest = (Request) requestList.get(i);
                        if (tempRequest.getFloor() <= CURRENTFLOOR.getFloorId()) {
                            tempRequestList.add(tempRequest.getFloor());
                        }
                    }
                    Collections.sort(tempRequestList, Collections.reverseOrder());
                    if (!tempRequestList.isEmpty()) {
                        CURRENTREQUEST.setFloor((int) tempRequestList.get(0));
                    }
                    System.out.println("ANDAR ATUAL " + CURRENTFLOOR);
                    while (CURRENTFLOOR.getFloorId() != CURRENTREQUEST.getFloor()) {
                        CURRENTFLOOR.setFloorId(CURRENTFLOOR.getFloorId() - 1);
                        try {
                            Thread.sleep(1000);
                            System.out.print("..........");
                            System.out.print("Tou a descer");
                            System.out.println("...........");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("CHEGUEI PUTOS, ANDAR " + CURRENTFLOOR);
                    doors.setDoorState(DoorState.OPEN);
                    System.out.println("Portas estao: "+doors.getDoorState().toString());
                    if (!tempRequestList.isEmpty()) {
                        for (int i = 0; i < requestList.size(); i++) {
                            Request tempRequest = (Request) requestList.get(i);
                            if (tempRequest.getFloor() == (int) tempRequestList.get(0)) {
                                requestList.remove(i);
                            }
                        }
                        tempRequestList.remove(0);
                    } else {
                        engine.setEngineState(EngineState.UP);
                    }

                }
            }

        }
        t1.interrupt();
        System.out.println(requestList.toString());
        sem.release();
    }
}