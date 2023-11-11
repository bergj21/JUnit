import java.util.*;

public class Main {
    public static void main(String[] args) {
        

        Unit u = new Unit();
        // Map<String, Throwable> map = u.testClass("Foo");
        Map<String, Object[]> map = u.quickCheckClass("Foo");
        // for (String key : map.keySet()) {
        //     System.out.println(key + ": " + map.get(key));
        // }

        // Assertion.assertThat(3).isEqualTo(3).isGreaterThan(1).isLessThan(4).isEqualTo(4);
        // Assertion.assertThat(true).isEqualTo(true).isTrue().isFalse();
        Assertion.assertThat("Hello").isEqualTo("Hello").startsWith("He").contains("ell");
        Assertion.assertThat("Hello").isNotEqualTo("Hell");

        // class Bar {
        //     public int i;
        //     public boolean b; 

        //     public Bar(int i, boolean b) {
        //         this.i = i;
        //         this.b = b;
        //     }

        //     public boolean equals(Object obj) {
        //         if (this == obj) {
        //             return true;
        //         }

        //         if (!(obj instanceof Bar)) {
        //             return false;
        //         }

        //         Bar b2 = (Bar) obj;

        //         return i == b2.i && b == b2.b;
        //     }
        // }

        // Bar o1 = new Bar(1, true);
        // Bar o2 = new Bar(1, true);
        // Bar o3 = new Bar(2, false);
        // Assertion.assertThat(o1).isInstanceOf(Bar.class);
        // Assertion.assertThat("Hello").isNotEqualTo(o1);

        /*
        for (Map.Entry<String, Object[]> entry : map.entrySet()) {
            String key = entry.getKey();
            Object[] value = entry.getValue();
            System.out.print("Key: " + key + " {");
            if (value != null) {
                for (int i = 0; i < value.length; i++) {
                    System.out.print(value[i] + ", ");
                }
            } else {
                System.out.print("Value: " + value);
            }
            System.out.println("}");
        }
        */

    }
}