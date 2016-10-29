package net.ttddyy.dsproxy.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * This proxies any {@link java.sql.ResultSet} results so that they can be consumed more than once.
 *
 * @param <T> The {@link java.sql.Statement} type the proxy is for.
 *
 * @author Liam Williams
 */
class StatementResultSetResultInvocationHandler<T extends Statement> implements InvocationHandler {

    private final T target;

    StatementResultSetResultInvocationHandler(T target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = method.invoke(target, args);
        if (result instanceof ResultSet) {
            return ResultSetInvocationHandler.proxy((ResultSet) result);
        }
        return result;
    }
}
