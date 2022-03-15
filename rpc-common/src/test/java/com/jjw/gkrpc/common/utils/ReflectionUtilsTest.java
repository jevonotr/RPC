package com.jjw.gkrpc.common.utils;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class ReflectionUtilsTest {

    @Test
    public void newInstance() {
        TestClass testClass = ReflectionUtils.newInstance(TestClass.class);
        assertNotNull(testClass);
    }

    @Test
    public void getPublicMethod() {
        Method[] publicMethod = ReflectionUtils.getPublicMethod(TestClass.class);
        assertEquals(1,publicMethod.length);
        String mname = publicMethod[0].getName();
        assertEquals("a",mname);


    }

    @Test
    public void invoke() {
        Method[] Methods = ReflectionUtils.getPublicMethod(TestClass.class);
        Method a = Methods[0];

        TestClass t = new TestClass();
        Object r = ReflectionUtils.invoke(t, a);

        assertEquals("a",r);
    }
}