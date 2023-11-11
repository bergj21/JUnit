public class ObjectAssertion {
    private Object o;

    public ObjectAssertion(Object o) {
        this.o = o;
    }

    public ObjectAssertion isNotNull() {
        if (o == null) {
            throw new RuntimeException("o is null");
        }
        return this;
    }

    public ObjectAssertion isNull() {
        if (o != null) {
            throw new RuntimeException("o is not null");
        }
        return this;
    } 

    public ObjectAssertion isEqualTo(Object o2) {
        if (!o.equals(o2)) {
            throw new RuntimeException("o is not equal to o2");
        }
        return this;
    }

    public ObjectAssertion isNotEqualTo(Object o2) {
        if (o.equals(o2)) {
            throw new RuntimeException("o is equal to o2");
        }
        return this;
    }

    public ObjectAssertion isInstanceOf(Class c) {
        if (!(c.isInstance(o))) {
            throw new RuntimeException("o is not instance of c");
        }
        return this;
    }
}