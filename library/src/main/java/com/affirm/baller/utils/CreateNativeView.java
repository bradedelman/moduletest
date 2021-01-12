package com.affirm.baller.utils;

import com.affirm.baller.platform.Native;
import com.affirm.baller.view.NativeView;

import java.lang.reflect.Constructor;

public class CreateNativeView {

    static public NativeView create(String nativeType, Native n)
    {
        try {
            Class myClass = Class.forName("com.affirm.baller.view." + nativeType);
            Class[] types = {Native.class};
            Constructor constructor = myClass.getConstructor(types);
            Object[] parameters = {n};
            Object o = constructor.newInstance(parameters);
            return (NativeView)o;

        } catch (Exception e) {
        }
        return null;
    }
}
