package hr.algebra.carcassonnegame2.utils;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public final class ReflectionUtils {
    private ReflectionUtils() {
    }


    public static void readClassAndMembersInfo(Class<?>clazz, StringBuilder classAndMembersInfo) {
        readClassInfo(clazz, classAndMembersInfo);
        initDiv(classAndMembersInfo, "Fields");
        appendFields(clazz, classAndMembersInfo);
        closeDiv(classAndMembersInfo);
        initDiv(classAndMembersInfo, "Methods");
        appendMethods(clazz, classAndMembersInfo);
        closeDiv(classAndMembersInfo);
        initDiv(classAndMembersInfo, "Constructors");
        appendConstructors(clazz, classAndMembersInfo);
        closeDiv(classAndMembersInfo);
    }

    public static void readClassInfo(Class<?> clazz, StringBuilder classInfo) {
        classInfo.append("<div>");
        appendPackage(clazz, classInfo);
        closeDiv(classInfo);
        appendModifiers(clazz, classInfo);
        classInfo.append(" ").append(clazz.getName());
        appendParent(clazz, classInfo, true);
        appendInterfaces(clazz, classInfo);
        classInfo.append("</div>");
    }

    private static void initDiv(StringBuilder sb, String title){
        sb.append("<div><h2>" + title +"</h2>");
    }
    private static void closeDiv(StringBuilder sb){
        sb.append("</div>");
    }

    private static void appendPackage(Class<?> clazz, StringBuilder classInfo) {
        classInfo
                .append("<strong>package </strong>")
                .append(clazz.getPackage().getName());
    }

    private static void appendModifiers(Class<?> clazz, StringBuilder classInfo) {
        classInfo.append("<strong>").append(Modifier.toString(clazz.getModifiers())).append(" </strong>");
    }

    private static void appendParent(Class<?> clazz, StringBuilder classInfo, boolean first) {
        Class<?> parent = clazz.getSuperclass();
        if(parent == null) {
            return;
        }
        if (first) {
            classInfo
                    .append("<br><strong>extends </strong>");
        }
        classInfo
                .append(" ")
                .append(parent.getName());
        appendParent(parent, classInfo, false);
    }

    private static void appendInterfaces(Class<?> clazz, StringBuilder classInfo) {
        if (clazz.getInterfaces().length > 0) {
            classInfo
                    .append("<br><strong>extends </strong>")
                    .append(
                            Arrays.stream(clazz.getInterfaces())
                                    .map(Class::getSimpleName)
                                    .collect(Collectors.joining(" "))
                    );
        }
    }

    private static void appendFields(Class<?> clazz, StringBuilder classAndMembersInfo) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            classAndMembersInfo.append("<p>")
                    .append(convertModifiersToString(field.getModifiers()))
                    .append(field.getType())
                    .append("<strong> ")
                    .append(field.getName())
                    .append("</strong></p>");
        }
    }

    public static String convertModifiersToString(int modifiers) {
        StringBuilder sb = new StringBuilder();
        if (Modifier.isPublic(modifiers)) {
            sb.append("<strong>public </strong>");
        }
        if (Modifier.isProtected(modifiers)) {
            sb.append("protected ");
        }
        if (Modifier.isPrivate(modifiers)) {
            sb.append("<u>private</u> ");
        }
        if (Modifier.isStatic(modifiers)) {
            sb.append("<em>static </em>");
        }
        if (Modifier.isFinal(modifiers)) {
            sb.append("<strong>final </strong>");
        }
        if (Modifier.isAbstract(modifiers)) {
            sb.append("<u>abstract </u>");
        }
        if (Modifier.isTransient(modifiers)) {
            sb.append("transient ");
        }
        if (Modifier.isVolatile(modifiers)) {
            sb.append("volatile ");
        }
        if (Modifier.isSynchronized(modifiers)) {
            sb.append("synchronized ");
        }
        if (Modifier.isNative(modifiers)) {
            sb.append("native ");
        }
        if (Modifier.isStrict(modifiers)) {
            sb.append("strictfp ");
        }
        return sb.toString();
    }

    private static void appendMethods(Class<?> clazz, StringBuilder classAndMembersInfo) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            appendAnnotations(method, classAndMembersInfo);
            classAndMembersInfo
                    .append("<br>")
                    .append(convertModifiersToString(method.getModifiers()))
                    .append(" ")
                    .append(method.getReturnType())
                    .append(" <strong>")
                    .append(method.getName())
                    .append(" </strong>");
            appendParameters(method, classAndMembersInfo);
            appendExceptions(method, classAndMembersInfo);
            classAndMembersInfo
                    .append("<br><br>");
        }
    }
    private static void appendAnnotations(Executable executable, StringBuilder classAndMembersInfo) {
        classAndMembersInfo.append(
                Arrays.stream(executable.getAnnotations())
                        .map(Objects::toString)
                        .collect(Collectors.joining(System.lineSeparator())));
    }
    private static void appendParameters(Executable executable, StringBuilder classAndMembersInfo) {
        classAndMembersInfo.append("(");
        for(Parameter parameter: executable.getParameters()){
            classAndMembersInfo.append(parameter.getType()).append("<em> ").append(parameter.getName()).append(" </em>");
        }
        classAndMembersInfo.append(")");
    }
    private static void appendExceptions(Executable executable, StringBuilder classAndMembersInfo) {
        if (executable.getExceptionTypes().length > 0) {
            classAndMembersInfo.append("<strong> throws </strong>");
            classAndMembersInfo.append(
                    Arrays.stream(executable.getExceptionTypes())
                            .map(Class::getName)
                            .collect(Collectors.joining(" "))
            );
        }
    }

    private static void appendConstructors(Class<?> clazz, StringBuilder classAndMembersInfo) {
        Constructor[] constructors = clazz.getDeclaredConstructors();
        for (Constructor constructor : constructors) {
            appendAnnotations(constructor, classAndMembersInfo);
            classAndMembersInfo
                    .append("<br>")
                    .append(convertModifiersToString(constructor.getModifiers()))
                    .append(" <em>")
                    .append(constructor.getName())
                    .append("</em>");
            appendParameters(constructor, classAndMembersInfo);
            appendExceptions(constructor, classAndMembersInfo);
            classAndMembersInfo
                    .append("<br><br>");
        }
    }
}