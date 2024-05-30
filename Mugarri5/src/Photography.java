import javax.swing.*;
import org.jdesktop.swingx.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.io.File;
import java.util.HashMap;
import javax.swing.ImageIcon;

public class Photography extends JFrame {
    ArrayList<Argazki> argazkis;
    Argazkilari[] argazkilaris;
    JLabel irudiaLabel;
    ImageIcon irudi;
    ImageIcon icon;
    DefaultListModel<Argazki> listModel;
    private DatuBaseKudeatzailea datuBaseKudeatzailea;
    int minAward;

    public Photography() {
        datuBaseKudeatzailea = new DatuBaseKudeatzailea();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(650, 400));
        setLayout(new GridLayout(3, 2));

        JPanel panel5 = new JPanel();
        JButton remove = new JButton("Remove");
        panel5.add(remove);
        add(panel5);

        JPanel panel6 = new JPanel();
        JButton award = new JButton("Award");
        panel6.add(award);
        add(panel6);

        JPanel panel1 = new JPanel();
        add(panel1);
        JLabel photographer = new JLabel("Photographer");
        panel1.add(photographer);
        argazkilaris = datuBaseKudeatzailea.argazkilariakLortu();
        JComboBox photographerBox = new JComboBox<>(argazkilaris);
        panel1.add(photographerBox);
        photographerBox.setPreferredSize(new Dimension(120, 30));

        JPanel panel2 = new JPanel();
        add(panel2);
        JLabel photos = new JLabel("Photos after");
        panel2.add(photos);
        JXDatePicker datePicker = new JXDatePicker();
        datePicker.setFormats("yyyy/MM/dd");
        datePicker.setPreferredSize(new Dimension(140, 30));
        panel2.add(datePicker);

        JPanel panel3 = new JPanel();
        add(panel3);
        Argazkilari argazkilariMomentukoa = (Argazkilari) photographerBox.getSelectedItem();
        argazkis = datuBaseKudeatzailea.argazkiakLortu();
        listModel = new DefaultListModel<>();
        for (Argazki argazki : argazkis) {
            assert argazkilariMomentukoa != null;
            if(argazki.getIdArgazkilari() == argazkilariMomentukoa.getIdArgazkilari()){

                listModel.addElement(argazki);
            }
        }
        JList<Argazki> jList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(jList);
        panel3.add(scrollPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(300, 150));

        JPanel panel4 = new JPanel();
        add(panel4);
        irudiaLabel = new JLabel();
        panel4.add(irudiaLabel);
        irudi = new ImageIcon("./irudiak/ansealdams1.jpg");
        irudiaLabel.setSize(new Dimension(260,200));
        icon = new ImageIcon(irudi.getImage().getScaledInstance(irudiaLabel.getWidth(), irudiaLabel.getHeight(), Image.SCALE_DEFAULT));
        irudiaLabel.setIcon(icon);

        pack();
        setVisible(true);
        photographerBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                listModel.clear();
                Argazkilari argazkilariMomentukoa = (Argazkilari) photographerBox.getSelectedItem();
                Date selectedDate = datePicker.getDate();
                for (Argazki argazki : argazkis) {
                    assert argazkilariMomentukoa != null;
                    if(argazki.getIdArgazkilari() == argazkilariMomentukoa.getIdArgazkilari() && argazki.getData().before(selectedDate)){
                        listModel.addElement(argazki);
                    }
                }
            }
        });

        jList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Argazki selectedImage = jList.getSelectedValue();
                if (selectedImage != null) {
                    irudiaAldatu(selectedImage);
                }
            }
        });
        irudiaLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Argazki selArgazki = jList.getSelectedValue();
                if (selArgazki != null) {
                    inkrementatuBistaratzeak(selArgazki);
                }
            }
        });
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {
                datuBaseKudeatzailea.itxi(argazkis, argazkilaris);
            }
            @Override
            public void windowClosed(WindowEvent e) {}
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {
                argazkilaris = datuBaseKudeatzailea.argazkilariakLortu();
                argazkis = datuBaseKudeatzailea.argazkiakLortu();
            }
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        award.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                minAward=Integer.parseInt(JOptionPane.showInputDialog(null,"asd","asd",JOptionPane.OK_CANCEL_OPTION));
                HashMap<Integer,Integer> mapa = sortuBistaratzeenMapa();
                for (Argazkilari argazkilari:argazkilaris) {
                    if (mapa.get(argazkilari.getIdArgazkilari()) > minAward){
                        argazkilari.setSaritua(1);
                    }else {
                        argazkilari.setSaritua(0);
                    }
                }

            }
        });
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Argazki argazki:argazkis) {
                    int idArgazkilari = argazki.idArgazkilari;
                    Argazkilari bereArgazkilari = null;

                    for (Argazkilari argazkilari:argazkilaris) {
                        if (argazkilari.getIdArgazkilari() == idArgazkilari){
                            bereArgazkilari = argazkilari;
                        }
                    }
                    if (argazki.getBistaratzeKop() == 0 || bereArgazkilari.getSaritua() == 0){
                        int erantzuna = JOptionPane.showConfirmDialog(null,argazki.getIzenburua()+" argazkia ezabatu nahi duzu?","Ezabatu",JOptionPane.YES_NO_OPTION);
                        if (erantzuna==1){
                            datuBaseKudeatzailea.argazkiaEzabatu(argazki);
                            if (datuBaseKudeatzailea.argazkilariArgKop(bereArgazkilari)==0){
                                datuBaseKudeatzailea.argazkilariaEzabatu(bereArgazkilari);
                            }
                        }
                    }
                }

            }
        });
    }



    public static void main(String[] args) {
        new Photography();
    }

    private void irudiaAldatu(Argazki imageName) {
        String imagePath = imageName.fitxategia;
        icon = new ImageIcon(imagePath);
        irudiaLabel.setIcon(icon);

    }
    public void inkrementatuBistaratzeak(Argazki a){

        int kop = a.getBistaratzeKop();
        kop = kop + 1;
        a.setBistaratzeKop(kop);
    }
    public HashMap<Integer, Integer> sortuBistaratzeenMapa(){
        HashMap<Integer, Integer> argBistKop = new HashMap<>();
        for (Argazkilari argazkilari : argazkilaris){
            int tot = 0;
            for (Argazki argazki:argazkis){
                if (argazki.getIdArgazkilari() == argazkilari.getIdArgazkilari()){
                    tot += argazki.bistaratzeKop;
                }
            }
            argBistKop.put(argazkilari.getIdArgazkilari(), tot);
        }
        return argBistKop;
    }

}
