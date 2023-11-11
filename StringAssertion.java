public class StringAssertion {
    private String s;

    public StringAssertion(String s) {
        this.s = s;
    }

    public StringAssertion isNotNull() {
        if (this.s == null) {
            throw new RuntimeException("o is null");
        }
        return this;
    }

    public StringAssertion isNull() {
        if (this.s != null) {
            throw new RuntimeException("o is not null");
        }
        return this;
    }

    public StringAssertion isEqualTo(Object o2) {
        String str = (String) o2;
        if (!s.equals(str)) {
            throw new RuntimeException("o2 is not equal");
        }
        return this;
    }

    public StringAssertion isNotEqualTo(Object o2) {
        // if (o2 == null && this.s == null) {
        //     System.out.println("Here");
        //     throw new RuntimeException("o2 is equal");
        // }
        String str = (String) o2;
        if (s.equals(str)) {
            throw new RuntimeException("o2 is equal");
        }
        return this;
    }

    public StringAssertion startsWith(String s2) {
        if (!s.startsWith(s2)) {
            throw new RuntimeException("s does not start with s2");
        }
        return this;
    }

    public StringAssertion isEmpty() {
        if (s != "") {
            throw new RuntimeException("s not empty string");
        }
        return this;
    }

    public StringAssertion contains(String s2) {
        if (!s.contains(s2)) {
            throw new RuntimeException("s does not contain s2");
        }
        return this;
    }
}