package net.ttddyy.dsproxy.proxy.jdk;

import net.ttddyy.dsproxy.proxy.InterceptorHolder;
import org.junit.Test;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Liam Williams
 */
public class ResultSetProxyJdbcProxyFactoryTest {

    private static final String DATA_SOURCE_NAME = "DataSource";
    private static final String QUERY = "SELECT * FROM test";

    private final StatementResultSetResultProxyFactory statementResultSetResultProxyFactory = mock(StatementResultSetResultProxyFactory.class);

    private final ResultSetProxyJdbcProxyFactory resultSetProxyJdbcProxyFactory = spy(new ResultSetProxyJdbcProxyFactory(statementResultSetResultProxyFactory));

    private final Statement originalStatement = mock(Statement.class);
    private final Statement resultSetProxiedStatement = mock(Statement.class);
    private final Statement delegateProxiedStatement = mock(Statement.class);

    private final PreparedStatement originalPreparedStatement = mock(PreparedStatement.class);
    private final PreparedStatement resultSetProxiedPreparedStatement = mock(PreparedStatement.class);
    private final PreparedStatement delegateProxiedPreparedStatement = mock(PreparedStatement.class);

    private final CallableStatement originalCallableStatement = mock(CallableStatement.class);
    private final CallableStatement resultSetProxiedCallableStatement = mock(CallableStatement.class);
    private final CallableStatement delegateProxiedCallableStatement = mock(CallableStatement.class);

    private final InterceptorHolder interceptorHolder = mock(InterceptorHolder.class);

    @Test
    public void createStatementDelegatesWithAProxiedStatement() throws SQLException {
        when(statementResultSetResultProxyFactory.proxy(originalStatement, Statement.class)).thenReturn(resultSetProxiedStatement);
        when(resultSetProxyJdbcProxyFactory.superCreateStatement(resultSetProxiedStatement, interceptorHolder)).thenReturn(delegateProxiedStatement);

        Statement statement = resultSetProxyJdbcProxyFactory.createStatement(originalStatement, interceptorHolder);

        assertThat(statement).isSameAs(delegateProxiedStatement);
    }

    @Test
    public void createStatementWithDataSourceNameDelegatesWithAProxiedStatement() throws SQLException {
        when(statementResultSetResultProxyFactory.proxy(originalStatement, Statement.class)).thenReturn(resultSetProxiedStatement);
        when(resultSetProxyJdbcProxyFactory.superCreateStatement(resultSetProxiedStatement, interceptorHolder, DATA_SOURCE_NAME)).thenReturn(delegateProxiedStatement);

        Statement statement = resultSetProxyJdbcProxyFactory.createStatement(originalStatement, interceptorHolder, DATA_SOURCE_NAME);

        assertThat(statement).isSameAs(delegateProxiedStatement);
    }

    @Test
    public void createPreparedStatementDelegatesWithAProxiedStatement() throws SQLException {
        when(statementResultSetResultProxyFactory.proxy(originalPreparedStatement, PreparedStatement.class)).thenReturn(resultSetProxiedPreparedStatement);
        when(resultSetProxyJdbcProxyFactory.superCreatePreparedStatement(resultSetProxiedPreparedStatement, QUERY, interceptorHolder)).thenReturn(delegateProxiedPreparedStatement);

        PreparedStatement preparedStatement = resultSetProxyJdbcProxyFactory.createPreparedStatement(originalPreparedStatement, QUERY, interceptorHolder);

        assertThat(preparedStatement).isSameAs(delegateProxiedPreparedStatement);
    }

    @Test
    public void createPreparedStatementWithDataSourceNameDelegatesWithAProxiedStatement() throws SQLException {
        when(statementResultSetResultProxyFactory.proxy(originalPreparedStatement, PreparedStatement.class)).thenReturn(resultSetProxiedPreparedStatement);
        when(resultSetProxyJdbcProxyFactory.superCreatePreparedStatement(resultSetProxiedPreparedStatement, QUERY, interceptorHolder, DATA_SOURCE_NAME)).thenReturn(delegateProxiedPreparedStatement);

        PreparedStatement preparedStatement = resultSetProxyJdbcProxyFactory.createPreparedStatement(originalPreparedStatement, QUERY, interceptorHolder, DATA_SOURCE_NAME);

        assertThat(preparedStatement).isSameAs(delegateProxiedPreparedStatement);
    }

    @Test
    public void createCallableStatementWithDataSourceNameDelegatesWithAProxiedStatement() throws SQLException {
        when(statementResultSetResultProxyFactory.proxy(originalCallableStatement, CallableStatement.class)).thenReturn(resultSetProxiedCallableStatement);
        when(resultSetProxyJdbcProxyFactory.superCreateCallableStatement(resultSetProxiedCallableStatement, QUERY, interceptorHolder, DATA_SOURCE_NAME)).thenReturn(delegateProxiedCallableStatement);

        PreparedStatement preparedStatement = resultSetProxyJdbcProxyFactory.createCallableStatement(originalCallableStatement, QUERY, interceptorHolder, DATA_SOURCE_NAME);

        assertThat(preparedStatement).isSameAs(delegateProxiedCallableStatement);
    }
}