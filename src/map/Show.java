package map;

import objects.Food;
import objects.Item;
import objects.foods.*;
import objects.items.*;
import team.Felfedezo;
import team.characters.Character;
import team.characters.Felderito;
import team.characters.Katona;
import team.characters.Kereskedo;
import team.slots.Slot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import java.util.Scanner;

import static map.RandomNumber.randomNumber;

public class Show extends JFrame implements ActionListener {

    static int lines = 0;
    static int columns = 0;
    static String[] lvl = new String[20]; //beolvasott txt fajlbol
    static Map[][] map = new Map[20][20]; //Map elemeket tartalmazo terkep
    static JButton[][] buttons = new JButton[20][20]; //gombmatrix
    static int fuggolepes=0;

    private JLabel energiaLabel;
    private JLabel aranyLabel;
    private JLabel hirnevLabel;
    private JLabel labelInventory;
    private JComboBox boxInventory;
    private JLabel labelTeam;
    private JComboBox boxTeam;
    private JButton buttonUse;
    private JLabel hajoInventory;
    private JComboBox boxHajoInv;
    private JButton buttonEltarol;
    private JButton buttonBerak;
    private JLabel faluInventory;
    private JButton buttonFaluElad;
    private JComboBox boxFaluInv;
    private JLabel faluCharSaleLabel;
    private JComboBox boxChar;
    private JButton buttonChar;
    private JButton buttonNewMission;
    private JButton buttonKincsHirnev;
    private JLabel labelInitBuy;
    private JComboBox boxInitBuy;
    private JButton buttonInitBuy;
    private JLabel labelInitChar;
    private JComboBox boxInitChar;
    private JButton buttonInitChar;
    private JButton buttonStart;
    private JButton buttonStop;


    static int x =0;
    static int y=0;
    private static char mission='1';

    private static Hajo h;
    public static Felfedezo jozsi = new Felfedezo();
    public static Piramis p;
    private static InitShopList isl;
    private static boolean canStart;

    public static void read(){

        lines = 0;
        columns = 0;
        try {
            File myObj = new File("src/resources/lvl" + mission + ".txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                lvl[lines] = myReader.nextLine();
                lines++;
            }
            columns=lvl[0].length();
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public Show(){ //konstruktor xd
        super("The Peculiar Expedition"); //ez lesz a neve az ablaknak

        initComponents(); //meghivjuk az initComponenets fuggvenyt, ami inicializalja az ablak kinezetet

        setSize(columns*48,lines*48+50);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

    }


    private void initComponents(){
        isl = new InitShopList();
        canStart=false;

        setLayout(new FlowLayout());
        for(int i=0;i<lines;i++){
            for(int j=0;j<columns;j++){
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(40, 40));
                add(btn);
                btn.addActionListener(this::actionPerformed);
                buttons[i][j] = btn; // ------------------------------------> a gombok egy matrixban vannak tarolva
                initPaint(i,j);
            }
        }

        energiaLabel = new JLabel("Energia:" + jozsi.getEnergia());
        add(energiaLabel);
        aranyLabel = new JLabel("Arany:" + jozsi.getArany());
        add(aranyLabel);
        hirnevLabel = new JLabel("Hirnev:" + jozsi.getHirnev());
        add(hirnevLabel);

        labelInventory = new JLabel("Inventory:");
        add(labelInventory);
        boxInventory = new JComboBox(jozsi.getInventory());
        boxInventory.setEditable(false);
        add(boxInventory);
        buttonUse = new JButton("Use");
        add(buttonUse);
        buttonUse.addActionListener(this::actionPerformed);

        labelTeam = new JLabel("Team");
        add(labelTeam);
        boxTeam = new JComboBox(jozsi.getTeammates());
        add(boxTeam);


        paintFicko();
        initBuy();
//        JOptionPane.showMessageDialog(this,"Üdvözöllek a The Peculiar Expedition játékban." +
//                "\nAz első küldetés előtt tusz választani egy csapattársat meg alapvető eszközöket, ételeket venni." +
//                "\nFeladatod, hogy megtaláld az ararny piramist!" +
//                "\nJáték közben a küldetésed, hogy különböző kincseket gyűjts, és ezeket hírnevre vagy pénzre váltsd!" +
//                "\nStart gombra kattintva elindíthatod a játékot!" +
//                "\nJó szórakozást!");
    }

    private void initBuy(){

        labelInitBuy = new JLabel("Eszkozok:");
        add(labelInitBuy);
        boxInitBuy = new JComboBox(isl.getForSale());
        add(boxInitBuy);
        buttonInitBuy = new JButton("Vesz");
        buttonInitBuy.addActionListener(this::actionPerformed);
        add(buttonInitBuy);

        labelInitChar = new JLabel("Uj csapattars");
        add(labelInitChar);
        boxInitChar = new JComboBox(isl.getCharSale());
        add(boxInitChar);
        buttonInitChar = new JButton("Vesz");
        buttonInitChar.addActionListener(this::actionPerformed);
        add(buttonInitChar);

        buttonStart = new JButton("Start");
        buttonStart.addActionListener(this::actionPerformed);
        add(buttonStart);
    }

    private void initPaint(int i,int j){
        Ures ures = new Ures();
        switch (lvl[i].charAt(j)){
            case '0' : {Tenger t = new Tenger(); map[i][j]=t; buttons[i][j].setIcon(new ImageIcon(ures.getImg()));break;}
            case '1' : {Fold f = new Fold(); map[i][j]=f; buttons[i][j].setIcon(new ImageIcon(ures.getImg()));break;}
            case '2' : {Hegy h = new Hegy(); map[i][j]=h; buttons[i][j].setIcon(new ImageIcon(ures.getImg()));break;}
            case '3' : {To t = new To(); map[i][j]=t; buttons[i][j].setIcon(new ImageIcon(ures.getImg()));break;}
            case '4' : {Jungle jungle = new Jungle(); map[i][j]=jungle; buttons[i][j].setIcon(new ImageIcon(ures.getImg()));break;}
            case 'h' : {this.h= new Hajo(); map[i][j]=this.h; buttons[i][j].setIcon(new ImageIcon(this.h.getImg()));break;}
            case 'b' : {Barlang b = new Barlang(); map[i][j]=b; buttons[i][j].setIcon(new ImageIcon(ures.getImg()));break;}
            case 'o' : {Oltar o = new Oltar(); map[i][j]=o; buttons[i][j].setIcon(new ImageIcon(ures.getImg()));break;}
            case '*' : {this.p = new Piramis(); map[i][j]=this.p; buttons[i][j].setIcon(new ImageIcon(ures.getImg()));break;}
            case 'n' : {Nedves n = new Nedves(); map[i][j]=n; buttons[i][j].setIcon(new ImageIcon(ures.getImg()));break;}
            case 'f' : {Falu f = new Falu(); map[i][j]=f; buttons[i][j].setIcon(new ImageIcon(ures.getImg()));break;}
            case 'i' : {this.x=i;this.y=j;Fold f = new Fold(); map[i][j]=f; buttons[i][j].setIcon(new ImageIcon(ures.getImg()));break;}
        }
    }

    private void paint(int i,int j){
        buttons[i][j].setIcon(new ImageIcon(map[i][j].getImg()));
    }

    private void paintFicko(){
        int latokor = 1;
        if(jozsi.containsFelderito()){
            latokor = 2;
        }
        for(int i = x-latokor; i<=x+latokor; i++){
            for(int j = y-latokor; j<=y+latokor; j++){
                if(i>=0 && j>=0 && i<lines && j<columns){
                    paint(i,j);
                }
            }
        }
        if(lvl[x].charAt(y)=='i' || lvl[x].charAt(y)=='1' || lvl[x].charAt(y)=='4' || lvl[x].charAt(y)=='b' || lvl[x].charAt(y)=='o' ||lvl[x].charAt(y)=='*' || lvl[x].charAt(y)=='n' || lvl[x].charAt(y)=='f' || lvl[x].charAt(y)=='h'){
            buttons[x][y].setIcon(new ImageIcon(map[x][y].getImg_f()));
        }

    }

    private boolean isNext(int i, int j){
        if(i>=x-1 && i<=x+1 && j>=y-1 && j<=y+1 && (i!=x || j!=y) && canStart==true){
            return true;
        }
        return false;
    }

    private void hajoLep(){
        hajoInventory = new JLabel("Hajo raktar:");
        add(hajoInventory);
        boxHajoInv = new JComboBox(h.getRaktar());
        add(boxHajoInv);
        buttonEltarol = new JButton("Elraktaroz");
        buttonEltarol.addActionListener(this::actionPerformed);
        add(buttonEltarol);
        buttonBerak = new JButton("Berak");
        buttonBerak.addActionListener(this::actionPerformed);
        add(buttonBerak);
        if(this.p.isVisited()==false){
            buttonStop = new JButton("Hazamegy");
            buttonStop.addActionListener(this::actionPerformed);
            add(buttonStop);
        }
        if(this.p.isVisited()==true){
            buttonFaluElad = new JButton("Elad");
            buttonFaluElad.addActionListener(this::actionPerformed);
            add(buttonFaluElad);
            buttonKincsHirnev = new JButton("Hirnevre valt");
            buttonKincsHirnev.addActionListener(this::actionPerformed);
            add(buttonKincsHirnev);
            buttonNewMission = new JButton("New Mission");
            buttonNewMission.addActionListener(this::actionPerformed);
            add(buttonNewMission);
        }
    }

    public void faluLep(){
        faluInventory = new JLabel("Falu piac:");
        add(faluInventory);
        boxFaluInv = new JComboBox(map[x][y].getForSale());
        add(boxFaluInv);
        buttonBerak = new JButton("Megvasarol");
        buttonBerak.addActionListener(this::actionPerformed);
        add(buttonBerak);
        buttonFaluElad = new JButton("Elad");
        buttonFaluElad.addActionListener(this::actionPerformed);
        add(buttonFaluElad);
        faluCharSaleLabel = new JLabel("Megveheto tarsak:");
        add(faluCharSaleLabel);
        boxChar = new JComboBox(map[x][y].getCharSale());
        add(boxChar);
        buttonChar = new JButton("Felberel");
        buttonChar.addActionListener(this::actionPerformed);
        add(buttonChar);
    }


    //gombok nyomkolászása
    @Override
    public void actionPerformed(ActionEvent e) {
        //terkepen a gombok
        for(int i=0;i<lines;i++){
            for(int j=0;j<columns;j++){
                if(e.getSource() == buttons[i][j] && lvl[i].charAt(j)!='0' && lvl[i].charAt(j)!='3' && lvl[i].charAt(j)!='2' && isNext(i,j)){

                    //eltunteti a hajo inventoryt
                    if(map[x][y] instanceof Hajo){
                        buttonBerak.setVisible(false);
                        buttonEltarol.setVisible(false);
                        hajoInventory.setVisible(false);
                        boxHajoInv.setVisible(false);
                        remove(buttonBerak);
                        remove(buttonEltarol);
                        remove(hajoInventory);
                        remove(boxHajoInv);
                        if(this.p.isVisited()==false){
                            remove(buttonStop);
                            buttonStop.setVisible(false);
                        }
                        if(this.p.isVisited()){
                            remove(buttonNewMission);
                            buttonNewMission.setVisible(false);
                            remove(buttonFaluElad);
                            buttonFaluElad.setVisible(false);
                            remove(buttonKincsHirnev);
                            buttonKincsHirnev.setVisible(false);
                        }
                    }

                    //eltunteti a falu inventoryt
                    if(map[x][y] instanceof Falu){
                        buttonBerak.setVisible(false);
                        faluInventory.setVisible(false);
                        boxFaluInv.setVisible(false);
                        faluCharSaleLabel.setVisible(false);
                        boxChar.setVisible(false);
                        buttonChar.setVisible(false);
                        buttonFaluElad.setVisible(false);
                        remove(buttonBerak);
                        remove(faluInventory);
                        remove(boxFaluInv);
                        remove(faluCharSaleLabel);
                        remove(boxChar);
                        remove(buttonChar);
                        remove(buttonFaluElad);
                    }

                    //a kiiratasokat vegzi
                    paint(x,y);
                    x=i;
                    y=j;
                    paintFicko();
                    double a = (jozsi.getEnergia()-(map[x][y].getKoltseg()+1)*(double)jozsi.getMozgas()/(double)100);
                    a = Math.round(a * 100.0) / 100.0;
                    jozsi.setEnergia(a);
                    energiaLabel.setText("Energia:"+jozsi.getEnergia());

                    //ha hajora lep
                    if(map[x][y] instanceof Hajo){
                        hajoLep();
                    }
                    //ha falura lep
                    if(map[x][y] instanceof Falu){
                        faluLep();
                    }
                    //ha piramisra lep
                    if(map[x][y] instanceof Piramis && !this.p.isVisited()){
                        this.p.setVisited();
                        jozsi.setHirnev(jozsi.getHirnev()+1000);
                        hirnevLabel.setText("Hirnev:"+jozsi.getHirnev());
                        jozsi.addSlot(new Kincs(100));
                        JOptionPane.showMessageDialog(this,"Megtalaltad az arany piramist, +1000 hirnev");
                        boxInventory.setModel(new DefaultComboBoxModel(jozsi.getInventory()));
                    }
                    //ha oltarra vagy barlangra lep
                    if((map[x][y] instanceof Oltar || map[x][y] instanceof Barlang) && map[x][y].isMegtalalKincs()==false){
                        map[x][y].setMegtalalKincs();
                        int r = randomNumber(10,50);
                        jozsi.addSlot(new Kincs(r));
                        JOptionPane.showMessageDialog(this,"Talaltal egy kincset");
                        boxInventory.setModel(new DefaultComboBoxModel(jozsi.getInventory()));
                    }

                    //ha nincs energia, akkor 8%esellyel elhagyja minden csapattag a csapatot, majd te is
                    if(jozsi.getEnergia()==0){
                        int r = randomNumber(0,100);
                        int c = randomNumber(0,jozsi.countTeam());
                        if(r<8){
                            if(jozsi.countTeam()>1){
                                jozsi.removeCharacter(c);
                                jozsi.printTeam();
                                boxTeam.setModel(new DefaultComboBoxModel(jozsi.getTeammates()));
                            }else if(jozsi.countTeam()==1) {
                                jozsi.removeCharacter();
                                jozsi.printTeam();
                                boxTeam.setModel(new DefaultComboBoxModel(jozsi.getTeammates()));
                            }else{
                                JOptionPane.showMessageDialog(this,"Elfogyott az energiad: GAME OVER");
                                dispose();
                            }
                        }
                    }

                    //ha jozsi kabszi fuggo, akkor a kovetkezo 30 lepesen belul nem kap kivato kajat, akkor kilepnek
                    if(jozsi.isFuggo()){
                        fuggolepes++;
                    }else{
                        fuggolepes=0;
                    }
                    if(fuggolepes>30){
                        int r = randomNumber(0,100);
                        if(r<10){
                            JOptionPane.showMessageDialog(this,"Fuggo vagy, es 30-nal tobbet leptel: GAME OVER");
                            dispose();
                            System.exit(0);
                        }
                    }

                }
            }
        }

        //use gomb
        if(e.getSource()== buttonUse){
            Slot i = (Slot) boxInventory.getSelectedObjects()[0];
            if(i.getSlots()[0] instanceof Food){
                jozsi.consumeFood((Food) i.getSlots()[0]); //konzumalja mint FOOD
                if(jozsi.containsKereskedo() && (i.getSlots()[0] instanceof Whiskey)){ //ha van katona, es whiskeyt ittal akkor +20 energia
                    jozsi.setEnergia(jozsi.getEnergia()*1.2);
                }
                if(jozsi.containsSaman() && (i.getSlots()[0] instanceof Kabitoszer)){ //ha van katona, es whiskeyt ittal akkor +20 energia
                    jozsi.setEnergia(jozsi.getEnergia()*1.2);
                }
            }
            if(i.getSlots()[0] instanceof Bozotvago && map[x][y] instanceof Jungle){ //ez tipikusan a bozotvagora valo
                jozsi.consumeItem((Item) i.getSlots()[0]); //elhasználja mint ITEM
                map[x][y]=new Fold();
                paintFicko();
            }
            energiaLabel.setText("Energia:"+jozsi.getEnergia()); //felirat frissitese
            boxInventory.setModel(new DefaultComboBoxModel(jozsi.getInventory())); //legordulo box frissitese
        }

        //eltarol gomb
        if(e.getSource()==buttonEltarol){
            Slot s = (Slot) boxInventory.getSelectedObjects()[0];
            h.elraktaroz(s.getSlots()[0]);
            System.out.println(s.getSlots()[0] + "+" + h.getRaktar()[0]);
            jozsi.consumeItem(s.getSlots()[0]);
            boxHajoInv.setModel(new DefaultComboBoxModel(h.getRaktar()));
            boxInventory.setModel(new DefaultComboBoxModel(jozsi.getInventory()));

        }

        //berak gomb, az eltarol inverze
        if(e.getSource()==buttonBerak){
            if(map[x][y] instanceof Hajo){
                Slot s = (Slot) boxHajoInv.getSelectedObjects()[0];
                h.consumeItem(s.getSlots()[0]);
                System.out.println(s.getSlots()[0] + "+" + h.getRaktar()[0]);
                jozsi.addSlot(s.getSlots()[0]);
                boxHajoInv.setModel(new DefaultComboBoxModel(h.getRaktar()));
                boxInventory.setModel(new DefaultComboBoxModel(jozsi.getInventory()));
            }
            if(map[x][y] instanceof Falu){
                double akcio=1;
                if(jozsi.containsKereskedo()){ //ha van kereskedo, akkor olcsobban veszel
                    akcio = 0.8;
                }
                Slot s = (Slot) boxFaluInv.getSelectedObjects()[0];
                if(jozsi.getArany()>=(int)(akcio*s.getSlots()[0].getErtek())){
                    map[x][y].consumeItem(s.getSlots()[0]);
                    jozsi.addSlot(s.getSlots()[0]);
                    jozsi.setArany(jozsi.getArany()-(int)(akcio*s.getSlots()[0].getErtek()));
                }
                aranyLabel.setText("Arany:"+jozsi.getArany());
                boxFaluInv.setModel(new DefaultComboBoxModel(map[x][y].getForSale()));
                boxInventory.setModel(new DefaultComboBoxModel(jozsi.getInventory()));
            }
        }

        //csapattarsat vasarolgomb
        if(e.getSource()==buttonChar){
            double akcio=1;
            if(jozsi.containsKereskedo()){//ha van kereskedo, akkor olcsobban veszel
                akcio = 0.8;
            }
            Character s = (Character) boxChar.getSelectedObjects()[0];
            if(jozsi.getArany()>=(int)(akcio*150) && jozsi.countTeam()<3){
                map[x][y].buyCharacter(s);
                jozsi.addCharacter(s);
                jozsi.setArany(jozsi.getArany()-(int)(akcio*150));
            }
            aranyLabel.setText("Arany:"+jozsi.getArany());
            boxChar.setModel(new DefaultComboBoxModel(map[x][y].getCharSale()));
            boxTeam.setModel(new DefaultComboBoxModel(jozsi.getTeammates()));
        }

        //faluban elado gomb
        if(e.getSource()==buttonFaluElad){
            double akcio=1;
            if(jozsi.containsKereskedo()){//ha van kereskedo, akkor dragabban adsz el
                akcio = 1.2;
            }
            Slot s = (Slot) boxInventory.getSelectedObjects()[0];
            jozsi.consumeItem(s.getSlots()[0]);
            jozsi.setArany(jozsi.getArany()+(int)(akcio*s.getSlots()[0].getErtek()));
            boxInventory.setModel(new DefaultComboBoxModel(jozsi.getInventory()));
            aranyLabel.setText("Arany:"+jozsi.getArany());
        }

        //uj kuldetes gomb
        if(e.getSource()==buttonNewMission){
            if(mission<'2'){
                JOptionPane.showMessageDialog(this,"Sikeresen befejezted a kuldetest");
                canStart=false;
                mission++;
                getContentPane().removeAll();
                repaint();
                read();
                initComponents();
            }else{
                JOptionPane.showMessageDialog(this,"Sikeresen befejezted a jatekot");
                dispose();
            }

        }

        //kincset hirnevre valt gomb
        if(e.getSource()==buttonKincsHirnev){
            Slot s = (Slot) boxInventory.getSelectedObjects()[0];
            int ind = boxInventory.getSelectedIndex();
            if(s.getSlots()[0] instanceof Kincs){
                jozsi.consumeItem(ind);
                jozsi.setHirnev(jozsi.getHirnev()+s.getSlots()[0].getErtek());
                boxInventory.setModel(new DefaultComboBoxModel(jozsi.getInventory()));
                hirnevLabel.setText("Hirnev:"+jozsi.getHirnev());
            }
        }

        //cucct vesz kuldetes elott
        if(e.getSource()==buttonInitBuy){
            Slot s = (Slot) boxInitBuy.getSelectedObjects()[0];
            if(jozsi.getArany()>=s.getSlots()[0].getErtek()){
                isl.consumeItem(s.getSlots()[0]);
                jozsi.addSlot(s.getSlots()[0]);
                jozsi.setArany(jozsi.getArany()-s.getSlots()[0].getErtek());
            }
            aranyLabel.setText("Arany:"+jozsi.getArany());
            boxInitBuy.setModel(new DefaultComboBoxModel(isl.getForSale()));
            boxInventory.setModel(new DefaultComboBoxModel(jozsi.getInventory()));
        }

        //csapattarsat vesz kuldetes elott
        if(e.getSource()==buttonInitChar){
            Character s = (Character) boxInitChar.getSelectedObjects()[0];
            if(jozsi.getArany()>=150 && jozsi.countTeam()<3){
                isl.buyCharacter(s);
                jozsi.addCharacter(s);
                jozsi.setArany(jozsi.getArany()-150);
            }
            aranyLabel.setText("Arany:"+jozsi.getArany());
            boxInitChar.setModel(new DefaultComboBoxModel(isl.getCharSale()));
            boxTeam.setModel(new DefaultComboBoxModel(jozsi.getTeammates()));
        }

        //start gomb
        if(e.getSource()==buttonStart){
            labelInitBuy.setVisible(false);
            remove(labelInitBuy);
            boxInitBuy.setVisible(false);
            remove(boxInitBuy);
            buttonInitBuy.setVisible(false);
            remove(buttonInitBuy);
            labelInitChar.setVisible(false);
            remove(labelInitChar);
            boxInitChar.setVisible(false);
            remove(boxInitChar);
            buttonInitChar.setVisible(false);
            remove(buttonInitChar);
            buttonStart.setVisible(false);
            remove(buttonStart);
            canStart=true;
        }

        //hazamegy gomb
        if(e.getSource()==buttonStop){
            JOptionPane.showMessageDialog(this,"Vége a játéknak, hazamentél!");
            dispose();
        }
    }

    public static void build() {
        read();
        new Show().setVisible(true);
    }

}