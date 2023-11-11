public class IntAssertion {
    private int i;

    public IntAssertion(int i) {
        this.i = i;
    }

    public IntAssertion isEqualTo(int i2) {
        if (i != i2) {
            throw new RuntimeException("i != i2");
        }
        return this;
    }

    public IntAssertion isLessThan(int i2) {
        if (i >= i2) {
            throw new RuntimeException("i >= i2");
        }
        return this;
    }

    public IntAssertion isGreaterThan(int i2) {
        if (i <= i2) {
            throw new RuntimeException("i <= i2");
        }
        return this;
    }
}