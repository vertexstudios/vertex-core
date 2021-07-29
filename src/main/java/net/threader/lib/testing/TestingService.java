package net.threader.lib.testing;

import net.threader.lib.testing.impl.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TestingService {
    private Map<String, ITest> registry = new HashMap<>();

    public ITest loadTest(Method method) {
        TestRunner runner = method.getAnnotation(TestRunner.class);
        return new Test(runner.id(), method);
    }

    public void loadTests(Class<?> clazz) {
        Arrays.stream(clazz.getDeclaredMethods()).forEach(this::loadTest);
    }
}
