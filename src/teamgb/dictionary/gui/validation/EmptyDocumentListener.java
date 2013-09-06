package teamgb.dictionary.gui.validation;

import javax.swing.JButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class EmptyDocumentListener implements DocumentListener {
	JButton button;
	
	public EmptyDocumentListener(JButton button) {
		this.button = button;
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

	public void validate(DocumentEvent de) {
		String currText = "";
		try {
			Document doc = (Document) de.getDocument();
			currText = doc.getText(0, doc.getLength());
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		if (currText.length() == 0)
			button.setEnabled(false);
		else
			button.setEnabled(true);
	}
}
