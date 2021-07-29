package net.threader.lib.test;

import net.threader.lib.Registry;
import net.threader.lib.test.impl.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

public class TestService {
    private Registry<ITest> registry = new Registry<>();

    public ITest load(Method method) {
        TestRunner runner = method.getAnnotation(TestRunner.class);
        return new Test(runner.id(), method);
    }

    public void loadAll(Class<?> clazz) {
        Arrays.stream(clazz.getDeclaredMethods()).forEach(this::load);
    }
}
