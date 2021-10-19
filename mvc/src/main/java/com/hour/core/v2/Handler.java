package com.hour.core.v2;

import java.util.Map;
import java.util.regex.Pattern;
import java.lang.reflect.Method;

public class Handler {
    protected Object controller;
    protected Method method;
    protected Pattern pattern;
    protected Map<String, Integer> paramIndexMapping;

}
