import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Eine grafische Anzeige des Simulationsfeldes. Diese Anzeige zeigt ein
 * einfache grafische Darstellung von dem Inhalt der jeweiligen Position. Die
 * Hintergrundfarbe ist voreingestellt. Die Farben der jewiligen Spezie werden
 * mit der setColor-Methode eingestellt.
 * 
 * Mittlerweile wurde die Anzeige erweitert um ein Panel, welches den Verlauf
 * der Population darstellt, SchaltknÃ¶pfen zur Steuerung der Simulation und
 * einem zusÃ¤tzlichen Fenster, welches erlaubt Simulationsoptionen zu setzen.
 * 
 * @author Frank Lesske / C. Brand
 */
public class SimulatorAnzeige extends JFrame implements ActionListener,
    ChangeListener {
  // Farbe fÃ¼r leere Felder
  private static final Color EMPTY_COLOR = Color.white;

  // Farbe fÃ¼r unbekannte Objekte
  private static final Color UNKNOWN_COLOR = Color.gray;

  private final String STEP_PREFIX = "Schritt: ";

  private final String POPULATION_PREFIX = "Bevölkerung: ";

  private JLabel stepLabel, population, fuchsAnzL, haseAnzL, jagdAnzL;

  private JTextField anzahlFeld;

  private JLabel anzahlLabel;

  private JButton startB, stopB, exitB, optionB;
  
  private JCheckBox jagdbox;

  private OptionFrame optionFrame;

  private FieldView fieldView;

  private PopulationView pview;

  private JScrollPane scroller, fieldScroll;

  private JSlider waitSlider;

  protected static Image plantImage;

  protected static Image rabbitImage, foxImage, jagdImage;

  protected static Image StdImage;

  private Simulator simulator;

  private int schritte;

  private int GRID_VIEW_SCALING_FACTOR = 15;

  // Ein Objekt zur Speicherung von statistischen Simulationsdaten
  // private FieldStats stats;

  /**
   * Erzeuge eine Anzeige der gegebenen Breite und HÃ¶he
   */
  public SimulatorAnzeige(int hoch, int breit, Simulator sim) {

    simulator = sim;
    setTitle("Hase und Fuchs Simulation");
    stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
    population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);
    JLabel fuchsL = new JLabel("Füchse:");
    fuchsAnzL = new JLabel("      ");
    fuchsAnzL.setForeground(Color.red);
    JLabel haseL = new JLabel("Hasen:");
    haseAnzL = new JLabel("     ");
    haseAnzL.setForeground(Color.darkGray);
    JLabel jagdL = new JLabel("Jäger:");
    jagdAnzL = new JLabel("      ");
    jagdAnzL.setForeground(Color.blue);

    setLocation(100, 50);

    fieldView = new FieldView(hoch, breit);

    pview = new PopulationView(breit);

    Container contents = getContentPane();
    contents.add(stepLabel, BorderLayout.NORTH);

    fieldScroll = new JScrollPane(fieldView);
    fieldScroll.setBackground(Color.white);
    contents.add(fieldScroll, BorderLayout.CENTER);
    scroller = new JScrollPane(pview);
    scroller.setBackground(Color.white);

    JPanel bottom = new JPanel();
    GridBagLayout grid = new GridBagLayout();
    bottom.setLayout(grid);

    GridBagConstraints c = new GridBagConstraints();

    c.gridx = 0;
    c.weightx = 1.0;
    c.weighty = 1.0;
    c.gridy = GridBagConstraints.RELATIVE;
    c.gridwidth = GridBagConstraints.REMAINDER;

    JPanel popView = new JPanel();
    popView.add(population);
    popView.add(fuchsL);
    popView.add(fuchsAnzL);
    popView.add(haseL);
    popView.add(haseAnzL);
    popView.add(jagdL);
    popView.add(jagdAnzL);

    grid.setConstraints(popView, c);
    bottom.add(popView);
    c.fill = GridBagConstraints.BOTH;
    c.ipady = 50;
    grid.setConstraints(scroller, c);
    bottom.add(scroller);
    c.ipady = 0;
    
    anzahlLabel = new JLabel("Anzahl der Simulationsschritte");
    jagdbox = new JCheckBox("Jäger?", false);
    startB = new JButton("Start");
    startB.addActionListener(this);
    stopB = new JButton("Stop");
    stopB.addActionListener(this);

    Hashtable slideLabels = new Hashtable();
    slideLabels.put(5, new JLabel("schnell"));
    slideLabels.put(600, new JLabel("langsam"));

    waitSlider = new JSlider(0, 600, 300);
    waitSlider.setPaintTicks(false);
    waitSlider.setMajorTickSpacing(100);
    // waitSlider.setPaintLabels(true);
    waitSlider.addChangeListener(this);
    waitSlider.setLabelTable(slideLabels);
    waitSlider.setPaintLabels(true);

    exitB = new JButton("Exit");
    exitB.addActionListener(this);

    optionB = new JButton("Options");
    optionB.addActionListener(this);

    JPanel bSouth = new JPanel();
    bSouth.add(anzahlLabel);
    anzahlFeld = new JTextField(3);
    anzahlFeld.setText("100");
    bSouth.add(anzahlFeld);
    bSouth.add(jagdbox);
    bSouth.add(startB);
    bSouth.add(stopB);
    bSouth.add(waitSlider);
    bSouth.add(optionB);
    bSouth.add(exitB);

    grid.setConstraints(bSouth, c);
    bottom.add(bSouth);

    contents.add(bottom, BorderLayout.SOUTH);

    this.setPreferredSize(new Dimension(900, 600));
    pack();
    setVisible(true);

    try {
      Toolkit tk = Toolkit.getDefaultToolkit();
      plantImage = tk.getImage("pflanze.jpg");
      rabbitImage = tk.getImage("hase.jpg");
      foxImage = tk.getImage("fuchs.jpg");
      jagdImage = tk.getImage("jagd.jpg");
      MediaTracker tracker = new MediaTracker(this);
      tracker.addImage(plantImage, 0);
      tracker.addImage(rabbitImage, 1);
      tracker.addImage(foxImage, 2);
      tracker.addImage(jagdImage, 3);
      tracker.waitForID(0);
      tracker.waitForID(1);
      tracker.waitForID(2);

      StdImage = rabbitImage;

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Zeigt den aktuellen Zustand des Feldes. Show the current status of the
   * field.
   * 
   * @param schritt
   *            Um den wievielten Iterationsschritt handelt es sich
   * @param feld
   *            FÃ¼r welches Feld soll der Zustand dargestellt werden.
   */
  public void showStatus(int step, Feld feld, String pop) {
    if (!isVisible())
      setVisible(true);

    stepLabel.setText(STEP_PREFIX + step);

    // stats.reset();
    fieldView.preparePaint();
    this.validate();

    for (int row = 0; row < feld.getHeight(); row++) {
      for (int col = 0; col < feld.getWidth(); col++) {
        PositionsObjekt objekt = feld.getObjektAt(row, col);
        if (objekt != null) {
          // falls statt Bilder lieber gefÃ¼llte Rechtecke erwÃ¼nscht sind
          // so Ã¤ndere hier den Aufruf auf
          //fieldView.drawMark(col, row, objekt.getColor());
          fieldView.drawImage(col, row, objekt.getImage());
        } else {
          fieldView.drawMark(col, row, EMPTY_COLOR);
        }
      }
    }

    Vector<Integer> fanzahl = feld.getFoxNumbers();
    int fn = 0;
    if (fanzahl != null && fanzahl.size() > 0) {
      fn = fanzahl.lastElement().intValue();
    }
    fuchsAnzL.setText("" + fn);

    Vector<Integer> hanzahl = feld.getRabbitNumbers();
    int hn = 0;
    if (hanzahl != null && hanzahl.size() > 0) {
      hn = hanzahl.lastElement().intValue();
    }
    haseAnzL.setText("" + hn);

    Vector<Integer> janzahl = feld.getHunterNumbers();
    int jn = 0;
    if (janzahl != null && janzahl.size() > 0) {
      jn = janzahl.lastElement().intValue();
    }
    jagdAnzL.setText("" + jn);
    
    fieldView.repaint();

    pview.setFeld(feld);
    pview.repaint();
  }

  /**
   * Eine grafische Darstellung des Feldes als Rechteck. Das ist eine
   * sogenannte innere Klasse (Klasse definiert in einer Klasse) die uns eine
   * Komponente fï¿½r die grafische Benutzerschnittstelle definiert. Diese
   * Komponente zeigt das Feld an. Hierbei handelt es sich um
   * fortgeschrittenes Programmieren - fÃ¼r das Projekt mÃ¼ssen Sie das nicht
   * unbedingt verstehen.
   */
  private class FieldView extends JPanel {

    private int gridWidth, gridHeight;

    private int xScale, yScale;

    Dimension size;

    private Graphics g;

    private Image fieldImage;

    /**
     * Erzeuge neue graphische Komponente zur Felddarstellung.
     */
    public FieldView(int height, int width) {
      gridHeight = height;
      gridWidth = width;
      size = new Dimension(0, 0);
    }

    /**
     * Bevorzugte GrÃ¶ÃŸe fÃ¼r den GUI Manager.
     */
    public Dimension getPreferredSize() {
      return new Dimension(gridWidth * 25,
          gridHeight * 25);
    }

    /**
     * Vorbereitung fÃ¼r die graphische Darstellung des Feldes.
     * Hier ist vor allem die Skalierung wichtig.
     */
    public void preparePaint() {

      if (size != getSize()) {
        size = getSize();
      }
      xScale = GRID_VIEW_SCALING_FACTOR;
      yScale = GRID_VIEW_SCALING_FACTOR;
      int weite = gridWidth * GRID_VIEW_SCALING_FACTOR;
      int hoehe = gridHeight * GRID_VIEW_SCALING_FACTOR;

      fieldImage = fieldView.createImage(weite, hoehe);
      g = fieldImage.getGraphics();
    }

    /**
     * FÃ¼lle eine Positionim Feld mit einer Farbe
     */
    public void drawMark(int x, int y, Color color) {
      g.setColor(color);
      g.fillRect(x * xScale, y * yScale, xScale - 1, yScale - 1);
    }

    /*
     * Zeichne ein Image an eine Feldposition
     */
    public void drawImage(int x, int y, Image image) {
      // g.setColor(color);
      // g.fillRect(x * xScale, y * yScale, xScale-1, yScale-1);
      g.drawImage(image, x * xScale, y * yScale, xScale - 1, yScale - 1,
          null);
    }

    /**
     * Die Felddarstellung wird neu angezeigt. Das Feldbild wird neu gezeichnet
     */
    public void paintComponent(Graphics g) {
      g.setColor(Color.white);
      g.fillRect(0, 0, size.width, size.height);
      if (fieldImage != null) {
        g.drawImage(fieldImage, 0, 0, null);
      }
    }
  }

  /**
   * Klasse zur Darstellung des Populationsverlaufes.
   * @author Frank LeÃŸke
   *
   */
  class PopulationView extends JPanel {

    private Image populationImage;

    private int width;

    private Dimension size;

    private Feld feld;
    private int scale;

    int index = -1;

    private final int yZero = 100;

    public PopulationView(int w) {
      width = GRID_VIEW_SCALING_FACTOR * w;
      size = new Dimension(width, 120);
      if (simulator.getHeight()> simulator.getWidth()) {
        scale = simulator.getHeight()/3;
      } else
        scale = simulator.getWidth()/3;
      // populationImage = t.createImage(size.width, size.height);
    }

    private void setFeld(Feld f) {
      feld = f;
      index++;
    }



    public Dimension getPreferredSize() {
      return new Dimension(width, yZero + 10);
    }


    public final synchronized void update(Graphics g) {
      Graphics grafik = populationImage.getGraphics();
      paint(grafik);
      g.drawImage(populationImage, 0, 0, null);
    }

    public void paint(Graphics g) {
      g.setColor(getBackground());
      g.fillRect(0, 0, size.width, size.height);
      int x1 = 0, y1 = 0;
      if (feld != null) {
        Vector<Integer> panzahl = feld.getPlantNumbers();
        double k = (double) size.width / schritte;
        for (int i = 0; i < panzahl.size(); i++) {
          g.setColor(Color.green);
          g.drawLine(x1, y1, (int) (k * (i + 1)), yZero
              - panzahl.get(i) / scale);
          x1 = (int) (k * (i + 1));
          y1 = yZero - panzahl.get(i) / scale;
          // System.out.println("(" + x1 + "," + y1 + ")");
        }
        Vector<Integer> hanzahl = feld.getRabbitNumbers();
        x1 = 0;
        y1 = 0;
        for (int i = 0; i < hanzahl.size(); i++) {
          g.setColor(Color.DARK_GRAY);
          g.drawLine(x1, y1, (int) (k * (i + 1)), yZero
              - hanzahl.get(i) / scale);
          x1 = (int) (k * (i + 1));
          y1 = yZero - hanzahl.get(i) / scale;
        }
        Vector<Integer> fanzahl = feld.getFoxNumbers();
        x1 = 0;
        y1 = 0;
        for (int i = 0; i < fanzahl.size(); i++) {
          g.setColor(Color.RED);
          g.drawLine(x1, y1, (int) (k * (i + 1)), yZero
              - fanzahl.get(i) / scale);
          x1 = (int) (k * (i + 1));
          y1 = yZero - fanzahl.get(i) / scale;
        }
        Vector<Integer> janzahl = feld.getHunterNumbers();
        x1 = 0;
        y1 = 0;
        for (int i = 0; i < janzahl.size(); i++) {
          g.setColor(Color.BLUE);
          g.drawLine(x1, y1, (int) (k * (i + 1)), yZero
              - janzahl.get(i) / scale);
          x1 = (int) (k * (i + 1));
          y1 = yZero - janzahl.get(i) / scale;
        }
      }
    }

  }

  class OptionFrame extends JFrame implements ActionListener, ChangeListener {
    JPanel kohlOpt;

    JTextField kohlAge, haseAge, hasePaar, haseWurf, fuchsJagd, fuchsPaar,
        fuchsAge, fuchsWurf;

    JPanel haseOpt, fuchsOpt;

    JButton ok, cancel;

    JTextField fBreite, fHoehe;

    JSlider zoom;

    public OptionFrame() {
      GridBagLayout gb = new GridBagLayout();
      GridBagConstraints c = new GridBagConstraints();

      setFont(new Font("Helvetica", Font.PLAIN, 14));
      setTitle("Optionen");
      setLayout(gb);

      // c.gridx = 0;
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.weightx = 1.0;
      c.weighty = 1.0;
      c.fill = GridBagConstraints.HORIZONTAL;
      // c.ipadx = 1;
      // c.ipady = 1;
      c.anchor = GridBagConstraints.WEST;

      JPanel feldOpt = new JPanel();
      FlowLayout flow = new FlowLayout();
      flow.setAlignment(FlowLayout.LEADING);
      feldOpt.setLayout(flow);
      TitledBorder tb = BorderFactory.createTitledBorder("Feld");
      tb.setTitleFont(getFont().deriveFont(Font.BOLD));
      feldOpt.setBorder(tb);
      JLabel feldB = new JLabel("Breite:");
      feldOpt.add(feldB);
      fBreite = new JTextField(3);
      fBreite.setText(""+simulator.getWidth());
      feldOpt.add(fBreite);
      JLabel feldH = new JLabel("Höhe");
      feldOpt.add(feldH);
      fHoehe = new JTextField(3);
      fHoehe.setText(""+simulator.getHeight());
      feldOpt.add(fHoehe);

      Hashtable zoomLabels = new Hashtable();
      zoomLabels.put(5, new JLabel("klein"));
      zoomLabels.put(25, new JLabel("groß"));

      zoom = new JSlider(5, 25, 15);
      zoom.setPaintTicks(false);
      zoom.setMajorTickSpacing(1);
      zoom.addChangeListener(this);
      zoom.setLabelTable(zoomLabels);
      zoom.setPaintLabels(true);
      feldOpt.add(zoom);

      gb.setConstraints(feldOpt, c);
      this.getContentPane().add(feldOpt);

      // setLayout(new GridLayout(3,1));
      kohlOpt = new JPanel();
      FlowLayout fl = new FlowLayout();
      fl.setAlignment(FlowLayout.LEADING);
      kohlOpt.setLayout(fl);
      TitledBorder b1 = BorderFactory.createTitledBorder("Kohl");
      b1.setTitleColor(Color.green);
      b1.setTitleFont(getFont().deriveFont(Font.BOLD));
      kohlOpt.setBorder(b1);
      // kohlOpt.setBorder(new Border());
      JLabel kohlL = new JLabel("maximales Kohlalter:");
      kohlAge = new JTextField(3);
      kohlAge.setText("" + Kohl.MAX_ALTER);
      kohlAge.addActionListener(this);
      kohlOpt.add(kohlL);
      kohlOpt.add(kohlAge);
      gb.setConstraints(kohlOpt, c);
      this.getContentPane().add(kohlOpt);

      haseOpt = new JPanel();
      FlowLayout fl2 = new FlowLayout();
      fl2.setAlignment(FlowLayout.LEADING);
      haseOpt.setLayout(fl2);
      TitledBorder b2 = BorderFactory.createTitledBorder("Hase");
      b2.setTitleColor(Color.darkGray);
      b2.setTitleFont(getFont().deriveFont(Font.BOLD));
      haseOpt.setBorder(b2);
      JLabel haseAgeL = new JLabel("maximales Hasenalter");
      haseAge = new JTextField(3);
      haseAge.setText(""+Hase.MAX_ALTER);
      haseOpt.add(haseAgeL);
      haseOpt.add(haseAge);
      JLabel hasePaarL = new JLabel("Paarungswahrscheinlichkeit:");
      haseOpt.add(hasePaarL);
      hasePaar = new JTextField(3);
      hasePaar.setText(""+Hase.PAARUNGS_WAHRSCHEINLICHKEIT);
      haseOpt.add(hasePaar);
      JLabel maxKids = new JLabel("maximale Wurfgröße:");
      haseOpt.add(maxKids);
      haseWurf = new JTextField(3);
      haseWurf.setText(""+Hase.MAX_BRUT);
      haseOpt.add(haseWurf);
      // c.gridheight = GridBagConstraints.RELATIVE;
      gb.setConstraints(haseOpt, c);
      this.getContentPane().add(haseOpt);

      fuchsOpt = new JPanel();
      FlowLayout fl3 = new FlowLayout();
      fl3.setAlignment(FlowLayout.LEADING);
      fuchsOpt.setLayout(fl3);
      TitledBorder b3 = BorderFactory.createTitledBorder("Fuchs");
      b3.setTitleColor(Color.red);
      b3.setTitleFont(getFont().deriveFont(Font.BOLD));
      fuchsOpt.setBorder(b3);
      JLabel fuchsAgeL = new JLabel("maximales Fuchsalter");
      fuchsAge = new JTextField(3);
      fuchsAge.setText(""+Fuchs.MAX_ALTER);
      fuchsOpt.add(fuchsAgeL);
      fuchsOpt.add(fuchsAge);
      JLabel fuchsPaarL = new JLabel("Paarungswahrscheinlichkeit:");
      fuchsOpt.add(fuchsPaarL);
      fuchsPaar = new JTextField(3);
      fuchsPaar.setText(""+Fuchs.PAARUNGS_WAHRSCHEINLICHKEIT);
      fuchsOpt.add(fuchsPaar);
      JLabel maxKidsF = new JLabel("maximale Wurfgröße:");
      fuchsOpt.add(maxKidsF);
      fuchsWurf = new JTextField(3);
      fuchsWurf.setText(""+Fuchs.MAX_BRUT);
      fuchsOpt.add(fuchsWurf);
      JLabel jagdL = new JLabel("Jagdwahrscheinlichkeit");
      fuchsOpt.add(jagdL);
      fuchsJagd = new JTextField(3);
      fuchsJagd.setText(""+Fuchs.JAGD_WAHRSCHEINLICHKEIT);
      fuchsOpt.add(fuchsJagd);
      c.gridheight = GridBagConstraints.RELATIVE;
      gb.setConstraints(fuchsOpt, c);
      this.getContentPane().add(fuchsOpt);

      JPanel buttonP = new JPanel();
      ok = new JButton("Ok");
      ok.addActionListener(this);
      buttonP.add(ok);
      cancel = new JButton("Cancel");
      cancel.addActionListener(this);
      buttonP.add(cancel);
      c.gridheight = GridBagConstraints.REMAINDER;
      gb.setConstraints(buttonP, c);
      this.getContentPane().add(buttonP);

      setSize(780, 350);
    }

    public void actionPerformed(ActionEvent arg0) {
      if (arg0.getSource() == cancel) {
        this.setVisible(false);
        return;
      }
      if (arg0.getSource() == ok) {
        try {
          Kohl.MAX_ALTER = Integer.parseInt(kohlAge.getText());
          Hase.MAX_ALTER = Integer.parseInt(haseAge.getText());
          Hase.PAARUNGS_WAHRSCHEINLICHKEIT = Double
              .parseDouble(hasePaar.getText());
          Hase.MAX_BRUT = Integer.parseInt( haseWurf.getText());
          Fuchs.MAX_ALTER = Integer.parseInt(fuchsAge.getText());
          Fuchs.PAARUNGS_WAHRSCHEINLICHKEIT = Double.parseDouble(fuchsPaar.getText());
          Fuchs.MAX_BRUT = Integer.parseInt(fuchsWurf.getText());
          Fuchs.JAGD_WAHRSCHEINLICHKEIT = Double.parseDouble(fuchsJagd.getText());
          int nh = Integer.parseInt(fHoehe.getText());
          int nb = Integer.parseInt(fBreite.getText());
          if (nh != simulator.getHeight() || nb != simulator.getWidth())
            simulator.changeDimension(nb, nh);
            
        } catch (Exception e) {
        }
        this.setVisible(false);
        return;
      }

    }

    public void stateChanged(ChangeEvent arg0) {
      if (arg0.getSource() == zoom) {
        int v = zoom.getValue();
        GRID_VIEW_SCALING_FACTOR = v;
      }
    }

  }

  public void actionPerformed(ActionEvent arg0) {
    if (arg0.getSource() == startB) {
      schritte = 100;
      try {
        schritte = Integer.parseInt(anzahlFeld.getText());
      } catch (Exception e) {
      }
      //-----FD-----
      //Checkbox für Jäger-Spawn
      if (jagdbox.isSelected()==false){
    	  simulator.setJaegerzahl(0);
      }
      else if (jagdbox.isSelected()==true){
    	  simulator.setJaegerzahl(0.002);
      }
      // simulator = new Simulator(60,90);
      simulator.setSchrittZahl(schritte);
      simulator.simuliere();
    }
    if (arg0.getSource() == stopB) {
      schritte = 0;
      simulator.halte();
    }
    if (arg0.getSource() == exitB) {
      System.exit(0);
    }
    if (arg0.getSource() == optionB) {
      if (optionFrame == null) {
        optionFrame = new OptionFrame();
      }
      optionFrame.setVisible(true);
    }
  }

  public void stateChanged(ChangeEvent arg0) {
    if (arg0.getSource() == waitSlider) {
      int v = waitSlider.getValue();
      simulator.setWaitTime(v);
    }

  }

}
