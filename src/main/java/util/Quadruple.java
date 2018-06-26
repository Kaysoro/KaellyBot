package util;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * <p>A quadruple consisting of foor elements.</p>
 *
 *
 * <p>Subclass implementations may be mutable or immutable.
 * However, there is no restriction on the type of the stored objects that may be stored.
 * If mutable objects are stored in the quadruple, then the quadruple itself effectively becomes mutable.</p>
 *
 * @param <F> the first element type
 * @param <S> the second element type
 * @param <T> the third element type
 * @param <L> the last element type
 */
public class Quadruple<F, S, T, L> implements Comparable<Quadruple<F, S, T, L>>, Serializable {

    /** Serialization version */
    private static final long serialVersionUID = 1L;

    /**
     * An immutable triple of nulls.
     */
    // This is not defined with generics to avoid warnings in call sites.
    @SuppressWarnings("rawtypes")
    private static final Quadruple NULL = Quadruple.of(null, null, null, null);

    /**
     * Returns an immutable triple of nulls.
     *
     * @param <F> the first element of this quadruple. Value is {@code null}.
     * @param <S> the middle element of this quadruple. Value is {@code null}.
     * @param <T> the right element of this quadruple. Value is {@code null}.
     * @param <L> the right element of this quadruple. Value is {@code null}.
     * @return an immutable quadruple of nulls.
     */
    @SuppressWarnings("unchecked")
    public static <F, S, T, L> Quadruple<F, S, T, L> nullQuadruple() {
        return NULL;
    }

    /**
     * <p>Obtains an immutable quadruple of from quadruple objects inferring the generic types.</p>
     *
     * <p>This factory allows the quadruple to be created using inference to
     * obtain the generic types.</p>
     *
     * @param <F> the first element type
     * @param <S> the second element type
     * @param <T> the third element type
     * @param <L> the last element type
     * @param first  the first element, may be null
     * @param second the second element, may be null
     * @param third  the third element, may be null
     * @param last   the last element, may be null
     * @return a quadruple formed from the foor parameters, not null
     */
    public static <F, S, T, L> Quadruple<F, S, T, L> of(final F first, final S second, final T third, final L last) {
        return new Quadruple<>(first, second, third, last);
    }

    //-----------------------------------------------------------------------
    /** First object */
    public final F first;
    /** Second object */
    public final S second;
    /** Third object */
    public final T third;
    /** Last object */
    public final L last;

    /**
     * Create a new quadruple instance.
     *
     * @param first  the first element, may be null
     * @param second the second element, may be null
     * @param third  the third element, may be null
     * @param last   the last element, may be null
     */
    public Quadruple(final F first, final S second, final T third, final L last) {
        super();
        this.first = first;
        this.second = second;
        this.third = third;
        this.last = last;
    }
    //-----------------------------------------------------------------------
    /**
     * <p>Compares the quadruple based on the elements.
     * The types must be {@code Comparable}.</p>
     *
     * @param other  the other quadruple, not null
     * @return negative if this is less, zero if equal, positive if greater
     */
    @Override
    public int compareTo(final Quadruple<F, S, T, L> other) {
        return new CompareToBuilder().append(getFirst(), other.getFirst())
                .append(getSecond(), other.getSecond())
                .append(getThird(), other.getThird())
                .append(getLast(), other.getLast()).toComparison();
    }

    /**
     * <p>Compares this quadruple to another based on the foor elements.</p>
     *
     * @param obj  the object to compare to, null returns false
     * @return true if the elements of the quadruple are equal
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Quadruple<?, ?, ?, ?>) {
            final Quadruple<?, ?, ?, ?> other = (Quadruple<?, ?, ?, ?>) obj;
            return Objects.equals(getFirst(), other.getFirst())
                    && Objects.equals(getSecond(), other.getSecond())
                    && Objects.equals(getThird(), other.getThird())
                    && Objects.equals(getLast(), other.getLast());
        }
        return false;
    }

    /**
     * <p>Returns a suitable hash code.</p>
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return (getFirst() == null ? 0 : getFirst().hashCode()) ^
                (getSecond() == null ? 0 : getSecond().hashCode()) ^
                (getThird() == null ? 0 : getThird().hashCode()) ^
                (getLast() == null ? 0 : getLast().hashCode());
    }

    /**
     * <p>Returns a String representation of this triple using the format {@code ($left,$middle,$right)}.</p>
     *
     * @return a string describing this object, not null
     */
    @Override
    public String toString() {
        return "(" + getFirst() + "," + getSecond() + "," + getThird() + "," + getLast() + ")";
    }

    /**
     * <p>Formats the receiver using the given format.</p>
     *
     * <p>This uses {@link java.util.Formattable} to perform the formatting. Foor variables may
     * be used to embed the left and right elements. Use {@code %1$s} for the first
     * element, {@code %2$s} for the second {@code %3$s} for the third element and {@code %4$s} for the last element.
     * The default format used by {@code toString()} is {@code (%1$s,%2$s,%3$s,%4$s)}.</p>
     *
     * @param format  the format string, optionally containing {@code %1$s}, {@code %2$s}, {@code %3$s} and {@code %4$s}, not null
     * @return the formatted string, not null
     */
    public String toString(final String format) {
        return String.format(format, getFirst(), getSecond(), getThird(), getLast());
    }

    /**
     * <p>Gets the first element from this quadruple.</p>
     *
     * @return the first element, may be null
     */
    public F getFirst(){
        return first;
    }

    /**
     * <p>Gets the second element from this quadruple.</p>
     *
     * @return the second element, may be null
     */
    public S getSecond(){
        return second;
    }

    /**
     * <p>Gets the third element from this quadruple.</p>
     *
     * @return the third element, may be null
     */
    public T getThird(){
        return third;
    }

    /**
     * <p>Gets the last element from this quadruple.</p>
     *
     * @return the last element, may be null
     */
    public L getLast(){
        return last;
    }
}