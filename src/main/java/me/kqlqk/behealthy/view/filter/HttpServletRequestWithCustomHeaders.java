package me.kqlqk.behealthy.view.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;

public class HttpServletRequestWithCustomHeaders extends HttpServletRequestWrapper {
    private final Map<String, String> customHeaderMap = new HashMap<>();

    public HttpServletRequestWithCustomHeaders(HttpServletRequest request) {
        super(request);
    }

    public void addHeader(String name, String value) {
        customHeaderMap.put(name, value);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getHeader(name);
        if (value == null) {
            value = customHeaderMap.get(name);
        }

        String customValue = customHeaderMap.get(name);
        if (customValue != null) {
            if (!value.equals(customValue)) {
                value = customValue;
            }
        }

        return value;
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null) {
            value = customHeaderMap.get(name);
        }

        String customValue = customHeaderMap.get(name);
        if (customValue != null) {
            if (!value.equals(customValue)) {
                value = customValue;
            }
        }

        return value;
    }
}
