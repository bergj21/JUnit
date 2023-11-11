public class BooleanAssertion {
    private boolean b;

    public BooleanAssertion(boolean b) {
        this.b = b;
    }

    public BooleanAssertion isEqualTo(boolean b2) {
        if (b != b2) {
            throw new RuntimeException("b != b2");
        }
        return this;
    }

    public BooleanAssertion isTrue() {
        if (!b) {
            throw new RuntimeException("b is false");
        }
        return this;
    }

    public BooleanAssertion isFalse() {
        if (b) {
            throw new RuntimeException("b is true");
        }
        return this;
    }
}