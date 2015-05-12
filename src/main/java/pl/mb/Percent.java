package pl.mb;

import org.omg.IOP.RMICustomMaxStreamFormat;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Percent implements Comparable<Percent> {

    private static final int SCALE = 2;
    private static final RoundingMode RM = RoundingMode.HALF_UP;
    private BigDecimal value;


    public static Percent zero() {
        return new Percent(0);
    }

    public static Percent hundred() {
        return new Percent(100);
    }

    public static Percent make(int first, int second) {
        BigDecimal res = BigDecimal.valueOf(first).divide(BigDecimal.valueOf(second),
                SCALE, RM);
        res = res.multiply(new BigDecimal(100)).setScale(SCALE, RM);
        return new Percent(res);
    }

    public Percent(BigDecimal v) {
        value = v.setScale(SCALE, RM);
    }

    public Percent(int v) {
        this(BigDecimal.valueOf(v));
    }

    @Override
    public String toString() {
        return value + "%";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Percent)) return false;

        Percent percent = (Percent) o;

        if (value != null ? !value.equals(percent.value) : percent.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public int compareTo(Percent o) {
        return value.compareTo(o.value);
    }
}
