// The "Hand" interface.public interface Hand{    // Deals num cards from a deck    // The deck and position to deal from within that deck    // is specified by the deck iterator    public void deal( int num, Iterator deck );        // Inserts a card into a hand    // Throws an exception if the hand is full    public void insertCard ( Card card ) throws HandFullException;        // Returns a hand that is the calling hand and the parameter joined    // Thus the length of the returned hand is the sum of     // the lengths of the calling hand and the parameter hand    //public Hand join ( Hand h );         // Returns a String version of a hand that     // can be used to display the hand    public String toString( );        // Returns a clone of a hand    // (a copy, not a pointer to the same hand)    // Overrides the clone method in the Object class    public Object clone ( );        } // Hand interface