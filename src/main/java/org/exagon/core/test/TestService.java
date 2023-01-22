package org.exagon.core.test;

import org.exagon.core.service.IService;
import org.exagon.core.test.impl.Test;
import org.exagon.core.util.Registry;

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
