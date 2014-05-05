/*
 *  CaptainBern-Reflection-Framework contains several utils and tools
 *  to make Reflection easier.
 *  Copyright (C) 2014  CaptainBern
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.captainbern.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

public class AbstractAccess<T> implements Access<T> {

    protected Class<T> clazz;
    private boolean forceAccess;

    public AbstractAccess(Class<T> clazz, boolean forceAccess) {
        this.clazz = clazz;
        this.forceAccess = forceAccess;
    }

    @Override
    public Class<T> getReflectedClass() {
        return this.clazz;
    }

    @Override
    public Set<ReflectedField> getFields() {
        Set<ReflectedField> fields = new LinkedHashSet<ReflectedField>();

        if(forceAccess) {
            for(Field field : getReflectedClass().getDeclaredFields()) {
                fields.add(Reflection.reflect(field));
            }
        }

        for(Field field : getReflectedClass().getFields()) {
            fields.add(Reflection.reflect(field));
        }
        return fields;
    }

    @Override
    public Set<ReflectedField> getDeclaredFields(Class<?> exemptedSuperClass) {
        Set<ReflectedField> fields = new LinkedHashSet<ReflectedField>();

        Class<?> current = this.clazz;

        while(current != null && current != exemptedSuperClass) {
            for(Field field : current.getDeclaredFields()) {
                fields.add(Reflection.reflect(field));
            }
            current = current.getSuperclass();
        }

        return fields;
    }

    @Override
    public List<ReflectedField> getFieldsByType(Class<?> type) {
        List<ReflectedField> fields = new ArrayList<ReflectedField>();

        for(ReflectedField field : getFields()) {
            if(type.isAssignableFrom(field.getType().getReflectedClass())) {
                fields.add(field);
            }
        }

        return fields;
    }

    @Override
    public ReflectedField getFieldByNameAndType(String name, Class<?> type) {
        List<ReflectedField> fields = getFieldsByType(type);

        if(fields.size() > 0) {
            for (ReflectedField field : fields) {
                if (field.getName().equals(name)) {
                    return field;
                }
            }
        }

        throw new IllegalArgumentException("Failed to find field: " + name + " in class: " + this.clazz.getCanonicalName());
    }

    @Override
    public ReflectedField getFieldByName(String name) {
        for(ReflectedField field : getFields()) {
            if(field.getName().equals(name)) {
                return field;
            }
        }

        throw new IllegalArgumentException("Failed to find field: " + name + " in class: " + this.clazz.getCanonicalName());
    }

    @Override
    public Set<ReflectedMethod> getMethods() {
        Set<ReflectedMethod> methods = new HashSet<>();

        if(forceAccess) {
            for(Method method : this.clazz.getDeclaredMethods()) {
                methods.add(Reflection.reflect(method));
            }
        }

        for(Method method : this.clazz.getMethods()) {
            methods.add(Reflection.reflect(method));
        }

        return methods;
    }

    @Override
    public Set<ReflectedMethod> getDeclaredMethods(Class<?> exemptedSuperClass) {
        if(forceAccess) {
            Set<ReflectedMethod> methods = new LinkedHashSet<ReflectedMethod>();
            Class<?> current = this.clazz;

            while(current != null && current != exemptedSuperClass) {
                for(Method method : current.getDeclaredMethods()) {
                    methods.add(Reflection.reflect(method));
                }
                current = current.getSuperclass();
            }

            return methods;
        }

        return getMethods();
    }

    @Override
    public ReflectedMethod getMethod(String name, Class<?> returnType, Class[] arguments) {
        for(ReflectedMethod method : getMethods()) {
            if((name == null || method.getName().equals(name)) && (returnType == null || method.member().getReturnType().equals(returnType)) && (arguments == null || Arrays.equals(arguments, method.member().getParameterTypes()))) {
                return method;
            }
        }
        return null;
    }

    @Override
    public Set<ReflectedConstructor> getConstructors() {
        Set<ReflectedConstructor> constructors = new HashSet<>();

        if(forceAccess) {
            for(Constructor constructor : this.clazz.getDeclaredConstructors()) {
                constructors.add(Reflection.reflect(constructor));
            }
        }

        for(Constructor constructor : this.clazz.getConstructors()) {
            constructors.add(Reflection.reflect(constructor));
        }

        return constructors;
    }

    @Override
    public Set<ReflectedConstructor> getDeclaredConstructors(Class<?> exemptedSuperClass) {
        if(forceAccess) {
            Set<ReflectedConstructor> constructors = new LinkedHashSet<ReflectedConstructor>();
            Class<?> current = this.clazz;

            while(current != null && current != exemptedSuperClass) {
                for(Constructor constructor : current.getDeclaredConstructors()) {
                    constructors.add(Reflection.reflect(constructor));
                }
                current = current.getSuperclass();
            }

            return constructors;
        }

        return getConstructors();
    }

    @Override
    public boolean isAssignableFrom(Class<?> clazz) {
        return this.getReflectedClass().isAssignableFrom(clazz);
    }

    @Override
    public boolean isAssignableFromObject(Object object) {
        return this.getReflectedClass().isAssignableFrom(object.getClass());
    }

    @Override
    public boolean isInstanceOf(Object object) {
        return this.getReflectedClass().isInstance(object);
    }

    @Override
    public Type getType() {
        return this.getReflectedClass().getGenericSuperclass();
    }
}
