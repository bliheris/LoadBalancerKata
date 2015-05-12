package pl.mb;

public class Percent {

    private int value;

    public static Percent zero() {
        return new Percent(0);
    }

    public static Percent hundred() {
        return new Percent(100);
    }

    public Percent(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Percent)) return false;

        Percent percent = (Percent) o;

        if (value != percent.value) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value;
    }
}
