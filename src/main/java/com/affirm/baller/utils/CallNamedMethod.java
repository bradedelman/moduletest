package com.affirm.baller.utils;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

public class CallNamedMethod {

    static public Object call(Object o, String method, List<Object> args)
    {
        Class javaClass = o.getClass();

        Class types[] = new Class[args.size()];
        Object useArgs[] = new Object[args.size()];
        Iterator<Object> i = args.iterator();
        int k = 0;
        while (i.hasNext()) {
            Object arg = i.next();
            Object useArg = arg;
            Class argClass = arg.getClass();
            Class useClass = argClass;
            // fix up Kotlin types
            if (argClass.equals(Boolean.class)) {
                useClass = boolean.class;
            }
            if (argClass.equals(Double.class)) {
                useClass = int.class;
                useArg = ((Double)arg).intValue();
            }
            types[k]  = useClass;
            useArgs[k] = useArg;
            k++;
        };

        Method javaMethod = null;
        try {
            javaMethod = javaClass.getMethod(method, types);
            switch (args.size()) {
                case 0: return javaMethod.invoke(o);
                case 1: return javaMethod.invoke(o, useArgs[0]);
                case 2: return javaMethod.invoke(o, useArgs[0], useArgs[1]);
                case 3: return javaMethod.invoke(o, useArgs[0], useArgs[1], useArgs[2]);
                case 4: return javaMethod.invoke(o, useArgs[0], useArgs[1], useArgs[2], useArgs[3]);
                case 5: return javaMethod.invoke(o, useArgs[0], useArgs[1], useArgs[2], useArgs[3], useArgs[4]);
                case 6: return javaMethod.invoke(o, useArgs[0], useArgs[1], useArgs[2], useArgs[3], useArgs[4], useArgs[5]);
            }
        } catch (Exception e) {
        }

        return null;
    }
}
