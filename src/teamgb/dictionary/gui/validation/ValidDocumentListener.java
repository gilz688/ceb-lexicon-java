package teamgb.dictionary.gui.validation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class ValidDocumentListener implements DocumentListener {
	private JButton button;
	private JTextField textField;
	private List<String> taken;
	private static List<JTextField> tfs;
	private static Color COLOR_BLACK = new Color(0,0,0);
	private static Color COLOR_RED = new Color(255,0,0);

	public ValidDocumentListener(JButton button, List<String> taken, JTextField textField) {
		this.button = button;
		this.taken = taken;
		this.textField = textField;
		if(tfs == null)
			tfs = new ArrayList<JTextField>();
		else
			tfs.add(textField);
	}

	@Override
	public void changedUpdate(DocumentEvent de) {

	}

	@Override
	public void insertUpdate(DocumentEvent de) {
		validate(de);
	}

	@Override
	public void removeUpdate(DocumentEvent de) {
		validate(de);
	}

	private void validate(DocumentEvent de) {
		String currText = "";
		try {
			Document doc = (Document) de.getDocument();
			currText = doc.getText(0, doc.getLength());
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		
		if (currText.length() == 0){
			textField.setForeground(COLOR_RED);
			button.setEnabled(false);
			return;
		}
		
		//check if already taken
		for(String str : taken)
			if(currText.equalsIgnoreCase(str)){
				textField.setForeground(COLOR_RED);
				button.setEnabled(false);
				return;
			}
		
		//set foreground color to BLACK
		textField.setForeground(COLOR_BLACK);
		
		//check is foregrounds are red  
		for(JTextField tf : tfs){
			if(tf.getForeground().equals(Color.RED)){
				button.setEnabled(false);
				return;
			}
		}
		
		//since none is red then enable button
		button.setEnabled(true);
	}
}
