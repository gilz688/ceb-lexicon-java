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
import teamgb.dictionary.lexicon.Example;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.JLabel;

public class ExampleDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5009282350550263589L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField_English;
	private Choice selected;
	private JTextField textField_Cebuano;

	public enum Choice {
		OK, CANCEL
	}

	/**
	 * Create the dialog.
	 */
	public ExampleDialog(Example selectedEx, String type) {
		selected = Choice.CANCEL;
		setBounds(100, 100, 450, 144);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

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
			textField_English = new JTextField();
			textField_English.getDocument().addDocumentListener(
					new EmptyDocumentListener(okButton));
			contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
					FormFactory.RELATED_GAP_COLSPEC,
					ColumnSpec.decode("default:grow"),
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					ColumnSpec.decode("424px"),},
				new RowSpec[] {
					FormFactory.LINE_GAP_ROWSPEC,
					RowSpec.decode("20px"),
					FormFactory.UNRELATED_GAP_ROWSPEC,
					RowSpec.decode("20px"),}));
			
			JLabel lblCebuano = new JLabel("CEB");
			contentPanel.add(lblCebuano, "2, 2, right, default");
			
			JLabel lblEnglish = new JLabel("ENG");
			contentPanel.add(lblEnglish, "2, 4, right, default");
			contentPanel.add(textField_English, "4, 4, fill, top");
			textField_English.setColumns(10);
		}
		
		textField_Cebuano = new JTextField();
		contentPanel.add(textField_Cebuano, "4, 2, fill, top");
		textField_Cebuano.setColumns(10);

		if (selectedEx == null) {
			setTitle("Add New " + type);
			selectedEx = new Example();
			okButton.setEnabled(false);
		} else {
			setTitle("Edit " + type);
			textField_English.setText(selectedEx.getEng_example());
			textField_Cebuano.setText(selectedEx.getCeb_example());
		}
	}

	public Choice showDialog() {
		setLocationRelativeTo(null);
		setModalityType(SenseDialog.ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
		return selected;
	}

	public Example getInput() {
		Example ex = new Example();
		ex.setEng_example(textField_English.getText());
		ex.setCeb_example(textField_Cebuano.getText());
		return ex;
	}
}
