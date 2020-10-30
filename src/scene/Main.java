package scene;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import shapes.*;
import shapes.Point;

public class Main extends Applet implements ActionListener, ItemListener, MouseListener{
	Button zoomInButton;
	Button zoomOutButton;
	
	shapes.Grid grid;
	//Environment variables
	private shapes.View v; 
	private shapes.Pixel pixels;
	
	private Choice tranformationChoice, objectChoice, wingTypeChoice;
	
	private Checkbox scaleDownChoiceCheckbox;
	
	private Label xCenterLabel, yCenterLabel, birdHeightLabel, birdWidthLabel;
	private TextField xCenterInput, yCenterInput, birdHeightInput, birdWidthInput;
	
	private Label moveXLabel ,moveYLabel, thetaLabel, sxLabel, syLabel;
	private TextField moveXInput, moveYInput, thetaInput, sxInput, syInput;
	
	Button drawButton, moveButton, scaleButton, rotateButton;
	
	private boolean start;
	
	objects.Bird bird;
	shapes.Point pivotPoint;
	double theta;
	boolean scaleUp;
	int xCenter, yCenter, birdWidth, birdHeight, xMoveBy, yMoveBy, sx, sy, wingType;
	
	int drawType;
	
	public void init()
	{	
		this.setSize(new Dimension(1920,1080));
		this.v = new shapes.View(this.getHeight(), this.getWidth(), 10, 10);
		this.pixels = new shapes.Pixel(v);
		
		Color gridColor = new Color(195, 195, 195);
		grid = new shapes.Grid(gridColor);
		
		
		Font f = new Font("Arial", Font.BOLD, 16);
		this.setFont(f);
		
		start = true;
		
		zoomInButton = new Button("Zoom in");
		add(zoomInButton);
		zoomInButton.addActionListener(this);
		
		zoomOutButton = new Button("Zoom Out");
		add(zoomOutButton);
		zoomOutButton.addActionListener(this);
		
		this.tranformationChoice = new Choice();
		this.tranformationChoice.add("Select Choice");
		this.tranformationChoice.add("Draw");
		this.tranformationChoice.add("Translate");
		this.tranformationChoice.add("Scale");
		this.tranformationChoice.add("Rotate");
		
		add(this.tranformationChoice);
		this.tranformationChoice.addItemListener(this);
		
		//Components for drawing the bird
		this.xCenterLabel = new Label("xCentre");
		add(this.xCenterLabel);
		this.xCenterInput = new TextField("0");
		add(this.xCenterInput);
		
		
		this.yCenterLabel = new Label("yCentre");
		add(this.yCenterLabel);
		this.yCenterInput = new TextField("0");
		add(this.yCenterInput);
		
		this.birdHeightLabel = new Label("Bird Height");
		this.birdHeightInput = new TextField("40");
		add(this.birdHeightLabel);
		add(this.birdHeightInput);
		
		this.birdWidthLabel = new Label("Bird Width");
		this.birdWidthInput = new TextField("80");
		add(this.birdWidthLabel);
		add(this.birdWidthInput);
		
		//***********************************************************************
		this.wingTypeChoice = new Choice();
		this.wingTypeChoice.add("Wing Type");
		this.wingTypeChoice.add("1");
		this.wingTypeChoice.add("2");
		//this.wingTypeChoice.add("3");
		//this.wingTypeChoice.add("4");
		
		add(this.wingTypeChoice);
		this.wingTypeChoice.addItemListener(this);
		
		//Draw button
		this.drawButton = new Button("Draw");
		add(this.drawButton);
		this.drawButton.addMouseListener(this);
		this.drawButton.addActionListener(this);
		
		this.xCenter = this.yCenter = 0;
		this.birdHeight = 40;
		this.birdWidth = 80;
		this.wingType = 1;
		this.bird = new objects.Bird(v, xCenter, yCenter, birdHeight, birdWidth, wingType);
		
		//Transformation components
		this.moveXLabel = new Label("Move X by");
		add(this.moveXLabel);
		this.moveXInput = new TextField("10");
		add(this.moveXInput);
		this.moveYLabel = new Label("Move Y by");
		add(this.moveYLabel);
		this.moveYInput = new TextField("-5");
		add(this.moveYInput);
		this.moveButton = new Button("Move");
		add(this.moveButton);
		this.moveButton.addMouseListener(this);
		this.moveButton.addActionListener(this);
		
		this.xMoveBy = 20;
		this.yMoveBy = -5;
		
		this.pivotPoint = new shapes.Point(v);
		//Scale Menu
		
		this.sxLabel = new Label("X scale Factor");
		this.syLabel = new Label("Y Scale Factor");
		this.sxInput = new TextField("2");
		this.syInput = new TextField("2");
		this.scaleDownChoiceCheckbox = new Checkbox("Check to scale down");
		this.scaleDownChoiceCheckbox.addItemListener(this);
		this.scaleButton = new Button("Scale");
		this.scaleButton.addMouseListener(this);
		this.scaleButton.addActionListener(this);
		this.scaleUp = true;
		this.sx = 2;
		this.sy = 2;
		
		
		add(this.sxLabel);
		add(this.sxInput);
		add(this.syLabel);
		add(this.syInput);
		add(this.scaleDownChoiceCheckbox);
		add(this.scaleButton);
		this.scaleButton.addActionListener(this);
		
		//Rotation Components
		this.thetaLabel = new Label("Theat(In Degrees)");
		add(this.thetaLabel);
		this.thetaInput = new TextField("45.0");
		add(this.thetaInput);
		this.rotateButton = new Button("Rotate");
		this.add(this.rotateButton);
		this.rotateButton.addActionListener(this);
		this.rotateButton.addMouseListener(this);
		this.theta = 45.0;
		addMouseListener(this);
	}
	
	private void hideAllTransformations() {
		//Draw Components
		this.removeDrawMenu();
		
		//Transformation Components
		this.removeTranslateMenu();

		//Scale components
		this.removeScaleMenu();
		
		//Rotate Components
		this.removeRotationMenu();
	}
	
	public void paint(Graphics g){
		
		Color bgColor = new Color(184, 223, 230);
		this.grid.drawGrid(g, v);
		this.setBackground(bgColor);
		pixels.updateAllPixels(bgColor);

		if(start) {
			start = false;
			this.hideAllTransformations();
		}
		
		if(drawType==1) {
			this.bird.draw(g, this.v, this.pixels);
		}
		else if(drawType==2) {
			this.bird.translate(v, this.xMoveBy, this.yMoveBy);
			this.bird.draw(g, this.v, this.pixels);
			
		}
		else if(drawType==3) {
			this.pivotPoint = this.bird.getCenter(v);
			this.bird.scale(this.v, this.pivotPoint, this.sx, this.sy, this.scaleUp);
			this.bird.draw(g, this.v, this.pixels);
		}
		else if(drawType==4) {
			this.pivotPoint = this.bird.getCenter(v);
			this.bird.rotateAndDraw(g, this.v, this.pixels, this.pivotPoint, this.theta);
		}
		/*
		shapes.Ellipse el = new shapes.Ellipse(v, 5, 5, 40, 20);
		pivotPoint = el.getCenter(v);
		el.drawRotatedBoundary(g, v, pixels, pivotPoint, 45);
		pivotPoint = el.getCenter(v);
		el.drawRotatedBoundary(g, v, pixels, pivotPoint, 45);
		*/
	}
	
		
	public void actionPerformed(ActionEvent e)
	{
		
		if (e.getSource() == zoomInButton) {
			onZoomIn();
		}
		else if(e.getSource() == zoomOutButton) {
			onZoomOut();
		}
		
	}
	
	private void onZoomIn() {
		if(this.v.scaleup()) {
			repaint();
		}
	}
	
	private void onZoomOut() {
		if(this.v.scaledown()) {
			repaint();
		}
	}
	public void itemStateChanged(ItemEvent ie) {
		if(ie.getSource() == this.tranformationChoice) {
			String ch = this.tranformationChoice.getSelectedItem();
			if(ch=="Draw") {
				this.removeTranslateMenu();
				this.removeScaleMenu();
				this.removeRotationMenu();
				this.addDrawMenu();
			}
			if(ch=="Translate") {
				this.removeDrawMenu();
				this.removeScaleMenu();
				this.removeRotationMenu();
				this.addTranslateMenu();
			}
			else if(ch=="Scale") {
				this.removeDrawMenu();
				this.removeTranslateMenu();
				this.removeRotationMenu();
				this.addScaleMenu();
			}
			else if(ch=="Rotate") {
				this.removeDrawMenu();
				this.removeScaleMenu();
				this.removeTranslateMenu();
				this.addRotationMenu();
			}
		}
		if(ie.getSource() == this.wingTypeChoice) {
			String ch = this.wingTypeChoice.getSelectedItem();
			if(ch=="1") {
				this.wingType = 1;
			}
			if(ch=="2") {
				this.wingType = 2;
			}
			else {
				this.wingType = 1;
			}
		}
		if(ie.getSource() == this.scaleDownChoiceCheckbox) {
			this.scaleUp = !this.scaleDownChoiceCheckbox.getState();
		}
	}
	
	private void addDrawMenu() {
		int xpos=10, ypos=40;
		this.xCenterLabel.setLocation(xpos,ypos);
		this.xCenterLabel.setVisible(true);
		
		xpos += this.xCenterLabel.getSize().getWidth()+10;
		this.xCenterInput.setLocation(xpos,ypos);
		this.xCenterInput.setVisible(true);
		
		xpos += this.xCenterInput.getSize().getWidth()+10;
		this.yCenterLabel.setLocation(xpos,ypos);
		this.yCenterLabel.setVisible(true);
		
		xpos += this.yCenterLabel.getSize().getWidth()+10;
		this.yCenterInput.setLocation(xpos,ypos);
		this.yCenterInput.setVisible(true);
		
		xpos += this.yCenterInput.getSize().getWidth()+10;
		this.birdHeightLabel.setLocation(xpos, ypos);
		this.birdHeightLabel.setVisible(true);
		
		xpos += this.birdHeightLabel.getSize().getWidth()+10;
		this.birdHeightInput.setLocation(xpos, ypos);
		this.birdHeightInput.setVisible(true);
		
		xpos += this.birdHeightInput.getSize().getWidth()+10;
		this.birdWidthLabel.setLocation(xpos, ypos);
		this.birdWidthLabel.setVisible(true);
		
		xpos += this.birdWidthLabel.getSize().getWidth()+10;
		this.birdWidthInput.setLocation(xpos, ypos);
		this.birdWidthInput.setVisible(true);
		
		xpos += this.birdWidthInput.getSize().getWidth()+10;
		this.wingTypeChoice.setLocation(xpos, ypos);
		this.wingTypeChoice.setVisible(true);
		
		xpos += this.wingTypeChoice.getSize().getWidth()+10;
		this.drawButton.setLocation(xpos, ypos);
		this.drawButton.setVisible(true);
		//this.update(this.getGraphics());
	}
	
	private void removeDrawMenu() {
		this.xCenterLabel.setVisible(false);
		this.xCenterInput.setVisible(false);
		this.yCenterLabel.setVisible(false);
		this.yCenterInput.setVisible(false);
		this.birdHeightLabel.setVisible(false);
		this.birdHeightInput.setVisible(false);
		this.birdWidthLabel.setVisible(false);
		this.birdWidthInput.setVisible(false);
		this.drawButton.setVisible(false);
		this.wingTypeChoice.setVisible(false);
	}

	public void addTranslateMenu() {
		int xpos=10;
		this.moveXLabel.setLocation(xpos, 40);
		this.moveXLabel.setVisible(true);
		
		xpos += (int)this.moveXLabel.getSize().getWidth()+10;
		this.moveXInput.setLocation(xpos, 40);
		this.moveXInput.setVisible(true);
		
		xpos += (int)this.moveXInput.getSize().getWidth()+10;
		this.moveYLabel.setLocation(xpos, 40);
		this.moveYLabel.setVisible(true);
		
		xpos += (int)this.moveYLabel.getSize().getWidth()+10;
		this.moveYInput.setLocation(xpos, 40);
		this.moveYInput.setVisible(true);
		
		xpos += (int)this.moveYInput.getSize().getWidth()+10;
		this.moveButton.setLocation(xpos, 40);
		this.moveButton.setVisible(true);
		
	}
	public void removeTranslateMenu() {
		this.moveXLabel.setVisible(false);
		this.moveYLabel.setVisible(false);
		this.moveXInput.setVisible(false);
		this.moveYInput.setVisible(false);
		this.moveButton.setVisible(false);
		
	}
	public void addScaleMenu() {
		int xpos=10, ypos=40;
		
		this.sxLabel.setLocation(xpos,ypos);
		this.sxLabel.setVisible(true);
		
		xpos += (int)this.sxLabel.getSize().getWidth()+10;
		this.sxInput.setLocation(xpos,ypos);
		this.sxInput.setVisible(true);
		
		xpos += (int)this.sxInput.getSize().getWidth()+10;
		this.syLabel.setLocation(xpos,ypos);
		this.syLabel.setVisible(true);
		
		xpos += (int)this.syLabel.getSize().getWidth()+10;
		this.syInput.setLocation(xpos,ypos);
		this.syInput.setVisible(true);
		
		xpos += (int)this.syInput.getSize().getWidth()+10;
		this.scaleDownChoiceCheckbox.setLocation(xpos,ypos);
		this.scaleDownChoiceCheckbox.setVisible(true);
		
		xpos += (int)this.scaleDownChoiceCheckbox.getSize().getWidth()+10;
		this.scaleButton.setLocation(xpos,ypos);
		this.scaleButton.setVisible(true);
	}
	public void removeScaleMenu() {
		this.sxLabel.setVisible(false);
		this.syLabel.setVisible(false);
		this.sxInput.setVisible(false);
		this.syInput.setVisible(false);
		this.scaleDownChoiceCheckbox.setVisible(false);
		this.scaleButton.setVisible(false);
	}
	
	public void addRotationMenu() {
		int xpos=10, ypos=40;
		
		this.thetaLabel.setLocation(xpos,ypos);
		this.thetaLabel.setVisible(true);
		
		xpos += (int)this.thetaLabel.getSize().getWidth()+10;
		this.thetaInput.setLocation(xpos,ypos);
		this.thetaInput.setVisible(true);
		
		xpos += (int)this.thetaInput.getSize().getWidth()+10;
		this.rotateButton.setLocation(xpos,ypos);
		this.rotateButton.setVisible(true);
	}
	
	public void removeRotationMenu() {
		this.thetaLabel.setVisible(false);
		this.thetaInput.setVisible(false);
		this.rotateButton.setVisible(false);
	}

	private void readDrawInputs() {
		this.xCenter = Integer.parseInt(this.xCenterInput.getText());
		this.yCenter = Integer.parseInt(this.yCenterInput.getText());
		this.birdHeight = Integer.parseInt(this.birdHeightInput.getText());
		this.birdWidth = Integer.parseInt(this.birdWidthInput.getText());
		this.wingType = Integer.parseInt(this.wingTypeChoice.getSelectedItem());
	}
	private void readTranslateInput() {
		this.xMoveBy = Integer.parseInt(this.moveXInput.getText());
		this.yMoveBy = Integer.parseInt(this.moveYInput.getText());
	}
	private void readScaleInput() {
		this.sx = Integer.parseInt(this.sxInput.getText());
		this.sy = Integer.parseInt(this.sxInput.getText());
		this.scaleUp = !this.scaleDownChoiceCheckbox.getState();
	}
	private void readRotateInput() {
		this.theta = Double.parseDouble(this.thetaInput.getText());
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		
		if(arg0.getSource() == this.drawButton) {
			this.readDrawInputs();
			this.bird.updateBird(v, xCenter, yCenter, birdHeight, birdWidth, wingType);
			this.drawType = 1;
			update(this.getGraphics());
			this.tranformationChoice.select("Draw");
		}
		if(arg0.getSource() == this.moveButton) {
			this.readTranslateInput();
			this.drawType = 2;
			update(this.getGraphics());
			this.tranformationChoice.select("Translate");
			this.xMoveBy = this.yMoveBy = 0;
		}
		if(arg0.getSource()==this.scaleButton) {
			this.readScaleInput();
			this.drawType = 3;
			update(this.getGraphics());
			this.tranformationChoice.select("Scale");
			this.sx = this.sy = 1;
		}
		if(arg0.getSource()==this.rotateButton) {
			System.out.println("Rotate Button Pressed");
			this.readRotateInput();
			this.drawType = 4;
			update(this.getGraphics());
			this.tranformationChoice.select("Rotate");
			this.theta = 0;
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
