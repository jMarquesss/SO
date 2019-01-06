import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Buttons extends JFrame implements ActionListener, Runnable {

    private JButton[] buttonsArray = new JButton[4 + 3];
    private String labelString;
    private ArrayList requestList = new ArrayList();
    private ArrayList doorRequest = new ArrayList();
    private boolean on = true;
    private Semaphore sem;
    private Reader reader= new Reader();
    private String imgPath;
    public Buttons(Semaphore sem) {
        this.sem = sem;
    }

    @Override
    public void run() {
        createButtons(reader.fileReader());
        enableButtons(buttonsArray, true);
        setTitle("Elevador");
        setPreferredSize(new Dimension(500, 500));
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        JPanel panel = new JPanel();
        JPanel image = new JPanel();
        JLabel label = new JLabel();
        label.setText(labelString);
        add(label);
        JLabel img = new JLabel(new ImageIcon(imgPath));
        image.add(img);
        panel.setLayout(new GridLayout(3,3));
        for (int i = 0; i < buttonsArray.length; i++) {
            panel.add(buttonsArray[i]);
            buttonsArray[i].addActionListener(this);
        }
        setLayout(new BorderLayout());
        add(panel, BorderLayout.SOUTH);
        add(image, BorderLayout.CENTER);
        pack();
        while(isOn()){
            try {
                Thread.sleep(100);
                img.setIcon(new ImageIcon (imgPath));
                label.setText(labelString);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //setSize(215, 730);
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

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getLabelString() {
        return labelString;
    }

    public void setLabelString(String labelString) {
        this.labelString = labelString;
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