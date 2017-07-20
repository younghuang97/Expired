public class PairofDates
{
    private int fridgedate;
    private int freezerdate;
    public PairofDates(int fridge, int freezer)
    {
        super();
        this.fridgedate = fridge;
        this.freezerdate = freezer;
    }

    public int getFridge() { return fridgedate; };
    public int getFreezer() { return freezerdate; };
}