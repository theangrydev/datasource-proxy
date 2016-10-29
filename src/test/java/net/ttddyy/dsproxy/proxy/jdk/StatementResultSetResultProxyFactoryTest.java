package net.ttddyy.dsproxy.proxy.jdk;

import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Liam Williams
 */
public class StatementResultSetResultProxyFactoryTest {

    @Test
    public void nonResultSetResultMethodsPassThrough() throws SQLException {
        PreparedStatement statement = mock(PreparedStatement.class);
        PreparedStatement proxy = new StatementResultSetResultProxyFactory().proxy(statement, PreparedStatement.class);

        proxy.executeUpdate();

        verify(statement).executeUpdate();
    }
}