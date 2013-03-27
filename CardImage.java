import java.awt.*;import java.awt.event.*;import javax.swing.*;// This class creates and alters CardImage objects (ie. the little card// graphics seen in the game's interface).public class CardImage extends JLabel {	// ImageIcon holds the graphic for the card's true face (may or may not be shown)	private ImageIcon faceImage;	// ImageIcon of the back of a card is always available	private static ImageIcon cardback = new ImageIcon ("cards/cardback.jpg");		// Constructor creates a basic card with the cardback image for its face.	CardImage () {			super (cardback);			faceImage = cardback;	}	// This method sets faceImage to the given card image.  If open == true then also 	// causes that image to be displayed.	public void setImage (String input, boolean open) {			ImageIcon face = new ImageIcon ("cards/" + input + ".jpg");		faceImage = face;			if (open) 			setIcon (faceImage);			setVisible (true);	}		// This method causes a CardImage object's faceImage to be shown.	public void flipToFace () {				setIcon (faceImage);		}	// This method sets the card's faceImage and displayed image to be set back 	// to the cardback image.	public void clearFace () {			setIcon (cardback);		faceImage = cardback;	}}