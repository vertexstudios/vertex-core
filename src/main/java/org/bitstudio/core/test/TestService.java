package org.bitstudio.core.test;

import org.bitstudio.core.service.IService;
import org.bitstudio.core.test.impl.Test;
import org.bitstudio.core.util.Registry;

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
