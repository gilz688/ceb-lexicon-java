package teamgb.dictionary.gui;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import teamgb.dictionary.gui.validation.EmptyDocumentListener;
import teamgb.dictionary.lexicon.CebuanoLexiconExample;
import teamgb.dictionary.lexicon.PartOfSpeech;
import teamgb.dictionary.lexicon.CebuanoLexiconSense;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;


public class SenseDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6890310713250213718L;
	private final JPanel contentPanel = new JPanel();
	private JComboBox posComboBox;
	private Choice selected;
	private CebuanoLexiconSense sense;
	private JTextField glossTextField;

	public enum Choice {
	    OK, CANCEL
	}

	/**
	 * Create the dialog.
	 */
	public SenseDialog(CebuanoLexiconSense pSense) {
		sense = pSense;
		selected = Choice.CANCEL;
		setBounds(100, 100, 500, 412);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		{
			JLabel lblPOS = new JLabel("POS:");
			contentPanel.add(lblPOS, "2, 2, right, default");
		}
			String pos[] = {"noun","verb","adjective","adverb","preposition","conjunction"};
			posComboBox = new JComboBox(pos);
			contentPanel.add(posComboBox, "4, 2, 5, 1, fill, default");
			
		{
			JLabel lblGloss = new JLabel("GLOSS:");
			lblGloss.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblGloss, "2, 4, right, default");
		}

			glossTextField = new JTextField();
			contentPanel.add(glossTextField, "4, 4, 5, 1, fill, default");
			glossTextField.setColumns(10);

		{
			JLabel lblSL = new JLabel("SUB-LEMMAS:");
			contentPanel.add(lblSL, "2, 6, right, default");
		}
		
			
		JScrollPane slScrollPane = new JScrollPane();
		contentPanel.add(slScrollPane, "4, 6, 5, 3, fill, fill");
		final DefaultListModel<String> lmSL = new DefaultListModel<String>();
		final JList<String> slList = new JList<String>(lmSL);
		slScrollPane.setViewportView(slList);
		
		final JButton btnEditSL = new JButton("Edit");
			btnEditSL.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						String selectedSL = (String) slList.getSelectedValue();
						int index = slList.getSelectedIndex();
						AddEditStringDialog dialog = new AddEditStringDialog(selectedSL,"SL-E");
						AddEditStringDialog.Choice choice = dialog.showDialog();
						if(choice == AddEditStringDialog.Choice.OK){
							selectedSL = dialog.getInput();
							lmSL.set(index, selectedSL);
						}
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}
			});
			contentPanel.add(btnEditSL, "6, 10");
		{
			JButton btnAddSL = new JButton("Add");
			btnAddSL.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						AddEditStringDialog dialog = new AddEditStringDialog(null,"SL-E");
						AddEditStringDialog.Choice choice = dialog.showDialog();
						if(choice == AddEditStringDialog.Choice.OK){
							lmSL.addElement(dialog.getInput());
							btnEditSL.setEnabled(true);
						}
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}
			});
			contentPanel.add(btnAddSL, "4, 10");
		}

		{
			JButton btnDeleteSL = new JButton("Delete");
			btnDeleteSL.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int selectedIndex = slList.getSelectedIndex();
					if (selectedIndex != -1) {
					    lmSL.remove(selectedIndex);
					    if(lmSL.isEmpty())
					    	btnEditSL.setEnabled(false);
					}
				}
			});
			contentPanel.add(btnDeleteSL, "8, 10");
		}
		{
			JLabel lblExamples = new JLabel("EXAMPLES:");
			contentPanel.add(lblExamples, "2, 12, right, default");
		}

		JScrollPane exScrollPane = new JScrollPane();
		contentPanel.add(exScrollPane, "4, 12, 5, 3, fill, fill");
		final DefaultListModel<CebuanoLexiconExample> lmEx = new DefaultListModel<CebuanoLexiconExample>();
		final JList<CebuanoLexiconExample> examplesList = new JList<CebuanoLexiconExample>(lmEx);
		exScrollPane.setViewportView(examplesList);
		final JButton btnEditEx = new JButton("Edit");
			btnEditEx.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						CebuanoLexiconExample selectedEx = (CebuanoLexiconExample) examplesList.getSelectedValue();
						int index = examplesList.getSelectedIndex();
						ExampleDialog dialog = new ExampleDialog(selectedEx,"EXAMPLE-E");
						ExampleDialog.Choice choice = dialog.showDialog();
						if(choice == ExampleDialog.Choice.OK){
							selectedEx = dialog.getInput();
							lmEx.set(index, selectedEx);
						}
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}
			});
			contentPanel.add(btnEditEx, "6, 16");
		{
			JButton btnAddEx = new JButton("Add");
			btnAddEx.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						ExampleDialog dialog = new ExampleDialog(null,"EXAMPLE-E");
						ExampleDialog.Choice choice = dialog.showDialog();
						if(choice == ExampleDialog.Choice.OK){
							lmEx.addElement(dialog.getInput());
							btnEditEx.setEnabled(true);
						}
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}
			});
			contentPanel.add(btnAddEx, "4, 16");
		}
		{
			JButton btnDeleteEx = new JButton("Delete");
			btnDeleteEx.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int selectedIndex = examplesList.getSelectedIndex();
					if (selectedIndex != -1) {
					    lmEx.remove(selectedIndex);
					    if(lmEx.isEmpty())
					    	btnEditEx.setEnabled(false);
					}
				}
			});
			contentPanel.add(btnDeleteEx, "8, 16");
		}
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
				final JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						selected = Choice.OK;
						sense = new CebuanoLexiconSense();
						sense.setPartOfSpeech(PartOfSpeech.valueOf(posComboBox.getSelectedItem().toString().toUpperCase()));
						sense.setGloss(glossTextField.getText());
						sense.setExamples(lmEx.toArray());
						sense.setSublemmas(lmSL.toArray());
						setVisible(false);
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				glossTextField.getDocument().addDocumentListener(new EmptyDocumentListener(okButton));
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setVisible(false);
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		
		if(sense == null){
			setTitle("Add New SENSE");
			okButton.setEnabled(false);
			sense = new CebuanoLexiconSense();
			btnEditSL.setEnabled(false);
			btnEditEx.setEnabled(false);
		}
		else{
			setTitle("Edit SENSE");
			posComboBox.setSelectedIndex(sense.getPartOfSpeech().ordinal());
			glossTextField.setText(sense.getGloss());
			for(String sl : sense.getSublemmas())
				lmSL.addElement(sl);
			for(CebuanoLexiconExample ex : sense.getExamples())
				lmEx.addElement(ex);
			
			if(lmSL.isEmpty())
				btnEditSL.setEnabled(false);
			
			if(lmEx.isEmpty())
				btnEditEx.setEnabled(false);
		}
	}
	
	public Choice showDialog(){
		setLocationRelativeTo(null);
		setModalityType(SenseDialog.ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
		return selected;
	}

	public CebuanoLexiconSense getInput(){
		return sense;
	}

}
