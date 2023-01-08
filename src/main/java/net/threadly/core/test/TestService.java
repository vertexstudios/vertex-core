package net.threadly.core.test;

import net.threadly.core.util.Registry;
import net.threadly.core.service.IService;
import net.threadly.core.test.impl.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

public class TestService implements IService {
    private Registry<String, ITest> registry = new Registry<>();

    public ITest load(Method method) {
        TestRunner runner = method.getAnnotation(TestRunner.class);
        return new Test(runner.id(), method);
    }

    public void loadAll(Class<?> clazz) {
        Arrays.stream(clazz.getDeclaredMethods()).forEach(this::load);
    }
}
