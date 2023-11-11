public class Assertion {
    /* You'll need to change the return type of the assertThat methods */
    public static ObjectAssertion assertThat(Object o) {
	    return new ObjectAssertion(o);
    }
    public static StringAssertion assertThat(String s) {
	    return new StringAssertion(s);
    }
    public static BooleanAssertion assertThat(boolean b) {
	    return new BooleanAssertion(b);
    }
    public static IntAssertion assertThat(int i) {
        return new IntAssertion(i);
    }
}
