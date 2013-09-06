package teamgb.dictionary.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import teamgb.dictionary.gui.validation.EmptyDocumentListener;

public class AddEditStringDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4944950158863398547L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private Choice selected;

	public enum Choice {
		OK, CANCEL
	}

	/**
	 * Create the dialog.
	 */
	public AddEditStringDialog(String elem, String type) {
		selected = Choice.CANCEL;
		setBounds(100, 100, 450, 109);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		final JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selected = Choice.OK;
				setVisible(false);
				dispose();
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
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

		{
			textField = new JTextField();
			textField.getDocument().addDocumentListener(
					new EmptyDocumentListener(okButton));
			contentPanel.add(textField, BorderLayout.CENTER);
			textField.setColumns(10);
		}

		if (elem == null) {
			setTitle("Add New " + type);
			elem = new String();
			okButton.setEnabled(false);
		} else {
			setTitle("Edit " + type);
			textField.setText(elem);
		}
	}

	public Choice showDialog() {
		setLocationRelativeTo(null);
		setModalityType(SenseDialog.ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
		return selected;
	}

	public String getInput() {
		return textField.getText();
	}
}
