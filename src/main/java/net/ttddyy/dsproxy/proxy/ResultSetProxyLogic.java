package net.ttddyy.dsproxy.proxy;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

public class ResultSetProxyLogic {

    private final Map<String, Integer> columnNameToIndex;
    private final ResultSet target;
    private final int columnCount;

    private int resultPointer;
    private boolean resultSetConsumed;
    private boolean closed;
    private Object[] currentResult;
    private final List<Object[]> allResults = new ArrayList<Object[]>();

    private ResultSetProxyLogic(Map<String, Integer> columnNameToIndex, ResultSet target, int columnCount) throws SQLException {
        this.columnNameToIndex = columnNameToIndex;
        this.target = target;
        this.columnCount = columnCount;
    }

    public static ResultSetProxyLogic resultSetProxyLogic(ResultSet target) throws SQLException {
        ResultSetMetaData metaData = target.getMetaData();
        int columnCount = metaData.getColumnCount();
        return new ResultSetProxyLogic(columnNameToIndex(metaData), target, columnCount);
    }

    private static Map<String, Integer> columnNameToIndex(ResultSetMetaData metaData) throws SQLException {
        Map<String, Integer> columnNameToIndex = new HashMap<String, Integer>();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            columnNameToIndex.put(metaData.getColumnLabel(i).toUpperCase(), i);
        }
        return columnNameToIndex;
    }

    public Object invoke(Method method, Object[] args) throws Throwable {
        if (isGetTargetMethod(method)) {
            // ProxyJdbcObject interface has a method to return original object.
            return target;
        }
        if (isGetMetaDataMethod(method)) {
            return method.invoke(target, args);
        }
        if (isCloseMethod(method)) {
            closed = true;
            return method.invoke(target, args);
        }
        if (closed) {
            throw new SQLException("Already closed");
        }
        if (resultSetConsumed) {
            if (isGetMethod(method) && resultPointer >= allResults.size()) {
                throw new SQLException(format("Result set exhausted. There were %d result(s) only", allResults.size()));
            }
            if (isGetMethod(method) && resultPointer < allResults.size()) {
                int columnIndex = determineColumnIndex(method, args);
                return currentResult[columnIndex];
            }
            if (isNextMethod(method) && resultPointer < allResults.size() - 1) {
                currentResult = allResults.get(resultPointer);
                resultPointer++;
                return true;
            }
            if (isNextMethod(method) && resultPointer == allResults.size() - 1) {
                return false;
            }
        } else {
            if (isGetMethod(method)) {
                int columnIndex = determineColumnIndex(method, args);
                Object result = method.invoke(target, args);
                currentResult[columnIndex] = result;
                return result;
            }
            if (isNextMethod(method)) {
                Object result = method.invoke(target, args);
                currentResult = new Object[columnCount + 1];
                allResults.add(currentResult);
                return result;
            }
            if (isBeforeFirstMethod(method)) {
                resultPointer = 0;
                resultSetConsumed = true;
                return null;
            }
        }
        throw new UnsupportedOperationException(format("Method '%s' is not supported by this proxy", method));
    }

    private boolean isCloseMethod(Method method) {
        return method.getName().equals("close");
    }

    private boolean isGetTargetMethod(Method method) {
        return method.getName().equals("getTarget");
    }

    private boolean isGetMetaDataMethod(Method method) {
        return method.getName().equals("getMetaData");
    }

    private boolean isGetMethod(Method method) {
        return method.getName().startsWith("get") && method.getParameterTypes().length > 0;
    }

    private boolean isNextMethod(Method method) {
        return method.getName().equals("next");
    }

    private boolean isBeforeFirstMethod(Method method) {
        return method.getName().equals("beforeFirst");
    }

    private int determineColumnIndex(Method method, Object[] args) {
        Integer columnIndex = columnIndexParameter(args);
        if (columnIndex != null) {
            return columnIndex;
        }
        String columnName = columnNameParameter(args);
        if (columnName != null) {
            return columnNameToIndex(columnName);
        }
        throw new IllegalStateException(format("Could not determine column index for method '%s'", method));
    }

    private Integer columnNameToIndex(String s) {
        return columnNameToIndex.get(s.toUpperCase());
    }

    // assumed to be the first int parameter
    private Integer columnIndexParameter(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof Integer) {
                return (Integer) arg;
            }
        }
        return null;
    }

    // assumed to be the first String parameter
    private String columnNameParameter(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof String) {
                return (String) arg;
            }
        }
        return null;
    }
}
