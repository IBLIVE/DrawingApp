// Assignment #7, CSE205, Arizona State University 
// Name: Iskander Kushbay
// StudentID: 1223885234
// Lecture time: 4:30
// Description: SketchPane class which draws figures

import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import java.util.*;

public class SketchPane extends BorderPane {
	
	//Task 1: Declare all instance variables listed in UML diagram
	private ArrayList<Shape> shapeList;
	private ArrayList<Shape> tempList;
	private Button undoButton;
	private Button eraseButton;
	private Label fillColorLabel;
	private Label strokeColorLabel;
	private Label strokeWidthLabel;
	private ComboBox<String> fillColorCombo;
	private ComboBox<String> strokeColorCombo;
	private ComboBox<String> strokeWidthCombo;
	private RadioButton radioButtonLine;
	private RadioButton radioButtonRectangle;
	private RadioButton radioButtonCircle;
	private Pane sketchCanvas;
	private Color[] colors;
	private String[] strokeWidth;
	private String[] colorLabels;
	private Color currentStrokeColor;
	private Color currentFillColor;
	private int currentStrokeWidth;
	private Line line;
	private Circle circle;
	private Rectangle rectangle;
	private double x1;
	private double y1;

	
	//Task 2: Implement the constructor
	public SketchPane() {
		// Colors, labels, and stroke widths that are available to the user
		colors = new Color[] {Color.BLACK, Color.GREY, Color.YELLOW, Color.GOLD, Color.ORANGE, Color.DARKRED, Color.PURPLE, Color.HOTPINK, Color.TEAL, Color.DEEPSKYBLUE, Color.LIME} ;
        colorLabels = new String[] {"black", "grey", "yellow", "gold", "orange", "dark red", "purple", "hot pink", "teal", "deep sky blue", "lime"};
        fillColorLabel = new Label("Fill Color:");
        strokeColorLabel = new Label("Stroke Color:");
        strokeWidthLabel = new Label("Stroke Width:");
        strokeWidth = new String[] {"1", "3", "5", "7", "9", "11", "13"};    
        
        //initialization of objects 
        
        shapeList = new ArrayList<Shape>();
        tempList = new ArrayList<Shape>();
        
        currentFillColor = colors[0];
        currentStrokeColor = colors[0];
        currentStrokeWidth = 1;
        
        fillColorCombo = new ComboBox<String>();
        fillColorCombo.getItems().addAll(colorLabels);
        strokeColorCombo = new ComboBox<String>();
        strokeColorCombo.getItems().addAll(colorLabels);
        strokeWidthCombo = new ComboBox<String>();
        strokeWidthCombo.getItems().addAll(strokeWidth);
        EventHandler<ActionEvent> colorHandler = new ColorHandler();
        fillColorCombo.setOnAction(colorHandler);
        strokeColorCombo.setOnAction(colorHandler);
        EventHandler<ActionEvent> widthHandler = new WidthHandler();
        strokeWidthCombo.setOnAction(widthHandler);
        
        undoButton = new Button("Undo");
        eraseButton = new Button("Erase");
        
        ToggleGroup toggle = new ToggleGroup();
        radioButtonRectangle = new RadioButton("Recatangle");
        radioButtonCircle = new RadioButton("Circle");
        radioButtonLine = new RadioButton("Line");
        radioButtonRectangle.setToggleGroup(toggle);
        radioButtonLine.setToggleGroup(toggle);
        radioButtonCircle.setToggleGroup(toggle);
        toggle.selectToggle(radioButtonLine);
        
        EventHandler<ActionEvent> buttonHandler = new ButtonHandler();
        undoButton.setOnAction(buttonHandler);
        eraseButton.setOnAction(buttonHandler);
        
        sketchCanvas = new Pane();
        BackgroundFill bgw = new BackgroundFill(Color.WHITE, null, null);
        sketchCanvas.setBackground(new Background(bgw));
        
        HBox boxHolder = new HBox(40);
        boxHolder.setPrefWidth(40);
        boxHolder.setPrefHeight(20);
        boxHolder.setAlignment(Pos.CENTER);
        BackgroundFill bgg = new BackgroundFill(Color.LIGHTGREY, null, null);
        boxHolder.setBackground(new Background(bgg));
        boxHolder.getChildren().addAll(fillColorLabel, fillColorCombo, 
        							strokeWidthLabel, strokeWidthCombo,
        							strokeColorLabel,strokeColorCombo);
        HBox buttonHolder = new HBox(40);
        buttonHolder.setPrefWidth(40);
        buttonHolder.setPrefHeight(20);
        buttonHolder.setAlignment(Pos.CENTER);
        buttonHolder.setBackground(new Background(bgg));
        buttonHolder.getChildren().addAll(radioButtonLine, radioButtonRectangle,
        								radioButtonCircle, undoButton, eraseButton);
        this.setCenter(sketchCanvas);
        this.setTop(boxHolder);
        this.setBottom(buttonHolder);
        
        x1 = 0;
        y1 = 0;
        
        EventHandler<MouseEvent> mouseHandler = new MouseHandler();
        sketchCanvas.addEventHandler(MouseEvent.ANY, mouseHandler);
    }

	private class MouseHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			// TASK 3: Implement the mouse handler for Circle and Line
			// Rectangle Example given!
			if (radioButtonRectangle.isSelected()) { //creating a rectangle
				//Mouse is pressed
				if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
					x1 = event.getX();
					y1 = event.getY();
					rectangle = new Rectangle();
					rectangle.setX(x1);
					rectangle.setY(y1);
					shapeList.add(rectangle);
					rectangle.setFill(Color.WHITE);
					rectangle.setStroke(Color.BLACK);	
					sketchCanvas.getChildren().add(rectangle);
				}
				//Mouse is dragged
				else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
					if (event.getX() - x1 <0) 
						rectangle.setX(event.getX());
					if (event.getY() - y1 <0) 
						rectangle.setY(event.getY());
					rectangle.setWidth(Math.abs(event.getX() - x1));
					rectangle.setHeight(Math.abs(event.getY() - y1));
				}
				//Mouse is released
				else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
					rectangle.setFill(currentFillColor);
					rectangle.setStroke(currentStrokeColor);
					rectangle.setStrokeWidth(currentStrokeWidth);
				}
			}
			
			if (radioButtonCircle.isSelected()) { //creating a circle
				//Mouse is pressed
				if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
					x1 = event.getX();
					y1 = event.getY();
					circle = new Circle();
					circle.setCenterX(x1);
					circle.setCenterY(y1);
					shapeList.add(circle);
					circle.setFill(Color.WHITE);
					circle.setStroke(Color.BLACK);	
					sketchCanvas.getChildren().add(circle);
				}
				//Mouse is dragged
				else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
					double radius = Math.sqrt(Math.pow((event.getX()-x1),2) + 
							Math.pow((event.getY()-y1), 2));
					circle.setRadius(radius);

				}
				//Mouse is released
				else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
					circle.setFill(currentFillColor);
					circle.setStroke(currentStrokeColor);
					circle.setStrokeWidth(currentStrokeWidth);
				}
			}
			
			if (radioButtonLine.isSelected()) { //creating a line
				//Mouse is pressed
				if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
					x1 = event.getX();
					y1 = event.getY();
					line = new Line();
					line.setStartX(x1);
					line.setStartY(y1);
					line.setEndX(x1);
					line.setEndY(y1);
					shapeList.add(line);	
					sketchCanvas.getChildren().add(line);
				}
				//Mouse is dragged
				else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
					line.setEndX(event.getX());
					line.setEndY(event.getY());

				}
				//Mouse is released
				else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
					line.setStroke(currentStrokeColor);
					line.setStrokeWidth(currentStrokeWidth);
				}
			}
		}
	}
		
	private class ButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			// TASK 4: Implement the button handler
			if ((event.getSource()==undoButton)&&(shapeList.size()>0)) { //removes the last added object
				shapeList.remove(shapeList.size()-1);
				sketchCanvas.getChildren().remove(shapeList.size());
			} 
			else if((event.getSource()==eraseButton)) { //clears canvas
				for(int i = 0; i < shapeList.size(); i++) {
					tempList.add(shapeList.get(i));
				}
				sketchCanvas.getChildren().clear();
				shapeList.clear();
			} else if(shapeList.size()==0) { //undoes the last action, be it erase or addition of a new object
				for(int i = 0; i < tempList.size(); i++) {
					shapeList.add(tempList.get(i));
					sketchCanvas.getChildren().add(shapeList.get(i));
				}
				tempList.clear();
			}
		}
	}

	private class ColorHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) { //changing of stroke's and fill's colors
			// TASK 5: Implement the color handler
			//int index = fillColorCombo.getSelectionModel().getSelectedIndex();
			Color clr1 = colors[fillColorCombo.getSelectionModel().getSelectedIndex()];
			currentFillColor = clr1;
			int strokeclr = strokeColorCombo.getSelectionModel().getSelectedIndex();
			Color clr2 = colors[strokeclr];
			currentStrokeColor = clr2;
		}
	}
	
	private class WidthHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event){  //changing of stroke's width
			// TASK 6: Implement the stroke width handler
			int index = strokeWidthCombo.getSelectionModel().getSelectedIndex();
			currentStrokeWidth = Integer.parseInt(strokeWidth[index]);
		}
	}
}