import java.util.*;

public class Foo {

    @Test
    public void testc() {
        System.out.println("Test C");
    }

    @Test
    public void testa() {
        System.out.println("Test A");
        throw new RuntimeException();
    }

    @Test
    public void testb() {
        System.out.println("Test B");
        throw new NullPointerException();
    }

    @Before
    public void Before() {
        System.out.println("Before");
        // throw new RuntimeException();
    }

    @BeforeClass
    public static void BeforeClass() {
        System.out.println("BeforeClass");
    }

    @AfterClass
    public static void AfterClass() {
        System.out.println("AfterClass");
    }

    @After
    public void After() {
        System.out.println("After");
    }

    @Property
    public boolean ptest1(@IntRange(min = 0, max = 5) Integer i) {
        if (i == 4) {
            throw new RuntimeException();
        }
        return Math.abs(i.intValue()) >= 0;
    }

    @Property
    public boolean ptest2(@IntRange(min = 1, max = 5) Integer i, @IntRange(min = -5, max = 0) Integer j) {
        // System.out.println(i + ", " + j);
        return i.intValue() != j.intValue();
    }

    @Property
    public boolean ptest3(@IntRange(min = 1, max = 4) Integer i, @IntRange(min = 5, max = 8) Integer j, @IntRange(min = -4, max = 0) Integer k) {
        return i.intValue() != j.intValue();
    }

    @Property
    public boolean ptest4(@StringSet(strings={"s1", "s2", "s3", "s4"}) String s) {
        return s.equals(s);
    }

    @Property
    public boolean ptest5(@IntRange(min = 0, max = 5) Integer i, @StringSet(strings={"s1", "s2", "s3", "s4"}) String s) {
        return true;
    }

    @Property
    public boolean ptest6(@IntRange(min = 0, max = 5) Integer i, @StringSet(strings={"s1", "s2", "s3", "s4", "s5"}) String s, @IntRange(min = 4, max = 8) Integer j) {
        return true;
    }

    @Property
    public boolean ptest7(@ListLength(min = 0, max = 2) List<@IntRange(min = 5, max = 7) Integer> list) {
        return true;
    }

    @Property
    public boolean ptest8(@ListLength(min = 0, max = 2) List<@StringSet(strings={"s1", "s2", "s3", "s4", "s5"}) String> list) {
        return true;
    }

    @Property
    public boolean ptest9(@ListLength(min = 0, max = 2) List<@IntRange(min = 5, max = 7) Integer> list, @IntRange(min = 0, max = 5) Integer i) {
        return true;
    }

    @Property
    public boolean ptest11(@ForAll(name="genIntSet", times=10) Object o) {
        return true;
    }

    int count = 0;
    public Object genIntSet() {
        Set s = new HashSet();
        for (int i = 0; i < count; i++) { s.add(i); }
        count++;
        return s;
    }
    

    // @Property
    // public boolean ptest10(@ListLength(min = 0, max = 2) List<@ListLength(min = 0, max = 2) List<@IntRange(min = 5, max = 7) Integer>> list) {
    //     return true;
    // }

}