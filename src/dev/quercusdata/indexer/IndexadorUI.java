/**
 *
 */
package dev.quercusdata.indexer;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dev.quercusdata.util.ApplicationGetPropertyValues;
import dev.quercusdata.util.QuercusUtil;

/**
 * @author Manu *
 */
public class IndexadorUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final String SEPARATOR = ";";

	private Logger logger = Logger.getLogger(IndexadorUI.class.getName());
	private Parameters params;

	private JPanel panelIndexer;
	private final JLabel labelFormats = new JLabel("Valid formats example: mp4;avi;wmv;mpg *");
	private JTextField textFormats;
	private final JLabel labelAvoidDirectories = new JLabel(
			"Evitar los directorios: (por ejemplo, C:\\musica\\queen\\Jazz;C:\\musica\\queen\\Innuendo)");
	private JTextField textAvoidDirectories;
	private final JLabel labelDirToindex = new JLabel("Directorio a indexar: (por ejemplo, C:\\musica\\queen)*");
	private JTextField textDirToIndex;
	private JButton buttonBrowseDirectory = new JButton("Examinar...");
	private final JLabel labelOutput = new JLabel("Fichero de salida:*");
	private JTextField textOutput;
	private JButton buttonCreate = new JButton("Crear");
	private JButton buttonClose = new JButton("Cerrar");

	private JPanel panelMyMedia;
	private JLabel labelMusicFolders = new JLabel("Music files in folders (and subfolders)");
	private JTextField textMusicFolders;
	private JLabel labelMusicExclusionFolders = new JLabel("Music folders excluded");
	private JTextField textMusicExclusionFolders;
	private JLabel labelPhotoFolders = new JLabel("Photo files in folders (and subfolders)");
	private JTextField textPhotoFolders;
	private JLabel labelPhotoExclusionFolders = new JLabel("Photo folders excluded");
	private JTextField textPhotoExclusionFolders;

	JMenuBar menuBar = new JMenuBar();
	JMenu menu1 = new JMenu("File");
	JMenu menuOptions = new JMenu("Options");

	JMenuItem menuItem1 = new JMenuItem("Exit");
	JMenuItem menuItemOptionIndexer = new JMenuItem("Indexer");
	JMenuItem menuItemOptionMyMedia = new JMenuItem("My Media");

	public static void main(String[] args) {
		new IndexadorUI();
	}

	public IndexadorUI() {
		try {
			params = QuercusUtil.readProperties();
		} catch (IOException e1) {
			logger.severe(e1.getMessage());
		}
		setTitle("My Files Manager by Manu");

		setMenusApplication();

		panelIndexer = new JPanel();
		setPanelIndexerContent();
		add(panelIndexer);

		setSize(900, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(buttonBrowseDirectory)) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Selecciona un directorio para indexar");
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.setAcceptAllFileFilterUsed(false);
			File current = new File(textDirToIndex.getText().trim());
			fileChooser.setCurrentDirectory(current.exists() ? current : new File(getDefaultDownloadsDirectory()));

			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				textDirToIndex.setText(fileChooser.getSelectedFile().getAbsolutePath());
			}
			return;
		}

		if (e.getSource().equals(menuItem1) || e.getSource().equals(buttonClose)) {
			// TODO write in .properties the field values
			System.exit(0);
        }
        if (e.getSource()==buttonCreate) {
        	if (!textDirToIndex.getText().isEmpty() && !textFormats.getText().isEmpty()
    				&& !textOutput.getText().isEmpty()) {
	    			File sourceDir = new File(textDirToIndex.getText().trim());
	    			if (!sourceDir.exists() || !sourceDir.isDirectory()) {
	    				JOptionPane.showMessageDialog(this,
	    						"El directorio indicado no existe en este equipo.",
	    						"Directorio no valido",
	    						JOptionPane.ERROR_MESSAGE);
	    				return;
	    			}
    			Parameters param = new Parameters();
	    			param.setUnidadOrigen(sourceDir.getAbsolutePath());
    			param.setFicheroSalida(textOutput.getText());
    			param.setExtensionesFicheros(
	    					ApplicationGetPropertyValues.valuesToExtensionsArray(textFormats.getText(), SEPARATOR));
    			param.setExcluyeDirectorios(ApplicationGetPropertyValues
    					.valuesToArray(textAvoidDirectories.getText().replace(" ", ""), SEPARATOR));
    			new FileIndexer(param);
    			JOptionPane.showMessageDialog(null,
    					"Hecho!\nConsultar el fichero " + textDirToIndex.getText() + "\\" + textOutput.getText());
    		}
        }

	}

	private void setPanelIndexerContent() {

		panelIndexer.setLayout(new GridLayout(5, 2));

		panelIndexer.add(labelDirToindex);
		panelIndexer.add(labelFormats);

		textDirToIndex = new JTextField(getDefaultDownloadsDirectory(), 20);
		JPanel directoryPanel = new JPanel(new BorderLayout(5, 0));
		directoryPanel.add(textDirToIndex, BorderLayout.CENTER);
		directoryPanel.add(buttonBrowseDirectory, BorderLayout.EAST);
		panelIndexer.add(directoryPanel);

		textFormats = new JTextField(QuercusUtil.listToString(params.getExtensionesFicheros()), 20);
		panelIndexer.add(textFormats);
		panelIndexer.add(labelOutput);
		panelIndexer.add(labelAvoidDirectories);

		textOutput = new JTextField(params.getFicheroSalida(), 20);
		panelIndexer.add(textOutput);

		textAvoidDirectories = new JTextField(QuercusUtil.listToString(params.getExcluyeDirectorios()), 20);
		panelIndexer.add(textAvoidDirectories);

		buttonCreate.addActionListener(this);
		buttonClose.addActionListener(this);
		buttonBrowseDirectory.addActionListener(this);

		panelIndexer.add(buttonCreate);
		panelIndexer.add(buttonClose);
	}

	private void setMenusApplication() {
		setJMenuBar(menuBar);
		menuBar.add(menu1);
		menuItem1.addActionListener(this);
		menu1.add(menuItem1);

		menuBar.add(menuOptions);
		menuItemOptionIndexer.addActionListener(this);
		menuItemOptionMyMedia.addActionListener(this);
		menuOptions.add(menuItemOptionIndexer);
		menuOptions.add(menuItemOptionMyMedia);
	}

	private String getDefaultDownloadsDirectory() {
		String userHome = System.getProperty("user.home");
		File downloadsDir = new File(userHome, "Downloads");
		return downloadsDir.getAbsolutePath();
	}
}

