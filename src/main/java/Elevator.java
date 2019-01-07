import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Semaphore;

public class Elevator implements Runnable {

    private Doors doors = new Doors(DoorState.CLOSED);
    private Engine engine = new Engine(EngineState.UP);
    private ArrayList floorList = new ArrayList();
    private Floor CURRENTFLOOR;
    private ArrayList requestList = new ArrayList();
    private Request CURRENTREQUEST = new Request(-1);
    private ArrayList doorRequest;
    private Semaphore sem;
    private Buttons b;
    private Reader reader= new Reader();
    private Writer writer= new Writer();
    private ArrayList imgPath= new ArrayList();

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
        ArrayList floor = new ArrayList();
        imgPath.add("close.png");
        imgPath.add("open.png");
        for(int i=1; i<= reader.fileReader();i++){
            floor.add(new Floor(i));
        }
        this.floorList = floor;
        this.CURRENTFLOOR = (Floor) floorList.get(0);
        Buttons b = new Buttons(sem);
        b.setImgPath((String) imgPath.get(1));
        b.setLabelString(CURRENTFLOOR.toString());
        Thread t1 = new Thread(b);
        t1.start();
        while (b.isOn()) {
            if (b.getDoorRequest().size()>0) {
                doorRequest = b.getDoorRequest();
                doors = ((Doors) doorRequest.get(doorRequest.size()-1));
                b.setImgPath((String) imgPath.get(0));
                if (doors.getDoorState() != DoorState.CLOSED) {
                    try {
                        sem.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    while(doors.getDoorState()!=DoorState.CLOSED) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        doorRequest = b.getDoorRequest();
                        doors = (Doors) doorRequest.get(doorRequest.size() - 1);
                        if(doors.getDoorState()==DoorState.CLOSED)
                            b.setImgPath((String) imgPath.get(0));
                        else{
                            b.setImgPath((String) imgPath.get(1));
                        }

                    }
                    sem.release();
                    b.getDoorRequest().clear();
                    doorRequest.clear();
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
                doors.setDoorState(DoorState.CLOSED);
                b.setImgPath((String) imgPath.get(0));
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
                    while (CURRENTFLOOR.getFloorId() != CURRENTREQUEST.getFloor()) {
                        CURRENTFLOOR.setFloorId(CURRENTFLOOR.getFloorId() + 1);
                        b.setLabelString(CURRENTFLOOR.toString());
                        try {
                            Thread.sleep(1000);
                            System.out.print("..........");
                            System.out.print(" Tou a subir ");
                            System.out.println("...........");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    doors.setDoorState(DoorState.OPEN);
                    b.setImgPath((String) imgPath.get(1));
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!tempRequestList.isEmpty()) {
                        for (int i = 0; i < requestList.size(); i++) {

                            Request tempRequest = (Request) requestList.get(i);
                            if (tempRequest.getFloor() == (int) tempRequestList.get(0)) {
                                requestList.remove(i);
                            }
                        }
                        writer.escritorMovimentos(tempRequestList.get(0).toString());
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
                        b.setLabelString(CURRENTFLOOR.toString());
                        try {
                            Thread.sleep(1000);
                            System.out.print("..........");
                            System.out.print("Tou a descer");
                            System.out.println("...........");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    doors.setDoorState(DoorState.OPEN);
                    b.setImgPath((String) imgPath.get(1));
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!tempRequestList.isEmpty()) {
                        for (int i = 0; i < requestList.size(); i++) {
                            Request tempRequest = (Request) requestList.get(i);
                            if (tempRequest.getFloor() == (int) tempRequestList.get(0)) {
                                requestList.remove(i);
                            }
                        }
                        writer.escritorMovimentos(tempRequestList.get(0).toString());
                        tempRequestList.remove(0);
                    } else {
                        engine.setEngineState(EngineState.UP);
                    }

                }
            }

        }
        System.exit(0);
    }
}