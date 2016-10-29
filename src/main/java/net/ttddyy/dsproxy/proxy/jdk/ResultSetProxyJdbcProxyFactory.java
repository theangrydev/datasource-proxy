package net.ttddyy.dsproxy.proxy.jdk;

import net.ttddyy.dsproxy.proxy.InterceptorHolder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * Extension of {@link net.ttddyy.dsproxy.proxy.jdk.JdkJdbcProxyFactory} that also proxies any
 * {@link java.sql.ResultSet} results so that they can be consumed more than once.
 *
 * @author Liam Williams
 */
public class ResultSetProxyJdbcProxyFactory extends JdkJdbcProxyFactory {

    private final StatementResultSetResultProxyFactory statementResultSetResultProxyFactory;

    public ResultSetProxyJdbcProxyFactory() {
        this(new StatementResultSetResultProxyFactory());
    }

    ResultSetProxyJdbcProxyFactory(StatementResultSetResultProxyFactory statementResultSetResultProxyFactory) {
        this.statementResultSetResultProxyFactory = statementResultSetResultProxyFactory;
    }

    @Override
    public Statement createStatement(Statement statement, InterceptorHolder interceptorHolder) {
        return superCreateStatement(statementResultSetResultProxyFactory.proxy(statement, Statement.class), interceptorHolder);
    }

    @Override
    public Statement createStatement(Statement statement, InterceptorHolder interceptorHolder, String dataSourceName) {
        return superCreateStatement(statementResultSetResultProxyFactory.proxy(statement, Statement.class), interceptorHolder, dataSourceName);
    }

    @Override
    public PreparedStatement createPreparedStatement(PreparedStatement preparedStatement, String query, InterceptorHolder interceptorHolder) {
        return superCreatePreparedStatement(statementResultSetResultProxyFactory.proxy(preparedStatement, PreparedStatement.class), query, interceptorHolder);
    }

    @Override
    public PreparedStatement createPreparedStatement(PreparedStatement preparedStatement, String query, InterceptorHolder interceptorHolder, String dataSourceName) {
        return superCreatePreparedStatement(statementResultSetResultProxyFactory.proxy(preparedStatement, PreparedStatement.class), query, interceptorHolder, dataSourceName);
    }

    @Override
    public CallableStatement createCallableStatement(CallableStatement callableStatement, String query, InterceptorHolder interceptorHolder, String dataSourceName) {
        return superCreateCallableStatement(statementResultSetResultProxyFactory.proxy(callableStatement, CallableStatement.class), query, interceptorHolder, dataSourceName);
    }

    // The following are visible for testing only.
    // The alternative was decorating JdbcProxyFactory but this led to having to duplicate all the
    // JdbcProxyFactory methods to add an additional JdbcProxyFactory parameter. This seemed the lesser evil...

    Statement superCreateStatement(Statement proxy, InterceptorHolder interceptorHolder) {
        return super.createStatement(proxy, interceptorHolder);
    }

    Statement superCreateStatement(Statement proxy, InterceptorHolder interceptorHolder, String dataSourceName) {
        return super.createStatement(proxy, interceptorHolder, dataSourceName);
    }

    PreparedStatement superCreatePreparedStatement(PreparedStatement proxy, String query, InterceptorHolder interceptorHolder) {
        return super.createPreparedStatement(proxy, query, interceptorHolder);
    }

    PreparedStatement superCreatePreparedStatement(PreparedStatement proxy, String query, InterceptorHolder interceptorHolder, String dataSourceName) {
        return super.createPreparedStatement(proxy, query, interceptorHolder, dataSourceName);
    }

    CallableStatement superCreateCallableStatement(CallableStatement proxy, String query, InterceptorHolder interceptorHolder, String dataSourceName) {
        return super.createCallableStatement(proxy, query, interceptorHolder, dataSourceName);
    }
}
