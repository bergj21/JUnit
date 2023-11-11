import java.util.*;
import java.lang.reflect.*;
import java.lang.annotation.*;

public class Unit {
    public static Map<String, Throwable> testClass(String name) {
        Map<String, Throwable> map = new HashMap<String, Throwable>();

        List<String> tests = new ArrayList<>();
        List<String> beforeClass = new ArrayList<>();
        List<String> before = new ArrayList<>();
        List<String> afterClass = new ArrayList<>();
        List<String> after = new ArrayList<>();

        try {
        
            Class<?> c = Class.forName(name);
            Object instance = c.getConstructor().newInstance();

            
            for (Method method : c.getMethods()) {
                
                Annotation[] annotations = method.getAnnotations();
                // Assert that the number of annotations is one
                if (!assertAnnotation(annotations)) {
                    throw new RuntimeException("Invalid Annotations!");
                } 
                else if (method.isAnnotationPresent(Test.class)) {
                    tests.add(method.getName());
                } else if (method.isAnnotationPresent(Before.class)) {
                    before.add(method.getName());
                } else if (method.isAnnotationPresent(BeforeClass.class)) {
                    beforeClass.add(method.getName());
                } else if (method.isAnnotationPresent(After.class)) {
                    after.add(method.getName());
                } else if (method.isAnnotationPresent(AfterClass.class)) {
                    afterClass.add(method.getName());
                } 
            
            }

            // Sort the tests array so that it is executed in alphabetical order
            Collections.sort(tests);
            Collections.sort(beforeClass);
            Collections.sort(before);
            Collections.sort(afterClass);
            Collections.sort(after);

            try {

                // Execute the BeforeClass methods
                for (String method : beforeClass) {
                    Method m = c.getMethod(method);
                    // Check method is static
                    if (!Modifier.isStatic(m.getModifiers())) {
                            throw new RuntimeException("BeforeClass method not static");
                        }
                    m.invoke(null);
                }

                // Execute the Test methods
                for (String test : tests) {
                    // Execute the Before Methods
                    for (String method : before) {
                        Method bMethod = c.getMethod(method);
                        bMethod.invoke(instance);
                    }

                    // Execute the Test Method
                    map.put(test, null);
                    try {
                        Method m = c.getMethod(test);
                        m.invoke(instance);
                    } catch (InvocationTargetException exception) {
                        Throwable e = exception.getCause();
                        map.put(test, e);

                    }


                    // Execute the After Methods
                    for (String method : after) {
                        Method aMethod = c.getMethod(method);
                        aMethod.invoke(instance);
                    }
                }

                // Execute the BeforeClass methods
                for (String method : afterClass) {
                    Method m = c.getMethod(method);
                    // Check method is static
                    if (!Modifier.isStatic(m.getModifiers())) {
                            throw new RuntimeException("AfterClass method not static");
                        }
                    m.invoke(null);
                }
            } catch (InvocationTargetException exception) {
                throw new RuntimeException(exception.getMessage());
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return map;
    }

    private static boolean assertAnnotation(Annotation[] annotations) {
        int cnt = 0;
        for (Annotation a : annotations) {
            if (a.annotationType() == Test.class || a.annotationType() == Before.class ||
                a.annotationType() == BeforeClass.class || a.annotationType() == After.class ||
                a.annotationType() == AfterClass.class) {
                    cnt++;
            }
        }
        return cnt <= 1;
    }

    public static Map<String, Object[]> quickCheckClass(String name) {
	    Map<String, Object[]> map = new HashMap<String, Object[]>();
        List<Method> propertyMethods = new ArrayList<>();
        try {
            Class<?> c = Class.forName(name);
            Object instance = c.getConstructor().newInstance();
            Method[] methods = c.getDeclaredMethods();
            // Loop through class methods and determine which are annotated with property 
            for (Method method : c.getMethods()) {
                if (method.isAnnotationPresent(Property.class)) {
                    propertyMethods.add(method);
                } 
            }
            // Sort the property methods such that they are executed in alphabetical order
            propertyMethods.sort(Comparator.comparing(Method::getName));
            // Iterate over each method annotated with property
            for (Method method : propertyMethods) {
                Parameter[] parameters = method.getParameters();
                int numParam = parameters.length;
                // The list of all possible inputs
                List<List<Object>> permutations = new ArrayList<>();
                // Insert an empty list to start
                List<Object> empty = new ArrayList<>();
                permutations.add(empty);
                // Iterate through each parameter and generate the list of all permutations
                for (int i = 0; i < numParam; i++) {
                    Parameter p = parameters[i];
                    // Generate the new range of values according to the parameter
                    List<Object> newValues = handler(permutations, p, c);
                    // Update the permutations list to include the new parameter values
                    permutations = combine(permutations, newValues);
                }

                // Initialize map-method as null
                map.put(method.getName(), null);
                int count = 0;
                for (List<Object> permutation : permutations) {
                    if (count == 100) {
                        break;
                    }
                    Object result;
                    try {
                        result = method.invoke(instance, permutation.toArray());
                        // If method returns false update map
                        if (! (Boolean) result) {
                            map.put(method.getName(), permutation.toArray());
                            break;
                        }
                    } catch (InvocationTargetException exception) {
                        Throwable e = exception.getCause();
                        map.put(method.getName(), permutation.toArray());
                        break;
                    }
                    count++;
                }
                
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
        return map;
    }

    public static List<Object> handler(List<List<Object>> permutations, Parameter p, Class<?> c) {
        // Initialize the list that will contain the new parameter values
        List<Object> newValues = null;
        if (p.getType() == Integer.class && p.isAnnotationPresent(IntRange.class)) {
            // Generate the new input values from the IntRange
            IntRange r = p.getAnnotation(IntRange.class);
            newValues = intRange(r);
        }
        else if (p.getType() == String.class && p.isAnnotationPresent(StringSet.class)) {
            // Generate the new input values from the StringSet
            StringSet set = p.getAnnotation(StringSet.class);
            newValues = stringRange(set);
        }
        else if (p.getType() == List.class && p.isAnnotationPresent(ListLength.class)) {
            // Grab the list annotation
            ListLength len = p.getAnnotation(ListLength.class);
            // Get the annotated type from the parameter
            AnnotatedParameterizedType apt = (AnnotatedParameterizedType) p.getAnnotatedType();
            Annotation ann = apt.getAnnotatedActualTypeArguments()[0].getAnnotations()[0];
            // Generate range of values from the list argument
            List<Object> newRange = listHandler(permutations, ann, c);
            // Generate the permutations of list using the given range
            List<List<Object>> result = generatePermutation(len, newRange);
            // Flatten the list so that it can be combined with all permutations
            newValues = listFlattener(result);

        } else if (p.getType() == Object.class && p.isAnnotationPresent(ForAll.class)) {
            ForAll f = p.getAnnotation(ForAll.class);
            // Generate the new input values from the Object
            newValues = objectRange(f, c);
        } else {
            throw new RuntimeException("Invalid Property");
        }
        return newValues;
    }
  
    private static List<Object> listHandler(List<List<Object>> permutations, Annotation ann, Class<?> c) {
        List<Object> newValues = new ArrayList<>();
        if (ann instanceof IntRange) {
            IntRange r = (IntRange) ann;
            newValues = intRange(r);
        } else if (ann instanceof StringSet) {
            StringSet set = (StringSet) ann;
            newValues = stringRange(set);
        } else if (ann instanceof ForAll) {
            ForAll f = (ForAll) ann;
            newValues = objectRange(f, c);
        } else {
            throw new RuntimeException("I am a failure");
        }
        return newValues;
    }

    private static List<Object> listFlattener(List<List<Object>> input) {
        List<Object> flatList = new ArrayList<>();
        for (List<Object> permutation : input) {
            flatList.add(permutation);
        }
        return flatList;
    }

    private static List<List<Object>> generatePermutation(ListLength len, List<Object> input) {
        List<List<Object>> permutations = new ArrayList<>();
        permutationHelper(input, len.min(), len.max(), new ArrayList<>(), permutations);
        return permutations;
    }

    private static void permutationHelper(List<Object> input, int min, int max, List<Object> currPermutation, List<List<Object>> result) {
        if (currPermutation.size() >= min && currPermutation.size() <= max) {
            result.add(new ArrayList<>(currPermutation));
        }
        if (currPermutation.size() == max) {
            return;
        }
        for (int i = 0; i < input.size(); i++) {
            currPermutation.add(input.get(i));
            permutationHelper(input, min, max, currPermutation, result);
            currPermutation.remove(currPermutation.size() - 1);
        }
    }

    private static List<List<Object>> combine(List<List<Object>> permutations, List<Object> input) {
        List<List<Object>> newList = new ArrayList<>();
        // Iterate over each permutation in the list of permutations
        for (List<Object> permutation : permutations) {
            // Iterate over each new object in the new parameter 
            for (Object o : input) {
                // Create a new permutation for each new input
                List<Object> newPermutation = new ArrayList<>(permutation);
                newPermutation.add(o);
                // Add to the new list of permutations
                newList.add(newPermutation);
            }
        }
        return newList;
    }

    private static List<Object> intRange(IntRange range) {
        List<Object> values = new ArrayList<>();
        for (int i = range.min(); i <= range.max(); i++) {
            values.add(i);
        }
        return values;
    } 

    private static List<Object> stringRange(StringSet set) {
        List<Object> values = new ArrayList<>();
        String[] strings = set.strings(); 
        for (int i = 0; i < strings.length; i++) {
            values.add(strings[i]);
        }
        return values;
    } 

    private static List<Object> objectRange(ForAll f, Class<?> c) {
        List<Object> values = new ArrayList<>();
        String name = f.name();
        int times = f.times();
        Method m;
        try { 
            // Get the method described in the annotation
            m = c.getMethod(name); 
            Object instance = c.getConstructor().newInstance();
            // Invoke the method a specified number of times
            for (int i = 0; i < times; i++) {
                Object o = m.invoke(instance);
                values.add(o);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Method Not Found");
        }
        return values;
    }


}
