/**
 * MapEditor, the main class that contains a whole component
 * @author Ren DING a1202524
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;

import java.io.*;
import java.util.*;

public class MapEditor {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable(){
			public void run() {
				MapEditor mapEditor = new MapEditor();
			}
		});
	}
	
	private MapFrame mapFrame;
	private Map map;
	private MapPanel mapPanel;
	
	private static final int DEFAULT_FRAME_WIDTH = 700;
	private static final int DEFAULT_FRAME_HEIGHT = 700;
	
	/**
	 * The constructor, create a main frame and add exit listener
	 */
	public MapEditor() {
		map = new MapImpl();

		mapFrame = new MapFrame();
		mapFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mapFrame.setVisible(true);
		mapFrame.addWindowListener(exitListener);
	}
	
	/**
	 * windowsExitListener, the program warn the user 
	 * and offer a choice of proceeding or cancelling
	 */
	WindowListener exitListener = new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
            int confirm = JOptionPane.showOptionDialog(mapFrame, "Are You Sure to Close Application?", "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            		if (confirm == 0) {
            			System.exit(0);
            		}
        }
    };
    
	/**
	 * The main frame of the map
	 */
	class MapFrame extends JFrame {
		MapFrame() {
			setTitle("MapEditor author: Ren DING a1202524");
			setSize(DEFAULT_FRAME_WIDTH,DEFAULT_FRAME_HEIGHT);
			
			this.setLayout(new BorderLayout());
			
			//add a menu bar
			MapMenu mapMenu = new MapMenu();
			add(mapMenu,BorderLayout.PAGE_START);
			
			//add a map panel
			mapPanel = new MapPanel();
			add(mapPanel);
			
			//add mapPanel as a listener to map
			map.addListener(mapPanel);
			mapPanel.setMap(map);
			
			//add a control bar
			ControlBar controlBar = new ControlBar();
			add(controlBar,BorderLayout.PAGE_END);
		}
	}
	
	/**
	 * The menu bar
	 */
	
	class MapMenu extends JMenuBar {
		// two menus
		private JMenu fileMenu;
		private JMenu editMenu;
		
		//four menu items on the file menu and
		//seven menu items on the edit menu
		private JMenuItem open;
		private JMenuItem save;
		private JMenuItem append;
		private JMenuItem quit;
		
		private JMenuItem newPlace;
		private JMenuItem newRoad;
		private JMenuItem setStart;
		private JMenuItem unsetStart;
		private JMenuItem setEnd;
		private JMenuItem unsetEnd;
		private JMenuItem delete;
		
		MapMenu() {
			//initialise all menu
			fileMenu = new JMenu("File");
			editMenu = new JMenu("Edit");
			
			//initialise all menu item
			open = new JMenuItem("Open...");
			save = new JMenuItem("Save...");
			append = new JMenuItem("Append...");
			quit = new JMenuItem("Quit");
			
			newPlace = new JMenuItem("New place...");
			newRoad = new JMenuItem("New road...");
			setStart = new JMenuItem("Set start");
			unsetStart = new JMenuItem("Unset start");
			setEnd = new JMenuItem("Set end");
			unsetEnd = new JMenuItem("Unset End");
			delete = new JMenuItem("Delete");
			
			//set menu item accelerator
			open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
			save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
			append.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.CTRL_MASK));
			quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.CTRL_MASK));

			//add listener to these menu items
			open.addActionListener(new OpenListener());
			save.addActionListener(new SaveListener());
			append.addActionListener(new AppendListener());
			quit.addActionListener(new QuitListener());
			newPlace.addActionListener(new NewPlaceListener());
			newRoad.addActionListener(new NewRoadListener());
			setStart.addActionListener(new SetStartListener());
			unsetStart.addActionListener(new UnsetStartListener());
			setEnd.addActionListener(new SetEndListener());
			unsetEnd.addActionListener(new UnsetEndListener());
			delete.addActionListener(new DeleteListener());
			
			//add menus to the menu bar
			this.add(fileMenu);
			this.add(editMenu);
						
			//add menu items to these menus
			fileMenu.add(open);
			fileMenu.add(save);
			fileMenu.add(append);
			fileMenu.add(quit);

			editMenu.add(newPlace);
			editMenu.add(newRoad);
			editMenu.add(setStart);
			editMenu.add(unsetStart);
			editMenu.add(setEnd);
			editMenu.add(unsetEnd);
			editMenu.add(delete);
		}
		
		/**
		 *  OpenListener Monitor the open menu item 
		 *  will pop a dialog and require user to choose a file to open
		 */
		class OpenListener implements ActionListener {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser openChooser = new JFileChooser(".");
				
				//file name filter, only .map can be displayed
				FileNameExtensionFilter filter = new FileNameExtensionFilter("map File","map");
				openChooser.setFileFilter(filter);
				
				int result = openChooser.showOpenDialog(mapFrame);
				
				if (result == JFileChooser.APPROVE_OPTION) {
					File fileIn = openChooser.getSelectedFile();
					try {
						FileReader reader = new FileReader(fileIn);
						MapReaderWriter mapRW = new MapReaderWriter();
						
						mapPanel.removeAll();
						
						map = new MapImpl();
						mapPanel.clear();
						map.addListener(mapPanel);
						mapPanel.setMap(map);
						
						mapRW.read(reader, map);
						reader.close();
					} catch(FileNotFoundException fne) {
						JOptionPane.showMessageDialog(mapFrame, "Map file cannot be found");
						//System.out.println("map file cannot be found:" + fne.getMessage());
					} catch(MapFormatException mfe) {
						JOptionPane.showMessageDialog(mapFrame, "Map format exception when open a map");
						//System.out.println("map format exception when open a map: " + mfe.getMessage());
					} catch(IOException ioe) {
						JOptionPane.showMessageDialog(mapFrame,"IOException when open a map");
						//System.out.println("IOException when open a map: " + ioe.getMessage());
					}
				} else if(result == JFileChooser.CANCEL_OPTION) {
					
				}
				
			}	
		}
		
		/**
		 *  SaveListener Monitor the save menu item 
		 *  will pop a dialog and require user to save a map to a file
		 */
		class SaveListener implements ActionListener {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser saveChooser = new JFileChooser(".");

				//file name filter, only .map can be displayed
				FileNameExtensionFilter filter = new FileNameExtensionFilter("map File","map");
				saveChooser.setFileFilter(filter);
				
				int result = saveChooser.showSaveDialog(mapFrame);
				
				if (result == JFileChooser.APPROVE_OPTION) {
					File fileOut = saveChooser.getSelectedFile();
					try {
						FileWriter writer = new FileWriter(fileOut);
						MapReaderWriter mapRW = new MapReaderWriter();
						mapRW.write(writer, map);
						writer.close();
					} catch(FileNotFoundException fne) {
						System.out.println("save map file cannot be found:" + fne.getMessage());
					} catch(IOException ioe) {
						System.out.println("IOException when save a map: " + ioe.getMessage());
					}
				} else if(result == JFileChooser.CANCEL_OPTION) {
					
				}
			}	
		}
		
		/**
		 *  AppendListener Monitor the append menu item 
		 *  will pop a dialog and require user to choose a file to append
		 */
		class AppendListener implements ActionListener {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser appendChooser = new JFileChooser(".");

				//file name filter, only .map can be displayed
				FileNameExtensionFilter filter = new FileNameExtensionFilter("map File","map");
				appendChooser.setFileFilter(filter);
				
				int result = appendChooser.showOpenDialog(mapFrame);
				
				if (result == JFileChooser.APPROVE_OPTION) {
					File fileIn = appendChooser.getSelectedFile();
					try {
						FileReader reader = new FileReader(fileIn);
						MapReaderWriter mapRW = new MapReaderWriter();			
						mapRW.read(reader, map);
						reader.close();
					} catch(FileNotFoundException fne) {
						System.out.println("append map file cannot be found:" + fne.getMessage());
					} catch(MapFormatException mfe) {
						System.out.println("map format exception when append a map: " + mfe.getMessage());
					} catch(IOException ioe) {
						System.out.println("IOException when append a map: " + ioe.getMessage());
					}
				} else if(result == JFileChooser.CANCEL_OPTION) {
					
				}
			}	
		}

		/**
		 *  QuitListener Monitor the quit menu item and warn the user 
		 *  with a choice of proceeding or cancelling
		 */
		class QuitListener implements ActionListener {
			public void actionPerformed(ActionEvent ae) {
				int confirm = JOptionPane.showOptionDialog(mapFrame, "Are You Sure to Close Application?", "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (confirm == 0) {
                		System.exit(0);
                	} 
			}	
		}

		/**
		 *  NewPlaceListener Monitor the New Place menu item 
		 *  will pop a dialog and require user to create a place
		 */
		class NewPlaceListener implements ActionListener {
			public void actionPerformed(ActionEvent ae) {
				//create and add components to the pop dialog
				String msgInfo = new String("Please enter a Name to create new Place");
				JTextField newPlaceName = new JTextField(10);
				Object[] dialogInfo = {msgInfo, newPlaceName};
				String btnString1 = "OK";
				String btnString2 = "Cancel";
				Object[] options = {btnString1, btnString2};
				JOptionPane optionPanel = new JOptionPane(dialogInfo,
                        									JOptionPane.QUESTION_MESSAGE,
                        									JOptionPane.YES_NO_OPTION,
                        									null,
                        									options,
                        									options[0]);
				JDialog newPlaceDialog = new JDialog(mapFrame,"New Place");
				newPlaceDialog = optionPanel.createDialog("New Place");
				newPlaceDialog.setBounds(mapFrame.getX()+100, mapFrame.getY()+70, 400, 200);
				newPlaceDialog.setContentPane(optionPanel);
				newPlaceDialog.setVisible(true);
				
				if(optionPanel.getValue() == null) {
					return;
				}
				if(optionPanel.getValue().equals(btnString1)) {
					//user press Ok
					try {
						map.newPlace(newPlaceName.getText(),
									mapPanel.getX() + mapPanel.getWidth() / 2,
									mapPanel.getY() + mapPanel.getHeight() /2);
					} catch(IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(mapFrame,"the name is illegal or the place already exists");
					}
				} else if(optionPanel.getValue().equals(btnString2)) {
					//user press Cancel
				}
			}	
		}
		
		/**
		 *  NewRoadListener Monitor the New Road menu item 
		 *  will pop a dialog and require user to create a road
		 */
		class NewRoadListener implements ActionListener {
			public void actionPerformed(ActionEvent ae) {
				//create and add components to the pop dialog
				String msgInfo1 = new String("Please enter a Name to create new Road");
				JTextField newRoadName = new JTextField(10);
				String msgInfo2 = new String("Please enter a int value for the length");
				JTextField newRoadLength = new JTextField(10);
				
				Object[] dialogInfo = {msgInfo1, newRoadName,msgInfo2,newRoadLength};
				String btnString1 = "OK";
				String btnString2 = "Cancel";
				Object[] options = {btnString1, btnString2};
				JOptionPane optionPanel = new JOptionPane(dialogInfo,
                        									JOptionPane.QUESTION_MESSAGE,
                        									JOptionPane.YES_NO_OPTION,
                        									null,
                        									options,
                        									options[0]);
				JDialog newPlaceDialog = new JDialog(mapFrame,"New Road");
				newPlaceDialog = optionPanel.createDialog("New Road");
				newPlaceDialog.setBounds(mapFrame.getX()+100, mapFrame.getY()+70, 400, 200);
				newPlaceDialog.setContentPane(optionPanel);
				newPlaceDialog.setVisible(true);
				
				if(optionPanel.getValue() == null) {
					return;
				}
				
				if(optionPanel.getValue().equals(btnString1)) {
					//user press Ok
					if(newRoadLength.getText().matches("[0-9]+") && newRoadName.getText() != null) {
						mapPanel.newRoadName = newRoadName.getText();
						mapPanel.newRoadLenght = Integer.parseInt(newRoadLength.getText());
					} else {
						JOptionPane.showMessageDialog(mapFrame,"the name format or length format is wrong");
						return;
					}
					
					mapPanel.willNewRoad = true;
					//mapFrame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
					mapPanel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
					Set<PlaceIcon> icons = mapPanel.getPlaceIcons();
					for(PlaceIcon icon: icons) {
						if(icon.isSelected()) {
							icon.isSelected(false);
						}
					}
				} else if(optionPanel.getValue().equals(btnString2)) {
					//user press Cancel
					mapPanel.willNewRoad = false;
				}
			}	
		}
		
		/**
		 *  SetStartListener Monitor the Set Start menu item 
		 *  will set the selected Place as its Start Place
		 */
		class SetStartListener implements ActionListener {
			public void actionPerformed(ActionEvent ae) {
				Set<PlaceIcon> icons = mapPanel.getPlaceIcons();
				int selectCounter = 0;
				PlaceIcon selectedIcon = null;
				for(PlaceIcon icon: icons) {
					if(icon.isSelected()) {
						selectCounter++;
						selectedIcon = icon;
					}
				}
				
				if(selectCounter != 1) {
					JOptionPane.showMessageDialog(mapFrame, "Only one place can be selected.");
				} else {
					map.setStartPlace(selectedIcon.getPlace());
				}
			}
		}
		
		/**
		 *  UnsetStartListener Monitor the Unset Start menu item 
		 *  will unset its Start Place
		 */
		class UnsetStartListener implements ActionListener {
			public void actionPerformed(ActionEvent ae) {
				map.setStartPlace(null);
			}
		}
		
		/**
		 *  SetEndListener Monitor the Set End menu item 
		 *  will set the selected Place as its End Place
		 */
		class SetEndListener implements ActionListener {
			public void actionPerformed(ActionEvent ae) {
				Set<PlaceIcon> icons = mapPanel.getPlaceIcons();
				int selectCounter = 0;
				PlaceIcon selectedIcon = null;
				for(PlaceIcon icon: icons) {
					if(icon.isSelected()) {
						selectCounter++;
						selectedIcon = icon;
					}
				}
				
				if(selectCounter != 1) {
					JOptionPane.showMessageDialog(mapFrame, "Only one place can be selected.");
				} else {
					map.setEndPlace(selectedIcon.getPlace());
				}
			}
		}
		
		/**
		 *  UnsetEndListener Monitor the Unset End menu item 
		 *  will unset its End Place
		 */
		class UnsetEndListener implements ActionListener {
			public void actionPerformed(ActionEvent ae) {
				map.setEndPlace(null);
			}
		}
		
		/**
		 *  DeleteListener Monitor the delete menu item 
		 *  will delete the select item
		 */
		class DeleteListener implements ActionListener {
			public void actionPerformed(ActionEvent ae) {
				System.out.println("delete");
				Component[] components = mapPanel.getComponents();
				ArrayList<PlaceIcon> placeIconList = new ArrayList<PlaceIcon>();
				ArrayList<RoadIcon> roadIconList = new ArrayList<RoadIcon>();
				for(Component component:components) {
					if(component instanceof PlaceIcon) {
						PlaceIcon icon = (PlaceIcon)component;
						if(icon.isSelected()) {
							placeIconList.add(icon);
						}
					} else if(component instanceof RoadIcon) {
						RoadIcon icon = (RoadIcon)component;
						if(icon.isSelected()) {
							roadIconList.add(icon);
						}
					}
				}

				//delete raods
				for(RoadIcon ri : roadIconList) {
					//System.out.println(ri.getRoad());
					map.deleteRoad(ri.getRoad());
				}
				
				//delete place
				for(PlaceIcon pi : placeIconList) {
					//System.out.println(pi.getPlace());
					map.deletePlace(pi.getPlace());
				}
			}
		}
	}
	
	/**
	 * control bar
	 */
	class ControlBar extends JPanel {
		private JButton calculateDistance;
		private JTextField calculateResult;
		
		ControlBar() {
			calculateDistance = new JButton("calculateDistance");
			calculateResult = new JTextField(10);
			
			this.add(calculateDistance);
			this.add(calculateResult);
			
			calculateDistance.addActionListener(new CalculateDistanceListener());
		}
		
		/**
		 *  CalculateDistanceListener Monitor the calculateDistance Button, 
		 *  it will displace the result on calculateResult TextField
		 */
		class CalculateDistanceListener implements ActionListener {
			public void actionPerformed(ActionEvent ae) {
				calculateResult.setText( Integer.toString(map.getTripDistance()));
				
				Set<RoadIcon> roads = mapPanel.getRoadIcons();
				for(RoadIcon ri : roads) {
					ri.isSelected( ri.getRoad().isChosen() );
				}
				mapPanel.repaint();
			}
		}
	}
}