package teamgb.dictionary.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import teamgb.dictionary.lexicon.Entry;
import teamgb.dictionary.lexicon.Sense;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class EntryDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8758318693373729760L;
	private final JPanel contentPanel = new JPanel();
	private JTextField idTextField;
	private JTextField lemmaTextField;
	private Entry entry;
	private Choice selected;

	public enum Choice {
		OK, CANCEL
	}

	/**
	 * Create the dialog.
	 */
	public EntryDialog(Entry pEntry, List<String> ids, List<String> lemmas) {
		selected = Choice.CANCEL;
		entry = pEntry;
		setBounds(100, 100, 400, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel
				.setLayout(new FormLayout(new ColumnSpec[] {
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("50dlu:grow"),
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("50dlu:grow"),
						FormFactory.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("default:grow"),
						FormFactory.RELATED_GAP_COLSPEC, }, new RowSpec[] {
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
						FormFactory.DEFAULT_ROWSPEC, }));
		{
			JLabel lblID = new JLabel("ID:");
			contentPanel.add(lblID, "2, 2, right, default");
		}

		idTextField = new JTextField();
		idTextField.setEditable(false);

		contentPanel.add(idTextField, "4, 2, 5, 1, fill, default");
		idTextField.setColumns(10);

		{
			JLabel lblLEMMA = new JLabel("LEMMA:");
			contentPanel.add(lblLEMMA, "2, 4, right, default");
		}
		{
			lemmaTextField = new JTextField();
			contentPanel.add(lemmaTextField, "4, 4, 5, 1, fill, default");
			lemmaTextField.setColumns(10);
		}
		{
			JLabel lblSenses = new JLabel("SENSES:");
			contentPanel.add(lblSenses, "2, 6");
		}

		final DefaultListModel<Sense> lmSenses = new DefaultListModel<Sense>();
		JScrollPane sensesScrollPane = new JScrollPane();
		contentPanel.add(sensesScrollPane, "4, 6, 5, 5, fill, fill");
		final JList<Sense> sensesList = new JList<Sense>(lmSenses);
		sensesScrollPane.setViewportView(sensesList);

		final JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Sense selectedSense = (Sense) sensesList.getSelectedValue();
					int index = sensesList.getSelectedIndex();
					SenseDialog dialog = new SenseDialog(selectedSense);
					SenseDialog.Choice choice = dialog.showDialog();
					if (choice == SenseDialog.Choice.OK) {
						lmSenses.set(index, dialog.getInput());
					}
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});
		contentPanel.add(btnEdit, "6, 12");

		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					SenseDialog dialog = new SenseDialog(null);
					SenseDialog.Choice choice = dialog.showDialog();
					if (choice == SenseDialog.Choice.OK) {
						lmSenses.addElement(dialog.getInput());
						btnEdit.setEnabled(true);
					}

				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});
		contentPanel.add(btnAdd, "4, 12");

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedIndex = sensesList.getSelectedIndex();
				if (selectedIndex != -1) {
					lmSenses.remove(selectedIndex);
					if (lmSenses.isEmpty())
						btnEdit.setEnabled(false);
				}
			}
		});
		contentPanel.add(btnDelete, "8, 12");

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		final JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				entry = new Entry();
				entry.setId(Integer.parseInt(idTextField.getText()));
				entry.setLemma(lemmaTextField.getText());
				entry.setSenses(lmSenses.toArray());
				selected = Choice.OK;
				setVisible(false);
				dispose();
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

		lemmaTextField.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				updateId();
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				updateId();
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				updateId();
			}
			
			private void updateId(){
				idTextField.setText(String.valueOf(lemmaTextField.getText().toString().hashCode()));
			}
		});

		if (entry == null) {
			setTitle("Add New Entry");
			entry = new Entry();
			okButton.setEnabled(false);
		} else {
			setTitle("Edit Entry");
			idTextField.setText(String.valueOf(entry.getId()));
			lemmaTextField.setText(entry.getLemma());
			for (Sense s : entry.getSenses())
				lmSenses.addElement(s);
		}
		if (lmSenses.isEmpty())
			btnEdit.setEnabled(false);
	}

	public Choice showDialog() {
		setLocationRelativeTo(null);
		setModalityType(SenseDialog.ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
		return selected;
	}

	public Entry getInput() {
		return entry;
	}

}
