package teamgb.dictionary.gui.validation;

import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class NumberDocumentFilter extends DocumentFilter {
	
	 public void replace(DocumentFilter.FilterBypass fb, int offset, int length,
	      String text, javax.swing.text.AttributeSet attr) throws BadLocationException {
	           fb.insertString(offset, text.replaceAll("\\D", ""), attr);   
	}
}
