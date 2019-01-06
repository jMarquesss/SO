import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Buttons extends JFrame implements ActionListener, Runnable {

    private JButton[] buttonsArray = new JButton[4 + 3];
    private ArrayList requestList = new ArrayList();
    private ArrayList doorRequest = new ArrayList();
    private boolean on = true;
    private Semaphore sem;

    public Buttons(Semaphore sem) {
        this.sem = sem;
    }

    @Override
    public void run() {
        createButtons(4);
        enableButtons(buttonsArray, true);
        int x = 0;
        int y = 50;
        int w = 100;
        int h = 60;
        setLayout(null);
        for (int i = 0; i < buttonsArray.length; i++) {
            buttonsArray[i].setBounds(x * i, y * i, w, h);
            add(buttonsArray[i]);
            buttonsArray[i].addActionListener(this);
        }
        setTitle("Buttons");
        setSize(300, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void createButtons(int andares) {
        for (int i = 0; i < andares; i++) {
            JButton jb = new JButton(("Andar " + String.valueOf(i + 1)));
            buttonsArray[i] = jb;
        }
        JButton jb1 = new JButton("A-Abrir porta");
        buttonsArray[andares] = jb1;
        JButton jb2 = new JButton("F-Fechar porta");
        buttonsArray[andares + 1] = jb2;
        JButton jb3 = new JButton("F-Desligar elevador");
        buttonsArray[andares + 2] = jb3;
    }

    public void enableButtons(JButton[] buttonsArray, boolean active) {
        for (JButton b : buttonsArray) {
            b.setEnabled(active);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonsArray[0]) {
            System.out.println("Carreguei no botao 1");
            requestList.add(new Request(1));
        } else if (e.getSource() == buttonsArray[1]) {
            System.out.println("Carreguei no botao 2");
            requestList.add(new Request(2));
        } else if (e.getSource() == buttonsArray[2]) {
            System.out.println("Carreguei no botao 3");
            requestList.add(new Request(3));
        } else if (e.getSource() == buttonsArray[3]) {
            System.out.println("Carreguei no botao 4");
            requestList.add(new Request(4));
        } else if (e.getSource() == buttonsArray[4]) {
            System.out.println("Carreguei no botao Abrir");
            doorRequest.add(new Doors(DoorState.OPEN));
        } else if (e.getSource() == buttonsArray[5]) {
            System.out.println("Carreguei no botao Fechar");
            doorRequest.add(new Doors(DoorState.CLOSE));
        } else {
            System.out.println("Carreguei no botao Encerrar programa");
            on = false;
        }

    }

    public ArrayList getDoorRequest() {
        return doorRequest;
    }

    public void setDoorRequest(ArrayList doorRequest) {
        this.doorRequest = doorRequest;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public Semaphore getSem() {
        return sem;
    }

    public void setSem(Semaphore sem) {
        this.sem = sem;
    }

    public ArrayList getRequestList() {
        return requestList;
    }

    public void setRequestList(ArrayList requestList) {
        this.requestList = requestList;
    }

}