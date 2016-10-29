package net.ttddyy.dsproxy.proxy.jdk;

import net.ttddyy.dsproxy.proxy.ProxyJdbcObject;

import java.lang.reflect.Proxy;
import java.sql.Statement;

/**
 * Factory for proxying a {@link java.sql.Statement} with a {@link StatementResultSetResultInvocationHandler}.
 *
 * @author Liam Williams
 */
class StatementResultSetResultProxyFactory {

    public <T extends Statement> T proxy(T target, Class<T> interfaceToProxy) {
        return interfaceToProxy.cast(Proxy.newProxyInstance(ProxyJdbcObject.class.getClassLoader(), new Class<?>[]{ProxyJdbcObject.class, interfaceToProxy}, new StatementResultSetResultInvocationHandler<T>(target)));
    }
}
