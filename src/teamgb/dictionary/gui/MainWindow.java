package teamgb.dictionary.gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import notmycode.StringUtils.Levenshtein;
import teamgb.dictionary.lexicon.CebuanoLexiconEntry;
import teamgb.dictionary.lexicon.CebuanoLexicon;
import teamgb.dictionary.lexicon.LexiconUtils;
import teamgb.dictionary.lexicon.RatedSense;
import teamgb.dictionary.lexicon.CebuanoLexiconSense;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class MainWindow {

	private JFrame frame;
	private JTextField searchTextField;
	private DefaultListModel<CebuanoLexiconEntry> lmEntries;
	private File cFile;
	private CebuanoLexicon lex;
	private JList<CebuanoLexiconEntry> list;
	private JTextPane textArea;
	private JMenuItem mntmRemoveEntry;
	private JMenuItem mntmEditSelectedEntry;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Cebuano Lexicon - Data Entry");
		frame.setBounds(100, 100, 612, 350);
		frame.setPreferredSize(new Dimension(600, 350));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmOpen = new JMenuItem("Open XML file");
		mnFile.add(mntmOpen);

		JSeparator separator_2 = new JSeparator();
		mnFile.add(separator_2);

		final JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.setEnabled(false);
		mnFile.add(mntmSave);

		final JMenuItem mntmSaveAs = new JMenuItem("Save As");
		mntmSaveAs.setEnabled(false);
		mnFile.add(mntmSaveAs);

		JSeparator separator_1 = new JSeparator();
		mnFile.add(separator_1);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
		mnFile.add(mntmExit);

		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);

		final JMenuItem mntmAddEntry = new JMenuItem("Add new entry");
		mntmAddEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					EntryDialog dialog = new EntryDialog(null, lex.getIds(),
							lex.getLemmas());
					EntryDialog.Choice choice = dialog.showDialog();
					if (choice == EntryDialog.Choice.OK) {
						lmEntries.addElement(dialog.getInput());
						sortLM();
						refresh();
					}

				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});
		mntmAddEntry.setEnabled(false);
		mnEdit.add(mntmAddEntry);

		mntmEditSelectedEntry = new JMenuItem("Edit selected entry");
		mntmEditSelectedEntry.setEnabled(false);
		mnEdit.add(mntmEditSelectedEntry);

		mntmRemoveEntry = new JMenuItem("Remove selected entry");
		mntmRemoveEntry.setEnabled(false);
		mnEdit.add(mntmRemoveEntry);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem mntmABout = new JMenuItem("About");
		mntmABout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					AboutMe dialog = new AboutMe();
					dialog.setTitle("About");
					dialog.setPreferredSize(new Dimension(280, 180));
					dialog.setModalityType(SenseDialog.ModalityType.APPLICATION_MODAL);
					dialog.setResizable(false);
					dialog.pack();
					dialog.setLocationRelativeTo(null);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		mnHelp.add(mntmABout);

		frame.getContentPane().setLayout(
				new FormLayout(new ColumnSpec[] {
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("70dlu"),
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.MIN_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"),
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC, }, new RowSpec[] {
						FormFactory.LINE_GAP_ROWSPEC, FormFactory.MIN_ROWSPEC,
						FormFactory.RELATED_GAP_ROWSPEC,
						RowSpec.decode("default:grow"),
						FormFactory.RELATED_GAP_ROWSPEC, }));

		searchTextField = new JTextField();
		frame.getContentPane().add(searchTextField, "2, 2, fill, fill");
		searchTextField.setColumns(10);

		final JButton btnSearch = new JButton("Search");
		btnSearch.setEnabled(false);
		frame.getRootPane().setDefaultButton(btnSearch);
		frame.getContentPane().add(btnSearch, "4, 2, default, fill");

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, "6, 2, fill, fill");
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		final JRadioButton rdbtnSense = new JRadioButton("by SENSE");
		rdbtnSense.setSelected(true);
		panel.add(rdbtnSense);

		JRadioButton rdbtnEntry = new JRadioButton("by ENTRY");
		panel.add(rdbtnEntry);

		final ButtonGroup group = new ButtonGroup();
		group.add(rdbtnSense);
		group.add(rdbtnEntry);

		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText(search(searchTextField.getText(),
						rdbtnSense.isSelected()));
			}
		});

		JScrollPane listScrollPane = new JScrollPane();
		frame.getContentPane().add(listScrollPane, "2, 4, 3, 1, fill, fill");

		lmEntries = new DefaultListModel<CebuanoLexiconEntry>();
		list = new JList<CebuanoLexiconEntry>(lmEntries);
		list.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent ke) {
				if(ke.getKeyCode() == KeyEvent.VK_DELETE){
					int index = list.getSelectedIndex();
					if (index != -1) {
						lmEntries.remove(index);
					}
					refresh();
				}
			}
		});
		listScrollPane.setViewportView(list);

		JScrollPane entryScrollPane = new JScrollPane();
		entryScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(entryScrollPane, "6, 4, 3, 1, fill, fill");

		textArea = new JTextPane();
		textArea.setContentType("text/html");
		textArea.setEditable(false);
		entryScrollPane.setViewportView(textArea);

		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				if(me.getClickCount() == 2){
					CebuanoLexiconEntry s = list.getSelectedValue();
					if(s != null){
						try {
							int index = list.getSelectedIndex();
							EntryDialog dialog = new EntryDialog(s, lex.getIds(s), lex
									.getLemmas(s));
							EntryDialog.Choice choice = dialog.showDialog();
							if (choice == EntryDialog.Choice.OK) {
								lmEntries.set(index, dialog.getInput());
								sortLM();
								refresh();
							}
						} catch (Exception exception) {
							exception.printStackTrace();
						}
					}
				}
			}
		});
		
		mntmEditSelectedEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					CebuanoLexiconEntry s = (CebuanoLexiconEntry) list.getSelectedValue();
					int index = list.getSelectedIndex();
					EntryDialog dialog = new EntryDialog(s, lex.getIds(s), lex
							.getLemmas(s));
					EntryDialog.Choice choice = dialog.showDialog();
					if (choice == EntryDialog.Choice.OK) {
						lmEntries.set(index, dialog.getInput());
						sortLM();
					}
				} catch (Exception exception) {
					exception.printStackTrace();
				}
				refresh();
			}
		});

		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser openChooser = new JFileChooser();
				FileNameExtensionFilter openFilter = new FileNameExtensionFilter(
						"XML file (*.xml)", "xml");
				openChooser.setFileFilter(openFilter);
				int returnVal = openChooser.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					lex = new CebuanoLexicon();
					lmEntries.clear();
					if (cFile != null) {
						int dialogButton = JOptionPane.YES_NO_OPTION;
						int dialogResult = JOptionPane
								.showConfirmDialog(
										null,
										"Are you sure you want to open another file?",
										"Warning", dialogButton);
						if (dialogResult == JOptionPane.NO_OPTION) {
							return;
						}
					}
					cFile = openChooser.getSelectedFile();

					if (!cFile.exists()) {
						String name = cFile.getName();
						if(!name.endsWith(".xml"))
							cFile = new File(cFile.getParent(), name.concat(".xml"));
						try {
							LexiconUtils.saveAsXML(lex,cFile);
							mntmAddEntry.setEnabled(true);
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(frame,
									"Cannot create new file!", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					} else {
						lex = LexiconUtils.readFromXML(cFile);
						lex.sort();
						for (CebuanoLexiconEntry entry : lex.getEntries())
							lmEntries.addElement(entry);
						mntmAddEntry.setEnabled(true);
						mntmSaveAs.setEnabled(true);
						btnSearch.setEnabled(true);
						mntmSave.setEnabled(true);
						refresh();
					}

					refresh();
				}
			}
		});

		mntmRemoveEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = list.getSelectedIndex();
				if (index != -1) {
					lmEntries.remove(index);
				}
				refresh();
			}
		});

		mntmSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final JFileChooser saveChooser = new JFileChooser();
				FileNameExtensionFilter saveFilter = new FileNameExtensionFilter(
						"XML file (*.xml)", "xml");
				saveChooser.setFileFilter(saveFilter);
				int returnVal = saveChooser.showSaveDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					cFile = saveChooser.getSelectedFile();
					save();
				}
			}
		});

		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				save();
			}
		});
		
		list.addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(list.getSelectedIndex() == -1){
					mntmEditSelectedEntry.setEnabled(false);
					mntmRemoveEntry.setEnabled(false);
				}
		        refresh();
			}
			
		});
	}

	private String search(String text, boolean bySense) {
		updateLexicon();
		StringBuilder sb = new StringBuilder();
		sb.append("<html><strong>Search Results:</strong><br><br>");
		if (bySense) {
			ArrayList<RatedSense> res = new ArrayList<RatedSense>();
			for (CebuanoLexiconEntry e : lex.getEntries()) {
				int diff = 0;
				String lemma = e.getLemma();
				if (lemma.startsWith(text)) {
					diff = Levenshtein.getLevenshteinDistance(lemma, text);
					for (CebuanoLexiconSense s : e.getSenses()) {
						res.add(new RatedSense(s, diff));
					}
					continue;
				}

				for (CebuanoLexiconSense s : e.getSenses()) {
					for (String sl : s.getSublemmas()) {
						diff = Levenshtein.getLevenshteinDistance(sl, text);
						if (sl.startsWith(text)) {
							res.add(new RatedSense(s, diff));
						}
					}
				}
			}
			Collections.sort(res);
			for (RatedSense rs : res) {
				sb.append("S: ");
				sb.append(LexiconUtils.generateHtml(rs.getSense(),true));
				sb.append("<br><br>");
			}

		} else {
			for (CebuanoLexiconEntry entry : lex.getEntries()) {
				if (entry.getLemma().startsWith(text))
					sb.append(LexiconUtils.generateHtml(entry));
			}
		}
		sb.append("</html>");
		return sb.toString();
	}

	private void save() {
		updateLexicon();
		if(!cFile.exists()){
			String name = cFile.getName();
			if(!name.endsWith(".xml"))
				cFile = new File(cFile.getParent(), name.concat(".xml"));
		}
		try {
			LexiconUtils.saveAsXML(lex,cFile);
			int count = lex.getEntries().size();
			String entry = (count == 1) ? " entry" : " entries";
			JOptionPane.showMessageDialog(frame, count + entry
					+ " saved to XML file", "Success",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame, "Cannot save file!", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		if (cFile != null)
			frame.setTitle(cFile.getAbsolutePath());
	}

	private void refresh() {
		updateLexicon();
		if (cFile != null)
			frame.setTitle(cFile.getAbsolutePath());
		CebuanoLexiconEntry selected = (CebuanoLexiconEntry) list.getSelectedValue();
		if (selected != null) {
			textArea.setText("<html>" + LexiconUtils.generateHtml(selected) + "</html>");
			mntmRemoveEntry.setEnabled(true);
			mntmEditSelectedEntry.setEnabled(true);
			textArea.select(1, 1);
		} else {
			textArea.setText("<html><font color=\"white\"></font></html>");
		}
	}

	private void updateLexicon() {
		lex = new CebuanoLexicon();
		lex.setEntries(lmEntries.toArray());
	}
	
	private void sortLM(){
		lex.sort();
		Object[] entries = lmEntries.toArray();
		Arrays.sort(entries);
		lmEntries.removeAllElements();
		for (Object obj : entries)
			lmEntries.addElement((CebuanoLexiconEntry) obj);
		refresh();
	}
}
